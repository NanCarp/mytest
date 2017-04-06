package mytest.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.servlet.http.HttpServletResponse;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import morality.util.tool.EncordUtil;

/**
 * @ClassName:FileManageService
 * @Description:文件传输
 * @author:yangbo
 * @date:2017年3月24日
 */
public class FileManageService {

	/************************文件传阅管理*****************************/
	/**
	 * @author xuhui 
	 * @param pageno
	 * @param pageSize
	 * @param rid
	 * @return Page<Record> result
	 * @desc 获得文件传阅列表
	 */
	public static Page<Record> getWjcyList(Integer pageno, int pageSize,Integer rid) {
		String sqlExceptSelect = " FROM t_file a LEFT JOIN t_employee b ON a.uploader = b.id";
			sqlExceptSelect += " where FIND_IN_SET("+rid+",recipient) or a.uploader="+rid;
		return Db.paginate(pageno, pageSize, "SELECT a.id,a.recipient, a.file_name, a.file_url,b.name,a.modify_time",
				sqlExceptSelect);
	}

	/**
	 * @author yangbo
	 * @return List<Record> result
	 */
	// 获得园区主任
	public static List<Record> getParkheadInfo() {
		return Db.find(
				"SELECT id, name,9999 AS pid FROM t_employee UNION SELECT 9999 AS id, '海安科技园' AS name, 0 FROM DUAL");
	}

	/**
	 * @author yangbo 
	 * @param response
	 * @param id
	 * @throws IOException
	 * @desc 文件下载
	 */
	public static void downloadFile(HttpServletResponse response, Integer id) throws IOException {
		byte[] buffer = new byte[4096];
		Record file = Db.findById("t_file", id);
		String ZipName = file.getStr("file_name") + ".rar";
		response.setContentType("application/octet-stream");
		response.setHeader("Content-Disposition", "attachment; filename=" + EncordUtil.toUtf8String(ZipName));
		ZipOutputStream out = new ZipOutputStream(response.getOutputStream());
		String file_url = file.getStr("file_url");
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
		out.close();
	}
	
	/*********************** 项目申报管理 ************************/
	
	/**
	 * @author xuhui 
	 * @param pageno
	 * @param pagesize
	 * @param recipient_id
	 * @return Page<Record> result
	 * @desc 获得项目申报列表
	 */
	public static Page<Record> getProjectList(Integer pageno, Integer pagesize, Integer recipient_id) {
		String sqlExceptSelect = " FROM t_project a LEFT JOIN t_employee b ON a.uploader = b.id WHERE FIND_IN_SET("+recipient_id+",recipient) "
				+ "OR uploader= "+recipient_id;
		return Db.paginate(pageno, pagesize, "SELECT a.id,a.recipient, a.project_name, a.file_url,b.name,a.modify_time",
				sqlExceptSelect);
	}

	/**
	 * @author yangbo 
	 * @param response
	 * @param id
	 * @throws IOException
	 * @desc 项目下载
	 */
	public static void downloadProject(HttpServletResponse response, Integer id) throws IOException {
		byte[] buffer = new byte[4096];
		Record file = Db.findById("t_project", id);
		String ZipName = file.getStr("project_name") + ".rar";
		response.setContentType("application/octet-stream");
		response.setHeader("Content-Disposition", "attachment; filename=" + EncordUtil.toUtf8String(ZipName));
		ZipOutputStream out = new ZipOutputStream(response.getOutputStream());
		String file_url = file.getStr("file_url");
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
		out.close();
	}
}

