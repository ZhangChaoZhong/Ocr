package com.ocr.servlet;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.json.JSONObject;

import com.baidu.ai.aip.utils.Ocr;
import java.util.List;

import sun.net.www.content.audio.aiff;


/**
 * Servlet implementation class OcrServlet
 */
@WebServlet("/OcrServlet")
public class OcrServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public OcrServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		JSONObject resultObject = new JSONObject();
		String method = request.getParameter("method");
		if(method.equals("upload") ){
			uploadPhoto(request);
		}
		if(method.equals("startRecognition") )
		{	
			String key = request.getParameter("current");
			String words_general_basic="";
			switch (key) {
				case "1":	//通用文字识别
					{
				        String general_basicUrl = "https://aip.baidubce.com/rest/2.0/ocr/v1/general_basic";
				        //String general_basicFilePath = "C:/Users/Administrator/Desktop/general_basic.jpg";
				        String general_basicFilePath = "/usr/local/tomcat/apache-tomcat-7.0.93/webapps/Ocr/imgs/ai.jpg";
				        JSONObject jsonObject = Ocr.ocr(general_basicUrl,general_basicFilePath);
				        
				        String words = jsonObject.get("words_result").toString();
				        //正则匹配
				        Matcher m = Pattern.compile("words\":\"(.*?)\"}").matcher(words);
				        while (m.find( )) {
				        	words_general_basic=words_general_basic + m.group(1)+"<br>";
				         }
				        resultObject.put("words_general_basic", words_general_basic);	
					}
					break;
				case "2":	//身份证识别
					{
				        String idcardUrl = "https://aip.baidubce.com/rest/2.0/ocr/v1/idcard";
				        //String idcardFilePath = "C:/Users/Administrator/Desktop/idcard.jpg";
				        String idcardFilePath =  "/usr/local/tomcat/apache-tomcat-7.0.93/webapps/Ocr/imgs/ai.jpg";
				        JSONObject jsonObject = Ocr.ocr(idcardUrl,idcardFilePath);
				        
				        JSONObject idcard_json=new JSONObject(jsonObject.get("words_result").toString());		  
						if(idcard_json.get("姓名") != null){
							resultObject.put("xm", new JSONObject(idcard_json.get("姓名").toString()).get("words"));
						}else{
							resultObject.put("xm", "识别失败");
						}
						if(idcard_json.get("民族") != null){
							resultObject.put("mz", new JSONObject(idcard_json.get("民族").toString()).get("words"));
						}else{
							resultObject.put("mz", "识别失败");
						}
						if(idcard_json.get("住址") != null){
							resultObject.put("zz", new JSONObject(idcard_json.get("住址").toString()).get("words"));
						}else{
							resultObject.put("zz", "识别失败");
						}
						if(idcard_json.get("公民身份号码") != null){
							resultObject.put("sfz", new JSONObject(idcard_json.get("公民身份号码").toString()).get("words"));
						}else{
							resultObject.put("sfz", "识别失败");
						}
						if(idcard_json.get("出生") != null){
							resultObject.put("cs", new JSONObject(idcard_json.get("出生").toString()).get("words"));
						}else{
							resultObject.put("cs", "识别失败");
						}
						if(idcard_json.get("性别") != null){
							resultObject.put("xb", new JSONObject(idcard_json.get("性别").toString()).get("words"));
						}else{
							resultObject.put("xb", "识别失败");
						}		
					}   
					break;
				case "3":	//通用发票识别
					{
				        String receiptUrl = "https://aip.baidubce.com/rest/2.0/ocr/v1/receipt";
				        //String receiptFilePath = "C:/Users/Administrator/Desktop/receipt.jpg";
				        String receiptFilePath =  "/usr/local/tomcat/apache-tomcat-7.0.93/webapps/Ocr/imgs/ai.jpg";
				        JSONObject jsonObject = Ocr.ocr(receiptUrl,receiptFilePath);
				        
					    String words = jsonObject.get("words_result").toString();
					    //正则匹配
					    Matcher m = Pattern.compile("words\":\"(.*?)\",\"").matcher(words);
					    while (m.find( )) {
							if(m.group(1).indexOf("代码") != -1){
								resultObject.put("dm", m.group(1).substring(4));
							}
							if(m.group(1).indexOf("号码") != -1){
								resultObject.put("hm", m.group(1).substring(4));
							}  
							if(m.group(1).indexOf("日期") != -1){  
								resultObject.put("rq", m.group(1).substring((m.group(1).indexOf(":")+1)));
							}
							if(m.group(1).indexOf("付款单位名称") != -1 || m.group(1).indexOf("付款方") != -1){
								resultObject.put("fkf", m.group(1).substring((m.group(1).indexOf(":")+1)));
							}	
							if(m.group(1).indexOf("收款单位名称") != -1 || m.group(1).indexOf("收款方") != -1){
								resultObject.put("skf", m.group(1).substring((m.group(1).indexOf(":")+1)));
							}
							if(m.group(1).indexOf("收款单位税号") != -1 || m.group(1).indexOf("税号") != -1){
								resultObject.put("sh", m.group(1).substring((m.group(1).indexOf(":")+1)));
							}
							if(m.group(1).indexOf("合计") != -1){
								resultObject.put("hj", m.group(1).substring((m.group(1).indexOf(":")+1)));
							}	  
					    	if(m.group(1).indexOf("开票人") != -1){		   
					    		resultObject.put("kpr", m.group(1).substring((m.group(1).indexOf(":")+1)));
					    	}
				         }
					}
					break;
				case "4":	//车牌识别
				{
				      String license_plateUrl = "https://aip.baidubce.com/rest/2.0/ocr/v1/license_plate";
				      //String license_plateFilePath = "C:/Users/Administrator/Desktop/license_plate.jpg";
				      String license_plateFilePath = "/usr/local/tomcat/apache-tomcat-7.0.93/webapps/Ocr/imgs/ai.jpg";
				      JSONObject jsonObject =  Ocr.ocr(license_plateUrl,license_plateFilePath);
				      
				      JSONObject license_plate_json=new JSONObject(jsonObject.get("words_result").toString());
				      resultObject.put("number",license_plate_json.get("number"));
				      resultObject.put("color",license_plate_json.get("color"));
				}
					break;
				default:
					break;
			}
		}
		resultObject.put("status", "true");
		response.setContentType("application/json;charset=utf-8;");
		response.setCharacterEncoding("utf-8");	
		response.getWriter().print(resultObject);
	}
	
	public void uploadPhoto(HttpServletRequest request) throws UnsupportedEncodingException{
		//获取文件需要上传到的路径
        String path ="/usr/local/tomcat/apache-tomcat-7.0.93/webapps/Ocr/imgs/";
		//String path ="C:/Users/Administrator/Desktop/";
		// 用上面传入的这个路径创建一个文件并检查文件是否存在，不存在就创建一个。
       File dir = new File(path);
       if (!dir.exists()) {
           dir.mkdir();
       }
       
       request.setCharacterEncoding("utf-8");  //设置编码
       //创建DiskFileItemFactory对象，设置缓冲区大小和临时文件目录
       DiskFileItemFactory factory = new DiskFileItemFactory();
       factory.setRepository(dir);
       factory.setSizeThreshold(1024 * 1024);
       //使用DiskFileItemFactory 对象创建ServletFileUpload对象
       ServletFileUpload upload = new ServletFileUpload(factory);
       try {
			// 调用ServletFileUpload.parseRequest方法解析request对象，得到一个保存了所有上传内容的List对象。
    	   @SuppressWarnings("unchecked")
		List<FileItem> list = upload.parseRequest(request);
           FileItem picture = null;
			// 对list进行迭代，每迭代一个FileItem对象，调用其isFormField方法判断是否是上传文件（false是上传文件的类型，true是普通表单类型）
           for (FileItem item : list) {
               //获取表单的属性名字
               String name = item.getFieldName();
               //如果获取的表单信息是普通的 文本 信息
               if (item.isFormField()) {
                   //获取用户具体输入的字符串
                   String value = item.getString();
                   request.setAttribute(name, value);
               } else {
            	   picture = item;
               }
           }

           //真正写到磁盘上
           File file = new File(path+"ai.jpg");
           OutputStream out = new FileOutputStream(file);
           InputStream in = picture.getInputStream();
           int length = 0;
           byte[] buf = new byte[1024];
           // in.read(buf) 每次读到的数据存放在buf 数组中
           while ((length = in.read(buf)) != -1) {
               //在buf数组中取出数据写到（输出流）磁盘上
               out.write(buf, 0, length);
           }
           in.close();
           out.close();
       } catch (FileUploadException e1) {
           System.out.println(e1);
       } catch (Exception e) {
    	   System.out.println(e);
       }
	}
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
