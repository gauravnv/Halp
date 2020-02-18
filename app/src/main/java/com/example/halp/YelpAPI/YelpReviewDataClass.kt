package com.example.halp.YelpAPI


import com.google.gson.annotations.SerializedName

data class YelpReviewDataClass(
    @SerializedName("possible_languages")
    val possibleLanguages: List<String>,
    val reviews: List<Review>,
    val total: Int // 1705
) {
    data class Review(
        val id: String, // gbrddRN2W7fovuwMhp7W5g
        val rating: Int, // 1
        val text: String, // The food from here is tasty, but I had a weird experience the one time I was here that won't have me coming back. A while back I ordered takeout from here...
        @SerializedName("time_created")
        val timeCreated: String, // 2020-01-13 12:21:13
        val url: String, // https://www.yelp.com/biz/north-india-restaurant-san-francisco?adjust_creative=sDm9_j0YBhxOB0_140oN8w&hrid=gbrddRN2W7fovuwMhp7W5g&utm_campaign=yelp_api_v3&utm_medium=api_v3_business_reviews&utm_source=sDm9_j0YBhxOB0_140oN8w
        val user: User
    ) {
        data class User(
            val id: String, // _3HeZFgkPV7U1jZdBldzAg
            @SerializedName("image_url")
            val imageUrl: String, // https://s3-media2.fl.yelpcdn.com/photo/UFl6FI_BRqN8dkB5CwPxyQ/o.jpg
            val name: String, // M. W.
            @SerializedName("profile_url")
            val profileUrl: String // https://www.yelp.com/user_details?userid=_3HeZFgkPV7U1jZdBldzAg
        )
    }
}