package com.example.hollyapp.services

import com.example.hollyapp.models.ReviewMovie
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.RealmModel
import io.realm.RealmObject
import java.util.*

fun configRealm() {
    val config = RealmConfiguration.Builder()
                                    .schemaVersion(1)
                                    .deleteRealmIfMigrationNeeded()
                                    .build()

    Realm.setDefaultConfiguration(config)
}

fun getAllReviewMovie() : ArrayList<ReviewMovie> {
    configRealm()
    val realm = Realm.getDefaultInstance()

    val data = ArrayList<ReviewMovie>()
    val results = realm.where(ReviewMovie::class.java).findAll()

    data.addAll(results)

    return data
}