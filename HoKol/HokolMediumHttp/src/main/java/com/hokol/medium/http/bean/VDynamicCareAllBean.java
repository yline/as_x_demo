package com.hokol.medium.http.bean;

import java.util.List;

/**
 * 用户关注的人,所有动态;返回参数
 *
 * @author yline 2017/3/8 --> 23:14
 * @version 1.0.0
 */
public class VDynamicCareAllBean
{
	private List<VDynamicCareBean> list;

	public List<VDynamicCareBean> getList()
	{
		return list;
	}

	public void setList(List<VDynamicCareBean> list)
	{
		this.list = list;
	}

	@Override
	public String toString()
	{
		return "VDynamicCareAllBean{" +
				"list=" + list +
				'}';
	}
}
