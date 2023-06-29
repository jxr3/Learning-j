package com.wcpdoc.exam.sys.controller;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wcpdoc.exam.core.controller.BaseController;
import com.wcpdoc.exam.core.entity.PageIn;
import com.wcpdoc.exam.core.entity.PageOut;
import com.wcpdoc.exam.core.entity.PageResult;
import com.wcpdoc.exam.sys.entity.Dict;
import com.wcpdoc.exam.sys.service.DictService;

/**
 * 数据字典控制层
 * 
 * v1.0 zhanghc 2016-11-3下午9:03:40
 */
@Controller
@RequestMapping("/dict")
public class DictController extends BaseController {
	private static final Logger log = LoggerFactory.getLogger(DictController.class);

	@Resource
	private DictService dictService;

	/**
	 * 到达数据字典列表页面
	 */
	@RequestMapping("/toList")
	public String toList() {
		try {
			return "sys/dict/dictList";
		} catch (Exception e) {
			log.error("到达数据字典列表页面错误：", e);
			return "sys/dict/dictList";
		}
	}

	/**
	 * 数据字典列表
	 */
	@RequestMapping("/list")
	@ResponseBody
	public PageOut list(PageIn pageIn) {
		try {
			return dictService.getListpage(pageIn);
		} catch (Exception e) {
			log.error("数据字典列表错误：", e);
			return new PageOut();
		}
	}

	/**
	 * 到达添加数据字典页面
	 */
	@RequestMapping("/toAdd")
	public String toAdd() {
		try {
			return "sys/dict/dictEdit";
		} catch (Exception e) {
			log.error("到达添加数据字典页面错误：", e);
			return "sys/dict/dictEdit";
		}
	}

	/**
	 * 完成添加数据字典
	 */
	@RequestMapping("/doAdd")
	@ResponseBody
	public PageResult doAdd(Dict dict) {
		try {
			dictService.addAndUpdate(dict);
			return new PageResult(true, "添加成功");
		} catch (Exception e) {
			log.error("完成添加数据字典错误：", e);
			return new PageResult(false, "添加失败：" + e.getMessage());
		}
	}

	/**
	 * 到达修改数据字典页面
	 */
	@RequestMapping("/toEdit")
	public String toEdit(Model model, Integer id) {
		try {
			Dict dict = dictService.getEntity(id);
			model.addAttribute("dict", dict);
			return "sys/dict/dictEdit";
		} catch (Exception e) {
			log.error("到达修改数据字典页面错误：", e);
			return "sys/dict/dictEdit";
		}
	}

	/**
	 * 完成修改数据字典
	 */
	@RequestMapping("/doEdit")
	@ResponseBody
	public PageResult doEdit(Dict dict) {
		try {
			dictService.updateAndUpdate(dict);
			return new PageResult(true, "修改成功");
		} catch (Exception e) {
			log.error("完成修改数据字典错误：", e);
			return new PageResult(false, "修改失败：" + e.getMessage());
		}
	}

	/**
	 * 完成删除数据字典
	 */
	@RequestMapping("/doDel")
	@ResponseBody
	public PageResult doDel(Integer[] ids) {
		try {
			dictService.delAndUpdate(ids);
			return new PageResult(true, "删除成功");
		} catch (Exception e) {
			log.error("完成删除数据字典错误：", e);
			return new PageResult(false, "删除失败：" + e.getMessage());
		}
	}
}
