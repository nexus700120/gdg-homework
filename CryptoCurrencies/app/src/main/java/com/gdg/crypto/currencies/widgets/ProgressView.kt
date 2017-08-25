package com.gdg.crypto.currencies.widgets

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v4.view.animation.LinearOutSlowInInterpolator
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.*
import com.gdg.crypto.currencies.R
import com.gdg.crypto.currencies.ext.toPx

/**
 * Created by vkirillov on 30.05.2017.
 */
class ProgressView : FrameLayout {

    constructor(context: Context?) : super(context) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private var progressView: ProgressBar? = null
    private var errorContainerView: LinearLayout? = null
    private var errorDescription: TextView? = null
    private var retryButton: SmallButton? = null
    private var retryCallback: (() -> Unit)? = null
    private var alphaAnimator: ValueAnimator? = null

    private fun init() {
        addProgressView()
        addErrorView()
    }

    fun setOnRetryListener(lambda: (() -> Unit)?) {
        retryCallback = lambda
    }

    fun isProgressVisible() = progressView?.visibility == View.VISIBLE
    
    private fun addProgressView() {
        progressView = ProgressBar(context)
        val params = FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT)
        params.gravity = Gravity.CENTER
        progressView?.layoutParams = params
        progressView?.visibility = View.GONE
        addView(progressView)
    }

    private fun addErrorView() {
        errorContainerView = LinearLayout(context)
        errorContainerView?.orientation = LinearLayout.VERTICAL
        errorContainerView?.layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT).apply {
            gravity = Gravity.CENTER_HORIZONTAL
        }

        val errorIconSize = 48.toPx()
        val errorIcon = ImageView(context)
        errorIcon.setImageResource(R.drawable.ic_warning)
        errorIcon.layoutParams = LinearLayout.LayoutParams(errorIconSize, errorIconSize).apply {
            gravity = Gravity.CENTER_HORIZONTAL
        }
        errorContainerView?.addView(errorIcon)

        errorDescription = TextView(context)
        errorDescription?.setTextColor(ContextCompat.getColor(context, R.color.error_text))
        errorDescription?.layoutParams = LinearLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT).apply {
            val margin = 16.toPx()
            leftMargin = margin
            rightMargin = margin
            topMargin = 8.toPx()
        }
        errorDescription?.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14F)

        retryButton = SmallButton(context)
        retryButton?.text = context.getString(R.string.btn_common_retry)
        retryButton?.layoutParams = LinearLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT).apply {
            gravity = Gravity.CENTER_HORIZONTAL
            topMargin = 8.toPx()
        }
        retryButton?.setOnClickListener { retryCallback?.invoke() }
        errorContainerView?.addView(errorDescription)
        errorContainerView?.addView(retryButton)
        errorContainerView?.visibility = View.GONE
        addView(errorContainerView)
    }

    fun showProgress(withAnimation: Boolean = true) {
        if (progressView?.visibility == View.VISIBLE) {
            return
        }

        if (!withAnimation) {
            val viewList = getViewList(progressView)
            errorContainerView?.visibility = View.INVISIBLE
            viewList.forEach { it.visibility = View.INVISIBLE }
            progressView?.visibility = View.VISIBLE
            progressView?.alpha = 1F
            return
        }

        val viewList = getViewList(progressView)

        progressView?.visibility = View.VISIBLE
        progressView?.alpha = 0F

        cancelAnimator()

        alphaAnimator = ValueAnimator.ofFloat(0F, 1F)
        alphaAnimator?.duration = 250
        alphaAnimator?.interpolator = LinearOutSlowInInterpolator()
        alphaAnimator?.addUpdateListener {
            val animatedValue = it.animatedValue as Float
            progressView?.alpha = animatedValue
            viewList.forEach { it.alpha = 1 - animatedValue }
        }
        alphaAnimator?.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                viewList.forEach { it.visibility = View.INVISIBLE }
            }

            override fun onAnimationCancel(animation: Animator?) {
                viewList.forEach { it.visibility = View.INVISIBLE }
            }
        })
        alphaAnimator?.start()
    }

    fun hideProgress(withAnimation: Boolean = true) {
        if (progressView?.visibility == View.GONE) {
            return
        }

        val viewList = getViewList(progressView, errorContainerView)

        if (!withAnimation) {
            progressView?.visibility = View.GONE
            viewList.forEach { it.alpha = 1.0F; it.visibility = View.VISIBLE }
            return
        }

        cancelAnimator()

        alphaAnimator = ValueAnimator.ofFloat(0F, 1F)
        alphaAnimator?.duration = 250
        alphaAnimator?.interpolator = LinearOutSlowInInterpolator()
        alphaAnimator?.addUpdateListener {
            val animatedValue = it.animatedValue as Float
            progressView?.alpha = 1 - animatedValue
            viewList.forEach { it.alpha = animatedValue }
        }
        alphaAnimator?.addListener(object : AnimatorListenerAdapter() {

            override fun onAnimationStart(animation: Animator?) {
                viewList.forEach { it.visibility = View.VISIBLE }
            }

            override fun onAnimationEnd(animation: Animator?) {
                progressView?.visibility = View.GONE
            }

            override fun onAnimationCancel(animation: Animator?) {
                progressView?.visibility = View.GONE
            }
        })
        alphaAnimator?.start()
    }

    fun error(message: String?) {
        val viewList = getViewList(errorContainerView)
        cancelAnimator()

        errorDescription?.text = message
        errorContainerView?.visibility = View.VISIBLE
        errorContainerView?.alpha = 0F

        alphaAnimator = ValueAnimator.ofFloat(0F, 1F)
        alphaAnimator?.duration = 250
        alphaAnimator?.interpolator = LinearOutSlowInInterpolator()
        alphaAnimator?.addUpdateListener {
            val animatedValue = it.animatedValue as Float
            errorContainerView?.alpha = animatedValue
            viewList.forEach { it.alpha = 1 - animatedValue }
        }
        alphaAnimator?.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                viewList.forEach { it.visibility = View.INVISIBLE }
            }
        })
        alphaAnimator?.start()
    }

    private fun cancelAnimator() {
        if (alphaAnimator?.isRunning == true) {
            alphaAnimator?.cancel()
        }
    }

    private fun getViewList(vararg excludeViews: View?): List<View> {
        val viewList = mutableListOf<View>()
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            if (!excludeViews.contains(child)) {
                viewList.add(child)
            }
        }
        return viewList
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        if (!changed) return
        (errorContainerView?.layoutParams as? FrameLayout.LayoutParams)?.let {
            it.topMargin = (height * 0.2).toInt()
        }
    }
}