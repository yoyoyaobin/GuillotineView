package com.hyb.guillotine.Animation

import android.animation.Animator
import android.animation.ObjectAnimator
import android.animation.TimeInterpolator
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import com.hyb.guillotine.Interpolator.GuillotineInterpolator
import com.hyb.guillotine.`interface`.GuillotineListener

/**
 * 类描述：GuillotineView
 * 创建人：huangyaobin
 * 创建时间：2019/5/6
 * 修改人：
 * 修改时间：
 * 修改备注：
 */
class GuillotineView(mBuilder:GuillotineBuilder) {

    private val ROTATION = "rotation"
    private val GUILLOTINE_CLOSED_ANGLE_RIGHT_TO_LEFT = -180f
    private val GUILLOTINE_CLOSED_ANGLE_LEFT_TO_RIGHT = 180f
    private val GUILLOTINE_OPENED_ANGLE = 0f
    private var DEFAULT_DURATION = 625

    private var mGuillotineView: View? = null
    private var mDuration: Long = 0
    private var mOpeningAnimation: ObjectAnimator? = null
    private var mClosingAnimation: ObjectAnimator? = null
    private var mListener: GuillotineListener? = null
    private var mInterpolator: TimeInterpolator? = null
    private var mDelay: Long = 0

    private var isOpening: Boolean = false
    private var isClosing: Boolean = false
    private var isLeftBtn: Boolean = false
    private var isRightBtn: Boolean = false
    private var closeAngle: Float = 0f


    private var mBuilder: GuillotineBuilder? = null

    init {
        this.mBuilder = mBuilder
        this.mGuillotineView = mBuilder.guillotineView
        this.mDuration = if(mBuilder.duration > 0){
            mBuilder.duration
        }else{
            DEFAULT_DURATION.toLong()
        }
        this.mListener = mBuilder.guillotineListener
        this.mDelay = mBuilder.startDelay
        this.mInterpolator = if(mBuilder.interpolator == null){
            GuillotineInterpolator()
        }else{
            mBuilder.interpolator
        }
        this.isLeftBtn = mBuilder.isLeftBtn
        this.isRightBtn = mBuilder.isRightBtn

        closeAngle = when(isLeftBtn){
            true->GUILLOTINE_CLOSED_ANGLE_RIGHT_TO_LEFT
            false->GUILLOTINE_CLOSED_ANGLE_LEFT_TO_RIGHT
        }

        addGuillotineViewToRootView()

        setAnimationPivot(mBuilder.openingView)
        this.mOpeningAnimation = buildOpeningAnimation()
        this.mClosingAnimation = buildClosingAnimation()

        mBuilder.openingView?.setOnClickListener {
            open()
        }

        mBuilder.closingView?.setOnClickListener {
            close()
        }

        if (mBuilder.isClosedOnStart) {
            mGuillotineView?.rotation = closeAngle
            mGuillotineView?.visibility = View.INVISIBLE
        }

    }

    private fun addGuillotineViewToRootView(){
        val rooView = ((mBuilder?.mContext as Activity).findViewById<View>(android.R.id.content) as ViewGroup).getChildAt(0) as ViewGroup
        rooView.addView(mBuilder?.guillotineView)
    }

    private fun open(){
        if (!isOpening) {
            mOpeningAnimation?.start()
        }
    }

    private fun close(){
        if (!isClosing) {
            mClosingAnimation?.start()
        }
    }

    @SuppressLint("ObjectAnimatorBinding")
    private fun buildOpeningAnimation(): ObjectAnimator {

        val rotationAnimator = initAnimator(
            ObjectAnimator.ofFloat(
                mGuillotineView,
                ROTATION,
                closeAngle,
                GUILLOTINE_OPENED_ANGLE
            )
        )
        rotationAnimator.interpolator = mInterpolator
        rotationAnimator.duration = mDuration
        rotationAnimator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {
                mGuillotineView?.visibility = View.VISIBLE
                isOpening = true
            }

            override fun onAnimationEnd(animation: Animator) {
                isOpening = false
                mListener?.onGuillotineOpened()
            }

            override fun onAnimationCancel(animation: Animator) {

            }

            override fun onAnimationRepeat(animation: Animator) {

            }
        })
        return rotationAnimator
    }

    @SuppressLint("ObjectAnimatorBinding")
    private fun buildClosingAnimation(): ObjectAnimator {
        val rotationAnimator = initAnimator(
            ObjectAnimator.ofFloat(
                mGuillotineView,
                ROTATION,
                GUILLOTINE_OPENED_ANGLE,
                closeAngle
            )
        )
        rotationAnimator.duration = (mDuration * GuillotineInterpolator.ROTATION_TIME).toLong()
        rotationAnimator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {
                isClosing = true
                mGuillotineView?.visibility = View.VISIBLE
            }

            override fun onAnimationEnd(animation: Animator) {
                isClosing = false
                mGuillotineView?.visibility = View.GONE
                mListener?.onGuillotineClosed()
            }

            override fun onAnimationCancel(animation: Animator) {

            }

            override fun onAnimationRepeat(animation: Animator) {

            }
        })
        return rotationAnimator
    }

    private fun initAnimator(animator: ObjectAnimator): ObjectAnimator {
        animator.startDelay = mDelay
        return animator
    }

    private fun calculatePivotY(burger: View): Float {
        return (burger.top + burger.height / 2).toFloat()
    }

    private fun calculatePivotX(burger: View): Float {
        return (burger.left + burger.width / 2).toFloat()
    }


    private fun setAnimationPivot(openView: View?) {
        mGuillotineView?.viewTreeObserver?.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    mGuillotineView?.viewTreeObserver?.removeOnGlobalLayoutListener(this)
                } else {
                    mGuillotineView?.viewTreeObserver?.removeGlobalOnLayoutListener(this)
                }
                mGuillotineView?.pivotX = calculatePivotX(openView!!)
                mGuillotineView?.pivotY = calculatePivotY(openView!!)
            }
        })
    }

    /**
     * 工厂
     */
    class GuillotineBuilder(context: Context){
        var mContext: Context? = context
        var guillotineView: View? = null
        var openingView: View? = null
        var closingView: View? = null
        var guillotineListener: GuillotineListener? = null
        var duration: Long = 0
        var startDelay: Long = 0
        var interpolator: TimeInterpolator? = null
        var isClosedOnStart: Boolean = false
        var isLeftBtn = false
        var isRightBtn = false

        fun setGuillotineView(guillotineView: View): GuillotineBuilder {
            this.guillotineView = guillotineView
            return this
        }


        fun setCloseItemView(closingView: View): GuillotineBuilder {
            this.closingView = closingView
            return this
        }

        fun setOpenItemView(openingView: View): GuillotineBuilder {
            this.openingView = openingView
            return this
        }

        fun setGuillotineListener(guillotineListener: GuillotineListener): GuillotineBuilder {
            this.guillotineListener = guillotineListener
            return this
        }

        fun setDuration(duration: Long): GuillotineBuilder {
            this.duration = duration
            return this
        }

        fun setStartDelay(startDelay: Long): GuillotineBuilder {
            this.startDelay = startDelay
            return this
        }

        fun setIsLeftBtn(isLeftBtn: Boolean): GuillotineBuilder {
            this.isLeftBtn = isLeftBtn
            return this
        }

        fun setIsRightBtn(isRightBtn: Boolean): GuillotineBuilder {
            this.isRightBtn = isRightBtn
            return this
        }

        fun setInterpolator(interpolator: TimeInterpolator): GuillotineBuilder {
            this.interpolator = interpolator
            return this
        }

        fun setClosedOnStart(isClosedOnStart: Boolean): GuillotineBuilder {
            this.isClosedOnStart = isClosedOnStart
            return this
        }

        fun build(): GuillotineView {
            return GuillotineView(this)
        }
    }

}