package mytest.controller;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.kit.JsonKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

import mytest.interceptor.ManageInterceptor;
import mytest.service.LoginService;
import mytest.utils.MD5Util;

public class LoginController extends Controller {

	/**
	 * @description 主页面
	 * @author liyu
	 * @date 2017/03/28
	 */
	@Before(ManageInterceptor.class)
	public void index() {
		// 从 session 中读取 user
		Record user = getSessionAttr("user");
		// 转调了 HttpServletRequest.setAttribute(String, Object)，该方法可以将
		// 各种数据传递给 View 并在 View 中显示出来
		// 
		setAttr("user", user);
		// 根据用户权限，展示相应菜单
		// 根据角色 ID 找出所有菜单
		Integer roleId = user.getInt("role_id");
		List<Record> menus = LoginService.getMenusByRoleId(roleId);
		// 设置菜单列表的值
		List<Object> menuList = new ArrayList<>();
		for (Record menu : menus) {
			// 设置父菜单的值
			Map<String, Object> parentMenu = new HashMap<>();
			if(menu.getInt("pid") == 0) {
				parentMenu.put("title", menu.getStr("name"));
				parentMenu.put("icon", menu.getStr("icon"));
				parentMenu.put("spread", menu.getInt("id")==1?true:false);// 首菜单展开
				// 根据父菜单 id 找到所有子菜单，设置子菜单的值
				List<Object> childMenusList = new ArrayList<>();
				for (Record menu2 : menus) {
					Map<String, Object> childMap = new HashMap<>();
					if (menu2.getInt("pid") == menu.getInt("menu_id")) {
						childMap.put("title", menu2.getStr("name"));
						childMap.put("icon", menu2.getStr("icon"));
						childMap.put("href", menu2.getStr("url"));
						childMenusList.add(childMap);
					}
				}
				parentMenu.put("children", childMenusList);
				menuList.add(parentMenu);
			}
		}
		
		//　菜单列表转成 Json，传递给 view
		setAttr("navs", JsonKit.toJson(menuList));

		render("index.html");
	}

	/**
	 * @description 登录页 
	 * @author liyu
	 * @date 2017/03/28
	 */
	public void login() {
		render("login.html");
	}

	/**
	 * @description 登录操作
	 * @throws NoSuchAlgorithmException
	 * @throws UnsupportedEncodingException void 
	 * @author liyu
	 * @date 2017/03/28
	 */
	public void userLogin() throws NoSuchAlgorithmException, UnsupportedEncodingException {
		// 登陆结果，默认false
		boolean result = false;
		// 登陆结果消息
		String msg = new String("用户名或密码错误");

		// 登录页面提交的用户名、密码
		String username = getPara("userName");
		String password = getPara("password");
		// 根据用户名，从数据库查询用户记录
		Record user = LoginService.getLoginInfo(username);
		// 用户存在
		if (null != user) {
			// 用户存在，校验密码
			boolean isPassword = MD5Util.validatePassword(password, user.getStr("password"));
			// 密码正确，登陆成功
			if (isPassword) {
				result = true;
				msg = "登录成功";
				// 向session存储用户信息 
				getSession().setAttribute("user", user);
				// 创建cookie，7天免登陆
				setCookie("morality", "" + user.getInt("id"), 1000 * 60 * 60 * 24 * 7, "/login/");
			}
		} 
		// 结果数据写入map，以json格式传给页面
		Map<String, Object> responseMap = new HashMap<>();
		responseMap.put("result", result);
		responseMap.put("msg", msg);
		renderJson(responseMap);
	}
	
	
}
