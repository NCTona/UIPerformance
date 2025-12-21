package com.tona.myapplication.animationdemo

import android.content.Context
import android.graphics.Color
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.FrameLayout
import com.tona.myapplication.R

class OptimizedAnimationView(context: Context, numBars: Int) : FrameLayout(context) {

    private val bars = mutableListOf<View>()

    init {
        for (i in 0 until numBars) {
            val container = FrameLayout(context)
            val containerParams = LayoutParams(LayoutParams.MATCH_PARENT, 50)
            containerParams.topMargin = i * 60
            container.layoutParams = containerParams
            container.setBackgroundResource(R.drawable.progress_bar_bg)
            container.setPadding(4, 4, 4, 4)
            addView(container)

            val bar = View(context)
            val barParams = LayoutParams(50, LayoutParams.MATCH_PARENT)
            bar.layoutParams = barParams
            bar.setBackgroundResource(R.drawable.progress_bar_fg)
            container.addView(bar)
            bars.add(bar)
        }
    }

    fun startOptimizedAnimation() {
        bars.forEachIndexed { index, bar ->
            animateBar(bar, index * 50L)
        }
    }

    private fun animateBar(bar: View, delay: Long) {
        bar.animate()
            .scaleX(10f)
            .setDuration(300)
            .setStartDelay(delay)
            .setInterpolator(LinearInterpolator()) // mượt
            .withEndAction {
                bar.animate()
                    .scaleX(1f)
                    .setDuration(500)
                    .setInterpolator(LinearInterpolator())
                    .withEndAction { animateBar(bar, 0) }
                    .start()
            }
            .start()
    }
}
