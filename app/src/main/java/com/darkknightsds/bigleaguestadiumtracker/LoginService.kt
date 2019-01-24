package com.darkknightsds.bigleaguestadiumtracker

import android.content.Context
import com.google.android.gms.auth.api.signin.GoogleSignInOptions

class LoginService(context: Context) {
    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(context.getString(R.string.default_web_client_id))
        .requestEmail()
        .build()

}