/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.ui.home

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import androidx.lifecycle.Observer
import ca.llamabagel.transpo.R
import ca.llamabagel.transpo.ui.trips.TripsActivity
import ca.llamabagel.transpo.utils.Activities
import ca.llamabagel.transpo.utils.startActivity
import javax.inject.Inject

class HomeFragment : Fragment() {

    companion object {
        fun newInstance() = HomeFragment()
    }

    // TODO: Get rid of the Main view model
    private lateinit var viewModel: HomeViewModel
    private lateinit var mainViewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.home_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mainViewModel = ViewModelProviders.of(requireActivity()).get(MainViewModel::class.java)

        requireView().findViewById<Button>(R.id.button).setOnClickListener {
            mainViewModel.checkAndApplyDataUpdates()
        }

        mainViewModel.workInfo.observe(this, Observer {
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
