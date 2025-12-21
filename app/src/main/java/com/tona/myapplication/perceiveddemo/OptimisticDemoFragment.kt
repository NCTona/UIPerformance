package com.tona.myapplication.perceiveddemo

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.tona.myapplication.R
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

class OptimisticDemoFragment : Fragment(R.layout.fragment_optimistic_demo) {

    private var isLiked: Boolean? = null
    private var syncJob: Job? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val btnLike = view.findViewById<Button>(R.id.btnLike)
        val likeStatus = view.findViewById<TextView>(R.id.likeStatusText)
        val syncStatus = view.findViewById<TextView>(R.id.syncStatusText)
        val progressBar = view.findViewById<ProgressBar>(R.id.progressSync)

        // Trạng thái ban đầu
        resetUI(btnLike, likeStatus, syncStatus, progressBar)

        btnLike.setOnClickListener {

            // Toggle trạng thái
            isLiked = when (isLiked) {
                null -> true
                true -> false
                false -> true
            }

            // Update UI ngay lập tức
            updateLikeUI(btnLike, likeStatus)

            // Huỷ sync cũ
            syncJob?.cancel()

            // Sync trạng thái CUỐI
            syncJob = viewLifecycleOwner.lifecycleScope.launch {

                syncStatus.text = "Syncing with server..."
                progressBar.visibility = View.VISIBLE
                progressBar.progress = 100

                val totalTime = Random.nextLong(1000, 3000)
                val step = 50L
                val steps = (totalTime / step).toInt()

                repeat(steps) { i ->
                    delay(step)
                    progressBar.progress = 100 - (i * 100 / steps)
                }

                progressBar.visibility = View.GONE
                syncStatus.text =
                    if (isLiked == true) "Server state: LIKED"
                    else "Server state: UNLIKED"

            }
        }
    }

    private fun updateLikeUI(button: Button, statusText: TextView) {
        when (isLiked) {
            true -> {
                button.text = "Unlike"
                statusText.text = "You liked this post"
            }
            false -> {
                button.text = "Like"
                statusText.text = "You unliked this post"
            }
            null -> Unit
        }
    }

    private fun resetUI(
        button: Button,
        likeStatus: TextView,
        syncStatus: TextView,
        progressBar: ProgressBar
    ) {
        button.text = "Like"
        likeStatus.text = ""
        syncStatus.text = ""
        progressBar.visibility = View.GONE
    }
}
