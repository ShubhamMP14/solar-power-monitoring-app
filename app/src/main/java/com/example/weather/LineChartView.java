package com.example.weather;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class LineChartView extends View {
    private List<Float> dataPoints = new ArrayList<>();

    public LineChartView(Context context) {
        super(context);
    }

    public LineChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LineChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Define paint for drawing the line
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(5);
        paint.setStyle(Paint.Style.STROKE);

        // Define paint for drawing the text
        Paint textPaint = new Paint();
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(40);
        textPaint.setTextAlign(Paint.Align.CENTER);

        // Define padding
        float padding = 150; // Adjust as needed

        // Draw the line chart
        if (!dataPoints.isEmpty()) {
            float startX = padding;
            float startY = getHeight() - dataPoints.get(0) * (getHeight() / 5); // Adjusted for 0-5 voltage range
            for (int i = 1; i < dataPoints.size(); i++) {
                float endX = padding + (getWidth() - padding * 2) / (dataPoints.size() - 1) * i;
                float endY = getHeight() - dataPoints.get(i) * (getHeight() / 5); // Adjusted for 0-5 voltage range
                canvas.drawLine(startX, startY, endX, endY, paint);
                startX = endX;
                startY = endY;
            }
        }

        // Define paint for Y-axis labels (voltage)
        Paint yAxisPaint = new Paint();
        yAxisPaint.setColor(Color.WHITE);
        yAxisPaint.setTextSize(40);
        yAxisPaint.setTextAlign(Paint.Align.RIGHT);

        // Draw Y-axis labels (voltage)
        for (int i = 0; i <= 5; i++) {
            String voltageText = String.valueOf(i);
            float yPosition = getHeight() - i * (getHeight() - 2 * padding) / 5; // Adjusted for padding
            canvas.drawText(voltageText, getWidth() * 0.1f, yPosition, yAxisPaint); // Adjusted for 0-5 voltage range
        }

        // Define paint for X-axis labels
        Paint xAxisPaint = new Paint();
        xAxisPaint.setColor(Color.WHITE);
        xAxisPaint.setTextSize(40);
        xAxisPaint.setTextAlign(Paint.Align.CENTER);

        // Draw X-axis labels (time)
        int numIntervals = 6; // Adjusted for 6 intervals from 8 AM to 1 PM
        float timeInterval = (getWidth() - padding * 2) / (numIntervals - 1);
        int hour = 8; // Starting hour
        float yOffset = getHeight() + 80; // Adjusted for text height and some padding
        for (int i = 0; i < numIntervals; i++) {
            String timeText;
            if (hour > 12) {
                timeText = (hour - 12) + " PM";
            } else {
                timeText = hour + " AM";
            }
            float xPosition = padding + i * timeInterval;
            canvas.drawText(timeText, xPosition, yOffset, xAxisPaint); // Use xAxisPaint for X-axis labels
            hour++;
        }

        // Draw X-axis line
        canvas.drawLine(padding, getHeight(), getWidth() - padding, getHeight(), paint);

        // Draw Y-axis line
        canvas.drawLine(padding, 0, padding, getHeight(), paint);

        // Draw coordinates for Y-axis
        for (int i = 0; i <= 5; i++) {
            String coordinateText = "0," + i;
            canvas.drawText(coordinateText, padding * 0.1f, getHeight() - i * (getHeight() / 5), textPaint);
        }

        // Draw coordinates for X-axis
        for (int i = 0; i < numIntervals; i++) {
            String coordinateText = (8 + i) + ",0";
            float xPosition = padding + i * timeInterval;
            float yPosition = getHeight() + 40; // Adjusted for text height
            canvas.drawText(coordinateText, xPosition, yPosition + 80, textPaint);
        }
    }

    public void setData(List<Float> data) {
        this.dataPoints = data;
        invalidate(); // Refresh the view
    }
}
