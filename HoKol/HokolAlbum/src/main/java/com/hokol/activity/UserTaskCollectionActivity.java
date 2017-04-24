package com.hokol.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.hokol.R;
import com.yline.base.BaseAppCompatActivity;

public class UserTaskCollectionActivity extends BaseAppCompatActivity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_task_collection);
	}

	public static void actionStart(Context context)
	{
		context.startActivity(new Intent(context, UserTaskCollectionActivity.class));
	}
}
