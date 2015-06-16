package com.satan.openucloading;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;

/**
 * Created by satan on 2015/6/15.
 */
public class LoadingView extends View {

    private static final float DEFAULT_RADIUS = 60;
    private static final float DEFAULT_DISTANCE = 200;
    private static final int DEFAULT_COLOR = Color.RED;

    private Paint mPaint;
    private float mRadius;
    private static float mDistance;
    private static final int ANIMATION_DURATION = 400;
    private int mColor;
    public float mFactor = 1.2f;


    public LoadingView(Context context) {
        this(context, null);
    }

    public LoadingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.UCLoadingView);
        mColor = typedArray.getColor(R.styleable.UCLoadingView_view_color, DEFAULT_COLOR);
        mRadius = typedArray.getDimension(R.styleable.UCLoadingView_view_radius, DEFAULT_RADIUS);
        mDistance = typedArray.getDimension(R.styleable.UCLoadingView_view_distance, DEFAULT_DISTANCE);

        mPaint = new Paint();
        mPaint.setColor(mColor);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
    }

    public void setViewColor(int color) {
        mColor = color;
    }

    public void setVIewRadius(float radius) {
        mRadius = radius;
    }

    public void setViewDistance(float distance) {
        mDistance = distance;
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        this.postDelayed(new Runnable() {
            @Override
            public void run() {
                freeFall();
            }
        }, 500);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int width;
        int height;
        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else {
            width = (int) mRadius * 2 + getPaddingLeft() + getPaddingRight();
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {
            height = (int) mRadius * 2 + getPaddingTop() + getPaddingBottom();
        }

        setMeasuredDimension(width, height);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (getVisibility() == GONE) {
            return;
        }
        canvas.drawCircle(mRadius, mRadius, mRadius, mPaint);
    }


    /**
     * 落地挤压
     */
    public void pressView() {
        ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(this, "scaleY", 1f, 0.7f);
        ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(this, "scaleX", 0.7f, 1f);
        scaleYAnimator.setDuration(ANIMATION_DURATION / 2);
        scaleXAnimator.setDuration(ANIMATION_DURATION / 2);
        scaleXAnimator.setInterpolator(new DecelerateInterpolator(mFactor));
        scaleYAnimator.setInterpolator(new DecelerateInterpolator(mFactor));
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(ANIMATION_DURATION / 2);
        animatorSet.playTogether(scaleXAnimator, scaleYAnimator);
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                springView();
            }
        });
        animatorSet.start();
    }

    /**
     * 反弹
     */
    public void springView() {
        ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(this, "scaleX", 1f, 0.7f);
        ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(this, "scaleY", 0.7f, 1f);
        scaleYAnimator.setDuration(ANIMATION_DURATION / 4);
        scaleXAnimator.setDuration(ANIMATION_DURATION / 4);
        scaleXAnimator.setInterpolator(new AccelerateInterpolator(mFactor));
        scaleYAnimator.setInterpolator(new AccelerateInterpolator(mFactor));
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(ANIMATION_DURATION / 4);
        animatorSet.playTogether(scaleXAnimator, scaleYAnimator);
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                upThrow();
            }
        });
        animatorSet.start();
    }


    /**
     * 上抛
     */
    public void upThrow() {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(this, "translationY", mDistance, 0);
        ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(this, "scaleX", 0.7f, 1f);
        objectAnimator.setDuration(ANIMATION_DURATION);
        scaleYAnimator.setDuration(ANIMATION_DURATION);
        scaleYAnimator.setInterpolator(new DecelerateInterpolator(mFactor));
        objectAnimator.setInterpolator(new DecelerateInterpolator(mFactor));
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(ANIMATION_DURATION);
        animatorSet.playTogether(objectAnimator, scaleYAnimator);
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                freeFall();
            }
        });
        animatorSet.start();
    }



    /**
     * 下落
     */
    public void freeFall() {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(this, "translationY", 0, mDistance);
        ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(this, "scaleX", 1f, 0.7f);
        objectAnimator.setDuration(ANIMATION_DURATION);
        scaleYAnimator.setDuration(ANIMATION_DURATION);
        scaleYAnimator.setInterpolator(new AccelerateInterpolator(mFactor));
        objectAnimator.setInterpolator(new AccelerateInterpolator(mFactor));
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(ANIMATION_DURATION);
        animatorSet.playTogether(objectAnimator, scaleYAnimator);
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                pressView();
            }
        });
        animatorSet.start();
    }

}
