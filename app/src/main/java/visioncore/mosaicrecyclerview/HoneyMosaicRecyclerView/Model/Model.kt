package visioncore.mosaicrecyclerview.HoneyMosaicLayout.Model

import android.widget.LinearLayout


/**
 * Created by kaysaith on 06/08/2017.
 */

class HoneyImageModel(data: ArrayList<Any>?)  {
	
	val params = data!![0] as LinearLayout.LayoutParams
	val photo = data!![1].toString()
	val marginLeft = data!![2] as Int
	val marginTop = data!![3] as Int
	val isBigPhoto =  data!![4] as Boolean
	val width = data!![5] as Int
	val height = data!![6] as Int
}