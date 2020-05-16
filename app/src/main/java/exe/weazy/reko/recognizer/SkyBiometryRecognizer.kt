package exe.weazy.reko.recognizer

import android.net.Uri
import androidx.core.net.toFile
import exe.weazy.reko.data.network.NetworkService
import exe.weazy.reko.di.App
import exe.weazy.reko.model.Recognized
import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject


class SkyBiometryRecognizer : Recognizer {

    @Inject
    lateinit var networkService: NetworkService

    init {
        App.getComponent().inject(this)
    }

    override fun recognize(imageUri: Uri): Observable<Recognized> {
        val file = imageUri.toFile()

        val filePart = MultipartBody.Part.createFormData(
            "urls",
            file.name,
            RequestBody.create(null, file)
        )
        /*val requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file)
        val body = MultipartBody.Part.createFormData("urls", file.name, requestFile);*/

        return networkService.recognize(filePart)
            .map { it.convert() }
    }
}