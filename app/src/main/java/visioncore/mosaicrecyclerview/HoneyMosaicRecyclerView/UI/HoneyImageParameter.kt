package visioncore.mosaicrecyclerview.HoneyMosaicLayout.View

import android.widget.LinearLayout.LayoutParams
import org.jetbrains.anko.matchParent
import visioncore.mosaicrecyclerview.HoneyMosaicLayout.Utils.getScreenWidth
import visioncore.mosaicrecyclerview.HoneyMosaicLayout.View.ColumnCount.Big
import visioncore.mosaicrecyclerview.HoneyMosaicLayout.View.ColumnCount.Full
import visioncore.mosaicrecyclerview.HoneyMosaicLayout.View.ColumnCount.Middle
import visioncore.mosaicrecyclerview.HoneyMosaicLayout.View.ColumnCount.Small
import visioncore.mosaicrecyclerview.HoneyMosaicLayout.View.MosaicImageSize.UnitSize
import visioncore.mosaicrecyclerview.HoneyMosaicLayout.View.PhotoShapeType.*

/**
 * Created by kaysaith on 07/08/2017.
 */

enum class PhotoShapeType {
	SmallSquare,
	MiddleSquare,
	BigSquare,
	SmallHorizontal,
	MiddleHorizontal,
	BigHorizontal,
	SmallVertical,
	MiddleVertical,
	BigVertical
}

enum class Category {
	Square,
	Vertical,
	Horizontal
}

object ColumnCount {
	val Small = 4
	val Middle = 6
	val Big = 8
	val Full = 12
}

object RowCount {
	val SmallHorizontal = 3
	val MiddleHorizontal = 4
	val BigHorizontal = 6
	val SmallVertical = 6
	val MiddleVertical = 9
	val BigVertical = 12
}

object MosaicImageSize {
	val UnitSize = getScreenWidth() / Full
}

object ShapeList {
	val squareList = listOf(SmallSquare, MiddleSquare, BigSquare)
	val horizontalList = listOf(SmallHorizontal, MiddleHorizontal, BigHorizontal)
	val verticalList = listOf(SmallVertical, MiddleVertical, BigVertical)
	
	val squareListWithoutMiddle = listOf(SmallSquare, BigSquare)
	val horizontalListWithoutMiddle = listOf(SmallHorizontal, BigHorizontal)
	val verticalListWithoutMiddle = listOf(SmallVertical, BigVertical)
}

fun cellIDList(type: PhotoShapeType, startWallInt: Int): ArrayList<Int> {
	
	val willRemoveList = ArrayList<Int>()
	var rowCount = 0
	var columnCount = 0
	
	when (type) {
		SmallSquare -> {
			rowCount = Small
			columnCount = Small
		}
		MiddleSquare -> {
			rowCount = Middle
			columnCount = Middle
		}
		BigSquare -> {
			rowCount = Big
			columnCount = Big
		}
	
	// Horizontal
		
		SmallHorizontal -> {
			rowCount = RowCount.SmallHorizontal
			columnCount = Small
		}
		MiddleHorizontal -> {
			rowCount = RowCount.MiddleHorizontal
			columnCount = Middle
		}
		BigHorizontal -> {
			rowCount = RowCount.BigHorizontal
			columnCount = Big
		}
	
	// Vertical
		
		SmallVertical -> {
			rowCount = RowCount.SmallVertical
			columnCount = Small
		}
		
		MiddleVertical -> {
			rowCount = RowCount.MiddleVertical
			columnCount = Middle
		}
		
		BigVertical -> {
			rowCount = RowCount.BigVertical
			columnCount = Big
		}
	}
	
	for (row in 0 .. rowCount - 1) {
		(startWallInt .. startWallInt + columnCount - 1).mapTo(willRemoveList) { it + row * Full }
	}
	
	return willRemoveList
}

fun distanceBetweenRowStartToEnd(startInt: Int, wallInt: ArrayList<Int>): Int {
	var distance = 0
	if (startInt + 11 == wallInt[11] && startInt % Full == 0) {
		distance = Full
		return distance
	} else if (startInt + 7 == wallInt[7] && wallInt[7] % Full > 3) {
		distance = Big
		return distance
	} else if (startInt + 5 == wallInt[5] && wallInt[5] % Full > 3) {
		distance = Middle
		return distance
	} else if (startInt + 3 == wallInt[3] && wallInt[3] % Full > 2) {
		distance = Small
		return distance
	}
	return distance
}

fun shapeLayoutParams(type: PhotoShapeType): LayoutParams {
	var Params = LayoutParams(matchParent, matchParent)
	when (type) {
	//Square
		SmallSquare -> Params = LayoutParams(UnitSize * Small, UnitSize * Small)
		MiddleSquare -> Params = LayoutParams(UnitSize * Middle, UnitSize * Middle)
		BigSquare -> Params = LayoutParams(UnitSize * Big, UnitSize * Big)
	//Vertical
		SmallHorizontal -> Params = LayoutParams(UnitSize * Small, UnitSize * RowCount.SmallHorizontal)
		MiddleHorizontal -> Params = LayoutParams(UnitSize * Middle, UnitSize * RowCount.MiddleHorizontal)
		BigHorizontal -> Params = LayoutParams(UnitSize * Big, UnitSize * RowCount.BigHorizontal)
	//Vertical
		SmallVertical -> Params = LayoutParams(UnitSize * Small, UnitSize * RowCount.SmallVertical)
		MiddleVertical -> Params = LayoutParams(UnitSize * Middle, UnitSize * RowCount.MiddleVertical)
		BigVertical -> Params = LayoutParams(UnitSize * Big, UnitSize * RowCount.BigVertical)
	}
	return Params
}

