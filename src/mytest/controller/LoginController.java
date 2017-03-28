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

//@Before(ManageInterceptor.class)
public class LoginController extends Controller {

	/**
	 * @description 主页面
	 * @author liyu
	 * @date 2017/03/28
	 */
	public void index() {
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
				// 创建cookie，??
				Cookie cookie = new Cookie("morality", "" + user.getInt("id"));
				cookie.setMaxAge(1000 * 60 * 60 * 24 * 7);// 7天
				cookie.setPath("/login/");//？？
				getResponse().addCookie(cookie);
			}
		} 
		// 结果数据写入map，以json格式传给页面
		Map<String, Object> responseMap = new HashMap<>();
		responseMap.put("result", result);
		responseMap.put("msg", msg);
		renderJson(responseMap);
	}
}
