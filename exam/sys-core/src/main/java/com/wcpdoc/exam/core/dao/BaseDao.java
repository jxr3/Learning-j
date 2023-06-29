package com.wcpdoc.exam.core.dao;

import java.util.List;
import java.util.Map;

import org.hibernate.Session;

import com.wcpdoc.exam.core.entity.PageIn;
import com.wcpdoc.exam.core.entity.PageOut;
import com.wcpdoc.exam.core.util.SqlUtil;

/**
 * 数据访问层接口
 */
public interface BaseDao<T> {
	/**
	 * 添加实体
	 */
	public void add(T entity);

	/**
	 * 修改实体
	 */
	public void update(T entity);

	/**
	 * 删除实体
	 */
	public void del(Integer id);

	/**
	 * 删除实体
	 */
	public void del(Integer[] ids);

	/**
	 * 获取实体
	 */
	public T getEntity(Integer id);

	/**
	 * 获取当前线程上绑定的session
	 */
	public Session getCurSession();

	/**
	 * 把当前线程的绑定的session缓存刷入数据库
	 */
	public void flush();

	/**
	 * 获取分页列表。
	 */
	public PageOut getListpage(PageIn pageIn);

	/**
	 * 获取分页列表。
	 */
	public PageOut getListpage(SqlUtil sqlUtil, PageIn pageIn);

	/**
	 * 返回列表
	 */
	public List<Map<String, Object>> getList(String sql, Object[] params);

	/**
	 * 返回列表
	 */
	public List<Map<String, Object>> getList(String sql);

	/**
	 * 返回列表
	 */
	@SuppressWarnings("hiding")
	public <T> List<T> getList(String sql, Object[] params, Class<T> t);

	/**
	 * 返回列表
	 */
	@SuppressWarnings("hiding")
	public <T> List<T> getList(String sql, Class<T> t);

	/**
	 * 返回单条数据
	 * @param sql
	 * @param params
	 * @return Map<String,Object> 如果存在多条，返回第一条；如果未找到，返回null
	 */
	public Map<String, Object> getUnique(String sql, Object[] params);

	/**
	 * 返回单条数据
	 * @param sql
	 * @return Map<String,Object> 如果存在多条，返回第一条；如果未找到，返回null
	 */
	public Map<String, Object> getUnique(String sql);

	/**
	 * 返回单条数据
	 * @param sql
	 * @param params
	 * @param t
	 * @return Map<String,Object> 如果存在多条，返回第一条；如果未找到，返回null
	 */
	@SuppressWarnings("hiding")
	public <T> T getUnique(String sql, Object[] params, Class<T> t);

	/**
	 * 返回单条数据
	 * @param sql
	 * @param t
	 * @return Map<String,Object> 如果存在多条，返回第一条；如果未找到，返回null
	 */
	@SuppressWarnings("hiding")
	public <T> T getUnique(String sql, Class<T> t);

	/**
	 * 用自定义sql更新或删除
	 * @param sql
	 * @param params
	 * @return int 更新数量
	 */
	public int update(String sql, Object... params);

	/**
	 * 清除hibernate缓存对象
	 */
	public void evict(Object obj);
}
