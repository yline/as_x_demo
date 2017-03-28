package com.hokol.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.hokol.R;
import com.hokol.base.common.BaseAppCompatActivity;

public class UserVIPActivity extends BaseAppCompatActivity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_vip);
	}

	public static void actionStart(Context context)
	{
		context.startActivity(new Intent(context, UserVIPActivity.class));
	}
}