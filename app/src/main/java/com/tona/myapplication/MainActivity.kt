package com.tona.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.tona.myapplication.animationdemo.AnimationDemoActivity
import com.tona.myapplication.layoutdemo.LayoutDemoActivity
import com.tona.myapplication.openglesdemo.TextureCompareActivity
import com.tona.myapplication.perceiveddemo.PerceivedDemoActivity
import com.tona.myapplication.threaddemo.ThreadDemoActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun openLayoutDemo(v: View) =
        startActivity(Intent(this, LayoutDemoActivity::class.java))

    fun openThreadDemo(v: View) =
        startActivity(Intent(this, ThreadDemoActivity::class.java))

    fun openAnimationDemo(v: View) =
        startActivity(Intent(this, AnimationDemoActivity::class.java))

    fun openPerceivedDemo(v: View) =
        startActivity(Intent(this, PerceivedDemoActivity::class.java))

    fun openOpenGLESDemo(v: View) =
        startActivity(Intent(this, TextureCompareActivity::class.java))
}
