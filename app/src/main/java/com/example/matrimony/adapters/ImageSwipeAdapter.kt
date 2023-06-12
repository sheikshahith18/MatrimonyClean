package com.example.matrimony.adapters

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.matrimony.R
import com.example.matrimony.ui.mainscreen.homescreen.profilescreen.ViewImageActivity
import com.example.matrimony.ui.mainscreen.homescreen.profilescreen.ViewProfileActivity

class ImageSwipeAdapter(private val context: Context, private val imageList: List<Bitmap?>) :
    RecyclerView.Adapter<ImageSwipeAdapter.ImageViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.profile_image_item1, parent, false)
        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        if (imageList[position] != null)
            holder.imageView.setImageBitmap(imageList[position])
//            Glide.with(holder.imageView.context)
//                .load(imageList[position])
//                .centerCrop()
//                .centerInside()
//                .fitCenter()
//                .into(holder.imageView)
    }

    override fun getItemCount(): Int {
        return imageList.size
    }

    inner class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.iv_profile_image)
    }
}

class ImageSwipeAdapter1(
    private val context: Context,
    private val imageList: List<Bitmap?>,
    private val userId: Int,
    private val startActivity: Boolean
) :
    RecyclerView.Adapter<ImageSwipeAdapter1.ImageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.profile_image_item, parent, false)
        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        if (imageList[position] != null) {
            holder.imageView.setImageBitmap(imageList[position])
//            Glide.with(holder.imageView.context)
//                .load(imageList[position])
//                .centerCrop()
//                .centerInside()
//                .fitCenter()
//                .into(holder.imageView)

            if (startActivity)
                holder.imageView.setOnClickListener {
//            Toast.makeText(context,"imgSwipPos=$position",Toast.LENGTH_SHORT).show()
                    openFullImageActivity(position)
                }
        }
    }

    override fun getItemCount(): Int {
        return imageList.size
    }

    private fun openFullImageActivity(position: Int) {
        val intent = Intent(context as ViewProfileActivity, ViewImageActivity::class.java)
        intent.putExtra("user_id", userId)
        intent.putExtra("position", position)
        intent.putExtra("class_name", "ImageSwipeAdapter")
        context.startActivity(intent)

    }

    inner class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.iv_profile_image)
    }
}
