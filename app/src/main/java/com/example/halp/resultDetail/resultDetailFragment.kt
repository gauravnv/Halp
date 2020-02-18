package com.example.halp.resultDetail

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions

import com.example.halp.R
import com.example.halp.YelpAPI.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.result_detail_fragment.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val RESULT_KEY_EXTRA = "resultkey"
const val LATITUDE = "business_latitude"
const val LONGITUDE = "business_longitude"
const val BUSINESS_NAME = "business_name"

class ResultDetailFragment : Fragment(), OnMapReadyCallback {
    private lateinit var recyclerView: RecyclerView
    private lateinit var reviewAdapter: RecyclerView.Adapter<*>
    var resultKey: String? = null
    var business_latitude: Double = 75.5
    var business_longitude: Double = 49.5
    var business_name_marker: String? = "Business Name"
    private var mMap: MapView? = null
    private lateinit var latLng: LatLng


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        resultKey = arguments?.getString(RESULT_KEY_EXTRA)
        business_latitude = arguments!!.getDouble(LATITUDE)
        business_longitude = arguments!!.getDouble(LONGITUDE)
        business_name_marker  = arguments!!.getString(BUSINESS_NAME)
    }


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

        mMap = rootView.findViewById(R.id.mapView) as MapView
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
        val businessReview = mutableListOf<YelpReviewDataClass.Review>()

        recyclerView = rootView.findViewById(R.id.review_recycler)
        reviewAdapter = ReviewAdapter(this.context, businessReview)
        recyclerView.adapter = reviewAdapter
        recyclerView.layoutManager = LinearLayoutManager(this.context)

        val businessDetailCall = yelpService.getBusinessDetails("Bearer $API_KEY", resultKey!!)
        latLng = LatLng(-74.3, 75.5)
        val businessReviewCall = yelpService.getBusinessReviews("Bearer $API_KEY", resultKey!!)


        businessDetailCall.enqueue(object :
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

                businessReviewCall.enqueue(object :
                    Callback<YelpReviewDataClass> {
                    override fun onResponse(
                        call: Call<YelpReviewDataClass>,
                        response: Response<YelpReviewDataClass>
                    ) {
                        Log.i(TAG, "onResponse $response")
                        val body = response.body()
                        if (body == null) {
                            Log.w(TAG, "Did not receive valid response body from Yelp API... exiting")
                            return
                        }
                        businessReview.addAll(body.reviews)
                        reviewAdapter.notifyDataSetChanged()
                        updateUI(business)
                    }

                    override fun onFailure(call: Call<YelpReviewDataClass>, t: Throwable) {
                        Log.i(TAG, "onFailure $t")
                    }
                }
                )
            }

            override fun onFailure(call: Call<YelpBusinessDetail>, t: Throwable) {
                Log.i(TAG, "onFailure $t")
            }

        })

        return rootView
    }

    fun updateUI(business: MutableLiveData<YelpBusinessDetail>) {

        Log.i(TAG, business.value!!.name)
        business_name.text = business.value!!.name
        num_reviews_text.text = business.value!!.numReviews.toString()
        business_address.text = business.value!!.location.address
        open_hours_info.text = "until ${business.value?.hours?.get(0)?.open?.get(0)?.end}"

        if(business.value!!.isClosed) {
            business_is_open.setTextColor(Color.RED)
            business_is_open.text = "Closed"
        } else {
            business_is_open.setTextColor(Color.parseColor("#00CC00"))
            business_is_open.text = "Open"
        }
        business_detail_rating.rating = business.value!!.rating.toFloat()
        business_price.text = business.value!!.price
        business_category.text = business.value!!.categories[0].title
        latLng = LatLng(business.value!!.coordinates.latitude, business.value!!.coordinates.longitude)

        Glide.with(this.activity!!).load(business.value!!.imageUrl).apply(
            RequestOptions().transforms(
                CenterCrop(), RoundedCorners(15)
            )).into(business_picture)
    }




    companion object {
        private val TAG = "ResultDetailFragment"
        private const val BASE_URL = "https://api.yelp.com/v3/"
        private const val API_KEY =
            "HQTTPg0B255lZaSn4bWP-ZWAbsl_zZKL0UgWdqLW4As0p7FxS9K6BDwIKmOVRTrySrbXlrWR8ocTvMZzlFseRxoZkw925ApI4tnkLmMAo3RZ6KVoTpU5q71lFZggXnYx"
        val GAPI = "AIzaSyBsEUUnQK0-IBgMp-mjL02YSpjcAiC0pYc"
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
        latLng = LatLng(business_latitude, business_longitude)
        val mBusiness = googleMap.addMarker(MarkerOptions().position(latLng).title(business_name_marker))
        mBusiness.tag = 0
        val cameraPosition = CameraPosition(latLng, 17.0f, 30f, 90f)
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
    }

    override fun onResume() {
        super.onResume()
        mMap?.onResume()
    }

    override fun onStart() {
        super.onStart()
        mMap?.onStart()
    }

    override fun onPause() {
        super.onPause()
        mMap?.onPause()
    }

    override fun onStop() {
        super.onStop()
        mMap?.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        mMap?.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mMap?.onLowMemory()
    }

}
