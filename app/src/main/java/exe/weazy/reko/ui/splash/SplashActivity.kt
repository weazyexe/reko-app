package exe.weazy.reko.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import exe.weazy.reko.R
import exe.weazy.reko.ui.login.LoginActivity
import exe.weazy.reko.ui.main.MainActivity
import exe.weazy.reko.ui.main.MainViewModel
import exe.weazy.reko.util.extensions.useViewModel
import exe.weazy.reko.util.values.EMPTY_STRING

class SplashActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        viewModel = useViewModel(this, MainViewModel::class.java)

        Handler().postDelayed({
            if (viewModel.getUserToken() != EMPTY_STRING) {
                openMainScreen()
            } else {
                openLoginScreen()
            }
        }, 300)
    }

    private fun openMainScreen() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun openLoginScreen() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}
