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

/**
 * 텍스트 아이템들을 가로 곡선 형태로 표시하는 커스텀 뷰입니다.
 * 사용자의 가로 스크롤에 따라 아이템들이 반원 형태의 궤적을 그리며 이동하며,
 * 스크롤이 멈출 때 가장 가까운 아이템으로 정렬되는 스냅(Snap) 기능을 포함합니다.
 */
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

    // 생성자
    public CurvedScrollView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        paint.setTextAlign(Paint.Align.CENTER);
        scroller = new OverScroller(context);
        touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        paint.setColor(Color.WHITE);
    }

    // 생성자
    public CurvedScrollView(Context context) {
        this(context, null);
    }

    // 아이템들을 가져옴
    public void setItems(List<String> list) {
        items.clear();
        items.addAll(list);
        scrollX = 0; // 초기화
        invalidate(); // 뷰를 다시 그리도록 요청
    }

    // 어떤어떤 복잡한 계산을 통해 뷰를 그려줌. 외부 산식을 가져옴
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

    // 토탈 너비를 계산
    private int getTotalWidth() {
        if (items.isEmpty()) return 0;
        return (int) ((textSize * 2 + itemSpacing) * (items.size() - 1));
    }

    // 사용자가 터치, 스와이프를 하였을 때의 부드러운 동작을 정의
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
