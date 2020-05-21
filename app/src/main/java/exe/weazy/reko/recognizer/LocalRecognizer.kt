package exe.weazy.reko.recognizer

import android.graphics.BitmapFactory
import android.net.Uri
import exe.weazy.reko.data.firebase.RxFirebase
import exe.weazy.reko.di.App
import exe.weazy.reko.model.Recognized
import exe.weazy.reko.model.RecognizerName
import exe.weazy.reko.recognizer.tensorflow.EmotionClassifier
import exe.weazy.reko.util.generateId
import io.reactivex.Observable
import java.util.*
import javax.inject.Inject

class LocalRecognizer : Recognizer {

    @Inject
    lateinit var emotionClassifier: EmotionClassifier

    init {
        App.getComponent().inject(this)
    }

    override fun recognize(imageUri: Uri): Observable<Recognized> {
        val bitmap = BitmapFactory.decodeFile(imageUri.path)

        return RxFirebase.uploadImage(imageUri)
            .flatMap { link -> Observable.create<Recognized> { emitter ->
                val emotion = emotionClassifier.classify(bitmap)
                if (emotion != null) {
                    val recognized = Recognized(
                        id = generateId(7),
                        image = link,
                        date = Date(),
                        recognizer = RecognizerName.LOCAL,
                        emotions = emotion
                    )
                    emitter.onNext(recognized)
                    emitter.onComplete()
                } else {
                    emitter.onError(Throwable("Faces not found or emotion not recognized"))
                }
            } }
    }

}