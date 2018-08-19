package com.alessandrosperotti.expandablecardviewexample;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.animation.Transformation;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by alessandros on 23/02/2018.
 *
 * @author Alessandro Sperotti
 */

//TODO Aggiungi listeners iscollapsing/isExpanding

public class ExpandableCardView extends LinearLayout {

    private String title;

    private View innerView;
    private ViewGroup containerView;
    private ViewGroup header;

    private ImageButton arrowBtn;
    private TextView textViewTitle;

    private TypedArray typedArray;
    private int innerViewRes;


    private boolean firstTime = false;


    private final static int COLLAPSING = 0;
    private final static int EXPANDING = 1;

    private boolean isExpanded = false;
    private boolean isExpanding = false;
    private boolean isCollapsing = false;

    private int previousHeight = 0;
    private int innerViewHeight;
    private int titleColor;
    private int headerBackgroundColor;
    private Drawable headerBackgroundDrawable;

    public ExpandableCardView(Context context) {
        super(context);
    }

    public ExpandableCardView(Context context, AttributeSet attrs) {
        super(context, attrs);

        initAttributes(context, attrs);
        initView(context);
    }

    public ExpandableCardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initAttributes(context, attrs);
        initView(context);
    }

    private void initView(Context context) {
        //Inflating View
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.expandable_cardview, this);
    }

    private void initAttributes(Context context, AttributeSet attrs) {
        //Ottengo attributi
        typedArray = context.obtainStyledAttributes(attrs, R.styleable.ExpandableCardView);
        title = typedArray.getString(R.styleable.ExpandableCardView_title);
        innerViewRes = typedArray.getResourceId(R.styleable.ExpandableCardView_inner_view, View.NO_ID);
        titleColor = typedArray.getColor(R.styleable.ExpandableCardView_titleColor, getContext().getResources().getColor(android.R.color.black));
        headerBackgroundColor = typedArray.getColor(R.styleable.ExpandableCardView_headerBackgroundColor, getContext().getResources().getColor(android.R.color.white));
        headerBackgroundDrawable = typedArray.getDrawable(R.styleable.ExpandableCardView_headerBackgroundDrawable);
        typedArray.recycle();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        //Una volta che la view è inflatata setto tutto

        arrowBtn = findViewById(R.id.arrow);
        textViewTitle = findViewById(R.id.title);

        //Inflato inner view
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        containerView = findViewById(R.id.viewContainer);
        //the header relative layout to set background
        header = findViewById(R.id.header);
        innerView = inflater.inflate(innerViewRes, null);
        //innerView.setVisibility(View.INVISIBLE);

        if (!TextUtils.isEmpty(title)) textViewTitle.setText(title);

        header.setBackgroundColor(headerBackgroundColor);
        textViewTitle.setTextColor(titleColor);
        if (!(headerBackgroundDrawable == null))
            header.setBackground(headerBackgroundDrawable);
    }

    //setting title color
    public void setTitleColor(int resId) {
        textViewTitle.setTextColor(getContext().getResources().getColor(resId));
    }

    //setting header color
    public void setHeader(int resId) {
        header.setBackgroundColor(getContext().getResources().getColor(resId));
    }

    //setting header drawable
    public void setHeader(Drawable drawable) {
        header.setBackground(drawable);
    }

    public void expand() {

        final int initialHeight = getHeight();


        if (!isMoving()) {
            previousHeight = initialHeight;
            containerView.addView(innerView);
        }

        measure(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        int targetHeight = innerView.getMeasuredHeight() + initialHeight;

        if (targetHeight - initialHeight != 0) {
            if (firstTime) {
                containerView.getLayoutParams().height = initialHeight;
                firstTime = false;
            }
            animateViews(initialHeight,
                    targetHeight - initialHeight,
                    EXPANDING);
        }
    }

    public void collapse() {
        int initialHeight = getMeasuredHeight();

        if (initialHeight - previousHeight != 0) {
            animateViews(initialHeight,
                    initialHeight - previousHeight,
                    COLLAPSING);
        }

    }

    public boolean isExpanded() {
        return isExpanded;
    }

    private void animateViews(final int initialHeight, final int distance, final int animationType) {

        Animation expandAnimation = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if (interpolatedTime == 1) {
                    //Setting isExpanding/isCollapsing to false
                    isExpanding = false;
                    isCollapsing = false;

                    if (animationType == COLLAPSING) {
                        containerView.removeView(innerView);
                    }
                }

                getLayoutParams().height = animationType == EXPANDING ? (int) (initialHeight + (distance * interpolatedTime)) :
                        (int) (initialHeight - (distance * interpolatedTime));
                requestLayout();

                Log.d("Animation", "" + containerView.getHeight() + " " + interpolatedTime);


            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        RotateAnimation arrowAnimation = animationType == EXPANDING ?
                new RotateAnimation(0, 180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                        0.5f) :
                new RotateAnimation(180, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                        0.5f);

        arrowAnimation.setFillAfter(true);


        arrowAnimation.setDuration((long) distance);
        expandAnimation.setDuration((long) distance);

        isExpanding = animationType == EXPANDING;
        isCollapsing = animationType == COLLAPSING;

        startAnimation(expandAnimation);
        Log.d("SO", "Started animation: " + (animationType == EXPANDING ? "Expanding" : "Collapsing"));
        arrowBtn.startAnimation(arrowAnimation);
        isExpanded = animationType == EXPANDING;

    }

    private boolean isExpanding() {
        return isExpanding;
    }

    private boolean isCollapsing() {
        return isCollapsing;
    }

    private boolean isMoving() {
        return isExpanding() || isCollapsing();
    }
}
