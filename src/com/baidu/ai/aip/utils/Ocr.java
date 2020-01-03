package com.baidu.ai.aip.utils;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONObject;

import com.baidu.ai.api.auth.AuthService;
import com.baidu.aip.util.Base64Util;


public class Ocr {
	public static void main(String[] args) throws IOException {
	
		/*通用文字识别*/
//        String general_basicUrl = "https://aip.baidubce.com/rest/2.0/ocr/v1/general_basic";
//        String general_basicFilePath = "C:/Users/Administrator/Desktop/general_basic.jpg";
//        JSONObject jsonObject = ocr(general_basicUrl,general_basicFilePath);
		
		/*身份证识别*/
//        String idcardUrl = "https://aip.baidubce.com/rest/2.0/ocr/v1/idcard";
//        String idcardFilePath = "C:/Users/Administrator/Desktop/idcard.jpg";
//        JSONObject jsonObject = ocr(idcardUrl,idcardFilePath);
		
        /*通用发票识别*/
        String receiptUrl = "https://aip.baidubce.com/rest/2.0/ocr/v1/receipt";
        String receiptFilePath = "C:/Users/Administrator/Desktop/receipt.jpg";
        JSONObject jsonObject = ocr(receiptUrl,receiptFilePath);
		
        /*车牌识别*/
//        String license_plateUrl = "https://aip.baidubce.com/rest/2.0/ocr/v1/license_plate";
//        String license_plateFilePath = "C:/Users/Administrator/Desktop/license_plate.jpg";
//        JSONObject jsonObject = ocr(license_plateUrl,license_plateFilePath);
        
        System.out.println(jsonObject);
        
        /*通用文字*/
//        String words = jsonObject.get("words_result").toString();
//        //正则匹配
//        Matcher m = Pattern.compile("words\":\"(.*?)\"}").matcher(words);
//        while (m.find( )) {
//            System.out.println(m.group(1) );
//         }
        
        /*身份证*/
//        JSONObject idcard_json=new JSONObject(jsonObject.get("words_result").toString());
//        System.out.println(new JSONObject(idcard_json.get("姓名").toString()).get("words"));
//        System.out.println(new JSONObject(idcard_json.get("民族").toString()).get("words"));
//        System.out.println(new JSONObject(idcard_json.get("住址").toString()).get("words"));
//        System.out.println(new JSONObject(idcard_json.get("公民身份号码").toString()).get("words"));
//        System.out.println(new JSONObject(idcard_json.get("出生").toString()).get("words"));
//        System.out.println(new JSONObject(idcard_json.get("性别").toString()).get("words"));
        
        /*通用发票*/
      String words = jsonObject.get("words_result").toString();
      //正则匹配
      Matcher m = Pattern.compile("words\":\"(.*?)\",\"").matcher(words);
      JSONObject jo= new JSONObject();
      while (m.find( )) {
			if(m.group(1).indexOf("代码") != -1){
				jo.put("dm", m.group(1).substring(4));
			}
			if(m.group(1).indexOf("号码") != -1){
				jo.put("hm", m.group(1).substring(4));
			}
			  
			if(m.group(1).indexOf("日期") != -1){  
				jo.put("rq", m.group(1).substring((m.group(1).indexOf(":")+1)));
			}
			if(m.group(1).indexOf("付款单位名称") != -1 || m.group(1).indexOf("付款方") != -1){
				jo.put("fkf", m.group(1).substring((m.group(1).indexOf(":")+1)));
			}	
			if(m.group(1).indexOf("收款单位名称") != -1 || m.group(1).indexOf("收款方") != -1){
				jo.put("skf", m.group(1).substring((m.group(1).indexOf(":")+1)));
			}
			if(m.group(1).indexOf("收款单位税号") != -1 || m.group(1).indexOf("税号") != -1){
				jo.put("rh", m.group(1).substring((m.group(1).indexOf(":")+1)));
			}
			if(m.group(1).indexOf("合计") != -1){
				jo.put("hj", m.group(1).substring((m.group(1).indexOf(":")+1)));
			}	  
	    	if(m.group(1).indexOf("开票人") != -1){		   
	    		jo.put("kpr", m.group(1).substring((m.group(1).indexOf(":")+1)));
	    	}		  
       }
      System.out.println(jo);
        
      /*车牌*/
//      JSONObject license_plate_json=new JSONObject(jsonObject.get("words_result").toString());
//      System.out.println(license_plate_json.get("number"));
//      System.out.println(license_plate_json.get("color"));
	}
	
	/*文字识别接口*/
    public static JSONObject ocr(String url,String filePath) {
        try {
            byte[] imgData = FileUtil.readFileByBytes(filePath);
            String imgStr = Base64Util.encode(imgData);
            String imgParam = URLEncoder.encode(imgStr, "UTF-8");

            String param = "id_card_side=" + "front" + "&image=" + imgParam;

            // 注意这里仅为了简化编码每一次请求都去获取access_token，线上环境access_token有过期时间， 客户端可自行缓存，过期后重新获取。
            String accessToken = AuthService.getAuth();

            String result = HttpUtil.post(url, accessToken, param);
            JSONObject jsonObject = new JSONObject(result); 
            
            return jsonObject;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
