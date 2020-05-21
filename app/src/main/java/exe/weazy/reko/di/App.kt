package exe.weazy.reko.di

import android.app.Application

class App : Application() {

    companion object {
        private lateinit var component : AppComponent

        fun getComponent() = component
    }

    override fun onCreate() {
        super.onCreate()

        component = DaggerAppComponent.builder()
            .networkModule(NetworkModule())
            .repositoryModule(RepositoryModule(applicationContext))
            .recognizerModule(RecognizerModule(applicationContext))
            .build()
    }
}