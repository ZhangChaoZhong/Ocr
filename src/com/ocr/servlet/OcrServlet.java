package com.ocr.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.baidu.ai.aip.utils.Ocr;
import com.sun.xml.internal.bind.v2.schemagen.xmlschema.List;


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
		String method = request.getParameter("method");
		System.out.println(method);
		if(method == "upload" ){
			//System.out.println("LLLL");
		}
		else{
			JSONObject resultObject = new JSONObject();
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

			resultObject.put("status", "true");
			response.setContentType("application/json;charset=utf-8;");
			response.setCharacterEncoding("utf-8");	
			response.getWriter().print(resultObject);
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
