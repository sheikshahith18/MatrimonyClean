package com.example.matrimony.ui.mainscreen.connectionsscreen

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.widget.ViewPager2
import com.example.matrimony.R
import com.example.matrimony.TAG
import com.example.matrimony.adapters.ViewPagerAdapter
import com.example.matrimony.databinding.FragmentConnectionsPageBinding
import com.example.matrimony.db.entities.Connections
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ConnectionsPageFragment : Fragment() {

    lateinit var binding: FragmentConnectionsPageBinding
    private var fragmentView: View? = null
    private var connectionRequestFragment: ConnectionRequestsFragment? = null
    private var connectedProfilesFragment: ConnectedProfilesFragment? = null

    private val connectionsViewModel by activityViewModels<ConnectionsViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (fragmentView == null) {

            binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_connections_page,
                container,
                false
            )
            setConnectionsViewPager()
            fragmentView = binding.root
        }
        return fragmentView
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        binding.viewPagerConnections.setCurrentItem(connectionsViewModel.currentSelectedPage,false)
    }

    override fun onPause() {
        super.onPause()
        connectionsViewModel.removeFromConnections.forEach {
            Log.i(TAG,"remove connection userId $it")
//            connectionsViewModel.setConnectionStatus(it, "NOT_CONNECTED")
            connectionsViewModel.removeConnection(it)
        }
        connectionsViewModel.removeFromConnections.clear()

        connectionsViewModel.sendConnectionsTo.forEach {
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


    private fun setConnectionsViewPager() {
        if (connectionRequestFragment == null)
            connectionRequestFragment = ConnectionRequestsFragment()
        if (connectedProfilesFragment == null)
            connectedProfilesFragment = ConnectedProfilesFragment()

        val viewPagerAdapter = ViewPagerAdapter(parentFragmentManager, lifecycle).apply {
//            add(connectionRequestFragment!!)
//            add(connectedProfilesFragment!!)
            add(ConnectionRequestsFragment())
            add(ConnectedProfilesFragment())
        }
        binding.viewPagerConnections.isSaveEnabled=false
        binding.viewPagerConnections.adapter = viewPagerAdapter

        TabLayoutMediator(
            binding.tabLayoutConnections,
            binding.viewPagerConnections
        ) { tab, position ->
            when (position) {
                0 -> tab.text = "Requests"
                1 -> tab.text = "My Connections"
            }
        }.attach()

        binding.viewPagerConnections.registerOnPageChangeCallback(object :ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                connectionsViewModel.currentSelectedPage=position
            }
        })
    }
}