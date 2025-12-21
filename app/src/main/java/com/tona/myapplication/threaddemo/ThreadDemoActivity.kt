package com.tona.myapplication.threaddemo

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.tona.myapplication.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ThreadDemoActivity : AppCompatActivity() {

    private lateinit var btnBad: Button
    private lateinit var btnGood: Button
    private lateinit var progress: ProgressBar
    private lateinit var resultBox: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_thread_demo)

        btnBad = findViewById(R.id.btnBad)
        btnGood = findViewById(R.id.btnGood)
        progress = findViewById(R.id.progress)
        resultBox = findViewById(R.id.resultBox)

        setupBadDemo()
        setupOptimizedDemo()
    }

    /**
     * BAD: xử lý nặng trên UI thread
     */
    private fun setupBadDemo() {
        btnBad.setOnClickListener {
            progress.visibility = View.VISIBLE
            resultBox.visibility = View.GONE

            // Block UI thread
            val result = heavyCalculation()

            progress.visibility = View.GONE
            resultBox.text =
                "BAD (UI Thread)\n\n" +
                        "Tác vụ nặng được thực hiện trực tiếp trên UI thread.\n" +
                        "Trong thời gian xử lý, giao diện bị đơ.\n\n" +
                        "Kết quả: $result"
            resultBox.visibility = View.VISIBLE
        }
    }

    /**
     * GOOD: xử lý nặng trên background thread
     */
    private fun setupOptimizedDemo() {
        btnGood.setOnClickListener {
            progress.visibility = View.VISIBLE
            resultBox.visibility = View.GONE

            lifecycleScope.launch {
                val result = withContext(Dispatchers.Default) {
                    heavyCalculation()
                }

                progress.visibility = View.GONE
                resultBox.text =
                    "OPTIMIZED (Background Thread)\n\n" +
                            "Tác vụ nặng được chuyển sang background thread.\n" +
                            "UI vẫn phản hồi mượt mà trong quá trình xử lý.\n\n" +
                            "Kết quả: $result"
                resultBox.visibility = View.VISIBLE
            }
        }
    }

    /**
     * Giả lập tác vụ nặng
     */
    private fun heavyCalculation(): Int {
        Thread.sleep(2000) // giả lập xử lý 2s
        return (1..1000).sum()
    }
}
