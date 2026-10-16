[CmdletBinding()]
param(
    [string]$SpecUrl = "http://localhost:8080/v3/api-docs.yaml"
)

Set-StrictMode -Version Latest
$ErrorActionPreference = "Stop"

$repoRoot     = Split-Path -Parent $PSScriptRoot
$specPath     = Join-Path $repoRoot "openapi.yaml"
$clientPath   = Join-Path $repoRoot "kotlin-client"
$suffix       = [guid]::NewGuid().ToString("N")
$downloadPath = Join-Path $repoRoot ".openapi-download-$suffix.yaml"
$specBackup   = Join-Path $repoRoot ".openapi-backup-$suffix.yaml"
$clientBackup = Join-Path $repoRoot ".kotlin-client-backup-$suffix"

$specExisted   = Test-Path -LiteralPath $specPath
$clientExisted = Test-Path -LiteralPath $clientPath
$specBackedUp  = $false
$clientBackedUp = $false
$locationPushed = $false

try {
    # 1. Download the contract from the running backend.
    Invoke-WebRequest -Uri $SpecUrl -OutFile $downloadPath -TimeoutSec 30 -UseBasicParsing

    $specText = Get-Content -LiteralPath $downloadPath -Raw
    if ($specText -notmatch "(?m)^openapi:\s*3\.") {
        throw "The response from '$SpecUrl' is not an OpenAPI 3 YAML document."
    }

    # 2. Back up the current spec and generated client for rollback.
    if ($specExisted) {
        Move-Item -LiteralPath $specPath -Destination $specBackup
        $specBackedUp = $true
    }
    if ($clientExisted) {
        Copy-Item -LiteralPath $clientPath -Destination $clientBackup -Recurse
        $clientBackedUp = $true
    }

    Move-Item -LiteralPath $downloadPath -Destination $specPath

    # 3. Drop the previously generated sources so models/APIs that disappeared
    #    from the contract do not linger as stale files (the generator only
    #    overwrites; it does not prune). Protected files live outside src/.
    $generatedSrc = Join-Path $clientPath "src"
    if (Test-Path -LiteralPath $generatedSrc) {
        Remove-Item -LiteralPath $generatedSrc -Recurse -Force
    }

    # Generate in place. Protected files (build.gradle, wrapper, README) are
    #    skipped via kotlin-client/.openapi-generator-ignore.
    #    The generator version is pinned in openapitools.json.
    Push-Location $repoRoot
    $locationPushed = $true

    $npx = Get-Command "npx" -ErrorAction SilentlyContinue
    if ($null -eq $npx) {
        throw "'npx' was not found on PATH. Install Node.js to run the pinned OpenAPI Generator wrapper."
    }

    # Generator version is pinned in openapitools.json; the wrapper downloads it on first use.
    $generatorArguments = @(
        "generate",
        "-i", "./openapi.yaml",
        "-g", "kotlin",
        "-o", "./kotlin-client",
        "--library=jvm-retrofit2",
        "--type-mappings", "DateTime=LocalDateTime",
        "--additional-properties=useCoroutines=true,serializableModel=true,enumPropertyNaming=UPPERCASE"
    )
    & $npx.Source --yes "@openapitools/openapi-generator-cli" @generatorArguments
    if ($LASTEXITCODE -ne 0) {
        throw "OpenAPI Generator failed with exit code $LASTEXITCODE."
    }


    # 4. Sanity-check the output.
    $apisDir   = Join-Path $clientPath "src/main/kotlin/org/openapitools/client/apis"
    $modelsDir = Join-Path $clientPath "src/main/kotlin/org/openapitools/client/models"
    $generatedApis   = @(Get-ChildItem -LiteralPath $apisDir   -Filter "*.kt" -ErrorAction SilentlyContinue)
    $generatedModels = @(Get-ChildItem -LiteralPath $modelsDir -Filter "*.kt" -ErrorAction SilentlyContinue)
    if ($generatedApis.Count -eq 0 -or $generatedModels.Count -eq 0) {
        throw "Generation completed without producing both API and model sources."
    }

    if ($specBackedUp)   { Remove-Item -LiteralPath $specBackup   -Force }
    if ($clientBackedUp) { Remove-Item -LiteralPath $clientBackup -Recurse -Force }

    Write-Host "Synchronized OpenAPI from $SpecUrl"
    Write-Host "Generated $($generatedApis.Count) API files and $($generatedModels.Count) model files in kotlin-client."
} catch {
    if ($locationPushed) {
        Pop-Location
        $locationPushed = $false
    }

    if ($clientBackedUp -and (Test-Path -LiteralPath $clientBackup)) {
        if (Test-Path -LiteralPath $clientPath) {
            Remove-Item -LiteralPath $clientPath -Recurse -Force
        }
        Move-Item -LiteralPath $clientBackup -Destination $clientPath
    }

    if ($specBackedUp -and (Test-Path -LiteralPath $specBackup)) {
        if (Test-Path -LiteralPath $specPath) {
            Remove-Item -LiteralPath $specPath -Force
        }
        Move-Item -LiteralPath $specBackup -Destination $specPath
    }

    throw
} finally {
    if ($locationPushed) {
        Pop-Location
    }
    if (Test-Path -LiteralPath $downloadPath)  { Remove-Item -LiteralPath $downloadPath -Force }
    if ($specBackedUp -and (Test-Path -LiteralPath $specBackup))   { Remove-Item -LiteralPath $specBackup   -Force }
    if ($clientBackedUp -and (Test-Path -LiteralPath $clientBackup)) { Remove-Item -LiteralPath $clientBackup -Recurse -Force }
}
