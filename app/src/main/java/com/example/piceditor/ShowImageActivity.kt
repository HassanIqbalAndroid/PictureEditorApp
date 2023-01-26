package com.example.piceditor

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.SystemClock
import android.util.DisplayMetrics
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.io.File

class ShowImageActivity : AppCompatActivity(), View.OnClickListener {

    private var image_uri: String? = null
    private var img_show: ImageView? = null
    private var saved_file: File? = null
    private var density: Float = 0.toFloat()
    internal var D_height: Int = 0
    internal var D_width: Int = 0
    private var display: DisplayMetrics? = null

    private var mLastClickTime: Long = 0
    fun checkClick() {
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
            return
        }
        mLastClickTime = SystemClock.elapsedRealtime()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_show_image)
        Toast.makeText(this,"Image saved!",Toast.LENGTH_SHORT).show()
        image_uri = intent.getStringExtra("image_uri")

        saved_file = File(image_uri!!)
        img_show = findViewById<View>(R.id.img_show) as ImageView

        display = resources.displayMetrics
        density = resources.displayMetrics.density
        D_width = display!!.widthPixels
        D_height = (display!!.heightPixels.toFloat() - density * 150.0f).toInt()

        val layoutParams = RelativeLayout.LayoutParams(D_width, ViewGroup.LayoutParams.WRAP_CONTENT)
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE)
        img_show!!.layoutParams = layoutParams

        img_show!!.setImageURI(Uri.parse(image_uri))

        findViewById<View>(R.id.img_folder).setOnClickListener(this)

    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.img_folder -> {
                checkClick()
                var intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }


    override fun onBackPressed() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

}
