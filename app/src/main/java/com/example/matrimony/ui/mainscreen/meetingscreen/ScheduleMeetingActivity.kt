package com.example.matrimony.ui.mainscreen.meetingscreen

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.example.matrimony.R
import com.example.matrimony.TAG
import com.example.matrimony.databinding.ActivityScheduleMeetingBinding
import com.example.matrimony.db.entities.Meetings
import com.example.matrimony.ui.mainscreen.UserProfileViewModel
import com.example.matrimony.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.*


@AndroidEntryPoint
class ScheduleMeetingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityScheduleMeetingBinding

    private val userProfileViewModel by viewModels<UserProfileViewModel>()
    private val meetingsViewModel by viewModels<MeetingsViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_schedule_meeting)

        var userId = intent.getIntExtra("user_id", -1)

        userProfileViewModel.currentUserId = userId

        val sharedPref =
            getSharedPreferences(MY_SHARED_PREFERENCES, Context.MODE_PRIVATE)
        userProfileViewModel.userId = sharedPref.getInt(CURRENT_USER_ID, -1)

        Log.i(TAG, "userId= $userId")
        if (userId == -1) {
            val senderUserId = intent.getIntExtra("sender_user_id", -1)
            val receiverUserId = intent.getIntExtra("receiver_user_id", -1)
//        Log.i(TAG,"userId= $userId")

            Log.i(TAG, "sender $senderUserId")
            Log.i(TAG, "receiver $receiverUserId")
            userId = if (senderUserId == userProfileViewModel.userId)
                receiverUserId
            else
                senderUserId
            userProfileViewModel.currentUserId =
                if (userId == senderUserId) receiverUserId else senderUserId
            Log.i(TAG, "userId $userId")
        }

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        lifecycleScope.launch {
            userProfileViewModel.getUser(userId).observe(this@ScheduleMeetingActivity) {

                binding.tvScheduleMeet.text = "Schedule an appointment with ${it.name}"
                if (it.profile_pic != null) {
                    binding.ivProfilePic.visibility = View.VISIBLE
                    binding.noProfilesPic.visibility = View.GONE
                    if(it.profile_pic!=null)
                        binding.ivProfilePic.setImageBitmap(it.profile_pic)
                    else
                        binding.ivProfilePic.setImageResource(R.drawable.default_profile_pic)
//                    Glide.with(binding.ivProfilePic.context)
//                        .load(it.profile_pic)
////                        .apply(RequestOptions.bitmapTransform(RoundedCorners(200)))
//                        .into(binding.ivProfilePic)
                } else {
                    binding.ivProfilePic.visibility = View.GONE
                    binding.noProfilesPic.visibility = View.VISIBLE
                }
            }
        }



        initValues()
        initFocusListeners()
        initDatePicker()
        initTimePicker()

//        binding.btnCancelMeeting.setOnClickListener {
//            finish()
//        }

        binding.imgBtnSave?.setOnClickListener {
            scheduleMeeting()
        }


    }

//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        menuInflater.inflate(R.menu.save_menu,menu)
//        return true
//    }


    private fun initFocusListeners() {
        binding.etMeetingTitle.addTextChangedListener {
            binding.tilMeetingTitle.isErrorEnabled = false
        }
        binding.etMeetingDate.addTextChangedListener {
            binding.tilMeetingDate.isErrorEnabled = false
        }
        binding.etMeetingTime.addTextChangedListener {
            binding.tilMeetingTime.isErrorEnabled = false
        }
        binding.etMeetingPlace.addTextChangedListener {
            binding.tilMeetingPlace.isErrorEnabled = false
        }
    }

    private fun initValues() {
        val meetingId = intent.getIntExtra("meeting_id", -1)
        if (meetingId == -1)
            return
        lifecycleScope.launch {
            meetingsViewModel.getMeetingOnId(meetingId).observe(this@ScheduleMeetingActivity) {
                binding.etMeetingTitle.setText(it.title)
                val date = it.date
                val pattern = "dd/MM/yyyy"
                val sdf = SimpleDateFormat(pattern, Locale.getDefault())
                val dateString = sdf.format(date)
                binding.etMeetingDate.setText(dateString)
                binding.etMeetingTime.setText(it.time)
                binding.etMeetingPlace.setText(it.place)
            }
        }
    }


    private fun initDatePicker() {
        binding.etMeetingDate.setOnClickListener {
//            Toast.makeText(this, "date", Toast.LENGTH_SHORT).show()
            val datePicker = DatePickerFragment(false,binding.etMeetingDate.text.toString().ifBlank { null })
            datePicker.datePickerListener =
                DatePickerListener { date -> binding.etMeetingDate.setText(date) }
            datePicker.show(supportFragmentManager, "date-picker")
        }
    }

    private fun initTimePicker() {
        binding.etMeetingTime.setOnClickListener {

            val timePicker = TimePickerFragment(binding.etMeetingTime.text.toString().ifBlank { null })
            timePicker.timePickerListener =
                TimePickerListener { time -> binding.etMeetingTime.setText(formatTime(time)) }
            timePicker.show(supportFragmentManager, "date-picker")
        }
    }

    private fun scheduleMeeting() {
        if (isPageFilled()) {
            val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
            val date = getDateFromString(binding.etMeetingDate.text.toString())
//            val localDate = LocalDate.parse(binding.etMeetingDate.text.toString(), formatter)
//            val localDateTime = localDate.atStartOfDay()

//            val date = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant())

            val meetingId = intent.getIntExtra("meeting_id", -1)
            Log.i(TAG, "meetingId =$meetingId")
            if (meetingId != -1) {
                lifecycleScope.launch {

                    meetingsViewModel.updateMeetingDetails(
                        meetingId,
                        binding.etMeetingTitle.text.toString(),
                        date!!,
                        formatTime(binding.etMeetingTime.text.toString()),
                        binding.etMeetingPlace.text.toString(),
                    )

                }
//                return
            } else {

                Log.i(TAG, "$date")
                Log.i(TAG, formatTime(binding.etMeetingTime.text.toString()))
                lifecycleScope.launch {

                    meetingsViewModel.addNewMeeting(
                        Meetings(
                            0,
                            userProfileViewModel.userId,
                            userProfileViewModel.currentUserId,
                            binding.etMeetingTitle.text.toString(),
                            date!!,
                            formatTime(binding.etMeetingTime.text.toString()),
                            binding.etMeetingPlace.text.toString(),
                            "UPCOMING"
                        )
                    )
                }
            }
            finish()
        }
    }

    private fun formatTime(timeString: String): String {
        val parts = timeString.split(":")
        val hour = parts[0].trim().toInt()
        val minute = parts[1].trim().toInt()
        return String.format("%02d:%02d", hour, minute)
    }


    private fun isPageFilled(): Boolean {
        var isFilled = true
        binding.etMeetingTitle.text.toString().ifBlank {
            binding.tilMeetingTitle.isErrorEnabled = true
            binding.tilMeetingTitle.error = "Enter Meeting Title "
            isFilled = false
        }
        binding.etMeetingDate.text.toString().ifBlank {
            binding.tilMeetingDate.isErrorEnabled = true
            binding.tilMeetingDate.error = "Set Meeting Date"
            isFilled = false
        }
        binding.etMeetingTime.text.toString().ifBlank {
            binding.tilMeetingTime.isErrorEnabled = true
            binding.tilMeetingTime.error = "Set Meeting Time"
            isFilled = false
        }
        binding.etMeetingPlace.text.toString().ifBlank {
            binding.tilMeetingPlace.isErrorEnabled = true
            binding.tilMeetingPlace.error = "Enter Meeting Place"
            isFilled = false
        }
        return isFilled
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

}