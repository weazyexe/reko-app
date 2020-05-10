package exe.weazy.reko.recognizer

import android.net.Uri
import exe.weazy.reko.model.Recognized
import io.reactivex.Observable

interface Recognizer {
    fun recognize(link: String): Observable<Recognized>
}