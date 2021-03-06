package com.hokol.medium.http.bean;

public class WEnterLoginPhonePwdBean
{
	/**
	 * 用户手机
	 */
	private String user_tel;

	/**
	 * 用户密码
	 */
	private String user_pwd;

	public WEnterLoginPhonePwdBean(String user_tel, String user_pwd)
	{
		this.user_tel = user_tel;
		this.user_pwd = user_pwd;
	}

	public String getUser_tel()
	{
		return user_tel;
	}

	public void setUser_tel(String user_tel)
	{
		this.user_tel = user_tel;
	}

	public String getUser_pwd()
	{
		return user_pwd;
	}

	public void setUser_pwd(String user_pwd)
	{
		this.user_pwd = user_pwd;
	}

	@Override
	public String toString()
	{
		return "WEnterLoginPhonePwdBean{" +
				"user_tel='" + user_tel + '\'' +
				", user_pwd='" + user_pwd + '\'' +
				'}';
	}
}
