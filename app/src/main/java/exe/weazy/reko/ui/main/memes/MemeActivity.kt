package exe.weazy.reko.ui.main.memes

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import exe.weazy.reko.R
import exe.weazy.reko.model.Meme
import exe.weazy.reko.model.UserInfo
import exe.weazy.reko.state.ScreenState
import exe.weazy.reko.util.extensions.useViewModel
import exe.weazy.reko.util.handleTopInsets
import exe.weazy.reko.util.share
import exe.weazy.reko.util.values.MEME_ID
import kotlinx.android.synthetic.main.activity_meme.*
import org.ocpsoft.prettytime.PrettyTime

class MemeActivity : AppCompatActivity() {

    private lateinit var viewModel: MemeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_meme)

        rootViewMeme.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION

        handleTopInsets(memeToolbarLayout)

        viewModel = useViewModel(this, MemeViewModel::class.java)

        val memeId = intent.getLongExtra(MEME_ID, 0)

        viewModel.getMeme(memeId)
        viewModel.getUserInfo()

        initListeners()
        initObservers()
    }

    private fun initListeners() {
        closeButton.setOnClickListener {
            onBackPressed()
        }

        shareButton.setOnClickListener {
            val meme = viewModel.meme.value
            if (meme != null) {
                share(this, meme)
            }

        }

        favoriteButton.setOnClickListener {
            viewModel.likeMeme()
        }
    }

    private fun initObservers() {
        viewModel.state.observe(this, Observer {
            setState(it)
        })

        viewModel.meme.observe(this, Observer {
            showMemeInfo(it)
        })

        viewModel.userInfo.observe(this, Observer {
            showUserInfo(it)
        })
    }

    private fun setState(state: ScreenState) {
        when (state) {
            ScreenState.SUCCESS -> {
                loadingLayout.isVisible = false
                memeLayout.isVisible = true
                errorLayout.isVisible = false
            }
            ScreenState.ERROR, ScreenState.DEFAULT, ScreenState.EMPTY -> {
                loadingLayout.isVisible = false
                memeLayout.isVisible = false
                errorLayout.isVisible = true
            }
            ScreenState.LOADING -> {
                loadingLayout.isVisible = true
                memeLayout.isVisible = false
                errorLayout.isVisible = false
            }
        }
    }

    private fun showMemeInfo(meme: Meme) {
        titleTextView.text = meme.title
        descriptionTextView.text = meme.description?.replace("<br/>", "\n")
        dateTextView.text = PrettyTime().format(meme.createDate)

        val favoriteDrawable = if (meme.isFavorite) {
            R.drawable.ic_favorite_filled_blue_24dp
        } else {
            R.drawable.ic_favorite_border_white_24dp
        }

        favoriteButton.setImageDrawable(
            ContextCompat.getDrawable(this, favoriteDrawable)
        )

        Glide
            .with(this)
            .load(meme.photoUrl)
            .into(memeImageView)
    }

    private fun showUserInfo(userInfo: UserInfo) {
        usernameTextView.text = userInfo.username
    }
}
