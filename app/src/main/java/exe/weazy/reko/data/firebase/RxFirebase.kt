package exe.weazy.reko.data.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import io.reactivex.Observable

object RxFirebase {

    private val auth = FirebaseAuth.getInstance()

    fun signIn(email: String, password: String): Observable<FirebaseUser> = Observable.create { emitter ->
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    val user = it.result?.user
                    if (user != null) {
                        emitter.onNext(user)
                    } else {
                        emitter.onError(Throwable("User is null"))
                    }
                } else {
                    emitter.onError(Throwable("Request isn't successful"))
                }
            }
            .addOnCanceledListener {
                emitter.onError(Throwable("Request was cancelled"))
            }
    }
}