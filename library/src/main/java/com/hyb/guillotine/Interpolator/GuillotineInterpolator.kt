package com.hyb.guillotine.Interpolator

import android.animation.TimeInterpolator

class GuillotineInterpolator : TimeInterpolator {

    override fun getInterpolation(t: Float): Float {
        return when {
            t < ROTATION_TIME -> rotation(t)
            t < ROTATION_TIME + FIRST_BOUNCE_TIME -> firstBounce(t)
            else -> secondBounce(t)
        }
    }

    private fun rotation(t: Float): Float {
        return 4.592f * t * t
    }

    private fun firstBounce(t: Float): Float {
        return 2.5f * t * t - 3f * t + 1.85556f
    }

    private fun secondBounce(t: Float): Float {
        return 0.625f * t * t - 1.083f * t + 1.458f
    }

    companion object {

        const val ROTATION_TIME = 0.46667f
        const val FIRST_BOUNCE_TIME = 0.26666f
        val SECOND_BOUNCE_TIME = 0.26667f
    }
}
