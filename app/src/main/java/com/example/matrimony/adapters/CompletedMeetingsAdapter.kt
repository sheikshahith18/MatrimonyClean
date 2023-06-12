package com.example.matrimony.adapters

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.matrimony.R
import com.example.matrimony.TAG
import com.example.matrimony.databinding.CompletedMeetingsViewBinding
import com.example.matrimony.databinding.UpcomingMeetingViewBinding
import com.example.matrimony.db.entities.Meetings
import com.example.matrimony.ui.mainscreen.MainActivity
import com.example.matrimony.ui.mainscreen.homescreen.profilescreen.ViewImageActivity
import com.example.matrimony.ui.mainscreen.homescreen.profilescreen.albumscreen.AlbumViewModel
import com.example.matrimony.ui.mainscreen.meetingscreen.CancelledMeetingsFragment
import com.example.matrimony.ui.mainscreen.meetingscreen.MeetingsViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.internal.managers.FragmentComponentManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class CompletedMeetingsAdapter(
    private val context: Context,
    private val meetingsViewModel: MeetingsViewModel,
    private val albumViewModel: AlbumViewModel,
    private val className: String,
    private val viewFullProfile: (Int) -> Unit
) : RecyclerView.Adapter<CompletedMeetingsAdapter.CompletedMeetingsViewHolder>() {

    private var meetingsList = mutableListOf<Meetings>()

    fun setMeetingsList(list: List<Meetings>) {
        Log.i(TAG, "upcomingMeetingsCount ${list.size}")
        this.meetingsList = list.toMutableList()
        notifyDataSetChanged()
    }


    inner class CompletedMeetingsViewHolder(private val binding: CompletedMeetingsViewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(meeting: Meetings) {
            binding.btnStatus.isClickable = false
            val userId =
                if (meetingsViewModel.userId == meeting.receiver_user_id) meeting.sender_user_id else meeting.receiver_user_id

            if (className == CancelledMeetingsFragment::class.java.simpleName) {
                binding.btnStatus.text = "Status : Cancelled"
                binding.btnStatus.setBackgroundColor(
                    context.resources.getColor(
                        R.color.red, null
                    )
                )
            }
            CoroutineScope(Dispatchers.Main).launch {
                val user = meetingsViewModel.getUserData(userId)

                binding.tvProfileName.text = user.name


                if(user.profile_pic!=null)
                    binding.ivProfilePic.setImageBitmap(user.profile_pic)
                else
                    binding.ivProfilePic.setImageResource(R.drawable.default_profile_pic)

//                Glide.with(binding.ivProfilePic.context).load(
//                    user.profile_pic ?: ResourcesCompat.getDrawable(
//                        context.resources, R.drawable.default_profile_pic, null
//                    )
//                )//.apply(RequestOptions.bitmapTransform(RoundedCorners(50)))
//                    .into(binding.ivProfilePic)

                binding.btnStatus.setOnClickListener(null)

                binding.tvDescription.text = meeting.title
                binding.tvDescription.text = meeting.title
                val date = meeting.date
                val pattern = "dd/MM/yyyy"
                val sdf = SimpleDateFormat(pattern)
                val dateString = sdf.format(date)

                Log.i(TAG, "meet date = ${meeting.date}")
                Log.i(TAG, "meet dateString = $dateString")
                binding.tvWhen.text = "$dateString, ${meeting.time}"
                binding.tvWhere.text = meeting.place

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
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CompletedMeetingsViewHolder {
        val binding: CompletedMeetingsViewBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context), R.layout.completed_meetings_view, parent, false
        )

        return CompletedMeetingsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CompletedMeetingsViewHolder, position: Int) {

        holder.itemView.setOnClickListener {
            viewFullProfile(if (meetingsViewModel.userId == meetingsList[position].receiver_user_id) meetingsList[position].sender_user_id else meetingsList[position].receiver_user_id)
        }

        holder.bind(meetingsList[position])
    }

    override fun getItemCount(): Int {
        return meetingsList.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

}