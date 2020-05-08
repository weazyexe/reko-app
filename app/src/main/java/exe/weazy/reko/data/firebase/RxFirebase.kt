package exe.weazy.reko.data.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import io.reactivex.Observable

object RxFirebase {

    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

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

    fun getCollection(collection: String): Observable<List<DocumentSnapshot>> = Observable.create { emitter ->
        val uid = auth.currentUser?.uid
        if (uid == null) {
            emitter.onError(Throwable("User id is null"))
        } else {
            firestore.collection("$uid/$collection").get()
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        val docs = it.result?.documents
                        if (docs != null) {
                            emitter.onNext(docs)
                        } else {
                            emitter.onError(Throwable("Request is successful but documents are null"))
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
}