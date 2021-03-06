package com.hokol.test.http.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.hokol.medium.http.HttpEnum;
import com.hokol.medium.http.XHttpUtil;
import com.hokol.medium.http.bean.VHomeMainBean;
import com.hokol.medium.http.bean.WHomeMainBean;
import com.hokol.medium.http.hokol.HokolAdapter;
import com.hokol.test.common.BaseTestActivity;

import java.util.Arrays;

public class TestHomeActivity extends BaseTestActivity
{
	public static void actionStart(Context context)
	{
		context.startActivity(new Intent(context, TestHomeActivity.class));
	}

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
				final int index = parseInt(editTextOne, 1);
				HttpEnum.UserTag userTag = HttpEnum.getUserTag(index);

				final int num1 = parseInt(editTextTwo, 0);
				final int length = parseInt(editTextThree, 1);

				WHomeMainBean wHomeMainBean = new WHomeMainBean(userTag, num1, length);
				wHomeMainBean.setP_code("浙江省");
				wHomeMainBean.setC_code(Arrays.asList("杭州市"));
				XHttpUtil.doHomeMain(wHomeMainBean, new HokolAdapter<VHomeMainBean>()
				{
					@Override
					public void onSuccess(VHomeMainBean vHomeMainBean)
					{

					}
				});
			}
		});
	}
}
