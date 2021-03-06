package com.hokol.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.hokol.R;
import com.hokol.activity.TaskAssignedActivity;
import com.hokol.activity.TaskDetailActivity;
import com.hokol.activity.TaskPublishActivity;
import com.hokol.application.AppStateManager;
import com.hokol.application.DeleteConstant;
import com.hokol.medium.http.HttpEnum;
import com.hokol.medium.http.XHttpUtil;
import com.hokol.medium.http.bean.VAreaAllBean;
import com.hokol.medium.http.bean.VRecommendHomeBean;
import com.hokol.medium.http.bean.VRecommendTaskBean;
import com.hokol.medium.http.bean.VTaskMainAllBean;
import com.hokol.medium.http.bean.WTaskMainAllBean;
import com.hokol.medium.http.hokol.HokolAdapter;
import com.hokol.medium.viewcustom.SuperSwipeRefreshLayout;
import com.hokol.medium.widget.ADWidget;
import com.hokol.util.IntentUtil;
import com.hokol.viewhelper.MainTaskHelper;
import com.yline.application.SDKManager;
import com.yline.base.BaseFragment;
import com.yline.log.LogFileUtil;
import com.yline.utils.UIScreenUtil;
import com.yline.view.recycler.callback.OnRecyclerItemClickListener;
import com.yline.view.recycler.holder.RecyclerViewHolder;

import java.util.ArrayList;
import java.util.List;

public class MainTaskFragment extends BaseFragment implements MainTaskHelper.OnTaskFilterCallback
{
	private MainTaskHelper mainTaskHelper;

	private SuperSwipeRefreshLayout superRefreshLayout;

	private WTaskMainAllBean taskMainAll;

	private int taskRefreshNumber;

	private LinearLayout adLinearLayout;

	public static MainTaskFragment newInstance()
	{
		Bundle args = new Bundle();

		MainTaskFragment fragment = new MainTaskFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
	{
		return inflater.inflate(R.layout.fragment_main_task, container, false);
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
	{
		super.onViewCreated(view, savedInstanceState);

		mainTaskHelper = new MainTaskHelper(getContext());
		initView(view);
		initData();
	}

	private void initView(View parentView)
	{
		// 广告
		adLinearLayout = (LinearLayout) parentView.findViewById(R.id.ll_main_task_ad);

		// 下拉菜单
		TabLayout menuTabLayout = (TabLayout) parentView.findViewById(R.id.tab_main_task_menu);
		mainTaskHelper.initTabDownMenuView(menuTabLayout);

		// 刷新
		superRefreshLayout = (SuperSwipeRefreshLayout) parentView.findViewById(R.id.swipe_main_task);
		mainTaskHelper.initRefreshLayout(superRefreshLayout);

		// 内容
		RecyclerView recycleView = (RecyclerView) parentView.findViewById(R.id.recycle_main_task);
		mainTaskHelper.initRecycleView(recycleView);
		mainTaskHelper.setOnRecyclerClickListener(new OnRecyclerItemClickListener<VTaskMainAllBean.TaskMainAllOne>()
		{
			@Override
			public void onItemClick(RecyclerViewHolder viewHolder, VTaskMainAllBean.TaskMainAllOne taskDetailBean, int position)
			{
				TaskDetailActivity.actionStart(getContext(), taskDetailBean.getTask_id(), false);
			}
		});

		mainTaskHelper.setOnTaskFilterCallback(this);

		final String userId = AppStateManager.getInstance().getUserLoginId(getContext());

		// 头部
		parentView.findViewById(R.id.iv_main_task_action_task).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				String userId = AppStateManager.getInstance().getUserLoginId(getContext());
				if (!TextUtils.isEmpty(userId))
				{
					TaskPublishActivity.actionStart(getContext(), userId);
				}
				else
				{
					SDKManager.toast("亲，请先登录");
				}
			}
		});
		parentView.findViewById(R.id.iv_main_task_action_history).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if (TextUtils.isEmpty(userId))
				{
					SDKManager.toast("亲，请先登录");
				}
				else
				{
					TaskAssignedActivity.actionStart(getContext(), userId);
				}
			}
		});
	}

	private void initData()
	{
		// AD 广告
		XHttpUtil.doRecommendTask(new HokolAdapter<VRecommendTaskBean>()
		{
			@Override
			public void onSuccess(VRecommendTaskBean vRecommendTaskBean)
			{
				final List<VRecommendTaskBean.VRecommendTaskOneBean> resultList = vRecommendTaskBean.getList();
				if (null != resultList && 0 != resultList.size())
				{
					adLinearLayout.removeAllViews(); // 清除所有的View

					ADWidget adWidget = new ADWidget()
					{
						@Override
						protected int getViewPagerHeight()
						{
							return UIScreenUtil.dp2px(getContext(), 120);
						}
					};
					View adView = adWidget.start(getContext(), resultList.size());
					adLinearLayout.addView(adView);
					adWidget.setListener(new ADWidget.OnPageListener()
					{
						@Override
						public void onPageClick(View v, int position)
						{
							VRecommendTaskBean.VRecommendTaskOneBean recommendBean = resultList.get(position);

							if (recommendBean.getType() == VRecommendHomeBean.TypeUser)
							{
								TaskDetailActivity.actionStart(getContext(), recommendBean.getInfo(), false);
							}
							else if (recommendBean.getType() == VRecommendHomeBean.TypeUrl)
							{
								IntentUtil.openBrower(getContext(), recommendBean.getInfo());
							}
							else
							{
								LogFileUtil.v("Task Recommend type error");
							}
						}

						@Override
						public void onPageInstance(ImageView imageView, int position)
						{
							Glide.with(getContext()).load(resultList.get(position).getBanner_img()).placeholder(R.drawable.global_load_failed).error(R.drawable.global_load_failed).into(imageView);
						}
					});
				}
				else
				{
					LogFileUtil.v("Task Recommend list is null or size is zero");
				}
			}
		});

		// 地区
		XHttpUtil.doAreaAll(new HokolAdapter<VAreaAllBean>()
		{
			@Override
			public void onSuccess(VAreaAllBean vAreaAllBean)
			{
				mainTaskHelper.setAreaData(vAreaAllBean);
			}
		});

		taskRefreshNumber = 0;
		taskMainAll = new WTaskMainAllBean(0, DeleteConstant.defaultNumberNormal);
		doRequest();
	}

	private void doRequest()
	{
		// Recycler
		mainTaskHelper.setRecyclerShowEmpty(false);
		XHttpUtil.doTaskMainAll(taskMainAll, new HokolAdapter<VTaskMainAllBean>()
		{
			@Override
			public void onSuccess(VTaskMainAllBean vTaskMainAll)
			{
				mainTaskHelper.setRecyclerShowEmpty(true);

				List<VTaskMainAllBean.TaskMainAllOne> result = vTaskMainAll.getList();
				if (null != result)
				{
					taskRefreshNumber = result.size();
					mainTaskHelper.setRecyclerData(result);
				}
				else
				{
					taskRefreshNumber = 0;
					mainTaskHelper.setRecyclerData(new ArrayList<VTaskMainAllBean.TaskMainAllOne>());
				}
			}
		});
	}

	@Override
	public void onFilterClassify(HttpEnum.UserTag userTag)
	{
		LogFileUtil.v("userTag = " + userTag);

		taskRefreshNumber = 0;
		taskMainAll.setNum1(0);
		taskMainAll.setLength(DeleteConstant.defaultNumberNormal);

		taskMainAll.setTask_tag(userTag.getIndex());

		doRequest();
	}

	@Override
	public void onFilterSex(HttpEnum.UserSex userSex)
	{
		LogFileUtil.v("userSex = " + userSex);

		taskRefreshNumber = 0;
		taskMainAll.setNum1(0);
		taskMainAll.setLength(DeleteConstant.defaultNumberNormal);

		taskMainAll.setTask_sex(userSex.getIndex());

		doRequest();
	}

	@Override
	public void onFilterArea(String firstCode, List<String> secondCodeList)
	{
		LogFileUtil.v("first = " + firstCode + ", second = " + secondCodeList.toString());

		taskRefreshNumber = 0;
		taskMainAll.setNum1(0);
		taskMainAll.setLength(DeleteConstant.defaultNumberNormal);

		taskMainAll.setP_code(firstCode);
		taskMainAll.setC_code(secondCodeList);

		doRequest();
	}
}
