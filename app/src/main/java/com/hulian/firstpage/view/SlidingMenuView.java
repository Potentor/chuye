package com.hulian.firstpage.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.hulian.firstpage.R;
import com.nineoldandroids.view.ViewHelper;

public class SlidingMenuView extends HorizontalScrollView {

	private ViewGroup menuContainer;
	private ViewGroup contentContainer;

	private int screenWidth;
	private int menuWidth;
	private int menuContainerRightPadding = 50;

	private boolean isMeasured = false;
	private boolean isOpen=false;

	private float lastX;
	private float lastY;
	private boolean isTransDown = true;
    private boolean isJadged=false;

	
	public SlidingMenuView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public SlidingMenuView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
				R.styleable.SlidingMenuView, defStyle, 0);

		int n = a.getIndexCount();
		for (int i = 0; i < n; i++) {
			int attr = a.getIndex(i);
			switch (attr) {
			case R.styleable.SlidingMenuView_rightPadding:
				menuContainerRightPadding = a.getDimensionPixelSize(attr,
						(int) TypedValue.applyDimension(
								TypedValue.COMPLEX_UNIT_DIP, 50, context
										.getResources().getDisplayMetrics()));
				break;
			}
		}
		a.recycle();

		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics outMetrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(outMetrics);
		screenWidth = outMetrics.widthPixels;

	}

	public SlidingMenuView(Context context) {
		this(context, null);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		if (!isMeasured) {
            LinearLayout wrapper = (LinearLayout) getChildAt(0);
			menuContainer = (ViewGroup) wrapper.getChildAt(0);
			contentContainer = (ViewGroup) wrapper.getChildAt(1);
			menuWidth = menuContainer.getLayoutParams().width = screenWidth
					- menuContainerRightPadding;
			contentContainer.getLayoutParams().width = screenWidth;
			isMeasured = true;
		}
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
       // Log.i("menu","layout");
		super.onLayout(changed, l, t, r, b);
		if (changed) {
			this.scrollTo(menuWidth, 0);
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		int action = ev.getAction();

      //  Log.i("tag1", "touch"+ev.getAction());

        switch (action){
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                int scrollX = getScrollX();
                if (scrollX >= menuWidth / 2) {
                    this.smoothScrollTo(menuWidth, 0);
                  //  Log.i("tag1", "close");
                    isOpen = false;
                } else {
                    this.smoothScrollTo(0, 0);
                   // Log.i("tag1", "open");
                    isOpen = true;
                }
                isJadged=false;
                return true;
        }
        return super.onTouchEvent(ev);

	}

	public void openMenu() {
		if (isOpen)
			return;
		this.smoothScrollTo(0, 0);
		isOpen = true;
	}

	public void closeMenu() {
		if (!isOpen)
			return;
		this.smoothScrollTo(menuWidth, 0);
		isOpen = false;
	}

	/**
	 * 菜单开关
	 */
	public void toggle() {
		if (isOpen) {
			closeMenu();
		} else {
			openMenu();
		}
	}

   @Override
public boolean dispatchTouchEvent(MotionEvent ev) {
	 //  Log.i("tag","dispatch");


	return super.dispatchTouchEvent(ev);



}


	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub

        int action=ev.getAction();
        if (action==MotionEvent.ACTION_DOWN)
        {
            lastX = ev.getRawX();
            lastY = ev.getRawY();
          //  Log.i("tag1","down");
            return super.onInterceptTouchEvent(ev);//关键点  有待深入了解 keyPoint
           // return false;
        }

        if (action==MotionEvent.ACTION_MOVE)
        {
            if(!isJadged) {
                float cuX = ev.getRawX();
                float cuY = ev.getRawY();

                float capX = Math.abs(cuX - lastX);
                float capY = Math.abs(cuY - lastY);
               // Log.i("tag1", "capX" + capX);

                //重要方法，真机上滑动前几个事件可能含有相同的(x，y)
                if (capX == 0 && capY == 0) {
                  //  Log.i("tag1", "cevET");
                    return false;
                }


                if (capX > capY) {
                 //   Log.i("tag1", "截断");
                    isJadged=true;
                    isTransDown=false;

                } else {
                    isJadged=true;
                 //   Log.i("tag1", "通过");
                    isTransDown=true;
                }
            }
            return !isTransDown;

        }

       // Log.i("tag1", "其它"+ev.getAction());
        if (action==MotionEvent.ACTION_UP){
            isJadged=false;
        }

        return super.onInterceptTouchEvent(ev);
	}

	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {

		super.onScrollChanged(l, t, oldl, oldt);


		float scale = l* 1.0f / menuWidth; // 1 ~ 0
		float rightScale = 0.7f + 0.3f * scale;
		float leftScale = 1.0f - scale * 0.3f;
		float leftAlpha = 0.6f + 0.4f * (1 - scale);

		ViewHelper.setTranslationX(menuContainer, menuWidth * scale * 0.8f);

		ViewHelper.setScaleX(menuContainer, leftScale);
		ViewHelper.setScaleY(menuContainer, leftScale);
		ViewHelper.setAlpha(menuContainer, leftAlpha);

		ViewHelper.setPivotX(contentContainer, 0);
		ViewHelper.setPivotY(contentContainer, contentContainer.getHeight() / 2);
		ViewHelper.setScaleX(contentContainer, rightScale);
		ViewHelper.setScaleY(contentContainer, rightScale);
	}

}
