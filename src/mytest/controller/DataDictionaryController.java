package mytest.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @desc 数据字典
 * @author liyu
 */
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import mytest.service.DataDictionaryService;

/**
 * 
 * @ClassName: DataDictionaryController
 * @Description: 数据字典管理控制器
 * @author liyu
 * @date 2017/3/23
 *
 */
public class DataDictionaryController extends Controller {

	/******************************* 职位管理 *******************************/
	// 职位列表
	/**
	 * @author liyu
	 * @date 2017/3/23
	 * @param void
	 * @return void
	 */
	public void position_list() {
		// 验证权限
		Record user = getSessionAttr("user");
		Integer rid = user.getInt("role_id");
		String mopids = Db.queryStr("select module_power_id from t_role_permissions where role_id = ?", rid);
		if (mopids.indexOf("110") != -1) {
			setAttr("_add", true);
		}
		if (mopids.indexOf("111") != -1) {
			setAttr("_delete", true);
		}
		if (mopids.indexOf("112") != -1) {
			setAttr("_edit", true);
		}
		
		Integer pageno = getParaToInt() == null ? 1 : getParaToInt();
		Page<Record> page = DataDictionaryService.getPositionList(pageno, 16);

		setAttr("pageno", page.getPageNumber());
		setAttr("totalpage", page.getTotalPage());
		setAttr("totalrow", page.getTotalRow());
		setAttr("positionlist", page.getList());

		render("position_list.html");
	}

	// 获得单条记录
	/**
	 * @author liyu
	 * @date 2017/3/23
	 * @param void
	 * @return void
	 */
	public void getPosition() {
		Integer id = getParaToInt();
		if (null != id) {
			setAttr("position", Db.findById("t_position", id));
		}
		render("position_detail.html");
	}

	// 保存数据
	/**
	 * @author liyu
	 * @date 2017/3/23
	 * @param void
	 * @return void
	 */
	public void savePosition() {
		Integer id = getParaToInt("id");
		String positionname = getPara("positionname");
		boolean result = DataDictionaryService.savePosition(id, positionname);

		renderJson("result", result);
	}

	// 删除数据
	/**
	 * @author liyu
	 * @date 2017/3/23
	 * @param void
	 * @return void
	 */
	public void delPosition() {
		Integer id = getParaToInt();
		boolean result = Db.deleteById("t_position", id);
		renderJson(result);
	}

	/******************************* 部门管理 *******************************/
	// 部门列表
	/**
	 * @author liyu
	 * @date 2017/3/23
	 * @param void
	 * @return void
	 */
	public void department_list() {
		// 验证权限
		Record user = getSessionAttr("user");
		Integer rid = user.getInt("role_id");
		String mopids = Db.queryStr("select module_power_id from t_role_permissions where role_id = ?", rid);
		if (mopids.indexOf("113") != -1) {
			setAttr("_add", true);
		}
		if (mopids.indexOf("114") != -1) {
			setAttr("_delete", true);
		}
		if (mopids.indexOf("115") != -1) {
			setAttr("_edit", true);
		}

		Integer pageno = getParaToInt() == null ? 1 : getParaToInt();

		Page<Record> page = DataDictionaryService.getDepartmentList(pageno, 16);

		setAttr("pageno", page.getPageNumber());
		setAttr("totalpage", page.getTotalPage());
		setAttr("totalrow", page.getTotalRow());
		setAttr("departmentlist", page.getList());

		render("department_list.html");
	}

	// 获得单条记录
	/**
	 * @author liyu
	 * @date 2017/3/23
	 * @param void
	 * @return void
	 */
	public void getDepartment() {
		Integer id = getParaToInt();
		if (null != id) {
			setAttr("department", Db.findById("t_department", id));
		}
		render("department_detail.html");
	}

	// 保存数据
	/**
	 * @author liyu
	 * @date 2017/3/23
	 * @param void
	 * @return void
	 */
	public void saveDepartment() {
		Integer id = getParaToInt("id");
		String departmentname = getPara("departmentname");
		String description = getPara("description");
		boolean result = DataDictionaryService.saveDepartment(id, departmentname, description);

		renderJson("result", result);
	}

	// 删除数据
	/**
	 * @author liyu
	 * @date 2017/3/23
	 * @param void
	 * @return void
	 */
	public void delDepartment() {
		Integer id = getParaToInt();
		boolean result = Db.deleteById("t_department", id);
		renderJson(result);
	}

	/******************************* 大楼编号管理 *******************************/
	// 大楼编号列表
	/**
	 * @author liyu
	 * @date 2017/3/23
	 * @param void
	 * @return void
	 */
	public void building_no_list() {
		// 验证权限
		Record user = getSessionAttr("user");
		Integer rid = user.getInt("role_id");
		String mopids = Db.queryStr("select module_power_id from t_role_permissions where role_id = ?", rid);
		if (mopids.indexOf("116") != -1) {
			setAttr("_add", true);
		}
		if (mopids.indexOf("117") != -1) {
			setAttr("_delete", true);
		}
		if (mopids.indexOf("118") != -1) {
			setAttr("_edit", true);
		}

		Integer pageno = getParaToInt() == null ? 1 : getParaToInt();
		Integer pagesize = 16;

		Page<Record> page = DataDictionaryService.getBuildingNoList(pageno, pagesize);

		setAttr("pageno", page.getPageNumber());
		setAttr("totalpage", page.getTotalPage());
		setAttr("totalrow", page.getTotalRow());
		setAttr("buildingnolist", page.getList());

		render("building_no_list.html");
	}

	// 获得单条记录
	/**
	 * @author liyu
	 * @date 2017/3/23
	 * @param void
	 * @return void
	 */
	public void getBuildingNo() {
		Integer id = getParaToInt();
		if (null != id) {
			setAttr("buildingno", Db.findById("t_building_number", id));
		}
		render("building_no_detail.html");
	}

	// 保存数据
	/**
	 * @author liyu
	 * @date 2017/3/23
	 * @param void
	 * @return void
	 */
	public void saveBuildingNo() {
		Integer id = getParaToInt("id");
		String buildingNo = getPara("buildingno");
		Integer sortId = getParaToInt("sortid");
		boolean result = DataDictionaryService.saveBuildingNo(id, buildingNo, sortId);
		renderJson("result", result);
	}

	// 删除数据
	/**
	 * @author liyu
	 * @date 2017/3/23
	 * @param void
	 * @return void
	 */
	public void delBuildingNo() {
		Integer id = getParaToInt();
		boolean result = Db.deleteById("t_building_number", id);
		renderJson(result);
	}

	/******************************* 大楼性质管理 *******************************/
	// 大楼性质列表
	/**
	 * @author liyu
	 * @date 2017/3/23
	 * @param void
	 * @return void
	 */
	public void building_nature_list() {
		// 验证权限
		Record user = getSessionAttr("user");
		Integer rid = user.getInt("role_id");
		String mopids = Db.queryStr("select module_power_id from t_role_permissions where role_id = ?", rid);
		if (mopids.indexOf("119") != -1) {
			setAttr("_add", true);
		}
		if (mopids.indexOf("120") != -1) {
			setAttr("_delete", true);
		}
		if (mopids.indexOf("121") != -1) {
			setAttr("_edit", true);
		}

		Integer pageno = getParaToInt() == null ? 1 : getParaToInt();
		Integer pagesize = 16;

		Page<Record> page = DataDictionaryService.getBuildingNatureList(pageno, pagesize);

		setAttr("pageno", page.getPageNumber());
		setAttr("totalpage", page.getTotalPage());
		setAttr("totalrow", page.getTotalRow());
		setAttr("buildingnaturelist", page.getList());

		render("building_nature_list.html");
	}

	// 获得单条记录
	/**
	 * @author liyu
	 * @date 2017/3/23
	 * @param void
	 * @return void
	 */
	public void getBuildingNature() {
		Integer id = getParaToInt();
		if (null != id) {
			setAttr("buildingnature", Db.findById("t_building_nature", id));
		}
		render("building_nature_detail.html");
	}

	// 保存数据
	/**
	 * @author liyu
	 * @date 2017/3/23
	 * @param void
	 * @return void
	 */
	public void saveBuildingNature() {
		Integer id = getParaToInt("id");
		String name = getPara("name");
		Integer sortId = getParaToInt("sortid");
		boolean result = DataDictionaryService.saveBuildingNature(id, name, sortId);

		renderJson("result", result);
	}

	// 删除数据
	/**
	 * @author liyu
	 * @date 2017/3/23
	 * @param void
	 * @return void
	 */
	public void delBuildingNature() {
		Integer id = getParaToInt();
		boolean result = Db.deleteById("t_building_nature", id);
		renderJson(result);
	}

	/******************************* 行业代码父级管理 *******************************/
	// 行业代码父级列表
	/**
	 * @author liyu
	 * @date 2017/3/23
	 * @param void
	 * @return void
	 */
	public void superior_industry_list() {
		// 验证权限
		Record user = getSessionAttr("user");
		Integer rid = user.getInt("role_id");
		String mopids = Db.queryStr("select module_power_id from t_role_permissions where role_id = ?", rid);
		if (mopids.indexOf("122") != -1) {
			setAttr("_add", true);
		}
		if (mopids.indexOf("123") != -1) {
			setAttr("_delete", true);
		}
		if (mopids.indexOf("124") != -1) {
			setAttr("_edit", true);
		}

		Integer pageno = getParaToInt() == null ? 1 : getParaToInt();
		Integer pagesize = 16;

		Page<Record> page = DataDictionaryService.getSuperiorIndustryList(pageno, pagesize);

		setAttr("pageno", page.getPageNumber());
		setAttr("totalpage", page.getTotalPage());
		setAttr("totalrow", page.getTotalRow());
		setAttr("superiorindustrylist", page.getList());

		render("superior_industry_list.html");
	}

	// 获得单条记录
	/**
	 * @author liyu
	 * @date 2017/3/23
	 * @param void
	 * @return void
	 */
	public void getSuperiorIndustry() {
		Integer id = getParaToInt();
		if (null != id) {
			setAttr("superiorindustry", Db.findById("t_superior_industry", id));
		}
		render("superior_industry_detail.html");
	}

	// 保存数据
	/**
	 * @author liyu
	 * @date 2017/3/23
	 * @param void
	 * @return void
	 */
	public void saveSuperiorIndustry() {
		Integer id = getParaToInt("id");
		String industryCode = getPara("industrycode");
		String industryName = getPara("industryname");
		boolean result = DataDictionaryService.saveSuperiorIndustry(id, industryCode, industryName);

		renderJson("result", result);
	}

	// 删除数据
	/**
	 * @author liyu
	 * @date 2017/3/23
	 * @param void
	 * @return void
	 */
	public void delSuperiorIndustry() {
		Integer id = getParaToInt();
		boolean result = Db.deleteById("t_superior_industry", id);
		renderJson(result);
	}

	/******************************* 行业代码子级管理 *******************************/
	// 行业代码子级列表
	/**
	 * @author liyu
	 * @date 2017/3/23
	 * @param void
	 * @return void
	 */
	public void sub_industry_list() {
		// 验证权限
		Record user = getSessionAttr("user");
		Integer rid = user.getInt("role_id");
		String mopids = Db.queryStr("select module_power_id from t_role_permissions where role_id = ?", rid);
		if (mopids.indexOf("125") != -1) {
			setAttr("_add", true);
		}
		if (mopids.indexOf("126") != -1) {
			setAttr("_delete", true);
		}
		if (mopids.indexOf("127") != -1) {
			setAttr("_edit", true);
		}

		Integer pageno = getParaToInt() == null ? 1 : getParaToInt();
		Integer pagesize = 16;

		Page<Record> page = DataDictionaryService.getSubIndustryList(pageno, pagesize);

		setAttr("pageno", page.getPageNumber());
		setAttr("totalpage", page.getTotalPage());
		setAttr("totalrow", page.getTotalRow());
		setAttr("subindustrylist", page.getList());

		render("sub_industry_list.html");
	}

	// 获得单条记录
	/**
	 * @author liyu
	 * @date 2017/3/23
	 * @param void
	 * @return void
	 */
	public void getSubIndustry() {
		Integer id = getParaToInt();
		if (null != id) {
			setAttr("subindustry", Db.findById("t_sub_industry", id));
		}

		// 获得父级行业列表
		List<Record> superiorindustrylist = DataDictionaryService.getSuperiorIndustryList();
		setAttr("superiorindustrylist", superiorindustrylist);

		render("sub_industry_detail.html");
	}

	// 通过父级行业ID，获得其子级行业列表，以Json格式返回
	/**
	 * @author liyu
	 * @date 2017/3/23
	 * @param void
	 * @return void
	 */
	public void getIndustryBySubId() {
		Integer subId = getParaToInt("subindustryid");// 父级行业ID
		List<Record> industrylist = DataDictionaryService.getIndustryBySubId(subId);

		renderJson(industrylist);
	}

	// 保存数据
	/**
	 * @author liyu
	 * @date 2017/3/23
	 * @param void
	 * @return void
	 */
	public void saveSubIndustry() {
		Integer id = getParaToInt("id");
		String industrycode = getPara("industrycode");
		String industryname = getPara("industryname");
		int superiorindustryid = getParaToInt("superiorindustryid");
		boolean result = DataDictionaryService.saveSubIndustry(id, industrycode, industryname, superiorindustryid);

		renderJson("result", result);
	}

	// 删除数据
	/**
	 * @author liyu
	 * @date 2017/3/23
	 * @param void
	 * @return void
	 */
	public void delSubIndustry() {
		Integer id = getParaToInt();
		boolean result = Db.deleteById("t_sub_industry", id);
		renderJson(result);
	}

	/******************************* 行业代码管理 *******************************/
	// 行业代码列表
	/**
	 * @author liyu
	 * @date 2017/3/23
	 * @param void
	 * @return void
	 */
	public void industry_code_list() {
		// 验证权限
		Record user = getSessionAttr("user");
		Integer rid = user.getInt("role_id");
		String mopids = Db.queryStr("select module_power_id from t_role_permissions where role_id = ?", rid);
		if (mopids.indexOf("128") != -1) {
			setAttr("_add", true);
		}
		if (mopids.indexOf("129") != -1) {
			setAttr("_delete", true);
		}
		if (mopids.indexOf("130") != -1) {
			setAttr("_edit", true);
		}

		Integer pageno = getParaToInt() == null ? 1 : getParaToInt();
		Integer pagesize = 16;

		Page<Record> page = DataDictionaryService.getIndustryCodeList(pageno, pagesize);

		setAttr("pageno", page.getPageNumber());
		setAttr("totalpage", page.getTotalPage());
		setAttr("totalrow", page.getTotalRow());
		setAttr("industrycodelist", page.getList());

		render("industry_code_list.html");
	}

	// 获得单条记录
	/**
	 * @author liyu
	 * @date 2017/3/23
	 * @param void
	 * @return void
	 */
	public void getIndustryCode() {
		Integer id = getParaToInt();
		List<Record> subindustrylist = DataDictionaryService.getSubIndustryList();

		if (null != id) {
			setAttr("industrycode", Db.findById("t_industry_code", id));// 父级行业代码
		}

		// 获得父级行业列表
		List<Record> superiorindustrylist = DataDictionaryService.getSuperiorIndustryList();

		setAttr("superiorindustrylist", superiorindustrylist);// 父级行业列表
		setAttr("subindustrylist", subindustrylist);// 子级行业列表

		render("industry_code_detail.html");
	}

	// 通过父级行业ID，获得其子级行业列表，以Json格式返回
	/**
	 * @author liyu
	 * @date 2017/3/23
	 * @param void
	 * @return void
	 */
	public void getSubIndustryBySuperId() {
		Integer superId = getParaToInt("superiorindustryid");// 父级行业ID
		List<Record> subindustrylist = DataDictionaryService.getSubIndustryBySuperId(superId);

		renderJson(subindustrylist);
	}

	// 保存数据
	/**
	 * @author liyu
	 * @date 2017/3/23
	 * @param void
	 * @return void
	 */
	public void saveIndustryCode() {
		// 获取父级行业名称
		Integer superiorId = getParaToInt("superiorindustryid");
		Record superiorIndustry = Db.findById("t_superior_industry", superiorId);
		String superiorIndustryName = superiorIndustry.getStr("industry_name");

		// 获取子级行业名称
		Integer subId = getParaToInt("subindustryid");
		Record subIndustry = Db.findById("t_sub_industry", subId);
		String subIndustryName = subIndustry.getStr("sub_industry_name");

		Record record = new Record();
		record.set("id", getParaToInt("id"));
		record.set("industry_code", getPara("industrycode"));
		record.set("industry_name", getPara("industryname"));
		record.set("superior_industry", superiorIndustryName);
		record.set("sub_industry", subIndustryName);
		record.set("modify_time", new Date());

		boolean result = DataDictionaryService.saveIndustryCode(record);

		renderJson("result", result);
	}

	// 删除数据
	/**
	 * @author liyu
	 * @date 2017/3/23
	 * @param void
	 * @return void
	 */
	public void delIndustryCode() {
		Integer id = getParaToInt();
		boolean result = Db.deleteById("t_industry_code", id);
		renderJson(result);
	}

}
