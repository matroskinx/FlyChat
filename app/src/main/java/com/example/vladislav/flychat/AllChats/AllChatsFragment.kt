package com.example.vladislav.flychat.AllChats


import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vladislav.flychat.Models.LastMessage
import com.example.vladislav.flychat.R
import kotlinx.android.synthetic.main.fragment_all_chats.*


class AllChatsFragment : Fragment() {

    private val repo = AllChatsRemoteRepository()
    private lateinit var viewModel: AllChatsViewModel
    private lateinit var adapterLatestMessages: LatestMessagesRecyclerAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.let {
            viewModel = ViewModelProviders.of(it).get(AllChatsViewModel::class.java)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_all_chats, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        linearLayoutManager = LinearLayoutManager(activity)
        chrv.layoutManager = linearLayoutManager
        chrv.setHasFixedSize(true)

        val latestMessagesObserver = Observer<MutableMap<String, LastMessage>> {
            adapterLatestMessages = LatestMessagesRecyclerAdapter(it)
            chrv.adapter = adapterLatestMessages
        }

        viewModel.latestMessages.observe(this, latestMessagesObserver)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.main_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.onNavDestinationSelected(item, view!!.findNavController())
                || super.onOptionsItemSelected(item)
    }
}
