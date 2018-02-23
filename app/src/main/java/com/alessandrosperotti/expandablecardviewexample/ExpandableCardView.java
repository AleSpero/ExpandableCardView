package com.alessandrosperotti.expandablecardviewexample;


import android.content.Context;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.animation.Transformation;
import android.widget.ImageButton;

import static android.content.ContentValues.TAG;

/**
 * Created by alessandros on 23/02/2018.
 *
 * @author Alessandro Sperotti
 */

//TODO Aggiungi listeners iscollapsing/isExpanding

public class ExpandableCardView extends CardView {

    private final static int COLLAPSING = 0;
    private final static int EXPANDING = 1;

    private boolean isExpanded = false;
    private boolean isExpanding = false;
    private boolean isCollapsing = false;

    private int previousHeight = 0;

    public ExpandableCardView(Context context) {
        super(context);
    }

    public ExpandableCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ExpandableCardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void expand() {

        final int initialHeight = getHeight();


        if(!isMoving()) {
            previousHeight = initialHeight;
        }

        measure(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        int targetHeight = getMeasuredHeight();

        animateViews(initialHeight,
                targetHeight - initialHeight,
                EXPANDING);
    }

    public void collapse() {
        int initialHeight = getMeasuredHeight();

        animateViews(initialHeight,
                initialHeight - previousHeight,
                COLLAPSING);
    }


    public boolean isExpanded() {
        return isExpanded;
    }

    private void animateViews(final int initialHeight, final int distance, final int animationType){

        ImageButton arrowBtn = findViewById(R.id.arrow);

        Animation expandAnimation = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if (interpolatedTime == 1){
                    //Setting isExpanding/isCollapsing to false
                    isExpanding = false;
                    isCollapsing = false;
                }

                getLayoutParams().height = animationType == EXPANDING ? (int) (initialHeight + (distance * interpolatedTime)) :
                        (int) (initialHeight - (distance * interpolatedTime));
                requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        RotateAnimation arrowAnimation = animationType == EXPANDING ?
                new RotateAnimation(0,180,Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f) :
                new RotateAnimation(180,0,Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                        0.5f);

        arrowAnimation.setFillAfter(true);


        arrowAnimation.setDuration((long) distance);
        expandAnimation.setDuration((long) distance);

        isExpanding = animationType == EXPANDING;
        isCollapsing = animationType == COLLAPSING;

        startAnimation(expandAnimation);
        arrowBtn.startAnimation(arrowAnimation);

        isExpanded = animationType == EXPANDING;

    }

    private boolean isExpanding(){
        return isExpanding;
    }

    private boolean isCollapsing(){
        return isCollapsing;
    }

    private boolean isMoving(){
        return isExpanded() || isCollapsing();
    }
}
