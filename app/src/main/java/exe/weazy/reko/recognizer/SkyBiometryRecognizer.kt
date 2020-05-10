package exe.weazy.reko.recognizer

import exe.weazy.reko.model.Recognized
import io.reactivex.Observable
import java.util.*

class SkyBiometryRecognizer : Recognizer {

    override fun recognize(link: String): Observable<Recognized> {
        // TODO: make sky biometry recognizing
        return Observable.just(Recognized("", "", Date(), mapOf()))
    }
}