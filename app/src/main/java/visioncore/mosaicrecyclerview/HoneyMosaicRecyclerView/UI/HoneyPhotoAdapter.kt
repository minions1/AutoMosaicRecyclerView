package visioncore.mosaicrecyclerview.HoneyMosaicLayout.View

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import visioncore.mosaicrecyclerview.HoneyMosaicLayout.Model.HoneyImageModel
import kotlin.collections.ArrayList

/**
 * Created by kaysaith on 06/08/2017.
 */
class HoneyPhotoAdapter(
	
	val dataset: ArrayList<ArrayList<Any>>) : RecyclerView.Adapter<HoneyPhotoAdapter.ViewHolder>() {
	
	override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
		
		val viewHolder: ViewHolder?
		val image = HoneyImageView(parent?.context!!)
		viewHolder = ViewHolder(image)
		return viewHolder
	}
	
	override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
		
		val cell = holder!!.itemView as HoneyImageView
		
		cell.model = HoneyImageModel(dataset[position])
		
	}
	
	override fun getItemCount(): Int {
		return dataset.size
	}
	
	inner class ViewHolder(itemView: HoneyImageView?) : RecyclerView.ViewHolder(itemView)
	
}