<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<%--<%@ include file="/WEB-INF/views/include/highcharts.jsp"%>--%>
<html>
<head>
    <title>平台报表管理</title>
    <meta name="decorator" content="default"/>
    <script type="text/javascript">
        var contextPath = '<c:out value="${pageContext.request.contextPath}"/>';
        $(document).ready(function () {

            function chaxun() {
                var arr = [];
                var data = [{
                    "title": "教师绩效考核结果分析",
                    "data": [{"num": 0, "name": "0-20分"}, {"num": 1, "name": "21-40分"}, {"num": 0, "name": "41-60分"}, {
                        "num": 0,
                        "name": "61-80分"
                    }, {"num": 0, "name": "81-100分"}]
                }];

                //$(data.data).each(function(index,item){
                arr.push(["0-20分", 22]);
                arr.push(["0-44440分", 12]);
                //});
                chart.series[0].setData(arr);//数据填充到highcharts上面


            };
            $.ajax({
                url: contextPath + '/linechart4',
                dataType: 'json',
                success: function (data) {
                    chart = new Highcharts.Chart({
                        chart: {
                            renderTo: 'chart1-container',
                            plotBackgroundColor: null,
                            plotBorderWidth: null,
                            plotShadow: false
                        }, title: {
                            text: '教师绩效分析图'
                        },
                        tooltip: {
                            pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
                        },
                        plotOptions: {
                            pie: {
                                allowPointSelect: true,
                                cursor: 'pointer',
                                dataLabels: {
                                    enabled: true,
                                    color: '#000000',
                                    connectorColor: '#000000',
                                    format: '<b>{point.name}</b>: {point.percentage:.1f} %'
                                },
                                showInLegend: true
                            }
                        },
                        series: [{
                            type: 'pie',
                            name: '所占比例',
                        }]
                    });

                    chaxun();


                    var options = {
                        chart: {
                            type: 'column'
                        },
                        title: {
                            text: '水果消费情况'
                        },
                        xAxis: {
                            categories: []
                        },
                        yAxis: {
                            title: {
                                text: '单位'
                            }
                        },
                        tooltip: {
                            formatter: function () {
                                return '<b>' + this.series.name + '</b><br/>' +
                                    this.x + ': ' + this.y;
                            }
                        },
                        legend: {
                            layout: 'vertical',
                            align: 'right',
                            verticalAlign: 'top',
                            x: -10,
                            y: 100,
                            borderWidth: 0
                        },
                        series: []
                    }


                    var categories = data.categories;
                    var title = data.title;
                    var yTitle = data.yAxisTitle;
                    var xTitle = data.xAxisTitle;
                    var divId = data.divId;

                    $.each(data.series, function (i, seriesItem) {
                        console.log(seriesItem);
                        var series = {
                            data: []
                        };
                        series.name = seriesItem.name;
                        series.color = seriesItem.color;

                        $.each(seriesItem.data, function (j, seriesItemData) {
                            console.log("Data (" + j + "): " + seriesItemData);
                            series.data.push(parseFloat(seriesItemData));
                        });

                        options.series[i] = series;
                    });

                    new Highcharts.Chart('chart2-container', options);

                },
                error: function (xhr, ajaxOptions, thrownError) {
                    alert(xhr.status);
                    alert(thrownError);
                },
                cache: false
            });
        });


    </script>
</head>
<body>
<%--<ul class="nav nav-tabs">
    <shiro:hasPermission name="pdd:pddPlatform:view"><li><a href="${ctx}/pdd/pddPlatform/highCharts">平台报表</a></li></shiro:hasPermission>
</ul>--%>

<div id="wrap">
    <!-- NAVBAR -->
    <!-- Docs master nav -->
    <div class="navbar navbar-default navbar-fixed-top">
        <div class="container">
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
    </div>
    <br/><br/><br/><br/>

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
                            <div id="chart1-container"
                                 style="min-width:400px;height:400px;  height: 300px; margin: 0 auto"></div>
                        </div>
                    </div>
                    <div class="col-md-4">
                        <div class="panel panel-primary">
                            <div class="panel-heading">
                                <h3 class="panel-title">Highchart Chart 2</h3>
                            </div>
                            <div id="chart2-container"
                                 style="min-width:400px;height:400px;   height: 300px; margin: 0 auto"></div>
                        </div>
                    </div>
                </div>
            </div>
        </div>


    </div><!--/CONTAINER -->
    <div id="push"></div>
</div><!--/WRAP -->
</body>
</html>