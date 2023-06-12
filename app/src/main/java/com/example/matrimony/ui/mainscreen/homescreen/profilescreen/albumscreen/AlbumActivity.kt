package com.example.matrimony.ui.mainscreen.homescreen.profilescreen.albumscreen

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.graphics.Point
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Display
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.exifinterface.media.ExifInterface
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.matrimony.R
import com.example.matrimony.TAG
import com.example.matrimony.adapters.AlbumAdapter
import com.example.matrimony.databinding.ActivityAlbumBinding
import com.example.matrimony.db.entities.Album
import com.example.matrimony.ui.mainscreen.UserProfileViewModel
import com.example.matrimony.utils.CURRENT_USER_ID
import com.example.matrimony.utils.MY_SHARED_PREFERENCES
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class AlbumActivity : AppCompatActivity(), OpenCameraDialogFragment.Companion.ButtonClickListener {

    private val albumViewModel by viewModels<AlbumViewModel>()
    private val userProfileViewModel by viewModels<UserProfileViewModel>()

    lateinit var binding: ActivityAlbumBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_album)

        Log.i(TAG, "loaded ${albumViewModel.loaded}")
        albumViewModel.loaded = true

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        val sharedPref =
            getSharedPreferences(MY_SHARED_PREFERENCES, Context.MODE_PRIVATE) ?: return

        userProfileViewModel.userId = sharedPref.getInt(CURRENT_USER_ID, -1)

        loadAlbum()
    }

    private val albumList = mutableListOf<Album>()
    private var adapter: AlbumAdapter? = null

    private fun loadAlbum() {
        Log.i(TAG, "album act load album")

        val albumRecyclerView = binding.rvAlbum
        albumRecyclerView.layoutManager = GridLayoutManager(this, 1)
        adapter = AlbumAdapter(this,userProfileViewModel, albumViewModel, addImage,::getPermission,::checkForPermission)
        albumRecyclerView.adapter = adapter


        lifecycleScope.launch {
            albumViewModel.getUserAlbum(userProfileViewModel.userId)
                .observe(this@AlbumActivity) { album ->
                    albumList.clear()
                    album?.forEach {
                        if (it != null)
                            if (it.isProfilePic)
                                albumList.add(0, it)
                            else
                                albumList.add(it)
                    }

                    adapter?.setList(albumList)
                }

        }

        Log.i(TAG, "Inside AlbumAct getProfilePic")
        registerForContextMenu(albumRecyclerView)
    }

    private val addImage = {
        val dialog = OpenCameraDialogFragment()
        dialog.show(supportFragmentManager, null)

    }

    override fun onButtonClick(clickedItem: String) {

        getPermission()
        if (checkForPermission()) {

            when (clickedItem) {
                "camera" -> {
//                getPermission()
                    Log.i(TAG, "Check Permission ${checkForPermission()}")
//                if (checkForPermission())
                    launchCamera()

                }
                "gallery" -> {
                    startGalleryIntent()
                }
            }
        }
    }

    private fun startGalleryIntent() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, REQUEST_IMAGE_CHOOSE)
    }

    private var imageUri: Uri? = null

    private fun launchCamera() {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "new_picture")
        values.put(MediaStore.Images.Media.DESCRIPTION, "from_camera")
        imageUri = this.contentResolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values
        )
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)

        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)

    }

    private fun getRealPathFromURI(contentUri: Uri?): String? {
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        val cursor: Cursor? = contentResolver.query(contentUri!!, proj, null, null, null)
        val columnIndex: Int? = cursor?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor?.moveToFirst()
        return cursor?.getString(columnIndex!!).also { cursor?.close() }
    }


    private fun loadNewImage(filePath: String) {

        Log.i(TAG, "load image: $filePath")
        var mBitmap = BitmapFactory.decodeFile(filePath)
        Log.d("Images New ", mBitmap.toString())
        val display: Display = this.windowManager.getDefaultDisplay()
        val size = Point()
        display.getSize(size)
        val width: Int = size.x
        val height: Int = size.y
        Log.i(TAG, "bitmap: " + mBitmap!!.getWidth() + " " + mBitmap!!.getHeight())
        val maxP = Math.max(mBitmap.getWidth(), mBitmap!!.getHeight())
        val scale1280 = maxP.toFloat() / 1280
        Log.i(TAG, "scaled: " + scale1280 + " - " + 1 / scale1280)
//        mImageView!!.maxZoom = width * 2 / 1280f
        mBitmap = Bitmap.createScaledBitmap(
            mBitmap,
            (mBitmap.width / scale1280).toInt(),
            (mBitmap.height / scale1280).toInt(),
            true
        )
        val exif = ExifInterface(filePath)
        val orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED)

        val matrix = Matrix()
        when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> matrix.setRotate(90f)
            ExifInterface.ORIENTATION_ROTATE_180 -> matrix.setRotate(180f)
            ExifInterface.ORIENTATION_ROTATE_270 -> matrix.setRotate(270f)
            else -> return
        }
        mBitmap = Bitmap.createBitmap(mBitmap, 0, 0, mBitmap.width, mBitmap.height, matrix, true)



        lifecycleScope.launch {
            val album = (Album(0, userProfileViewModel.userId, mBitmap, false))
            albumViewModel.addAlbum(album)
            albumList.add(album)
            adapter?.notifyDataSetChanged()
        }
//        mImageView!!.setImageBitmap(mBitmap)
    }

    private fun deleteImage(uri: Uri?) {
        if (uri != null) {
            val contentResolver = this.contentResolver
            contentResolver.delete(uri, null, null)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_CAPTURE) {
                val path = getRealPathFromURI(imageUri)!!
                loadNewImage(path)
            }
            else if(requestCode == REQUEST_IMAGE_CHOOSE){
                imageUri=data?.data
                imageUri?.let {
                    val filePath=getRealPathFromURI(imageUri)
                    filePath?.let {
                        loadNewImage(filePath)
                    }
                }
            }
        } else {
            deleteImage(imageUri)
            imageUri = null
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }


    private fun getPermission() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_MEDIA_IMAGES
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.READ_MEDIA_IMAGES),
                    REQUEST_PERMISSION
                );

            }
        } else {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    REQUEST_PERMISSION
                );

            }
        }
    }

    private fun checkForPermission(): Boolean {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            return ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_MEDIA_IMAGES
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            return ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_PERMISSION) {
            if (grantResults.size == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                Intent(this, AddPostActivity::class.java).apply {
//                    startActivity(this)
//                }
                //return
            } else {
                openPermissionNeededDialog()
            }
        }
    }

    private fun openPermissionNeededDialog() {
        val mediaPermissionDialogFragment = MediaPermissionDialogFragment()
        mediaPermissionDialogFragment.show(supportFragmentManager, "permissiondialog")
    }


    companion object {
        const val REQUEST_IMAGE_CAPTURE = 1
        const val REQUEST_IMAGE_CHOOSE = 2
        const val REQUEST_PERMISSION = 3
    }
}