package exe.weazy.reko.data.firebase

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import exe.weazy.reko.data.firebase.raws.ApiKeysRaw
import exe.weazy.reko.util.generateId
import io.reactivex.Observable

object RxFirebase {

    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()

    fun signIn(email: String, password: String): Observable<FirebaseUser> = Observable.create { emitter ->
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    val user = it.result?.user
                    if (user != null) {
                        emitter.onNext(user)
                        emitter.onComplete()
                    } else {
                        emitter.onError(it.exception ?: Throwable("User is null"))
                    }
                } else {
                    emitter.onError(it.exception ?: Throwable("Request isn't successful"))
                }
            }
            .addOnCanceledListener {
                emitter.onError(Throwable("Request was cancelled"))
            }
    }

    fun signUp(email: String, password: String): Observable<FirebaseUser> = Observable.create { emitter ->
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    val user = it.result?.user
                    if (user != null) {
                        emitter.onNext(user)
                        emitter.onComplete()
                    } else {
                        emitter.onError(it.exception ?: Throwable("User is null"))
                    }
                } else {
                    emitter.onError(it.exception ?: Throwable("Request isn't successful"))
                }
            }
            .addOnCanceledListener {
                emitter.onError(Throwable("Request was cancelled"))
            }
    }

    fun getApiKeys(): Observable<ApiKeysRaw> = Observable.create { emitter ->
        firestore.document("api_keys/info").get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    emitter.onNext(it.result?.toObject(ApiKeysRaw::class.java) ?: throw Throwable("Result is null"))
                    emitter.onComplete()
                } else {
                    emitter.onError(it.exception ?: Throwable("Request isn't successful"))
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
                            emitter.onComplete()
                        } else {
                            emitter.onError(it.exception ?: Throwable("Request is successful but documents are null"))
                        }
                    } else {
                        emitter.onError(it.exception ?: Throwable("Request isn't successful"))
                    }
                }
                .addOnCanceledListener {
                    emitter.onError(Throwable("Request was cancelled"))
                }
        }
    }

    fun <T : Any> saveDocument(document: String, data: T): Observable<T> = Observable.create { emitter ->
        val uid = auth.currentUser?.uid
        if (uid == null) {
            emitter.onError(Throwable("User id is null"))
        } else {
            firestore.document("$uid/$document").set(data)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        emitter.onNext(data)
                        emitter.onComplete()
                    } else {
                        emitter.onError(it.exception ?: Throwable("Document saving request isn't successful"))
                    }
                }
                .addOnCanceledListener {
                    emitter.onError(Throwable("Document saving request was canceled"))
                }
        }
    }

    fun uploadImage(imageUri: Uri) : Observable<String> = Observable.create { emitter ->
        val uid = auth.currentUser?.uid
        if (uid == null) {
            emitter.onError(Throwable("User id is null"))
        } else {
            val filename = generateId()
            val ref = storage.getReference("$uid/$filename.jpeg")
            ref.putFile(imageUri)
                .addOnCompleteListener { loadTask ->
                    if (loadTask.isSuccessful) {
                        ref.downloadUrl
                            .addOnCompleteListener {
                                if (it.isSuccessful) {
                                    emitter.onNext(it.result.toString())
                                    emitter.onComplete()
                                } else {
                                    emitter.onError(it.exception ?: Throwable("Download URL request isn't successful"))
                                }
                            }
                            .addOnCanceledListener {
                                emitter.onError(Throwable("Download URL was cancelled"))
                            }
                    } else {
                        emitter.onError(loadTask.exception ?: Throwable("Put file request isn't successful"))
                    }
                }
                .addOnCanceledListener {
                    emitter.onError(Throwable("Put file request was cancelled"))
                }
        }
    }
}