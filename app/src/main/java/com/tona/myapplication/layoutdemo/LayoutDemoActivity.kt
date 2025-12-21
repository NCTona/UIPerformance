package com.tona.myapplication.layoutdemo

import android.os.Bundle
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tona.myapplication.R

class LayoutDemoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_layout_demo)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        val switchLayout = findViewById<Switch>(R.id.switchLayout)

        recyclerView.layoutManager = LinearLayoutManager(this)

        fun updateAdapter(optimized: Boolean) {
            recyclerView.adapter =
                if (optimized) OptimizedAdapter()
                else BadLayoutAdapter()
        }

        // Mặc định: layout chưa tối ưu
        updateAdapter(false)

        switchLayout.setOnCheckedChangeListener { _, isChecked ->
            updateAdapter(isChecked)
        }
    }
}

