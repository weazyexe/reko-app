package exe.weazy.reko.ui.login

import android.content.Intent
import android.os.Bundle
import android.telephony.PhoneNumberFormattingTextWatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import exe.weazy.reko.R
import exe.weazy.reko.state.ScreenState
import exe.weazy.reko.ui.main.MainActivity
import exe.weazy.reko.util.extensions.showErrorSnackbar
import exe.weazy.reko.util.extensions.useViewModel
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        viewModel = useViewModel(this, LoginViewModel::class.java)

        initObservers()
        initListeners()
    }

    private fun initListeners() {
        passwordEditText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                passwordEditTextLayout.helperText = getString(R.string.password_length)
            } else {
                passwordEditTextLayout.helperText = ""
            }
        }

        loginEditText.addTextChangedListener(object : PhoneNumberFormattingTextWatcher() {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                super.onTextChanged(s, start, before, count)
                if (!viewModel.validateLogin(s.toString())) {
                    loginEditTextLayout.error = getString(R.string.field_can_not_be_empty)
                } else {
                    loginEditTextLayout.error = ""
                }
            }
        })

        passwordEditText.addTextChangedListener {
            if (!viewModel.validatePassword(it.toString())) {
                passwordEditTextLayout.error = getString(R.string.field_can_not_be_empty)
            } else {
                passwordEditTextLayout.error = ""
            }
        }

        signInButton.setOnClickListener {
            viewModel.signIn(
                login = loginEditText.text.toString(),
                password = passwordEditText.text.toString()
            )
        }
    }

    private fun initObservers() {
        viewModel.state.observe(this, Observer {
            setState(it)
        })
    }

    private fun setState(state: ScreenState) {
        when(state) {
            ScreenState.DEFAULT, ScreenState.EMPTY -> {
                makeButtonLoading(false)
            }
            ScreenState.LOADING -> {
                makeButtonLoading(true)
            }
            ScreenState.ERROR -> {
                makeButtonLoading(false)
                showErrorSnackbar(R.string.wrong_credentials, rootViewLogin)
            }
            ScreenState.SUCCESS -> {
                openMainScreen()
            }
        }
    }

    private fun openMainScreen() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun makeButtonLoading(isLoading: Boolean) {
        if (isLoading) {
            signInButton.text = ""
            loadingBar.isVisible = true
        } else {
            signInButton.text = getString(R.string.sign_in)
            loadingBar.isVisible = false
        }
    }
}
