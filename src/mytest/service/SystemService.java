package mytest.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

/**
* 系统管理
* @author yangbo
*/
public class SystemService {

	// 获得角色列表
	public static Page<Record> getRoleList(Integer pageno, int pagesize) {
		return Db.paginate(pageno, pagesize, "SELECT a.id,a.role_name,b.department_name,a.role_desc,a.create_time  ", "FROM t_role a LEFT JOIN t_department b ON a.department_id = b.id");
	}

	// 获得部门
	public static List<Record> getDepartMents() {
		return Db.find("SELECT id,department_name FROM t_department");
	}

	// 保存数据
	public static boolean saveRole(Integer id, String rolename, Integer did, String desc) {
		Record record = new Record();
		record.set("role_name", rolename);
		record.set("department_id", did);
		record.set("role_desc", desc);
		record.set("modify_time", new Date());
		if(id != null){
			record.set("id", id);
			return Db.update("t_role", record);
		}else{
			record.set("create_time", new Date());
			return Db.save("t_role", record);
		}
	}
	
	// 获得菜单列表
	public static Page<Record> getMenuList(Integer pageno, int pagesize) {
		return Db.paginate(pageno, pagesize, " SELECT a.id, a.name,b.name AS pname,a.url,a.remarks ", " FROM t_menu a LEFT JOIN t_menu b ON a.pid = b.id ORDER BY a.id");
	}

	// 获得菜单
	public static List<Record> getMenu() {
		return Db.find("select id,name,url,pid,icon from t_menu");
	}
	
	// 获得菜单权限
	public static List<Record> getAuthMenu() {
		return Db.find("SELECT id,pid,name FROM t_menu UNION SELECT a.button_id,b.id AS pid,a.method_name FROM t_power_register a LEFT JOIN t_menu b ON a.menu_id = b.id");
	}

	// 保存菜单
	public static boolean saveMenu(Integer id, String name, Integer pid, String url, String desc) {
		Record record = new Record();
		record.set("name", name);
		record.set("url", url);
		record.set("pid", pid);
		record.set("remarks", desc);
		record.set("modify_time", new Date());
		if(id != null){
			record.set("id", id);
			return Db.update("t_menu", record);
		}else{
			record.set("icon", "&#xe620;");
			record.set("create_time", new Date());
			return Db.save("t_menu", record);
		}
	}

	// 保存权限
	public static boolean saveAuthority(Integer id, String mid, int rid) {
		boolean result = false;
		String[] mlist = new String[]{};
		if(rid == 1){
			mid = Db.queryStr("SELECT GROUP_CONCAT(id) FROM t_menu");
		}
		mlist = mid.split(",");
		Db.update("delete from t_role_details where role_id=?", rid);
		String mpid = "";
		for(int i=0;i<mlist.length;i++){
			if(Integer.parseInt(mlist[i]) < 100){
				Record record = new Record();
				record.set("role_id", rid);
				record.set("menu_id", mlist[i]);
				record.set("create_time", new Date());
				record.set("modify_time", new Date());
				result = Db.save("t_role_details", record);
				if(!result){
					return result;
				}
			}else{
				mpid += mlist[i] + ",";
			}
		}
		if(rid == 1){
			mpid = Db.queryStr("SELECT GROUP_CONCAT(button_id) FROM t_power_register");
		}else{
			mpid = mpid.substring(0, mpid.length()-1);
		}
		Number rnum = Db.queryNumber("select count(1) from t_role_permissions where role_id=?", rid);
		if(rnum.intValue()==0){
			Record r = new Record();
			r.set("role_id", rid);
			r.set("module_power_id", mpid);
			r.set("create_time", new Date());
			r.set("modify_time", new Date());
			return Db.save("t_role_permissions", r);
		}else{
			int unum = Db.update("update t_role_permissions set module_power_id = ? where role_id = ?", mpid, rid);
			return unum==1?true:false;
		}
	}

	// 获得模块列表
	public static Page<Record> getAuthList(Integer pageno, int pagesize) {
		return Db.paginate(pageno, pagesize, "SELECT a.id,b.name ,a.button_id, method_name ", " FROM t_power_register a LEFT JOIN t_menu b ON a.menu_id = b.id WHERE b.pid <> 0 order by a.id");
	}

	// 获得所有子菜单
	public static List<Record> getZiMenuList() {
		return Db.find("SELECT id,name FROM t_menu WHERE pid <> 0");
	}

	// 保存按钮
	public static Map<String, Object> saveAuth(Integer id, String buttonid, Integer zimenu, String method_name) {
		boolean result = false;
		String msg = new String();
		id = id==null?0:id;
		Number bnum = Db.queryNumber("select count(1) from t_power_register where button_id=? and id <> ?", buttonid, id);
		if(bnum.intValue() == 0){
			Record record = new Record();
			record.set("menu_id", zimenu);
			record.set("button_id", buttonid);
			record.set("method_name", method_name);
			record.set("modify_time", new Date());
			if(id != 0){
				record.set("id", id);
				result = Db.update("t_power_register", record);
			}else{
				record.set("create_time", new Date());
				result = Db.save("t_power_register", record);
			}
			if(result){
				msg = "保存成功";
			}else{
				msg = "数据异常";
			}
		}else{
			msg = "此按钮ID已存在，请重新输入";
		}
		Map<String, Object> map = new HashMap<>();
		map.put("result", result);
		map.put("msg", msg);
		return map;
	}

	// 删除对应的权限
	public static void deleteAuthForRole(Integer id) {
		String buid = Db.queryStr("select button_id from t_power_register where id=?", id);
		List<Record> roleauths = Db.find("select id, role_id, module_power_id from t_role_permissions");
		for (Record roleauth : roleauths) {
			String buids = roleauth.getStr("module_power_id");
			System.out.println(buids.indexOf(buid));
			if(buids.indexOf(buid) >= 0){
				String[] buttons = buids.split(",");
				String[] newBtn = new String[buttons.length];
				for(int i=0; i<buttons.length; i++){
					if(!buid.equals(buttons[i])){
						newBtn[i] = buttons[i];
					}
				}
				String newids = "";
				for(int j=0; j<newBtn.length; j++){
					newids += newBtn[j];
					if(j<newBtn.length-1){
						newids += ",";
					}
				}
				newids = newids.replace("null,", "");
				Record record = new Record();
				record.set("id", roleauth.getInt("id"));
				record.set("module_power_id", newids);
				record.set("modify_time", new Date());
				Db.update("t_role_permissions", record);
			}
		}
	}
}
