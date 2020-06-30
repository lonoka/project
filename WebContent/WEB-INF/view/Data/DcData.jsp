<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<script src="/js/jquery-3.5.0.min.js"></script>
<script type="text/javascript">
	$(window).on("load",function(){
		getData();
	});
	
	function getData(){
		$.ajax({
			url : "/Data/getDcData.do",
			type : "post",
			dataType : "JSON",
			contentType : "application/json; charset=UTF-8",
			success : function(json){
				var Dc_Data = "";
				for(var i = 0; i<json.length; i++){
					Dc_Data += (json[i].title+" | ");
					Dc_Data += (json[i].writer+" | ");
					Dc_Data += (json[i].time+"<br>");
				}
				$("#Dc_Data").html(Dc_Data);
			}
		})
	}
</script>
</head>
<body>
	<h1>DC 컴본갤</h1>
	<hr>
	<div id="Dc_Data"></div>
	<br>
	<hr>
</body>
</html>