package com.example.halp.resultView

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.halp.R
import com.example.halp.YelpAPI.YelpRestaurant
import com.example.halp.YelpAPI.YelpSearchResult
import com.example.halp.YelpAPI.YelpService
import kotlinx.android.synthetic.main.result_view_fragment.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ResultViewFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: RecyclerView.Adapter<*>
    private lateinit var layoutManager: RecyclerView.LayoutManager
    private lateinit var currentLayoutManagerType: LayoutManagerType

    enum class LayoutManagerType { LINEAR_LAYOUT_MANAGER, GRID_LAYOUT_MANAGER }


    override fun onCreateView(inflater: LayoutInflater,
                          container: ViewGroup?,
                          savedInstanceState: Bundle?): View? {
        // Get a reference to the binding object and inflate the fragment views.
//        val binding: ResultViewFragmentBinding = DataBindingUtil.inflate(
//            inflater, R.layout.result_view_fragment, container, false)
//
//
//
//        val application = requireNotNull(this.activity).application
//
//        val dataSource = ResultDatabase.getInstance(application).ResultDatabaseDao
//
//        val viewModelFactory = ResultViewModelFactory(dataSource, application)
//
//        val resultViewModel =
//            ViewModelProviders.of(
//                this, viewModelFactory).get(ResultViewModel::class.java)
//
//        binding.resultViewModel = resultViewModel
//
//        binding.setLifecycleOwner(this)
//
//
////        Navigation
//        resultViewModel.navigateToResultDetail.observe(viewLifecycleOwner, Observer {result ->
//            result?.let {
//                this.findNavController().navigate(
//                    ResultViewFragmentDirections.actionResultViewFragmentToResultDetail(result.id)
//                )
//                resultViewModel.doneNavigating()
//            }
//        })
//
//
////        LAYOUT MANAGEMENT
////        layoutManager = LinearLayoutManager(activity)
////
////        currentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER
////
////        if (savedInstanceState != null) {
////            // Restore saved layout manager type.
////            currentLayoutManagerType = savedInstanceState
////                .getSerializable(KEY_LAYOUT_MANAGER) as LayoutManagerType
////        }
////        setRecyclerViewLayoutManager(currentLayoutManagerType)
////
////        // Set CustomAdapter as the adapter for RecyclerView.
////        recyclerView.adapter = ResultViewAdapter(dataset)
////
////        binding.root.findViewById<ImageView>(R.id.search_icon).setOnClickListener{
////            setRecyclerViewLayoutManager(LayoutManagerType.LINEAR_LAYOUT_MANAGER)
////        }
//
//        val manager = GridLayoutManager(activity, 2)
//        binding.resultGrid.layoutManager = manager
//
//        manager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
//            override fun getSpanSize(position: Int) =  when (position) {
//                0 -> 2
//                else -> 1
//            }
//        }


//        val adapter = ResultViewAdapter(ResultViewListener { result ->
//            ResultViewModel.onResultClicked(result)
//        })
//
//        binding.resultGrid.adapter = adapter
//
//        ResultViewModel.results.observe(viewLifecycleOwner, Observer {
//            it?.let {
//                adapter.addHeaderAndSubmitList(it)
//            }
//        })


        val rootView = inflater.inflate(
            R.layout.result_view_fragment,
            container, false).apply { tag = TAG}

        currentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER
        recyclerView = rootView.findViewById(R.id.result_grid)

//        if (savedInstanceState != null) {
//            // Restore saved layout manager type.
//            currentLayoutManagerType = savedInstanceState
//                .getSerializable(KEY_LAYOUT_MANAGER) as LayoutManagerType
//        }
//        setRecyclerViewLayoutManager(currentLayoutManagerType)


        val restaurants = mutableListOf<YelpRestaurant>()
        adapter = ResultViewAdapter(this.context, restaurants)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this.context)

        val retrofit =
            Retrofit
                .Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        val yelpService = retrofit.create(YelpService::class.java)

        yelpService.searchRestaurants("Bearer $API_KEY", "Bakery", "Toronto").enqueue(object :
            Callback<YelpSearchResult> {
            override fun onResponse(call: Call<YelpSearchResult>, response: Response<YelpSearchResult>) {
                Log.i(TAG, "onResponse $response")
                val body = response.body()
                if (body == null) {
                    Log.w(TAG, "Did not receive valid response body from Yelp API... exiting")
                    return
                }
                restaurants.addAll(body.restaurants)
                adapter.notifyDataSetChanged()
            }

            override fun onFailure(call: Call<YelpSearchResult>, t: Throwable) {
                Log.i(TAG, "onFailure $t")
            }
        })





        // Set CustomAdapter as the adapter for RecyclerView.
//        recyclerView.adapter = ResultViewAdapter(dataset)


//        rootView.findViewById<ImageView>(R.id.search_icon).setOnClickListener{
//            setRecyclerViewLayoutManager(LayoutManagerType.GRID_LAYOUT_MANAGER)
//        }


//        var result = getBusiness()


//        return binding.root
        return rootView
    }


    /**
     * Set RecyclerView's LayoutManager to the one given.
     *
     * @param layoutManagerType Type of layout manager to switch to.
     */
    private fun setRecyclerViewLayoutManager(layoutManagerType: LayoutManagerType) {
        var scrollPosition = 0

        // If a layout manager has already been set, get current scroll position.
        if (recyclerView.layoutManager != null) {
            scrollPosition = (recyclerView.layoutManager as LinearLayoutManager)
                .findFirstCompletelyVisibleItemPosition()
        }

        when (layoutManagerType) {
            LayoutManagerType.GRID_LAYOUT_MANAGER -> {
                layoutManager = GridLayoutManager(activity, SPAN_COUNT)
                currentLayoutManagerType = LayoutManagerType.GRID_LAYOUT_MANAGER
            }
            LayoutManagerType.LINEAR_LAYOUT_MANAGER -> {
                layoutManager = LinearLayoutManager(activity)
                currentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER
            }
        }

        with(recyclerView) {
            layoutManager = this@ResultViewFragment.layoutManager
            scrollToPosition(scrollPosition)
        }

    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) {

        // Save currently selected layout manager.
        savedInstanceState.putSerializable(KEY_LAYOUT_MANAGER, currentLayoutManagerType)
        super.onSaveInstanceState(savedInstanceState)
    }


    companion object {
        private val TAG = "ResultViewFragment"
        private val KEY_LAYOUT_MANAGER = "layoutManager"
        private val SPAN_COUNT = 2
        private val DATASET_COUNT = 60
        private const val BASE_URL = "https://api.yelp.com/v3/"
        private const val API_KEY = "HQTTPg0B255lZaSn4bWP-ZWAbsl_zZKL0UgWdqLW4As0p7FxS9K6BDwIKmOVRTrySrbXlrWR8ocTvMZzlFseRxoZkw925ApI4tnkLmMAo3RZ6KVoTpU5q71lFZggXnYx"
    }
}