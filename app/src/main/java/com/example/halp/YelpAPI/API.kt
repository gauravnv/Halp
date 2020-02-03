package com.example.halp.YelpAPI

import com.yelp.fusion.client.connection.YelpFusionApi
import com.yelp.fusion.client.connection.YelpFusionApiFactory
import com.yelp.fusion.client.models.ApiKey
import com.yelp.fusion.client.models.Business
import com.yelp.fusion.client.models.Location
import com.yelp.fusion.client.models.SearchResponse
import java.util.ArrayList


class API {
    private val APIkey = ApiKey("HQTTPg0B255lZaSn4bWP-ZWAbsl_zZKL0UgWdqLW4As0p7FxS9K6BDwIKmOVRTrySrbXlrWR8ocTvMZzlFseRxoZkw925ApI4tnkLmMAo3RZ6KVoTpU5q71lFZggXnYx", "Bearer")
    private val apiFactory = YelpFusionApiFactory()
    private val yelpFusionApi: YelpFusionApi = apiFactory.createAPI(APIkey.apiKey)

    fun getBusiness(): ArrayList<Business>? {

        val params: MutableMap<String, String> = HashMap()

        // general params
        params["term"] = "indian food"
        params["latitude"] = "40.581140"
        params["longitude"] = "-111.914184"

        val call = yelpFusionApi.getBusinessSearch(params)
        val searchResponse: SearchResponse? = call.execute().body()

        val totalNumberOfResult = searchResponse?.total // 3


        val businesses = searchResponse?.businesses
        val businessName = businesses?.get(0)?.name

        val rating = businesses?.get(0)?.rating // 4.0

        return businesses

    }
}