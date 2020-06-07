package exe.weazy.reko

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import exe.weazy.reko.recognizer.LocalRecognizer
import exe.weazy.reko.recognizer.SkyBiometryRecognizer

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import java.io.File

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class RecognizersTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("exe.weazy.reko", appContext.packageName)
    }

    @Test
    fun skyBiometryRecognizing_isCorrect() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val uri = getUriByFileName(appContext, "test1.jpg")

        val recognizer = SkyBiometryRecognizer()
        val result = recognizer.recognize(uri)

        result.subscribe({
            Log.d("SUCCESS", it.toString())
            assert(it.emotions.isNotEmpty())
        }, {
            Log.d("FAILURE", "Failed to recognize any emotion")
            it.printStackTrace()
            assert(false)
        })
    }

    @Test
    fun localRecognizing_isCorrect() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val uri = getUriByFileName(appContext, "test2.jpg")

        val recognizer = LocalRecognizer()
        val result = recognizer.recognize(uri)

        result.subscribe({
            Log.d("SUCCESS", it.toString())
            assert(it.emotions.isNotEmpty())
        }, {
            Log.d("FAILURE", "Failed to recognize any emotion")
            it.printStackTrace()
            assert(false)
        })
    }

    private fun getUriByFileName(context: Context, filename: String): Uri {
        val inputStream = context.assets.open(filename)
        val readBytes = inputStream.readBytes()
        val file = File("${context.filesDir}/$filename")
        file.writeBytes(readBytes)
        return Uri.fromFile(file)
    }
}
