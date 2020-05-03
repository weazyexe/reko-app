package exe.weazy.reko.ui.main.memes

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import exe.weazy.reko.R
import exe.weazy.reko.model.Meme
import exe.weazy.reko.recycler.MemesAdapter
import exe.weazy.reko.state.ScreenState
import exe.weazy.reko.ui.main.MainViewModel
import exe.weazy.reko.util.extensions.showErrorSnackbar
import exe.weazy.reko.util.extensions.useViewModel
import exe.weazy.reko.util.handleBottomInsets
import exe.weazy.reko.util.handleTopInsets
import exe.weazy.reko.util.share
import exe.weazy.reko.util.values.MEME_ID
import kotlinx.android.synthetic.main.fragment_memes.*


class MemesFragment : Fragment() {

    private lateinit var viewModel: MainViewModel
    private lateinit var adapter: MemesAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_memes, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = useViewModel(requireActivity(), MainViewModel::class.java)
        viewModel.fetchMemes()

        handleTopInsets(toolbarLayout)
        handleBottomInsets(recyclerLayout)

        initObservers()
        initListeners()
    }

    override fun onResume() {
        super.onResume()
        viewModel.refreshMemes()
    }

    private fun initListeners() {
        swipeRefreshMemesLayout.setOnRefreshListener {
            viewModel.fetchMemes()
            swipeRefreshMemesLayout.isRefreshing = false
        }
    }

    private fun initObservers() {
        viewModel.memesState.observe(viewLifecycleOwner, Observer {
            setState(it)
        })

        viewModel.memes.observe(viewLifecycleOwner, Observer { memes ->
            if (::adapter.isInitialized) {
                adapter.updateMemes(memes)
            } else {
                initAdapter(memes)
            }
        })
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
        memesRecyclerView.layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
    }

    private fun setState(state: ScreenState) {
        when (state) {
            ScreenState.LOADING -> {
                if (viewModel.memes.value.isNullOrEmpty()) {
                    noContentProgressBar.isVisible = true
                    contentProgressBar.isVisible = false
                    memesRecyclerView.isVisible = false
                } else {
                    noContentProgressBar.isVisible = false
                    contentProgressBar.isVisible = true
                    memesRecyclerView.isVisible = true
                }

                errorTextView.isVisible = false
                emptyTextView.isVisible = false
            }

            ScreenState.ERROR -> {
                noContentProgressBar.isVisible = false
                contentProgressBar.isVisible = false
                emptyTextView.isVisible = false

                if (viewModel.memes.value.isNullOrEmpty()) {
                    errorTextView.isVisible = true
                    memesRecyclerView.isVisible = false
                } else {
                    errorTextView.isVisible = false
                    memesRecyclerView.isVisible = true
                }

                val rootView = activity?.findViewById<ViewGroup>(R.id.rootViewMain)
                if (rootView != null) {
                    activity?.showErrorSnackbar(R.string.memes_error, rootView)
                }

            }

            ScreenState.SUCCESS, ScreenState.DEFAULT -> {
                noContentProgressBar.isVisible = false
                contentProgressBar.isVisible = false
                memesRecyclerView.isVisible = true
                errorTextView.isVisible = false
                emptyTextView.isVisible = false
            }

            ScreenState.EMPTY -> {
                noContentProgressBar.isVisible = false
                contentProgressBar.isVisible = false
                memesRecyclerView.isVisible = false
                errorTextView.isVisible = false
                emptyTextView.isVisible = true
            }
        }
    }

    private fun openMemeActivity(meme: Meme) {
        val intent = Intent(activity, MemeActivity::class.java)
        intent.putExtra(MEME_ID, meme.id)
        startActivity(intent)
    }
}