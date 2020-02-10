package com.example.halp.YelpAPI

import retrofit2.Call
import retrofit2.http.*

interface YelpService {

    @GET("businesses/search")
    fun searchRestaurants(
        @Header("Authorization") authHeader: String,
        @Query("term") searchTerm: String,
        @Query("location") location: String,
        @Query("limit") limit: Int,
        @Query("sort_by") distance: String) : Call<YelpBusinessesSearchResult>

    @GET("businesses/{id}")
    fun getBusinessDetails(
        @Header("Authorization") authHeader: String,
        @Path("id") id: String) : Call<YelpBusinessDetail>
}