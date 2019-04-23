package com.example.vladislav.flychat.AllChats


import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vladislav.flychat.R
import kotlinx.android.synthetic.main.fragment_all_chats.*


class AllChatsFragment : Fragment() {

    private val repo = AllChatsRemoteRepository()
    private lateinit var adapterLatestMessages: LatestMessagesRecyclerAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        repo.loadChatsList()

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_all_chats, container, false)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.main_menu, menu)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        button_show.setOnClickListener {
            linearLayoutManager = LinearLayoutManager(activity)
            chrv.layoutManager = linearLayoutManager

            adapterLatestMessages = LatestMessagesRecyclerAdapter(repo.latestMessages.values.toMutableList())
            chrv.adapter = adapterLatestMessages
            chrv.setHasFixedSize(true)

            repo.getAvailableUsers()

        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.new_chat -> {
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
