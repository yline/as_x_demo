package com.hokol.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.hokol.R;
import com.hokol.medium.callback.OnRecyclerDeleteCallback;
import com.hokol.medium.http.HttpEnum;
import com.hokol.medium.http.bean.VTaskUserPublishedBean;
import com.hokol.medium.widget.recycler.WidgetRecyclerAdapter;
import com.hokol.util.HokolTimeConvertUtil;
import com.yline.view.recycler.holder.RecyclerViewHolder;

/**
 * 已发任务
 *
 * @author yline 2017/6/22 -- 16:47
 * @version 1.0.0
 */
public class TaskAssignedAdapter extends WidgetRecyclerAdapter<VTaskUserPublishedBean.VTaskUserPublishedOneBean>
{
	// 待报名 回调
	private OnTaskAssignedSignCallback assignedSignCallback;

	// 待交易 回调
	private OnTaskAssignedTradeCallback assignedTradeCallback;

	// 评价 回调
	private OnTaskAssignedEvaluateCallback assignedEvaluateCallback;

	// 长按回调, 只有过期的，并且结束的按钮，才能出现删除按钮
	private OnRecyclerDeleteCallback<VTaskUserPublishedBean.VTaskUserPublishedOneBean> onRecyclerDeleteCallback;

	private Context sContext;

	public TaskAssignedAdapter(Context context)
	{
		this.sContext = context;
	}

	@Override
	public int getItemRes()
	{
		return R.layout.item_task_assigned;
	}

	@Override
	public void onBindViewHolder(final RecyclerViewHolder viewHolder, final int position)
	{
		super.onBindViewHolder(viewHolder, position);

		VTaskUserPublishedBean.VTaskUserPublishedOneBean taskBean = sList.get(position);
		// 初始化数据
		viewHolder.setText(R.id.tv_item_main_task_price, String.format("￥%d × %d", taskBean.getTask_fee(), taskBean.getTask_peo_num()));
		viewHolder.setText(R.id.tv_item_main_task_title, taskBean.getTask_title());

		// 右侧状态
		String rightState = taskBean.getIs_guarantee() == VTaskUserPublishedBean.Guaranteed ? "已担保交易" : "未担保交易";
		viewHolder.setText(R.id.tv_item_main_task_state, rightState);
		
		// 头像
		ImageView avatarImageView = viewHolder.get(R.id.iv_item_main_task_avatar);
		Glide.with(sContext).load(taskBean.getUser_logo()).into(avatarImageView);

		// 用户名
		viewHolder.setText(R.id.tv_item_main_task_user, taskBean.getUser_nickname());

		// 报名 状态
		viewHolder.setText(R.id.tv_item_main_task_user_state, String.format("%d人报名，%d人录用", taskBean.getJoin_num(), taskBean.getEmployee_num()));

		// 截止时间
		String showTime = HokolTimeConvertUtil.stampToRestFormatTime(taskBean.getTask_end_time() * 1000);
		if (TextUtils.isEmpty(showTime))
		{
			viewHolder.setText(R.id.tv_item_main_task_time, "已到期");

		}
		else
		{
			viewHolder.setText(R.id.tv_item_main_task_time, "剩" + showTime);
		}

		// 初始状态
		int status = taskBean.getStatus();
		HttpEnum.AssignedStatus assignedStatus = HttpEnum.getAssignedStatus(status);
		onBindViewClick(viewHolder, sList.get(position), assignedStatus, position);
	}

	@Override
	public int getEmptyItemRes()
	{
		return super.getEmptyItemRes();
	}

	@Override
	public void onBindEmptyViewHolder(RecyclerViewHolder viewHolder, int position)
	{
		viewHolder.setText(R.id.tv_loading_cover, "还没有任务哦");
		// viewHolder.setText(R.id.btn_loading_cover, "发布任务");
	}

	private void onBindViewClick(final RecyclerViewHolder viewHolder, final VTaskUserPublishedBean.VTaskUserPublishedOneBean bean, final HttpEnum.AssignedStatus assignedStatus, final int position)
	{
		// 待报名
		if (assignedStatus.equals(HttpEnum.AssignedStatus.ToBeSign))
		{
			viewHolder.get(R.id.ll_task_assigned_start).setVisibility(View.VISIBLE);
			viewHolder.setOnClickListener(R.id.tv_task_assigned_start_cancel, new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					if (null != assignedSignCallback)
					{
						assignedSignCallback.onSignCancelClick(v, bean.getTask_id());
					}
				}
			});
			viewHolder.setOnClickListener(R.id.tv_task_assigned_start_over, new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					if (null != assignedSignCallback)
					{
						assignedSignCallback.onSignFinishClick(v, bean.getTask_id());
					}
				}
			});
			viewHolder.setOnClickListener(R.id.tv_task_assigned_start_detail, new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					if (null != assignedSignCallback)
					{
						if (VTaskUserPublishedBean.TypeNegotiable == bean.getFee_type())
						{
							assignedSignCallback.onSignDetailClick(v, bean.getTask_id(), true);
						}
						else
						{
							assignedSignCallback.onSignDetailClick(v, bean.getTask_id(), false);
						}
					}
				}
			});
		}
		else
		{
			viewHolder.get(R.id.ll_task_assigned_start).setVisibility(View.INVISIBLE);
		}

		// 待交易
		if (assignedStatus.equals(HttpEnum.AssignedStatus.ToBeTrade))
		{
			viewHolder.get(R.id.ll_task_assigned_trade).setVisibility(View.VISIBLE);
			viewHolder.setOnClickListener(R.id.tv_task_assigned_trade_cancel, new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					if (null != assignedTradeCallback)
					{
						assignedTradeCallback.onTradeCancelClick(v, bean.getTask_id(), (bean.getEmployee_num() > 0));
					}
				}
			});
			viewHolder.setOnClickListener(R.id.tv_task_assigned_trade_detail, new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					if (null != assignedTradeCallback)
					{
						assignedTradeCallback.onTradeDetailClick(v, bean.getTask_id());
					}
				}
			});
			viewHolder.setOnClickListener(R.id.tv_task_assigned_trade_sure_detail, new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					if (null != assignedTradeCallback)
					{
						assignedTradeCallback.onTradeConfirmClick(v, bean.getTask_id());
					}
				}
			});
		}
		else
		{
			viewHolder.get(R.id.ll_task_assigned_trade).setVisibility(View.INVISIBLE);
		}

		// 评论
		if (assignedStatus.equals(HttpEnum.AssignedStatus.ToBeEvaluate))
		{
			viewHolder.get(R.id.ll_task_assigned_finish).setVisibility(View.VISIBLE);
			viewHolder.setOnClickListener(R.id.tv_item_task_assigned_evaluate, new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					if (null != assignedEvaluateCallback)
					{
						assignedEvaluateCallback.onEvaluateClick(v, bean.getTask_id());
					}
				}
			});
		}
		else
		{
			viewHolder.get(R.id.ll_task_assigned_finish).setVisibility(View.INVISIBLE);
		}

		// 删除任务
		if (assignedStatus == HttpEnum.AssignedStatus.Finished || assignedStatus == HttpEnum.AssignedStatus.Canceled || assignedStatus == HttpEnum.AssignedStatus.Passed)
		{
			viewHolder.get(R.id.ll_task_assigned_delete).setVisibility(View.VISIBLE);
			viewHolder.setOnClickListener(R.id.tv_item_task_assigned_delete, new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					if (null != onRecyclerDeleteCallback)
					{
						onRecyclerDeleteCallback.onDelete(viewHolder, bean, position);
					}
				}
			});
		}
		else
		{
			viewHolder.get(R.id.ll_task_assigned_delete).setVisibility(View.INVISIBLE);
		}
	}

	/**
	 * 更新任务 数据
	 * 并不调用item更新
	 *
	 * @param position
	 * @param newStatus
	 */
	public void updateStatus(int position, int newStatus)
	{
		sList.get(position).setStatus(newStatus);
	}

	public void setOnAssignedSignCallback(OnTaskAssignedSignCallback assignedSignCallback)
	{
		this.assignedSignCallback = assignedSignCallback;
	}

	public void setOnAssignedTradeCallback(OnTaskAssignedTradeCallback assignedTradeCallback)
	{
		this.assignedTradeCallback = assignedTradeCallback;
	}

	public void setOnAssignedEvaluateCallback(OnTaskAssignedEvaluateCallback assignedEvaluateCallback)
	{
		this.assignedEvaluateCallback = assignedEvaluateCallback;
	}

	public void setOnRecyclerDeleteCallback(OnRecyclerDeleteCallback<VTaskUserPublishedBean.VTaskUserPublishedOneBean> onRecyclerDeleteCallback)
	{
		this.onRecyclerDeleteCallback = onRecyclerDeleteCallback;
	}

	/**
	 * 待报名
	 */
	public interface OnTaskAssignedSignCallback
	{
		/**
		 * 取消任务
		 *
		 * @param view
		 */
		void onSignCancelClick(View view, String taskId);

		/**
		 * 结束报名
		 *
		 * @param view
		 */
		void onSignFinishClick(View view, String taskId);

		/**
		 * 报名详情
		 *
		 * @param view
		 */
		void onSignDetailClick(View view, String taskId, boolean isNegotiable);
	}

	/**
	 * 待交易
	 */
	public interface OnTaskAssignedTradeCallback
	{
		/**
		 * 取消交易
		 *
		 * @param view      view
		 * @param taskId    任务id
		 * @param hasEmploy 对方是否确认接单
		 */
		void onTradeCancelClick(View view, String taskId, boolean hasEmploy);

		/**
		 * 交易详情
		 *
		 * @param view
		 */
		void onTradeDetailClick(View view, String taskId);

		/**
		 * 确认交易
		 *
		 * @param view
		 */
		void onTradeConfirmClick(View view, String taskId);
	}

	/**
	 * 待评价
	 */
	public interface OnTaskAssignedEvaluateCallback
	{
		/**
		 * 评价
		 *
		 * @param view
		 */
		void onEvaluateClick(View view, String taskId);
	}

	/**
	 * 刷新数据
	 */
	public interface OnTaskAssignedRefreshCallback
	{
		/**
		 * 重新刷新 "全部"中的数据
		 */
		void onAllRefresh(int start, int length);

		/**
		 * 重新刷新 "待报名" 中的数据
		 */
		void onSignRefresh(int start, int length);

		/**
		 * 重新刷新 "待交易" 中的数据
		 */
		void onTradeRefresh(int start, int length);

		/**
		 * 重新刷新 "待评价" 中的数据
		 */
		void onEvaluateRefresh(int start, int length);
	}

	public interface OnTaskAssignedRefreshListener
	{
		/**
		 * 重新刷新数据
		 */
		void onRefreshData(String userId, int start, int length);
	}
}
