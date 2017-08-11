package visioncore.mosaicrecyclerview.HoneyMosaicLayout.View

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.MotionEvent
import android.view.ViewGroup
import org.jetbrains.anko.matchParent

/**
 * Created by kaysaith on 06/08/2017.
 */

class BaseRecyclerView(context: Context) : RecyclerView(context) {
	
	init {
		layoutManager = HoneyMosaicLayoutManager()
		layoutParams = ViewGroup.LayoutParams(matchParent, matchParent)
	}
	
	override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
		if (event.action == MotionEvent.ACTION_DOWN && this.scrollState == RecyclerView.SCROLL_STATE_IDLE) {
			this.stopScroll()
		}
		return super.onInterceptTouchEvent(event)
	}
	
}

