package mytest.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.kit.PathKit;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.upload.UploadFile;

import mytest.interceptor.ManageInterceptor;
import mytest.service.FileManageService;

/**
 * @ClassName:FileManageController
 * @Description:文件管理模块
 * @author xuhui
 * @data 2017年03月23日
 *
 */
@Before(ManageInterceptor.class)
public class FileManageController extends Controller {

	/*********************** 文件传阅管理 ************************/
	/**
	 * @desc 文件传阅管理
	 */
	public void filelist() {
		Record admin = getSessionAttr("admin");
		Integer rid = admin.getInt("role_id");
		Integer recipient_id = admin.getInt("id");// 登录者id，用于判断是否有权限
		String mopids = Db.queryStr("select module_power_id from t_role_permissions where role_id = ?", rid);
		if (mopids.indexOf("177") != -1) {
			setAttr("_add", true);
		}
		if (mopids.indexOf("178") != -1) {
			setAttr("_delete", true);
		}
		if (mopids.indexOf("179") != -1) {
			setAttr("_edit", true);
		}
		if (mopids.indexOf("180") != -1) {
			setAttr("_download", true);
		}
		Integer pageno = getParaToInt() == null ? 1 : getParaToInt();
		Page<Record> page = FileManageService.getWjcyList(pageno, 16, recipient_id);
		setAttr("pageno", page.getPageNumber());
		setAttr("totalpage", page.getTotalPage());
		setAttr("totalrow", page.getTotalRow());
		setAttr("wjcylist", page.getList());
		render("fileread.html");
	}

	/**
	 * @desc 添加以及修改文件传阅管理
	 */
	public void getFileRead() {
		Integer id = getParaToInt();
		Record record = Db.findById("t_file", id);
		if (record != null) {
			setAttr("file", record);
			setAttr("fid", record.getStr("recipient"));
			String file_url = record.getStr("file_url");
			setAttr("farr", file_url);
		}
		render("fileread_detail.html");
	}

	/**
	 * @desc 获得园区主任
	 */
	public void getParkhead() {
		renderJson(FileManageService.getParkheadInfo());
	}

	/**
	 * @desc 删除文件
	 */
	public void delFile() {
		Integer id = getParaToInt();
		boolean result = Db.deleteById("t_file", id);
		renderJson(result);
	}

	/**
	 * @desc 上传文件
	 */
	public void uploadfile() {
		// String path = new SimpleDateFormat("yyyy/MM/dd").format(new Date());
		String path = "";
		UploadFile file = getFile("file1");
		File source = file.getFile();
		String fileName = file.getFileName();
		String extension = fileName.substring(fileName.lastIndexOf("."));
		String prefix;
		prefix = "file";
		JSONObject json = new JSONObject();
		try {
			FileInputStream fis = new FileInputStream(source);
			File targetDir = new File(PathKit.getWebRootPath() + "/" + prefix + path);
			if (!targetDir.exists()) {
				targetDir.mkdirs();
			}
			File target = new File(targetDir, fileName);
			if (!target.exists()) {
				target.createNewFile();
			}
			FileOutputStream fos = new FileOutputStream(target);
			byte[] bts = new byte[1024];
			while ((fis.read(bts, 0, 1024)) != -1) {
				fos.write(bts, 0, 300);
			}
			fos.close();
			fis.close();
			json.put("eror", 0);
			json.put("url", "/" + prefix + path + fileName);
			source.delete();
		} catch (FileNotFoundException e) {
			json.put("error", 1);
			json.put("message", "文件上传出现错误，请稍后再上传");
		} catch (IOException e) {
			json.put("error", 1);
            json.put("message", "文件写入服务器出现错误，请稍后再上传");
		}
		
		
		renderJson(json.toJSONString());
	}

	/**
	 * @desc 保存文件
	 */
	public void saveFile() {
		boolean result = false;
		Integer id = getParaToInt("id");
		Record admin = getSessionAttr("admin");
		Record record = new Record();
		record.set("uploader", admin.getInt("id"));
		record.set("recipient", getPara("recipient"));
		record.set("file_name", getPara("file_name"));
		record.set("file_url", getPara("file_url"));
		if (id != null) {
			record.set("id", id);
			record.set("modify_time", new Date());
			result = Db.update("t_file", record);
		} else {
			record.set("create_time", new Date());
			record.set("modify_time", new Date());
			result = Db.save("t_file", record);
		}
		renderJson(result);
	}

	/**
	 * @throws IOException
	 * @desc 下载文件
	 */
	public void downloadFile() throws IOException {
		Integer id = getParaToInt();
		FileManageService.downloadFile(getResponse(), id);
		renderNull();
	}

	/*********************** 项目申报管理 ************************/
	/**
	 * @desc 项目申报管理
	 */
	public void projectDeclar() {
		// 验证权限
		Record admin = getSessionAttr("admin");
		Integer rid = admin.getInt("role_id");
		Integer recipient_id = admin.getInt("id");// 登录者id，用于判断是否有权限
		String mopids = Db.queryStr("select module_power_id from t_role_permissions where role_id = ?", rid);
		if (mopids.indexOf("181") != -1) {
			setAttr("_add", true);
		}
		if (mopids.indexOf("182") != -1) {
			setAttr("_delete", true);
		}
		if (mopids.indexOf("183") != -1) {
			setAttr("_edit", true);
		}
		if (mopids.indexOf("184") != -1) {
			setAttr("_download", true);
		}
		Integer pageno = getParaToInt() == null ? 1 : getParaToInt();
		Page<Record> page = FileManageService.getProjectList(pageno, 16, recipient_id);
		setAttr("pageno", page.getPageNumber());
		setAttr("totalpage", page.getTotalPage());
		setAttr("totalrow", page.getTotalRow());
		setAttr("projectlist", page.getList());
		render("projectdeclar.html");
	}

	/**
	 * @desc 添加项目申报管理
	 */
	public void getProject() {
		Integer id = getParaToInt();
		Record record = Db.findById("t_project", id);
		if (record != null) {
			setAttr("project", record);
			setAttr("fid", record.getStr("recipient"));
			String file_url = record.getStr("file_url");
			setAttr("farr", file_url);
		}
		render("projectdeclar_detail.html");
	}

	/**
	 * @desc 删除项目
	 */
	public void delProject() {
		Integer id = getParaToInt();
		boolean result = Db.deleteById("t_project", id);
		renderJson(result);
	}

	/**
	 * @desc 保存项目
	 */
	public void saveProject() {
		boolean result = false;
		Integer id = getParaToInt("id");
		Record admin = getSessionAttr("admin");
		Record record = new Record();
		record.set("uploader", admin.getInt("id"));
		record.set("recipient", getPara("recipient"));
		record.set("project_name", getPara("project_name"));
		record.set("file_url", getPara("file_url"));
		if (id != null) {
			record.set("id", id);
			record.set("modify_time", new Date());
			result = Db.update("t_project", record);
		} else {
			record.set("create_time", new Date());
			record.set("modify_time", new Date());
			result = Db.save("t_project", record);
		}
		renderJson(result);
	}

	/**
	 * @throws IOException
	 * @desc 下载项目
	 */
	public void downloadProject() throws IOException {
		Integer id = getParaToInt();
		FileManageService.downloadProject(getResponse(), id);
		renderNull();
	}
}
