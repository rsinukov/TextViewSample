package com.example.lib;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import static android.view.View.MeasureSpec.EXACTLY;

public class AnimatedSquareTextView extends AppCompatTextView {

    private static final int DEFAULT_TRANSITION_TIME_MILLIS = 500;
    private static final int DEFAULT_STATIC_TIME_MILLIS = 2000;
    private static final int NO_ITEMS_ID = -1;

    public AnimatedSquareTextView(@NonNull Context context) {
        super(context);
        init(context, null);
    }

    public AnimatedSquareTextView(@NonNull Context context, @NonNull AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public AnimatedSquareTextView(@NonNull Context context, @NonNull AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(@NonNull Context context, @Nullable AttributeSet attrs) {
        final TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.AnimatedSquareTextView, 0, 0);
        final int transitionDuration;
        final int staticDuration;
        final Drawable[] items;

        try {
            transitionDuration = ta.getInt(
                    R.styleable.AnimatedSquareTextView_transitionDuration,
                    DEFAULT_TRANSITION_TIME_MILLIS
            );
            staticDuration = ta.getInt(
                    R.styleable.AnimatedSquareTextView_staticDuration,
                    DEFAULT_STATIC_TIME_MILLIS
            );
            final int itemsArrayId = ta.getResourceId(R.styleable.AnimatedSquareTextView_items, NO_ITEMS_ID);
            if (itemsArrayId == NO_ITEMS_ID) {
                items = new Drawable[]{
                        ContextCompat.getDrawable(context, R.drawable.gradient_1),
                        ContextCompat.getDrawable(context, R.drawable.gradient_2)
                };
            } else {
                final TypedArray itemsTypedArray = context.getResources().obtainTypedArray(itemsArrayId);
                try {
                    items = new Drawable[itemsTypedArray.length()];
                    for (int i = 0; i < items.length; i++) {
                        items[i] = itemsTypedArray.getDrawable(i);
                    }
                } finally {
                    itemsTypedArray.recycle();
                }
            }
        } finally {
            ta.recycle();
        }

        final AnimationDrawable bg = new AnimationDrawable();
        for (Drawable item : items) {
            bg.addFrame(item, staticDuration + transitionDuration);
        }
        bg.setEnterFadeDuration(transitionDuration);
        bg.setExitFadeDuration(transitionDuration);
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
