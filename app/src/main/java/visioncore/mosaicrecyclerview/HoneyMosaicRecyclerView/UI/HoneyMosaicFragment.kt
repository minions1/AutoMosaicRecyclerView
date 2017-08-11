package visioncore.mosaicrecyclerview.HoneyMosaicLayout.UI

import android.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.jetbrains.anko.UI
import org.jetbrains.anko.matchParent
import visioncore.mosaicrecyclerview.HoneyMosaicLayout.Presenter.DataPresenter
import visioncore.mosaicrecyclerview.HoneyMosaicLayout.Utils.observing
import visioncore.mosaicrecyclerview.HoneyMosaicLayout.View.BaseRecyclerView
import visioncore.mosaicrecyclerview.HoneyMosaicLayout.View.HoneyMosaicLayoutManager
import visioncore.mosaicrecyclerview.HoneyMosaicLayout.View.HoneyPhotoAdapter

/**
 * Created by kaysaith on 06/08/2017.
 */
class HoneyMosaicFragment : Fragment() {
	
	var data: ArrayList<ArrayList<Any>>? by observing(
		null,
		didSet = {
			albumAdapter = HoneyPhotoAdapter(data!!)
			layoutList.adapter = albumAdapter
		}
	)
	
	private lateinit var albumAdapter: HoneyPhotoAdapter
	private val presenter = DataPresenter(this)
	private lateinit var layoutList: BaseRecyclerView
	
	override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
		return UI {
			layoutList = BaseRecyclerView(context)
			layoutList.layoutManager = HoneyMosaicLayoutManager()
			presenter.loadPhotoAlbum()
			addView(layoutList, ViewGroup.LayoutParams(matchParent, matchParent))
		}.view
	}
}