<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<html>
<head>
    <title>平台报表管理</title>
    <meta name="decorator" content="default"/>
    <script type="text/javascript">
        $(document).ready(function () {
            $.ajax({
                url:'${ctx}/pdd/pddPlatform/report?id=${pddPlatform.id}',
                type: "post",
                contentType : 'application/json;charset=utf-8',
                cache: false,
                success: function(data){
                    var countValue = data.countValue;

                    $("#countValue").text(countValue);

                    var dataBean_categories = data.DataBean.categories;
                    var dataBean_series = data.DataBean.series;
                    var dataBean_divId = data.DataBean.divId;
                    var dataBean_title = data.DataBean.title;
                    var dataBean_yAxisTitle = data.DataBean.yAxisTitle;
                    var dataBean_xAxisTitle = data.DataBean.xAxisTitle;


                    var DataBeanLine_categories = data.DataBeanLine.categories;
                    var DataBeanLine_series = data.DataBeanLine.series;
                    var DataBeanLine_divId = data.DataBeanLine.divId;
                    var DataBeanLine_title = data.DataBeanLine.title;
                    var DataBeanLine_yAxisTitle = data.DataBeanLine.yAxisTitle;
                    var DataBeanLine_xAxisTitle = data.DataBeanLine.xAxisTitle;


                    var pie_title = data.pieBean.title;
                    var pie_data = data.pieBean.data;
                    var pie_divId = data.pieBean.divId;

                    var arr = [];
                    $.each(pie_data, function (i, seriesItem) {
                        arr.push([seriesItem.name, seriesItem.number]);
                    });

                     options_pie = {
                        chart: {
                            plotBackgroundColor: null,
                            plotBorderWidth: null,
                            plotShadow: false
                        }, title: {
                            text: pie_title
                        },
                        tooltip: {
                            pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
                        },
                         credits: { enabled: false},
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
                            name: pie_title,
                            data:arr
                        }]
                    }

                    options_chart = {
                        chart: {
                            type: 'column'
                        },
                        title: {
                            text: dataBean_title
                        },
                        credits: { enabled: false},
                        xAxis: {
                            categories: dataBean_categories,
                            title: {
                                text: dataBean_xAxisTitle
                            }
                        },
                        yAxis: {
                            title: {
                                text: dataBean_yAxisTitle
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

                    $.each(dataBean_series, function (i, seriesItem) {
                        var series = {
                            data: []
                        };
                        series.name = seriesItem.name;
                        series.color = seriesItem.color;

                        $.each(seriesItem.data, function (j, seriesItemData) {
                            series.data.push(parseFloat(seriesItemData));
                        });

                        options_chart.series[i] = series;
                    });

                     options_line_chart = {
                        chart: {
                            type: 'line'
                        },
                        title: {
                            text: DataBeanLine_title
                        },
                         credits: { enabled: false},
                        xAxis: {
                             title: {
                                 text: DataBeanLine_xAxisTitle
                             },
                            categories: DataBeanLine_categories
                        },
                        yAxis: {
                            title: {
                                text: DataBeanLine_yAxisTitle
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


                    $.each(DataBeanLine_series, function (i, seriesItem) {
                        var series = {
                            data: []
                        };
                        series.name = seriesItem.name;
                        series.color = seriesItem.color;

                        $.each(seriesItem.data, function (j, seriesItemData) {
                            series.data.push(parseFloat(seriesItemData));
                        });

                        options_line_chart.series[i] = series;
                    });

                    new Highcharts.Chart(pie_divId, options_pie);
                    new Highcharts.Chart(dataBean_divId, options_chart);
                    new Highcharts.Chart(DataBeanLine_divId, options_line_chart);

                },
                error: function(){
                    alert('请求服务器出错......');
                }
            });
        });
    </script>
</head>
<body>
<div id="wrap">
    <!-- NAVBAR -->
    <!-- CONTAINER -->
    <div class="container">
        <h3 class="panel-title"><div id="countValue"></div></h3>
        <div class="row">
            <div class="col-md-12">
                <div class="row">
                    <div class="col-md-4">
                        <div class="panel panel-danger">
                            <div class="panel-heading">
                                <h3 class="panel-title">数据比例表</h3>
                            </div>
                            <div id="pie_container"
                                 style="min-width:400px;height:400px;  height: 300px; margin: 0 auto"></div>
                        </div>
                    </div>
                    <div class="col-md-4">
                        <div class="panel panel-danger">
                            <div class="panel-heading">
                                <h3 class="panel-title">柱状图表</h3>
                            </div>
                            <div id="container"
                                 style="min-width:400px;height:400px;  height: 300px; margin: 0 auto"></div>
                        </div>
                    </div>
                    <div class="col-md-4">
                        <div class="panel panel-primary">
                            <div class="panel-heading">
                                <h3 class="panel-title">线性图表</h3>
                            </div>
                            <div id="line_container"
                                 style="min-width:400px;height:400px;   height: 300px; margin: 0 auto"></div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div><!--/CONTAINER -->
</div><!--/WRAP -->
</body>
</html>