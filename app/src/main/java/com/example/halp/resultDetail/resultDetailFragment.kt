package com.example.halp.resultDetail

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions

import com.example.halp.R
import com.example.halp.YelpAPI.*
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.result_detail_fragment.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val RESULT_KEY_EXTRA = "resultkey"

class ResultDetailFragment : Fragment(), OnMapReadyCallback {
    var resultKey: String? = null
    private var mMap: MapView? = null


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        mMap?.onSaveInstanceState(outState)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val rootView = inflater.inflate(
            R.layout.result_detail_fragment,
            container, false
        )

        mMap = rootView?.findViewById(R.id.mapView) as MapView
        mMap?.onCreate(savedInstanceState)
        mMap?.getMapAsync(this)

        val application = requireNotNull(this.activity).application
//            val arguments = resultDetailFragmentArgs.fromBundle(arguments!!)
        val retrofit =
            Retrofit
                .Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        val yelpService = retrofit.create(YelpService::class.java)
        val business = MutableLiveData<YelpBusinessDetail>()

        val call = yelpService.getBusinessDetails("Bearer $API_KEY", resultKey!!)


        call.enqueue(object :
            Callback<YelpBusinessDetail> {
            override fun onResponse(
                call: Call<YelpBusinessDetail>,
                response: Response<YelpBusinessDetail>
            ) {
                Log.i(TAG, "onResponse $response")
                val body = response.body()
                if (body == null) {
                    Log.w(TAG, "Did not receive valid response body from Yelp API... exiting")
                    return
                }
                business.value = body
                updateUI(business, application)
            }

            override fun onFailure(call: Call<YelpBusinessDetail>, t: Throwable) {
                Log.i(TAG, "onFailure $t")
            }

        })

        return rootView
    }

    fun updateUI(business: MutableLiveData<YelpBusinessDetail>, application: Context) {

        Log.i(TAG, business.value!!.name)
        business_name.text = business.value!!.name
        num_reviews_text.text = business.value!!.numReviews.toString()
        business_address.text = business.value!!.location.address
        if(business.value!!.isClosed) {
            open_hours_info.setTextColor(Color.RED)
            open_hours_info.text = "Closed"
        } else {
            open_hours_info.setTextColor(Color.GREEN)
            open_hours_info.text = "Open"
        }
        business_detail_rating.rating = business.value!!.numReviews.toFloat()
        business_price.text = business.value!!.price
        business_category.text = business.value!!.categories[0].title

        Glide.with(this.activity!!).load(business.value!!.imageUrl).apply(
            RequestOptions().transforms(
                CenterCrop(), RoundedCorners(15)
            )).into(business_picture)

//            rootView.findViewById<TextView>(R.id.business_address).setText(business.location)
//            rootView.findViewById<TextView>(R.id.open_hours_info).setText(business.isClosed)
//            business_picture.setImageURI(business.imageUrl.toUri())
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        resultKey = arguments?.getString(RESULT_KEY_EXTRA)
    }

    companion object {
        private val TAG = "ResultDetailFragment"
        private const val BASE_URL = "https://api.yelp.com/v3/"
        private const val API_KEY =
            "HQTTPg0B255lZaSn4bWP-ZWAbsl_zZKL0UgWdqLW4As0p7FxS9K6BDwIKmOVRTrySrbXlrWR8ocTvMZzlFseRxoZkw925ApI4tnkLmMAo3RZ6KVoTpU5q71lFZggXnYx"
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        googleMap.addMarker(MarkerOptions().position(LatLng(0.0, 0.0)).title("Marker"))
    }
}
