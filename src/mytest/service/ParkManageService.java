package mytest.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.Region;

import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

	/**
	 * @ClassName:ParkManageService
	 * @Description:园区管理
	 * @author:xuhui
	 * @date 2017年03月23日
	 */

public class ParkManageService {

	/***********************员工管理************************/
	/**
	 * @param pageno
	 * @param pagesize
	 * @return Page<Record> result
	 * @desc  员工管理
	 */
	public static Page<Record> getEmpList(Integer pageno, int pagesize){
		return Db.paginate(pageno, pagesize, "select e.id,e.name,e.empno,e.phone,"
				+ "d.department_name,p.position_name,e.status,r.role_name",
				"from t_employee e left join t_department d on e.department_id = d.id "
				+ "left join t_position p on p.id = e.position left join t_role r ON r.id = e.role_id"
				+ " order by e.id");
	}
	
	/**
	 * @param id
	 * @return Record result
	 * @desc 根据id查询员工信息
	 */
	public static Record getEmp(int id){
		return Db.findById("t_employee", id);
	}
	
	/**
	 * @param id
	 * @return Record result
	 * @desc 根据id删除员工信息
	 */
	public static boolean delEmp(int id){
		return Db.deleteById("t_employee", id);	
	}
	
	/**
	 * @param id
	 * @param name
	 * @param password
	 * @param empno
	 * @param phone
	 * @param department_id
	 * @param position
	 * @param role_id
	 * @param status
	 * @return boolean result
	 * @desc 根据id存储或者更新员工信息
	 */
	public static boolean saveEmp(Integer id,String name,String password,int empno
			,String phone,int department_id,int position,int role_id,int status){
		Record record = new Record();
		record.set("name", name);
		record.set("password",password);
		record.set("empno", empno);
		record.set("phone", phone);
		record.set("department_id",department_id);
		record.set("position", position);
		record.set("role_id", role_id);
		record.set("status", status);
		record.set("modify_time", new Date());
		if(id != null){
			record.set("id", id);
			return Db.update("t_employee", record);
		}else{
			record.set("create_time", new Date());
			return Db.save("t_employee", record);
		}
	}
	
	/**
	 * @return List<Record> result
	 * @desc 获取职位
	 */
	public static List<Record> getPosition(){
		return Db.find("select id,position_name from t_position");
	}
	
	/**
	 * @return List<Record> result
	 * @desc 查询角色列表
	 */
	public static List<Record> getRole(){
		return Db.find("select * from t_role");
	}
	
	/***********************楼宇管理************************/
	/**
	 * @param pageno
	 * @param pagesize
	 * @param fstate
	 * @param buildno
	 * @return Page<Record> result
	 * @desc 楼宇管理
	 */
	public static Page<Record> getBuilding(Integer pageno, int pagesize,Integer fstate,String buildno){
		String sqlExceptSelect = " from t_building b LEFT JOIN t_building_nature n "
				                + "ON b.nature = n.id LEFT JOIN t_building_number u "
				                + "ON b.building_no = u.id where 1=1";
		//fstate==1表示选择'已满',fstate==0表示选择'未满',fstate==9表示未选择区域状态;
		if(fstate !=null && fstate!=9){                   
			sqlExceptSelect += " and b.status = "+ fstate;
		}
		if(buildno !=null && buildno !=""){
			sqlExceptSelect += " and u.building_NO ='"+buildno+"'";
		}
		sqlExceptSelect += " ORDER BY b.id";
		return Db.paginate(pageno, pagesize, "select b.id,b.name,n.name as naturename,"
				       + "b.floor_no,u.building_NO,b.total_area,b.usable_area,b.status"
				       + ",b.total_area-IFNULL((SELECT SUM(a.area) from t_area a where a.building_no=b.building_no),0) as usable",sqlExceptSelect);
	}
	
	/**
	 * @param id
	 * @return Record result
	 * @desc 根据id查询单个楼宇信息
	 */
	public static Record getBuildingMessage(int id){	
		return Db.findFirst("select b.id,b.nature,b.building_no,b.name,n.name as naturename,"
				+ "b.floor_no,u.building_NO,b.total_area,b.usable_area,b.status,"
				+ "b.total_area-IFNULL((SELECT SUM(a.area) from t_area a where a.building_no=b.building_no),0) as usable "
				+ "from t_building b LEFT JOIN t_building_nature n ON n.id = b.nature LEFT JOIN t_building_number u "
				+ "on b.building_no = u.id where b.id = ?",id);
	}
	
	/**
	 * @param id
	 * @param name
	 * @param nature
	 * @param floor_no
	 * @param building_no
	 * @param total_area
	 * @param usable_area
	 * @param status
	 * @return boolean result
	 * @desc 修改以及添加楼宇信息
	 */
	
	public static boolean saveBuild(Integer id,String name,int nature,int floor_no
						,int building_no,int total_area,int usable_area,int status){
		Record record = new Record();
		record.set("name", name);
		record.set("nature", nature);
		record.set("floor_no", floor_no);
		record.set("building_no", building_no);
		record.set("total_area", total_area);
		record.set("usable_area",usable_area);
		record.set("status", status);
		record.set("modify_time", new Date());
		if(id!=null){
			record.set("id", id);
			return Db.update("t_building", record);
		}else{
			record.set("create_time", new Date());
			return Db.save("t_building", record);
		}
		
	}
	
	/**
	 * @param id
	 * @return boolean result
	 * @desc 根据id删除楼宇信息
	 */
	public static boolean delBuild(int id){
		return Db.deleteById("t_building", id);
	}
	
	/**
	 * @return List<Record> result
	 * @desc 查询大楼所有性质
	 */
	public static List<Record> getBuildingNature(){
		return Db.find("select * from t_building_nature");
	}
	
	/**
	 * @return List<Record> result
	 * @desc 查询t_building_number中所有楼号
	 */
	public static List<Record> getBuildNos(){
		return Db.find("select * from t_building_number");
	}
	
	/***********************区域管理************************/
	/**
	 * @param pageno
	 * @param pagesize
	 * @param fstate
	 * @param direction
	 * @param areaname
	 * @param company_name
	 * @return Page<Record> result
	 * @desc 区域管理
	 */
	public static Page<Record> getArea(Integer pageno,int pagesize,Integer fstate,String direction
									,String areaname,String company_name){
		String sqlExceptSelect = " from t_area a LEFT JOIN t_building_number u on a.building_no = u.id where 1=1";
		boolean status = false;
		//fstate==1表示选择'已满',fstate==0表示选择'未满',fstate==9表示未选择区域状态;
		if(fstate !=null && fstate!=9){
			if(fstate==1){
				status = true;
				sqlExceptSelect += " and a.status = "+ status;
			}else{
				status = false;
				sqlExceptSelect += " and a.status = "+ status;
			}
		}
		if(direction !=null && !direction.equals("朝向")){
			sqlExceptSelect += " and a.direction ='"+direction+"'";
		}
		if(areaname != null&& areaname!=""){
			sqlExceptSelect +=" and a.area_name like '%"+areaname+"%'";
		}
		if(company_name != null&& company_name!=""){
			sqlExceptSelect +=" and a.the_company like '%"+company_name+"%'";
		}
		sqlExceptSelect += " ORDER BY a.id";
		return Db.paginate(pageno, pagesize, "select a.*,u.building_NO as numb ", sqlExceptSelect);	
	}
	
	/**
	 * @param id
	 * @return Record result
	 * @desc 根据id查询单个区域信息
	 */
	public static Record getArea(int id){
		return Db.findFirst("select * from t_area where id =?", id);
	}
	
	/**
	 * @param id
	 * @param company_name
	 * @return boolean result
	 * @desc 根据id添加区域管理的入驻退驻企业
	 */
	public static boolean saveAreaConpanyName(int id,String company_name){
		Record record = new Record();
		record = Db.findById("t_area", id).set("the_company", company_name).set("status",true);
		return Db.update("t_area", record);
	}
	
	/**
	 * @param id
	 * @return boolean result
	 * @desc 根据id清空区域管理入驻退驻企业
	 */
	public static boolean saveAreaCompanyNull(int id){
		Record record = new Record();
		record = Db.findById("t_area", id).set("the_company",null).set("status",false);
		return Db.update("t_area", record);
	}
	/**
	 * @param id
	 * @return Record result
	 * @desc 根据id查询单个企业信息
	 */
	public static Record getEnterperiseById(int id){
		return Db.findById("t_enterprise_in", id);
	}
	
	/**
	 * @param id
	 * @param area_name
	 * @param direction
	 * @param area_no
	 * @param building_no
	 * @param floor_no
	 * @param area
	 * @param status
	 * @param the_company
	 * @return boolean result
	 * @desc 修改以及添加区域信息
	 */
	public static boolean saveArea(Integer id,String area_name
			,String direction,String area_no,String building_no
			,String floor_no,int area,Byte status,String the_company){
		Record record = new Record();
		record.set("area_name", area_name);
		record.set("direction", direction);
		record.set("area_no", area_no);
		record.set("building_no", building_no);
		record.set("floor_no", floor_no);
		record.set("area", area);
		record.set("status", status);
		record.set("the_company", the_company);
		record.set("modify_time", new Date());
		if(id!=null){
			record.set("id", id);
			return Db.update("t_area", record);
		}else{
			record.set("create_time", new Date());
			return Db.save("t_area", record);
		}
	}
	
	/**
	 * @param id
	 * @return boolean result
	 * @desc 根据id删除区域信息
	 */
	public static boolean delArea(int id){
		return Db.deleteById("t_area", id);
	}
	
	/**
	 * @return List<Record> result
	 * @desc 查询所有单位信息
	 */
	public static List<Record> getEnterprise(){
		return Db.find("select * from t_enterprise_in");
	}
	
	/**
	 * @param buildingno
	 * @return Record result
	 * @desc 根据楼号查询剩余面积数
	 */
	public static Record savebuildingstatus(Integer buildingno){
		return Db.findFirst("SELECT  b.building_no,b.total_area-(SELECT SUM(area) from t_area a where a.building_no = b.building_no) "
				+ "as sta from t_building b WHERE b.building_no = ?;",buildingno);
	}
	
	/***********************缴费管理************************/
	/**
	 * @param pageno
	 * @param pagesize
	 * @param year
	 * @param quarterly
	 * @param company_name
	 * @return Page<Record> result
	 * @desc 查询缴费记录
	 */
	public static Page<Record> getPayment(Integer pageno,int pagesize,Integer year,String quarterly,String company_name){
		String sqlExceptSelect ="from t_payment p LEFT JOIN t_enterprise_in e ON p.company_id=e.id where 1=1";
		if(year!=null && year!=0){
			sqlExceptSelect +=" and p.year="+year;
		}
		if(quarterly!=null&& !quarterly.equals("选择季度")){
			sqlExceptSelect +=" and p.quarterly='"+quarterly+"'";
		}
		if(company_name!=null&&company_name!=""){
			sqlExceptSelect +=" and p.company_name like '%"+company_name+"%'";
		}
		sqlExceptSelect += " ORDER BY p.id";
		return Db.paginate(pageno, pagesize, "select p.*,e.in_time ", sqlExceptSelect);	
	}
	
	/**
	 * @param id
	 * @return Record result
	 * @desc 根据id查询单个缴费记录
	 */
	public static Record getPayment(int id){
		return Db.findFirst("select * from t_payment where id =?", id);
	}
	
	/**
	 * @param id
	 * @param year
	 * @param quarterly
	 * @param company_id
	 * @param company_name
	 * @param should_pay_rent
	 * @param paid_rent
	 * @param property_costs
	 * @param paid_property_charges
	 * @param should_pay_water
	 * @param real_water_fee
	 * @return boolean result
	 * @desc 修改以及添加缴费管理 
	 */
	public static boolean savePayment(Integer id,String year
			,String quarterly,Integer company_id,String company_name,double should_pay_rent
			,double paid_rent,double property_costs,double paid_property_charges
			,double should_pay_water,double real_water_fee){
		Record record = new Record();
		record.set("year", year);
		record.set("quarterly", quarterly);
		record.set("company_id", company_id);
		record.set("company_name", company_name);
		record.set("should_pay_rent", should_pay_rent);
		record.set("paid_rent", paid_rent);
		record.set("property_costs", property_costs);
		record.set("paid_property_charges", paid_property_charges);
		record.set("should_pay_water", should_pay_water);
		record.set("real_water_fee", real_water_fee);
		record.set("modify_time", new Date());
		if(id!=null){
			record.set("id", id);
			return Db.update("t_payment", record);
		}else{
			record.set("create_time", new Date());
			return Db.save("t_payment", record);
		}
	}
	
	/**
	 * @param id
	 * @return boolean result
	 * @desc 根据id删除缴费信息
	 */
	public static boolean delPay(int id){
		return Db.deleteById("t_payment", id);
	}
	
	/***********************安全管理检查记录************************/
	/**
	 * @param pageno
	 * @param pagesize
	 * @param sdate
	 * @param rectification
	 * @param company_name
	 * @return Page<Record> result
	 * @desc 安全管理检查记录
	 */
	public static Page<Record> getSafeInspect(Integer pageno,int pagesize,String sdate
											,Integer rectification,String company_name){
		String sqlExceptSelect = "from t_safety_inspection s LEFT JOIN t_enterprise_in e on s.company_id = e.id where 1=1";
		boolean is_rectification = false;
		if(company_name!=null && company_name!=""){
			sqlExceptSelect += " and company_name like '%"+company_name+"%'";
		}
		//rectification==1表示选择'已检查',rectification==0表示选择'未检查',rectification==9表示未选择;
		if(rectification!=null&&rectification!=9){
			if(rectification==1){
				is_rectification = true;
				sqlExceptSelect +=" and is_rectification="+ is_rectification;
			}else{
				is_rectification = false;
				sqlExceptSelect +=" and is_rectification="+ is_rectification;
			}
		}
		if(sdate!=null&&sdate!=""){
			sqlExceptSelect +=" and DATE_FORMAT(check_time,'%Y-%m')='"+sdate+"'";
		}
		sqlExceptSelect += " ORDER BY s.id";
		return Db.paginate(pageno, pagesize,"select s.*,e.representative,e.contact",sqlExceptSelect);
	}
	
	/**
	 * @param id
	 * @return Record result
	 * @desc 根据id查询单个安全管理检查记录
	 */
	public static Record getSafeRecord(int id){
		return Db.findFirst("select * from t_safety_inspection where id =?",id);
	}
	
	/**
	 * @param id
	 * @param company_id
	 * @param company_name
	 * @param check_time
	 * @param examiner
	 * @param rectification_time
	 * @param rectification_man
	 * @param supervision_personnel
	 * @param hidden_danger
	 * @param performance
	 * @param remarks
	 * @param is_rectification
	 * @return boolean result
	 * @desc 修改以及添加安全检查记录
	 */
	public static boolean saveSafetyRecord(Integer id,int company_id
			,String company_name,Date check_time,String examiner
			,Date rectification_time,String rectification_man
			,String supervision_personnel,String hidden_danger
			,String performance,String remarks,int is_rectification){
		Record record = new Record();
		record.set("company_id", company_id);
		record.set("company_name", company_name);
		record.set("check_time", check_time);
		record.set("examiner", examiner);
		record.set("rectification_time", rectification_time);
		record.set("rectification_man", rectification_man);
		record.set("supervision_personnel", supervision_personnel);
		record.set("hidden_danger", hidden_danger);
		record.set("performance", performance);
		record.set("remarks", remarks);
		record.set("is_rectification", is_rectification);
		record.set("modify_time", new Date());
		if(id!=null){
			record.set("id", id);
			return Db.update("t_safety_inspection", record);
		}else{
			record.set("create_time", new Date());
			return Db.save("t_safety_inspection", record);
		}
	}
	
	/**
	 * @param id
	 * @return boolean result
	 * @desc 根据id删除安全管理检查记录
	 */
	public static boolean delSaveRecord(int id){
		return Db.deleteById("t_safety_inspection", id);
	}
	/**
	 * @param id
	 * @return result
	 * @desc 根据id查询单个公司
	 */
	public static Record getCompanyName(int id){
		return Db.findFirst("select * from t_enterprise_in where id=?", id);
	}
	/***********************园区安全责任书签订************************/
	/**
	 * @param pageno
	 * @param pagesize
	 * @return Page<Record> result
	 * @desc 园区安全责任书签订
	 */
	public static Page<Record> getSafeProtocols(Integer pageno,int pagesize){
		return Db.paginate(pageno, pagesize, "select p.*,e.enterprise_name", 
				"from t_security_protocols p left join t_enterprise_in e on p.company_id = e.id order by p.id");
	}
	
	/**
	 * @param id
	 * @return Record result
	 * @desc 根据id查询单个园区安全责任书签订情况
	 */
	public static Record getSafeArgree(int id){
		return Db.findFirst("select * from t_security_protocols where id=?",id);
	}
	
	/**
	 * @param id
	 * @return boolean result
	 * @desc 根据id删除安全责任书签订情况
	 */
	public static boolean delSafeAgree(int id){
		return Db.deleteById("t_security_protocols", id);
	}
	
	/**
	 * @param id
	 * @param company_id
	 * @param status
	 * @param attachment
	 * @return boolean result
	 * @desc 添加以及修改安全责任书签订情况
	 */
	public static boolean saveSafeAgree(Integer id,Integer company_id,boolean status,String attachment){
		Record record = new Record();
		record.set("company_id", company_id);
		record.set("status", status);
		record.set("attachment", attachment);
		if(id!=null){
			record.set("id", id);
			return Db.update("t_security_protocols", record);
		}else{
			record.set("create_time", new Date());
			return Db.save("t_security_protocols", record);
		}
	}
	
	/**
	 * @param response
	 * @param id
	 * @throws IOException
	 * @desc 文件下载
	 */
	public static void downloadFile(HttpServletResponse response, Integer id) throws IOException {
/*		byte[] buffer = new byte[4096];
		Record file = Db.findById("t_security_protocols", id);
		Integer company_id = file.getInt("company_id");
		String company_name = Db.findById("t_enterprise_in", company_id).getStr("enterprise_name");
		String ZipName = company_name+"安全协议签订相关附件" + ".rar";
		response.setContentType("application/octet-stream");
		response.setHeader("Content-Disposition", "attachment; filename=" + EncordUtil.toUtf8String(ZipName));
		ZipOutputStream out = new ZipOutputStream(response.getOutputStream());
		String file_url = file.getStr("attachment");
		String[] farr = file_url.split(",");
		File[] fs = new File[farr.length];
		for (int i = 0; i < farr.length; i++) {
			fs[i] = new File(PropKit.get("filepath") + farr[i]);
		}   
		for (int j = 0; j < fs.length; j++) {
			FileInputStream fis = new FileInputStream(fs[j]);
			out.putNextEntry(new ZipEntry(fs[j].getName()));
			int len;
			// 读入需要下载的文件的内容，打包到zip文件
			while ((len = fis.read(buffer)) > 0) {
				out.write(buffer, 0, len);
			}
			out.closeEntry();
			fis.close();
		}
		out.close();*/
	}
	/***********************通知公告************************/
	/**
	 * @param pageno
	 * @param pagesize
	 * @return Page<Record> result
	 * @desc 查询通知公告记录
	 */
	public static Page<Record> getNotice(Integer pageno,int pagesize){
		return Db.paginate(pageno, pagesize, "select *", "from t_notice");
	}
	
	/**
	 * @param id
	 * @return Record result
	 * @desc 根据id查询单个通知公告
	 */
	public static Record getsingleNotice(int id){
		return Db.findFirst("select * from t_notice where id = ?",id);
	}
	
	/**
	 * @return List<Record> result
	 * @desc 获取所有通知公告显示，list显示
	 */
	public static List<Record> getNoticeList(){
		return Db.find("select * from t_notice");
	}
	
	/**
	 * @param id
	 * @param inputman
	 * @param title
	 * @param content
	 * @return boolean result
	 * @desc 添加以及修改通知公告
	 */
	public static boolean saveNotice(Integer id,String inputman,String title,String content){
		Record record = new Record();
		record.set("title", title);
		record.set("content", content);
		record.set("inputman", inputman);
		record.set("modify_time", new Date());
		if(id!=null){
			record.set("id", id);
			return Db.update("t_notice", record);
		}else{
			record.set("create_time", new Date());
			return Db.save("t_notice", record);
		}
		
	}
	
	/**
	 * @param id
	 * @return boolean result
	 * @desc 根据id删除通知公告
	 */
	public static boolean delNotice(int id){
		return Db.deleteById("t_notice", id);
	}
		
	/**
	 * @param response
	 * @param sdate
	 * @param rectification
	 * @param company_name
	 * @return boolean result
	 * @desc 导出安全管理检查记录列表excel表格
	 */
	public static boolean getSafeRecordForExcel(HttpServletResponse response,String sdate,Integer rectification,String company_name ){
	/*	List<Record> list = ParkManageService.getSafeInspect(1, 1000000, sdate,rectification, company_name).getList();
		HSSFWorkbook wbook = new HSSFWorkbook();//创建一个workbook对应一个excel
		HSSFSheet sheet = wbook.createSheet();//创建一个sheet
		wbook.setSheetName(0, "软件园安全隐患排查登记表", (short)1);
		sheet.addMergedRegion(new Region(0, (short)0 , 0, (short)11));
		
		//设置列宽
		sheet.setColumnWidth((short)0, (short)2000);//序列
		sheet.setColumnWidth((short)1, (short)5000);//被检查单位
		sheet.setColumnWidth((short)2, (short)5000);//法定代表人
		sheet.setColumnWidth((short)3, (short)6000);//电话
		sheet.setColumnWidth((short)4, (short)8000);//安全生产工作检查
		sheet.setColumnWidth((short)5, (short)5000);//检查时间
		sheet.setColumnWidth((short)6, (short)5000);//检查人
		sheet.setColumnWidth((short)7, (short)8000);//隐患内容
		sheet.setColumnWidth((short)8, (short)8000);//落实情况
		sheet.setColumnWidth((short)9, (short)5000);//整改时间
		sheet.setColumnWidth((short)10, (short)5000);//整改责任人
		sheet.setColumnWidth((short)11, (short)5000);//督查责任人

        HSSFCellStyle cellStyle = wbook.createCellStyle();
        cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        HSSFFont font = wbook.createFont();
        font.setFontName("黑体");
        font.setFontHeightInPoints((short) 20);//设置字体大小    
        cellStyle.setFont(font);
        cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框    
        cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框 
        
        HSSFCellStyle cellBorder = wbook.createCellStyle();
        cellBorder.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        cellBorder.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框    
        cellBorder.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框    
        cellBorder.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框    
        cellBorder.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框 
        
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdfe = new SimpleDateFormat("yyyy年MM");
        String date = sdfe.format(new Date()).substring(0, 7);
        
        HSSFRow row;
        for(int i=0;i<list.size();i++){//遍历list，将list数据放入excel中
        	if(i==0){
        		row = sheet.createRow(i);
				HSSFCell cell0 = row.createCell((short)0);
				cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0.setCellStyle(cellStyle);//第0行采用的样式
				cell0.setCellValue("软件园安全隐患"+date+"月排查登记表");//第0行的内容，即标题
        	}
        	if(i==0){
        		row = sheet.createRow(1);
        		HSSFCell cell0 = row.createCell((short)0);
        		cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
        		cell0.setCellStyle(cellBorder);
        		cell0.setCellValue("序号");

        		row = sheet.createRow(1);
        		HSSFCell cell1 = row.createCell((short)1);
        		cell1.setEncoding(HSSFCell.ENCODING_UTF_16);
        		cell1.setCellStyle(cellBorder);
        		cell1.setCellValue("被检查单位");
        		
        		row = sheet.createRow(1);
        		HSSFCell cell2 = row.createCell((short)2);
        		cell2.setEncoding(HSSFCell.ENCODING_UTF_16);
        		cell2.setCellStyle(cellBorder);
        		cell2.setCellValue("法定代表人");
        		
        		row = sheet.createRow(1);
        		HSSFCell cell3 = row.createCell((short)3);
        		cell3.setEncoding(HSSFCell.ENCODING_UTF_16);
        		cell3.setCellStyle(cellBorder);
        		cell3.setCellValue("电话");
        		
        		row = sheet.createRow(1);
        		HSSFCell cell4 = row.createCell((short)4);
        		cell4.setEncoding(HSSFCell.ENCODING_UTF_16);
        		cell4.setCellStyle(cellBorder);
        		cell4.setCellValue("安全生产工作检查");
        		
        		row = sheet.createRow(1);
        		HSSFCell cell5 = row.createCell((short)5);
        		cell5.setEncoding(HSSFCell.ENCODING_UTF_16);
        		cell5.setCellStyle(cellBorder);
        		cell5.setCellValue("检查时间");
        		
        		row = sheet.createRow(1);
        		HSSFCell cell6 = row.createCell((short)6);
        		cell6.setEncoding(HSSFCell.ENCODING_UTF_16);
        		cell6.setCellStyle(cellBorder);
        		cell6.setCellValue("检查人");
        		
        		row = sheet.createRow(1);
        		HSSFCell cell7 = row.createCell((short)7);
        		cell7.setEncoding(HSSFCell.ENCODING_UTF_16);
        		cell7.setCellStyle(cellBorder);
        		cell7.setCellValue("隐患内容");
        			
        		row = sheet.createRow(1);
        		HSSFCell cell8 = row.createCell((short)8);
        		cell8.setEncoding(HSSFCell.ENCODING_UTF_16);
        		cell8.setCellStyle(cellBorder);
        		cell8.setCellValue("落实情况");
        		
        		row = sheet.createRow(1);
        		HSSFCell cell9 = row.createCell((short)9);
        		cell9.setEncoding(HSSFCell.ENCODING_UTF_16);
        		cell9.setCellStyle(cellBorder);
        		cell9.setCellValue("整改时间");
        		
        		row = sheet.createRow(1);
        		HSSFCell cell10 = row.createCell((short)10);
        		cell10.setEncoding(HSSFCell.ENCODING_UTF_16);
        		cell10.setCellStyle(cellBorder);
        		cell10.setCellValue("整改责任人");
        		
        		row = sheet.createRow(1);
        		HSSFCell cell11 = row.createCell((short)11);
        		cell11.setEncoding(HSSFCell.ENCODING_UTF_16);
        		cell11.setCellStyle(cellBorder);
        		cell11.setCellValue("督查责任人");
        	}
			row = sheet.createRow(i+2);//从第三行开始
			Record r = list.get(i);
			
			HSSFCell cell0 = row.createCell((short)0);
			cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
			cell0.setCellStyle(cellBorder);
			cell0.setCellValue(i+1);
			
			HSSFCell cell1 = row.createCell((short)1);
			cell1.setEncoding(HSSFCell.ENCODING_UTF_16);
			cell1.setCellStyle(cellBorder);
			cell1.setCellValue(r.getStr("company_name"));
			
			HSSFCell cell2 = row.createCell((short)2);
			cell2.setEncoding(HSSFCell.ENCODING_UTF_16);
			cell2.setCellStyle(cellBorder);
			cell2.setCellValue(r.getStr("representative"));
			
			HSSFCell cell3 = row.createCell((short)3);
			cell3.setEncoding(HSSFCell.ENCODING_UTF_16);
			cell3.setCellStyle(cellBorder);
			cell3.setCellValue(r.getStr("contact"));
			
			HSSFCell cell4 = row.createCell((short)4);
			cell4.setEncoding(HSSFCell.ENCODING_UTF_16);
			cell4.setCellStyle(cellBorder);
			cell4.setCellValue(r.getBoolean("is_rectification")==true?"已检查":"未检查");
			
			HSSFCell cell5 = row.createCell((short)5);
			cell5.setEncoding(HSSFCell.ENCODING_UTF_16);
			cell5.setCellStyle(cellBorder);
			cell5.setCellValue(sdf.format(r.getDate("rectification_time")));
			
			HSSFCell cell6 = row.createCell((short)6);
			cell6.setEncoding(HSSFCell.ENCODING_UTF_16);
			cell6.setCellStyle(cellBorder);
			cell6.setCellValue(r.getStr("examiner"));
			
			HSSFCell cell7 = row.createCell((short)7);
			cell7.setEncoding(HSSFCell.ENCODING_UTF_16);
			cell7.setCellStyle(cellBorder);
			cell7.setCellValue(r.getStr("hidden_danger"));
			
			HSSFCell cell8 = row.createCell((short)8);
			cell8.setEncoding(HSSFCell.ENCODING_UTF_16);
			cell8.setCellStyle(cellBorder);
			cell8.setCellValue(r.getStr("performance"));
			
			HSSFCell cell9 = row.createCell((short)9);
			cell9.setEncoding(HSSFCell.ENCODING_UTF_16);
			cell9.setCellStyle(cellBorder);
			cell9.setCellValue( sdf.format(r.getDate("create_time")));
        	
			HSSFCell cell10 = row.createCell((short)10);
			cell10.setEncoding(HSSFCell.ENCODING_UTF_16);
			cell10.setCellStyle(cellBorder);
			cell10.setCellValue(r.getStr("rectification_man"));
			
			HSSFCell cell11 = row.createCell((short)11);
			cell11.setEncoding(HSSFCell.ENCODING_UTF_16);
			cell11.setCellStyle(cellBorder);
			cell11.setCellValue(r.getStr("supervision_personnel"));
        }//循环结束
		response.addHeader("Content-Disposition", "attachment;filename=" + EncordUtil.toUtf8String("安全管理检查"+date+"月记录统计")+ ".xls");
		response.setContentType("application/vnd.ms-excel;charset=utf-8");
		try {
			ServletOutputStream out = response.getOutputStream();
			wbook.write(out);
			out.flush();
			out.close();	
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		return true;
	}
	
	/**
	 * @param response
	 * @param request
	 * @param id
	 * @throws IOException
	 * @desc 导出安全管理检查记录的word
	 */
	public static void excWord(HttpServletResponse response, HttpServletRequest request, Integer id)
			throws IOException {
		/*Map<String, Object> dataMap = new HashMap<>();
		Record safetyrecord = getSafeRecord(id);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		dataMap.put("company_name", safetyrecord.getStr("company_name"));
		dataMap.put("checktime", sdf.format(safetyrecord.getDate("check_time")));
		dataMap.put("examiner", safetyrecord.getStr("examiner"));
		dataMap.put("hidden_danger", safetyrecord.getStr("hidden_danger"));
		dataMap.put("performance", safetyrecord.getStr("performance"));
		DocUtil.toSafe(request, DocUtil.WORD_TEEPLATE, dataMap);//将map传递
		try {
			File saferecord = new File(request.getSession().getServletContext().getRealPath(DocUtil.SAFE_DOC));
			InputStream is = new FileInputStream(saferecord);
			response.reset();
			response.setContentType("application/vnd.ms-word;charset=UTF-8");
			response.addHeader("Content-Disposition", "attachment; filename="
					+ EncordUtil.toUtf8String("海安软件园安全生产检查记录-" + safetyrecord.getStr("company_name")) + ".doc");
			byte[] b = new byte[1024];
			int len;
			while ((len = is.read(b)) > 0) {
				response.getOutputStream().write(b, 0, len);
			}
			response.getOutputStream().flush();
			response.getOutputStream().close();
			is.close();
		} catch (Exception e) {
			e.printStackTrace();
		}*/
	}
}
