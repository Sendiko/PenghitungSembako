package id.my.sendiko.sembako.dashboard.presentation

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import id.my.sendiko.sembako.BuildConfig

class GoogleAuthUI {

    companion object {
        suspend fun silentSignIn(context: Context): Result<GetCredentialResponse> {
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
                // It's common to fail here if no user is signed in silently
                e.printStackTrace()
                Result.failure(e)
            }
        }

        suspend fun interactiveSignIn(context: Context): Result<GetCredentialResponse> {
            val googleIdOption = GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(false) // This will show the UI
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