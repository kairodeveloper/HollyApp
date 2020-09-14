package com.example.hollyapp.models

import io.realm.Realm
import io.realm.RealmObject
import io.realm.RealmResults
import io.realm.annotations.PrimaryKey
import java.util.*
import kotlin.collections.ArrayList


open class ReviewMovie(var title: String? = null,
                       var author: String? = null,
                       var resume: String? = null,
                       var publish_date: Date? = null,
                       var url_review: String? = null,
                       var url_pic: String? = null,
                       var article: String? = null) : RealmObject() {
    @PrimaryKey
    var mid: Long = 0

}