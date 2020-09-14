package com.example.hollyapp.services

import com.example.hollyapp.models.ReviewMovie
import com.example.hollyapp.models.ReviewMovieFavorite
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.RealmModel
import io.realm.RealmObject
import kotlin.collections.ArrayList

fun configRealm() {
    val config = RealmConfiguration.Builder()
                                    .schemaVersion(4)
                                    .deleteRealmIfMigrationNeeded()
                                    .build()

    Realm.setDefaultConfiguration(config)
}

fun findAll(clazz: Class<out RealmModel?>?) : ArrayList<Any> {
    val realm = Realm.getDefaultInstance()

    val data = ArrayList<Any>()
    val results = realm.where(clazz).findAll()
    data.addAll(results)

    return data
}

fun findAllByTitle(stringValue: String) : ArrayList<ReviewMovie> {
    val realm = Realm.getDefaultInstance()

    val data = ArrayList<ReviewMovie>()
    val results = realm.where(ReviewMovie::class.java).contains("title", stringValue).findAll()
    data.addAll(results)

    return data
}

fun findFirstFavoriteByMovie(movieTitle: String): ReviewMovieFavorite {
    val realm = Realm.getDefaultInstance()
    val result = realm.where(ReviewMovieFavorite::class.java).equalTo("reviewMovie", movieTitle).findFirst()

    return if (result==null) {
        val newReviewMovieFavorite = ReviewMovieFavorite(movieTitle, false)
        newReviewMovieFavorite.mid = getNextMid(ReviewMovieFavorite::class.java)
        newReviewMovieFavorite
    } else {
        result
    }
}

fun getNextMid(clazz: Class<out RealmModel?>?): Long {
    val realm = Realm.getDefaultInstance()
    val primaryKey = realm.where(clazz).max("mid")
    return (primaryKey?.toLong() ?: 0) + 1
}

fun updateFavorite(obj: ReviewMovieFavorite, status: Boolean) {
    val realm = Realm.getDefaultInstance()
    realm.beginTransaction()
    obj.favorite = status
    realm.copyToRealmOrUpdate(obj)
    realm.commitTransaction()
    realm.close()
}

fun updateReviewMovie(mid: Long, article: String) {
    val realm = Realm.getDefaultInstance()
    realm.beginTransaction()

    val obj = realm.where(ReviewMovie::class.java).equalTo("mid", mid).findFirst()
    obj?.article = article
    realm.copyToRealmOrUpdate(obj)

    realm.commitTransaction()
    realm.close()
}