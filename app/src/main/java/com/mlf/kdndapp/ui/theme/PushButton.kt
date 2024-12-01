package com.mlf.kdndapp.ui.theme

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View.OnTouchListener
import android.widget.TextView

@SuppressLint("ClickableViewAccessibility")
class PushButton : TextView
{
    private val TIME_STEP_MAX: Long = 200
    private val TIME_STEP_MIN: Long = 80
    private val TIME_STEP_DELTA: Long = 20
    private val ACTION_NONE = 0
    private val ACTION_PUSH = 1

    private var runAction: Runnable? = null
    private var threadAction: Thread? = null
    private var action: Int = ACTION_NONE
    private var onAction: (()->Unit) = { }

    constructor(context: Context?) : this(context, null)

    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context?, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle)
    {
        runAction = Runnable {
            var step: Long = TIME_STEP_MAX
            var next = System.currentTimeMillis() + step
            onAction()
            while (action != ACTION_NONE) {
                if (System.currentTimeMillis() >= next) {
                    onAction()
                    if (step > TIME_STEP_MIN) {
                        step -= TIME_STEP_DELTA
                    }
                    next = System.currentTimeMillis() + step
                }
            }
        }

        setOnTouchListener(OnTouchListener { v, event ->
            val x = event.x.toInt()
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    if(action == ACTION_NONE)
                    {
                        action = ACTION_PUSH
                        threadAction = Thread(runAction)
                        threadAction!!.start()
                    }
                    return@OnTouchListener true
                }
                MotionEvent.ACTION_UP -> {
                    if(action != ACTION_NONE)
                    {
                        action = ACTION_NONE
                    }
                    return@OnTouchListener true
                }
            }
            false
        })
    }
    fun setAction(onAction: (()->Unit) = { })
    {
        this.onAction = onAction
    }
}