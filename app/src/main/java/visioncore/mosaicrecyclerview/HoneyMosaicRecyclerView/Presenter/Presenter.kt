package visioncore.mosaicrecyclerview.HoneyMosaicLayout.Presenter

import android.os.Build
import android.provider.MediaStore
import android.provider.MediaStore.Images.Media.*
import android.provider.MediaStore.MediaColumns.HEIGHT
import android.provider.MediaStore.MediaColumns.WIDTH
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import visioncore.mosaicrecyclerview.HoneyMosaicLayout.UI.HoneyMosaicFragment
import visioncore.mosaicrecyclerview.HoneyMosaicLayout.Utils.detectorShape
import visioncore.mosaicrecyclerview.HoneyMosaicLayout.Utils.getImageSize
import visioncore.mosaicrecyclerview.HoneyMosaicLayout.View.*
import visioncore.mosaicrecyclerview.HoneyMosaicLayout.View.Category.*
import visioncore.mosaicrecyclerview.HoneyMosaicLayout.View.ColumnCount.Full
import visioncore.mosaicrecyclerview.HoneyMosaicLayout.View.MosaicImageSize.UnitSize
import visioncore.mosaicrecyclerview.HoneyMosaicLayout.View.PhotoShapeType.*
import visioncore.mosaicrecyclerview.HoneyMosaicLayout.View.ShapeList.horizontalList
import visioncore.mosaicrecyclerview.HoneyMosaicLayout.View.ShapeList.horizontalListWithoutMiddle
import visioncore.mosaicrecyclerview.HoneyMosaicLayout.View.ShapeList.squareList
import visioncore.mosaicrecyclerview.HoneyMosaicLayout.View.ShapeList.squareListWithoutMiddle
import visioncore.mosaicrecyclerview.HoneyMosaicLayout.View.ShapeList.verticalList
import visioncore.mosaicrecyclerview.HoneyMosaicLayout.View.ShapeList.verticalListWithoutMiddle
import java.lang.Math.ceil
import java.util.*
import kotlin.collections.ArrayList

@Suppress("DEPRECATION", "CAST_NEVER_SUCCEEDS")
/**
 * Created by kaysaith on 06/08/2017.
 */
class DataPresenter(val fragment: HoneyMosaicFragment) {
	
	private val mosaicWall = ArrayList<Int>()
	
	fun loadPhotoAlbum() {
		doAsync {
			val listPath = ArrayList<List<Any>>()
			val mImageUri = EXTERNAL_CONTENT_URI
			val proImage = arrayOf(_ID, DATA, SIZE, DISPLAY_NAME, WIDTH, HEIGHT)
			val mCursor = fragment.activity.contentResolver.query(mImageUri, proImage, "$MIME_TYPE=? or $MIME_TYPE=?", arrayOf("image/jpeg", "image/png"), DATE_MODIFIED + " desc")
			if (mCursor != null) {
				while (mCursor.moveToNext()) {
					// 获取图片的路径
					val path = mCursor.getString(mCursor.getColumnIndex(DATA))
					val size = mCursor.getInt(mCursor.getColumnIndex(MediaStore.MediaColumns.SIZE) / 1024)
					var width = mCursor.getInt(mCursor.getColumnIndex(WIDTH))
					var height = mCursor.getInt(mCursor.getColumnIndex(HEIGHT))
					var photoCategory: Category?
					
					if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
						// Height Api Not Work In SDK 15
						photoCategory = detectorShape(width, height)
					} else {
						photoCategory = detectorShape(getImageSize(path)[0], getImageSize(path)[1])
						width = getImageSize(path)[0]
						height = getImageSize(path)[1]
					}
					
					listPath.add(listOf(path, photoCategory,size, width, height))
				}
				mCursor.close()
			}
			
			//更新界面
			uiThread {
				
				mosaicWall += 0 .. Full * 100
				
				val data = arrayListOf<ArrayList<Any>>()
				val photoPath = 0
				val photoCategory = 1
				val photoSize = 2
				val photoWidth = 3
				val photoHeight = 4
				
				for (index in 0 .. listPath.count() - 1) {
					
					val theMostTopAndMostLeftPosition = mosaicWall[0]
					var photoShape: PhotoShapeType? = null
					val shape = listPath[index][photoCategory] as Category
					
					val condition = distanceBetweenRowStartToEnd(theMostTopAndMostLeftPosition, mosaicWall)
					
					when (condition) {
						Full -> when (shape) {
							Square -> photoShape = squareList[Random().nextInt(squareList.size)]
							Horizontal -> photoShape = horizontalList[Random().nextInt(horizontalList.size)]
							Vertical -> photoShape = verticalList[Random().nextInt(verticalList.size)]
						}
						ColumnCount.Big -> when (shape) {
							Square -> photoShape = squareListWithoutMiddle[Random().nextInt(squareListWithoutMiddle.size)]
							Horizontal -> photoShape = horizontalListWithoutMiddle[Random().nextInt(horizontalListWithoutMiddle.size)]
							Vertical -> photoShape = verticalListWithoutMiddle[Random().nextInt(verticalListWithoutMiddle.size)]
						}
						ColumnCount.Middle -> when (shape) {
							Square -> photoShape = MiddleSquare
							Horizontal -> photoShape = MiddleHorizontal
							Vertical -> photoShape = MiddleVertical
						}
						ColumnCount.Small -> when (shape) {
							Square -> photoShape = SmallSquare
							Horizontal -> photoShape = SmallHorizontal
							Vertical -> photoShape = SmallVertical
						}
					}
					
					if (mosaicWall.size < Full * 10) {
						val currentMaxInt = mosaicWall[mosaicWall.size - 1]
						mosaicWall += currentMaxInt + 1 .. currentMaxInt + Full * 100
					}
					
					val photoArrayID = cellIDList(photoShape!!, mosaicWall[0])
					mosaicWall.removeAll(cellIDList(photoShape, mosaicWall[0]))
					
					val params = shapeLayoutParams(photoShape)
					val photoFirstPosition = photoArrayID[0]
					val relativeInt = ceil(photoFirstPosition.toDouble() / Full)
					val absoluteInt = photoFirstPosition.toDouble() / Full
					val marginLeft = photoFirstPosition	% Full * UnitSize
					val marginTop = if (relativeInt == absoluteInt) relativeInt.toInt() * UnitSize
													else (relativeInt - 1).toInt() * UnitSize
					val isBigPhoto: Boolean = (listPath[index][photoSize] as Int) > 1024
					val photoInfoArray = arrayListOf<Any>().apply {
						add(params) // 布局尺寸 LayoutParams
						add(listPath[index][photoPath]) // 图片String
						add(marginLeft) // 距离左边的位置
						add(marginTop)  // 距离右边的位置
						add(isBigPhoto)
						add(listPath[index][photoWidth])
						add(listPath[index][photoHeight])
					}
					
					data.add(photoInfoArray)
				}
				fragment.data = data
			}
		}
		
	}
}