package com.uuabc.classroomlib.widget

import android.animation.Animator
import android.animation.ObjectAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieCompositionFactory

open class EmojiLottieView : LottieAnimationView {
    private lateinit var alphaAnimator: ObjectAnimator
    private lateinit var json: String

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attributes: AttributeSet?) : super(context, attributes) {
        initView()
    }

    private fun initView() {
        this.addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(p0: Animator?) {
            }

            override fun onAnimationEnd(p0: Animator?) {
                alphaAnimator.start()
            }

            override fun onAnimationCancel(p0: Animator?) {
            }

            override fun onAnimationStart(p0: Animator?) {
            }

        })
        initAnimator()
    }

    private fun initAnimator() {
        alphaAnimator = ObjectAnimator.ofFloat(this, "Alpha", 0f)
        alphaAnimator.duration = 300
        alphaAnimator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(p0: Animator?) {
            }

            override fun onAnimationEnd(p0: Animator?) {
                reset()
            }

            override fun onAnimationCancel(p0: Animator?) {
            }

            override fun onAnimationStart(p0: Animator?) {

            }

        })
    }

    fun reset() {
        this.translationX = (-this.width).toFloat()
        this.alpha = 1f
        this.visibility = View.GONE
    }

    fun viewInSide(json: String) {
        this.visibility = View.VISIBLE
        this.json = json
        val lottieResult = LottieCompositionFactory.fromJsonStringSync(json, "json") ?: return
        if (lottieResult.value == null) return
        setComposition(lottieResult.value!!)
    }
}