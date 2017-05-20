package com.example.lib;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import static android.view.View.MeasureSpec.EXACTLY;

public class AnimatedSquareTextView extends AppCompatTextView {

    public static final int TRANSITION_TIME_MILLIS = 500;

    public AnimatedSquareTextView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public AnimatedSquareTextView(@NonNull Context context, @NonNull AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public AnimatedSquareTextView(@NonNull Context context, @NonNull AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(@NonNull Context context) {
        final AnimationDrawable bg = (AnimationDrawable) ContextCompat.getDrawable(context, R.drawable.text_bg);
        bg.setEnterFadeDuration(TRANSITION_TIME_MILLIS);
        bg.setExitFadeDuration(TRANSITION_TIME_MILLIS);
        setBackgroundDrawable(bg);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        assertCorrectBackground();
        ((AnimationDrawable) getBackground()).start();
    }

    @Override
    protected void onDetachedFromWindow() {
        assertCorrectBackground();
        ((AnimationDrawable) getBackground()).stop();
        super.onDetachedFromWindow();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int width = MeasureSpec.getSize(widthMeasureSpec);
        final int height = MeasureSpec.getSize(heightMeasureSpec);
        final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        final int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        if (widthMode == EXACTLY && heightMode == EXACTLY) {
            // if both dimensions are set to exact value, then choose max of them
            final int dimens = width > height ? widthMeasureSpec : heightMeasureSpec;
            super.onMeasure(dimens, dimens);
        } else if (widthMode == EXACTLY) {
            // if only width is exact, force height to be the same
            //noinspection SuspiciousNameCombination (by design)
            super.onMeasure(widthMeasureSpec, widthMeasureSpec);
        } else if (heightMode == EXACTLY) {
            // if only height is exact, force width to be the same
            //noinspection SuspiciousNameCombination (by design)
            super.onMeasure(heightMeasureSpec, heightMeasureSpec);
        } else {
            // if nothing is exact, let TextView measure itself and use max
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            final int size = Math.max(getMeasuredWidth(), getMeasuredHeight());
            int widthSpec = MeasureSpec.makeMeasureSpec(size, MeasureSpec.EXACTLY);
            int heightSpec = MeasureSpec.makeMeasureSpec(size, MeasureSpec.EXACTLY);
            // setMeasuredDimension() breaks gravity, so we need to call onMeasure() twice =(
            super.onMeasure(widthSpec, heightSpec);
        }
    }

    private void assertCorrectBackground() {
        if (!(getBackground() instanceof AnimationDrawable)) {
            throw new IllegalStateException("Background was changed to " + getBackground());
        }
    }
}
