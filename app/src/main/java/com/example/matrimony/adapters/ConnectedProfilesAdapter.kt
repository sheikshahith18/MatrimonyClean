package com.example.matrimony.adapters

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.matrimony.R
import com.example.matrimony.TAG
import com.example.matrimony.databinding.ConnectedProfilesViewBinding
import com.example.matrimony.models.UserData
import com.example.matrimony.ui.mainscreen.MainActivity
import com.example.matrimony.ui.mainscreen.connectionsscreen.ConnectionsViewModel
import com.example.matrimony.ui.mainscreen.homescreen.profilescreen.ViewImageActivity
import com.example.matrimony.ui.mainscreen.homescreen.profilescreen.albumscreen.AlbumViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.internal.managers.FragmentComponentManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class ConnectedProfilesAdapter(
    private val context: Context,
    private val connectionsViewModel: ConnectionsViewModel,
    private val albumViewModel: AlbumViewModel,
    private val onCallButtonClick: (userId: Int) -> Unit,
    private val onRemoveButtonClicked: (userId: Int,adapterPosition:Int) -> Unit,
    private val onScheduleButtonClicked: (userId: Int) -> Unit,
    private val viewFullProfile: (Int) -> Unit

) :
    RecyclerView.Adapter<ConnectedProfilesAdapter.ConnectedProfilesViewHolder>() {


    private var usersList = mutableListOf<UserData>()

    fun setUserList(list: List<UserData>) {
        Log.i(TAG, "connecteduserlistcount ${list.size}")
        this.usersList = list.toMutableList()
        notifyDataSetChanged()
    }

    fun removeUser(position: Int){
        usersList.removeAt(position)
        notifyItemRemoved(position)
    }


    inner class ConnectedProfilesViewHolder(private val binding: ConnectedProfilesViewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        val btnText = MutableLiveData<String>("CONNECTED")

        val btnRemoveConnection = binding.btnRemoveConnection

        fun bind(user: UserData) {
            Log.i(TAG, "onBind Connection")
            binding.tvProfileName.text = user.name
            binding.tvProfileAge.text = "👤${user.age},"
            binding.tvProfileHeight.text = user.height
            binding.tvProfileEducation.text = "🎓${user.education}, ${user.occupation}"
            if (user.city != null) {
                if (user.state != null) binding.tvProfileLocation.text =
                    "📍 ${user.city}, ${user.state}"
                else binding.tvProfileLocation.text = "📍 ${user.city}"
                binding.tvProfileLocation.visibility = View.VISIBLE
            } else {
                if (user.state != null) {
                    binding.tvProfileLocation.text = "📍 ${user.state}"
                    binding.tvProfileLocation.visibility = View.VISIBLE
                } else
                    binding.tvProfileLocation.visibility = View.GONE
            }

            binding.imgBtnCall.setOnClickListener {
                onCallButtonClick(user.userId)
            }

            binding.imgBtnSchedule.setOnClickListener {
                onScheduleButtonClicked(user.userId)
            }

            binding.ivProfilePic.setOnClickListener {
                val intent = Intent(context as MainActivity, ViewImageActivity::class.java)
                intent.putExtra("user_id", user.userId)
                intent.putExtra("position", 0)
                context.startActivity(intent)
            }

            getConnectionStatus(user.userId)

//            btnRemoveConnection.text = "Remove Connection"
//            btnRemoveConnection.setBackgroundColor(
//                context.resources.getColor(
//                    R.color.red,
//                    null
//                )
//            )
            binding.btnRemoveConnection.setOnClickListener {
//                usersList.removeAt(absoluteAdapterPosition)
//                notifyItemRemoved(absoluteAdapterPosition)

                onRemoveButtonClicked(
                    user.userId,
//                    "",
                    absoluteAdapterPosition
                )

            }

            CoroutineScope(Dispatchers.Main).launch {
                albumViewModel.getUserAlbumCount(user.userId)
                    .observe((FragmentComponentManager.findActivity(binding.root.context) as AppCompatActivity)) {
                        if (it > 1) {
                            binding.tvAlbumCount.visibility = View.VISIBLE
                            binding.tvAlbumCount.text = it.toString()
                        }
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

            Log.i(TAG, "Connected Profiles user : $user")
            if(user.profile_pic!=null)
                binding.ivProfilePic.setImageBitmap(user.profile_pic)
            else
                binding.ivProfilePic.setImageResource(R.drawable.default_profile_pic)
//            Glide.with(binding.ivProfilePic.context)
//                .load(
//                    user.profile_pic ?: ResourcesCompat.getDrawable(
//                        binding.ivProfilePic.context.resources,
//                        R.drawable.default_profile_pic,
//                        null
//                    )
//                )
//
//                .into(binding.ivProfilePic)
        }

        private fun getConnectionStatus(userId: Int) {

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConnectedProfilesViewHolder {
        val binding: ConnectedProfilesViewBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.connected_profiles_view, parent, false
        )

        return ConnectedProfilesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ConnectedProfilesViewHolder, position: Int) {
        holder.bind(usersList[position])
        holder.itemView.setOnClickListener {

            viewFullProfile(usersList[position].userId)
        }
    }

    override fun getItemCount(): Int {
        return usersList.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

}