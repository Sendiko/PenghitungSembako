package com.github.sendiko.penghitungsembako.user.presentation

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import com.github.sendiko.penghitungsembako.BuildConfig
import com.google.android.libraries.identity.googleid.GetGoogleIdOption

class GoogleAuthUI {

    companion object {
        suspend fun signIn(context: Context): Result<GetCredentialResponse> {
            val googleIdOption = GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(true)
                .setServerClientId(BuildConfig.API_KEY)
                .build()

            val request = GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build()

            return try {
                val credentialManager = CredentialManager.create(context)
                val result = credentialManager.getCredential(context, request)
                Result.success(result)
            } catch (e: Exception) {
                e.printStackTrace()
                Result.failure(e)
            }
        }
    }

}