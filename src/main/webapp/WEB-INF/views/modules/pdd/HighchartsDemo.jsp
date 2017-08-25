<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>平台报表管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
        var contextPath = '<c:out value="${pageContext.request.contextPath}"/>';
		$(document).ready(function() {
                getRemoteDataDrawChart(contextPath + '/linechart1', createNewLineChart('chart1-container'));
                getRemoteDataDrawChart(contextPath + '/linechart2', createNewLineChart('chart2-container'));
                getRemoteDataDrawChart(contextPath + '/linechart3', createNewLineChart('chart3-container'));
		});
	</script>
</head>
<body>
<div id="wrap">
	<!-- NAVBAR -->
	<!-- Docs master nav -->
	<div class="navbar navbar-default navbar-fixed-top">
		<div class="container">
			<a href="<c:url value='/charts'/>" class="navbar-brand"><small>Highcharts</small></a>
			<button class="navbar-toggle" type="button" data-toggle="collapse" data-target="#navbar-main">
				<span class="icon-bar"></span>
				<span class="icon-bar"></span>
				<span class="icon-bar"></span>
			</button>
			<div class="nav-collapse collapse" id="navbar-main">
				<ul class="nav navbar-nav pull-left">
				</ul>
			</div>
		</div>
	</div><br/><br/><br/><br/>

	<!-- CONTAINER -->
	<div class="container">
		<div class="row">
			<div class="col-md-12">
				<div class="row">
					<div class="col-md-4">
						<div class="panel panel-danger">
							<div class="panel-heading">
								<h3 class="panel-title">Highchart Chart 1</h3>
							</div>
							<div id="chart1-container" style="min-width: 300px; max-width: 500px;  height: 300px; margin: 0 auto"></div>
						</div>
					</div>
					<div class="col-md-4">
						<div class="panel panel-primary">
							<div class="panel-heading">
								<h3 class="panel-title">Highchart Chart 2</h3>
							</div>
							<div id="chart2-container" style="min-width: 300px; max-width: 500px;  height: 300px; margin: 0 auto"></div>
						</div>
					</div>
					<div class="col-md-4">
						<div class="panel panel-warning">
							<div class="panel-heading">
								<h3 class="panel-title">Highchart Chart 3</h3>
							</div>
							<div id="chart3-container" style="min-width: 300px; max-width: 500px;  height: 300px; margin: 0 auto"></div>
						</div>
					</div>
				</div>
			</div>
		</div>


	</div><!--/CONTAINER -->
	<div id="push"></div>
</div><!--/WRAP -->
</body>
</body>
</html>