package visioncore.mosaicrecyclerview.HoneyMosaicLayout.Utils

import android.content.res.Resources
import android.graphics.BitmapFactory
import visioncore.mosaicrecyclerview.HoneyMosaicLayout.View.Category
import java.io.File
import kotlin.properties.ObservableProperty
import kotlin.reflect.KProperty

/**
 * Created by kaysaith on 11/08/2017.
 */

fun <T> observing(initialValue: T, willSet: () -> Unit = { }, didSet: () -> Unit = { }) = object : ObservableProperty<T>(initialValue) {
	override fun beforeChange(property: KProperty<*>, oldValue: T, newValue: T): Boolean = true.apply { willSet() }
	
	override fun afterChange(property: KProperty<*>, oldValue: T, newValue: T) = didSet()
}

fun getScreenWidth(): Int {
	return Resources.getSystem().displayMetrics.widthPixels
}

fun getScreenHeight(): Int {
	return Resources.getSystem().displayMetrics.heightPixels
}

fun dpFromUIPx (px: Int): Float {
	//PX是IOS的一倍图尺寸
	return px * 1.018f
}

fun pxFromDp (dp: Int): Int {
	return (dp * Resources.getSystem().displayMetrics.density).toInt()
}

fun Int.UIPx (): Int{
	return pxFromDp(dpFromUIPx(this).toInt())
}

fun detectorShape(width: Any, height: Any): Category {
	val imageWidth = Integer.parseInt(width.toString()).toFloat()
	val imageHeight = Integer.parseInt(height.toString()).toFloat()
	val shape = if (imageWidth / imageHeight in 0.0 .. 0.75) Category.Vertical else if (imageWidth / imageHeight in 0.76 .. 1.25) Category.Square else Category.Horizontal
	return shape
}

fun getImageSize(imagePath: String): ArrayList<Int> {
	val options = BitmapFactory.Options()
	options.inJustDecodeBounds = true
	BitmapFactory.decodeFile(File(imagePath).absolutePath, options)
	val imageHeight = options.outHeight
	val imageWidth = options.outWidth
	val sizeInfo = arrayListOf(imageWidth, imageHeight)
	return sizeInfo
}
