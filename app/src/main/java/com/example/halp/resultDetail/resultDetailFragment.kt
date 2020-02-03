package com.example.halp.resultDetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController

import com.example.halp.R
import com.example.halp.database.ResultDatabase
import com.example.halp.databinding.ResultDetailFragmentBinding
import com.example.halp.resultView.ResultViewFragmentArgs
import com.example.halp.resultView.ResultViewFragmentDirections


class resultDetail : Fragment() {

        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                                  savedInstanceState: Bundle?): View? {

            // Get a reference to the binding object and inflate the fragment views.
            val binding: ResultDetailFragmentBinding = DataBindingUtil.inflate(
                inflater, R.layout.result_detail_fragment, container, false)

            val application = requireNotNull(this.activity).application
            val arguments = ResultViewFragmentArgs.fromBundle(arguments!!)


            // Create an instance of the ViewModel Factory.
            val dataSource = ResultDatabase.getInstance(application).ResultDatabaseDao
            val viewModelFactory = ResultDetailViewModelFactory(arguments.resultKey, dataSource)

            // Get a reference to the ViewModel associated with this fragment.
            val resultDetailViewModel =
                ViewModelProviders.of(
                    this, viewModelFactory).get(ResultDetailViewModel::class.java)

            // To use the View Model with data binding, you have to explicitly
            // give the binding object a reference to it.
            binding.resultDetailViewModel = resultDetailViewModel

            binding.setLifecycleOwner(this)

            // Add an Observer to the state variable for Navigating when a Quality icon is tapped.
            resultDetailViewModel.navigateToResultView.observe(viewLifecycleOwner, Observer {
                if (it == true) { // Observed state is true.
                    this.findNavController().navigate(
                        ResultViewFragmentDirections.actionResultViewFragmentToResultDetail(arguments.resultKey))
                    // Reset state to make sure we only navigate once, even if the device
                    // has a configuration change.
                    resultDetailViewModel.doneNavigating()
                }
            })

//            return binding.root

            val rootView = inflater.inflate(
                R.layout.result_detail_fragment,
                container, false)


//        return binding.root
            return rootView
        }
}
