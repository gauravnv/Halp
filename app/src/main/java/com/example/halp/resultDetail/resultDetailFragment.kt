package com.example.halp.resultDetail

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions

import com.example.halp.R
import com.example.halp.YelpAPI.*
import com.example.halp.resultView.ResultViewFragment
import kotlinx.android.synthetic.main.result_detail_fragment.*
import kotlinx.android.synthetic.main.result_row.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val RESULT_KEY_EXTRA = "resultkey"
class ResultDetailFragment : Fragment() {
    var resultKey : String? = null
    lateinit var business: YelpBusinessDetail

        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                                  savedInstanceState: Bundle?): View? {

            val rootView = inflater.inflate(
                R.layout.result_detail_fragment,
                container, false)

            val application = requireNotNull(this.activity).application
//            val arguments = resultDetailFragmentArgs.fromBundle(arguments!!)
            val retrofit =
                Retrofit
                    .Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

            val yelpService = retrofit.create(YelpService::class.java)
//            var business = YelpBusinessDetail("Bake Me Up", 3.4,
//                "$$$", YelpLocation("155 Wellington"), listOf(YelpCategory("Bakery")), false,
//                "!@1234", 1233, 1200.00, "data:image/jpeg;base64,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",
//                "", listOf())

            yelpService.getBusinessDetails("Bearer $API_KEY", resultKey!!).enqueue(object :
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
                    business = body
                }

                override fun onFailure(call: Call<YelpBusinessDetail>, t: Throwable) {
                    Log.i(TAG, "onFailure $t")
                }

            })


//            business_name.text = if (business.name == null) "Yelp Business Name" else business.name
//            num_reviews_text.text = if (business.numReviews == null) "666" else business.numReviews.toString()
//
//            if (application != null) {
//                Glide.with(application).load(business.imageUrl).apply(
//                    RequestOptions().transforms(
//                        CenterCrop(), RoundedCorners(15)
//                    )).into(business_picture)
//            }



//            rootView.findViewById<TextView>(R.id.business_address).setText(business.location)
//            rootView.findViewById<TextView>(R.id.open_hours_info).setText(business.isClosed)
//            business_picture.setImageURI(business.imageUrl.toUri())




            return rootView
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        resultKey = arguments?.getString(RESULT_KEY_EXTRA)
    }

    companion object {
        private val TAG = "ResultDetailFragment"
        private const val BASE_URL = "https://api.yelp.com/v3/"
        private const val API_KEY = "HQTTPg0B255lZaSn4bWP-ZWAbsl_zZKL0UgWdqLW4As0p7FxS9K6BDwIKmOVRTrySrbXlrWR8ocTvMZzlFseRxoZkw925ApI4tnkLmMAo3RZ6KVoTpU5q71lFZggXnYx"
    }
}
