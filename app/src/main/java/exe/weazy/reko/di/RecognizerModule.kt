package exe.weazy.reko.di

import android.content.Context
import dagger.Module
import dagger.Provides
import exe.weazy.reko.data.SettingsRepository
import exe.weazy.reko.model.RecognizerName
import exe.weazy.reko.recognizer.LocalRecognizer
import exe.weazy.reko.recognizer.Recognizer
import exe.weazy.reko.recognizer.SkyBiometryRecognizer
import exe.weazy.reko.recognizer.tensorflow.EmotionClassifier
import javax.inject.Singleton

@Module
class RecognizerModule(private val context: Context) {

    @Provides
    fun provideRecognizer(settingsRepository: SettingsRepository): Recognizer {
        return when(settingsRepository.getRecognizer()) {
            RecognizerName.SKY_BIOMETRY -> {
                SkyBiometryRecognizer()
            }
            RecognizerName.LOCAL -> {
                LocalRecognizer()
            }
        }
    }

    @Provides
    @Singleton
    fun provideEmotionClassifier(): EmotionClassifier {
        return EmotionClassifier(context)
    }
}