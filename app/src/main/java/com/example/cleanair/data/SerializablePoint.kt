package com.example.cleanair.data
import com.arcgismaps.geometry.Point
import com.google.gson.annotations.SerializedName

data class SerializablePoint(
    @SerializedName("x") val x: Double,
    @SerializedName("y") val y: Double
    // Add other properties of Point if needed
)

fun Point.toSerializablePoint(): SerializablePoint {
    return SerializablePoint(this.x, this.y)
}

fun SerializablePoint.toPoint(): Point {
    return Point(this.x, this.y)
}
