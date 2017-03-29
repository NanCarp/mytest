package mytest.interceptor;

import javax.servlet.http.Cookie;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Record;

import mytest.service.LoginService;

public class ManageInterceptor implements Interceptor {

	@Override
	public void intercept(Invocation inv) {
		// 得到控制器
		Controller controller = inv.getController();
		// 从session获取用户（登陆成功时保存的）
		Record user = controller.getSessionAttr("user");
		// 用户已登录，返回
		if (user != null) {
			inv.invoke();
		} else {
			// 用户未登录，读取cookie
			Cookie[] cookies = controller.getCookieObjects();
			// 查找morality，根据morality的值查找用户
			if (cookies.length > 0) {
				for (Cookie cookie : cookies) {
					if ("morality".equals(cookie.getName())) {
						int id = Integer.parseInt(cookie.getValue());
						user = LoginService.getLoginById(id);
					}
				}
			}
			// morality未过期，将查询到的用户存入session
			if (user != null) {
				controller.setSessionAttr("user", user);
				inv.invoke();
			} else {
				// morality过期，获取控制器键（请求的URL）
				String controllerKey = inv.getControllerKey();
				// 重定向到登录页面
				if("/manage".equals(controllerKey)) {
					controller.redirect("/manage/login");
				} else {
					// 
					controller.renderHtml("<script>window.parent.window.loginOut();</script>");
				}
			}
		}

	}

}
