package com.example.halp.YelpAPI

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

public interface YelpService {

    @GET("businesses/search")
    fun searchRestaurants(
        @Header("Authorization") authHeader: String,
        @Query("term") searchTerm: String,
        @Query("location") location: String,
        @Query("limit") limit: Int,
        @Query("sort_by") distance: String) : Call<YelpSearchResult>

    @GET("businesses/")
    fun getBusinessDetails(
        @Header("Authorization") authHeader: String,
        @Query("id") id: String) : Call<YelpBusinessDetail>
}