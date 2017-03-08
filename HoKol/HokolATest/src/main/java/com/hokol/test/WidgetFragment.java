package com.hokol.test;

import android.view.View;

import com.hokol.test.widget.ADActivity;
import com.hokol.test.widget.DropMenuActivity;
import com.hokol.test.widget.LabelLayoutActivity;

public class WidgetFragment extends BaseTestFragment
{
	@Override
	protected void test()
	{
		addButton("AD 广告", new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				ADActivity.actionStart(getContext());
			}
		});

		addButton("下拉菜单", new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				DropMenuActivity.actionStart(getContext());
			}
		});

		addButton("多个标签", new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				LabelLayoutActivity.actionStart(getContext());
			}
		});
	}
}