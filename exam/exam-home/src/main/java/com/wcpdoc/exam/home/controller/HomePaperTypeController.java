package com.wcpdoc.exam.home.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
import com.wcpdoc.exam.core.entity.PaperType;
import com.wcpdoc.exam.core.service.PaperTypeService;
import com.wcpdoc.exam.core.util.ValidateUtil;
import com.wcpdoc.exam.sys.entity.User;

/**
 * 试卷分类控制层
 */
@Controller
@RequestMapping("/home/paperType")
public class HomePaperTypeController extends BaseController{
	private static final Logger log = LoggerFactory.getLogger(HomePaperTypeController.class);
	
	@Resource
	private PaperTypeService paperTypeService;
	
	/**
	 * 到达试卷分类列表页面
	 */
	@RequestMapping("/toList")
	public String toList() {
		try {
			return "home/paperType/paperTypeList";
		} catch (Exception e) {
			log.error("到达试卷分类列表页面错误：", e);
			return "home/paperType/paperTypeList";
		}
	}
	
	/**
	 * 获取试卷分类树
	 */
	@RequestMapping("/treeList")
	@ResponseBody
	public List<Map<String, Object>> treeList() {
		try {
			return paperTypeService.getTreeList();
		} catch (Exception e) {
			log.error("获取试卷分类树错误：", e);
			return new ArrayList<Map<String,Object>>();
		}
	}
	
	/**
	 * 试卷分类列表
	 */
	@RequestMapping("/list")
	@ResponseBody
	public PageOut list(PageIn pageIn) {
		try {
			return paperTypeService.getListpage(pageIn);
		} catch (Exception e) {
			log.error("试卷分类列表错误：", e);
			return new PageOut();
		}
	}
	
	/**
	 * 到达添加试卷分类页面
	 */
	@RequestMapping("/toAdd")
	public String toAdd(Model model, Integer parentId) {
		try {
			model.addAttribute("parent", paperTypeService.getEntity(parentId));
			return "home/paperType/paperTypeEdit";
		} catch (Exception e) {
			log.error("到达添加试卷分类页面错误", e);
			return "home/paperType/paperTypeEdit";
		}
	}
	
	/**
	 * 完成添加试卷分类
	 */
	@RequestMapping("/doAdd")
	@ResponseBody
	public PageResult doAdd(PaperType paperType) {
		try {
			paperType.setUpdateUserId(getCurUser().getId());
			paperType.setUpdateTime(new Date());
			paperType.setState(1);
			paperTypeService.addAndUpdate(paperType);
			return new PageResult(true, "添加成功");
		} catch (Exception e) {
			log.error("完成添加试卷分类错误：", e);
			return new PageResult(false, "添加失败：" + e.getMessage());
		}
	}
	
	/**
	 * 到达修改试卷分类页面
	 */
	@RequestMapping("/toEdit")
	public String toEdit(Model model, Integer id) {
		try {
			PaperType paperType = paperTypeService.getEntity(id);
			model.addAttribute("paperType", paperType);
			PaperType parent = paperTypeService.getEntity(paperType.getParentId());
			if(parent != null){
				model.addAttribute("parent", paperTypeService.getEntity(paperType.getParentId()));
			}
			return "home/paperType/paperTypeEdit";
		} catch (Exception e) {
			log.error("到达修改试卷分类页面错误", e);
			return "home/paperType/paperTypeEdit";
		}
	}
	
	/**
	 * 完成修改试卷分类
	 */
	@RequestMapping("/doEdit")
	@ResponseBody
	public PageResult doEdit(PaperType paperType) {
		try {
			PaperType entity = paperTypeService.getEntity(paperType.getId());
			entity.setName(paperType.getName());
			entity.setUpdateTime(new Date());
			entity.setUpdateUserId(((User)getCurUser()).getId());
			entity.setNo(paperType.getNo());
			paperTypeService.editAndUpdate(entity);
			return new PageResult(true, "修改成功");
		} catch (Exception e) {
			log.error("完成修改试卷分类错误：", e);
			return new PageResult(false, "修改失败：" + e.getMessage());
		}
	}
	
	/**
	 * 完成删除试卷分类
	 */
	@RequestMapping("/doDel")
	@ResponseBody
	public PageResult doDel(Integer[] ids) {
		try {
			paperTypeService.delAndUpdate(ids);
			return new PageResult(true, "删除成功");
		} catch (Exception e) {
			log.error("完成删除试卷分类错误：", e);
			return new PageResult(false, "删除失败：" + e.getMessage());
		}
	}

	/**
	 * 到达权限列表页面
	 */
	@RequestMapping("/toAuth")
	public String toAuth(Model model, Integer id) {
		try {
			model.addAttribute("id", id);
			return "home/paperType/paperTypeAuthList";
		} catch (Exception e) {
			log.error("到达权限列表页面错误：", e);
			return "home/paperType/paperTypeAuthList";
		}
	}
	
	/**
	 * 获取组织机构树
	 */
	@RequestMapping("/authUserOrgTreeList")
	@ResponseBody
	public List<Map<String, Object>> authUserOrgTreeList() {
		try {
			return paperTypeService.getOrgTreeList();
		} catch (Exception e) {
			log.error("获取组织机构树错误：", e);
			return new ArrayList<Map<String,Object>>();
		}
	}
	
	/**
	 * 权限用户列表
	 */
	@RequestMapping("/authUserList")
	@ResponseBody
	public PageOut authUserList(PageIn pageIn) {
		try {
			return paperTypeService.getAuthUserListpage(pageIn);
		} catch (Exception e) {
			log.error("权限用户列表错误：", e);
			return new PageOut();
		}
	}
	
	/**
	 * 到达添加权限用户列表页面
	 */
	@RequestMapping("/toAuthUserAddList")
	public String toAuthUserAddList(Model model, Integer id) {
		try {
			model.addAttribute("id", id);
			return "home/paperType/paperTypeAuthUserAddList";
		} catch (Exception e) {
			log.error("到达添加权限用户列表页面错误：", e);
			return "home/paperType/paperTypeAuthUserAddList";
		}
	}
	
	/**
	 * 权限用户添加列表
	 */
	@RequestMapping("/authUserAddList")
	@ResponseBody
	public PageOut authUserAddList(PageIn pageIn) {
		try {
			return paperTypeService.getAuthUserAddList(pageIn);
		} catch (Exception e) {
			log.error("权限用户添加列表错误：", e);
			return new PageOut();
		}
	}
	
	/**
	 * 完成添加权限用户
	 */
	@RequestMapping("/doAuthUserAdd")
	@ResponseBody
	public PageResult doAuthUserAdd(Integer id, Integer[] userIds, boolean syn2Sub) {
		try {
			paperTypeService.doAuthUserAdd(id, userIds, syn2Sub, getCurUser());
			return new PageResult(true, "添加成功");
		} catch (Exception e) {
			log.error("完成添加权限用户错误：", e);
			return new PageResult(false, "添加失败：" + e.getMessage());
		}
	}
	
	/**
	 * 完成删除权限用户
	 */
	@RequestMapping("/doAuthUserDel")
	@ResponseBody
	public PageResult doAuthUserDel(Integer id, Integer[] userIds, boolean syn2Sub) {
		try {
			paperTypeService.doAuthUserDel(id, userIds, syn2Sub, getCurUser());
			return new PageResult(true, "删除成功");
		} catch (Exception e) {
			log.error("完成删除权限用户错误：", e);
			return new PageResult(false, "删除失败：" + e.getMessage());
		}
	}
	
	/**
	 * 完成添加权限机构
	 */
	@RequestMapping("/doAuthOrgUpdate")
	@ResponseBody
	public PageResult doAuthOrgUpdate(Integer id, Integer[] orgIds, boolean syn2Sub) {
		try {
			paperTypeService.doAuthOrgUpdate(id, orgIds, syn2Sub, getCurUser());
			return new PageResult(true, "添加成功");
		} catch (Exception e) {
			log.error("完成添加权限机构错误：", e);
			return new PageResult(false, "添加成功：" + e.getMessage());
		}
	}
	
	/**
	 * 获取组织机构树
	 */
	@RequestMapping("/authOrgOrgTreeList")
	@ResponseBody
	public List<Map<String, Object>> authOrgOrgTreeList(Integer id) {
		try {
			List<Map<String, Object>> orgTreeList = paperTypeService.getOrgTreeList();
			PaperType paperType = paperTypeService.getEntity(id);
			if(paperType == null){
				return orgTreeList;
			}
			
			String orgIds = paperType.getOrgIds();
			if(!ValidateUtil.isValid(orgIds)){
				return orgTreeList;
			}
			
			for(Map<String, Object> map : orgTreeList){
				String orgId = map.get("ID").toString();
				if(orgIds.contains("," + orgId + ",")){
					map.put("CHECKED", true);
				}
			}
			return orgTreeList;
		} catch (Exception e) {
			log.error("获取组织机构树错误：", e);
			return new ArrayList<Map<String,Object>>();
		}
	}
	
	/**
	 * 获取机构岗位树
	 */
	@RequestMapping("/authPostOrgTreeList")
	@ResponseBody
	public List<Map<String, Object>> authPostOrgTreeList(Integer id) {
		try {
			List<Map<String, Object>> orgPostTree = paperTypeService.getOrgPostTreeList();
			PaperType paperType = paperTypeService.getEntity(id);
			if(paperType == null){
				return orgPostTree;
			}
			
			String postIds = paperType.getPostIds();
			if(!ValidateUtil.isValid(postIds)){
				return orgPostTree;
			}
			
			for(Map<String, Object> map : orgPostTree){
				String type = map.get("TYPE").toString();
				if(!"POST".equals(type)){
					continue;
				}
				
				String postId = map.get("ID").toString();
				if(postIds.contains("," + postId + ",")){
					map.put("CHECKED", true);
				}
			}
			
			return orgPostTree;
		} catch (Exception e) {
			log.error("获取机构岗位树错误：", e);
			return new ArrayList<Map<String,Object>>();
		}
	}
	
	/**
	 * 完成添加权限岗位
	 */
	@RequestMapping("/doAuthPostUpdate")
	@ResponseBody
	public PageResult doAuthPostUpdate(Integer id, Integer[] postIds, boolean syn2Sub) {
		try {
			paperTypeService.doAuthPostUpdate(id, postIds, syn2Sub, getCurUser());
			return new PageResult(true, "添加成功");
		} catch (Exception e) {
			log.error("完成添加权限岗位错误：", e);
			return new PageResult(false, "添加成功：" + e.getMessage());
		}
	}
}
