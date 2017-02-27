package com.hokol.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hokol.R;
import com.hokol.base.common.BaseFragment;

import java.util.ArrayList;
import java.util.List;

public class MainNewsFragment extends BaseFragment
{
	private List<Fragment> fragmentList;

	private ViewPager viewPager;

	private TabLayout tabLayout;

	private static final int[] IDS = new int[]{R.string.news_title_one, R.string.news_title_two, R.string.news_title_three, R.string.news_title_four, R.string.news_title_five};

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
	{
		return inflater.inflate(R.layout.fragment_main_news, container, false);
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
	{
		super.onViewCreated(view, savedInstanceState);

		initView(view);
		initData();
	}

	private void initView(View view)
	{
		viewPager = (ViewPager) view.findViewById(R.id.viewpager_main_news);
		tabLayout = (TabLayout) view.findViewById(R.id.tab_layout_main_news);
	}

	private void initData()
	{
		fragmentList = new ArrayList<>();

		MainNewsHotFragment newsHotFragment = new MainNewsHotFragment();
		fragmentList.add(newsHotFragment);

		DeleteFragment deleteFragment2 = new DeleteFragment();
		fragmentList.add(deleteFragment2);

		DeleteFragment deleteFragment3 = new DeleteFragment();
		fragmentList.add(deleteFragment3);

		DeleteFragment deleteFragment4 = new DeleteFragment();
		fragmentList.add(deleteFragment4);

		DeleteFragment deleteFragment5 = new DeleteFragment();
		fragmentList.add(deleteFragment5);

		viewPager.setOffscreenPageLimit(5);
		viewPager.setAdapter(new FragmentPagerAdapter(getChildFragmentManager())
		{
			@Override
			public Fragment getItem(int position)
			{
				return fragmentList.get(position);
			}

			@Override
			public int getCount()
			{
				return fragmentList.size();
			}

			@Override
			public CharSequence getPageTitle(int position)
			{
				return getResources().getString(IDS[position]);
			}
		});
		viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener()
		{
			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
			{

			}

			@Override
			public void onPageSelected(int position)
			{
				if (fragmentList.get(position) instanceof DeleteFragment)
				{
					((DeleteFragment) fragmentList.get(position)).setText(IDS[position]);
				}
			}

			@Override
			public void onPageScrollStateChanged(int state)
			{

			}
		});

		tabLayout.setupWithViewPager(viewPager);
		tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
		tabLayout.setTabMode(TabLayout.MODE_FIXED);
	}
}
