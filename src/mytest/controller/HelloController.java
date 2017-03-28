/**   
* @Title: HelloController.java 
* @Package mytest.controller 
* @Description: TODO
* @author liyu 
* @date 2017年3月27日 下午2:58:41 
* @version V1.0   
*/
package mytest.controller;

import com.jfinal.core.Controller;

/** 
* @ClassName: HelloController 
* @Description: TODO
* @author liyu
* @date 2017年3月27日 下午2:58:41 
*  
*/
public class HelloController extends Controller {
	public void index() {
		renderText("Hello");
	}
}
