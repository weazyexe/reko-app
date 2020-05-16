package exe.weazy.reko.ui.auth

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import exe.weazy.reko.R
import exe.weazy.reko.util.extensions.useViewModel
import kotlinx.android.synthetic.main.activity_auth.*
import kotlinx.android.synthetic.main.fragment_sign_in.*

class AuthActivity : AppCompatActivity() {

    private lateinit var signInFragment: SignInFragment
    private lateinit var signUpFragment: SignUpFragment

    private lateinit var active: Fragment

    private var startingPosition = 0
    private var newPosition = 0

    private lateinit var viewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        rootViewAuth.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION

        viewModel = useViewModel(this, AuthViewModel::class.java)
        viewModel.checkAccount()

        loadFragments()
    }

    override fun onStart() {
        super.onStart()
        initListeners()
    }

    override fun onBackPressed() {
        if (startingPosition == 1) {
            newPosition = 0
            changeFragment(signInFragment)
        } else {
            super.onBackPressed()
        }
    }

    private fun initListeners() {
        createAccountButton.setOnClickListener {
            newPosition = 1
            changeFragment(signUpFragment)
        }
    }

    private fun loadFragments() {
        signInFragment = SignInFragment()
        signUpFragment = SignUpFragment()

        supportFragmentManager.beginTransaction().add(R.id.rootViewAuth, signInFragment).commit()
        supportFragmentManager.beginTransaction().add(R.id.rootViewAuth, signUpFragment).hide(signUpFragment).commit()

        active = signInFragment
    }

    private fun changeFragment(fragment : Fragment) {
        if (startingPosition > newPosition) {
            supportFragmentManager.beginTransaction().setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_right).show(fragment).hide(active).commit()
        } else {
            supportFragmentManager.beginTransaction().setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_left).show(fragment).hide(active).commit()
        }

        startingPosition = newPosition
        active.onPause()
        active = fragment
        active.onResume()
    }
}
