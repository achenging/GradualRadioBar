package com.achenging.view.gradualradiobar;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RadioGroup;


/**
 * Created by Droidroid on 2016/4/27.
 * Modify by achenging on 2017/3/19.
 */
public class GradualRadioGroup extends RadioGroup implements ViewPager.OnPageChangeListener {

    private ViewPager                  mViewPager;
    private OnPageItemSelectedCallback mOnPageItemSelectedCallback;

    public void setViewPager(ViewPager viewPager, OnPageItemSelectedCallback onPageItemSelectedCallback) {
        mViewPager = viewPager;
        mViewPager.setOffscreenPageLimit(4);
        mViewPager.addOnPageChangeListener(this);
        mOnPageItemSelectedCallback = onPageItemSelectedCallback;
    }
    public void setViewPager(ViewPager viewPager) {
        this.setViewPager(viewPager, null);
    }

    public GradualRadioGroup(Context context) {
        super(context);
    }

    public GradualRadioGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        for (int i = 0; i < getChildCount(); i++) {
            final int position = i;
            getChildAt(i).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    setViewPagerItem(position);
                }
            });
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        updateGradient(position, positionOffset);
        if (mOnPageItemSelectedCallback != null) {
            mOnPageItemSelectedCallback.onPageItemSelected(position);
        }
    }

    public void setViewPagerItem(int position) {
        setClickedViewChecked(position);
        if (mViewPager != null) {
            mViewPager.setCurrentItem(position, false);
        }
    }

    private void updateGradient(int position, float offset) {
        if (offset >= 0) {
            View child = getChildAt(position);
            if (child != null) {
                ((GradualRadioButton) child).updateAlpha(255 * (1 - offset));
            }

            child = getChildAt(position + 1);
            if (child != null) {
                ((GradualRadioButton) child).updateAlpha(255 * offset);
            }
        }
    }

    @Override
    public void onPageSelected(int position) {
        setSelectedViewChecked(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private void setSelectedViewChecked(int position) {
        for (int i = 0; i < getChildCount(); i++) {
            ((GradualRadioButton) getChildAt(i)).setChecked(false);
        }
        ((GradualRadioButton) getChildAt(position)).setChecked(true);
    }

    private void setClickedViewChecked(int position) {
        for (int i = 0; i < getChildCount(); i++) {
            ((GradualRadioButton) getChildAt(i)).setRadioButtonChecked(false);
        }
        ((GradualRadioButton) getChildAt(position)).setRadioButtonChecked(true);
    }

    public interface OnPageItemSelectedCallback {
        void onPageItemSelected(int position);
    }

    public int getCurrentItem() {
        return mViewPager.getCurrentItem();
    }
}
