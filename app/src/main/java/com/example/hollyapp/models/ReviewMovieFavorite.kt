package com.example.hollyapp.models

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class ReviewMovieFavorite(var reviewMovie: String? = null,
                               var favorite: Boolean = false) : RealmObject() {
    @PrimaryKey
    var mid: Long = 0
}