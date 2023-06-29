package com.wcpdoc.exam.sys.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.wcpdoc.exam.core.dao.impl.BaseDaoImpl;
import com.wcpdoc.exam.core.entity.PageIn;
import com.wcpdoc.exam.core.entity.PageOut;
import com.wcpdoc.exam.core.util.SqlUtil;
import com.wcpdoc.exam.core.util.ValidateUtil;
import com.wcpdoc.exam.core.util.SqlUtil.Order;
import com.wcpdoc.exam.sys.dao.DictDao;
import com.wcpdoc.exam.sys.entity.Dict;

/**
 * 数据字典数据访问层实现
 * 
 * v1.0 zhanghc 2016-11-3下午9:03:40
 */
@Repository
public class DictDaoImpl extends BaseDaoImpl<Dict> implements DictDao {

	@Override
	public PageOut getListpage(PageIn pageIn) {
		String sql = "SELECT DICT.ID, DICT.DICT_INDEX, DICT.DICT_KEY, DICT.DICT_VALUE, DICT.NO "
				+ "FROM SYS_DICT DICT ";
		SqlUtil sqlUtil = new SqlUtil(sql);
		sqlUtil.addWhere(ValidateUtil.isValid(pageIn.getTwo()), "DICT.DICT_INDEX LIKE ?", "%" + pageIn.getTwo() + "%")
				.addWhere(ValidateUtil.isValid(pageIn.getThree()), "DICT.DICT_KEY LIKE ?", "%" + pageIn.getThree() + "%")
				.addWhere(ValidateUtil.isValid(pageIn.getFour()), "DICT.DICT_VALUE LIKE ?", "%" + pageIn.getFour() + "%")
				.addOrder("DICT.DICT_INDEX", Order.ASC)
				.addOrder("DICT.NO", Order.ASC);
		PageOut pageOut = getListpage(sqlUtil, pageIn);
		return pageOut;
	}

	@Override
	public List<Dict> getList() {
		String sql = "SELECT * FROM SYS_DICT ORDER BY DICT_INDEX ASC, NO ASC";
		return getList(sql, Dict.class);
	}
}