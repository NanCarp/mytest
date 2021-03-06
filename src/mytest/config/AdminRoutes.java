package mytest.config;

import com.jfinal.config.Routes;

import mytest.controller.DataDictionaryController;
import mytest.controller.EnterpriseController;
import mytest.controller.FileManageController;
import mytest.controller.LoginController;
import mytest.controller.ParkManageController;
import mytest.controller.SystemController;

/**
 * @ClassName: AdminRoutes
 * @Description: TODO
 * @author liyu
 * @date 2017年3月27日 下午2:56:54
 * 
 */
public class AdminRoutes extends Routes {

	@Override
	public void config() {
		// 设置页面base路径
		setBaseViewPath("/manage");
		// 用户登录控制器
		add("/manage", LoginController.class, "/admin");

		//系统操作 
		add("/system", SystemController.class, "/system");
		
		// 数据字典
		add("/data", DataDictionaryController.class, "/data");
		
		//园区管理 
		add("/parkmanage",ParkManageController.class,"/parkmanage");
		
		//企业管理
		add("/enterprise", EnterpriseController.class, "/enterprise");
		
		//文件管理 
		add("/filemanage",FileManageController.class,"/filemanage");
		
		/*
		 * //系统操作 add("/systom", SystomController.class, "/systom");
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 * //查询统计 add("/statistic",StatisticController.class,"/statistic");
		 */
	}
}
