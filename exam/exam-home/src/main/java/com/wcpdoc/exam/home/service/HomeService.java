package com.wcpdoc.exam.home.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.wcpdoc.exam.core.service.BaseService;
import com.wcpdoc.exam.sys.entity.User;
 //首页服务层接口
public interface HomeService extends BaseService<Object>{
	 //获取权限总和
	Map<Integer, Long> getAuthSum(Integer userId);
	 // 完成登录
	User doIn(String loginName, String pwd, HttpServletRequest request);
	 // 完成退出
	void doOut(HttpServletRequest request);

}
