<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
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
<link rel="stylesheet" href="/css/bootstrap.min.css">
<link rel="stylesheet" href="/css/bootstrap-datepicker.css">
<title>Insert title here</title>
</head>
<body>
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
							<%@include file="/WEB-INF/view/user/frame/TopbarLogout.jsp"%>
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
	<div class="container-fluid" style="border: 1px solid black">
		<div class="col-11" style="margin: 0 auto;">
			<div class="row">
				<div class="col-4" style="border: 1px solid black; height: 200px;"></div>
				<div class="col-4" style="border: 1px solid black; height: 200px;"></div>
				<div class="col-4" style="border: 1px solid black; height: 200px;"></div>
			</div>
		</div>
	</div>
</body>
</html>