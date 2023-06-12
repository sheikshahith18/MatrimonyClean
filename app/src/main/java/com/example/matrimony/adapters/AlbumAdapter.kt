package com.example.matrimony.adapters

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import android.view.*
import android.widget.PopupMenu
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.matrimony.R
import com.example.matrimony.TAG
import com.example.matrimony.databinding.ImageViewGridBinding
import com.example.matrimony.db.entities.Album
import com.example.matrimony.ui.mainscreen.MainActivity
import com.example.matrimony.ui.mainscreen.UserProfileViewModel
import com.example.matrimony.ui.mainscreen.homescreen.profilescreen.albumscreen.AlbumViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AlbumAdapter(
    private val context:Context,
    private val userProfileViewModel: UserProfileViewModel,
    private val albumViewModel: AlbumViewModel,
    private val addImage: () -> Unit,
    private val getPermission: () -> Unit,
    private val checkPermission: () -> Boolean,
) :
    RecyclerView.Adapter<AlbumAdapter.ImageViewHolder>() {


    private var albumList = mutableListOf<Album>()

    fun setList(list: List<Album>) {
        Log.i(TAG, "Album count ${list.size}")
        this.albumList = list.toMutableList()
//        albumViewModel.albumList=list.toMutableList()
        notifyDataSetChanged()
    }

    fun addAlbum(userId: Int, image: Bitmap) {

        this.albumList.add(Album(0, userId, image, false))
        notifyDataSetChanged()
    }

    inner class ImageViewHolder(private val binding: ImageViewGridBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnCreateContextMenuListener,
        MenuItem.OnMenuItemClickListener {

        init {
            binding.ivPicture.setOnCreateContextMenuListener(this)
        }

        override fun onCreateContextMenu(
            menu: ContextMenu?,
            view: View?,
            menuInfo: ContextMenu.ContextMenuInfo?
        ) {
            val adapterPosition = absoluteAdapterPosition
            if (adapterPosition == 0 && albumList[0].isProfilePic) {
                val inflater: MenuInflater = MenuInflater(view?.context)
                inflater.inflate(R.menu.profile_pic_pop_menu, menu)
//                menu?.findItem(R.id.set_profile_pic)?.isVisible = false
            } else if (adapterPosition != albumList.size) {
                val inflater: MenuInflater = MenuInflater(view?.context)
                inflater.inflate(R.menu.album_pop_up_menu, menu)
            }
            menu?.findItem(R.id.remove_profile_pic)?.setOnMenuItemClickListener(this)
            menu?.findItem(R.id.set_profile_pic)?.setOnMenuItemClickListener(this)
            menu?.findItem(R.id.delete_picture)?.setOnMenuItemClickListener(this)

        }

        override fun onMenuItemClick(item: MenuItem): Boolean {
            when (item.itemId) {
                R.id.remove_profile_pic -> {
                    Toast.makeText(context,"Profile Picture Removed",Toast.LENGTH_SHORT).show()
//                    Snackbar.make(binding.root,"Profile Picture Removed", Snackbar.LENGTH_SHORT).show()
                    Log.i(TAG, "Remove Profile Pic")
                    userProfileViewModel.updateProfilePic(
                        albumList[absoluteAdapterPosition].user_id,
                        null
                    )
                    albumViewModel.removePicture(
                        userProfileViewModel.userId,
                        albumList[absoluteAdapterPosition].image_id
                    )
                    albumList.removeAt(0)
                    if (albumList.size != 0) bind(albumList[0], 0)
                    notifyDataSetChanged()
                }
                R.id.set_profile_pic -> {
                    Toast.makeText(context,"Profile Picture Updated",Toast.LENGTH_SHORT).show()
//                    Snackbar.make(binding.root,"Profile Picture Updated", Snackbar.LENGTH_SHORT).show()
                    CoroutineScope(Dispatchers.Main).launch {
                        val image = albumViewModel.getImage(
                            albumList[0].user_id,
                            albumList[0].image_id,
                        )
                        if (image.value?.isProfilePic == true) {
                            image.value?.isProfilePic = false
                        }


                        albumViewModel.addAlbum(
                            Album(
                                0,
                                albumList[absoluteAdapterPosition].user_id,
                                albumList[absoluteAdapterPosition].image,
                                true
                            )
                        )

                        albumViewModel.removePicture(
                            albumList[absoluteAdapterPosition].user_id,
                            albumList[absoluteAdapterPosition].image_id,
                        )

                        userProfileViewModel.updateProfilePic(
                            albumList[absoluteAdapterPosition].user_id,
                            albumList[absoluteAdapterPosition].image
                        )

                    }

                }
                R.id.delete_picture -> {
//                    Snackbar.make(binding.root,"Profile Removed From Album", Snackbar.LENGTH_SHORT).show()
                    Toast.makeText(context,"Picture Removed From Album",Toast.LENGTH_SHORT).show()
                    albumViewModel.removePicture(
                        userProfileViewModel.userId,
                        albumList[absoluteAdapterPosition].image_id
                    )
                    albumList.removeAt(0)
                    notifyDataSetChanged()
                }
            }
            return true
        }

        fun bind(album: Album?, position: Int) {
            if (position == albumList.size || album == null) {
                binding.ivPicture.setImageResource(R.drawable.ic_add_image)
                binding.ivPicture.setOnClickListener {

                    addImage()

                }
            } else {
                binding.ivPicture.setOnClickListener(null)
                binding.ivPicture.setImageBitmap(album.image)
                binding.root.setOnLongClickListener {
                    val popupMenu = PopupMenu(it.context, itemView)
                    popupMenu.inflate(R.menu.album_pop_up_menu)
                    popupMenu.setOnMenuItemClickListener { menuItem ->


                        when (menuItem.itemId) {
                            R.id.set_profile_pic -> {
                                if (absoluteAdapterPosition == 0) {
                                    menuItem.isVisible = false
                                    true
                                } else {
                                    menuItem.isVisible = true
                                }
                                true
                            }
                            R.id.remove_profile_pic -> {

                                true
                            }
                            R.id.delete_picture -> false
                            else -> true
                        }

                        false
                    }
                    popupMenu.show()
                    true

                }
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {

        val binding: ImageViewGridBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.image_view_grid, parent, false
        )

        return ImageViewHolder(binding)

    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        Log.i(TAG, "Album onBind[$position]")
        if (position < albumList.size)
            holder.bind(albumList[position], position)
        else
            holder.bind(null, position)
    }

    override fun getItemCount(): Int {
        if (albumList.size <= 5)
            return albumList.size + 1
        else
            return albumList.size
    }

}