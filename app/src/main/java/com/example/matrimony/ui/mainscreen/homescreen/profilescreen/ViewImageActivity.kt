package com.example.matrimony.ui.mainscreen.homescreen.profilescreen

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.example.matrimony.R
import com.example.matrimony.TAG
import com.example.matrimony.adapters.ImageSwipeAdapter
import com.example.matrimony.adapters.ImageSwipeAdapter1
import com.example.matrimony.adapters.SearchPageAdapter
import com.example.matrimony.databinding.ActivityViewImageBinding
import com.example.matrimony.db.entities.Album
import com.example.matrimony.db.entities.Connections
import com.example.matrimony.db.entities.Shortlists
import com.example.matrimony.models.ConnectionStatus
import com.example.matrimony.models.UserData
import com.example.matrimony.ui.mainscreen.MainActivity
import com.example.matrimony.ui.mainscreen.UserProfileViewModel
import com.example.matrimony.ui.mainscreen.connectionsscreen.ConnectionsViewModel
import com.example.matrimony.ui.mainscreen.connectionsscreen.RemoveConnectionDialogFragment
import com.example.matrimony.ui.mainscreen.connectionsscreen.RemoveConnectionListener
import com.example.matrimony.ui.mainscreen.homescreen.SlidePageTransformer
import com.example.matrimony.ui.mainscreen.homescreen.profilescreen.albumscreen.AlbumViewModel
import com.example.matrimony.ui.mainscreen.homescreen.settingsscreen.SettingsViewModel
import com.example.matrimony.utils.CURRENT_USER_ID
import com.example.matrimony.utils.MY_SHARED_PREFERENCES
import com.example.matrimony.utils.OnDelayClickListener
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.internal.managers.FragmentComponentManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ViewImageActivity : AppCompatActivity() {

    private val userProfileViewModel by viewModels<UserProfileViewModel>()
    private val albumViewModel by viewModels<AlbumViewModel>()
    private val settingsViewModel by viewModels<SettingsViewModel>()
    private val connectionsViewModel by viewModels<ConnectionsViewModel>()
    lateinit var binding: ActivityViewImageBinding

    lateinit var user: UserData


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_view_image)
        binding.imgBtnClose.setOnClickListener {
            finish()
        }


        if (intent.getStringExtra("class_name") == "ImageSwipeAdapter") {
            binding.tvViewFullProfile.visibility = View.GONE
        }

        binding.tvViewFullProfile.setOnClickListener {
            userProfileViewModel.dialogLoad = false
            val intent = Intent(this, ViewProfileActivity::class.java)
            intent.putExtra("USER_ID", user.userId)
            startActivity(intent)
        }

//        binding.btnConnection.setOnClickListener {
//            Toast.makeText(this,"Clicked",Toast.LENGTH_SHORT).show()
//        }
//        setContentView(R.layout.activity_view_image)

//        setSupportActionBar(binding.toolbar)
//        supportActionBar.setHomeAsUpIndicator()
//        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        init()
    }

    private var isShortlisted = false

    private fun init() {
        val sharedPref =
            getSharedPreferences(MY_SHARED_PREFERENCES, Context.MODE_PRIVATE)
        userProfileViewModel.userId = sharedPref.getInt(CURRENT_USER_ID, -1)

        val userId = intent.getIntExtra("user_id", -1)
        val position = intent.getIntExtra("position", 0)
        if (userId != -1) {
            userProfileViewModel.currentUserId = userId
            val albumList = mutableListOf<Album>()
//            lifecycleScope.launch {
//                user = userProfileViewModel.getUserData(userId)
//
//                albumViewModel.getUserAlbum(userProfileViewModel.currentUserId)
//                    .observe(this@ViewImageActivity) { album ->
//                        Log.i(TAG, "Inside ViewProfile getProfilePic")
//                        albumList.clear()
//                        album?.forEach { album1 ->
//                            if (album1 != null) if (album1.isProfilePic) albumList.add(
//                                0,
//                                album1
//                            )
//                            else albumList.add(album1)
//
//                        }
//                        val imageList = mutableListOf<Bitmap?>()
//                    albumList.forEach {
//                        imageList.add(it.image)
//                    }
                        lifecycleScope.launch {
                            userProfileViewModel.getConnectionStatus(userProfileViewModel.currentUserId)
                                .observe(this@ViewImageActivity) { connectionStatus ->
                                    lifecycleScope.launch {
                                        user = userProfileViewModel.getUserData(userId)

                                        albumViewModel.getUserAlbum(userProfileViewModel.currentUserId)
                                            .observe(this@ViewImageActivity) { album ->
                                                Log.i(TAG, "Inside ViewProfile getProfilePic")
                                                albumList.clear()
                                                album?.forEach { album1 ->
                                                    if (album1 != null) if (album1.isProfilePic) albumList.add(
                                                        0,
                                                        album1
                                                    )
                                                    else albumList.add(album1)

                                                }
                                                val imageList = mutableListOf<Bitmap?>()
                                    imageList.clear()
//                                    Toast.makeText(this@ViewImageActivity,"imagelist cleared",Toast.LENGTH_SHORT).show()
//                                    Toast.makeText(this@ViewImageActivity,"albumList size=${albumList.size}",Toast.LENGTH_SHORT).show()
                                    lifecycleScope.launch {
                                        settingsViewModel.getPrivacySettings(userProfileViewModel.currentUserId)
                                            .observe(this@ViewImageActivity) { privacy ->
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
                                                            imageList.add(BitmapFactory.decodeResource(resources,R.drawable.connect_message))
                                                        }
                                                    }
                                                    if (userProfileViewModel.isCurrentUser || privacy.view_my_album == "Everyone" || (connectionStatus != null && connectionStatus == "CONNECTED")) {
                                                        albumList.forEach {
                                                            imageList.add(it.image)
                                                        }
                                                    }else{
                                                        albumList.forEach { _ ->
                                                            imageList.add(BitmapFactory.decodeResource(resources,R.drawable.connect_message))
                                                        }
                                                    }
                                                }
                                                val imageSwipeAdapter = ImageSwipeAdapter(
                                                    this@ViewImageActivity,
                                                    imageList,
//                                                    userProfileViewModel.currentUserId
                                                )
                                                binding.viewPagerImages.adapter =
                                                    imageSwipeAdapter

                                                binding.viewPagerImages.setPageTransformer(com.example.matrimony.ui.mainscreen.homescreen.profilescreen.SlidePageTransformer())

//                                                TabLayoutMediator(
//                                                    binding.tabLayoutImages,
//                                                    binding.viewPagerProfilePics
//                                                ) { _, _ ->
//                                                }.attach()
                                                Log.i(TAG, "albumListSize : ${imageList.size}")
                                                if (imageList.isNotEmpty()) {
//                                                    if (imageList.size > 1)
////                                                        binding.tabLayoutImages.visibility =
////                                                            View.VISIBLE
//                                                    else
//                                                        binding.tabLayoutImages.visibility = View.GONE

                                                    binding.viewPagerImages.visibility =
                                                        View.VISIBLE
//                                                    binding.noImages?.visibility = View.GONE

                                                } else {
//                                                    binding.tabLayoutImages.visibility = View.GONE
//                                                    binding.viewPagerProfilePics.visibility = View.GONE
//                                                    binding.noImages?.visibility = View.VISIBLE
                                                }

//                                            if (imageList.isEmpty()) {
//                                                binding.collapsingToolbar.setBackgroundResource(R.drawable.default_profile_pic)
////                                                                            collapsingToolbarLayout.background = resource
//                                            }
                                            }
                                    }
                                }

                            binding.viewPagerImages.registerOnPageChangeCallback(object :
                                ViewPager2.OnPageChangeCallback() {
                                override fun onPageSelected(position: Int) {
                                    val totalPages = binding.viewPagerImages.adapter?.itemCount ?: 0
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
                    }
                binding.viewPagerImages.currentItem = position


//                Toast.makeText(this,"$userId",Toast.LENGTH_SHORT)
                val name = userProfileViewModel.getUserData(userProfileViewModel.currentUserId).name

                CoroutineScope(Dispatchers.Main).launch {
                    userProfileViewModel.getShortlistedStatus(userId)
                        .observe((FragmentComponentManager.findActivity(binding.root.context) as AppCompatActivity)) {
                            isShortlisted = it
                            if (isShortlisted) {
                                Log.i(TAG, "$userId saved already")
                                binding.imgBtnShortlist.setImageResource(R.drawable.ic_baseline_favorite_enabled)
                                binding.tvShortlist.text="Shortlisted"
//                            binding.imgBtnShortlist.tooltipText = "shortlisted"
                            } else {
                                binding.imgBtnShortlist.setImageResource(R.drawable.ic_favorite_white)
                                binding.tvShortlist.text="Shortlist"
//                            binding.imgBtnShortlist.tooltipText = "shortlist"
                            }
                        }
                }

                binding.imgBtnShortlist.setOnClickListener {
                    Log.i(
                        TAG,
                        "current user id = ${userProfileViewModel.userId}, shortlisted user id = $userId"
                    )
                    if (isShortlisted) {
                        isShortlisted = false
                        Log.i(TAG, "removed")
                        userProfileViewModel.removeShortlist(userProfileViewModel.userId, userId)
                        (it as ImageButton).setImageResource(R.drawable.ic_favorite_white)
                        binding.tvShortlist.text="Shortlist"
                        Snackbar.make(it, "Shortlist Removed For $name", Snackbar.LENGTH_SHORT)
                            .setAnchorView(binding.imgBtnShortlist)
                            .show()
                    } else {
                        isShortlisted = true
                        Log.i(TAG, "shortlisted")
                        userProfileViewModel.shortlistUser(
                            Shortlists(
                                0,
                                userProfileViewModel.userId,
                                userId
                            )
                        )
                        (it as ImageButton).setImageResource(R.drawable.ic_baseline_favorite_enabled)
                        binding.tvShortlist.text="Shortlisted"
                        Snackbar.make(it, "Shortlisted $name", Snackbar.LENGTH_SHORT)
                            .setAnchorView(binding.imgBtnShortlist)
                            .show()
                    }
                }
                getConnectionStatus(userId, name)

            }
        }
    }

    var connectionStatus: ConnectionStatus? = ConnectionStatus.NOT_CONNECTED
    private fun getConnectionStatus(userId: Int, name: String) {
        connectionsViewModel.userId = userProfileViewModel.userId

        CoroutineScope(Dispatchers.Main).launch {
            connectionsViewModel.getConnectionDetails(userProfileViewModel.userId, userId)
                .observe((FragmentComponentManager.findActivity(binding.root.context) as AppCompatActivity)) {
                    Log.i(TAG, "Search Adapter CoroutineScope Connection")
//                    Log.i(TAG, "Adapter Position : $absoluteAdapterPosition")
                    Log.i(TAG, "Connection Status : ${it?.status}")
                    if (it == null) {
                        connectionStatus = ConnectionStatus.NOT_CONNECTED
                        binding.btnConnection.setIconResource(R.drawable.ic_send_connection)
                        binding.tvLikeThisProfile.text="Like this profile?"
                        return@observe
                    }
                    connectionStatus = when (it.status) {
                        ConnectionStatus.CONNECTED.toString() -> {
                            binding.btnConnection.setIconResource(R.drawable.ic_remove_connection)
                            binding.tvLikeThisProfile.text="Remove Connection"
                            ConnectionStatus.CONNECTED
                        }
                        ConnectionStatus.REQUESTED.toString() -> {
                            val value: ConnectionStatus? =
                                if (connectionsViewModel.userId == it.user_id) {
                                    binding.btnConnection.setIconResource(R.drawable.ic_connection_sent)
                                    binding.tvLikeThisProfile.text="Cancel Request"
                                    ConnectionStatus.REQUESTED
                                } else {
                                    null
                                }

                            value
                        }
                        else -> {
                            binding.btnConnection.setIconResource(R.drawable.ic_send_connection)
                            binding.tvLikeThisProfile.text="Like this profile?"
                            //                                    binding.imgBtnSendReq.tooltipText = "not_connected"
                            ConnectionStatus.NOT_CONNECTED
                        }
                    }
                }

            binding.btnConnection.setOnClickListener(object : OnDelayClickListener {
                override fun onDelayClick() {
//                Toast.makeText(this@ViewImageActivity,"Con Btn Click",Toast.LENGTH_SHORT).show()
                    Log.i(TAG, "connectionStatus $connectionStatus")
                    onConnectionButtonClicked(
                        userId,
                        connectionStatus.toString(),
                        name
                    )
                }
            })
        }
    }

    private fun showConfirmConnectionDialog(userId:Int){
        val builder = AlertDialog.Builder(this)

        builder
//            .setTitle("Connection request pending")
            .setMessage("This user already sent you a request. Do you want to accept it?")
            .setPositiveButton("Ok") { dialog: DialogInterface, _: Int ->
                lifecycleScope.launch {
                    connectionsViewModel.setConnectionStatus(userId,"CONNECTED")
                }
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog: DialogInterface, _: Int ->
                dialog.dismiss()
            }
        val alertDialog: AlertDialog = builder.create()
        alertDialog.show()
    }

    private var connectedUserId = -1
    private val onConnectionButtonClicked: (Int, String, String) -> Unit =
        { userId: Int, connectionStatus: String, name: String ->
            when (connectionStatus) {
                "null" -> {
//                    Snackbar.make(
//                        binding.root,
//                        "Accept the connection request sent by the user",
//                        Snackbar.LENGTH_SHORT
//                    )
//                        .setAnchorView(binding.imgBtnShortlist)
//                        .show()
                    showConfirmConnectionDialog(userId)
                }
                "NOT_CONNECTED" -> {
                    connectedUserId = userId
//                    viewHolder.btnRemoveConnection.text = "Request Sent"
                    this.connectionStatus = ConnectionStatus.REQUESTED
                    binding.btnConnection.setIconResource(R.drawable.ic_connection_sent)
                    binding.tvLikeThisProfile.text="Cancel Request"

                    connectionsViewModel.addConnection(
                        Connections(
                            user_id = connectionsViewModel.userId,
                            connected_user_id = userId,
                            status = "REQUESTED"
                        )
                    )
                    Snackbar.make(
                        binding.root,
                        "Connection Request sent to $name",
                        Snackbar.LENGTH_SHORT
                    )
                        .setAnchorView(binding.imgBtnShortlist)
                        .show()
                }
                "REQUESTED" -> {
                    connectedUserId = userId
                    this.connectionStatus = ConnectionStatus.NOT_CONNECTED
                    binding.btnConnection.setIconResource(R.drawable.ic_send_connection)
                    binding.tvLikeThisProfile.text="Like this profile?"

                    CoroutineScope(Dispatchers.Main).launch {
                        if (userProfileViewModel.isConnectionAvailable(connectedUserId)) {
                            connectionsViewModel.removeConnection1.add(userId)
                        }
                    }

//                    connectionsViewModel.sendConnectionsTo.remove(connectedUserId)
//                    connectionsViewModel.removeFromConnections.add(connectedUserId)
                    connectionsViewModel.removeConnection(userId)

                    Snackbar.make(
                        binding.root,
                        "Connection Request to $name is Cancelled",
                        Snackbar.LENGTH_SHORT
                    )
                        .setAnchorView(binding.imgBtnShortlist)
                        .show()
                }
                "CONNECTED" -> {
//                    connectedUserId = userId
//                    val dialog = RemoveConnectionDialogFragment()
//                    dialog.removeConnectionListener = RemoveConnectionListener {
//                        Snackbar.make(
//                            binding.root,
//                            "Connection with $name is Removed",
//                            Snackbar.LENGTH_SHORT
//                        )
//                            .setAnchorView(binding.imgBtnShortlist)
//                            .show()
//                        this.connectionStatus = ConnectionStatus.NOT_CONNECTED
//                        binding.btnConnection.setIconResource(R.drawable.ic_send_connection)
//
//                        connectionsViewModel.removeConnection(connectedUserId)
//                        connectedUserId = -1
//                    }
//                    val args = Bundle()
//                    args.putString("CALLER", this::class.simpleName)
//                    dialog.arguments = args
//                    dialog.show(
//                        supportFragmentManager,
//                        "remove_connection_dialog"
//                    )
                    loadDialog()
                }
            }
        }

    private fun loadDialog() {
//        Toast.makeText(this, "load album", Toast.LENGTH_SHORT).show()
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
            )
                .setAnchorView(binding.imgBtnShortlist)
                .show()

            connectionStatus = ConnectionStatus.NOT_CONNECTED
            binding.btnConnection.setIconResource(R.drawable.ic_send_connection)
            binding.tvLikeThisProfile.text="Like this profile?"

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
        if (dialogFragment != null && dialogFragment.dialog?.isShowing == false)
            userProfileViewModel.dialogLoad = false

        if (userProfileViewModel.dialogLoad) {
            loadDialog()
        }
    }

}