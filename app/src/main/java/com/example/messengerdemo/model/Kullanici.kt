package com.example.messengerdemo.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Kullanici(val uid:String, val username:String, val profilImageUrl:String) :Parcelable {
constructor() : this("","","")
}