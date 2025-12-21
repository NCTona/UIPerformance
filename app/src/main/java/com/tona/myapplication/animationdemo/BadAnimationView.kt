package com.tona.myapplication.animationdemo

import android.content.Context
import android.graphics.Color
import android.view.View
import android.widget.FrameLayout
import com.tona.myapplication.R

class BadAnimationView(
    context: Context,
    private val numBars: Int = 10  // số thanh động
) : FrameLayout(context) {

    private val bars = mutableListOf<View>()

    init {
        for (i in 0 until numBars) {
            // Container (background)
            val container = FrameLayout(context)
            val containerParams = LayoutParams(LayoutParams.MATCH_PARENT, 50)
            containerParams.topMargin = i * 60
            container.layoutParams = containerParams
            container.setBackgroundResource(R.drawable.progress_bar_bg) // background bo tròn
            container.setPadding(4, 4, 4, 4)
            addView(container)

            // Thanh progress (foreground)
            val bar = View(context)
            val barParams = LayoutParams(50, LayoutParams.MATCH_PARENT)
            bar.layoutParams = barParams
            bar.setBackgroundResource(R.drawable.progress_bar_fg) // foreground bo tròn
            container.addView(bar)
            bars.add(bar)
        }
    }

    fun startBadAnimation() {
        bars.forEachIndexed { index, bar ->
            animateBar(bar, index, true)
        }
    }

    private fun animateBar(bar: View, index: Int, increasing: Boolean) {
        var width = 50
        var inc = increasing
        postDelayed(object : Runnable {
            override fun run() {
                val params = bar.layoutParams
                params.width = width
                bar.layoutParams = params  // force layout lại -> CPU tốn

                if (inc) width += 20 else width -= 20
                if (width >= 500) inc = false
                if (width <= 50) inc = true

                postDelayed(this, 16)  // ~60 FPS
            }
        }, index * 50L)
    }
}
