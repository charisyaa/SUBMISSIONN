package com.example.submissionpertamafundamental.ui.finished

import android.content.Intent
import androidx.fragment.app.viewModels
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.data.response.EventResponse
import com.data.response.ListEventsItem
import com.data.retrofit.ApiConfig
import com.example.submissionpertamafundamental.R
import com.example.submissionpertamafundamental.databinding.FragmentFinishedBinding
import com.example.submissionpertamafundamental.ui.EventAdapter
import com.example.submissionpertamafundamental.ui.EventDetailActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FinishedFragment : Fragment() {

    private var _binding: FragmentFinishedBinding? = null
    private val binding get() = _binding!!

    companion object {
        const val TAG = "FinishedFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFinishedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupSearchView()
        getListEvent()
    }

    private fun setupSearchView() {
        binding.searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrEmpty()) {
                    searchEvents(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrEmpty()) {
                    getListEvent()
                }
                return true
            }
        })
    }

    private fun searchEvents(query: String) {
        showLoading(true)
        val client = ApiConfig.getApiService().getEvent(active = 0, query = query)
        client.enqueue(object : Callback<EventResponse> {
            override fun onResponse(call: Call<EventResponse>, response: Response<EventResponse>) {
                showLoading(false)
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        setEventData(responseBody.listEvents)
                    }
                } else {
                    Log.e(TAG, "Search failed: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                showLoading(false)
                Log.e(TAG, "Search failed: ${t.message}")
            }
        })
    }


    private fun setupRecyclerView(){
        val layoutManager = LinearLayoutManager(requireContext())
        binding.rvNameFinished.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(requireContext(), layoutManager.orientation)
        binding.rvNameFinished.addItemDecoration(itemDecoration)
    }

    private fun getListEvent(){
        showLoading(true)
        val client = ApiConfig.getApiService().getEvent(active = 0)
        client.enqueue(object : Callback<EventResponse> {
            override fun onResponse(call: Call<EventResponse>, response: Response<EventResponse>) {
                showLoading(false)
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        setEventData(responseBody.listEvents)
                    }
                } else {
                    Log.e(TAG, "onFailure ${response.message()}")
                }
            }

            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                showLoading(false)
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    private fun setEventData(listEvent: List<ListEventsItem>) {
        val adapter = EventAdapter {event ->
            val intent = Intent(requireContext(), EventDetailActivity::class.java)
            intent.putExtra("EVENT_NAME", event.name)
            intent.putExtra("EVENT_DESCRIPTION", event.description)
            intent.putExtra("EVENT_IMAGE", event.imageLogo)
            intent.putExtra("EVENT_LINK", event.link)
            startActivity(intent)
        }
        adapter.submitList(listEvent)
        binding.rvNameFinished.adapter = adapter
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}