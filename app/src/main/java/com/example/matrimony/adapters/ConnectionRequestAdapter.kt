package com.example.matrimony.adapters

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.matrimony.R
import com.example.matrimony.TAG
import com.example.matrimony.databinding.RequestsListViewBinding
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

class ConnectionRequestAdapter(
    private val context: Context,
    private val connectionsViewModel: ConnectionsViewModel,
    private val albumViewModel: AlbumViewModel,
    private val viewFullProfile: (Int) -> Unit
) :
    RecyclerView.Adapter<ConnectionRequestAdapter.ConnectionRequestViewHolder>() {

    private var usersList = mutableListOf<UserData>()


    fun setUsersList(list: List<UserData>) {
        Log.i(TAG, "result size ${list.size}")
        this.usersList = list.toMutableList()
        notifyDataSetChanged()
    }


    inner class ConnectionRequestViewHolder(private val binding: RequestsListViewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(user: UserData) {
            binding.tvProfileName.text = user.name
            binding.tvProfileAge.text = "👤${user.age},"
            binding.tvProfileHeight.text = user.height ?: ""
            binding.tvProfileEducation.text = "🎓${user.education}/${user.occupation}"
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

            Log.i(TAG, "Connected Request user : $user")
            if (user.profile_pic != null)
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

            binding.ivProfilePic.setOnClickListener {
                val intent = Intent(context as MainActivity, ViewImageActivity::class.java)
                intent.putExtra("user_id", user.userId)
                intent.putExtra("position", 0)
                context.startActivity(intent)
            }
            binding.btnApproveReq.setOnClickListener {
                connectionsViewModel.setConnectionStatus(user.userId, "CONNECTED")
//                usersList.remove(user)
//                notifyItemRemoved(bindingAdapterPosition)
//                notifyDataSetChanged()

            }

            binding.btnRejectReq.setOnClickListener {
                connectionsViewModel.removeConnection(user.userId)
//                usersList.remove(user)
//                notifyItemRemoved(bindingAdapterPosition)
//                notifyDataSetChanged()
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
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConnectionRequestViewHolder {

        val binding: RequestsListViewBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.requests_list_view,
            parent,
            false
        )
        return ConnectionRequestViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ConnectionRequestViewHolder, position: Int) {
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