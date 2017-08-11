package visioncore.mosaicrecyclerview.HoneyMosaicRecyclerView.UI

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.verticalLayout
import visioncore.mosaicrecyclerview.HoneyMosaicLayout.UI.HoneyMosaicFragment

class MainActivity : AppCompatActivity() {
	
	val mosaicFragment = HoneyMosaicFragment()
	
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		verticalLayout {
			id = MainLayoutID
			backgroundColor = Color.BLACK
			
			if(verifyStoragePermissions(this@MainActivity)) {
				fragmentManager.beginTransaction().add(MainLayoutID, mosaicFragment).commit()
			}
		}
	}
	
	companion object {
		private val MainLayoutID = 100
	}
}

private val PERMISSION_EXTERNAL_STORAGE = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
private val REQUEST_EXTERNAL_STORAGE = 100

private fun verifyStoragePermissions(activity: Activity): Boolean {
	val permissionWrite = ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE)
	if (permissionWrite != PackageManager.PERMISSION_GRANTED) {
		ActivityCompat.requestPermissions(activity, PERMISSION_EXTERNAL_STORAGE, REQUEST_EXTERNAL_STORAGE)
		return false
	}
	return true
}






