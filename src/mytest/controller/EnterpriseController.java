package mytest.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import mytest.service.DataDictionaryService;
import mytest.service.EnterpriseService;

/**
 * @desc 企业管理
 * @author liyu
 */

public class EnterpriseController extends Controller {

	/******************************* 入驻企业管理 *******************************/
	// 入驻企业列表
	/**
	 * @author liyu
	 * @date 2017/03/23
	 */
	public void in_list() {
		// 验证权限
//		Record admin = getSessionAttr("admin");
//		Integer rid = admin.getInt("role_id");
//		String mopids = Db.queryStr("select module_power_id from t_role_permissions where role_id = ?", rid);
			setAttr("_add", true);
			setAttr("_delete", true);
			setAttr("_edit", true);
			setAttr("_search", 1);
			setAttr("_retreat", true);

		Integer pagesize = 16;
		Integer pageno = getParaToInt("pageno") == null ? 1 : getParaToInt("pageno");
		String enterprisename = getPara("enterprisename") == null ? "" : getPara("enterprisename").trim();

		Page<Record> page = EnterpriseService.getEnterpriseInListByPage(pageno, pagesize, enterprisename);

		setAttr("enterprisename", enterprisename);
		setAttr("pageno", page.getPageNumber());
		setAttr("totalpage", page.getTotalPage());
		setAttr("totalrow", page.getTotalRow());
		setAttr("enterpriseinlist", page.getList());

		render("in_list.html");
	}

	// 获得单条记录
	/**
	 * @author liyu
	 * @date 2017/03/23
	 */
	public void getEnterpriseIn() {
		// 企业ID
		Integer id = getParaToInt();

		// 获得父级行业列表
		List<Record> superiorindustrylist = DataDictionaryService.getSuperiorIndustryList();
		setAttr("superiorindustrylist", superiorindustrylist);

		// 获得企业类型（大楼性质）
		List<Record> typelist = Db.find(" SELECT id,name FROM t_building_nature ");
		setAttr("typelist", typelist);

		if (null != id) {
			// 查询相应id的企业，并设置父级行业、子级行业、行业id
			Record enterprisein = EnterpriseService.getIndustryIds(id).get(0);
			setAttr("enterprisein", enterprisein);
			// 相应父级行业下的子级行业列表
			Integer superId = enterprisein.getInt("superior_industry_id");
			setAttr("subindustrylist", DataDictionaryService.getSubIndustryBySuperId(superId));
			// 相应子级行业下的行业列表
			Integer subId = enterprisein.getInt("sub_industry_id");
			setAttr("industrycodelist", DataDictionaryService.getIndustryBySubId(subId));
		}

		render("in_detail.html");
	}

	// 保存数据
	/**
	 * @author liyu
	 * @date 2017/03/23
	 */
	public void saveEnterpriseIn() {

		Record record = new Record();
		record.set("id", getParaToInt("id"));
		record.set("enterprise_name", getPara("enterprisename"));
		record.set("industry", getParaToInt("industrycode"));
		record.set("organizational_code", getPara("organizationalcode"));
		record.set("registration_number", getPara("registrationnumber"));
		record.set("representative", getPara("representative"));
		record.set("contact", getPara("contact"));
		record.set("registered_address", getPara("registeredaddress"));
		record.set("registration_type", getParaToInt("registrationtype"));
		record.set("registration_time", getPara("registrationtime"));
		record.set("registered_capital", getPara("registeredcapital"));
		record.set("in_time", getPara("intime"));
		record.set("is_software", getParaToInt("issoftware"));
		record.set("room_number", getPara("roomnumber"));
		record.set("office_space", getParaToInt("officespace"));
		record.set("unit_price", getPara("unitprice"));
		record.set("enterprise_type", getParaToInt("enterprisetype"));
		record.set("is_retreat", false);
		record.set("modify_time", new Date());

		boolean result = EnterpriseService.saveEnterprise(record);

		renderJson("result", result);
	}

	// 删除数据
	/**
	 * @author liyu
	 * @date 2017/03/23
	 */
	public void delEnterpriseIn() {
		Integer id = getParaToInt();
		boolean result = Db.deleteById("t_enterprise_in", id);
		renderJson(result);
	}

	// 企业离驻
	/**
	 * @author liyu
	 * @date 2017/03/23
	 */
	public void enterpriseRetreat() {
		Record record = new Record();
		record.set("id", getParaToInt("id"));
		record.set("retreat_reason", getPara("retreatreason"));
		record.set("is_retreat", true);
		record.set("retreat_time", new Date());
		record.set("modify_time", new Date());
		boolean result = EnterpriseService.saveEnterprise(record);
		/*
		 * //企业离驻后，改变区域管理，区域状态为空，公司为空； String company_name =
		 * ParkManageService.getEnterperiseById(getParaToInt("id")).getStr(
		 * "enterprise_name"); boolean is_retreat =
		 * ParkManageService.getEnterperiseById(getParaToInt("id")).getBoolean(
		 * "is_retreat"); if(is_retreat == true){ Record rec =
		 * Db.findFirst("select * from t_area where the_company = '"
		 * +company_name+"' ").set("status", false).set("the_company", null);
		 * Db.update("t_area",rec); }
		 */

		renderJson("result", result);
	}

	/******************************* 离驻企业管理 *******************************/
	// 离驻企业列表
	/**
	 * @author liyu
	 * @date 2017/03/23
	 */
	public void retreat_list() {
		// 验证权限
		Record admin = getSessionAttr("admin");
		Integer rid = admin.getInt("role_id");
		String mopids = Db.queryStr("select module_power_id from t_role_permissions where role_id = ?", rid);
		if (mopids.indexOf("163") != -1) {
			setAttr("_search", true);
		}
		if (mopids.indexOf("164") != -1) {
			setAttr("_detail", true);
		}

		Integer pagesize = 16;
		Integer pageno = getParaToInt() == null ? 1 : getParaToInt();
		String start = getPara("start") == null ? "" : getPara("start");
		String end = getPara("end") == null ? "" : getPara("end");
		String enterprisename = getPara("enterprisename") == null ? "" : getPara("enterprisename").trim();

		Page<Record> page = EnterpriseService.getEnterpriseRetreatList(pageno, pagesize, enterprisename, start, end);

		setAttr("start", start);
		setAttr("end", end);
		setAttr("enterprisename", enterprisename);
		setAttr("pageno", page.getPageNumber());
		setAttr("totalpage", page.getTotalPage());
		setAttr("totalrow", page.getTotalRow());
		setAttr("enterpriseretreatlist", page.getList());

		render("retreat_list.html");
	}

	// 获得单条记录
	/**
	 * @author liyu
	 * @date 2017/03/23
	 */
	public void getEnterpriseRetreat() {
		Integer id = getParaToInt();
		if (null != id) {
			setAttr("enterpriseretreat", Db.findById("t_enterprise_in", id));
		}

		render("retreat_detail.html");
	}

	/******************************* 企业经济情况管理 *******************************/
	// 企业经济情况列表
	/**
	 * @author liyu
	 * @date 2017/03/23
	 */
	public void economy_list() {
		// 验证权限
		Record admin = getSessionAttr("admin");
		Integer rid = admin.getInt("role_id");
		String mopids = Db.queryStr("select module_power_id from t_role_permissions where role_id = ?", rid);
		if (mopids.indexOf("165") != -1) {
			setAttr("_add", true);
		}
		if (mopids.indexOf("166") != -1) {
			setAttr("_delete", true);
		}
		if (mopids.indexOf("167") != -1) {
			setAttr("_edit", true);
		}
		if (mopids.indexOf("168") != -1) {
			setAttr("_search", true);
		}

		Integer pagesize = 16;
		Integer pageno = getParaToInt("pageno") == null ? 1 : getParaToInt("pageno");
		String enterprisename = getPara("enterprisename") == null ? "" : getPara("enterprisename").trim();

		Page<Record> page = EnterpriseService.getEconomyList(pageno, pagesize, enterprisename);

		setAttr("enterprisename", enterprisename);
		setAttr("pageno", page.getPageNumber());
		setAttr("totalpage", page.getTotalPage());
		setAttr("totalrow", page.getTotalRow());
		setAttr("economylist", page.getList());

		render("economy_list.html");
	}

	// 获得单条记录
	/**
	 * @author liyu
	 * @date 2017/03/23
	 */
	public void getEconomy() {
		Integer id = getParaToInt();
		if (null != id) {
			setAttr("economy", Db.findById("t_enterprise_economy", id));
		}

		// 查询入驻企业列表
		List<Record> enterpriseinlist = EnterpriseService.getEnterpriseInList();
		setAttr("enterpriseinlist", enterpriseinlist);

		render("economy_detail.html");
	}

	// 保存数据
	/**
	 * @author liyu
	 * @date 2017/03/23
	 */
	public void saveEconomy() {
		// 通过ID查询企业，获得企业名称
		Integer companyid = getParaToInt("enterpriseid");
		Record company = Db.findById("t_enterprise_in", companyid);
		String companyname = company.get("enterprise_name");

		Record record = new Record();
		record.set("id", getParaToInt("id"));
		record.set("company_id", getParaToInt("enterpriseid"));
		record.set("company_name", companyname);
		record.set("the_date", getPara("thedate"));
		record.set("income", getPara("income"));
		record.set("net_profit", getPara("netprofit"));
		record.set("taxation", getPara("taxation"));
		record.set("investment", getPara("investment"));
		record.set("modify_time", new Date());

		boolean result = EnterpriseService.saveEconomy(record);

		renderJson("result", result);
	}

	// 删除数据
	/**
	 * @author liyu
	 * @date 2017/03/23
	 */
	public void delEconomy() {
		Integer id = getParaToInt();
		boolean result = Db.deleteById("t_enterprise_economy", id);
		renderJson(result);
	}

	/******************************* 企业从业人员管理 *******************************/
	// 企业从业人员列表
	/**
	 * @author liyu
	 * @date 2017/03/23
	 */
	public void practitioners_list() {
		// 验证权限
		Record admin = getSessionAttr("admin");
		Integer rid = admin.getInt("role_id");
		String mopids = Db.queryStr("select module_power_id from t_role_permissions where role_id = ?", rid);
		if (mopids.indexOf("169") != -1) {
			setAttr("_add", true);
		}
		if (mopids.indexOf("170") != -1) {
			setAttr("_delete", true);
		}
		if (mopids.indexOf("171") != -1) {
			setAttr("_edit", true);
		}
		if (mopids.indexOf("172") != -1) {
			setAttr("_search", true);
		}

		Integer pagesize = 16;
		Integer pageno = getParaToInt("pageno") == null ? 1 : getParaToInt("pageno");
		String enterprisename = getPara("enterprisename") == null ? "" : getPara("enterprisename").trim();

		Page<Record> page = EnterpriseService.getPractitionersList(pageno, pagesize, enterprisename);

		setAttr("enterprisename", enterprisename);
		setAttr("pageno", page.getPageNumber());
		setAttr("totalpage", page.getTotalPage());
		setAttr("totalrow", page.getTotalRow());
		setAttr("practitionerslist", page.getList());

		render("practitioners_list.html");
	}

	// 获得单条记录
	/**
	 * @author liyu
	 * @date 2017/03/23
	 */
	public void getPractitioner() {
		Integer id = getParaToInt();
		if (null != id) {
			setAttr("practitioner", Db.findById("t_practitioners", id));
		}

		// 查询入驻企业列表
		List<Record> enterpriseinlist = EnterpriseService.getEnterpriseInList();
		setAttr("enterpriseinlist", enterpriseinlist);

		render("practitioner_detail.html");
	}

	// 保存数据
	/**
	 * @author liyu
	 * @date 2017/03/23
	 */
	public void savePractitioner() {
		// 通过ID查询企业，获得企业名称
		Integer companyid = getParaToInt("enterpriseid");
		Record company = Db.findById("t_enterprise_in", companyid);
		String companyname = company.get("enterprise_name");

		Record record = new Record();
		record.set("id", getParaToInt("id"));
		record.set("company_id", companyid);
		record.set("company_name", companyname);
		record.set("the_date", getPara("thedate"));
		record.set("quantity", getParaToInt("quantity"));
		record.set("doctor", getParaToInt("doctor"));
		record.set("junior_college", getParaToInt("juniorcollege"));
		record.set("returnees", getParaToInt("returnees"));
		record.set("thousand_talents_program", getParaToInt("thousand"));
		record.set("fresh_graduates", getParaToInt("freshgraduates"));
		record.set("insurance", getParaToInt("insurance"));
		record.set("add_insurance", getParaToInt("addinsurance"));
		record.set("modify_time", new Date());

		boolean result = EnterpriseService.savePractitioner(record);

		renderJson("result", result);
	}

	// 删除数据
	/**
	 * @author liyu
	 * @date 2017/03/23
	 */
	public void delPractitioner() {
		Integer id = getParaToInt();
		boolean result = Db.deleteById("t_practitioners", id);
		renderJson(result);
	}

	/******************************* 企业知识产权管理 *******************************/
	// 企业知识产权列表
	/**
	 * @author liyu
	 * @date 2017/03/23
	 */
	public void property_right_list() {
		// 验证权限
		Record admin = getSessionAttr("admin");
		Integer rid = admin.getInt("role_id");
		String mopids = Db.queryStr("select module_power_id from t_role_permissions where role_id = ?", rid);
		if (mopids.indexOf("173") != -1) {
			setAttr("_add", true);
		}
		if (mopids.indexOf("174") != -1) {
			setAttr("_delete", true);
		}
		if (mopids.indexOf("175") != -1) {
			setAttr("_edit", true);
		}
		if (mopids.indexOf("176") != -1) {
			setAttr("_search", true);
		}

		Integer pagesize = 16;
		Integer pageno = getParaToInt("pageno") == null ? 1 : getParaToInt("pageno");
		String enterprisename = getPara("enterprisename") == null ? "" : getPara("enterprisename").trim();

		Page<Record> page = EnterpriseService.getPropertyRightList(pageno, pagesize, enterprisename);

		setAttr("enterprisename", enterprisename);
		setAttr("pageno", page.getPageNumber());
		setAttr("totalpage", page.getTotalPage());
		setAttr("totalrow", page.getTotalRow());
		setAttr("propertyrightlist", page.getList());

		render("property_right_list.html");
	}

	// 获得单条记录
	/**
	 * @author liyu
	 * @date 2017/03/23
	 */
	public void getPropertyRight() {
		Integer id = getParaToInt();
		if (null != id) {
			setAttr("propertyright", Db.findById("t_property_right", id));
		}

		// 查询入驻企业列表
		List<Record> enterpriseinlist = EnterpriseService.getEnterpriseInList();
		setAttr("enterpriseinlist", enterpriseinlist);

		render("property_right_detail.html");
	}

	// 保存数据
	/**
	 * @author liyu
	 * @date 2017/03/23
	 */
	public void savePropertyRight() {
		// 通过ID查询企业，获得企业名称
		Integer companyid = getParaToInt("enterpriseid");
		Record company = Db.findById("t_enterprise_in", companyid);
		String companyname = company.get("enterprise_name");

		Record record = new Record();
		record.set("id", getParaToInt("id"));
		record.set("company_id", companyid);
		record.set("company_name", companyname);
		record.set("the_date", getPara("thedate"));
		record.set("apply", getPara("apply"));
		record.set("approval", getPara("approval"));
		record.set("patent", getPara("patent"));
		record.set("copyright", getPara("copyright"));
		record.set("software_product", getPara("software"));
		record.set("modify_time", new Date());

		boolean result = EnterpriseService.savePropertyRight(record);

		renderJson("result", result);
	}

	// 删除数据
	/**
	 * @author liyu
	 * @date 2017/03/23
	 */
	public void delPropertyRight() {
		Integer id = getParaToInt();
		boolean result = Db.deleteById("t_property_right", id);
		renderJson(result);
	}
}
