{ pkgs, ... }: {
  # Usamos el canal estable que venía en tu plantilla
  channel = "stable-24.05"; 

  # Instalamos los componentes críticos para Android nativo
  packages = [
    pkgs.jdk17
    pkgs.gradle
    pkgs.kotlin
  ];

  # Variables de entorno (lo dejamos vacío por ahora)
  env = {};

  idx = {
    # Extensiones de VS Code para tener autocompletado de Kotlin
    extensions = [
      "mathiasfrohlich.Kotlin"
      "fwcd.kotlin"
    ];

    # Configuración crítica: Habilitamos el emulador de Android
    previews = {
      enable = true;
      previews = {
        android = {
          manager = "android";
          # Compila, instala y lanza tu aplicación específica
          command = ["sh" "-c" "./gradlew :app:installDebug && adb shell am start -n com.example.finanx/.MainActivity"];
        };
      };
    };

    # Hooks del ciclo de vida del espacio de trabajo
    workspace = {
      onCreate = {};
      onStart = {};
    };
  };
}