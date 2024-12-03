package com.example.submissionpertamafundamental.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.data.response.EventResponse
import com.data.response.ListEventsItem
import com.data.retrofit.ApiConfig
import com.example.submissionpertamafundamental.databinding.FragmentHomeBinding
import com.example.submissionpertamafundamental.ui.EventAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private lateinit var eventAdapterUpcoming: EventAdapter
    private lateinit var eventAdapterFinished: EventAdapter

    companion object {
        private const val TAG = "HomeFragment"
    }

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set up RecyclerView for Upcoming Events
        setupRecyclerViewUpcoming()

        // Set up RecyclerView for Finished Events
        setupRecyclerViewFinished()

        // Fetch events from API
        getListEvent()
    }

    private fun setupRecyclerViewUpcoming() {
        eventAdapterUpcoming = EventAdapter { event ->
            // Handle item click for Upcoming Events
            Log.d(TAG, "Upcoming Event clicked: ${event.name}")
        }
        val layoutManagerUpcoming = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.rvUpcomingEvents.layoutManager = layoutManagerUpcoming
        binding.rvUpcomingEvents.adapter = eventAdapterUpcoming
    }

    private fun setupRecyclerViewFinished() {
        eventAdapterFinished = EventAdapter { event ->
            // Handle item click for Finished Events
            Log.d(TAG, "Finished Event clicked: ${event.name}")
        }
        binding.rvFinishedEvents.layoutManager = LinearLayoutManager(context)
        binding.rvFinishedEvents.adapter = eventAdapterFinished
    }

    private fun getListEvent() {
        showLoadingUpcoming(true)
        showLoadingFinished(true)

        // Fetch Upcoming Events (Active Events)
        val clientUpcoming = ApiConfig.getApiService().getEvent(active = 1)
        clientUpcoming.enqueue(object : Callback<EventResponse> {
            override fun onResponse(call: Call<EventResponse>, response: Response<EventResponse>) {
                showLoadingUpcoming(false)
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        // Limit to 5 upcoming events
                        val upcomingEvents = responseBody.listEvents.take(5)
                        setUpcomingEventData(upcomingEvents)
                    }
                } else {
                    Log.e(TAG, "onFailure ${response.message()}")
                }
            }

            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                showLoadingUpcoming(false)
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })

        // Fetch Finished Events (Completed Events)
        val clientFinished = ApiConfig.getApiService().getEvent(active = 0)
        clientFinished.enqueue(object : Callback<EventResponse> {
            override fun onResponse(call: Call<EventResponse>, response: Response<EventResponse>) {
                showLoadingFinished(false)
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        // Limit to 5 finished events
                        val finishedEvents = responseBody.listEvents.take(5)
                        setFinishedEventData(finishedEvents)
                    }
                } else {
                    Log.e(TAG, "onFailure ${response.message()}")
                }
            }

            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                showLoadingFinished(false)
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }


    private fun showLoadingUpcoming(isLoading: Boolean) {
        binding.progressBarUpcoming.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.rvUpcomingEvents.visibility = if (isLoading) View.GONE else View.VISIBLE
    }

    private fun showLoadingFinished(isLoading: Boolean) {
        binding.progressBarFinished.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.rvFinishedEvents.visibility = if (isLoading) View.GONE else View.VISIBLE
    }

    private fun setUpcomingEventData(events: List<ListEventsItem>) {
        eventAdapterUpcoming.submitList(events)
    }

    private fun setFinishedEventData(events: List<ListEventsItem>) {
        eventAdapterFinished.submitList(events)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
