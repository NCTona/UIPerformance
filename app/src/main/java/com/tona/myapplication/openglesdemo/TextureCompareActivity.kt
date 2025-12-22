package com.tona.myapplication.openglesdemo

import android.annotation.SuppressLint
import android.opengl.GLSurfaceView
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.tona.myapplication.R

class TextureCompareActivity : AppCompatActivity() {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_texture_compare)

        val glView = findViewById<GLSurfaceView>(R.id.glSurface)
        val btn = findViewById<Button>(R.id.btnToggle)
        val tv = findViewById<TextView>(R.id.tvMode)

        val renderer = TextureCompareRenderer(this)

        glView.setEGLContextClientVersion(2)
        glView.setRenderer(renderer)

        btn.setOnClickListener {
            renderer.mode = when (renderer.mode) {
                DisplayMode.ORIGINAL -> DisplayMode.ETC1
                DisplayMode.ETC1 -> DisplayMode.DIFFERENCE
                DisplayMode.DIFFERENCE -> DisplayMode.ORIGINAL
            }
            tv.text = "MODE: ${renderer.mode}"
        }
    }
}
