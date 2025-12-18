package com.example.mobileprograming;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker; // 속도 추적 추가
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
    private VelocityTracker velocityTracker;

    private List<String> items = new ArrayList<>();
    private final List<Integer> itemCenters = new ArrayList<>();

    private int scrollX = 0; // 가로 스크롤을 위해 scrollX로 변경
    private float lastX;

    private final int itemSpacing = 60; // 아이템 간 간격
    private final float textSize = 60f;

    public CurvedScrollView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        paint.setTextAlign(Paint.Align.CENTER);
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
        scrollX = 0; // 초기화
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (items.isEmpty()) return;

        int width = getWidth();
        int height = getHeight();
        int centerX = width / 2;
        int centerY = height / 2;

        itemCenters.clear();

        int startX = centerX - scrollX;

        for (int i = 0; i < items.size(); ++i) {
            float xPos = startX + i * (textSize * 2 + itemSpacing);

            float distFromCenter = xPos - centerX;
            float maxDist = width / 2f;

            float normalizedDist = Math.max(-1f, Math.min(1f, distFromCenter / maxDist));

            float interpolated = (float) Math.cos(normalizedDist * (Math.PI / 2.5f));
            float alpha = Math.max(0.1f, interpolated);

            float yOffset = (float) Math.sin(normalizedDist * (Math.PI / 2f)) * (height / 4f);

            paint.setAlpha((int) (alpha * 255));
            paint.setTextSize(textSize * (0.6f + 0.4f * alpha));

            float parameter3 = normalizedDist < 0 ? centerY - yOffset + (paint.getTextSize() / 3f) : centerY + yOffset + (paint.getTextSize() / 3f);

            canvas.drawText(
                    items.get(i),
                    xPos,
                    parameter3,
                    paint
            );

            itemCenters.add(i * (int)(textSize * 2 + itemSpacing));
        }
    }

    private int getTotalWidth() {
        if (items.isEmpty()) return 0;
        return (int) ((textSize * 2 + itemSpacing) * (items.size() - 1));
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (velocityTracker == null) velocityTracker = VelocityTracker.obtain();
        velocityTracker.addMovement(event);

        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                if (!scroller.isFinished()) scroller.abortAnimation();
                lastX = event.getX();
                return true;

            case MotionEvent.ACTION_MOVE:
                float dx = lastX - event.getX();
                if (Math.abs(dx) > 0) {
                    scrollX += (int) dx;
                    invalidate();
                    lastX = event.getX();
                }
                return true;

            case MotionEvent.ACTION_UP:
                velocityTracker.computeCurrentVelocity(1000);
                float v = velocityTracker.getXVelocity();

                scroller.fling(scrollX, 0, (int)-v, 0, 0, getTotalWidth(), 0, 0);
                snapToNearest();

                if (velocityTracker != null) {
                    velocityTracker.recycle();
                    velocityTracker = null;
                }
                return true;
        }
        return super.onTouchEvent(event);
    }

    private void snapToNearest() {
        if (items.isEmpty()) return;

        int itemWidth = (int)(textSize * 2 + itemSpacing);
        int targetIndex = Math.round((float) scrollX / itemWidth);
        int targetX = Math.max(0, Math.min(targetIndex * itemWidth, getTotalWidth()));

        scroller.startScroll(scrollX, 0, targetX - scrollX, 0, 400);
        postInvalidateOnAnimation();
    }

    @Override
    public void computeScroll() {
        if (scroller.computeScrollOffset()) {
            scrollX = scroller.getCurrX();
            invalidate();
        }
    }
}
