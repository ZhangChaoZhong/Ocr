<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html><head>
<meta http-equiv="content-type" content="text/html; charset=UTF-8">
<title>文字识别系统</title>
<link href="files/font-awesome.css" rel="stylesheet">
<link type="text/css" rel="stylesheet" href="css/style.css">
</head>

<body>
<div style="text-align:center;color:white;">
	<h1> 文字识别系统 </h1>
</div>

<!-- 选择本地图片 -->
<div class="file">选择本地图片
	<input type="file"  name="" id="input_file" onchange="selectImage(this);">
</div>

<!-- 提交加载条 -->
<div id="cisLoading">
	<img src="imgs/loading.gif" style="width:100%;height:100%" />
</div>


<!-- 选项区域 -->
<div class="inpit">
	<div id="inpitassembly" class="limitHeight">
		<!---<h3 class="t">请选择识别的图片</h3>--->
		<div class="li" checkbox >
			<div name="my_check_box"  value="general_basic" class="ack" onclick="general_basicClick()">
				<h3>通用文字</h3>
			</div>
			<div name="my_check_box"  value="idcard" onclick="idcardClick()">
				<h3>身份证</h3>
			</div>
			<div name="my_check_box" value="receipt" onclick="receiptClick()">
				<h3>通用发票</h3>
			</div>
			<div name="my_check_box" value="license_plate" onclick="license_plateClick()">
				<h3>车牌</h3>
			</div>
		</div>
	</div>
</div>	

<!-- 按钮区域 -->
 <div class="col-half">
   <div class="btn bubble left" onclick="readImage()">读取图片</div>
   <div class="btn bubble left" onclick="startRecognition()">开始识别</div>
 </div>

<!-- 识别图片区域 -->
<div style="float:left;width:50%;height:350px;margin-left:80px;margin-top:20px;">
	<img src="imgs/bg.jpg" id="recognition_image" style="border-radius:5px;width:100%;height:100%;overflow: hidden;">
</div>
	
<!-- 识别结果区域 -->
<div id="container">
	<div class="product-image">
		<!-- product Information-->
		<div class="info" id="general_basic">
			<h2>通用文字识别</h2>
			<ul>
				<li id="words_general_basic"><strong >待识别</strong></li>
			</ul>
		</div>
		
		<div class="info" style="display:none;" id="idcard">
			<h2>身份证识别</h2>
			<ul>
				<li id="xm"><strong>姓名：</strong></li>
				<li id="xb"><strong>性别：</strong></li>
				<li id="mz"><strong>民族：</strong></li>
				<li id="cs"><strong>出生：</strong></li>
				<li id="zz"><strong>住址：</strong></li>
				<li id="sfz"><strong>公民身份号码：</strong></li>
			</ul>
		</div>
		
		<div class="info" style="display:none;" id="receipt">
			<h2>通用发票识别</h2>
			<ul>
				<li id="dm"><strong>发票代码：</strong></li>
				<li id="hm"><strong>发票号码：</strong></li>
				<li id="rq"><strong>开票日期：</strong></li>
				<li id="hj"><strong>合计金额：</strong></li>
				<li id="fkf"><strong>付款单位名称：</strong></li>
				<li id="skf"><strong>收款单位名称：</strong></li>
				<li id="sh"><strong>收款单位税号：</strong></li>
				<li id="kpr"><strong>开票人：</strong></li>
			</ul>
		</div>
		
		<div class="info" style="display:none;" id="license_plate">
			<h2>车牌识别</h2>
			<ul>
				<li id="number"><strong>车牌号：</strong></li>
				<li id="color"><strong>颜色：</strong></li>
			</ul>
		</div>
	</div>
</div>

	
<script type="text/javascript" src="js/jquery.js"></script>
<script type="text/javascript" src="js/inpitassembly-2.js"></script>
<script src="js/highlight.js"></script>
<script>
	var current = {"current":'1'}; 
	var formData = new FormData();
	$(document).ready(function(){
		$("#cisLoading").hide();
		
		$("#inpitassembly").inpitassembly({
			selected:"ack",
			ischeck_:true,
			ischeck_class:false,
			max:function(e,o){
				alert("最大"+o)
			},
			min:function(e,o){
				alert("最小"+o)
			}
		});
	})

	function readImage(){
		$("#recognition_image").attr("src","imgs/ai.jpg"+"?r="+Math.random()); //后面加上随机数，防止缓存显示相同
//		if(current.current == "1"){
//			$("#recognition_image").attr("src","imgs/general_basic.jpg");
//		}else if(current.current == "2"){
//			$("#recognition_image").attr("src","imgs/idcard.jpg");
//		}else if(current.current == "3"){
//			$("#recognition_image").attr("src","imgs/receipt.jpg");
//		}else if(current.current == "4"){
//			$("#recognition_image").attr("src","imgs/license_plate.jpg");
//		}
	}
	
	function startRecognition(){

		$("#cisLoading").show();
		$.ajax({
			type:"POST",
			dataType:"json",
			data: current,
			url:"/Ocr/OcrServlet",
			success: function (result) {
			 	if (result.status == "true") {
                	console.log("SUCCESS");
                }else{
                	console.log("FAILURE");
                }
			
				if(current.current == "1"){
					document.getElementById("words_general_basic").innerHTML=result.words_general_basic;
				}else if(current.current == "2"){
					document.getElementById("xm").innerHTML="<strong>姓名：</strong>"+result.xm;
					document.getElementById("xb").innerHTML="<strong>性别：</strong>"+result.xb;
					document.getElementById("mz").innerHTML="<strong>民族：</strong>"+result.mz;
					document.getElementById("cs").innerHTML="<strong>出生：</strong>"+result.cs;
					document.getElementById("zz").innerHTML="<strong>住址：</strong>"+result.zz;
					document.getElementById("sfz").innerHTML="<strong>公民身份号码：</strong>"+result.sfz;

				}else if(current.current == "3"){
					document.getElementById("dm").innerHTML="<strong>发票代码：</strong>"+result.dm;
					document.getElementById("hm").innerHTML="<strong>发票号码：</strong>"+result.hm;
					document.getElementById("rq").innerHTML="<strong>开票日期：</strong>"+result.rq;
					document.getElementById("hj").innerHTML="<strong>合计金额：</strong>"+result.hj;
					document.getElementById("fkf").innerHTML="<strong>付款单位名称：</strong>"+result.fkf;
					document.getElementById("skf").innerHTML="<strong>收款单位名称：</strong>"+result.skf;
					document.getElementById("sh").innerHTML="<strong>收款单位税号：</strong>"+result.sh;
					document.getElementById("kpr").innerHTML="<strong>开票人：</strong>"+result.kpr;
				}else if(current.current == "4"){
					document.getElementById("number").innerHTML="<strong>车牌号：</strong>"+result.number;
					document.getElementById("color").innerHTML="<strong>颜色：</strong>"+result.color;
				}
				
               
            },
            error : function() {
                alert("识别失败！");
            },
            complete:function (XMLHttpRequest, status) {
            	$("#cisLoading").hide();
            }
		})
	}
	
	function general_basicClick(){
		current = {"current":'1'};
		obj = document.getElementById("general_basic");
		obj.style.display = "block";
		obj = document.getElementById("idcard");
		obj.style.display = "none";
		obj = document.getElementById("receipt");
		obj.style.display = "none";
		obj = document.getElementById("license_plate");
		obj.style.display = "none";
	}
	function idcardClick(){
		current = {"current":'2'};
		obj = document.getElementById("idcard");
		obj.style.display = "block";
		obj = document.getElementById("general_basic");
		obj.style.display = "none";
		obj = document.getElementById("receipt");
		obj.style.display = "none";
		obj = document.getElementById("license_plate");
		obj.style.display = "none";
	}
	function receiptClick(){
		current = {"current":'3'};
		obj = document.getElementById("receipt");
		obj.style.display = "block";
		obj = document.getElementById("general_basic");
		obj.style.display = "none";
		obj = document.getElementById("idcard");
		obj.style.display = "none";
		obj = document.getElementById("license_plate");
		obj.style.display = "none";
	}
	function license_plateClick(){
		current = {"current":'4'};
		obj = document.getElementById("license_plate");
		obj.style.display = "block";
		obj = document.getElementById("general_basic");
		obj.style.display = "none";
		obj = document.getElementById("idcard");
		obj.style.display = "none";
		obj = document.getElementById("receipt");
		obj.style.display = "none";
	}
	function selectImage(file) {
        if (!file.files || !file.files[0]) {
            return;
        }
        var reader = new FileReader();
        reader.onload = function (evt) {
            document.getElementById('recognition_image').src = evt.target.result;
            image = evt.target.result;
        }
        reader.readAsDataURL(file.files[0]);
    }
	
	
</script>
	
</body>
</html>