package exe.weazy.reko.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import exe.weazy.reko.R
import exe.weazy.reko.state.ScreenState
import exe.weazy.reko.ui.InsetsHelper
import exe.weazy.reko.ui.main.MainActivity
import exe.weazy.reko.util.extensions.showErrorSnackbar
import exe.weazy.reko.util.extensions.useViewModel
import kotlinx.android.synthetic.main.fragment_sign_up.*

class SignUpFragment : Fragment() {

    private lateinit var viewModel: AuthViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_sign_up, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = useViewModel(requireActivity(), AuthViewModel::class.java)

        InsetsHelper.handleBottom(false, signUpButton)

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

        passwordEditText.addTextChangedListener {
            if (!viewModel.validatePassword(it.toString())) {
                passwordEditTextLayout.error = getString(R.string.field_can_not_be_empty)
            } else {
                passwordEditTextLayout.error = ""
            }
        }

        confirmPasswordEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                viewModel.signUp(
                    login = loginEditText.text.toString(),
                    password = passwordEditText.text.toString(),
                    confirm = confirmPasswordEditText.text.toString()
                )
            }

            false
        }

        signUpButton.setOnClickListener {
            viewModel.signUp(
                login = loginEditText.text.toString(),
                password = passwordEditText.text.toString(),
                confirm = confirmPasswordEditText.text.toString()
            )
        }
    }

    private fun initObservers() {
        viewModel.signUpState.observe(requireActivity(), Observer {
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
                showErrorSnackbar(requireContext(), R.string.wrong_credentials, rootViewSignUp)
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
            signUpButton.text = ""
            loadingBar.isVisible = true
        } else {
            signUpButton.text = getString(R.string.sign_up)
            loadingBar.isVisible = false
        }
    }
}