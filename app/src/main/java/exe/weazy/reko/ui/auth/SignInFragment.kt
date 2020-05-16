package exe.weazy.reko.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import exe.weazy.reko.R
import exe.weazy.reko.state.ScreenState
import exe.weazy.reko.ui.main.MainActivity
import exe.weazy.reko.util.extensions.showErrorSnackbar
import exe.weazy.reko.util.extensions.useViewModel
import kotlinx.android.synthetic.main.fragment_sign_in.*

class SignInFragment : Fragment() {

    private lateinit var viewModel: AuthViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_sign_in, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = useViewModel(requireActivity(), AuthViewModel::class.java)

        initListeners()
        initObservers()
    }

    private fun initListeners() {
        loginEditText.addTextChangedListener {
            if (!viewModel.validateLogin(it.toString())) {
                loginEditTextLayout.error = getString(R.string.field_can_not_be_empty)
            } else {
                loginEditTextLayout.error = ""
            }
        }

        passwordEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                viewModel.signIn(
                    login = loginEditText.text.toString(),
                    password = passwordEditText.text.toString()
                )
            }

            false
        }

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
        viewModel.signInState.observe(requireActivity(), Observer {
            setState(it)
        })
    }

    private fun setState(state: ScreenState) {
        when(state) {
            ScreenState.DEFAULT, ScreenState.EMPTY -> {
                linearLayout.isVisible = false
                buttonsLayout.isVisible = false
                makeButtonLoading(false)
            }
            ScreenState.LOADING -> {
                linearLayout.isVisible = true
                buttonsLayout.isVisible = true
                makeButtonLoading(true)
            }
            ScreenState.ERROR -> {
                linearLayout.isVisible = true
                buttonsLayout.isVisible = true
                makeButtonLoading(false)
                showErrorSnackbar(requireContext(), viewModel.errorMessage ?: getString(R.string.wrong_credentials), rootViewSignIn)
            }
            ScreenState.SUCCESS -> {
                openMainScreen()
            }
        }
    }

    private fun openMainScreen() {
        val intent = Intent(requireActivity(), MainActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
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