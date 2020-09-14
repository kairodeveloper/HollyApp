package com.example.hollyapp.adapters

import android.content.Context
import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hollyapp.R
import com.example.hollyapp.models.ReviewMovie
import com.example.hollyapp.utils.dataComDiaMes
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.card_review.view.*
import java.io.InputStream
import java.net.URL


open class RecyclerViewReviewAdapter (private var movies: ArrayList<ReviewMovie>,
                                 private val context: Context,
                                 private val clickListener: BtnClickListener) : RecyclerView.Adapter<RecyclerViewReviewAdapter.ViewHolder>() {

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        fun bindView(movie: ReviewMovie, context: Context) {
            val preview = context.getString(R.string.by)

            val title = itemView.tv_title_review
            val subtitle = itemView.tv_subtitle_review
            val image = itemView.iv_review
            val dayAndMonth = itemView.tv_day_month
            val year = itemView.tv_year

            val valueTitle = if (movie.title==null) { "" } else { movie.title }
            val valueAuthor = if (movie.author==null) { "" } else { movie.author }

            if (movie.url_pic!="") {
                Picasso.with(context).load(movie.url_pic).into(image)
            }

            title.text = if (valueTitle?.length!! > 30) { valueTitle.substring(0, 28).plus("...") } else { valueTitle }
            subtitle.text = preview.plus(" ").plus(if (valueAuthor?.length!! > 30) { valueAuthor.substring(0, 28).plus("...") } else { valueAuthor })
            dayAndMonth.text = dataComDiaMes(movie.publish_date?.time!!)
            year.text = movie.publish_date?.year.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.card_review, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return movies.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val movie = movies[position]

        holder.bindView(movie, context)

        holder.itemView.setOnClickListener {
            clickListener.onBtnClick(movie)
        }
    }

}