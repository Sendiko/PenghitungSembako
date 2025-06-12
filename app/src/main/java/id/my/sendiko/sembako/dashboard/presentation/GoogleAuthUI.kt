package id.my.sendiko.sembako.dashboard.presentation

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import id.my.sendiko.sembako.BuildConfig

class GoogleAuthUI {

    companion object {
        suspend fun signIn(context: Context): Result<GetCredentialResponse> {
            val googleIdOption = GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(false)
                .setServerClientId(BuildConfig.API_KEY)
                .build()

            val request = GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build()

            return try {
                val credentialManager = CredentialManager.Companion.create(context)
                val result = credentialManager.getCredential(context, request)
                Result.success(result)
            } catch (e: Exception) {
                e.printStackTrace()
                Result.failure(e)
            }
        }
    }

}