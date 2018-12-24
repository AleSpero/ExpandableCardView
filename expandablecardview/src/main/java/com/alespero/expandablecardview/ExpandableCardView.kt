package com.alespero.expandablecardview


import android.content.Context
import android.content.res.TypedArray
import android.graphics.drawable.Drawable
import android.os.Build
import androidx.core.content.ContextCompat
import android.text.TextUtils
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.view.animation.Transformation
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.expandable_cardview.view.*

/**
 *
 * Copyright (c) 2018 Alessandro Sperotti
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 *
 * Created by alessandros on 23/02/2018.
 * Modified by Adrian Devezin on 24/12/2018.
 * @author Alessandro Sperotti
 */

class ExpandableCardView : LinearLayout {

    private var title: String? = null

    private var innerView: View? = null

    private var typedArray: TypedArray? = null
    private var innerViewRes: Int = 0
    private var iconDrawable: Drawable? = null

    var animDuration = DEFAULT_ANIM_DURATION.toLong()

    var isExpanded = false
        private set
    private var isExpanding = false
    private var isCollapsing = false
    private var expandOnClick = false
    private var startExpanded = false

    private var previousHeight = 0

    private var listener: OnExpandedListener? = null

    private val defaultClickListener = OnClickListener {
        if (isExpanded)
            collapse()
        else
            expand()
    }

    private val isMoving: Boolean
        get() = isExpanding || isCollapsing

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initAttributes(context, attrs)
        initView(context)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initAttributes(context, attrs)
        initView(context)
    }

    private fun initView(context: Context) {
        //Inflating View
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.expandable_cardview, this)
    }

    private fun initAttributes(context: Context, attrs: AttributeSet) {
        //Ottengo attributi
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ExpandableCardView)
        this@ExpandableCardView.typedArray = typedArray
        title = typedArray.getString(R.styleable.ExpandableCardView_title)
        iconDrawable = typedArray.getDrawable(R.styleable.ExpandableCardView_icon)
        innerViewRes = typedArray.getResourceId(R.styleable.ExpandableCardView_inner_view, View.NO_ID)
        expandOnClick = typedArray.getBoolean(R.styleable.ExpandableCardView_expandOnClick, false)
        animDuration = typedArray.getInteger(R.styleable.ExpandableCardView_animationDuration, DEFAULT_ANIM_DURATION).toLong()
        startExpanded = typedArray.getBoolean(R.styleable.ExpandableCardView_startExpanded, false)
        typedArray.recycle()
    }

    override fun onFinishInflate() {
        super.onFinishInflate()

        //Setting attributes
        if (! TextUtils.isEmpty(title)) card_title.text = title

        if (iconDrawable != null) {
            card_header.visibility = View.VISIBLE
            card_icon.background = iconDrawable
        }

        setInnerView(innerViewRes)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            elevation = Utils.convertDpToPixels(context, 4f)

        if (startExpanded) {
            animDuration = 0
            expand()
        }

        if (expandOnClick) {
            card.setOnClickListener(defaultClickListener)
            card_arrow.setOnClickListener(defaultClickListener)
        }

    }

    fun expand() {

        val initialHeight = card.height

        if (! isMoving) {
            previousHeight = initialHeight
        }

        card.measure(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        val targetHeight = card.measuredHeight

        if (targetHeight - initialHeight != 0) {
            animateViews(initialHeight,
                    targetHeight - initialHeight,
                    EXPANDING)
        }
    }

    fun collapse() {
        val initialHeight = card.measuredHeight

        if (initialHeight - previousHeight != 0) {
            animateViews(initialHeight,
                    initialHeight - previousHeight,
                    COLLAPSING)
        }

    }

    private fun animateViews(initialHeight: Int, distance: Int, animationType: Int) {

        val expandAnimation = object : Animation() {
            override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
                if (interpolatedTime == 1f) {
                    //Setting isExpanding/isCollapsing to false
                    isExpanding = false
                    isCollapsing = false

                    listener?.let { listener ->
                        if (animationType == EXPANDING) {
                            listener.onExpandChanged(card, true)
                        } else {
                            listener.onExpandChanged(card, false)
                        }
                    }
                }

                card.layoutParams.height = if (animationType == EXPANDING)
                    (initialHeight + distance * interpolatedTime).toInt()
                else
                    (initialHeight - distance * interpolatedTime).toInt()
                card_container.requestLayout()

                card_container.layoutParams.height = if (animationType == EXPANDING)
                    (initialHeight + distance * interpolatedTime).toInt()
                else
                    (initialHeight - distance * interpolatedTime).toInt()

            }

            override fun willChangeBounds(): Boolean {
                return true
            }
        }

        val arrowAnimation = if (animationType == EXPANDING)
            RotateAnimation(0f, 180f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                    0.5f)
        else
            RotateAnimation(180f, 0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                    0.5f)

        arrowAnimation.fillAfter = true


        arrowAnimation.duration = animDuration
        expandAnimation.duration = animDuration

        isExpanding = animationType == EXPANDING
        isCollapsing = animationType == COLLAPSING

        startAnimation(expandAnimation)
        Log.d("SO", "Started animation: " + if (animationType == EXPANDING) "Expanding" else "Collapsing")
        card_arrow.startAnimation(arrowAnimation)
        isExpanded = animationType == EXPANDING

    }

    fun setOnExpandedListener(listener: OnExpandedListener) {
        this.listener = listener
    }

    fun setOnExpandedListener(method: (v: View?, isExpanded: Boolean) -> Unit) {
        this.listener = object : OnExpandedListener {
            override fun onExpandChanged(v: View?, isExpanded: Boolean) {
                method(v, isExpanded)
            }
        }
    }

    fun removeOnExpandedListener() {
        this.listener = null
    }

    fun setTitle(title: String) {
        card_title.text = title
    }

    fun setTitle(resId: Int) {
        card_title.setText(resId)
    }

    fun setIcon(drawable: Drawable) {
        card_header.background = drawable
        iconDrawable = drawable

    }

    fun setIcon(resId: Int) {
        iconDrawable = ContextCompat.getDrawable(context, resId)
        card_header.background = iconDrawable
    }

    private fun setInnerView(resId: Int) {
        card_stub.layoutResource = resId
        innerView = card_stub.inflate()
    }


    override fun setOnClickListener(l: View.OnClickListener?) {
        card_arrow.setOnClickListener(l)
        super.setOnClickListener(l)
    }


    /**
     * Interfaces
     */

    interface OnExpandedListener {

        fun onExpandChanged(v: View?, isExpanded: Boolean)

    }

    companion object {

        private const val DEFAULT_ANIM_DURATION = 350

        private const val COLLAPSING = 0
        private const val EXPANDING = 1
    }

}


