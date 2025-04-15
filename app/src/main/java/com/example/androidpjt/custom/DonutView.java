package com.example.androidpjt.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.androidpjt.R;

public class DonutView extends View {
    private int max = 100; //최댓값
    private int value = 50; //default값
    private float stroke = 40f; //원의 두께
    private int progressColor = Color.GREEN; //채워진 색
    private int backgroundColor = Color.GRAY; //안 채워진 색

    private Paint progressPaint; //채워진 색을 그리기 위한 Paint
    private Paint backgroundPaint; //안 채워진 색을 그리기 위한 Paint
    private RectF rect = new RectF();//그릴 영역

    public DonutView(Context context) {
        super(context);
        init(null);
    }

    public DonutView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public DonutView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    public DonutView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }


    //초기화함수
    private void init(@NonNull AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.DonutProgressView);
            max = a.getInt(R.styleable.DonutProgressView_max, max);
            value = a.getInt(R.styleable.DonutProgressView_value, value);
            progressColor = a.getColor(R.styleable.DonutProgressView_progressColor, progressColor);
            backgroundColor = a.getColor(R.styleable.DonutProgressView_backgroundColor, backgroundColor);
            stroke = a.getDimension(R.styleable.DonutProgressView_strokeWidth, stroke);
            a.recycle();
        }


        backgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        backgroundPaint.setStyle(Paint.Style.STROKE);
        backgroundPaint.setStrokeWidth(stroke);
        backgroundPaint.setColor(backgroundColor);
        backgroundPaint.setStrokeCap(Paint.Cap.ROUND);

        progressPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        progressPaint.setStyle(Paint.Style.STROKE);
        progressPaint.setStrokeWidth(stroke);
        progressPaint.setColor(progressColor);
        progressPaint.setStrokeCap(Paint.Cap.ROUND);

    }

    public void setValue(int value) {
        if (value >= 0 && value <= 100) {
            this.value = value;
            invalidate();//onDraw호출
        } else return;
    }

    public int getValue() {
        return value;
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);

        int size = Math.min(getWidth(), getHeight());
        float stroke = size * 0.2f;
        progressPaint.setStrokeWidth(stroke);
        backgroundPaint.setStrokeWidth(stroke);
        float padding = stroke / 2;
        float centerX = getWidth() / 2f;
        float centerY = getHeight() / 2f;
        float half = size / 2f;
        rect.set(centerX - half + padding, centerY - half + padding,
                centerX + half - padding, centerY + half - padding);
        canvas.drawArc(rect, 0f, 360f, false, backgroundPaint);
        float angle = 360f * value / max;
        canvas.drawArc(rect, -90f, angle, false, progressPaint);
    }
}