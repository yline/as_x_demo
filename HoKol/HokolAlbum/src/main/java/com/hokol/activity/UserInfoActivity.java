package com.hokol.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.hokol.R;

/**
 * 人物信息详情页面
 *
 * @author yline 2017/3/5 --> 15:24
 * @version 1.0.0
 */
public class UserInfoActivity extends AppCompatActivity
{
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_info);
	}

	public static void actionStart(Context context)
	{
		context.startActivity(new Intent(context, UserInfoActivity.class));
	}
}
