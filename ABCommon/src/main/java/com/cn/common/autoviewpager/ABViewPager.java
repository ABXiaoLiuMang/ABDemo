/**
 * 
 */
package com.cn.common.autoviewpager;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.cn.common.ui.R;

import java.util.ArrayList;


/**
 * 自动滚动封装
 */
public class ABViewPager extends LinearLayout {

	private LinearLayout layout_dog;
	private Context mContext;
	private ViewPager viewPager;
	private int currentItem = 0;
	private int oldPosition = 0;
	private int points;// 多少个点
	private boolean mCountAutoScroll = false;//数量大于1，才能滚动
	private boolean mAutoScroll;//是否自动滚动（布局中是否开启）
	private ArrayList<View> image_points = new ArrayList<>(); // 存放点的view

	int scrollTime;//滚动间隔时间
	int radius;
	int indicator_select;
	int indicator_normal;
	int dogBackground;

	public ABViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		setOrientation(LinearLayout.VERTICAL);
		LayoutInflater.from(context).inflate(R.layout.auto_viewpager, this, true);
		TypedArray mTypedArray = context.obtainStyledAttributes(attrs,R.styleable.ABViewPager);

		mAutoScroll = mTypedArray.getBoolean(R.styleable.ABViewPager_autoScroll, true);
		indicator_select = mTypedArray.getResourceId(R.styleable.ABViewPager_selectResId,R.drawable.vp_indicator_select);
		indicator_normal = mTypedArray.getResourceId(R.styleable.ABViewPager_normalResId,R.drawable.vp_indicator_normal);
		radius = mTypedArray.getInteger(R.styleable.ABViewPager_dogRadius,10);
		scrollTime = mTypedArray.getInteger(R.styleable.ABViewPager_scrollTime,4000);
		dogBackground = mTypedArray.getResourceId(R.styleable.ABViewPager_dogBackground, ContextCompat.getColor(getContext(),android.R.color.transparent));
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		layout_dog =  findViewById(R.id.layout_dog);
		viewPager =  findViewById(R.id.viewPager);
		layout_dog.setBackgroundColor(dogBackground);
	}

	public <T> void bindData(ArrayList<T> list, ABPagerAdapter<T> listAdapter) {
		viewPager.removeAllViews();
		layout_dog.removeAllViews();
		listAdapter.initData(list);
		viewPager.setAdapter(listAdapter);
		currentItem = list.size() * 20;
		viewPager.setCurrentItem(currentItem,false);
		viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			public void onPageSelected(int position) {
				currentItem = position;
				int index = position % points;
				image_points.get(oldPosition).setBackgroundResource(R.drawable.vp_indicator_normal);
				image_points.get(index).setBackgroundResource(R.drawable.vp_indicator_select);
				oldPosition = index;
			}

			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			public void onPageScrollStateChanged(int arg0) {
			}
		});

		View image_point;
		points = list.size();
		int w = dp2px(radius);
		for (int i = 0; i < points; i++) {
			if(list.size() > 1){
				image_point = new View(mContext);
				LayoutParams params1 = new LayoutParams(w, w);
				params1.leftMargin = 10;
				params1.rightMargin = 10;
				image_point.setLayoutParams(params1);
				image_points.add(image_point);
				image_point.setBackgroundResource(i == viewPager.getCurrentItem()%points ? indicator_select : indicator_normal);
				layout_dog.addView(image_point);
			}
		}

		mCountAutoScroll = list.size() > 1;
	}

	@Override
	protected void onWindowVisibilityChanged(int visibility) {
		super.onWindowVisibilityChanged(visibility);
		if(visibility == VISIBLE && mCountAutoScroll && mAutoScroll){
			handler.postDelayed(runnable, scrollTime);
		}else{
			handler.removeCallbacks(runnable);
		}
	}

	private  int dp2px(float value) {
		final float scale = mContext.getResources().getDisplayMetrics().densityDpi;
		return (int) (value * (scale / 160) + 0.5f);
	}

	Handler handler=new Handler();
	Runnable runnable=new Runnable() {
		@Override
		public void run() {
			currentItem++;
			viewPager.setCurrentItem(currentItem);
			handler.postDelayed(this, scrollTime);
		}
	};
}