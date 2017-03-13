package com.hokol.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.hokol.R;

/**
 * 动态信息详情界面
 *
 * @author yline 2017/3/5 --> 15:12
 * @version 1.0.0
 */
public class DynamicInfoActivity extends AppCompatActivity
{
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dynamic_info);
	}


	public static void actionStart(Context context)
	{
		context.startActivity(new Intent(context, DynamicInfoActivity.class));
	}
}
