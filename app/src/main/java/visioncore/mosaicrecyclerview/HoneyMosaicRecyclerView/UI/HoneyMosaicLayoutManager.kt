package visioncore.mosaicrecyclerview.HoneyMosaicLayout.View

import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.LayoutParams
import android.util.SparseArray
import visioncore.mosaicrecyclerview.HoneyMosaicLayout.Utils.getScreenHeight

/**
 * Created by kaysaith on 11/08/2017.
 */
class HoneyMosaicLayoutManager : RecyclerView.LayoutManager() {
	
	/** 用于保存item的位置信息 */
	private val allItemRect = SparseArray<Rect>()
	private val singlePageCount = 10
	private var verticalScrollOffset: Int = 0
	private var totalHeight = 0
	private var currentPageCount = 20
	private var nextPageCount = 0
	private var preLoadPhotoWhenVerticalOffset = getScreenHeight() * 0.2
	
	override fun generateDefaultLayoutParams(): LayoutParams {
		return LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
	}
	
	override fun onLayoutChildren(recycler: RecyclerView.Recycler, state: RecyclerView.State) {
		if (itemCount <= 0 || state.isPreLayout || childCount > 0) {
			return
		}
		// 先把所有的View先从RecyclerView中detach掉，然后标记为"Scrap"状态，表示这些View处于可被重用状态(非显示中)。
		detachAndScrapAttachedViews(recycler)
		if (currentPageCount > itemCount - 1) {
			currentPageCount = itemCount - 1
		}
		/* 这个方法主要用于计算并保存每个ItemView的位置 */
		calculateChildrenSite(recycler, 0, currentPageCount - 1)
	}
	
	private fun calculateChildrenSite(recycler: RecyclerView.Recycler, startIndex: Int, finalIndex: Int) {
		
		for (index in startIndex .. finalIndex) {
			val view = recycler.getViewForPosition(index)
			addView(view)
			measureChildWithMargins(view, 0, 0)
			
			val width = getDecoratedMeasuredWidth(view)
			val height = getDecoratedMeasuredHeight(view)
			
			var tempRect = allItemRect.get(index)
			if (tempRect == null) {
				tempRect = Rect()
			}
			
			(view as HoneyImageView).let {
				val marginLeft = it.model?.marginLeft ?: 0
				val marginTop = it.model?.marginTop ?: 0
				tempRect.set(marginLeft, marginTop, width + marginLeft, marginTop + height)
				layoutDecorated(view,marginLeft, marginTop, width + marginLeft, marginTop + height)
				totalHeight = marginTop + height
				// 保存ItemView的位置信息
				allItemRect.put(index, tempRect)
			}
			
		}
	}
	
	private fun recycleAndFillView(recycler: RecyclerView.Recycler, state: RecyclerView.State) {
		
		if (itemCount <= 0 || state.isPreLayout ) {
			return
		}
		// 当前scroll offset状态下的显示区域
		val screenRect = Rect(0, 0, horizontalSpace, verticalSpace)
		val offsetRect = Rect(0, verticalScrollOffset, horizontalSpace, verticalScrollOffset + verticalSpace)
		// 将滑出屏幕的Items回收到Recycle缓存中
		val childRect = Rect()
		for (index in childCount - 1 downTo  0) {
			// 这个方法获取的是RecyclerView中的View，注意区别Recycler中的View
			val child = getChildAt(index)
			childRect.left = getDecoratedLeft(child)
			childRect.top = getDecoratedTop(child)
			childRect.right = getDecoratedRight(child)
			childRect.bottom = getDecoratedBottom(child)
			if (!Rect.intersects(screenRect, childRect)) {
				// 移除并回收掉滑出屏幕的View
				detachAndScrapView(child, recycler)
			}
		}
		
		for (index in 0 .. currentPageCount - 1) {
			// 判断ItemView的位置和当前显区域是否重合
			if (Rect.intersects(offsetRect, allItemRect.get(index))) {
				//获得Recycler中缓存的View
				val itemView = recycler.getViewForPosition(index)
				addView(itemView)
				measureChildWithMargins(itemView, 0, 0)
				// 取出先前存好的ItemView的位置矩形
				val rect = allItemRect.get(index)
				layoutDecoratedWithMargins(itemView, rect.left, rect.top - verticalScrollOffset, rect.right, rect.bottom - verticalScrollOffset)
			}
		}
		System.out.println(childCount)
	}
	
	override fun canScrollVertically(): Boolean {
		return true
	}
	
	override fun scrollVerticallyBy(dy: Int, recycler: RecyclerView.Recycler, state: RecyclerView.State): Int {
		// 每次滑动时先释放掉所有的View，因为后面调用recycleAndFillView()时会重新addView()。
		detachAndScrapAttachedViews(recycler)
		// 实际要滑动的距离
		var travel = dy
		// 如果滑动到最顶部
		if (verticalScrollOffset + dy < 0) {
			travel = -verticalScrollOffset
		} else if (verticalScrollOffset + dy > totalHeight - verticalSpace) {
			// 如果滑动到最底部
			travel = totalHeight - verticalSpace - verticalScrollOffset
		}
		// 调用该方法通知view在y方向上移动指定距离
		offsetChildrenVertical(-travel)
		
		if (verticalScrollOffset > preLoadPhotoWhenVerticalOffset) {
			if (nextPageCount < itemCount)  {
				nextPageCount = currentPageCount + singlePageCount
				if (nextPageCount >= itemCount) {
					nextPageCount = itemCount
				}
				calculateChildrenSite(recycler, currentPageCount, nextPageCount - 1)
				currentPageCount = nextPageCount
			}
		}
		// 回收并显示View
		recycleAndFillView(recycler, state)
		// 将竖直方向的偏移量 + travel
		verticalScrollOffset += travel
		return travel
	}
	
	private val verticalSpace: Int
		// 计算RecyclerView的可用高度，除去上下Padding值
		get() {
			return height - paddingBottom - paddingTop
		}
	
	override fun canScrollHorizontally(): Boolean {
		return false
	}
	
	override fun scrollHorizontallyBy(dx: Int, recycler: RecyclerView.Recycler, state: RecyclerView.State): Int {
		// 在这个方法中处理水平滑动
		return super.scrollHorizontallyBy(dx, recycler, state)
	}
	
	val horizontalSpace: Int
		get() {
			return width - paddingLeft - paddingRight
		}
}



