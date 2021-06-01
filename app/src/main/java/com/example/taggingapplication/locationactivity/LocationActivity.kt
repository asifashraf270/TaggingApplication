package com.example.taggingapplication.locationactivity

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import com.example.taggingapplication.R
import com.example.taggingapplication.utilities.AppConstants
import com.example.taggingapplication.utilities.CustomPhotoAlbum

class LocationActivity : AppCompatActivity() {
    lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location)
        supportActionBar?.title = "Location Activity"
        sharedPreferences = getSharedPreferences(AppConstants.PREF_NAME, 0)
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window?.setLayout(
            CustomPhotoAlbum.getWidth(this) - 30,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        dialog.setCancelable(false)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.save_location_dialog)
        var location: EditText = dialog.findViewById(R.id.location)
        var cancelButton: Button = dialog.findViewById(R.id.cancel)
        var saveButton: Button = dialog.findViewById(R.id.save)
        cancelButton.setOnClickListener({
            dialog.dismiss()
        })
        saveButton.setOnClickListener({
            sharedPreferences.edit().putString(AppConstants.LOCATION, "11111,11111").commit()
            sharedPreferences.edit().putBoolean(AppConstants.ISLOCATIONSAVED, true).commit()
            var intent = Intent()
            setResult(Activity.RESULT_OK, intent)
            finish()

        })
        dialog.show()
    }
}