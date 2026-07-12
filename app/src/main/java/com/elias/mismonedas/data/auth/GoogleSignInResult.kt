package com.elias.mismonedas.data.auth

import android.net.Uri

data class GoogleSignInResult(
	val idToken: String,
	val subject: String?,
	val displayName: String?,
	val familyName: String?,
	val givenName: String?,
	val profilePictureUri: Uri?
)


