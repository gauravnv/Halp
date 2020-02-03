package com.example.halp.resultView

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.halp.R
import com.example.halp.YelpAPI.YelpRestaurant
import com.example.halp.database.ResultRow
import kotlinx.android.synthetic.main.result_row.view.*

class ResultViewAdapter(val context: Context?, val businesses: List<YelpRestaurant>) :
    RecyclerView.Adapter<ResultViewAdapter.ViewHolder>() {

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view.
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.result_row, viewGroup, false))
    }

    override fun getItemCount() = businesses.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val business = businesses[position]
        holder.bind(business)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(business: YelpRestaurant) {
            itemView.business_name.text = business.name
            itemView.rating_stars.rating = business.rating.toFloat()
            itemView.num_reviews_text.text = "${business.numReviews} Reviews"
            itemView.business_address.text = business.location.address
            itemView.business_category.text = business.categories[0].title
            itemView.business_distance.text = business.displayDistance()
            itemView.business_price.text = business.price
            if (context != null) {
                Glide.with(context).load(business.imageUrl).apply(
                    RequestOptions().transforms(
                        CenterCrop(), RoundedCorners(20)
                    )).into(itemView.business_picture)
            }
        }
    }

    companion object {
        private val TAG = "BusinessAdapter"
    }
}

//class ResultViewListener(val clickListener: (resultId: Long) -> Unit) {
//    fun onClick(result: ResultRow) = clickListener(result.r_id)
//}