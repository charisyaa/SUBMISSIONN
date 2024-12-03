package com.example.submissionpertamafundamental.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.submissionpertamafundamental.R
import com.example.submissionpertamafundamental.databinding.ActivityEventDetailBinding

class EventDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_event_detail)

        val imgMediaCover: ImageView = findViewById(R.id.imgMediaCover)
        val tvEventName: TextView = findViewById(R.id.tvEventName)
        val tvOwnerName: TextView = findViewById(R.id.tvOwnerName)
        val tvBeginTime: TextView = findViewById(R.id.tvBeginTime)
        val tvQuota: TextView = findViewById(R.id.tvQuota)
        val tvDescription: TextView = findViewById(R.id.tvDescription)
        val btnOpenLink: Button = findViewById(R.id.btnOpenLink)

        val eventName = intent.getStringExtra("EVENT_NAME") ?: "Event Name"
        val eventDescription = intent.getStringExtra("EVENT_DESCRIPTION") ?: "Description not available"
        val eventImage = intent.getStringExtra("EVENT_IMAGE") ?: ""
        val eventLink = intent.getStringExtra("EVENT_LINK") ?: "https://example.com"
        val ownerName = intent.getStringExtra("OWNER_NAME") ?: "Unknown Organizer"
        val beginTime = intent.getStringExtra("BEGIN_TIME") ?: "Not Specified"
        val quota = intent.getIntExtra("QUOTA", 0)
        val registrant = intent.getIntExtra("REGISTRANT", 0)
        val remainingQuota = quota - registrant

        tvEventName.text = eventName
        tvOwnerName.text = "Organized by: $ownerName"
        tvBeginTime.text = "Event Time: $beginTime"
        tvQuota.text = "Remaining Quota: $remainingQuota"
        tvDescription.text = HtmlCompat.fromHtml(eventDescription, HtmlCompat.FROM_HTML_MODE_LEGACY)
        Glide.with(this).load(eventImage).into(imgMediaCover)

        btnOpenLink.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(eventLink))
            startActivity(intent)
        }
    }
}