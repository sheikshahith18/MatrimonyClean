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
import android.media.ExifInterface
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
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
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


@AndroidEntryPoint
class AlbumActivity1 : AppCompatActivity(), OpenCameraDialogFragment.Companion.ButtonClickListener {

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


        val sharedPref = getSharedPreferences(MY_SHARED_PREFERENCES, Context.MODE_PRIVATE) ?: return

        userProfileViewModel.userId = sharedPref.getInt(CURRENT_USER_ID, -1)

        loadAlbum()
    }

    private val albumList = mutableListOf<Album>()
    private var adapter: AlbumAdapter? = null
    private fun loadAlbum() {

        val albumRecyclerView = binding.rvAlbum
        albumRecyclerView.layoutManager = GridLayoutManager(this, 1)
        adapter = AlbumAdapter(
            this,userProfileViewModel, albumViewModel, addImage, ::getPermission, ::checkForPermission
        )
        albumRecyclerView.adapter = adapter

        lifecycleScope.launch {
            albumViewModel.getUserAlbum(userProfileViewModel.userId)
                .observe(this@AlbumActivity1) { album ->

                    albumList.clear()
                    album?.forEach {
                        if (it != null) if (it.isProfilePic) albumList.add(0, it)
                        else albumList.add(it)
                    }

                    adapter?.setList(albumList)
                }

        }
    }

    private val addImage = {
        val dialog = OpenCameraDialogFragment()
        dialog.show(supportFragmentManager, null)
    }

    private fun getPermission(): Boolean {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(
                    this, Manifest.permission.READ_MEDIA_IMAGES
                ) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                    this, Manifest.permission.CAMERA
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.READ_MEDIA_IMAGES, Manifest.permission.CAMERA),
                    REQUEST_PERMISSION
                );

            }
        } else {
            if (ActivityCompat.checkSelfPermission(
                    this, Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                    this, Manifest.permission.CAMERA
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA),
                    REQUEST_PERMISSION
                );

            }
        }
        createImageFile()

        return checkForPermission()
    }

    lateinit var currentPhotoPath: String

    @Throws(IOException::class)
    private fun createImageFile(): File {

        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDir
        ).apply {
            currentPhotoPath = absolutePath
        }
    }

    private fun checkForPermission(): Boolean {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            return ActivityCompat.checkSelfPermission(
                this, Manifest.permission.READ_MEDIA_IMAGES
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            return ActivityCompat.checkSelfPermission(
                this, Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        }
    }

    private fun startGalleryIntent() {

        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, REQUEST_IMAGE_CHOOSE)
    }

    private var imageUri: Uri? = null
    override fun onButtonClick(clickedItem: String) {
//        getPermission()
        if (getPermission()) {

            when (clickedItem) {
                "camera" -> {
                    Log.i(TAG, "Check Permission ${checkForPermission()}")
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
                        val values = ContentValues()
                        values.put(MediaStore.Images.Media.TITLE, "New Picture")
                        values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera")
                        albumViewModel.imageUri = contentResolver.insert(
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values
                        )
                        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, albumViewModel.imageUri)
                        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)
                    } else {
                        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE + 10)

                    }
                }
                "gallery" -> {
                    startGalleryIntent()
                }
            }
        } else {
//            openPermissionNeededDialog()
        }
    }

    private fun getRealPathFromURI(contentUri: Uri?): String? {
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        val cursor: Cursor? = contentResolver.query(contentUri!!, proj, null, null, null)
        val columnIndex: Int? = cursor?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor?.moveToFirst()
        return cursor?.getString(columnIndex!!).also { cursor?.close() }
    }

    private fun deleteImage(uri: Uri?) {
        if (uri != null) {
//            val contentResolver = .contentResolver
            contentResolver.delete(uri, null, null)
        }
    }

    private fun rotateImage(source: Bitmap, angle: Float): Bitmap? {
        val matrix = Matrix()
        matrix.postRotate(angle)
        return Bitmap.createBitmap(
            source, 0, 0, source.width, source.height, matrix, true
        )
    }

    private fun loadImage(path: String?) {
        if (path == null) return
        val exif = ExifInterface(path)
        val orientation =
            exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED)
        var rotatedBitmap: Bitmap? = null
        val bitmap = getScaledBitmap(path, 800, 800) ?: return
        rotatedBitmap = when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> {
                rotateImage(bitmap, 90F)
            }
            ExifInterface.ORIENTATION_ROTATE_180 -> {
                rotateImage(bitmap, 180F)
            }
            ExifInterface.ORIENTATION_ROTATE_270 -> {
                rotateImage(bitmap, 270F)
            }
            else -> {
                bitmap
            }
        }
        lifecycleScope.launch {
            val album = (Album(0, userProfileViewModel.userId, rotatedBitmap, false))
            albumViewModel.addAlbum(album)
            albumList.add(album)
            adapter?.notifyDataSetChanged()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.i(TAG, "OnAct REs")
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_CAPTURE) {
                try {
                    val path = getRealPathFromURI(albumViewModel.imageUri)!!
                    loadImage(path)

                } catch (e: java.lang.Exception) {
                    Log.i(TAG, "error ${e.stackTrace}")
                }

            } else if (requestCode == REQUEST_IMAGE_CHOOSE) {

                try {
                    val selectedImage = data?.data
                    val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
                    val cursor =
                        contentResolver.query(selectedImage!!, filePathColumn, null, null, null)
                    cursor?.moveToFirst()
                    val columnIndex = cursor?.getColumnIndex(filePathColumn[0])
                    val picturePath = cursor?.getString(columnIndex!!)
                    Log.i(TAG, "picturepath = $picturePath")
                    cursor?.close()
                    loadImage(picturePath)
//                    val bitmap = getScaledBitmap(picturePath, 800, 800)
//                    lifecycleScope.launch {
//                        val album = (Album(0, userProfileViewModel.userId, bitmap, false))
//                        albumViewModel.addAlbum(album)
//                        albumList.add(album)
//                        adapter?.notifyDataSetChanged()
//                    }
                } catch (e: java.lang.Exception) {
                    Log.i(TAG, "Exception occurred ${e.stackTrace}")
                }
            } else if (requestCode == REQUEST_IMAGE_CAPTURE + 10) {
//                data?.extras
                // Set the image in imageview for display
//                adapter?.addAlbum(userProfileViewModel.userId,photo!!)
//                clickImageId.setImageBitmap(photo)
                val image = data?.extras!!["data"] as Bitmap?
                loadImage2(image!!)
            }
        }
    }


    private fun loadImage2(image: Bitmap) {
        lifecycleScope.launch {
            val album = (Album(0, userProfileViewModel.userId, image, false))
            albumViewModel.addAlbum(album)
            albumList.add(album)
            adapter?.notifyDataSetChanged()
        }
    }

    private fun getScaledBitmap(picturePath: String?, width: Int, height: Int): Bitmap? {
        val sizeOptions = BitmapFactory.Options()
        sizeOptions.inJustDecodeBounds = true
        BitmapFactory.decodeFile(picturePath, sizeOptions)
        val inSampleSize: Int = calculateInSampleSize(sizeOptions, width, height)
        sizeOptions.inJustDecodeBounds = false
        sizeOptions.inSampleSize = inSampleSize
        return BitmapFactory.decodeFile(picturePath, sizeOptions)
    }

    private fun calculateInSampleSize(
        options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int
    ): Int {
        // Raw height and width of image
        val height = options.outHeight
        val width = options.outWidth
        var inSampleSize = 1
        if (height > reqHeight || width > reqWidth) {

            val heightRatio = Math.round(height.toFloat() / reqHeight.toFloat())
            val widthRatio = Math.round(width.toFloat() / reqWidth.toFloat())
            inSampleSize = if (heightRatio < widthRatio) heightRatio else widthRatio
        }
        return inSampleSize
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_PERMISSION) {
            if (grantResults.size == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                if (requestCode == REQUEST_IMAGE_CHOOSE) {

                }
            } else {
                openPermissionNeededDialog()
            }
        }
    }

    private fun openPermissionNeededDialog() {
        val mediaPermissionDialogFragment = MediaPermissionDialogFragment()
        mediaPermissionDialogFragment.show(supportFragmentManager, "permissiondialog")
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

    companion object {
        const val REQUEST_IMAGE_CAPTURE = 1
        const val REQUEST_IMAGE_CHOOSE = 2
        const val REQUEST_PERMISSION = 3
    }

}