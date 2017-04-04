package mytest.service;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

/**
 * @desc 企业管理
 * @author liyu
 */

public class EnterpriseService {
	/*********************** 入驻企业管理 ************************/
	// 入驻企业分页查询
	/**
	 * @author liyu
	 * @date 2017/03/23
	 * @param pageno
	 * @param pagesize
	 * @param enterprisename
	 * @return Page<Record>
	 */
	public static Page<Record> getEnterpriseInListByPage(Integer pageno, Integer pagesize, String enterprisename) {
		String sqlExceptSelect = "FROM t_enterprise_in WHERE is_retreat='0' ";
		if ("" != enterprisename) {
			sqlExceptSelect += "AND enterprise_name LIKE '%" + enterprisename + "%' ";
		}

		return Db.paginate(pageno, pagesize, "SELECT * ", sqlExceptSelect);
	}

	// 查询入驻企业列表
	/**
	 * @author liyu
	 * @date 2017/03/23
	 * @return List<Record>
	 */
	public static List<Record> getEnterpriseInList() {
		return Db.find("SELECT * FROM t_enterprise_in WHERE is_retreat='0' ");
	}

	// 保存入驻企业
	/**
	 * @author liyu
	 * @date 2017/03/23
	 * @param record
	 * @return boolean
	 */
	public static boolean saveEnterprise(Record record) {
		if (null != record.getInt("id")) {
			return Db.update("t_enterprise_in", record);
		} else {
			record.set("create_time", new Date());
			return Db.save("t_enterprise_in", record);
		}
	}

	// 获取企业的行业id、子级行业id、父级行业id、企业类型（大楼性质）id
	/**
	 * @author liyu
	 * @date 2017/03/23
	 * @param id
	 * @return List<Record>
	 */
	public static List<Record> getIndustryIds(Integer id) {
		return Db
				.find(" SELECT a.*,b.industry_name,b.id as industry_id,c.id AS sub_industry_id,d.id AS superior_industry_id,e.id as type_id "
						+ " FROM  t_enterprise_in a LEFT JOIN t_industry_code b ON a.industry=b.industry_code "
						+ " LEFT JOIN t_sub_industry c ON b.sub_industry=c.sub_industry_name "
						+ " LEFT JOIN t_superior_industry d ON c.superior_industry_id=d.id "
						+ " LEFT JOIN t_building_nature e ON a.enterprise_type=e.id " + " WHERE a.id=" + id);
	}

	/*********************** 离驻企业管理 ************************/
	// 离驻企业分页查询
	/**
	 * @author liyu
	 * @date 2017/03/23
	 * @param pageno
	 * @param pagesize
	 * @param enterprisename
	 * @param start
	 * @param end
	 * @return Page<Record>
	 */
	public static Page<Record> getEnterpriseRetreatList(Integer pageno, Integer pagesize, String enterprisename,
			String start, String end) {
		String sqlExceptSelect = "FROM t_enterprise_in WHERE is_retreat='1' ";
		if ("" != enterprisename) {
			sqlExceptSelect += "AND enterprise_name LIKE '%" + enterprisename + "%' ";
		}
		if ("" != start) {
			sqlExceptSelect += "AND retreat_time >= '" + start + "'";
		}
		if ("" != end) {
			sqlExceptSelect += "AND retreat_time <= '" + end + "'";
		}

		return Db.paginate(pageno, pagesize, "SELECT * ", sqlExceptSelect);
	}

	public static boolean enterpriseRetreat(Record record) {
		return Db.tx(new IAtom() {

			@Override
			public boolean run() throws SQLException {
				boolean result = false;
				int id = record.getInt("id");
				result = Db.update("t_enterprise_in", record);
				if (!result) {
					return false;
				}
				// 企业离驻后，改变区域管理，区域状态为空，公司为空；
				String company_name = Db.findById("t_enterprise_in", id).getStr("enterprise_name");
				List<Record> areaList = Db.find("select * from t_area where the_company = '" + company_name + "' ");
				for (Record area : areaList) {
					area.set("status", false).set("the_company", null);
					result = Db.update("t_area", area);
					if (!result) {
						return false;
					}
				}
				return true;
			}

		});
	}

	/*********************** 企业经济情况管理 ************************/
	// 企业经济情况分页查询
	/**
	 * @author liyu
	 * @date 2017/03/23
	 * @param pageno
	 * @param pagesize
	 * @param enterprisename
	 * @return Page<Record>
	 */
	public static Page<Record> getEconomyList(Integer pageno, Integer pagesize, String enterprisename) {
		String sqlExceptSelect = "FROM t_enterprise_economy WHERE 1=1 ";
		if ("" != enterprisename) {
			sqlExceptSelect += "AND company_name LIKE '%" + enterprisename + "%' ";
		}

		return Db.paginate(pageno, pagesize, "SELECT id,company_name,the_date,income,net_profit,taxation,investment ",
				sqlExceptSelect);
	}

	// 保存企业经济情况
	/**
	 * @author liyu
	 * @date 2017/03/23
	 * @param record
	 * @return boolean
	 */
	public static boolean saveEconomy(Record record) {
		if (null != record.getInt("id")) {
			return Db.update("t_enterprise_economy", record);
		} else {
			record.set("create_time", new Date());
			return Db.save("t_enterprise_economy", record);
		}
	}

	/*********************** 企业从业人员管理 ************************/
	// 企业从业人员分页查询
	/**
	 * @author liyu
	 * @date 2017/03/23
	 * @param pageno
	 * @param pagesize
	 * @param enterprisename
	 * @return Page<Record>
	 */
	public static Page<Record> getPractitionersList(Integer pageno, Integer pagesize, String enterprisename) {
		String sqlExceptSelect = "FROM t_practitioners WHERE 1=1 ";
		if ("" != enterprisename) {
			sqlExceptSelect += "AND company_name LIKE '%" + enterprisename + "%' ";
		}

		return Db.paginate(pageno, pagesize, "SELECT * ", sqlExceptSelect);
	}

	// 保存企业从业人员
	/**
	 * @author liyu
	 * @date 2017/03/23
	 * @param record
	 * @return boolean
	 */
	public static boolean savePractitioner(Record record) {
		if (null != record.getInt("id")) {
			return Db.update("t_practitioners", record);
		} else {
			record.set("create_time", new Date());
			return Db.save("t_practitioners", record);
		}
	}

	/*********************** 企业知识产权管理 ************************/
	// 企业知识产权分页查询
	/**
	 * @author liyu
	 * @date 2017/03/23
	 * @param pageno
	 * @param pagesize
	 * @return Page<Record>
	 */
	public static Page<Record> getPropertyRightList(Integer pageno, Integer pagesize) {
		return Db.paginate(pageno, pagesize,
				"SELECT id,company_name,the_date,apply,approval,patent,copyright,software_product",
				"FROM t_property_right ");
	}

	/**
	 * @author liyu
	 * @date 2017/03/23
	 * @param pageno
	 * @param pagesize
	 * @param enterprisename
	 * @return Page<Record>
	 */
	public static Page<Record> getPropertyRightList(Integer pageno, Integer pagesize, String enterprisename) {
		String sqlExceptSelect = "FROM t_property_right WHERE 1=1 ";
		if ("" != enterprisename) {
			sqlExceptSelect += "AND company_name LIKE '%" + enterprisename + "%' ";
		}

		return Db.paginate(pageno, pagesize,
				"SELECT id,company_name,the_date,apply,approval,patent,copyright,software_product ", sqlExceptSelect);
	}

	// 保存企业知识产权
	/**
	 * @author liyu
	 * @date 2017/03/23
	 * @param record
	 * @return boolean
	 */
	public static boolean savePropertyRight(Record record) {
		if (null != record.getInt("id")) {
			return Db.update("t_property_right", record);
		} else {
			record.set("create_time", new Date());
			return Db.save("t_property_right", record);
		}
	}

}
