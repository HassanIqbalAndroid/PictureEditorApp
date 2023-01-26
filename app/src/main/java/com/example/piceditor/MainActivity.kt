package com.example.piceditor

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.*
import android.provider.MediaStore
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.example.piceditor.splash.Splash.Companion.isFromSplash
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private var PICK_IMAGE: Int = 111
    var CAMERA_REQUEST: Int = 123

    private lateinit var gallary_images: ArrayList<String>
//    lateinit var adapter: ImageAdapter

    companion object {
        var isFromSaved: Boolean = true
    }

    private fun ImagesPath(): ArrayList<String> {
        val cursor: Cursor
        val listOfAllImages = ArrayList<String>()
        var absolutePathOfImage: String? = null
        val uri: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI

        val projection = arrayOf(MediaStore.MediaColumns.DATA)

        cursor =
            contentResolver.query(
                uri,
                projection,
                null,
                null,
                MediaStore.Images.Media.DATE_TAKEN + " DESC"
            )!!

        val column_index_data: Int = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)

        while (cursor.moveToNext()) {
            absolutePathOfImage = cursor.getString(column_index_data)
            listOfAllImages.add(absolutePathOfImage)
        }
        return listOfAllImages
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_main)


        if (isFromSplash) {
            //    llProgressBar.visibility = View.VISIBLE
            window.setFlags(
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
            )
            Handler().postDelayed(object : Runnable {
                override fun run() {
                    isFromSplash = false
                    //     llProgressBar.visibility = View.GONE
                    window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                }
            }, 1000)
        }

        if (Build.VERSION.SDK_INT >= 23) {
            if (chechkPermission()) {
                setAdapter()
            } else {
                requestPermission()
            }
        } else {
            setAdapter()
        }
        editore.setOnClickListener(this)
        collage.setOnClickListener(this)
        camera.setOnClickListener(this)
    }

    private fun setAdapter() {

        gallary_images = ArrayList<String>()
        gallary_images.clear()

        gallary_images = ImagesPath()
//        adapter = ImageAdapter()
    }

    private fun chechkPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
            ),
            100
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == 100) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.e("Permission", "Granted")
                setAdapter()
            } else {
                Log.e("Permission", "Denied")

                requestPermission()
            }
        }
    }

    private var mLastClickTime: Long = 0
    private fun checkClick() {
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
            return
        }
        mLastClickTime = SystemClock.elapsedRealtime()
    }

    private var mCapturedImageUri: Uri? = null

    override fun onClick(v: View?) {

        when (v!!.id) {
            R.id.editore -> {
                checkClick()

                val intent = Intent()
                intent.type = "image/*"
                intent.action = Intent.ACTION_PICK
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE)
            }

            R.id.collage -> {
                checkClick()
                val intent = Intent(this, SelectImageActivity::class.java)
                startActivity(intent)
            }
            R.id.camera -> {

                checkClick()
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                if (intent.resolveActivity(packageManager) != null) {
                    var photofile: File? = null

                    try {
                        val capturedPath = "image_" + System.currentTimeMillis() + ".jpg"
                        photofile = File(
                            Environment.getExternalStorageDirectory().absolutePath + "/DCIM",
                            capturedPath
                        )
                        photofile.parentFile?.mkdirs()
                        mCapturedImageUri = Uri.fromFile(photofile)
                    } catch (_: java.lang.Exception) {

                    }
                    if (photofile != null) {
                        mCapturedImageUri = FileProvider.getUriForFile(
                            this,
                            BuildConfig.APPLICATION_ID + ".provider",
                            photofile
                        )
                    }
                }

                intent.putExtra(MediaStore.EXTRA_OUTPUT, mCapturedImageUri)
                startActivityForResult(intent, CAMERA_REQUEST)

            }
        }
    }
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//
//        if (resultCode == Activity.RESULT_OK && requestCode == PICK_IMAGE) {
//            if (data != null) {
//                try {
//                    val uri: Uri = data.data!!
//
//                    val intent = Intent(this, ImageEditActivity::class.java)
//                    intent.putExtra("image_uri", uri.toString())
//                    startActivity(intent)
//
//
//                } catch (e: NullPointerException) {
//                    e.printStackTrace()
//                }
//            }
//        } else if (resultCode == Activity.RESULT_OK && requestCode == CAMERA_REQUEST) {
//            if (mCapturedImageUri != null) {
//                var intent = Intent(this, ImageEditActivity::class.java)
//                intent.putExtra("image_uri", mCapturedImageUri.toString())
//                startActivity(intent)
//            }
//        }
//    }
//
//    inner class ImageAdapter : PagerAdapter() {
//
//        override fun instantiateItem(container: ViewGroup, position: Int): Any {
//            var view: View =
//                LayoutInflater.from(this@MainActivity)
//                    .inflate(R.layout.item_slider, container, false)
//            var img_slider: ImageView = view.findViewById(R.id.img_slider) as ImageView
//
//            /*     Picasso.with(this@MainActivity)
//                     .load("http://i.imgur.com/DvpvklR.png")
//                     .fit()
//                     .into(img_slider)
//     */
//
//            Glide.with(this@MainActivity)
//                .asBitmap()
//                .apply(RequestOptions.circleCropTransform())
//                .load(gallary_images[position])
//                .into(img_slider)
//
//            (container as ViewPager).addView(view)
//
//            img_slider.setOnClickListener(object : View.OnClickListener {
//                override fun onClick(v: View?) {
//                    checkClick()
//                    var uri = Uri.fromFile(File(gallary_images[position]))
//                    var intent = Intent(this@MainActivity, ImageEditActivity::class.java)
//                    intent.putExtra("image_uri", uri.toString())
//                    startActivity(intent)
//                }
//            })
//            return view
//        }
//
//        override fun isViewFromObject(view: View, `object`: Any): Boolean {
//            return (view == `object` as View)
//        }
//
//        override fun getCount(): Int {
//            return gallary_images.size
//        }
//
//        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
//            (container as ViewPager).removeView(`object` as View)
//        }
//    }
    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_HOME)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }
}
