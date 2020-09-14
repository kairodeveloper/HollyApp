package com.example.hollyapp.ui

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuItemCompat
import com.example.hollyapp.R
import com.example.hollyapp.adapters.BtnClickListener
import com.example.hollyapp.adapters.RecyclerViewReviewAdapter
import com.example.hollyapp.models.ReviewMovie
import com.example.hollyapp.services.*
import com.example.hollyapp.services.beans.DeviceResponse
import com.example.hollyapp.services.beans.MovieReviewBean
import com.example.hollyapp.utils.dataComDiaMes
import com.google.gson.Gson
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.Serializable
import java.time.LocalDate
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity(), BtnClickListener {
    private val API_KEY = "kce7uAwo3n0ycyR9WQIE5jGocsXdBCSl"
    private var list = ArrayList<ReviewMovie>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        setSupportActionBar(toolbar_main_page)

        Realm.init(this)
        configRealm()
    }

    override fun onResume() {
        super.onResume()

        list.addAll(findAll(ReviewMovie::class.java) as ArrayList<ReviewMovie>)

        if (list.size == 0) {
            getReviewsFromApi()
        } else {
            updateData(list)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_sync, menu)
        val menuItem = menu?.findItem(R.id.icon_search)
        val searchView = menuItem?.actionView as SearchView

        searchView.queryHint = getString(R.string.search_movie_label)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                getReviewsBySearch(p0.toString())
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                ll_click_more_movies.visibility = View.GONE
                list.clear()
                list.addAll(findAllByTitle(p0.toString()))
                updateData(list)
                return true
            }
        })


        searchView.setOnCloseListener {
            ll_click_more_movies.visibility = View.VISIBLE

            list.clear()
            list.addAll(findAll(ReviewMovie::class.java) as ArrayList<ReviewMovie>)

            updateData(list)

            true
        }

        return super.onCreateOptionsMenu(menu)
    }

    override fun onBtnClick(movie: ReviewMovie) {
        var dateString = ""
        dateString = dateString.plus(dataComDiaMes(movie.publish_date?.time!!))
        dateString = dateString.plus("/").plus(movie.publish_date?.year)

        val intent = Intent(this, ReviewActivity::class.java)
        intent.putExtra("movieMid", movie.mid)
        intent.putExtra("movieTitle", movie.title)
        intent.putExtra("movieAuthor", movie.author)
        intent.putExtra("movieData", dateString)
        intent.putExtra("movieUrlPic", movie.url_pic)
        intent.putExtra("movieUrl", movie.url_review)
        intent.putExtra("article", movie.article)
        startActivity(intent)
    }

    private fun getReviewsBySearch(valueString: String) {
        val response = RetrofitInitializer().reviewService().reviewSearch(API_KEY, valueString)
        response.enqueue(object : Callback<DeviceResponse> {
            override fun onFailure(call: Call<DeviceResponse>, t: Throwable) {
                Toast.makeText(this@MainActivity, "${t.message}", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(
                call: Call<DeviceResponse>,
                response: Response<DeviceResponse>
            ) {
                val returnData = ArrayList<MovieReviewBean>()
                returnData.addAll(response.body()?.results!!)

                val filteredData = ArrayList<ReviewMovie>()

                for (movieReviewBean in returnData) {
                    val dateReviewBean = movieReviewBean.publication_date
                    val listDate = dateReviewBean.split("-").toTypedArray()

                    val newDate = Date(listDate[0].toInt(), listDate[1].toInt() - 1, listDate[2].toInt())
                    val multimediaSrc = movieReviewBean.multimedia ?: ""

                    val reviewMovie = ReviewMovie(
                        movieReviewBean.display_title,
                        movieReviewBean.byline,
                        movieReviewBean.summary_short,
                        newDate,
                        movieReviewBean.link.url,
                        multimediaSrc.toString()
                    )

                    filteredData.add(reviewMovie)
                }

                list.clear()
                list.addAll(filteredData)

                updateData(list)

            }
        }
        )
    }

    private fun getReviewsFromApi() {
        val size = findAll(ReviewMovie::class.java).size
        val response = RetrofitInitializer().reviewService().reviewDevice(API_KEY, size)
        response.enqueue(object : Callback<DeviceResponse> {
            override fun onFailure(call: Call<DeviceResponse>, t: Throwable) {
                Toast.makeText(this@MainActivity, "${t.message}", Toast.LENGTH_SHORT).show()
            }

            @RequiresApi(Build.VERSION_CODES.O)
            override fun onResponse(
                call: Call<DeviceResponse>,
                response: Response<DeviceResponse>
            ) {
                val returnData = ArrayList<MovieReviewBean>()
                returnData.addAll(response.body()?.results!!)

                val realm = Realm.getDefaultInstance()
                realm.beginTransaction()

                for (movieReviewBean in returnData) {
                    val dateReviewBean = movieReviewBean.publication_date
                    val listDate = dateReviewBean.split("-").toTypedArray()

                    val newDate = Date(listDate[0].toInt(), listDate[1].toInt() - 1, listDate[2].toInt())

                    val reviewMovie = ReviewMovie(
                        movieReviewBean.display_title,
                        movieReviewBean.byline,
                        movieReviewBean.summary_short,
                        newDate,
                        movieReviewBean.link.url,
                        movieReviewBean.multimedia.src
                    )
                    reviewMovie.mid = getNextMid(ReviewMovie::class.java)

                    realm.copyToRealmOrUpdate(reviewMovie)
                }
                realm.commitTransaction()
                realm.close()

                list.clear()
                list.addAll(findAll(ReviewMovie::class.java) as ArrayList<ReviewMovie>)

                updateData(list)
            }

        })
    }

    fun updateData(list: ArrayList<ReviewMovie>) {
        val recyclerView = rv_reviews
        recyclerView.isNestedScrollingEnabled = false;
        recyclerView.adapter = RecyclerViewReviewAdapter(list, this, this)
    }

    fun clickButton(view: View) {
        getReviewsFromApi()
    }

}
