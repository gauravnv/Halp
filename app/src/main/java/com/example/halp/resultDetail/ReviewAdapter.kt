package com.example.halp.resultDetail

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.Placeholder
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.halp.R
import com.example.halp.YelpAPI.YelpReviewDataClass
import kotlinx.android.synthetic.main.review_row.view.*

class ReviewAdapter(val context: Context?,
                    val businessReview: List<YelpReviewDataClass.Review>):
        RecyclerView.Adapter<ReviewAdapter.ViewHolder>() {

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view.
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.review_row, viewGroup, false))
    }

    override fun getItemCount() = businessReview.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val business = businessReview[position]
        holder.bind(business)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(review: YelpReviewDataClass.Review) {
            itemView.reviewer_name.text = review.user.name
            itemView.review_date.text = review.timeCreated
            itemView.review_text.text = review.text
            itemView.review_rating_stars.rating = review.rating.toFloat()

            if (context != null) {
                Glide.with(context).load(review.user.imageUrl)
                    .apply(
                    RequestOptions()
                        .transforms(
                        CenterCrop(),
                        RoundedCorners(15)
                    ))
                    .into(itemView.reviewer_image)

                Glide.with(itemView).load(review.user.imageUrl).preload()
            }
        }
    }

    companion object {
        private val TAG = "ReviewAdapter"
    }
}