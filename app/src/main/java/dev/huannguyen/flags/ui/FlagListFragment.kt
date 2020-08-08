package dev.huannguyen.flags.ui

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import dev.huannguyen.flags.R
import dev.huannguyen.flags.domain.Flag
import kotlinx.android.synthetic.main.list_fragment.errorMessage
import kotlinx.android.synthetic.main.list_fragment.flagList
import kotlinx.android.synthetic.main.list_fragment.progressBar
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class FlagListFragment : Fragment() {

    private val viewModel by lazy {
        ViewModelProvider(this, FlagListViewModelFactory()).get(FlagListViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.list_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupFlagList()
        subscribeToData(view)

        // If no data is held by view model, we need to fetch data from network
        if (viewModel.flags.value == null) {
            viewModel.getFlags()
        } else {
            // Used for shared element transition. We need to postpone the transition until the data is loaded
            // (if not the view would not be ready for a shared element transition to happen).
            postponeEnterTransition()
        }
    }

    private fun setupFlagList() {
        flagList.layoutManager = GridLayoutManager(
            context,
            if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) 2 else 3
        )

        flagList.adapter = FlagAdapter().also {
            it.clicks
                .onEach { (view, data) -> showDetails(view, data) }
                .launchIn(viewModel.viewModelScope)
        }
    }

    private fun showDetails(sharedElement: View, data: Flag) {
        val args = Bundle().apply {
            putParcelable(FlagDetailsFragment.KEY_FLAG, data)
            putString(FlagDetailsFragment.KEY_TRANSITION_NAME, sharedElement.transitionName)
        }

        val extras = FragmentNavigatorExtras(sharedElement to sharedElement.transitionName)

        findNavController().navigate(R.id.action_list_to_details, args, null, extras)
    }

    private fun subscribeToData(view: View) {
        viewModel.flags.observe(viewLifecycleOwner, Observer { state ->
            when (state) {
                is UiState.Success -> {
                    errorMessage.hide()
                    progressBar.hide()
                    flagList.show()
                    (flagList.adapter as FlagAdapter).set(state.data)
                }

                is UiState.Failure -> {
                    progressBar.hide()
                    flagList.hide()
                    errorMessage.show()
                    errorMessage.text = getString(state.message)
                }

                is UiState.InProgress -> {
                    errorMessage.hide()
                    progressBar.show()
                }
            }

            // Wait until the view is about to draw to start the postponed shared element transition
            (view.parent as? ViewGroup)?.doOnPreDraw {
                startPostponedEnterTransition()
            }
        })
    }
}

fun View.hide() {
    visibility = View.GONE
}

fun View.show() {
    visibility = View.VISIBLE
}
