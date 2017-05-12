package com.hokol.viewhelper;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.hokol.R;
import com.hokol.application.DeleteConstant;
import com.hokol.application.IApplication;
import com.hokol.medium.widget.DropMenuWidget;
import com.hokol.medium.widget.FlowAbleWidget;
import com.hokol.medium.widget.labellayout.FlowLayout;
import com.hokol.medium.widget.labellayout.LabelFlowLayout;
import com.hokol.medium.widget.recycler.OnRecyclerItemClickListener;
import com.hokol.medium.widget.secondary.SecondaryWidget;
import com.hokol.medium.widget.swiperefresh.SuperSwipeRefreshLayout;
import com.yline.view.common.HeadFootRecyclerAdapter;
import com.yline.view.common.RecyclerViewHolder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Task helper
 *
 * @author yline 2017/3/7 --> 16:02
 * @version 1.0.0
 */
public class MainTaskHelper
{
	private Context context;

	public MainTaskHelper(Context context)
	{
		this.context = context;
	}

	/* %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% 下拉菜单 %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% */
	private String headers[] = {"分类", "男/女", "地区"};

	private DropMenuWidget dropMenuWidget;

	public void initTabDownMenuView(TabLayout menuTabLayout)
	{
		dropMenuWidget = new DropMenuWidget(context, menuTabLayout);

		List<View> contentViewList = new ArrayList<>();

		View classifyView = initClassifyView();
		contentViewList.add(classifyView);

		View sexView = initSexView();
		contentViewList.add(sexView);

		initAreaView(contentViewList);

		dropMenuWidget.start(Arrays.asList(headers), contentViewList);
	}

	// 分类
	private View initClassifyView()
	{
		View areaView = LayoutInflater.from(context).inflate(R.layout.fragment_main_task__menu_classify, null);

		final LabelFlowLayout labelFlowLayout = (LabelFlowLayout) areaView.findViewById(R.id.label_flow_main_task_menu_classify);
		labelFlowLayout.setLabelGravity(FlowLayout.LabelGravity.EQUIDISTANT);
		labelFlowLayout.setMaxSelectCount(1);
		labelFlowLayout.setMaxCountEachLine(4);
		FlowAbleWidget flowAbleWidget = new FlowAbleWidget(context, labelFlowLayout)
		{
			@Override
			protected int getItemResourceId()
			{
				return R.layout.activity_user_info__flow_able;
			}
		};
		flowAbleWidget.setDataList(Arrays.asList("全部", "网红", "主播", "演员", "模特", "歌手", "体育"));

		Button btnClassify = (Button) areaView.findViewById(R.id.btn_main_task_menu_classify);
		btnClassify.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				dropMenuWidget.closeMenu();
			}
		});

		return areaView;
	}

	// 男女
	private View initSexView()
	{
		View sexView = LayoutInflater.from(context).inflate(R.layout.fragment_main_task__menu_sex, null);

		final LabelFlowLayout labelFlowLayout = (LabelFlowLayout) sexView.findViewById(R.id.label_flow_main_task_menu_sex);
		labelFlowLayout.setMaxSelectCount(1);
		FlowAbleWidget labelClickableWidget = new FlowAbleWidget(context, labelFlowLayout)
		{
			@Override
			protected int getItemResourceId()
			{
				return R.layout.fragment_main_task__flow_able;
			}
		};
		labelClickableWidget.setDataList(Arrays.asList("不限", "女神", "男神"));

		Button btnSex = (Button) sexView.findViewById(R.id.btn_main_task_menu_sex);
		btnSex.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				dropMenuWidget.closeMenu();
			}
		});

		return sexView;
	}

	private SecondaryWidget secondaryWidget;

	// 地区
	private void initAreaView(List<View> contentViewList)
	{
		secondaryWidget = new SecondaryWidget(context, contentViewList);
		secondaryWidget.setOnSecondaryCallback(new SecondaryWidget.OnSecondaryCallback()
		{
			@Override
			public void onSecondarySelected(String first, List<String> second, String title)
			{
				dropMenuWidget.updateTitle(2, title);
				dropMenuWidget.closeMenu();
			}
		});
	}

	public void setAreaData(Map<String, List<String>> map)
	{
		secondaryWidget.setDataMap(map);
	}

	/* %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% Refresh %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% */
	public void initRefreshLayout(final SuperSwipeRefreshLayout swipeRefreshLayout)
	{
		swipeRefreshLayout.setOnRefreshListener(new SuperSwipeRefreshLayout.OnSwipeListener()
		{
			@Override
			public void onAnimate()
			{
				IApplication.toast("正在加载");
				IApplication.getHandler().postDelayed(new Runnable()
				{
					@Override
					public void run()
					{
						IApplication.toast("刷新结束");
						swipeRefreshLayout.setRefreshing(false);
					}
				}, 3000);
			}
		});
		swipeRefreshLayout.setOnLoadListener(new SuperSwipeRefreshLayout.OnSwipeListener()
		{
			@Override
			public void onAnimate()
			{
				IApplication.toast("正在加载");
				IApplication.getHandler().postDelayed(new Runnable()
				{
					@Override
					public void run()
					{
						IApplication.toast("刷新结束");
						swipeRefreshLayout.setLoadMore(false);
					}
				}, 3000);
			}
		});
	}

	/* %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% RecyclerView %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% */

	private TaskRecycleAdapter taskRecycleAdapter;

	public void initRecycleView(RecyclerView recycleView)
	{
		recycleView.setLayoutManager(new LinearLayoutManager(context));
		taskRecycleAdapter = new TaskRecycleAdapter();
		
		recycleView.setAdapter(taskRecycleAdapter);
	}

	public void setOnRecyclerClickListener(OnRecyclerItemClickListener listener)
	{
		taskRecycleAdapter.setOnRecyclerItemClickListener(listener);
	}

	public void setRecycleData()
	{
		List list = new ArrayList();
		for (int i = 0; i < 20; i++)
		{
			list.add("i");
		}
		taskRecycleAdapter.setDataList(list);
	}

	private class TaskRecycleAdapter extends HeadFootRecyclerAdapter
	{
		private OnRecyclerItemClickListener listener;

		public void setOnRecyclerItemClickListener(OnRecyclerItemClickListener listener)
		{
			this.listener = listener;
		}

		@Override
		public int getItemRes()
		{
			return R.layout.item_main_task;
		}

		@Override
		public void setViewContent(final RecyclerViewHolder viewHolder, final int position)
		{
			viewHolder.getItemView().setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					if (null != listener)
					{
						listener.onClick(viewHolder, sList.get(position), position);
					}
				}
			});

			// 设置点击时间
			ImageView imageView = viewHolder.get(R.id.iv_item_main_task_avatar);
			Glide.with(context).load(DeleteConstant.url_default_avatar).centerCrop()
					.bitmapTransform(new CropCircleTransformation(context)).placeholder(R.mipmap.ic_launcher)
					.into(imageView);
		}
	}
}
