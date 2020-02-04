package com.example.halp.YelpAPI

import com.google.gson.annotations.SerializedName

data class YelpSearchResult(
    @SerializedName("total") val total: Int,
    @SerializedName("businesses") val businesses: List<YelpBusiness>
)

data class YelpBusinessDetail(
    val name: String,
    val rating: Double,
    val price: String,
    val location: YelpLocation,
    val categories: List<YelpCategory>,
    val isClosed: Boolean,
    @SerializedName("id") val id: String,
    @SerializedName("review_count") val numReviews: Int,
    @SerializedName("distance") val distanceInMeters: Double,
    @SerializedName("image_url") val imageUrl: String,
    @SerializedName("transactions") val transactions: String,
    @SerializedName("businesses") val businesses: List<YelpBusiness>
)

data class YelpBusiness(
    val name: String,
    val rating: Double,
    val price: String,
    @SerializedName("review_count") val numReviews: Int,
    @SerializedName("distance") val distanceInMeters: Double,
    @SerializedName("image_url") val imageUrl: String,
    val categories: List<YelpCategory>,
    val location: YelpLocation
) {
    fun displayDistance(): String {
        val distanceInKm = "%.2f".format(distanceInMeters / 1000)
        return "$distanceInKm km"
    }
}

data class YelpCategory(
    val title: String
)

data class YelpLocation(
    @SerializedName("address1") val address: String
)