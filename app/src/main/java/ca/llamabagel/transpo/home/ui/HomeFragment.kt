/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.home.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import ca.llamabagel.transpo.R
import ca.llamabagel.transpo.di.injector
import ca.llamabagel.transpo.search.ui.SearchActivity
import ca.llamabagel.transpo.utils.startActivityForResult
import com.google.android.material.appbar.MaterialToolbar

class HomeFragment : Fragment() {

    companion object {
        fun newInstance() = HomeFragment()
    }

    private val viewModel: HomeViewModel by viewModels { injector.homeViewModelFactory() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.home_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        requireView().findViewById<MaterialToolbar>(R.id.toolbar).apply {
            inflateMenu(R.menu.main)
            setOnMenuItemClickListener(::onMenuItemSelected)
        }

        requireView().findViewById<EditText>(R.id.search_bar).setOnClickListener {
            requireActivity().startActivityForResult<SearchActivity>(
                requireActivity(),
                SearchActivity.SEARCH_REQUEST_CODE
            )
        }

        val adapter = LiveUpdatesAdapter()
        requireView().findViewById<RecyclerView>(R.id.recycler_view).adapter = adapter

        viewModel.workInfo.observe(this, Observer {
            if (it == null || it.isEmpty()) {
                return@Observer
            }

            val info = it[0]

            val finished = info.state.isFinished
            if (finished) {
                Toast.makeText(requireContext(), "Finished downloading data", Toast.LENGTH_LONG).show()
            }
        })

        viewModel.isRefreshing.observe(this, Observer {
            requireView().findViewById<SwipeRefreshLayout>(R.id.swipe_refresh_layout).isRefreshing = it
        })

        viewModel.updateData.observe(this, Observer {
            adapter.submitList(it)
        })
        viewModel.refreshLiveUpdates()

        requireView().findViewById<SwipeRefreshLayout>(R.id.swipe_refresh_layout).setOnRefreshListener {
            viewModel.refreshLiveUpdates()
        }
    }

    private fun onMenuItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.download -> viewModel.checkAndApplyDataUpdates()
        }

        return true
    }
}
