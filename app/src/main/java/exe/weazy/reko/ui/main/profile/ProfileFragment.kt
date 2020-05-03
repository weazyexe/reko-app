package exe.weazy.reko.ui.main.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import exe.weazy.reko.R
import exe.weazy.reko.model.Meme
import exe.weazy.reko.model.UserInfo
import exe.weazy.reko.recycler.MemesAdapter
import exe.weazy.reko.state.ScreenState
import exe.weazy.reko.ui.main.memes.MemeActivity
import exe.weazy.reko.ui.splash.SplashActivity
import exe.weazy.reko.util.extensions.useViewModel
import exe.weazy.reko.util.handleBottomInsets
import exe.weazy.reko.util.handleTopInsets
import exe.weazy.reko.util.share
import exe.weazy.reko.util.values.MEME_ID
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment: Fragment() {

    private lateinit var adapter: MemesAdapter

    private lateinit var viewModel: ProfileViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = useViewModel(this, ProfileViewModel::class.java)

        handleTopInsets(profileToolbarLayout)
        handleBottomInsets(contentLayout)

        viewModel.getLocalMemes()
        viewModel.getUserInfo()

        initListeners()
        initObservers()
    }

    override fun onResume() {
        super.onResume()
        viewModel.refreshMemes()
    }

    private fun initListeners() {
        profileToolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.about -> {

                }
                R.id.logout -> {
                    logoutDialog()
                }
            }
            false
        }
    }

    private fun initObservers() {
        viewModel.state.observe(viewLifecycleOwner, Observer {
            setState(it)
        })

        viewModel.memes.observe(viewLifecycleOwner, Observer {
           showMemes(it)
        })

        viewModel.userInfo.observe(viewLifecycleOwner, Observer {
            showUserInfo(it)
        })
    }

    private fun setState(state: ScreenState) {
        when (state) {
            ScreenState.LOADING -> {
                progressBar.isVisible = true
                errorTextView.isVisible = false
                emptyTextView.isVisible = false
                memesRecyclerView.isVisible = false
            }
            ScreenState.SUCCESS, ScreenState.DEFAULT -> {
                progressBar.isVisible = false
                errorTextView.isVisible = false
                emptyTextView.isVisible = false
                memesRecyclerView.isVisible = true
            }
            ScreenState.ERROR -> {
                progressBar.isVisible = false
                errorTextView.isVisible = true
                emptyTextView.isVisible = false
                memesRecyclerView.isVisible = false
            }
            ScreenState.EMPTY -> {
                progressBar.isVisible = false
                errorTextView.isVisible = false
                emptyTextView.isVisible = true
                memesRecyclerView.isVisible = false
            }
        }
    }

    private fun initAdapter(memes: List<Meme>) {
        adapter = MemesAdapter(
            memes,
            { viewModel.likeMeme(it) },
            { share(requireActivity(), it) },
            { openMemeActivity(it) }
        )

        memesRecyclerView.itemAnimator = null
        memesRecyclerView.adapter = adapter
        memesRecyclerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
    }

    private fun showUserInfo(userInfo: UserInfo) {
        usernameTextView.text = userInfo.username
        userDescriptionTextView.text = userInfo.description
    }

    private fun showMemes(memes: List<Meme>) {
        if (::adapter.isInitialized) {
            adapter.updateMemes(memes)
        } else {
            initAdapter(memes)
        }
    }

    private fun openMemeActivity(meme: Meme) {
        val intent = Intent(activity, MemeActivity::class.java)
        intent.putExtra(MEME_ID, meme.id)
        startActivity(intent)
    }

    private fun logoutDialog() {
        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setMessage(R.string.are_you_sure_for_logout)
            .setPositiveButton(R.string.logout) { _, _ ->
                viewModel.logout()
                openLoginActivity()
            }
            .setNegativeButton(R.string.cancel) { dialog, _ ->
                dialog.dismiss()
            }
            .create()

        dialog.show()
    }

    private fun openLoginActivity() {
        val intent = Intent(context, SplashActivity::class.java)
        startActivity(intent)
        activity?.finish()
    }
}