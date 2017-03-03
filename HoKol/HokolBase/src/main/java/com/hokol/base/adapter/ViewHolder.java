package com.hokol.base.adapter;

import android.app.Activity;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 统一管理View
 *
 * @author yline 2017/3/2 --> 14:54
 * @version 1.0.0
 */
public class ViewHolder
{
	private SparseArray<View> sArray;

	private View sView;

	public ViewHolder(View view)
	{
		this.sView = view;
		sArray = new SparseArray<>();
	}

	public ViewHolder(Activity activity)
	{
		
	}

	/**
	 * 获取到相应的资源
	 *
	 * @param viewId
	 * @return
	 */
	public <T extends View> T get(int viewId)
	{
		if (sArray.get(viewId) == null)
		{
			View view = sView.findViewById(viewId);
			sArray.put(viewId, view);
		}
		return (T) sArray.get(viewId);
	}

	/**
	 * 要求是TextView;   这样的方法就可以多写几个,然后就可以作死的连缀了
	 *
	 * @param viewId  资源
	 * @param content 内容
	 * @return 为了连缀写法, 返回自身
	 */
	public ViewHolder setText(int viewId, String content)
	{
		TextView textView = this.get(viewId);
		textView.setText(content);
		return this;
	}

	/**
	 * 要求是ImageView;
	 *
	 * @param viewId 资源id
	 * @param resId  图片背景id
	 * @return
	 */
	public ViewHolder setImageBackgroundResource(int viewId, int resId)
	{
		ImageView imageView = this.get(viewId);
		imageView.setBackgroundResource(resId);
		return this;
	}
}
