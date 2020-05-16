package exe.weazy.reko.ui.main

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.leinardi.android.speeddial.SpeedDialView
import exe.weazy.reko.R
import exe.weazy.reko.model.Recognized
import exe.weazy.reko.recycler.RecognizedAdapter
import exe.weazy.reko.state.ScreenState
import exe.weazy.reko.util.extensions.useViewModel
import exe.weazy.reko.util.handleBottomInsets
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel
    private lateinit var adapter: RecognizedAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rootViewMain.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION

        speedDialView.inflate(R.menu.main_menu)
        handleBottomInsets(speedDialView)

        viewModel = useViewModel(this, MainViewModel::class.java)
        viewModel.fetch()

        initObservers()
        initListeners()
    }

    private fun initObservers() {
        viewModel.state.observe(this, Observer {
            setState(it)
        })

        viewModel.recognized.observe(this, Observer {
            showRecognized(it)
        })
    }

    private fun initListeners() {
        swipeRefreshLayout.setOnRefreshListener {
            viewModel.fetch()
            swipeRefreshLayout.isRefreshing = false
        }

        speedDialView.setOnActionSelectedListener(SpeedDialView.OnActionSelectedListener { actionItem ->
            when (actionItem.id) {
                R.id.fromGalleryButton -> {
                    Toast.makeText(this, "From gallery", Toast.LENGTH_SHORT).show()
                    speedDialView.close()
                    return@OnActionSelectedListener true
                }
                R.id.photoButton -> {
                    Toast.makeText(this, "Photo", Toast.LENGTH_SHORT).show()
                    speedDialView.close()
                    return@OnActionSelectedListener true
                }
            }
            false
        })
    }

    @SuppressLint("SetTextI18n")
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

                errorLayout.text = getString(R.string.loading_error) + "\n\n${viewModel.errorMessage ?: ""}"
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
                Toast.makeText(this, it.id, Toast.LENGTH_SHORT).show()
            }

            recognizedRecyclerView.adapter = adapter
            recognizedRecyclerView.layoutManager = LinearLayoutManager(this)
        }
    }
}
