package com.example.halp.YelpAPI

import com.google.gson.annotations.SerializedName

data class YelpBusinessesSearchResult(
    @SerializedName("total") val total: Int,
    @SerializedName("businesses") val businesses: List<YelpBusiness>
)

data class YelpBusinessDetail(
    val id: String,
    val name: String,
    val rating: Double,
    val price: String,
    val location: YelpLocation,
//    val categories: List<YelpCategory>,
    val coordinates: YelpCoordinates,
    @SerializedName("is_closed") val isClosed: Boolean,
    @SerializedName("review_count") val numReviews: Int,
    @SerializedName("distance") val distanceInMeters: Double,
    @SerializedName("image_url") val imageUrl: String,
    val categories: List<YelpCategory>,
    val hours: List<YelpBusinessHours>
)

data class YelpBusiness(
    val id: String,
    val name: String,
    val rating: Double,
    val price: String,
    @SerializedName("review_count") val numReviews: Int,
    @SerializedName("distance") val distanceInMeters: Double,
    @SerializedName("image_url") val imageUrl: String,
    val categories: List<YelpCategory>,
    val coordinates: YelpCoordinates,
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

data class YelpCoordinates(
    val latitude: Double,
    val longitude: Double
)

data class YelpLocation(
    @SerializedName("address1") val address: String
)

data class YelpBusinessHours(
    val open: List<YelpOpenHours>,
    @SerializedName("is_open_now") val isOpenNow: Boolean
)

data class YelpOpenHours(
    val start: String,
    val end: String,
    val day: Int,
    @SerializedName("is_overnight") val isOvernight: Boolean
)