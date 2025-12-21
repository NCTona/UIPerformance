package com.tona.myapplication.perceiveddemo

import android.os.Bundle
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.tona.myapplication.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

class SpinnerDemoFragment : Fragment(R.layout.fragment_spinner_demo) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val btnLoad = view.findViewById<Button>(R.id.btnLoad)
        val progress = view.findViewById<ProgressBar>(R.id.progress)
        val content = view.findViewById<TextView>(R.id.contentBox)

        btnLoad.setOnClickListener {

            // Random thời gian loading: 300ms → 2500ms
            val loadTime = Random.nextLong(300, 2500)

            // Placeholder hiển thị ngay
            content.text = "Loading content..."
            content.alpha = 0.5f
            content.visibility = View.VISIBLE

            // Quy tắc spinner theo nghiên cứu:
            // Nếu < 1s → không dùng spinner
            val showSpinner = loadTime >= 1000

            if (showSpinner) {
                progress.visibility = View.VISIBLE
                startFastSpinner(progress)
            } else {
                progress.visibility = View.GONE
            }

            viewLifecycleOwner.lifecycleScope.launch {
                delay(loadTime)

                progress.clearAnimation()
                progress.visibility = View.GONE

                // Fade-in che cảm giác chuyển trạng thái
                val fadeIn = AlphaAnimation(0f, 1f)
                fadeIn.duration = 300
                content.startAnimation(fadeIn)

                content.text =
                    "Content loaded in ${loadTime} ms\n\n" +
                            if (showSpinner)
                                "Spinner was shown (delay long enough)"
                            else
                                "Spinner omitted (delay was short)"

                content.alpha = 1f
            }
        }
    }

    /**
     * Spinner quay nhanh để làm thời gian chờ
     * được cảm nhận là ngắn hơn
     */
    private fun startFastSpinner(progress: ProgressBar) {
        val rotate = RotateAnimation(
            0f,
            360f,
            RotateAnimation.RELATIVE_TO_SELF,
            0.5f,
            RotateAnimation.RELATIVE_TO_SELF,
            0.5f
        )
        rotate.duration = 500          // quay nhanh
        rotate.repeatCount = RotateAnimation.INFINITE
        rotate.interpolator = LinearInterpolator()
        progress.startAnimation(rotate)
    }
}
