package com.example.matrimony.ui.mainscreen.homescreen.profilescreen

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.example.matrimony.R
import com.example.matrimony.TAG
import com.example.matrimony.adapters.ImageSwipeAdapter1
import com.example.matrimony.adapters.ViewPagerAdapter
import com.example.matrimony.databinding.PageViewProfileBinding
import com.example.matrimony.db.entities.Album
import com.example.matrimony.db.entities.Connections
import com.example.matrimony.db.entities.Shortlists
import com.example.matrimony.models.ConnectionStatus
import com.example.matrimony.models.UserData
import com.example.matrimony.ui.mainscreen.UserProfileViewModel
import com.example.matrimony.ui.mainscreen.connectionsscreen.ConnectionsViewModel
import com.example.matrimony.ui.mainscreen.connectionsscreen.RemoveConnectionDialogFragment
import com.example.matrimony.ui.mainscreen.connectionsscreen.RemoveConnectionListener
import com.example.matrimony.ui.mainscreen.homescreen.profilescreen.albumscreen.AlbumActivity1
import com.example.matrimony.ui.mainscreen.homescreen.profilescreen.albumscreen.AlbumViewModel
import com.example.matrimony.ui.mainscreen.homescreen.settingsscreen.SettingsViewModel
import com.example.matrimony.ui.mainscreen.meetingscreen.ScheduleMeetingActivity
import com.example.matrimony.utils.CURRENT_USER_ID
import com.example.matrimony.utils.MY_SHARED_PREFERENCES
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.internal.managers.FragmentComponentManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.URLEncoder


@AndroidEntryPoint
class ViewProfileActivity : AppCompatActivity() {

    private val userProfileViewModel by viewModels<UserProfileViewModel>()
    private val connectionViewModel by viewModels<ConnectionsViewModel>()
    private val settingsViewModel by viewModels<SettingsViewModel>()
    private val albumViewModel by viewModels<AlbumViewModel>()

    lateinit var binding: PageViewProfileBinding
    lateinit var user: UserData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.i(TAG, "ViewProfile onCreate")


        val userId = intent.getIntExtra("USER_ID", -1)
        val sharedPref = getSharedPreferences(MY_SHARED_PREFERENCES, Context.MODE_PRIVATE)


        userProfileViewModel.isCurrentUser = (userId == sharedPref.getInt(CURRENT_USER_ID, -1))

        Log.i(TAG, "ViewProfileAct onCreate")
        Log.i(TAG, "${userProfileViewModel.isCurrentUser}")

        userProfileViewModel.userId = sharedPref.getInt(CURRENT_USER_ID, -1)
        connectionViewModel.userId = sharedPref.getInt(CURRENT_USER_ID, -1)


        binding = DataBindingUtil.setContentView(this, R.layout.page_view_profile)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        userProfileViewModel.currentUserId = userId



        lifecycleScope.launch {
            user = userProfileViewModel.getUserData(userId)
            if (!userProfileViewModel.isCurrentUser) userProfileViewModel.getNameOfUser(userId)
                .observe(this@ViewProfileActivity) {
                    binding.toolbar.title = "$it's Profile"
                }
        }

//        userProfileViewModel.getConnectionStatus(userProfileViewModel.currentUserId)
        lifecycleScope.launch {
            userProfileViewModel.getConnectionStatus(userProfileViewModel.currentUserId)
                .observe(this@ViewProfileActivity) {
                    userProfileViewModel.isUserConnected = it != null && it == "CONNECTED"
                    initFab()
                }
//                userProfileViewModel.isConnectionAvailable(userId).value == "CONNECTED"

            Log.i(TAG, "view profile curr user id= ${userProfileViewModel.currentUserId}")
            Log.i(TAG, "isConnected= ${userProfileViewModel.isUserConnected}")
            Log.i(
                TAG,
                "isConnected= ${userProfileViewModel.isConnectionAvailable(userProfileViewModel.userId)}"
            )
            if (userProfileViewModel.isCurrentUser) {
//                binding.extendedFab.visibility = View.GONE
                binding.fabShortlist.visibility = View.GONE
                binding.fabSendConnection.visibility = View.GONE
                binding.fabDial.visibility = View.GONE
                binding.fabMeeting.visibility = View.GONE
            } else {
//                setUpCollapsingToolbar()
//                initFab()
            }
        }

        binding.collapsingToolbar.contentScrim = null



        if (userProfileViewModel.userId == userProfileViewModel.currentUserId) {
            binding.imgBtnEditAlbum.visibility = View.VISIBLE
            binding.imgBtnEditAlbum.setOnClickListener {
                val intent = Intent(this, AlbumActivity1::class.java)
                startActivity(intent)
            }
        } else binding.imgBtnEditAlbum.visibility = View.GONE


        binding.collapsingToolbar.title = "UserProfile"


        setUpProfileDetailsDisplayPager()
        loadAlbum()
        setUpShortlistFab()
        setUpConnectionsFab()
        setUpDialFab()
        setUpMeetingFab()
        setUpWhatsappFab()
    }

    private fun setUpDialFab() {
        binding.fabDial.setOnClickListener {
            lifecycleScope.launch {
                userProfileViewModel.getUserMobile(userProfileViewModel.currentUserId)
                    .observe(this@ViewProfileActivity) {
                        val intent = Intent(Intent.ACTION_DIAL)
                        intent.data = Uri.parse("tel:$it")
                        startActivity(intent)
                    }
            }
        }
    }

    private fun setUpMeetingFab() {
        binding.fabMeeting.setOnClickListener {
            val intent = Intent(this, ScheduleMeetingActivity::class.java)
            intent.putExtra("user_id", userProfileViewModel.currentUserId)
            startActivity(intent)
        }
    }

    private fun isAppInstalled(packageName: String): Boolean {
        return try {
            packageManager.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES)
            true
        } catch (ignored: PackageManager.NameNotFoundException) {
            false
        }
    }

    private fun setUpWhatsappFab() {

        binding.fabWhatsapp.setOnClickListener {
            if (isAppInstalled("com.whatsapp")) {
                lifecycleScope.launch {

                    userProfileViewModel.getUserMobile(userProfileViewModel.currentUserId)
                        .observe(this@ViewProfileActivity) { mobile ->
                            val url =
                                "https://api.whatsapp.com/send?phone=+91$mobile&text=" + URLEncoder.encode(
                                    "Hi there! I came up on your profile on Vivahaa Matrimony. I would like to contact you...",
                                    "UTF-8"
                                );
                            val intent = Intent(Intent.ACTION_VIEW)
                            intent.`package` = "com.whatsapp"
                            intent.data = Uri.parse(url)
                            startActivity(intent)
                        }
                }
            } else {
                val builder = AlertDialog.Builder(this)

                builder
                    .setMessage("This device doesn't have whatsapp installed")
                    .setPositiveButton("Ok") { dialog: DialogInterface, _: Int ->
                        dialog.dismiss()
                    }
                val alertDialog: AlertDialog = builder.create()
                alertDialog.show()
            }
        }
    }

    var isAllBtnVisible: Boolean? = null

    private fun initFab() {

        isAllBtnVisible = false

        Log.i(TAG, "user connection status=${userProfileViewModel.isUserConnected}")
        if (!userProfileViewModel.isUserConnected) {
            binding.fabDial.visibility = View.GONE
            binding.fabMeeting.visibility = View.GONE
            binding.fabWhatsapp?.visibility = View.GONE
        } else {
            when (connectionStatus) {
                ConnectionStatus.CONNECTED -> {
//                    binding.fabShortlist.show()
//                    binding.fabSendConnection.show()
//                    binding.fabDial.show()
//                    binding.fabMeeting.show()
//                    binding.fabWhatsapp?.show()
                }
                ConnectionStatus.NOT_CONNECTED, ConnectionStatus.REQUESTED, null -> {
//                    binding.fabShortlist.show()
//                    binding.fabSendConnection.show()
                }
            }
        }

    }


    private var isShortlisted = false
    private fun setUpShortlistFab() {
        CoroutineScope(Dispatchers.Main).launch {
            userProfileViewModel.getShortlistedStatus(userProfileViewModel.currentUserId)
                .observe((FragmentComponentManager.findActivity(binding.root.context) as AppCompatActivity)) {
                    isShortlisted = it
                    if (isShortlisted) {
//                        Log.i(TAG, "$userId saved already")
                        (binding.fabShortlist as ImageButton).setImageResource(R.drawable.ic_baseline_favorite_enabled)
                    } else {
                        (binding.fabShortlist as ImageButton).setImageResource(R.drawable.ic_baseline_favorite_hollow)
                    }
                }
        }

        binding.fabShortlist.setOnClickListener {
            if (isShortlisted) {
                Snackbar.make(
                    binding.root,
                    "Shortlist Removed For ${user.name}",
                    Snackbar.LENGTH_SHORT
                ).show()
                userProfileViewModel.removeShortlist(userProfileViewModel.currentUserId)
                isShortlisted = false
                (it as ImageButton).setImageResource(R.drawable.ic_baseline_favorite_hollow)
            } else {
                Snackbar.make(binding.root, "Shortlisted ${user.name}", Snackbar.LENGTH_SHORT)
                    .show()
                userProfileViewModel.shortlistUser(
                    Shortlists(0, userProfileViewModel.userId, userProfileViewModel.currentUserId)
                )
                isShortlisted = true
                (it as ImageButton).setImageResource(R.drawable.ic_baseline_favorite_enabled)
            }
        }

    }

    private fun showConfirmConnectionDialog(userId:Int){
        val builder = AlertDialog.Builder(this)

        builder
//            .setTitle("Connection request pending")
            .setMessage("This user already sent you a request. Do you want to accept it?")
            .setPositiveButton("Ok") { dialog: DialogInterface, _: Int ->
                lifecycleScope.launch {
                    connectionViewModel.setConnectionStatus(userId,"CONNECTED")
                }
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog: DialogInterface, _: Int ->
                dialog.dismiss()
            }
        val alertDialog: AlertDialog = builder.create()
        alertDialog.show()
    }

    private var connectionStatus: ConnectionStatus? = ConnectionStatus.NOT_CONNECTED
    private fun setUpConnectionsFab() {
        CoroutineScope(Dispatchers.Main).launch {
            connectionViewModel.getConnectionDetails(
                userProfileViewModel.userId,
                userProfileViewModel.currentUserId
            )
                .observe(this@ViewProfileActivity) {
                    Log.i(TAG, "Search Adapter CoroutineScope Connection")
//                    Toast.makeText(this@ViewProfileActivity,"ConnectionStatus ${it?.status ?: "status null"}",Toast.LENGTH_SHORT).show()
                    if (it == null) {
                        (binding.fabSendConnection as ImageButton).setImageResource(R.drawable.ic_send_connection)
                        return@observe
                    }
                    connectionStatus = when (it.status) {
                        ConnectionStatus.CONNECTED.toString() -> {
                            (binding.fabSendConnection as ImageButton).setImageResource(R.drawable.ic_remove_connection)
                            ConnectionStatus.CONNECTED
                        }
                        ConnectionStatus.REQUESTED.toString() -> {
                            val value: ConnectionStatus? =
                                if (userProfileViewModel.userId == it.user_id) {
                                    (binding.fabSendConnection as ImageButton).setImageResource(R.drawable.ic_connection_sent)
                                    ConnectionStatus.REQUESTED
                                } else {
                                    null
                                }
                            value
                        }
                        else -> {
                            (binding.fabSendConnection as ImageButton).setImageResource(R.drawable.ic_send_connection)
                            ConnectionStatus.NOT_CONNECTED
                        }
                    }
                }
        }


        binding.fabSendConnection.setOnClickListener {
            when (connectionStatus) {
                null -> {
                    showConfirmConnectionDialog(user.userId)
//                    Snackbar.make(
//                        binding.root,
//                        "Accept the connection request sent by ${user.name}",
//                        Snackbar.LENGTH_SHORT
//                    )
//                        .show()
//                    Toast.makeText(
//                        this,
//                        "Accept the connection request sent by the user",
//                        Toast.LENGTH_SHORT
//                    ).show()
                }
                ConnectionStatus.NOT_CONNECTED -> {
                    connectionStatus = ConnectionStatus.REQUESTED
                    (binding.fabSendConnection as ImageButton).setImageResource(R.drawable.ic_connection_sent)
                    Snackbar.make(
                        binding.root,
                        "Connection request sent to ${user.name}",
                        Snackbar.LENGTH_SHORT
                    )
                        .show()
//                        .setAnchorView((requireActivity() as MainActivity).findViewById<BottomNavigationView>(R.id.bottom_nav_view))
                    userProfileViewModel.sendConnection = true
                }
                ConnectionStatus.REQUESTED -> {
                    Snackbar.make(
                        binding.root,
                        "Connection Request to ${user.name} is Cancelled",
                        Snackbar.LENGTH_SHORT
                    )
                        .show()
                    connectionStatus = ConnectionStatus.NOT_CONNECTED
                    (binding.fabSendConnection as ImageButton).setImageResource(R.drawable.ic_send_connection)
                    userProfileViewModel.sendConnection = false
                }
                ConnectionStatus.CONNECTED -> {
//                    val dialog = RemoveConnectionDialogFragment()
//                    dialog.removeConnectionListener = RemoveConnectionListener {
//
//                        Snackbar.make(
//                            binding.root,
//                            "Connection with ${user.name} is Removed",
//                            Snackbar.LENGTH_SHORT
//                        ).show()
//
//                        connectionStatus = ConnectionStatus.NOT_CONNECTED
//                        (binding.fabSendConnection as ImageButton).setImageResource(R.drawable.ic_send_connection)
//
//                        userProfileViewModel.removeConnection(
//                            userProfileViewModel.userId, userProfileViewModel.currentUserId
//                        )
//                    }
//                    dialog.show(supportFragmentManager, "remove_connection_dialog")
                    loadDialog()
//                    userProfileViewModel.dialogLoad=true
                }
            }
        }
    }

    private fun loadDialog() {
//        Toast.makeText(this,"load album",Toast.LENGTH_SHORT).show()
//        if(userProfileViewModel.dialogLoad){
        val dialogFragment =
            supportFragmentManager.findFragmentByTag("remove_connection_dialog") as RemoveConnectionDialogFragment?
        if (dialogFragment != null && dialogFragment.dialog?.isShowing == true) {
            dialogFragment.dialog?.dismiss()
        } else {
            // DialogFragment is not showing or doesn't exist
        }
//        }
        val dialog = RemoveConnectionDialogFragment()
        dialog.removeConnectionListener = RemoveConnectionListener {

            Snackbar.make(
                binding.root,
                "Connection with ${user.name} is Removed",
                Snackbar.LENGTH_SHORT
            ).show()

            connectionStatus = ConnectionStatus.NOT_CONNECTED
            (binding.fabSendConnection as ImageButton).setImageResource(R.drawable.ic_send_connection)

            userProfileViewModel.removeConnection(
                userProfileViewModel.userId, userProfileViewModel.currentUserId
            )
        }
        dialog.show(supportFragmentManager, "remove_connection_dialog")
        userProfileViewModel.dialogLoad = true
    }

    override fun onResume() {
        super.onResume()
        val dialogFragment =
            supportFragmentManager.findFragmentByTag("remove_connection_dialog") as RemoveConnectionDialogFragment?
        if (dialogFragment == null || dialogFragment.dialog?.isShowing == false)
            userProfileViewModel.dialogLoad = false

        if (userProfileViewModel.dialogLoad) {
            loadDialog()
        }


    }

    private fun loadAlbum() {

        val albumList = mutableListOf<Album>()

//        lifecycleScope.launch {
//            albumViewModel.getUserAlbum(userProfileViewModel.currentUserId)
//                .observe(this@ViewProfileActivity) { //album ->
//                    Log.i(TAG, "Inside ViewProfile getProfilePic")
//                    albumList.clear()
//                    album?.forEach { album1 ->
//                        if (album1 != null) if (album1.isProfilePic) albumList.add(
//                            0,
//                            album1
//                        )
//                        else albumList.add(album1)
//
//                    }
//                    val imageList = mutableListOf<Bitmap?>()
//                    albumList.forEach {
//                        imageList.add(it.image)
//                    }
        lifecycleScope.launch {
            userProfileViewModel.getConnectionStatus(userProfileViewModel.currentUserId)
                .observe(this@ViewProfileActivity) { connectionStatus ->
                    lifecycleScope.launch {
                        settingsViewModel.getPrivacySettings(userProfileViewModel.currentUserId)
                            .observe(this@ViewProfileActivity) { privacy ->

                                lifecycleScope.launch {
                                    albumViewModel.getUserAlbum(userProfileViewModel.currentUserId)
                                        .observe(this@ViewProfileActivity) { album ->
                                            albumList.clear()
                                            album?.forEach { album1 ->
                                                if (album1 != null) if (album1.isProfilePic) albumList.add(
                                                    0,
                                                    album1
                                                )
                                                else albumList.add(album1)

                                            }
                                            val imageList = mutableListOf<Bitmap?>()


                                            if (albumList.isNotEmpty()) {

                                                Log.i(TAG, "album")
                                                Log.i(
                                                    TAG,
                                                    "current user ${userProfileViewModel.isCurrentUser}"
                                                )
                                                Log.i(
                                                    TAG,
                                                    "privacy profile ${privacy.view_profile_pic}"
                                                )
                                                Log.i(
                                                    TAG,
                                                    "privacy album ${privacy.view_my_album}"
                                                )
                                                Log.i(
                                                    TAG,
                                                    "connection status $connectionStatus"
                                                )


                                                if (userProfileViewModel.isCurrentUser || privacy.view_profile_pic == "Everyone" || (connectionStatus != null && connectionStatus == "CONNECTED")) {
                                                    if (albumList[0].isProfilePic) {
                                                        imageList.add(albumList[0].image)
                                                        albumList.removeAt(0)
                                                    }
                                                } else {
                                                    if (albumList[0].isProfilePic) {
                                                        albumList.removeAt(0)
                                                        imageList.add(
                                                            BitmapFactory.decodeResource(
                                                                resources,
                                                                R.drawable.connect_message
                                                            )
                                                        )
                                                    }
                                                }
                                                if (userProfileViewModel.isCurrentUser || privacy.view_my_album == "Everyone" || (connectionStatus != null && connectionStatus == "CONNECTED")) {
                                                    albumList.forEach {
                                                        imageList.add(it.image)
//                                                        imageList.add(BitmapFactory.decodeResource(resources,R.drawable.connect_message))
                                                    }
                                                } else {
                                                    albumList.forEach { _ ->
                                                        imageList.add(
                                                            BitmapFactory.decodeResource(
                                                                resources,
                                                                R.drawable.connect_message
                                                            )
                                                        )
                                                    }
                                                }
                                            }
                                            val sharedPref =
                                                getSharedPreferences(
                                                    MY_SHARED_PREFERENCES,
                                                    Context.MODE_PRIVATE
                                                )
                                            userProfileViewModel.userId =
                                                sharedPref.getInt(CURRENT_USER_ID, -1)
                                            val imageSwipeAdapter = ImageSwipeAdapter1(
                                                this@ViewProfileActivity,
                                                imageList,
                                                userProfileViewModel.currentUserId,
                                                userProfileViewModel.userId != userProfileViewModel.currentUserId
                                            )
                                            binding.viewPagerProfilePics.adapter =
                                                imageSwipeAdapter

                                            TabLayoutMediator(
                                                binding.tabLayoutImages,
                                                binding.viewPagerProfilePics
                                            ) { _, _ ->
                                            }.attach()
                                            Log.i(TAG, "albumListSize : ${imageList.size}")
                                            if (imageList.isNotEmpty()) {
                                                if (imageList.size > 1)
                                                    binding.tabLayoutImages.visibility =
                                                        View.VISIBLE
                                                else
                                                    binding.tabLayoutImages.visibility =
                                                        View.GONE

                                                binding.viewPagerProfilePics.visibility =
                                                    View.VISIBLE
                                                binding.noImages.visibility = View.GONE

                                            } else {
                                                binding.tabLayoutImages.visibility = View.GONE
                                                binding.viewPagerProfilePics.visibility =
                                                    View.GONE
                                                binding.noImages.visibility = View.VISIBLE
                                            }

//                                            if (imageList.isEmpty()) {
//                                                binding.collapsingToolbar.setBackgroundResource(R.drawable.default_profile_pic)
////                                                                            collapsingToolbarLayout.background = resource
//                                            }
                                            if ((binding.viewPagerProfilePics.adapter?.itemCount
                                                    ?: 0) <= 1
                                            ) {
                                                binding.tvPageNo.visibility = View.GONE
                                            } else
                                                binding.tvPageNo.visibility = View.VISIBLE
                                        }
                                }
                            }
                    }
                }

            binding.viewPagerProfilePics.registerOnPageChangeCallback(object :
                ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    val totalPages =
                        binding.viewPagerProfilePics.adapter?.itemCount ?: 0
                    if (totalPages <= 1)
                        binding.tvPageNo.visibility = View.GONE
                    else
                        binding.tvPageNo.visibility = View.VISIBLE
                    val currentPage = position + 1
                    val pageText = "$currentPage/$totalPages"

                    // Update your page number TextView or any other view to display the pageText
                    binding.tvPageNo.text = pageText
                }
            })


        }
//                }

//        }


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        return true
    }


    private fun setUpProfileDetailsDisplayPager() {

        val tabLayout = findViewById<TabLayout>(R.id.tab_layout_profile_details)
        val viewPagerProfileDetails = findViewById<ViewPager2>(R.id.view_pager_profile_details)

        val viewPagerAdapter = ViewPagerAdapter(supportFragmentManager, lifecycle).apply {
            add(PersonalInfoFragment())
            add(ReligionInfoFragment())
            add(ProfessionalInfoFragment())
            add(PartnerPreferencesFragment())
        }
        viewPagerProfileDetails.adapter = viewPagerAdapter



        tabLayout.tabMode = TabLayout.MODE_SCROLLABLE
        TabLayoutMediator(tabLayout, viewPagerProfileDetails) { tab, position ->
            when (position) {
                0 -> tab.text = "Basic Details"
                1 -> tab.text = "Religion Details"
                2 -> {
                    tab.text = "Professional Details"

                }
                3 -> tab.text = "Partner Preferences"
            }
        }.attach()
    }

    override fun onPause() {
        super.onPause()
        if (userProfileViewModel.sendConnection != null && userProfileViewModel.sendConnection!!) {
            userProfileViewModel.addConnection(
                Connections(
                    0, userProfileViewModel.userId, userProfileViewModel.currentUserId, "REQUESTED"
                )
            )
        } else if (userProfileViewModel.sendConnection != null && !userProfileViewModel.sendConnection!!) {
            userProfileViewModel.removeConnection(
                userProfileViewModel.userId, userProfileViewModel.currentUserId
            )
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
}