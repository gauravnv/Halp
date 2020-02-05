package com.example.halp.resultView

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.halp.R
import com.example.halp.YelpAPI.YelpBusiness
import com.yelp.fusion.client.models.Business
import kotlinx.android.synthetic.main.result_row.view.*

class ResultViewAdapter(val context: Context?,
                        val businesses: List<YelpBusiness>,
                        val itemClickListener: ResultViewAdapter.onItemClickListener
) :
    RecyclerView.Adapter<ResultViewAdapter.ViewHolder>() {

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view.
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.result_row, viewGroup, false))
    }

    override fun getItemCount() = businesses.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val business = businesses[position]
        holder.bind(business, itemClickListener)

//        holder.itemView.setOnClickListener{ view ->
//            view.findNavController().navigate(R.id.action_resultViewFragment_to_resultDetail)
//        }
    }



    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(business: YelpBusiness, clickListener: onItemClickListener) {
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
                        CenterCrop(), RoundedCorners(15)
                    )).into(itemView.business_picture)
            }

            itemView.setOnClickListener {
                clickListener.onItemClicked(business)
            }
        }
    }

    companion object {
        private val TAG = "BusinessAdapter"
    }

    interface onItemClickListener {
        fun onItemClicked(business: YelpBusiness)
    }
}

//class ResultViewListener(val clickListener: (resultId: Long) -> Unit) {
//    fun onClick(result: ResultRow) = clickListener(result.r_id)
//}