package visioncore.mosaicrecyclerview.HoneyMosaicLayout.View

import android.content.Context
import android.net.Uri
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import com.bumptech.glide.Glide
import org.jetbrains.anko.*
import visioncore.mosaicrecyclerview.HoneyMosaicLayout.Model.HoneyImageModel
import visioncore.mosaicrecyclerview.HoneyMosaicLayout.Utils.UIPx
import visioncore.mosaicrecyclerview.HoneyMosaicLayout.Utils.observing
import java.io.File

/**
 * Created by kaysaith on 07/08/2017.
 */

class HoneyImageView(context: Context) : RelativeLayout(context) {
	
	var model: HoneyImageModel? by observing(
		null,
		didSet = {
			layoutParams = model?.params
			Glide
				.with(context)
				.load(Uri.fromFile(File(model?.photo)))
				.into(photo)
			
		}
	)
	
	var photo: ImageView = imageView {
		padding = 2.UIPx()
		layoutParams = ViewGroup.LayoutParams(matchParent, matchParent)
		scaleType = ImageView.ScaleType.CENTER_CROP
	}

}


