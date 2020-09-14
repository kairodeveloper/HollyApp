package com.example.hollyapp.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.hollyapp.R
import com.example.hollyapp.models.ReviewMovieFavorite
import com.example.hollyapp.services.findFirstFavoriteByMovie
import com.example.hollyapp.services.updateFavorite
import com.example.hollyapp.services.updateReviewMovie
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_review.*
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import java.io.IOException

class ReviewActivity : AppCompatActivity() {
    private var reviewMovieFavorite: ReviewMovieFavorite = ReviewMovieFavorite()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_review)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val reviewMovieMid = intent.getLongExtra("movieMid", 0L)
        val reviewMovieTitle = intent.getStringExtra("movieTitle")
        val reviewMovieAuthor = intent.getStringExtra("movieAuthor")
        val reviewMovieData = intent.getStringExtra("movieData")
        val reviewMovieUrlPic = intent.getStringExtra("movieUrlPic")
        val reviewMovieUrl = intent.getStringExtra("movieUrl")
        val reviewArticle = intent.getStringExtra("article")

        tv_title_movie_review.text = reviewMovieTitle
        tv_subtitle_movie_review.text = this.getString(R.string.by).plus(" ").plus(reviewMovieAuthor)
        tv_day_month_movie_review.text = reviewMovieData

        if (reviewMovieUrlPic!="") {
            Picasso.with(this).load(reviewMovieUrlPic).into(iv_review_activity)
        }

        reviewMovieFavorite = findFirstFavoriteByMovie(reviewMovieTitle.toString())

        if (reviewMovieFavorite.favorite) {
            ib_favorite.visibility = View.VISIBLE
            ib_no_favorite.visibility = View.GONE
        } else {
            ib_favorite.visibility = View.GONE
            ib_no_favorite.visibility = View.VISIBLE
        }


        if (reviewArticle?.length==0 || reviewArticle==null) {
            val size = reviewMovieUrl.toString().length
            val address ="https"+reviewMovieUrl.toString().substring(4, size)

            getArticleFromWeb(address, reviewMovieMid)
        } else {
           updateReview(reviewArticle!!)
        }
    }

    fun getArticleFromWeb(address: String, midReview: Long) {
        val downloadThread: Thread = object : Thread() {
            override fun run() {
                val doc: Document
                try {
                    doc = Jsoup.connect(address).get()
                    val paragraphs: Elements = doc.select("p")
                    var returnData = ""
                    println("Liiiink ######################$address")
                    for(p in paragraphs) {
                        if (p.hasClass("evys1bk0")){
                            println("######################"+p.text())
                            returnData += p.text()+"\n"
                        }
                    }

                    runOnUiThread {
                        updateReview(returnData)
                        if (midReview>0) {
                            updateReviewMovie(midReview, returnData)
                        }
                    }

                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
        downloadThread.start()

    }

    fun updateReview(text: String) {
        tv_review_text.text = text
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_review, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }

        return true
    }

    fun setAsFavorite(view: View) {
        ib_favorite.visibility = View.VISIBLE
        ib_no_favorite.visibility = View.GONE

        updateFavorite(reviewMovieFavorite, true)
    }

    fun setAsANoFavorite(view: View) {
        ib_favorite.visibility = View.GONE
        ib_no_favorite.visibility = View.VISIBLE

        updateFavorite(reviewMovieFavorite, false)
    }


}