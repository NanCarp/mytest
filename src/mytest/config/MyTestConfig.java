package mytest.config;

import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.c3p0.C3p0Plugin;
import com.jfinal.render.ViewType;
import com.jfinal.template.Engine;

public class MyTestConfig extends JFinalConfig {

	@Override
	public void configConstant(Constants me) {
		PropKit.use("config.txt");
		//设置当前环境为开发环境
		me.setDevMode(true);
		me.setViewType(ViewType.FREE_MARKER);
	}

	@Override
	public void configRoute(Routes me) {
         me.add(new FrontRoutes()); //前端路由
         me.add(new AdminRoutes()); //后端路由
	}

	@Override
	public void configEngine(Engine me) {
	}

	@Override
	public void configPlugin(Plugins me) {
		C3p0Plugin c3p0Plugin = new C3p0Plugin(PropKit.get("jdbcUrl"), PropKit.get("user"), PropKit.get("password"));
//		c3p0Plugin.setMinPoolSize(50);		// 连接池中保留的最小连接数
//		c3p0Plugin.setMaxPoolSize(200);		// 连接池中保留的最大连接数
//		c3p0Plugin.setInitialPoolSize(50);	// 初始链接数
//		c3p0Plugin.setMaxIdleTime(60);		// 每60秒检查所有连接池中的空闲连接。Default: 0
//		c3p0Plugin.setAcquireIncrement(10);	// 当连接池中的连接耗尽的时候c3p0一次同时获取的连接数
		me.add(c3p0Plugin);
		ActiveRecordPlugin arp = new ActiveRecordPlugin(c3p0Plugin);
		me.add(arp);
	}

	@Override
	public void configInterceptor(Interceptors me) {
		// TODO Auto-generated method stub

	}

	@Override
	public void configHandler(Handlers me) {
		// TODO Auto-generated method stub

	}

}
