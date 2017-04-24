package com.hokol.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.hokol.R;
import com.hokol.activity.StarDynamicActivity;
import com.hokol.application.DeleteConstant;
import com.hokol.application.IApplication;
import com.hokol.medium.widget.recycler.DefaultGridItemDecoration;
import com.hokol.medium.widget.recycler.HeadFootRecyclerAdapter;
import com.hokol.medium.widget.recycler.OnRecyclerItemClickListener;
import com.hokol.medium.widget.swiperefresh.SuperSwipeRefreshLayout;
import com.yline.base.BaseFragment;
import com.yline.common.CommonRecyclerViewHolder;
import com.yline.utils.UIResizeUtil;
import com.yline.utils.UIScreenUtil;

import java.util.ArrayList;
import java.util.List;

public class StarInfoDynamicFragment extends BaseFragment
{
	private HeadFootRecyclerAdapter starInfoDynamicAdapter;

	private SuperSwipeRefreshLayout superRefreshLayout;

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
	{
		return inflater.inflate(R.layout.fragment_star_info_dynamic, container, false);
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
	{
		super.onViewCreated(view, savedInstanceState);
		initView(view);
	}

	private void initView(View view)
	{
		RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycle_star_info_dynamic);
		recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
		recyclerView.addItemDecoration(new DefaultGridItemDecoration(getContext())
		{
			@Override
			protected int getDivideResourceId()
			{
				return R.drawable.widget_recycler_divider_white_small;
			}
		});

		starInfoDynamicAdapter = new StarInfoDynamicAdapter();
		recyclerView.setAdapter(starInfoDynamicAdapter);
		starInfoDynamicAdapter.setOnRecyclerItemClickListener(new OnRecyclerItemClickListener()
		{
			@Override
			public void onClick(RecyclerView.ViewHolder viewHolder, Object o, int position)
			{
				StarDynamicActivity.actionStart(getContext());
			}
		});

		List<String> data = new ArrayList<>();
		for (int i = 0; i < 35; i++)
		{
			data.add(DeleteConstant.getUrlSquare());
		}
		starInfoDynamicAdapter.addAll(data);

		superRefreshLayout = (SuperSwipeRefreshLayout) view.findViewById(R.id.super_swipe_star_info_dynamic);
		superRefreshLayout.setOnRefreshListener(new SuperSwipeRefreshLayout.OnSwipeListener()
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
						superRefreshLayout.setRefreshing(false);
					}
				}, 2000);
			}
		});
		superRefreshLayout.setOnLoadListener(new SuperSwipeRefreshLayout.OnSwipeListener()
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
						superRefreshLayout.setLoadMore(false);
					}
				}, 2000);
			}
		});
	}

	private class StarInfoDynamicAdapter extends HeadFootRecyclerAdapter<String>
	{
		private final int border_square;

		public StarInfoDynamicAdapter()
		{
			border_square = UIScreenUtil.getScreenWidth(getContext()) / 3 - 10;
		}

		@Override
		public int getItemRes()
		{
			return R.layout.item_star_info_dynamic;
		}

		@Override
		public void setViewContent(CommonRecyclerViewHolder viewHolder, int position)
		{
			ImageView imageView = viewHolder.get(R.id.iv_item_star_info_dynamic);

			UIResizeUtil.build().setWidth(border_square).setHeight(border_square).commit(imageView);

			Glide.with(getContext()).load(sList.get(position)).into(imageView);
		}
	}
}






















