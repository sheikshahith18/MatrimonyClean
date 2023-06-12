package com.example.matrimony.adapters

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.example.matrimony.R
import com.example.matrimony.TAG
import com.example.matrimony.databinding.ProfilesHorizontalGridViewBinding
import com.example.matrimony.models.UserData
import com.example.matrimony.ui.mainscreen.MainActivity
import com.example.matrimony.ui.mainscreen.UserProfileViewModel
import com.example.matrimony.ui.mainscreen.homescreen.profilescreen.ViewImageActivity
import com.example.matrimony.ui.mainscreen.homescreen.profilescreen.ViewProfileActivity
import com.example.matrimony.ui.mainscreen.homescreen.profilescreen.albumscreen.AlbumViewModel
import com.example.matrimony.ui.mainscreen.homescreen.settingsscreen.SettingsViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.internal.managers.FragmentComponentManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.abs

class ProfilesGridAdapter(
    private val context: Context,
    private val viewModel: UserProfileViewModel,
    private val settingsViewModel: SettingsViewModel,
    private val albumViewModel: AlbumViewModel,
    private val viewFullProfile: (Int) -> Unit
) :
    RecyclerView.Adapter<ProfilesGridAdapter.ProfilesGridViewHolder>() {

    private var userList: List<UserData> = mutableListOf()

    fun setList(list: List<UserData>) {
        this.userList = list.toMutableList()
        notifyDataSetChanged()
    }

    inner class ProfilesGridViewHolder(private val binding: ProfilesHorizontalGridViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val profileDp = binding.ivProfilePic
        private val profileName = binding.tvProfileName
        private val profileAge = binding.tvProfileAge

        fun bind(user: UserData) {
            profileName.text = userList[absoluteAdapterPosition].name
            profileAge.text = userList[absoluteAdapterPosition].age.toString()

            CoroutineScope(Dispatchers.Main).launch {

                viewModel.getConnectionStatus(viewModel.currentUserId)
                    .observe((FragmentComponentManager.findActivity(binding.root.context) as AppCompatActivity)) { connectionStatus ->
                        CoroutineScope(Dispatchers.Main).launch {
                            settingsViewModel.getPrivacySettings(user.userId)
                                .observe((FragmentComponentManager.findActivity(binding.root.context) as AppCompatActivity)) { privacy ->


                                    Log.i(TAG, "privacy setting of userId=${user.userId}")
                                    if (privacy.view_profile_pic == "Everyone" || connectionStatus == "CONNECTED") {

//                                        CoroutineScope(Dispatchers.Main).launch {
//                                        albumViewModel.getProfilePic(user.userId).observe((FragmentComponentManager.findActivity(binding.root.context) as AppCompatActivity)){
////                                            if(it!=null){
//                                                Glide.with(profileDp.context)
//                                                    .load(
//                                                        it?.image ?: R.drawable.default_profile_pic
//                                                    )
//                                                    .centerCrop()
//                                                    .fitCenter()
//                                                    .into(profileDp)
////                                            }
//                                        }
//                                        }

                                        if (user.profile_pic != null)
                                            binding.ivProfilePic.setImageBitmap(user.profile_pic)
                                        else
                                            binding.ivProfilePic.setImageResource(R.drawable.default_profile_pic)

//                                        Glide.with(profileDp.context)
//                                            .load(
//                                                user.profile_pic ?: R.drawable.default_profile_pic
//                                            )
//                                            .centerCrop()
//                                            .fitCenter()
//                                            .into(profileDp)
                                    } else
                                        binding.ivProfilePic.setImageResource(R.drawable.default_profile_pic)
//                                        Glide.with(profileDp.context)
//                                            .load(R.drawable.default_profile_pic)
//                                            .centerCrop()
//                                            .fitCenter()
//                                            .into(profileDp)

                                }
                        }
                    }
                albumViewModel.getUserAlbumCount(user.userId)
                    .observe((FragmentComponentManager.findActivity(binding.root.context) as AppCompatActivity)) {
                        if (it > 1)
                            binding.tvAlbumCount.text = it.toString()
                        else
                            binding.tvAlbumCount.visibility = View.GONE
                        if (it > 0) {
                            binding.ivProfilePic.setOnClickListener {
                                val intent =
                                    Intent(context as MainActivity, ViewImageActivity::class.java)
                                intent.putExtra("user_id", user.userId)
                                intent.putExtra("position", 0)
                                context.startActivity(intent)
                            }
                        } else
                            binding.ivProfilePic.setOnClickListener { view ->
                                Snackbar.make(
                                    view,
                                    "This user didn't added any images",
                                    Snackbar.LENGTH_SHORT
                                ).show()
                            }
                    }
            }




            binding.ivProfilePic.setOnClickListener {
//                viewFullProfile(user.userId)
                val intent = Intent(context as MainActivity, ViewImageActivity::class.java)
                intent.putExtra("user_id", user.userId)
                intent.putExtra("position", 0)
                context.startActivity(intent)
            }

        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfilesGridViewHolder {
        val binding: ProfilesHorizontalGridViewBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.profiles_horizontal_grid_view, parent, false
        )

        return ProfilesGridViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProfilesGridViewHolder, position: Int) {
        holder.bind(userList[position])

        holder.itemView.setOnClickListener {
            viewFullProfile(userList[position].userId)
        }
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

}