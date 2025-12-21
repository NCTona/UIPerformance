package com.tona.myapplication.animationdemo

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.tona.myapplication.R

class AnimationDemoActivity : AppCompatActivity() {

    private lateinit var container: FrameLayout
    private lateinit var switchAnim: Switch
    private lateinit var edtNumBars: EditText
    private lateinit var btnUpdate: Button

    private var numBars = 5 // mặc định số thanh

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_animation_demo)

        container = findViewById(R.id.container)
        switchAnim = findViewById(R.id.switchAnim)
        edtNumBars = findViewById(R.id.edtNumBars)
        btnUpdate = findViewById(R.id.btnUpdate)

        // Hiển thị số thanh hiện tại trong EditText
        edtNumBars.setText(numBars.toString())

        showCurrentAnimation() // hiển thị animation mặc định

        // Cập nhật số thanh khi nhấn nút
        btnUpdate.setOnClickListener {
            val input = edtNumBars.text.toString()
            val value = input.toIntOrNull()
            if (value != null && value > 0) {
                numBars = value
                edtNumBars.setText(numBars.toString()) // đảm bảo EditText hiển thị số mới
                showCurrentAnimation()
            } else {
                Toast.makeText(this, "Nhập số nguyên dương hợp lệ", Toast.LENGTH_SHORT).show()
            }
        }

        // Chuyển đổi BAD / OPTIMIZED
        switchAnim.setOnCheckedChangeListener { _, _ ->
            showCurrentAnimation()
        }
    }

    private fun showCurrentAnimation() {
        container.removeAllViews()
        if (switchAnim.isChecked) {
            val view = OptimizedAnimationView(this, numBars)
            container.addView(view)
            view.startOptimizedAnimation()
        } else {
            val view = BadAnimationView(this, numBars)
            container.addView(view)
            view.startBadAnimation()
        }
    }
}
