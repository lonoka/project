<%@page import="poly.util.CmmUtil"%>
<%@page import="poly.dto.DataDTO"%>
<%@page import="poly.dto.UserDTO"%>
<%@page import="poly.dto.CommuDTO"%>
<%@page import="poly.util.EncryptUtil"%>
<%@page import="java.util.List"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	List<Map> pList = (List<Map>) request.getAttribute("pList");
	List<DataDTO> seList = (List<DataDTO>) request.getAttribute("seList");
	String userId = (String) session.getAttribute("userId");
	String userAuthor = (String) session.getAttribute("userAuthor");
	String sValue = CmmUtil.nvl((String) request.getAttribute("sValue"));
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">


<link
	href="https://fonts.googleapis.com/css?family=Open+Sans:300,400,700"
	rel="stylesheet">
<link
	href="https://fonts.googleapis.com/css2?family=Nanum+Gothic:wght@400;700;800&display=swap"
	rel="stylesheet">
<link rel="stylesheet" href="/fonts/icomoon/style.css">
<link rel="stylesheet" href="/css/bootstrap.min.css">
<link rel="stylesheet" href="/css/jquery-ui.css">
<link rel="stylesheet" href="/css/owl.carousel.min.css">
<link rel="stylesheet" href="/css/owl.theme.default.min.css">
<link rel="stylesheet" href="/css/owl.theme.default.min.css">
<link rel="stylesheet" href="/css/jquery.fancybox.min.css">
<link rel="stylesheet" href="/css/bootstrap-datepicker.css">
<link rel="stylesheet" href="/fonts/flaticon/font/flaticon.css">
<link rel="stylesheet" href="/css/aos.css">
<link rel="stylesheet" href="/css/style.css">
<link rel="stylesheet" href="/css/ionicons.min.css">
<style type="text/css">
.block-27 ul {
	padding: 0;
	margin: 0;
}

.btn_alert {
	background-color: #F7CAC9;
	border-color: #F7CAC9;
}

.btn_alert:hover {
	background-color: #f7b6b5 !important;
	border-color: #f7b6b5 !important;
}

.block-27 ul li {
	display: inline-block;
	font-weight: 400;
}

.block-27 ul li a, .block-27 ul li span {
	color: dodgerblue;
	text-align: center;
	display: inline-block;
	width: 40px;
	height: 40px;
	line-height: 40px;
	border-radius: 50%;
	border: 1px solid #e6e6e6;
}

.block-27 ul li.active a, .block-27 ul li.active span {
	background: #E3F1FF;
	color: dodgerblue;
	border: 1px solid #e6e6e6;
}

h2 a:hover {
	color: aliceblue !important;
}

.div_content_container {
	display: table;
	table-layout: fixed;
	width: 100%;
	border: 1px solid #dee2e6;
}

.linkbody_content_container {
	display: table;
	table-layout: fixed;
	width: 100%;
	border-bottom: 1px solid #666666;
}

.linkheader_content_container {
	display: table;
	table-layout: fixed;
	width: 100%;
	border-top: 1px solid #666666;
	border-bottom: 1px solid #666666;
}

.linkdiv_content_box {
	display: table-cell;
	text-overflow: ellipsis;
	overflow: hidden;
	white-space: nowrap;
	text-align: center;
}

.link_1st {
	width: 70%;
}

.div_content_box {
	display: table-cell;
	border: 1px solid #dee2e6;
	text-overflow: ellipsis;
	overflow: hidden;
	white-space: nowrap;
	padding-top: 5px;
	padding-bottom: 5px;
	text-align: center;
}

.search_box {
	margin-right: 5px;
}

.table_1st {
	width: 8%;
}

.table_3rd {
	width: 15%;
}

.table_5th {
	width: 10%;
}

#ticker{
	float:left;
	width:100%;
}
.navi{
	float:right;
}
.recommend_text {
	height:30px; 
	overflow:hidden;
	width: 200px;
}
.recommend_text ul,.recommend_text li {
	margin:0; 
	padding:0; 
	list-style:none;
}
.recommend_text li a {
	height:30px; 
	line-height:30px;
	color : black;
	display:block;
	text-decoration:none;
	text-overflow: ellipsis;
}

</style>

<title>CA.</title>
</head>
<body data-spy="scroll" data-offset="300">

	<div id="overlayer"></div>
	<div class="loader">
		<div class="spinner-border text-primary" role="status">
			<span class="sr-only">Loading...</span>
		</div>
	</div>

	<div class="site-wrap">

		<div class="site-mobile-menu site-navbar-target">
			<div class="site-mobile-menu-header">
				<div class="site-mobile-menu-close mt-3">
					<span class="icon-close2 js-menu-toggle"></span>
				</div>
			</div>
			<div class="site-mobile-menu-body"></div>
		</div>


		<header class="site-navbar js-sticky-header site-navbar-target"
			role="banner">

			<div class="container">
				<div class="row align-items-center">

					<div class="col-6 col-xl-2">
						<h1 class="mb-0 site-logo">
							<a href="/index.do" class="h2 mb-0">CA<span
								class="text-primary">.</span>
							</a>
						</h1>
					</div>

					<div class="col-12 col-md-10 d-none d-xl-block">
						<nav class="site-navigation position-relative text-right"
							role="navigation">
							<%
								if (userId == null) {
							%>
							<%@include file="/WEB-INF/view/user/frame/TopbarLogout.jsp"%>
							<%
								} else {
							%>
							<%
								if (userAuthor.equals("1")) {
							%>
							<%@include file="/WEB-INF/view/user/frame/TopbarLoginAdmin.jsp"%>
							<%
								} else {
							%>
							<%@include file="/WEB-INF/view/user/frame/TopbarLoginUser.jsp"%>
							<%
								}
							%>
							<%
								}
							%>
						</nav>
					</div>


					<div class="col-6 d-inline-block d-xl-none ml-md-0 py-3"
						style="position: relative; top: 3px;">
						<a href="#" class="site-menu-toggle js-menu-toggle float-right"><span
							class="icon-menu h3"></span></a>
					</div>

				</div>
			</div>

		</header>
		<!-- Modal -->
		<%
			if (userId == null) {
		%>
		<%@include file="/WEB-INF/view/user/frame/ModalLogout.jsp"%>
		<%
			} else {
		%>
		<%
			if (userAuthor.equals("1")) {
		%>
		<%@include file="/WEB-INF/view/user/frame/ModalLoginAdmin.jsp"%>
		<%
			} else {
		%>
		<%@include file="/WEB-INF/view/user/frame/ModalLoginUser.jsp"%>
		<%
			}
		%>
		<%
			}
		%>
		<%@include file="/WEB-INF/view/user/frame/ModalContact.jsp"%>
		<%@include file="/WEB-INF/view/user/frame/ModalAlert.jsp"%>


		<div class="site-blocks-cover overlay"
			style="background-image: url(/images/bg.jpg);" data-aos="fade"
			id="home-section">

			<div class="container">
				<div class="row align-items-center justify-content-center">


					<div class="col-md-12 text-center">
						<div id="slider" class="carousel slide" data-ride="carousel">
							<ol class="carousel-indicators">
								<%for(int i = 0; i<pList.size();i++){ %>
								<%if(i==0){ %>
								<li data-target="#slider" data-slide-to="<%=i%>" class="active"></li>
								<%}else{ %>
								<li data-target="#slider" data-slide-to="<%=i%>"></li>
								<%} %>
								<%} %>
							</ol>
							<div class="carousel-inner">
								<%for(int i = 0; i<pList.size();i++){ 
									List<DataDTO> rList = (List<DataDTO>)pList.get(i).get("rList");	
								%>
								<%if(i==0){ %>
								<div class="carousel-item active">
									<%}else{ %>
									<div class="carousel-item">
										<%} %>
										<h2 style="font-family: Nanum Gothic">
											<a href="javascript:void(0)" class="" data-toggle="modal"
												data-target="#comu_modal_<%=i%>" data-backdrop="static"
												style="color: aliceblue !important;">
												<%if(sValue.equals("")){ %>
												<%=rList.get(0).getCommu_name()%>
												<%}else{ %>
												<%=rList.get(0).getCommu_name()%> : '<%=sValue %>' 검색결과
												<%} %>
												</a>
										</h2>
										<div id="wordcloud_<%=i%>" class=""
											style="height: 600px; width: 100%"></div>
									</div>
									<%} %>
								</div>
							</div>
							<br>
							<div class="col-md-8" style="margin: 0 auto">
								<div class="form-row">
									<div class="col-12 col-md-9 mb-2 mb-md-0">
										<input id="searchTitle" type="text" class="form-control form-control-lg"
											placeholder="검색어를 입력하세요">
									</div>
									<div class="col-12 col-md-3">
										<button type="button" class="btn btn-block btn-lg btn-primary" onclick="searchComu()"
											style="padding-top: 8.5px; padding-bottom: 8.5px; color: dodgerblue;">검색</button>
									</div>
								</div>
							</div>
							<div class="col-md-4" style="margin: 0 auto;">
								<div class="row" style="text-align:center; font-size: 24px; color: black;">
									<div style="margin: auto; text-align: center;">추천&nbsp검색어&nbsp:&nbsp</div>
									<div class="recommend_text" style="margin: auto; text-align: center;">
										<ul id="ticker">
											<%for(int i = 0; i<seList.size();i++){%>
										 		<li><a href="javascript:void(0)" onclick="searchClick('<%=seList.get(i)%>')"><%=seList.get(i) %></a></li>
											<%} %>
 									    </ul>
									</div>
								</div>
							</div>
							<form id = "searchForm" method="post" action="/searchPage.do">
								<input type="hidden" id = "searchValue" name="searchValue">
								<input type="hidden" name="checkNum" value="1">
							</form>
						</div>

					</div>


				</div>

			</div>
			<!-- 커뮤니티 모달 -->
			<%for(int i = 0; i <pList.size();i++){
			List<DataDTO> rList = (List<DataDTO>)pList.get(i).get("rList");
			List<CommuDTO> bList = (List<CommuDTO>)pList.get(i).get("bList");
		%>
			<div class="modal fade" id="comu_modal_<%=i%>" tabindex="-1"
				role="dialog" aria-labelledby="contactLabel" aria-hidden="true">
				<div class="modal-dialog modal-lg modal-dialog-centered">
					<div class="modal-content">
						<div class="modal-header">
							<h4 class="modal-title" id="contactLabel"><%=rList.get(0).getCommu_name()%></h4>
							<button type="button" class="close" data-dismiss="modal"
								aria-label="Close">
								<span aria-hidden="true">&times;</span>
							</button>

						</div>
						<div class="modal-body">
							<div class="row" style="font-weight: bold;">
								<div id="" class="col-md-6" style="text-align: center;">
									게시글 분석 개수 :
									<%=bList.size() %>
								</div>
								<div id="time_section_<%=i%>" class="col-md-6"
									style="text-align: center;"></div>
							</div>
							<div class="row">
								<div id="" class="col-md-6" style="text-align: center;">
									커뮤니티 긍정, 부정 정도 <br>(0에 가까울수록 부정적입니다.)
								</div>
								<div id="" class="col-md-6" style="text-align: center;">
									분석 게시글 시간대별 작성개수<br><%if(sValue.equals("")) {%>(10분단위)<%}else{ %>(일 단위)<%} %>
								</div>
							</div>
							<div class="row">
								<div id="opinion_chart_<%=i%>" class="col-md-6"></div>
								<div id="time_chart_<%=i%>" class="col-md-6"
									style="height: 250px;"></div>
							</div>
							<div class="row">
								<div id="" class="col-md-3" style="text-align: center;">
									게시글 작성자</div>
								<div id="" class="col-md-9" style="text-align: center;">
									조회수 많은 게시글</div>
							</div>

							<div class="row">
								<div id="writer_div_<%=i%>" class="col-md-3"
									style="margin: auto; height: 300px"></div>
								<div class="col-md-9" style="margin: auto; height: 300px">
									<div style="width: 100%">
										<div class="linkheader_content_container">
											<div style="display: table-row;">
												<div class="linkdiv_content_box link_1st">제목</div>
												<div class="linkdiv_content_box">작성자</div>
												<div class="linkdiv_content_box">조회수</div>
											</div>
										</div>
										<div id="link_div_<%=i%>" class="linkbody_content_container">
										</div>
									</div>
								</div>
							</div>
						</div>
						<div class="modal-footer">
							<button type="button" class="btn btn-default"
								data-dismiss="modal">Close</button>
							<button type="button"
								onclick="open_comu('<%=rList.get(0).getCommu_name()%>')"
								class="btn btn-primary" style="color: dodgerblue;">커뮤니티
								열기</button>
						</div>
					</div>
				</div>
			</div>
			<%} %>

		</div>
		<!-- .site-wrap -->
		<script src="/js/jquery-3.5.0.min.js"></script>
		<script src="/js/jquery-ui.js"></script>
		<script src="/js/popper.min.js"></script>
		<script src="/js/bootstrap.min.js"></script>
		<script src="/js/owl.carousel.min.js"></script>
		<script src="/js/jquery.countdown.min.js"></script>
		<script src="/js/jquery.easing.1.3.js"></script>
		<script src="/js/aos.js"></script>
		<script src="/js/jquery.fancybox.min.js"></script>
		<script src="/js/jquery.sticky.js"></script>
		<script src="/js/isotope.pkgd.min.js"></script>
		<script src="/js/main.js"></script>

		<script src="https://www.amcharts.com/lib/4/core.js"></script>
		<script src="https://www.amcharts.com/lib/4/charts.js"></script>
		<script src="https://www.amcharts.com/lib/4/plugins/wordCloud.js"></script>
		<script src="https://www.amcharts.com/lib/4/themes/animated.js"></script>



		<!-- 워드클라우드 스크립트 -->
		<%for(int j = 0; j<pList.size();j++){
		List<DataDTO>rList = (List<DataDTO>)pList.get(j).get("rList");
	%>
		<script type="text/javascript">
		console.log('<%=sValue%>');
	var rList<%=j%> = [
		<%if(rList.size()>200){%>
		<%for (int i = 0; i < 200; i++) {%>
			{word:'<%=rList.get(i).getWord()%>', count:<%=Integer.toString(rList.get(i).getCount())%>},
		<%}%>
		<%}else{%>
		<%for (int i = 0; i < rList.size(); i++) {%>
		{word:'<%=rList.get(i).getWord()%>', count:<%=Integer.toString(rList.get(i).getCount())%>},
		<%}%>
		<%}%>
	];
	am4core.ready(function() {
	// Themes begin
	am4core.useTheme(am4themes_animated);
	// Themes end
	var chart = am4core.create("wordcloud_<%=j%>", am4plugins_wordCloud.WordCloud);
	var series = chart.series.push(new am4plugins_wordCloud.WordCloudSeries());
	series.randomness = 0.1;
	series.labels.template.tooltipText = "{word}: {value}";
	series.fontFamily = "Courier New";
	series.data = rList<%=j%>;
	series.dataFields.word = "word";
	series.dataFields.value = "count";

	}); // end am4core.ready()
	function numberWithCommas(x) {
    	return x.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
	}
	</script>
		<%}%>
		<!-- 작성자 div -->
		<%for(int j = 0; j<pList.size();j++){
		List<DataDTO>wList = (List<DataDTO>)pList.get(j).get("wList");
	%>
		<script type="text/javascript">
	var str = '<div style="width:100%; height:27px; overflow:hidden;"></div>';
	<%if (wList.size() > 10) {%>
	   	<%for (int i = 0; i < 10; i++) {%>
	   		str += '<div style="width:100%; height:27px; overflow:hidden;">'+(<%=i%>+1)+'. <%=wList.get(i).getWord()%> : <%=wList.get(i).getCount()%></div>';
	  	<%}%>	
	<%} else {%>
		<%for (int i = 0; i < wList.size(); i++) {%>
			str += '<div style="width:100%; height:27px; overflow:hidden;">'+(<%=i%>+1)+'. <%=wList.get(i).getWord()%> : <%=wList.get(i).getCount()%></div>';
		<%}%>
	<%}%>
	$("#writer_div_<%=j%>").html(str);
	</script>
		<%}%>
		
		<!-- 게시글 div -->
		<%for(int j = 0; j<pList.size();j++){
		List<CommuDTO>bList = (List<CommuDTO>)pList.get(j).get("bList");
	%>
		<script type="text/javascript">
	var str = "";
	<%if (bList.size() > 10) {%>
	   	<%for (int i = 0; i < 10; i++) {%>
			var timediv = '<%=bList.get(i).getViews()%></div>';
			var writer = '<%=bList.get(i).getWriter()%>'
		   	str += '<div style="display: table-row;">';
		   	str += '<div class="linkdiv_content_box link_1st">';
		   	str += '<a href="<%=bList.get(i).getLink()%>" target="_blank">';
		   	str += '<%=bList.get(i).getTitle()%></a></div>';
	   		str += '<div class="linkdiv_content_box">'+writer+'</div>';
	   		str += '<div class="linkdiv_content_box">'+numberWithCommas(timediv)+'</div>';
	   		str += '</div>'
	   	<%}%>	
	<%} else {%>
		<%for (int i = 0; i < bList.size(); i++) {%>
			var timediv = '<%=bList.get(i).getViews()%></div>';
			var writer = '<%=bList.get(i).getWriter()%>'
		   	str += '<div style="display: table-row;">';
		   	str += '<div class="linkdiv_content_box link_1st">';
		   	str += '<a href="<%=bList.get(i).getLink()%>" target="_blank">';
		   	str += '<%=bList.get(i).getTitle()%></a></div>';
	   		str += '<div class="linkdiv_content_box">'+writer+'</div>';
   			str += '<div class="linkdiv_content_box">'+numberWithCommas(timediv)+'</div>';
   			str += '</div>'
		<%}%>
	<%}%>
	$("#link_div_<%=j%>").html(str);
	</script>
		<%
			}
		%>
		
		<!-- 시간대별 차트 -->
		<%
			for (int j = 0; j < pList.size(); j++) {
				List<DataDTO> tList = (List<DataDTO>) pList.get(j).get("tList");
		%>
		<script type="text/javascript">
	am4core.ready(function() {
	// Themes begin
	am4core.useTheme(am4themes_animated);
	// Themes end

	var chart = am4core.create("time_chart_<%=j%>", am4charts.XYChart);

	var data<%=j%> = [];
	var str<%=j%> = "";
	var value = 0;
	<%for (int i = 0; i < tList.size(); i++) {%>
		var date = new Date('<%=tList.get(i).getWord()%>');
		value = '<%=tList.get(i).getCount()%>';
		data<%=j%>.push({date:date, value: value});
	<%}%>
	data<%=j%>.sort(function(a,b){
		return a.date < b.date ? -1 : a.date > b.date ? 1:0;
	});
	chart.data = data<%=j%>;
	var str<%=j%> = data<%=j%>[0].date.format('yyyy-MM-dd HH:mm') +' ~ '+data<%=j%>[data<%=j%>.length-1].date.format('yyyy-MM-dd HH:mm');
	$("#time_section_<%=j%>").html(str<%=j%>);
	// Create axes
	var dateAxis = chart.xAxes.push(new am4charts.DateAxis());
	dateAxis.renderer.minGridDistance = 60;

	var valueAxis = chart.yAxes.push(new am4charts.ValueAxis());

	// Create series
	var series = chart.series.push(new am4charts.LineSeries());
	series.dataFields.valueY = "value";
	series.dataFields.dateX = "date";
	series.tooltipText = "{value}"

	series.tooltip.pointerOrientation = "vertical";

	chart.cursor = new am4charts.XYCursor();
	chart.cursor.snapToSeries = series;
	chart.cursor.xAxis = dateAxis;

	//chart.scrollbarY = new am4core.Scrollbar();
	chart.scrollbarX = new am4core.Scrollbar();

	}); // end am4core.ready()
	</script>
		<%
			}
		%>
		
		<!-- 오피니언 마이닝 -->
		<%
			for (int j = 0; j < pList.size(); j++) {
				List<DataDTO> oList = (List<DataDTO>) pList.get(j).get("oList");
		%>
		<script type="text/javascript">
		//차트 벨류값 받아오기
	<%if (oList.get(0).getWord().equals("긍정")) {%>
	var positive<%=j%> = <%=oList.get(0).getCount()%>
	var negative<%=j%> = <%=oList.get(1).getCount()%>
	<%} else if (oList.get(0).getWord().equals("부정")) {%>
	var positive<%=j%> = <%=oList.get(1).getCount()%>
	var negative<%=j%> = <%=oList.get(0).getCount()%>
	<%}%>
	var cal<%=j%> = (positive<%=j%>)/(positive<%=j%>+negative<%=j%>);
	cal<%=j%> = cal<%=j%>*100;
	am4core.ready(function() {

		// Themes begin
		am4core.useTheme(am4themes_animated);
		// Themes end

		// create chart
		// 차트위치 지정
		var chart = am4core.create("opinion_chart_<%=j%>", am4charts.GaugeChart);
		chart.hiddenState.properties.opacity = 0; // this makes initial fade in effect

		// 차트 크기
		chart.innerRadius = -25;

		var axis = chart.xAxes.push(new am4charts.ValueAxis());
		axis.min = 0;
		axis.max = 100;
		axis.strictMinMax = true;
		axis.renderer.grid.template.stroke = new am4core.InterfaceColorSet().getFor("background");
		axis.renderer.grid.template.strokeOpacity = 0.3;

		var colorSet = new am4core.ColorSet();

		//차트 색상 지정
		var range0 = axis.axisRanges.create();
		range0.value = 0;
		range0.endValue = 50;
		range0.axisFill.fillOpacity = 1;
		range0.axisFill.fill = am4core.color("#F7CAC9");
		range0.axisFill.zIndex = - 1;

		var range1 = axis.axisRanges.create();
		range1.value = 50;
		range1.endValue = 100;
		range1.axisFill.fillOpacity = 1;
		range1.axisFill.fill = am4core.color("#92A8D1");
		range1.axisFill.zIndex = -1;

		// 차트 화살표 생성
		var hand = chart.hands.push(new am4charts.ClockHand());

		hand.showValue(cal<%=j%>, 100, am4core.ease.cubicOut);

		}); // end am4core.ready()

	</script>
		<%
			}
		%>
		<script>
	Date.prototype.format = function (f) {
	    if (!this.valueOf()) return " ";
	    var weekKorName = ["일요일", "월요일", "화요일", "수요일", "목요일", "금요일", "토요일"];
	    var weekKorShortName = ["일", "월", "화", "수", "목", "금", "토"];
	    var weekEngName = ["Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"];
	    var weekEngShortName = ["Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"];
	    var d = this;
	    return f.replace(/(yyyy|yy|MM|dd|KS|KL|ES|EL|HH|hh|mm|ss|a\/p)/gi, function ($1) {
	        switch ($1) {
	            case "yyyy": return d.getFullYear(); // 년 (4자리)
	            case "yy": return (d.getFullYear() % 1000).zf(2); // 년 (2자리)
	            case "MM": return (d.getMonth() + 1).zf(2); // 월 (2자리)
	            case "dd": return d.getDate().zf(2); // 일 (2자리)
	            case "KS": return weekKorShortName[d.getDay()]; // 요일 (짧은 한글)
	            case "KL": return weekKorName[d.getDay()]; // 요일 (긴 한글)
	            case "ES": return weekEngShortName[d.getDay()]; // 요일 (짧은 영어)
	            case "EL": return weekEngName[d.getDay()]; // 요일 (긴 영어)
	            case "HH": return d.getHours().zf(2); // 시간 (24시간 기준, 2자리)
	            case "hh": return ((h = d.getHours() % 12) ? h : 12).zf(2); // 시간 (12시간 기준, 2자리)
	            case "mm": return d.getMinutes().zf(2); // 분 (2자리)
	            case "ss": return d.getSeconds().zf(2); // 초 (2자리)
	            case "a/p": return d.getHours() < 12 ? "오전" : "오후"; // 오전/오후 구분
	            default: return $1;
	        }
	    });
	};
	String.prototype.string = function (len) { var s = '', i = 0; while (i++ < len) { s += this; } return s; };
	String.prototype.zf = function (len) { return "0".string(len - this.length) + this; };
	Number.prototype.zf = function (len) { return this.toString().zf(len);
	};
    $('.carousel').carousel({
  interval: 10000
})  
</script>
<script>
	$('body').on('click', '.topbar_link', function(e) {
		var $this = $(this);
		e.preventDefault();

		if ( $('body').hasClass('offcanvas-menu') ) {
			$('body').removeClass('offcanvas-menu');
			$this.removeClass('active');
		}
	})
	function open_comu(e){
		var str = e;
		switch (e) {
		case "컴퓨터 본체 갤러리":
			window.open('https://gall.dcinside.com/board/lists/?id=pridepc_new3','_blank');
			break;
		case "SLR클럽":
			window.open('http://www.slrclub.com/bbs/zboard.php?id=free','_blank');
			break;
		case "뽐뿌":
			window.open('http://www.ppomppu.co.kr/','_blank');
			break;
		case "82쿡":
			window.open('https://www.82cook.com/','_blank');
			break;
		case "MLBPARK":
			window.open('http://mlbpark.donga.com/mp','_blank');
			break;
		default:
			break;
		}
	}
    // 특수문자 정규식 변수(공백 미포함)
    var replaceChar = /[~!@\#$%^&*\()\-=+_'\;<>\/.\`:\"\\,\[\]?|{}]/gi;
 
    // 완성형 아닌 한글 정규식
    var replaceNotFullKorean = /[ㄱ-ㅎㅏ-ㅣ]/gi;

	 $(document).ready(function(){
        
        $("#searchTitle").on("focusout", function() {
            var x = $(this).val();
            if (x.length > 0) {
                if (x.match(replaceChar) || x.match(replaceNotFullKorean)) {
                    x = x.replace(replaceChar, "").replace(replaceNotFullKorean, "");
                }
                $(this).val(x);
            }
            }).on("keyup", function() {
                $(this).val($(this).val().replace(replaceChar, ""));

       });

    });
	function searchClick(c){
		<%if(userId==null){%>
		$('#alert_modal_body').html('로그인이 필요한 서비스 입니다.');
		$('#alert_modal').modal('show')
		<%}else{%>
		$('#search_modal').modal('show')
		$('#searchValue').val(c);
		$.ajax({
			url : "/searchCheck.do",
			type : "post",
			data : {
				'searchTitle' : c
			},
			success : function(a){
				$('#searchForm').submit();
			}
		})
		<%}%>
	}
	function searchComu(){
		<%if(userId==null){%>
		$('#alert_modal_body').html('로그인이 필요한 서비스 입니다.');
		$('#searchTitle').val('');
		$('#alert_modal').modal('show')
		<%}else{%>
		$('#search_modal').modal('show')
		$('#searchValue').val($('#searchTitle').val());
		$.ajax({
			url : "/searchCheck.do",
			type : "post",
			data : {
				'searchTitle' : $('#searchTitle').val()
			},
			success : function(a){
				$('#searchForm').submit();
			}
		})
		<%}%>
	}
	jQuery(function($){
		var ticker = function()
			{
				timer = setTimeout(function(){
			    	$('#ticker li:first').animate( {marginTop: '-30px'}, 1500, function(){
			    		$(this).detach().appendTo('ul#ticker').removeAttr('style');
			    	});
			        ticker();
			    }, 2000);         
			};
			// 0번 이전 기능
			$(document).on('click','.prev',function(){
				$('#ticker li:last').hide().prependTo($('#ticker')).slideDown();
			    clearTimeout(timer);
			    ticker();
			    if($('#pause').text() == 'Unpause'){
			    	$('#pause').text('Pause');
			    };
			}); // 0번 기능 끝
			//2. 재생정지기능 시작, 아직 다음 기능과 연동은 안됨...그래서 3을 만듦
			var autoplay = true;
			$(document).on('click','.pause',function(){
			  	if(autoplay==true){
				   	clearTimeout(timer);
			        $(this).text('재생');
			        autoplay=false;
			    } else {
			     	autoplay=true;
			        $(this).text('정지');
			        ticker();
			    }
			}); // 재생정지기능 끝  
			//4 마우스를 올렸을 때 기능 정지
			var tickerover = function(){
				$('#ticker').mouseover(function(){
			    	clearTimeout(timer);
			    });
			    $('#ticker').mouseout(function(){
			    	ticker();
			    });  
			};
			tickerover();
			// 4 끝
			ticker();
		});
</script>
</body>
</html>