package com.alespero.expandablecardview;


import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
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

    private ImageButton arrowBtn;
    private TextView textViewTitle;

    private TypedArray typedArray;
    private int innerViewRes;

    private CardView card;

    public static final long ANIM_DURATION = 350;

    private final static int COLLAPSING = 0;
    private final static int EXPANDING = 1;

    private boolean isExpanded = false;
    private boolean isExpanding = false;
    private boolean isCollapsing = false;

    private int previousHeight = 0;
    private int innerViewHeight;

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

    private void initView(Context context){
        //Inflating View
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.expandable_cardview, this);
    }

    private void initAttributes(Context context, AttributeSet attrs){
        //Ottengo attributi
        typedArray = context.obtainStyledAttributes(attrs, R.styleable.ExpandableCardView);
        title = typedArray.getString(R.styleable.ExpandableCardView_title);
        innerViewRes = typedArray.getResourceId(R.styleable.ExpandableCardView_inner_view, View.NO_ID);
        typedArray.recycle();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        //Una volta che la view è inflatata setto tutto

        arrowBtn = findViewById(R.id.arrow);
        textViewTitle = findViewById(R.id.title);

        if(!TextUtils.isEmpty(title)) textViewTitle.setText(title);

        //Inflato inner view
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        card = findViewById(R.id.card);

        ViewStub stub = findViewById(R.id.viewStub);
        stub.setLayoutResource(innerViewRes);
        innerView = stub.inflate();

        setElevation(Utils.convertDpToPixels(getContext(), 4));
        //innerView.setVisibility(View.INVISIBLE);

    }

    public void expand() {

        final int initialHeight = card.getHeight();


        if(!isMoving()) {
            previousHeight = initialHeight;
        }

        card.measure(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        int targetHeight = card.getMeasuredHeight();

        if(targetHeight - initialHeight != 0) {
            animateViews(initialHeight,
                    targetHeight - initialHeight,
                    EXPANDING);
        }
    }

    public void collapse() {
        int initialHeight = card.getMeasuredHeight();

        if(initialHeight - previousHeight != 0) {
            animateViews(initialHeight,
                    initialHeight - previousHeight,
                    COLLAPSING);
        }

    }

    public boolean isExpanded() {
        return isExpanded;
    }

    private void animateViews(final int initialHeight, final int distance, final int animationType){

        Animation expandAnimation = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if (interpolatedTime == 1){
                    //Setting isExpanding/isCollapsing to false
                    isExpanding = false;
                    isCollapsing = false;
                }

                card.getLayoutParams().height = animationType == EXPANDING ? (int) (initialHeight + (distance * interpolatedTime)) :
                        (int) (initialHeight  - (distance * interpolatedTime));
                card.findViewById(R.id.viewContainer).requestLayout();

                ((View) findViewById(R.id.viewContainer)).getLayoutParams().height = animationType == EXPANDING ? (int) (initialHeight + (distance * interpolatedTime)) :
                        (int) (initialHeight  - (distance * interpolatedTime));

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


        arrowAnimation.setDuration(ANIM_DURATION);
        expandAnimation.setDuration(ANIM_DURATION);

        isExpanding = animationType == EXPANDING;
        isCollapsing = animationType == COLLAPSING;

        startAnimation(expandAnimation);
        Log.d("SO","Started animation: "+ (animationType == EXPANDING ? "Expanding" : "Collapsing"));
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
        return isExpanding() || isCollapsing();
    }
}
