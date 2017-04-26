package com.hokol.test.http.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.hokol.medium.http.XHttpUtil;
import com.hokol.medium.http.bean.VHomeMainBean;
import com.hokol.medium.http.bean.WHomeMainBean;
import com.hokol.test.common.BaseTestActivity;
import com.lib.http.XHttpAdapter;

public class TestHomeActivity extends BaseTestActivity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		final EditText editTextOne = addEditNumber("1:网红,2:主播,3:演员,4:模特,5:歌手,6:体育", "1");
		final EditText editTextTwo = addEditNumber("num1", "0");
		final EditText editTextThree = addEditNumber("length", "112");

		addButton("请求主页动态数据", new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				final int user_tag = parseInt(editTextOne, 1);
				final int num1 = parseInt(editTextTwo, 0);
				final int length = parseInt(editTextThree, 1);

				XHttpUtil.doHomeMain(new WHomeMainBean(user_tag, num1, length), new XHttpAdapter<VHomeMainBean>()
				{
					@Override
					public void onSuccess(VHomeMainBean vHomeMainBean)
					{

					}
				});
			}
		});
	}

	public static void actionStart(Context context)
	{
		context.startActivity(new Intent(context, TestHomeActivity.class));
	}
}
