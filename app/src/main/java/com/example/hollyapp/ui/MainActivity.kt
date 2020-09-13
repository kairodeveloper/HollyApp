package com.example.hollyapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.example.hollyapp.R
import com.example.hollyapp.adapters.BtnClickListener
import com.example.hollyapp.adapters.RecyclerViewReviewAdapter
import com.example.hollyapp.models.ReviewMovie
import com.example.hollyapp.services.getAllReviewMovie
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.RealmModel
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity(), BtnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar_main_page)

        Realm.init(this)
    }

    override fun onResume() {
        super.onResume()

        val list = ArrayList<ReviewMovie>()
        list.add(ReviewMovie("Titanic 1", "Kairo", "", Date(), "", "", false))
        list.add(ReviewMovie("Titanic 2", "Kairo", "", Date(), "", "", true))
        list.add(ReviewMovie("Titanic 3", "Kairo", "", Date(), "", "", false))
        list.add(ReviewMovie("Titanic 1", "Kairo", "", Date(), "", "", true))
        list.add(ReviewMovie("Titanic 2", "Kairo", "", Date(), "", "", false))
        list.add(ReviewMovie("Titanic 3", "Kairo", "", Date(), "", "", true))
        list.add(ReviewMovie("Titanic 1", "Kairo", "", Date(), "", "", false))
        list.add(ReviewMovie("Titanic 2", "Kairo", "", Date(), "", "", true))
        list.add(ReviewMovie("Titanic 3", "Kairo", "", Date(), "", "", false))
        list.add(ReviewMovie("Titanic 1", "Kairo", "", Date(), "", "", true))
        list.add(ReviewMovie("Titanic 2", "Kairo", "", Date(), "", "", false))
        list.add(ReviewMovie("Titanic 3", "Kairo", "", Date(), "", "", true))

        val recyclerView = rv_reviews
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = RecyclerViewReviewAdapter(list, this, this)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_sync, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.icon_sync -> {
                Toast.makeText(this, "Synced", Toast.LENGTH_SHORT).show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onBtnClick(mid: Long) {
        val intent = Intent(this, ReviewActivity::class.java)
        startActivity(intent)
    }

}