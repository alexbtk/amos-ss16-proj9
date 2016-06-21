<!-- 
*
* AMOS-SS16-PROJ9
*
* Released under the AGPL license
* Date: 2016-06-11
-->
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<title>AdminLTE 2 | Dashboard</title>
<!-- Tell the browser to be responsive to screen width -->
<meta
	content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no"
	name="viewport">
<!-- Bootstrap 3.3.6 -->
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css">
<!-- Font Awesome -->
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.5.0/css/font-awesome.min.css">
<!-- Ionicons -->
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/ionicons/2.0.1/css/ionicons.min.css">
<!-- Theme style -->
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/AdminLTE.min.css">
<!-- AdminLTE Skins. Choose a skin from the css/skins
       folder instead of downloading all of them to reduce the load. -->
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/skins/_all-skins.min.css">
<!-- iCheck -->
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/plugins/iCheck/flat/blue.css">
<!-- Morris chart -->
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/plugins/morris/morris.css">
<!-- jvectormap -->
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/plugins/jvectormap/jquery-jvectormap-1.2.2.css">
<!-- Date Picker -->
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/plugins/datepicker/datepicker3.css">
<!-- Daterange picker -->
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/plugins/daterangepicker/daterangepicker-bs3.css">
<!-- bootstrap wysihtml5 - text editor -->
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/plugins/bootstrap-wysihtml5/bootstrap3-wysihtml5.min.css">
<!-- Link to bootstrap slider css -->
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-slider/7.1.0/css/bootstrap-slider.css">

<!-- Tell the browser to be responsive to screen width -->
<meta
	content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no"
	name="viewport">
<!-- Bootstrap 3.3.6 -->
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css">
<!-- Font Awesome -->
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.5.0/css/font-awesome.min.css">
<!-- Ionicons -->
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/ionicons/2.0.1/css/ionicons.min.css">
<!-- Theme style -->
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/AdminLTE.min.css">
<!-- AdminLTE Skins. Choose a skin from the css/skins
       folder instead of downloading all of them to reduce the load. -->
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/skins/_all-skins.min.css">

<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/jquery-ui.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/style.css">

<style type="text/css">
.slider .slider-selection {
	background: red;
}

.slider .slider-track-high {
	background: green;
}
</style>

<script src="${pageContext.request.contextPath}/js/jquery-2.2.3.js"></script>
<script src="${pageContext.request.contextPath}/js/jquery-ui.js"></script>
<!-- Bootstrap 3.3.6 -->
<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"
	integrity="sha384-0mSbJDEHialfmuBBQP6A4Qrprq5OVfW37PRR3j5ELqxss1yVqOtnepnHVP9aJ7xS"
	crossorigin="anonymous"></script>
<script
	src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-slider/7.1.0/bootstrap-slider.js"></script>
<script
	src="${pageContext.request.contextPath}/plugins/chartjs/Chart.js"></script>
<script
	src="${pageContext.request.contextPath}/plugins/chartjs/Chart.Scatter.js"></script>

<script src="${pageContext.request.contextPath}/js/AMOSAlchemy.js"></script>
<script src="${pageContext.request.contextPath}/js/util.js"></script>
<script src="${pageContext.request.contextPath}/js/AMOSAlchemy.js"></script>
<script src="${pageContext.request.contextPath}/js/myScript.js"></script>


<!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
<!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
<!--[if lt IE 9]>
  <script src="https://oss.maxcdn.com/html5shiv/3.7.3/html5shiv.min.js"></script>
  <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
  <![endif]-->
</head>
<body class="hold-transition skin-blue sidebar-mini">

	<header class="main-header">
		<!-- Logo -->
		<a href="#" class="logo"> <!-- mini logo for sidebar mini 50x50 pixels -->
			<span class="logo-mini"><b>L</b>Ogo</span> <!-- logo for regular state and mobile devices -->
			<span class="logo-lg"><b>Cognitive</b>Logo</span>
		</a>
		<!-- Header Navbar: style can be found in header.less -->
		<nav class="navbar navbar-static-top">
			<!-- Sidebar toggle button-->
			<a href="#" class="sidebar-toggle" data-toggle="offcanvas"
				role="button"> <span class="sr-only">Toggle navigation</span>
			</a>

			<div id="searchMenu">
				<input id="dashboardCompanyInput" class="companyInput" type="text"
					name="companyName" maxlength="100"> <a href="#"
					id="searchButton" class="dropdown-toggle" style="cursor: pointer;">
					<span class="hidden-xs">Search!</span>
				</a>
			</div>

			<div class="navbar-custom-menu">
				<ul class="nav navbar-nav">
					<!-- Messages: style can be found in dropdown.less-->

					<!-- User Account: style can be found in dropdown.less -->
					<li class="dropdown user user-menu"><a href="#"
						id="logoutButton" class="dropdown-toggle" style="cursor: pointer;">
							<!-- <img src="${pageContext.request.contextPath}/img/user2-160x160.jpg" class="user-image" alt="User Image">  -->
							<span class="hidden-xs">Log out</span>
					</a></li>
					<!-- Control Sidebar Toggle Button -->
					<li><a href="#" data-toggle="control-sidebar"><i
							class="fa fa-gears"></i></a></li>
				</ul>
			</div>
		</nav>
	</header>


	<div id="login">
		<div class="box box-primary">
			<legend>Login</legend>
			<fieldset>
				<table>
					<tbody>
						<tr>
							<td><label for="apiKey">Alchemy API-Key</label></td>
						</tr>
						<tr>
							<td><input id="apiKey" type="text" maxLength=100 /></td>
						</tr>
						<tr>
							<td><label for="twitterConsumerKey">Twitter Consumer
									Key</label></td>
							<td><label for="twitterConsumerSecret">Twitter
									Consumer Secret</label></td>

						</tr>
						<tr>
							<td><input id="twitterConsumerKey" type="text" maxLength=100 /></td>
							<td><input id="twitterConsumerSecret" type="text"
								maxLength=100 /></td>
						</tr>
						<tr>
							<td><label for="twitterToken">Twitter Token</label></td>
							<td><label for="twitterTokenSecret">Twitter Token
									Secret</label></td>
						</tr>
						<tr>
							<td><input id="twitterToken" type="text" maxLength=100 /></td>
							<td><input id="twitterTokenSecret" type="text" maxLength=100 /></td>
						</tr>
					</tbody>
				</table>

				<br /> <br /> <a href="#" id="loginButton" class="dropdown-toggle"
					style="cursor: pointer;"> <!-- <img src="${pageContext.request.contextPath}/img/user2-160x160.jpg" class="user-image" alt="User Image">  -->
					<span class="hidden-xs">Login</span>
				</a>
				<!-- <button id="loginButton">Login</button> -->
			</fieldset>
		</div>
	</div>
	<div id="welcome">
		<img src="${pageContext.request.contextPath}/img/cognitive.JPG" />
		<h1>Welcome to Cognitive Services!</h1>
		<div class="box box-primary">
			<h2>Please enter a company name:</h2>
			<input id="welcomeCompanyInput" class="companyInput" type="text"
				name="companyName" maxlength="100" /> <a href="#"
				id="welcomeButton" class="dropdown-toggle" style="cursor: pointer;">
				<span class="hidden-xs">Go!</span>
			</a>
			<!-- <button id="welcomeButton">Go!</button> -->
		</div>
	</div>

	<div id="content">
		<!-- Main content -->
		<section class="content">
			<!-- Small boxes (Stat box) -->
			<div class="row">
				<div class="col-lg-3 col-xs-6">
					<!-- small box -->
					<div class="small-box bg-aqua">
						<div class="inner">
							<h3>150</h3>

							<p>Twitter</p>
						</div>
						<div class="icon">
							<i class="ion ion-social-twitter"></i>
						</div>
						<a href="#" onclick="openSection('twitter');"
							class="small-box-footer">More info <i
							class="fa fa-arrow-circle-right"></i></a>
					</div>
				</div>
				<!-- ./col -->
				<div class="col-lg-3 col-xs-6">
					<!-- small box -->
					<div class="small-box bg-green">
						<div class="inner">
							<h3>
								53<sup style="font-size: 20px">%</sup>
							</h3>

							<p>Products</p>
						</div>
						<div class="icon">
							<i class="ion ion-bag"></i>
						</div>
						<a href="#" onclick="openSection('products');"
							class="small-box-footer">More info <i
							class="fa fa-arrow-circle-right"></i></a>
					</div>
				</div>
				<!-- ./col -->
				<div class="col-lg-3 col-xs-6">
					<!-- small box -->
					<div class="small-box bg-yellow">
						<div class="inner">
							<h3>44</h3>

							<p>Company</p>
						</div>
						<div class="icon">
							<i class="ion ion-stats-bars"></i>
						</div>
						<a href="#" onclick="openSection('company');"
							class="small-box-footer">More info <i
							class="fa fa-arrow-circle-right"></i></a>
					</div>
				</div>
				<!-- ./col -->
				<div class="col-lg-3 col-xs-6">
					<!-- small box -->
					<div class="small-box bg-red">
						<div class="inner">
							<h3>65</h3>

							<p>Competitors</p>
						</div>
						<div class="icon">
							<i class="ion ion-ios-people"></i>
						</div>
						<a href="#" onclick="openSection('competitors');"
							class="small-box-footer">More info <i
							class="fa fa-arrow-circle-right"></i></a>
					</div>
				</div>
				<!-- ./col -->
			</div>
			<!-- /.row -->


			<section class="contentSection" id="competitorsSection">
				Competitors content</section>
			<section class="contentSection" id="productsSection">
				<div id="companyProducts"></div>
				<div id="competitorsProducts"></div>
				<div id="graphProducts"></div>
			</section>
			<section class="contentSection" id="companySection">
				<div class="box">
					<div class="box-header with-border">
						<h3 class="box-title">Location Map</h3>

						<div class="box-tools pull-right">
							<button type="button" class="btn btn-box-tool"
								data-widget="collapse" data-toggle="tooltip" title="Collapse">
								<i class="fa fa-minus"></i>
							</button>
							<button type="button" class="btn btn-box-tool"
								data-widget="remove" data-toggle="tooltip" title="Remove">
								<i class="fa fa-times"></i>
							</button>
						</div>
					</div>
					<div class="box-body">
						<div class="box box-info">
							<div class="box-header with-border">
								<h3 class="box-title"></h3>
							</div>
							<!-- /.box-header -->
							<!-- form start -->
							<form class="form-horizontal" method="POST"
								action="getCompanyLocationMap">
								<div class="box-body">
									<div id="world-map" style="width: 600px; height: 400px"></div>
								</div>
								<!-- /.box-body -->
								<div class="box-footer">
									<button type="submit" class="btn btn-info pull-right">Search</button>
								</div>
								<!-- /.box-footer -->
							</form>
						</div>


					</div>
					<!-- /.box-body -->

					<div class="box-footer">Footer</div>
					<!-- /.box-footer-->
				</div>
				<!-- /.box -->
				<a href="${pageContext.request.contextPath}/locationmap">Locationmap!</a>
				<div id="avgNewsSentimentGraph">
					<canvas id="avgNewsSentimentGraphCanvas"></canvas>
				</div>
			</section>
			<section class="contentSection" id="twitterSection">
				<div class="row">
					<div class="col-lg-6">
						<form method="POST" action="getSentiment">
							<fieldset>
								<div id="twitterAnswer">
									<br />Average Twitter Posts sentiment value:<br /> <input
										id="twitterslider" type="text" data-slider-min="-1"
										data-slider-max="1" data-slider-step="0.1"
										data-slider-enabled="false" />

								</div>

							</fieldset>
						</form>
						<br><br><br>
						<div class="tabbable">
							<ul class="nav nav-tabs">
								<li class="active"><a href="#negsent" data-toggle="tab">Negative
										Sentiment</a></li>
								<li><a href="#neutsent" data-toggle="tab">Neutral
										Sentiment</a></li>
								<li><a href="#possent" data-toggle="tab">Positive
										Sentiment</a></li>
							</ul>
							<div class="tab-content">
								<div class="tab-pane active" id="negsent">
									
								</div>
								<div class="tab-pane" id="neutsent">
									
								</div>
								<div class="tab-pane" id="possent">
									
									
								</div>
							</div>
						</div>

					</div>
				</div>
			</section>



		</section>

		<template id="template"> <!-- Default box -->
		<div class="box">
			<div class="box-header with-border">
				<h3 class="box-title">Industry 1</h3>

				<div class="box-tools pull-right">
					<button type="button" class="btn btn-box-tool"
						data-widget="collapse" data-toggle="tooltip" title="Collapse">
						<i class="fa fa-minus"></i>
					</button>
					<button type="button" class="btn btn-box-tool" data-widget="remove"
						data-toggle="tooltip" title="Remove">
						<i class="fa fa-times"></i>
					</button>
				</div>
			</div>
			<div class="box-body"></div>
			<!-- /.box-body -->
			<div class="box-footer"></div>
			<!-- /.box-footer-->
		</div>
		<!-- /.box --> </template>
	</div>



	<!-- SlimScroll -->
	<script
		src="${pageContext.request.contextPath}/plugins/slimScroll/jquery.slimscroll.min.js"></script>
	<!-- FastClick -->
	<script
		src="${pageContext.request.contextPath}/plugins/fastclick/fastclick.js"></script>
	<!-- AdminLTE App -->
	<script src="${pageContext.request.contextPath}/js/app.min.js"></script>
	<!-- AdminLTE for demo purposes -->
	<script src="${pageContext.request.contextPath}/js/demo.js"></script>

	<!-- JvectorMap for displaying companys locations -->
	<script
		src="${pageContext.request.contextPath}/plugins/jvectormap/jquery-jvectormap-2.0.3.min.js"></script>
	<script
		src="${pageContext.request.contextPath}/plugins/jvectormap/jquery-jvectormap-world-mill.js"></script>
</body>

</html>