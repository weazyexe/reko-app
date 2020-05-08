package exe.weazy.reko.data

import exe.weazy.reko.data.firebase.raws.RecognizedRaw
import exe.weazy.reko.data.firebase.RxFirebase
import exe.weazy.reko.model.Recognized
import io.reactivex.Observable

class RecognizedRepository {

    fun fetch(): Observable<List<Recognized>> = RxFirebase.getCollection("info/recognized")
        .map { response -> response.map { it.toObject(RecognizedRaw::class.java) } }
        .map { response -> response.filterNotNull() }
        .map { response -> response.map { it.convert() } }
}