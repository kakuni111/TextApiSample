package com.sample.textapisample;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;

import com.google.android.gms.vision.text.TextBlock;

import java.util.ArrayList;
import java.util.List;

public class OcrTextView extends View {

    Paint paint;
    private List<RectF> mRectList;

    public OcrTextView(Context context, AttributeSet attrs) {

        super(context, attrs);
        paint = new Paint();
    }

    public void showFrame(SparseArray<TextBlock> textBlockSparseArray) {

        mRectList = new ArrayList<>();

        if (textBlockSparseArray != null) {
            for (int i = 0; i < textBlockSparseArray.size(); i++) {
                TextBlock block = textBlockSparseArray.get(i);
                if (block != null) {
                    RectF rectF = new RectF(block.getBoundingBox());
                    mRectList.add(rectF);
                }
            }
        }
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mRectList == null || mRectList.isEmpty()) {
            return;
        }

        paint.setColor(Color.argb(50, 255, 255, 0));
        paint.setStyle(Paint.Style.FILL);
        for (RectF rectF : mRectList) {
            canvas.drawRect(rectF, paint);
        }

        paint.setColor(Color.argb(255, 255, 255, 0));
        paint.setStrokeWidth(2);
        paint.setStyle(Paint.Style.STROKE);
        for (RectF rectF : mRectList) {
            canvas.drawRect(rectF, paint);
        }
    }
}
