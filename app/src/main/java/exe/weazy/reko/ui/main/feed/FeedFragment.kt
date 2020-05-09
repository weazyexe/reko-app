package exe.weazy.reko.ui.main.feed

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import exe.weazy.reko.R
import exe.weazy.reko.model.Recognized
import exe.weazy.reko.recycler.RecognizedAdapter
import exe.weazy.reko.state.ScreenState
import exe.weazy.reko.util.extensions.useViewModel
import exe.weazy.reko.util.handleTopInsets
import kotlinx.android.synthetic.main.fragment_feed.*

class FeedFragment : Fragment() {

    private lateinit var viewModel: FeedViewModel
    private lateinit var adapter: RecognizedAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_feed, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        handleTopInsets(profileToolbarLayout)

        viewModel = useViewModel(this, FeedViewModel::class.java)
        viewModel.fetch()

        initObservers()
        initListeners()
    }

    private fun initObservers() {
        viewModel.state.observe(viewLifecycleOwner, Observer {
            setState(it)
        })

        viewModel.recognized.observe(viewLifecycleOwner, Observer {
            showRecognized(it)
        })
    }

    private fun initListeners() {
        swipeRefreshLayout.setOnRefreshListener {
            viewModel.fetch()
            swipeRefreshLayout.isRefreshing = false
        }
    }

    private fun setState(state: ScreenState) {
        when (state) {
            ScreenState.ERROR -> {
                errorLayout.isVisible = true
                progressLayout.isVisible = false
                recognizedRecyclerView.isVisible = false
                emptyLayout.isVisible = false
            }
            ScreenState.SUCCESS -> {
                errorLayout.isVisible = false
                progressLayout.isVisible = false
                recognizedRecyclerView.isVisible = true
                emptyLayout.isVisible = false
            }
            ScreenState.EMPTY -> {
                errorLayout.isVisible = false
                progressLayout.isVisible = false
                recognizedRecyclerView.isVisible = false
                emptyLayout.isVisible = true
            }
            ScreenState.LOADING -> {
                errorLayout.isVisible = false
                progressLayout.isVisible = true
                recognizedRecyclerView.isVisible = false
                emptyLayout.isVisible = false
            }
            ScreenState.DEFAULT -> {
                errorLayout.isVisible = false
                progressLayout.isVisible = false
                recognizedRecyclerView.isVisible = false
                emptyLayout.isVisible = false
            }
        }
    }

    private fun showRecognized(recognized: List<Recognized>) {
        if (::adapter.isInitialized) {
            adapter.update(recognized)
        } else {
            adapter = RecognizedAdapter(recognized.toMutableList()) {
                Toast.makeText(requireContext(), it.id, Toast.LENGTH_SHORT).show()
            }

            recognizedRecyclerView.adapter = adapter
            recognizedRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        }
    }
}