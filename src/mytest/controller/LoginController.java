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

//@Before(ManageInterceptor.class)
public class LoginController extends Controller{
	
	public void index(){
		render("index.html");
	}
	
	// 登录
	public void login(){
		render("login.html");
	}
	
	// 登录操作
	public void adminLogin() throws NoSuchAlgorithmException, UnsupportedEncodingException{
		String username = getPara("username");
		String password = getPara("password");
		
		boolean result = false;
		String msg = new String();
		
/*		Record admin = LoginService.getLoginInfo(username);
		if(admin == null){
			msg = "用户名或密码错误";
		}else{
			boolean v = MD5Util.validPassword(password, admin.getStr("password"));
			if(v){
				result = true;
				msg = "登录成功";
				getSession().setAttribute("admin", admin);
				Cookie cookie = new Cookie("morality", ""+admin.getInt("id"));
				cookie.setMaxAge(60*60*24*7);
				cookie.setPath("/login/");
				getResponse().addCookie(cookie);
			}else{
				msg = "用户名或密码错误";
			}
		}*/
		Map<String, Object> responseMap = new HashMap<>();
		responseMap.put("result", true);//写死	
		responseMap.put("msg", "登陆成功");
		renderJson(responseMap);
	}
}
