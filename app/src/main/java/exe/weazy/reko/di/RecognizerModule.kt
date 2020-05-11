package exe.weazy.reko.di

import dagger.Module
import dagger.Provides
import exe.weazy.reko.data.ApiKeyRepository
import exe.weazy.reko.data.SettingsRepository
import exe.weazy.reko.recognizer.Recognizer
import exe.weazy.reko.recognizer.SkyBiometryRecognizer
import exe.weazy.reko.util.values.LOCAL_RECOGNIZER
import exe.weazy.reko.util.values.SKY_BIOMETRY_RECOGNIZER

@Module
class RecognizerModule {

    @Provides
    fun provideRecognizer(settingsRepository: SettingsRepository): Recognizer {
        return when(settingsRepository.getRecognizer()) {
            SKY_BIOMETRY_RECOGNIZER -> {
                SkyBiometryRecognizer()
            }
            LOCAL_RECOGNIZER -> {
                // TODO: change to local
                SkyBiometryRecognizer()
            }
            else -> {
                // TODO: change to local
                SkyBiometryRecognizer()
            }
        }
    }
}