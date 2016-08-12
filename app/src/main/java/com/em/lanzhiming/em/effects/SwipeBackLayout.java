package com.em.lanzhiming.em.effects;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Scroller;

import com.em.lanzhiming.em.R;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by spiritTalk on 2015/5/5.
 * 跟随手指右滑退出，Activity调用attachToActivity方法使用，
 * 如果右滑退出时需要回掉方法，可实现OnSliddingFinishLister接口。
 */
public class SwipeBackLayout extends FrameLayout {
    private final String TAG = SwipeBackLayout.class.getSimpleName();
    private static final int MIN_FLING_VELOCITY = 600; // dips per second
    private final float minVelocity;
    private final int FULL_ALPHA = 255;
    private OnSlidingfinishListener mCallBackListener;
    private List<ViewPager> mViewPagers = new LinkedList<ViewPager>();
    private Activity mActivity;
    private Scroller mScroller;
    private Drawable mShadowLeft;
    /**
     * 滑动的距离 / SwipeLayout的宽度
     */
    private float mScrollPercent;
    /**
     * 多点触摸时，首先按下的手指作为主点
     */
    private int mActivePointerId;
    private int mTouchSlop;
    private VelocityTracker mVelocityTracker;
    /**
     * 手指按下时坐标值
     */
    private float downX, downY;
    /**
     * 手指最近的X坐标位置
     */
    private float mLastX;
    /**
     * 是否处于拖动状态
     */
    private boolean isDragging;
    /**
     * 滑动是否导致finish
     */
    private boolean isFinish;
    private int mViewWidth;
    private boolean isCanSlide;
    /**
     * 是否禁止滑动
     */
    private boolean isForbidSlide;
    /**
     * 是否从屏幕边缘开始滑
     */
    private boolean isScreenEdge = true;

    public SwipeBackLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwipeBackLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mScroller = new Scroller(context);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        mShadowLeft = getResources().getDrawable(R.drawable.shadow_left);
        final float density = getResources().getDisplayMetrics().density;
        minVelocity = MIN_FLING_VELOCITY * density;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (changed) {
            mViewWidth = getWidth();
            getAllViewPagers(mViewPagers, this);
        }
    }

    public void attachToActivity(Activity activity) {
        mActivity = activity;
        setWindowAndDecorViewBackground(activity);
        //在底视图上添加该Layout，底视图的child放到新加的Layout中
        ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
        ViewGroup decorChild = (ViewGroup) decorView.getChildAt(0);
        decorView.removeView(decorChild);
        addView(decorChild);
        decorView.addView(this);
    }

    public int setWindowAndDecorViewBackground(Activity activity) {
        if (activity instanceof OnSlidingfinishListener) {
            mCallBackListener = (OnSlidingfinishListener) activity;
        }
        TypedArray a = activity.getTheme().obtainStyledAttributes(new int[]{
                android.R.attr.windowBackground
        });
        int background = a.getResourceId(0, 0);
        a.recycle();
        ((ViewGroup) activity.getWindow().getDecorView()).getChildAt(0).setBackgroundResource(background);
        activity.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return background;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        //如果禁止滑动，则直接返回
        if (isForbidSlide) {
            return super.onInterceptTouchEvent(event);
        }
        //ViewPager不是位于首页时，ViewPager自己处理手势事件
        ViewPager mViewPager = getTouchViewPager(mViewPagers, event);
        if (mViewPager != null && mViewPager.getCurrentItem() != 0) {
            return super.onInterceptTouchEvent(event);
        }

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.d(TAG, "ACTION_DOWN: " + event.getRawX() + " , " + event.getRawY());
                mActivePointerId = event.getPointerId(0);
//                Log.v(TAG, "pointerId: " + mActivePointerId);
//                Log.d(TAG, "isCanSlide=== " + (downX > mTouchSlop * 2));
                isCanSlide = true;
                downX = mLastX = event.getRawX();
                if (isScreenEdge) {
                    //设置从屏幕边缘时，down的X坐标位置需小于等于(mTouchSlop * 2)
                    if (downX > mTouchSlop * 2) {
                        isCanSlide = false;
                        return super.onInterceptTouchEvent(event);
                    }
                }
                downY = (int) event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                //向右滑动时，拦截子View的手势事件
                if (isRightSlide(event) && isCanSlide) {
                    return true;
                }
                break;
        }
        return super.onInterceptTouchEvent(event);
    }

    /**
     * 判断手势是否为向右滑动
     *
     * @param event
     * @return
     */
    private boolean isRightSlide(MotionEvent event) {
        if (isScreenEdge) {
            //不考虑Y方向的值
            return event.getRawX() - downX > 0;
        } else {
            //X方向的移动距离大于Y方向的移动距离
            return (event.getRawX() - downX > mTouchSlop) && (Math.abs((int) event.getRawY() - downY) < mTouchSlop);
        }
    }

    /**
     * 获取子View中的ViewPager
     *
     * @param viewPagers
     * @param parent
     */
    private void getAllViewPagers(List<ViewPager> viewPagers, ViewGroup parent) {
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childView = parent.getChildAt(i);
            if (childView instanceof ViewPager) {
                viewPagers.add((ViewPager) childView);
            } else if (childView instanceof ViewGroup) {
                getAllViewPagers(viewPagers, (ViewGroup) childView);
            }
        }
    }

    /**
     * 返回手指touch的ViewPager
     *
     * @param mViewPagers
     * @param ev
     * @return
     */
    private ViewPager getTouchViewPager(List<ViewPager> mViewPagers, MotionEvent ev) {
        if (mViewPagers == null || mViewPagers.isEmpty()) {
            return null;
        }
        Rect mRect = new Rect();
        for (ViewPager v : mViewPagers) {
            v.getHitRect(mRect);
            if (mRect.contains((int) ev.getRawX(), (int) ev.getRawY())) {
                return v;
            }
        }
        return null;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);
        int action = MotionEventCompat.getActionMasked(event);
        int actionIndex = MotionEventCompat.getActionIndex(event);
//        Log.v(TAG, "action: " + action + " ,actionIndex：" + actionIndex);
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
//                int index = MotionEventCompat.findPointerIndex(event, mActivePointerId);
//                float endX = MotionEventCompat.getX(event, index);
                int endX = (int) event.getRawX();
                float distanceX = mLastX - endX;
                mLastX = endX;
                Log.d(TAG, "isX：" + (endX - downX > 0) + " , isY：" + (Math.abs((int) event.getRawY() - downY) < mTouchSlop));
                if (isRightSlide(event) && isCanSlide) {
                    isDragging = true;
                }
                if (endX - downX >= 0 && isDragging) {
                    scrollBy((int) distanceX, 0);
                }
                break;
            case MotionEvent.ACTION_UP:
//                Log.d(TAG, "ACTION_UP: " + event.getRawX() + " ~ " + event.getRawY());
                cancel();
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
//                Log.i(TAG, "ACTION_POINTER_DOWN: " + event.getRawX() + " , " + event.getRawY());
                break;
            case MotionEvent.ACTION_POINTER_UP:
//                Log.i(TAG, "ACTION_POINTER_UP: " + event.getRawX() + " ~ " + event.getRawY());
                int pointerId = MotionEventCompat.getPointerId(event, actionIndex);
                Log.v(TAG, "pointerId: " + pointerId);
                if (pointerId == mActivePointerId) {
                    cancel();
                }
                break;
        }
        return true;
    }

    private void computeScrollPercent() {
        mScrollPercent = (float) Math.abs(getScrollX()) / (mViewWidth/* + mShadowLeft.getIntrinsicWidth()*/);
//        Log.e(TAG, "getScrollX()：" + getScrollX() + " , mScrollPercent：" + mScrollPercent);
    }

    private void cancel() {
        mVelocityTracker.computeCurrentVelocity(1000);
        isFinish = getScrollX() <= -mViewWidth / 2 || (mVelocityTracker.getXVelocity() > minVelocity && isDragging);
        isDragging = false;
        if (isFinish) {
            scrollRigth();
        } else {
            scrollOrigin();
        }
    }

    /**
     * 向右滚动
     */
    public void scrollRigth() {
        isFinish = true;
        int delta = mViewWidth + getScrollX();//正直
        mScroller.startScroll(getScrollX(), 0, -delta, 0, Math.abs(delta));
        postInvalidate();
    }

    /**
     * 向左滚回初始位置
     */
    private void scrollOrigin() {
        int delta = getScrollX();//负值
        mScroller.startScroll(getScrollX(), 0, -delta, 0, Math.abs(delta));
        postInvalidate();
    }

    @Override
    public void computeScroll() {
        computeScrollPercent();
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            postInvalidate();
        }
        if (mScroller.isFinished() && isFinish) {
            if (mCallBackListener != null) {
                mCallBackListener.slidingFinish();
            } else if (mActivity != null) {
                mActivity.finish();
            }
        }
    }

    @Override
    protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
        drawShadow(canvas, child);
        return super.drawChild(canvas, child, drawingTime);
    }

    private void drawShadow(Canvas canvas, View child) {
        Rect childRect = new Rect();
        child.getHitRect(childRect);
//        Log.d(TAG, "intrinsicWidth：" + mShadowLeft.getIntrinsicWidth());
//        Log.d(TAG, childRect.left + " , " + childRect.top + " , " + childRect.bottom);
        mShadowLeft.setBounds(childRect.left - mShadowLeft.getIntrinsicWidth(), childRect.top,
                childRect.left, childRect.bottom);
        float num = (1 - mScrollPercent)/* < 0.1 ? 0 : 1 - mScrollPercent*/;
        mShadowLeft.setAlpha((int) (num * FULL_ALPHA));
        mShadowLeft.draw(canvas);
    }

    public interface OnSlidingfinishListener {
        void slidingFinish();
    }

    public void setForbidSlide(boolean isForbidSlide) {
        this.isForbidSlide = isForbidSlide;
    }

    public void setScreenEdge(boolean isScreenEdge) {
        this.isScreenEdge = isScreenEdge;
    }

    public boolean isForbidSlide() {
        return isForbidSlide;
    }

    public void setCallBackListener(OnSlidingfinishListener callBackListener) {
        this.mCallBackListener = callBackListener;
    }
}
