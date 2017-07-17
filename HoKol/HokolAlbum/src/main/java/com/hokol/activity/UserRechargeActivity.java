package com.hokol.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.hokol.R;
import com.hokol.application.AppStateManager;
import com.hokol.application.DeleteConstant;
import com.hokol.medium.http.XHttpUtil;
import com.hokol.medium.http.bean.VAliPayOrderInfoBean;
import com.hokol.medium.http.bean.WAliPayOrderInfoBean;
import com.hokol.medium.module.AliPayActivity;
import com.hokol.medium.widget.DialogIosWidget;
import com.hokol.medium.widget.recycler.DefaultGridItemDecoration;
import com.hokol.medium.widget.recycler.WidgetRecyclerAdapter;
import com.yline.application.SDKManager;
import com.yline.http.XHttpAdapter;
import com.yline.log.LogFileUtil;
import com.yline.view.recycler.callback.OnRecyclerItemClickListener;
import com.yline.view.recycler.holder.RecyclerViewHolder;
import com.yline.view.recycler.holder.ViewHolder;

import java.util.Arrays;

public class UserRechargeActivity extends AliPayActivity
{
	private static final String KeyRechargeUserId = "UserId";

	private ViewHolder viewHolder;

	private UserRechargeAdapter rechargeAdapter;

	private String userId;

	public static void actionStart(Context context, String userId)
	{
		context.startActivity(new Intent(context, UserRechargeActivity.class).putExtra(KeyRechargeUserId, userId));
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_recharge);

		viewHolder = new ViewHolder(this);
		initView();
		initData();
	}

	private void initView()
	{
		RecyclerView recyclerView = viewHolder.get(R.id.recycler_user_recharge);
		recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
		recyclerView.addItemDecoration(new DefaultGridItemDecoration(this)
		{
			@Override
			protected int getDivideResourceId()
			{
				return R.drawable.widget_solid_null_size_medium;
			}
		});

		rechargeAdapter = new UserRechargeAdapter();
		recyclerView.setAdapter(rechargeAdapter);

		rechargeAdapter.setDataList(Arrays.asList(100f, 680f, 1280f, 2680f, 5180f, 9980f));

		viewHolder.setOnClickListener(R.id.iv_user_recharge_cancel, new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				finish();
			}
		});
		viewHolder.setOnClickListener(R.id.tv_user_recharge_record, new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				UserRechargeRecordActivity.actionStart(UserRechargeActivity.this, userId);
			}
		});

		viewHolder.setOnClickListener(R.id.iv_user_recharge_wechat, new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				SDKManager.toast("微信充值");
			}
		});
		viewHolder.setOnClickListener(R.id.iv_user_recharge_ali_pay, new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				XHttpUtil.doAliPayOrderInfo(new WAliPayOrderInfoBean(userId, 0.01f), new XHttpAdapter<VAliPayOrderInfoBean>()
				{
					@Override
					public void onSuccess(VAliPayOrderInfoBean vAliPayOrderInfoBean)
					{
						doPay(vAliPayOrderInfoBean.getMess());
					}
					
					@Override
					public void onFailure(Exception ex)
					{
						super.onFailure(ex);
						SDKManager.toast("网络异常");
					}
				});
			}
		});

		rechargeAdapter.setOnRecyclerItemClickListener(new OnRecyclerItemClickListener<Integer>()
		{
			@Override
			public void onItemClick(RecyclerViewHolder viewHolder, Integer integer, int position)
			{
				SDKManager.toast("coin = " + integer);
			}
		});
	}

	private void initData()
	{
		// id
		userId = getIntent().getStringExtra(KeyRechargeUserId);

		// 充值账号
		String userTel = AppStateManager.getInstance().getUserTel(this);
		viewHolder.setText(R.id.tv_user_recharge, String.format("充值账号：%s", userTel));

		// 硬币
		int userCoin = AppStateManager.getInstance().getUserCoinNum(this);
		viewHolder.setText(R.id.tv_user_coin_num, String.format("余额：%d红豆", userCoin));
	}

	@Override
	public void onPayBack(final String memo, final String status, final String jsonResult)
	{
		LogFileUtil.v("memo = " + memo + ", status = " + status + ", jsonResult = " + jsonResult);
		DialogIosWidget dialogIosWidget = new DialogIosWidget(this)
		{
			@Override
			protected void initXView(TextView tvTitle, TextView tvMsg, Button btnNegative, Button btnPositive, Dialog dialog)
			{
				super.initXView(tvTitle, tvMsg, btnNegative, btnPositive, dialog);
				tvTitle.setText("memo = " + memo + "\n" + "status = " + status + "\n" + ", jsonResult = " + jsonResult);
			}
		};
		dialogIosWidget.show();
	}

	private class UserRechargeAdapter extends WidgetRecyclerAdapter<Float>
	{
		private int oldPosition = 0;

		@Override
		public int getItemRes()
		{
			return R.layout.item_user_recharge_value;
		}

		@Override
		public void onBindViewHolder(final RecyclerViewHolder holder, final int position)
		{
			super.onBindViewHolder(holder, position);

			holder.setText(R.id.tv_user_recharge_value_top, String.format("%.0f红豆", sList.get(position)));
			holder.setText(R.id.tv_user_recharge_value, String.format("￥%3.2f", sList.get(position) / DeleteConstant.ScaleOfHokolCoin));

			if (position == oldPosition)
			{
				holder.getItemView().setSelected(true);
			}
			else
			{
				holder.getItemView().setSelected(false);
			}

			holder.getItemView().setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					if (position != oldPosition)
					{
						if (-1 != oldPosition)
						{
							notifyItemChanged(oldPosition);
						}

						oldPosition = position;
						notifyItemChanged(oldPosition);
					}
				}
			});
		}
	}
}
