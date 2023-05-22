package uz.gitamy.photoeditor

import android.content.res.Resources
import android.graphics.PointF
import android.util.Log

val Int.dp: Int
    get() = (this / Resources.getSystem().displayMetrics.density).toInt()
val Int.px: Int
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()

fun lineLength(firstPoint: PointF, secondPoint: PointF): Double {
    return Math.sqrt(((firstPoint.x - secondPoint.x) * (firstPoint.x - secondPoint.x) + (firstPoint.y - secondPoint.y) * (firstPoint.y - secondPoint.y)).toDouble())
}

fun myLog(message: String, tag: String = "TTT") {
    Log.d(tag, message)
}

