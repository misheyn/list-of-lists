package com.example.rgr.structs

import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable
import kotlin.math.*

class GPS(private val latitude: Double, private val longitude: Double) : Comparable<GPS>, Serializable {

    fun distanceTo(otherLocation: GPS): Double {
        val earthRadius = 6371.0 // Средний радиус Земли в километрах

        val lat1 = Math.toRadians(latitude)
        val lon1 = Math.toRadians(longitude)
        val lat2 = Math.toRadians(otherLocation.latitude)
        val lon2 = Math.toRadians(otherLocation.longitude)

        val dLat = lat2 - lat1
        val dLon = lon2 - lon1

        val a = sin(dLat / 2) * sin(dLat / 2) +
                cos(lat1) * cos(lat2) * sin(dLon / 2) * sin(dLon / 2)

        val c = 2 * atan2(sqrt(a), sqrt(1 - a))

        return earthRadius * c
    }

    override fun compareTo(other: GPS): Int {
        return latitude.compareTo(other.latitude)
    }

    override fun toString(): String {
        return "Latitude: $latitude, Longitude: $longitude"
    }

    constructor(parcel: Parcel) : this(
        parcel.readDouble(),
        parcel.readDouble()
    )

    fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeDouble(latitude)
        parcel.writeDouble(longitude)
    }

    fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<GPS> {
        override fun createFromParcel(parcel: Parcel): GPS {
            return GPS(parcel)
        }

        override fun newArray(size: Int): Array<GPS?> {
            return arrayOfNulls(size)
        }
    }
}
