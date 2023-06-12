package com.example.matrimony.ui.mainscreen.connectionsscreen

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.matrimony.R
import com.example.matrimony.TAG
import com.example.matrimony.adapters.ConnectedProfilesAdapter
import com.example.matrimony.databinding.FragmentConnectedProfilesBinding
import com.example.matrimony.db.entities.Connections
import com.example.matrimony.ui.mainscreen.UserProfileViewModel
import com.example.matrimony.ui.mainscreen.homescreen.profilescreen.ViewProfileActivity
import com.example.matrimony.ui.mainscreen.homescreen.profilescreen.albumscreen.AlbumViewModel
import com.example.matrimony.ui.mainscreen.meetingscreen.ScheduleMeetingActivity
import com.example.matrimony.utils.CURRENT_USER_ID
import com.example.matrimony.utils.MY_SHARED_PREFERENCES
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.net.URLEncoder


@AndroidEntryPoint
class ConnectedProfilesFragment : Fragment() {

    lateinit var binding: FragmentConnectedProfilesBinding
    private var fragmentView: View? = null
    private var adapter: ConnectedProfilesAdapter? = null

    private val userProfileViewModel by activityViewModels<UserProfileViewModel>()
    private val connectionsViewModel by activityViewModels<ConnectionsViewModel>()
    private val albumViewModel by activityViewModels<AlbumViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.i(TAG, "ConProf OnCreate")
        Log.i(TAG, "conVM userId :${connectionsViewModel.userId}")
//        Log.i(TAG, "current user id ${userProfileViewModel.userId}")
        if (fragmentView == null) {

            binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_connected_profiles,
                container,
                false
            )
            fragmentView = binding.root

            val sharedPref =
                requireActivity().getSharedPreferences(MY_SHARED_PREFERENCES, Context.MODE_PRIVATE)
            connectionsViewModel.userId = sharedPref.getInt(CURRENT_USER_ID, -1)

            loadConnectedProfiles()
        }
        return fragmentView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        loadConnectedProfiles()
    }


    private val onCallButtonClicked = { userId: Int ->

        lifecycleScope.launch {
            connectionsViewModel.getUserMobile(userId).observe(viewLifecycleOwner) {

                val intent = Intent(Intent.ACTION_DIAL)
                intent.data = Uri.parse("tel:$it")
                startActivity(intent)
            }
        }
        Unit
    }

    private val onScheduleButtonCLicked: (Int) -> Unit = { userId ->
        val intent = Intent(requireActivity(), ScheduleMeetingActivity::class.java)
        intent.putExtra("user_id", userId)
        startActivity(intent)
    }

    private val onRemoveButtonClicked: (Int, Int) -> Unit =
        { userId: Int, adapterPosition: Int ->

            loadDialog(userId, adapterPosition)
            userProfileViewModel.dialogLoad=true
            userProfileViewModel.dialogUserId=userId
        }

    private fun loadDialog(userId: Int, adapterPosition: Int ){
        val dialogFragment = parentFragmentManager.findFragmentByTag("remove_connection_dialog") as RemoveConnectionDialogFragment?
        if (dialogFragment != null && dialogFragment.dialog?.isShowing == true) {
            Log.i(TAG+4,"dialog dismissed")
            Log.i(TAG+4,"after dismissal, userId= ${userProfileViewModel.dialogUserId}")
//            Toast.makeText(requireContext(),"dialog dismissed",Toast.LENGTH_SHORT).show()
            dialogFragment.dialog?.dismiss()
//            userProfileViewModel.dialogLoad=false
        }

        Log.i(TAG+4,"Before dialog creation")
        val dialog = RemoveConnectionDialogFragment()
        Log.i(TAG+4,"After dialog creation")
        dialog.removeConnectionListener = RemoveConnectionListener {
            Log.i(TAG+4,"inside removeCon")
            Log.i(TAG+4,"userId $userId")
//            Toast.makeText(requireContext(),"con rem",Toast.LENGTH_SHORT).show()
//            val viewHolder =
//                binding.rvConnectedProfiles.findViewHolderForAdapterPosition(adapterPosition) as ConnectedProfilesAdapter.ConnectedProfilesViewHolder


//            adapter?.removeUser(adapterPosition)
            connectedUserId = -1
            connectionsViewModel.removeConnection(userProfileViewModel.dialogUserId)
            adapter?.notifyDataSetChanged()
        }
//        Log.i(TAG+4,(dialog::removeConnectionListener.isInitialized).toString())

        val args = Bundle()
        args.putString("CALLER", this::class.simpleName)
        dialog.arguments = args
        dialog.show(parentFragmentManager, "remove_connection_dialog")

        userProfileViewModel.dialogLoad=true
        userProfileViewModel.dialogUserId=userId
//        Toast.makeText(requireContext(),"dialogLoadSetTo ${userProfileViewModel.dialogLoad}",Toast.LENGTH_SHORT).show()
//        userProfileViewModel.dialogUserId=userId
//        userProfileViewModel.dialogAdapterPosition=adapterPosition
//        userProfileViewModel.dialogUserName=name
    }

    override fun onResume() {
        Log.i(TAG+4,"ConPro onResume")
        super.onResume()
        val dialogFragment = parentFragmentManager.findFragmentByTag("remove_connection_dialog") as RemoveConnectionDialogFragment?
        Log.i(TAG+4,"dialogFrag isNull ?: $dialogFragment")
        Log.i(TAG+4,"dialogFrag isShowing ?: ${dialogFragment?.dialog?.isShowing ?: "null"}")
        if (dialogFragment == null || dialogFragment.dialog?.isShowing == false) {
            Log.i(TAG+4,"dialogFrag $dialogFragment")
            Log.i(TAG+4,"onRes inside if")
            userProfileViewModel.dialogLoad = false
        }else
            userProfileViewModel.dialogLoad = true

//        Toast.makeText(requireContext(),"dialogLoad ${userProfileViewModel.dialogLoad}",Toast.LENGTH_SHORT).show()
        Log.i(TAG+4,"dialogLoad ${userProfileViewModel.dialogLoad}")

        if(userProfileViewModel.dialogLoad){
            Log.i(TAG+4,"onRes loadNewDialog")
            loadDialog(userProfileViewModel.dialogUserId,userProfileViewModel.dialogAdapterPosition)
        }
    }


    //from dialog fragment
    private var connectedUserId = -1

    override fun onPause() {
        super.onPause()
        Log.i(TAG, "con prof onPause")
        Log.i(TAG, "remove connection ${connectionsViewModel.removeFromConnections.size}")

        var isChanged = false
        connectionsViewModel.removeFromConnections.forEach {
            Log.i(TAG, "remove connection userId $it")
//            connectionsViewModel.setConnectionStatus(it, "NOT_CONNECTED")
            connectionsViewModel.removeConnection(it)
        }
        connectionsViewModel.removeFromConnections.clear()

        connectionsViewModel.sendConnectionsTo.forEach {
            isChanged = true
            connectionsViewModel.addConnection(
                Connections(
                    user_id = connectionsViewModel.userId,
                    connected_user_id = it,
                    status = "REQUESTED"
                )
            )
        }
        connectionsViewModel.sendConnectionsTo.clear()
    }

    private val viewFullProfile: (Int) -> Unit = { userId: Int ->
        val intent = Intent(activity, ViewProfileActivity::class.java)
        intent.putExtra("USER_ID", userId)
        startActivity(intent)
    }

    private fun loadConnectedProfiles() {
        val connectedProfilesRecyclerView = binding.rvConnectedProfiles
        connectedProfilesRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        adapter = ConnectedProfilesAdapter(
            requireActivity(),
            connectionsViewModel,
            albumViewModel,
            onCallButtonClicked,
            onRemoveButtonClicked,
            onScheduleButtonCLicked,
            viewFullProfile
        )
        connectedProfilesRecyclerView.adapter = adapter
        lifecycleScope.launch {
            connectionsViewModel.getConnectedUsers(connectionsViewModel.userId)
                .observe(requireActivity()) {
                    Log.i(TAG, "connect profiles count : ${it.size}")
                    adapter?.setUserList(it)
                    if (it.isEmpty()) {
                        binding.noProfilesMessage.visibility = View.VISIBLE
                        binding.rvConnectedProfiles.visibility = View.GONE
                    } else {
                        binding.noProfilesMessage.visibility = View.GONE
                        binding.rvConnectedProfiles.visibility = View.VISIBLE
                    }
                }
        }
    }


}