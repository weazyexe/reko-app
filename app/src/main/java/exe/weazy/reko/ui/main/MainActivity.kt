package exe.weazy.reko.ui.main

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import exe.weazy.reko.R
import exe.weazy.reko.ui.main.feed.FeedFragment
import exe.weazy.reko.ui.main.recognize.RecognizeFragment
import exe.weazy.reko.ui.main.settings.SettingsFragment
import exe.weazy.reko.util.handleBottomInsets
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var feedFragment: FeedFragment
    private lateinit var recognizeFragment: RecognizeFragment
    private lateinit var settingsFragment: SettingsFragment

    private var active = Fragment()

    private var newPosition = 0
    private var startingPosition = 0

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.feedButton -> {
                newPosition = 0
                if (startingPosition != newPosition) {
                    changeFragment(feedFragment)
                }
                return@OnNavigationItemSelectedListener true
            }
            R.id.recognizeButton -> {
                newPosition = 1
                if (startingPosition != newPosition) {
                    changeFragment(recognizeFragment)
                }
                return@OnNavigationItemSelectedListener true
            }
            R.id.settingsButton -> {
                newPosition = 2
                if (startingPosition != newPosition) {
                    changeFragment(settingsFragment)
                }
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rootViewMain.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION

        handleBottomInsets(bottomNav)

        bottomNav.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        loadFragments()
    }

    private fun loadFragments() {
        feedFragment = FeedFragment()
        recognizeFragment = RecognizeFragment()
        settingsFragment = SettingsFragment()

        supportFragmentManager.beginTransaction().add(R.id.fragmentLayout, feedFragment).commit()
        supportFragmentManager.beginTransaction().add(R.id.fragmentLayout, recognizeFragment).hide(recognizeFragment).commit()
        supportFragmentManager.beginTransaction().add(R.id.fragmentLayout, settingsFragment).hide(settingsFragment).commit()

        bottomNav.selectedItemId = R.id.feedButton
        active = feedFragment
    }

    private fun changeFragment(fragment : Fragment) {
        supportFragmentManager.beginTransaction().show(fragment).hide(active).commit()
        startingPosition = newPosition
        active.onPause()
        active = fragment
        active.onResume()
    }
}
