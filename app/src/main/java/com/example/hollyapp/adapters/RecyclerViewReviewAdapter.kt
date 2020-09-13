package com.example.hollyapp.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hollyapp.R
import com.example.hollyapp.models.ReviewMovie
import com.example.hollyapp.utils.dataComDiaMes
import com.example.hollyapp.utils.dataSoAno
import kotlinx.android.synthetic.main.card_review.view.*
import java.util.*
import kotlin.collections.ArrayList

open class RecyclerViewReviewAdapter (private var movies: ArrayList<ReviewMovie>,
                                 private val context: Context,
                                 private val clickListener: BtnClickListener) : RecyclerView.Adapter<RecyclerViewReviewAdapter.ViewHolder>() {

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        @SuppressLint("SetTextI18n")
        fun bindView(movie: ReviewMovie, context: Context) {
            val preview = context.getString(R.string.by)

            val title = itemView.tv_title_review
            val subtitle = itemView.tv_subtitle_review
            val image = itemView.iv_review
            val dayAndMonth = itemView.tv_day_month
            val year = itemView.tv_year

            title.text = movie.title
            subtitle.text = preview+movie.author
            dayAndMonth.text = dataComDiaMes(movie.publish_date?.time!!)
            year.text = dataSoAno(movie.publish_date?.time!!)
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
            clickListener.onBtnClick(movie.mid)
        }
    }

}