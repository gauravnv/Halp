package com.example.halp.resultView

import android.app.Activity
import android.content.Context.LOCATION_SERVICE
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.halp.MainActivity
import com.example.halp.R
import com.example.halp.YelpAPI.YelpBusiness
import com.example.halp.YelpAPI.YelpBusinessesSearchResult
import com.example.halp.YelpAPI.YelpService
import com.example.halp.resultDetail.*
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class ResultViewFragment : Fragment(), ResultViewAdapter.onItemClickListener, View.OnClickListener {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: RecyclerView.Adapter<*>
    private var searchParam: String = "Bakery"
    private lateinit var searchButton: ImageButton
    private lateinit var searchBar: EditText
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onClick(v: View?) {
        Log.i(TAG, "THIS IS THE SEARCH PARAM $searchParam")
        populateView(searchParam)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this.activity as Activity)
    }

    private fun populateView(searchParam: String) {
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
//        val lat = 0.0
//        val lon = 0.0
//        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
//            lat = location?.latitude!!
//            lon = location?.longitude!!
//        }

        yelpService.searchRestaurants("Bearer $API_KEY", searchParam, "Toronto", 50, "distance").enqueue(object :
            Callback<YelpBusinessesSearchResult> {
            override fun onResponse(call: Call<YelpBusinessesSearchResult>, response: Response<YelpBusinessesSearchResult>) {
                Log.i(TAG, "onResponse $response")
                val body = response.body()
                if (body == null) {
                    Log.w(TAG, "Did not receive valid response body from Yelp API... exiting")
                    return
                }
                businesses.addAll(body.businesses)
                adapter.notifyDataSetChanged()
            }

            override fun onFailure(call: Call<YelpBusinessesSearchResult>, t: Throwable) {
                Log.i(TAG, "onFailure $t")
            }
        })
    }

    override fun onCreateView(inflater: LayoutInflater,
                          container: ViewGroup?,
                          savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(
            R.layout.result_view_fragment,
            container, false).apply { tag = TAG}


        recyclerView = rootView.findViewById(R.id.result_grid)
        searchBar = rootView.findViewById(R.id.search_for_business)
        searchButton = rootView.findViewById(R.id.search_icon)

        searchButton.setOnClickListener(this)
        searchBar.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                searchParam = s.toString()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                searchParam = s.toString()
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchParam = s.toString()
            }
        })

        populateView(searchParam)

        return rootView
    }


    override fun onItemClicked(business: YelpBusiness) {
//        Toast.makeText(this.context, "Business name ${business.name} \n Location:${business.location} \n ID: ${business.id}",
//            Toast.LENGTH_LONG)
//            .show()
        Log.i("BUSINESS_", business.name)
        val bundle = Bundle()
        bundle.putString(RESULT_KEY_EXTRA, business.id)
        bundle.putDouble(LATITUDE, business.coordinates.latitude)
        bundle.putDouble(LONGITUDE, business.coordinates.longitude)
        bundle.putString(BUSINESS_NAME, business.name)
        val resultDetailFragment = ResultDetailFragment()
        resultDetailFragment.arguments = bundle
        (context as MainActivity).replaceFragment(resultDetailFragment, true)
    }


    companion object {
        private val TAG = "ResultViewFragment"
        private const val BASE_URL = "https://api.yelp.com/v3/"
        private const val API_KEY = "HQTTPg0B255lZaSn4bWP-ZWAbsl_zZKL0UgWdqLW4As0p7FxS9K6BDwIKmOVRTrySrbXlrWR8ocTvMZzlFseRxoZkw925ApI4tnkLmMAo3RZ6KVoTpU5q71lFZggXnYx"
    }


}

