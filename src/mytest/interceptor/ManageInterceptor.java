package mytest.interceptor;

import javax.servlet.http.Cookie;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Record;

import mytest.service.LoginService;

public class ManageInterceptor implements Interceptor{

	@Override
	public void intercept(Invocation inv) {
		Controller c = inv.getController();
		Record admin = c.getSessionAttr("admin");
		if(admin != null){
			inv.invoke();
		}else{
			Cookie[] ci = c.getCookieObjects();
			if(ci.length > 0){
				for(Cookie cookie:ci){
					if("morality".equals(cookie.getName())){
						int id = Integer.parseInt(cookie.getValue());
						admin = LoginService.getLoginById(id);
					}
				}
			}
			if(admin!=null){
				c.setSessionAttr("admin", admin);
				inv.invoke();
			}else{
				String ck = inv.getControllerKey();
				if("/manage".equals(ck)){
					c.redirect("/manage/login");
				}else{
					c.renderHtml("<script>window.parent.window.loginOut();</script>");
				}
			}
		}
	}

}
