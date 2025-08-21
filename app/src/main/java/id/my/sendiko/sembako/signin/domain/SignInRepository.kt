package id.my.sendiko.sembako.signin.domain

import com.google.firebase.auth.FirebaseUser
import id.my.sendiko.sembako.core.domain.User

interface SignInRepository {
    suspend fun signInWithGoogle(idToken: String): Result<FirebaseUser?>
    suspend fun signInAnonymously(): Result<FirebaseUser?>
    suspend fun saveUserToRemote(user: User): Result<User>
    suspend fun saveUserToLocal(user: User): Result<Boolean>
}