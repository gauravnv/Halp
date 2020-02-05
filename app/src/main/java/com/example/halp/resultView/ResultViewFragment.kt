package com.example.halp.resultView

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.halp.R
import com.example.halp.YelpAPI.YelpBusiness
import com.example.halp.YelpAPI.YelpSearchResult
import com.example.halp.YelpAPI.YelpService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ResultViewFragment : Fragment(), ResultViewAdapter.onItemClickListener {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: RecyclerView.Adapter<*>

    override fun onCreateView(inflater: LayoutInflater,
                          container: ViewGroup?,
                          savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(
            R.layout.result_view_fragment,
            container, false).apply { tag = TAG}


        recyclerView = rootView.findViewById(R.id.result_grid)


        val businesses = mutableListOf<YelpBusiness>()
        adapter = ResultViewAdapter(this.context, businesses, this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this.context)

        val retrofit =
            Retrofit
                .Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        val yelpService = retrofit.create(YelpService::class.java)

        yelpService.searchRestaurants("Bearer $API_KEY", "Bakery", "Toronto", 50, "distance").enqueue(object :
            Callback<YelpSearchResult> {
            override fun onResponse(call: Call<YelpSearchResult>, response: Response<YelpSearchResult>) {
                Log.i(TAG, "onResponse $response")
                val body = response.body()
                if (body == null) {
                    Log.w(TAG, "Did not receive valid response body from Yelp API... exiting")
                    return
                }
                businesses.addAll(body.businesses)
                adapter.notifyDataSetChanged()
            }

            override fun onFailure(call: Call<YelpSearchResult>, t: Throwable) {
                Log.i(TAG, "onFailure $t")
            }
        })

        return rootView
    }

    override fun onItemClicked(business: YelpBusiness) {
        Toast.makeText(this.context, "Business name ${business.name} \n Location:${business.location} \n ID: ${business.b_ID}",
            Toast.LENGTH_LONG)
            .show()
        Log.i("BUSINESS_", business.name)

        this.findNavController().navigate(ResultViewFragmentDirections.actionResultViewFragmentToResultDetail(business.b_ID))
    }

    companion object {
        private val TAG = "ResultViewFragment"
        private const val BASE_URL = "https://api.yelp.com/v3/"
        private const val API_KEY = "HQTTPg0B255lZaSn4bWP-ZWAbsl_zZKL0UgWdqLW4As0p7FxS9K6BDwIKmOVRTrySrbXlrWR8ocTvMZzlFseRxoZkw925ApI4tnkLmMAo3RZ6KVoTpU5q71lFZggXnYx"
    }
}