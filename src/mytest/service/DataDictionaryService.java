package mytest.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

/**
 * 数据字典管理
 * 
 * @author liyu
 */

public class DataDictionaryService {
	/*********************** 职位管理 ************************/
	// 职位分页查询
	/**
	 * @author liyu
	 * @date 2017/03/23
	 * @param Integer pageno, int pagesize
	 * @return Page<Record>
	 */
	public static Page<Record> getPositionList(Integer pageno, int pagesize) {
		return Db.paginate(pageno, pagesize, "SELECT id,position_name,create_time ", "FROM t_position");
	}

	// 保存职位
	/**
	 * @author liyu
	 * @date 2017/03/23
	 * @param Integer id, String positionname
	 * @return boolean
	 */
	public static boolean savePosition(Integer id, String positionname) {
		Record record = new Record();
		record.set("position_name", positionname);
		record.set("modify_time", new Date());
		if (null != id) {
			record.set("id", id);
			return Db.update("t_position", record);
		} else {
			record.set("create_time", new Date());
			return Db.save("t_position", record);
		}
	}

	/*********************** 部门管理 ************************/
	// 部门分页查询
	/**
	 * @author liyu
	 * @date 2017/03/23
	 * @param Integer pageno, int pagesize
	 * @return Page<Record>
	 */
	public static Page<Record> getDepartmentList(Integer pageno, int pagesize) {
		return Db.paginate(pageno, pagesize, "SELECT id,department_name,create_time,`describe` ", "FROM t_department ");
	}

	// 保存部门
	/**
	 * @author liyu
	 * @date 2017/03/23
	 * @param Integer id, String departmentname, String description
	 * @return boolean
	 */
	public static boolean saveDepartment(Integer id, String departmentname, String description) {
		Record record = new Record();
		record.set("department_name", departmentname);
		record.set("describe", description);
		record.set("modify_time", new Date());
		if (null != id) {
			record.set("id", id);
			return Db.update("t_department", record);
		} else {
			record.set("create_time", new Date());
			return Db.save("t_department", record);
		}
	}

	/*********************** 大楼编号管理 ************************/
	// 大楼编号分页查询
	/**
	 * @author liyu
	 * @date 2017/03/23
	 * @param Integer pageno, Integer pagesize
	 * @return Page<Record>
	 */
	public static Page<Record> getBuildingNoList(Integer pageno, Integer pagesize) {
		return Db.paginate(pageno, pagesize, "SELECT id,building_no,sort_id,create_time ", "FROM t_building_number ");
	}

	// 保存大楼编号
	/**
	 * @author liyu
	 * @date 2017/03/23
	 * @param Integer id, String buildingNo, Integer sortId
	 * @return boolean
	 */
	public static boolean saveBuildingNo(Integer id, String buildingNo, Integer sortId) {
		Record record = new Record();
		record.set("building_no", buildingNo);
		record.set("sort_id", sortId);
		record.set("modify_time", new Date());
		if (null != id) {
			record.set("id", id);
			return Db.update("t_building_number", record);
		} else {
			record.set("create_time", new Date());
			return Db.save("t_building_number", record);
		}
	}

	/*********************** 大楼性质管理 ************************/
	// 大楼性质分页查询
	/**
	 * @author liyu
	 * @date 2017/03/23
	 * @param Integer pageno, Integer pagesize
	 * @return Page<Record>
	 */
	public static Page<Record> getBuildingNatureList(Integer pageno, Integer pagesize) {
		return Db.paginate(pageno, pagesize, "SELECT id,name,sort_id,create_time ", "FROM t_building_nature ORDER BY sort_id ");
	}

	// 保存大楼性质
	/**
	 * @author liyu
	 * @date 2017/03/23
	 * @param Integer id, String name, Integer sortId
	 * @return boolean
	 */
	public static boolean saveBuildingNature(Integer id, String name, Integer sortId) {
		Record record = new Record();
		record.set("name", name);
		record.set("sort_id", sortId);
		record.set("modify_time", new Date());
		if (null != id) {
			record.set("id", id);
			return Db.update("t_building_nature", record);
		} else {
			record.set("create_time", new Date());
			return Db.save("t_building_nature", record);
		}
	}

	/*********************** 父级行业管理 ************************/
	// 父级行业分页查询
	/**
	 * @author liyu
	 * @date 2017/03/23
	 * @param Integer pageno, Integer pagesize
	 * @return Page<Record>
	 */
	public static Page<Record> getSuperiorIndustryList(Integer pageno, Integer pagesize) {
		return Db.paginate(pageno, pagesize, "SELECT id,industry_code,industry_name,create_time ",
				"FROM t_superior_industry ");
	}

	// 查询父级行业列表
	/**
	 * @author liyu
	 * @date 2017/03/23
	 * @param 
	 * @return List<Record>
	 */
	public static List<Record> getSuperiorIndustryList() {
		return Db.find("SELECT id,industry_code,industry_name,'0' AS pid FROM t_superior_industry ");
	}

	// 通过子级行业代码，找到父级行业
	/**
	 * @author liyu
	 * @date 2017/03/23
	 * @param Integer id
	 * @return List<Record>
	 */
	public static List<Record> getSuperIndustryBySubId(Integer id) {
		return Db.find("SELECT * FROM t_superior_industry WHERE id= " + id);
	}

	// 保存父级行业
	/**
	 * @author liyu
	 * @date 2017/03/23
	 * @param Integer id, String industryCode, String industryName
	 * @return boolean
	 */
	public static boolean saveSuperiorIndustry(Integer id, String industryCode, String industryName) {
		Record record = new Record();
		record.set("industry_code", industryCode);
		record.set("industry_name", industryName);
		record.set("modify_time", new Date());
		if (null != id) {
			record.set("id", id);
			return Db.update("t_superior_industry", record);
		} else {
			record.set("create_time", new Date());
			return Db.save("t_superior_industry", record);
		}
	}

	/*********************** 子级行业管理 ************************/
	// 子级行业分页查询
	/**
	 * @author liyu
	 * @date 2017/03/23
	 * @param Integer pageno, Integer pagesize
	 * @return Page<Record>
	 */
	public static Page<Record> getSubIndustryList(Integer pageno, Integer pagesize) {
		return Db.paginate(pageno, pagesize,
				"SELECT a.id,sub_industry_code,sub_industry_name,superior_industry_id,a.create_time,b.industry_code,b.industry_name ",
				"FROM t_sub_industry a LEFT JOIN t_superior_industry b ON a.superior_industry_id=b.id ");
	}

	// 查询子级行业列表
	/**
	 * @author liyu
	 * @date 2017/03/23
	 * @param 
	 * @return List<Record>
	 */
	public static List<Record> getSubIndustryList() {
		return Db.find(
				"SELECT a.id,a.sub_industry_code,a.sub_industry_name,a.superior_industry_id,b.industry_name AS super_industry_name FROM t_sub_industry a LEFT JOIN t_superior_industry b ON a.superior_industry_id = b.id ");
	}

	// 查询子级行业列表，通过父级ID
	/**
	 * @author liyu
	 * @date 2017/03/23
	 * @param Integer superId
	 * @return List<Record>
	 */
	public static List<Record> getSubIndustryBySuperId(Integer superId) {
		return Db
				.find(" SELECT a.id,a.sub_industry_code,a.sub_industry_name,a.superior_industry_id,b.industry_name AS super_industry_name "
						+ " FROM t_sub_industry a LEFT JOIN t_superior_industry b ON a.superior_industry_id = b.id "
						+ " WHERE a.superior_industry_id = " + superId
						+ " ORDER BY sub_industry_code");
	}

	// 保存子级行业数据
	/**
	 * @author liyu
	 * @date 2017/03/23
	 * @param Integer id, String industrycode, String industryname, int superiorindustryid
	 * @return boolean
	 */
	public static boolean saveSubIndustry(Integer id, String industrycode, String industryname,
			int superiorindustryid) {
		Record record = new Record();
		record.set("sub_industry_code", industrycode);
		record.set("sub_industry_name", industryname);
		record.set("superior_industry_id", superiorindustryid);
		record.set("modify_time", new Date());
		if (null != id) {
			record.set("id", id);
			return Db.update("t_sub_industry", record);
		} else {
			record.set("create_time", new Date());
			return Db.save("t_sub_industry", record);
		}
	}

	/*********************** 行业代码管理 ************************/
	// 行业代码分页查询
	/**
	 * @author liyu
	 * @date 2017/03/23
	 * @param Integer pageno, Integer pagesize
	 * @return Page<Record>
	 */
	public static Page<Record> getIndustryCodeList(Integer pageno, Integer pagesize) {
		return Db.paginate(pageno, pagesize,
				"SELECT id,industry_code,industry_name,superior_industry,sub_industry,create_time ",
				"FROM t_industry_code ORDER BY industry_code ");
	}

	// 行业代码列表
	/**
	 * @author liyu
	 * @date 2017/03/23
	 * @param 
	 * @return List<Record>
	 */
	public static List<Record> getIndustryCodeList() {
		return Db.find("SELECT id,industry_code,industry_name,superior_industry,sub_industry FROM t_industry_code ");
	}
	
	// 查询行业列表，通过子级ID
	/**
	 * @author liyu
	 * @date 2017/03/23
	 * @param Integer subId
	 * @return List<Record>
	 */
	public static List<Record> getIndustryBySubId(Integer subId) {
		return Db.find("SELECT a.id,a.industry_code,a.industry_name "
				+ "FROM t_industry_code a LEFT JOIN t_sub_industry b ON a.sub_industry=b.sub_industry_name "
				+ "WHERE b.id=" + subId);
	}

	
	// 行业代码列表，增加父级行业id
	/**
	 * @author liyu
	 * @date 2017/03/23
	 * @param 
	 * @return List<Record>
	 */
	public static List<Record> getIndustryList() {
		return Db.find("SELECT a.id,a.industry_code,a.industry_name,a.superior_industry,a.sub_industry,b.industry_code "
				+ "FROM t_industry_code AS a LEFT JOIN t_sub_industry AS b ON a.sub_industry=b.sub_industry_name");
	}
	
	// 保存行业代码数据
	/**
	 * @author liyu
	 * @date 2017/03/23
	 * @param Record record
	 * @return boolean
	 */
	public static boolean saveIndustryCode(Record record) {
		if (null != record.getInt("id")) {
			return Db.update("t_industry_code", record);
		} else {
			record.set("create_time", new Date());
			return Db.save("t_industry_code", record);
		}
	}

}
