package com.example.mobileprograming;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.OverScroller;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class CurvedScrollView extends View {

    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final OverScroller scroller;
    private final int touchSlop;

    private List<String> items = new ArrayList<>();
    private final List<Integer> itemCenters = new ArrayList<>();

    private int scrollY = 0;
    private float lastY;

    private final int itemSpacing = 32;
    private final float textSize = 72f;

    public CurvedScrollView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(textSize);
        scroller = new OverScroller(context);
        touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();

        paint.setColor(Color.WHITE);
    }

    public CurvedScrollView(Context context) {
        this(context, null);
    }

    public void setItems(List<String> list) {
        items.clear();
        items.addAll(list);
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (items.isEmpty()) return;

        int width = getWidth();
        int height = getHeight();
        int centerY = height / 2;

        itemCenters.clear();

        int y = centerY - scrollY;

        for (int i = 0; i < items.size(); i++) {
            float dist = Math.abs(y - centerY);
            float scrollPercent = (float) scrollY / Math.max(1, getTotalHeight());

            float elementRatio = (float) i / Math.max(1, items.size() - 1);
            float interpolated = (float) Math.cos((scrollPercent - elementRatio) * Math.PI);

            float alpha = Math.max(0f, interpolated);
            float indent = interpolated * width / 2f;

            paint.setAlpha((int) (alpha * 255));
            paint.setTextSize(textSize * Math.max(0.5f, alpha));

            canvas.drawText(
                    items.get(i),
                    width / 2f + indent,
                    y,
                    paint
            );

            itemCenters.add(scrollY + y - centerY);
            y += textSize + itemSpacing;
        }
    }

    private int getTotalHeight() {
        return (int) ((textSize + itemSpacing) * items.size());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                if (!scroller.isFinished()) scroller.abortAnimation();
                lastY = event.getY();
                return true;

            case MotionEvent.ACTION_MOVE:
                float dy = lastY - event.getY();
                if (Math.abs(dy) > touchSlop) {
                    scrollY += dy;
                    clampScroll();
                    invalidate();
                    lastY = event.getY();
                }
                return true;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                snapToNearest();
                return true;
        }
        return super.onTouchEvent(event);
    }

    private void clampScroll() {
        scrollY = Math.max(0, Math.min(scrollY, getTotalHeight()));
    }

    private void snapToNearest() {
        if (itemCenters.isEmpty()) return;

        int minDist = Integer.MAX_VALUE;
        int target = scrollY;

        for (int c : itemCenters) {
            int dist = Math.abs(c - scrollY);
            if (dist < minDist) {
                minDist = dist;
                target = c;
            }
        }

        scroller.startScroll(0, scrollY, 0, target - scrollY, 300);
        postInvalidateOnAnimation();
    }

    @Override
    public void computeScroll() {
        if (scroller.computeScrollOffset()) {
            scrollY = scroller.getCurrY();
            invalidate();
        }
    }
}