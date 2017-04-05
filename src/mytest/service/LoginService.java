package mytest.service;

import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

public class LoginService {

	// 拦截器获得用户信息
	public static Record getLoginById(int id){
		return Db.findFirst("SELECT a.id,a.name,a.password,b.id as role_id,b.role_name,c.department_name FROM t_employee a "+
				"LEFT JOIN t_role b ON a.role_id = b.id LEFT JOIN t_department c ON a.department_id = c.id WHERE a.status = 1 AND a.id=?", id);
	}
	
	/**
	 * @description 获得登录用户信息：用户id，姓名，密码，角色，角色名，部门名
	 * @param username 用户名
	 * @return Record 
	 * @author liyu
	 * @date 2017/03/28
	 */
	public static Record getLoginInfo(String username){
	  return Db.findFirst("SELECT a.id,a.name,a.password,a.role_id,b.role_name,c.department_name FROM t_employee a "+
			"LEFT JOIN t_role b ON a.role_id = b.id LEFT JOIN t_department c ON a.department_id = c.id WHERE a.status = 1 AND a.name=?", username);
	}
	
	// 根据角色ID找出所有菜单
	public static List<Record> getMenusByRoleId(int rid){
		return Db.find("SELECT b.id, b.url, a.menu_id, a.role_id,b.icon, b.pid, b.name FROM t_role_details a LEFT JOIN t_menu b ON a.menu_id = b.id WHERE role_id = ?", rid);
	}
	
	// 根据父次啊单和角色ID找出所有子菜单
	public static List<Record> getzMenusById(int pid, int rid){
		return  Db.find("SELECT a.name, a.url, a.icon FROM t_menu a LEFT JOIN t_role_details b ON a.id = b.menu_id WHERE a.pid = ? AND b.role_id = ?", pid, rid);
	}
	
	// 查询区域消息提醒列表
	public static List<Record> getNeedAreaList() {
		return Db.find("SELECT * FROM t_area WHERE status=1 ORDER BY modify_time DESC LIMIT 5 ");
	}

	// 查询需要缴费列表
	public static List<Record> getNeedPayList() {
		return Db.find(
				"SELECT * FROM t_payment WHERE should_pay_rent > paid_rent OR property_costs > paid_property_charges "
						+ " OR should_pay_water > real_water_fee LIMIT 5 ");
	}

	// 查询需要安全检查列表
	public static List<Record> getNeedInspectList() {
		return Db.find("SELECT * FROM t_safety_inspection WHERE is_rectification=0 LIMIT 5 ");
	}

}
