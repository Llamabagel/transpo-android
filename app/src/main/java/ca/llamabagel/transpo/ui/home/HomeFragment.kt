/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import ca.llamabagel.transpo.R
import ca.llamabagel.transpo.di.injector
import ca.llamabagel.transpo.ui.trips.TripsActivity
import ca.llamabagel.transpo.utils.Activities
import ca.llamabagel.transpo.utils.startActivity

class HomeFragment : Fragment() {

    companion object {
        fun newInstance() = HomeFragment()
    }

    private lateinit var viewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.home_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProviders.of(requireActivity(), requireActivity().injector.homeViewModelFactory())[HomeViewModel::class.java]

        requireView().findViewById<Button>(R.id.button).setOnClickListener {
            viewModel.checkAndApplyDataUpdates()
        }

        viewModel.workInfo.observe(this, Observer {
            if (it == null || it.isEmpty()) {
                return@Observer
            }

            val info = it[0]

            val finished = info.state.isFinished
            requireView().findViewById<ProgressBar>(R.id.progressBar).visibility = if (finished) View.INVISIBLE else View.VISIBLE
        })

        requireView().findViewById<Button>(R.id.openButton).setOnClickListener {
            val id = requireView().findViewById<EditText>(R.id.editText).text.toString()
            requireActivity().startActivity<TripsActivity>(requireActivity()) {
                putExtra(Activities.Trips.EXTRA_STOP_ID, id)
            }
        }
        // TODO: Use the ViewModel
    }

}
