package exe.weazy.reko.di

import dagger.Module
import dagger.Provides
import exe.weazy.reko.data.SettingsRepository
import exe.weazy.reko.model.RecognizerName
import exe.weazy.reko.recognizer.Recognizer
import exe.weazy.reko.recognizer.SkyBiometryRecognizer

@Module
class RecognizerModule {

    @Provides
    fun provideRecognizer(settingsRepository: SettingsRepository): Recognizer {
        return when(settingsRepository.getRecognizer()) {
            RecognizerName.SKY_BIOMETRY -> {
                SkyBiometryRecognizer()
            }
            RecognizerName.LOCAL -> {
                // TODO: change to local
                SkyBiometryRecognizer()
            }
        }
    }
}