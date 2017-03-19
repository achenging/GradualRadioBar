package com.achenging.view.gradualradiobar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.StaticLayout;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.RadioButton;


/**
 * Created by Droidroid on 2016/4/27.
 * Modify by achenging on 2017/3/19.
 */
public class GradualRadioButton extends RadioButton {

    private Paint mIconPaint;
    private Paint mTextPaint;
    private Paint mBackgroundPaint;

    private Bitmap mBitmap;
    private Bitmap mBackground;

    private int iconWidth;
    private int iconPadding;
    private int iconHeight;
    private int textPadding;

    private Canvas   mCanvas;
    private Drawable mIconDrawable;
    private Rect     mRect;
    private int      mAlpha;
    private int      mColor;
    private float    mFontHeight;
    private float    mTextWidth;


    public GradualRadioButton(Context context) {
        this(context, null);
    }

    public GradualRadioButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mIconPaint = new Paint();
        mTextPaint = new Paint();
        mBackgroundPaint = new Paint();

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.GradualRadioButton);
        mColor = ta.getColor(R.styleable.GradualRadioButton_gradual_color, Color.BLUE);
        mIconDrawable = ta.getDrawable(R.styleable.GradualRadioButton_gradual_icon);
        ta.recycle();

        setButtonDrawable(null);
        if (mIconDrawable != null) {
            setCompoundDrawablesWithIntrinsicBounds(null, mIconDrawable, null, null);
            mIconDrawable = getCompoundDrawables()[1];
        } else {
            mIconDrawable = getCompoundDrawables()[1];
        }
        if (mIconDrawable == null) {
            throw new RuntimeException("'gradual_icon' or 'drawableTop' attribute should be defined");
        }

        mRect = mIconDrawable.getBounds();
        iconWidth = mRect.width();
        iconHeight = mRect.height();
        iconPadding = 0;
        textPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, getResources().getDisplayMetrics());

        mFontHeight = getPaint().getTextSize();
        mTextWidth = StaticLayout.getDesiredWidth(getText(), getPaint());

        mBackground = getBitmapFromDrawable(mIconDrawable);
        mBitmap = Bitmap.createBitmap(iconWidth, iconHeight, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);

        if (isChecked()) {
            mAlpha = 255;
        }
    }

    private Bitmap getBitmapFromDrawable(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        } else {
            throw new RuntimeException("The Drawable must be an instance of BitmapDrawable");
        }
    }

    /**
     * modify icon and text position
     */
    @Override
    protected void onDraw(Canvas canvas) {
        drawBackgroundIcon(canvas);
        drawTargetIcon(canvas);
        drawBackgroundText(canvas);
        drawTargetText(canvas);
    }

    private void drawBackgroundIcon(Canvas canvas) {
        mBackgroundPaint.setAlpha(255 - mAlpha);
        int parentHeight = ((View) getParent()).getMeasuredHeight();
        canvas.drawBitmap(mBackground, (getWidth() - iconWidth) / 2,
                parentHeight - mFontHeight - textPadding / 2 - iconHeight - getPaddingBottom(),
                mBackgroundPaint);
    }

    private void drawTargetIcon(Canvas canvas) {
        mIconDrawable.draw(mCanvas);
        mIconPaint.setColor(mColor);
        mIconPaint.setAlpha(mAlpha);
        mIconPaint.setAntiAlias(true);
        mIconPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        mCanvas.drawRect(mRect, mIconPaint);
        int parentHeight = ((View) getParent()).getMeasuredHeight();
        canvas.drawBitmap(mBitmap, (getWidth() - iconWidth) / 2,
                parentHeight - mFontHeight - textPadding / 2 - iconHeight - getPaddingBottom(),
                null);
    }

    private void drawBackgroundText(Canvas canvas) {
        mTextPaint.setColor(getCurrentTextColor());
        mTextPaint.setAntiAlias(true);
        mTextPaint.setAlpha(255 - mAlpha);
        mTextPaint.setTextSize(getTextSize());
        int parentHeight = ((View) getParent()).getMeasuredHeight();
        canvas.drawText(getText().toString(), getWidth() / 2 - mTextWidth / 2
                , parentHeight - textPadding / 2 - getPaddingBottom(), mTextPaint);
    }

    private void drawTargetText(Canvas canvas) {
        mTextPaint.setColor(mColor);
        mTextPaint.setAlpha(mAlpha);
        int parentHeight = ((View) getParent()).getMeasuredHeight();
        canvas.drawText(getText().toString(), getWidth() / 2 - mTextWidth / 2
                , parentHeight - textPadding / 2 - getPaddingBottom(), mTextPaint);
    }


    public void updateAlpha(float alpha) {
        mAlpha = (int) alpha;
        invalidate();
    }

    public void setRadioButtonChecked(boolean isChecked) {
        setChecked(isChecked);
        if (isChecked) {
            mAlpha = 255;
        } else {
            mAlpha = 0;
        }
        invalidate();
    }

}
