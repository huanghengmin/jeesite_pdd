<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<html>
<head>
    <title>平台报表管理</title>
    <meta name="decorator" content="default"/>
    <style>
        .divcss5 {
            width: auto;
            text-align: center;
            height: 50px;
            padding: 10px;
            border: 1px solid #0093e8
        }

        .div-inline {
            display: inline;
            height: 50px;
            border: 1px;
            text-align: center;
        }
    </style>
    <script type="text/javascript">
        $(document).ready(function () {
            $.ajax({
                url: '${ctx}/pdd/pddPlatform/report?id=${pddPlatform.id}',
                type: "post",
                contentType: 'application/json;charset=utf-8',
                cache: false,
                success: function (data) {
                    var allCount = data.allCount;
                    var count_0 = data.count_0;
                    var count_1 = data.count_1;
                    var count_2 = data.count_2;
                    var count_3 = data.count_3;
                    var count_4 = data.count_4;
                    var count_5 = data.count_5;
                    var count_6 = data.count_6;
                    var count_201 = data.count_201;
                    var count_st = data.count_st;
                    var count_sf = data.count_sf;
                    var count_tt = data.count_tt;

                    $("#allCount").html(allCount);
                    $("#count_0").html(count_0);
                    $("#count_1").html(count_1);
                    $("#count_2").html(count_2);
                    $("#count_3").html(count_3);
                    $("#count_4").html(count_4);
                    $("#count_5").html(count_5);
                    $("#count_6").html(count_6);
                    $("#count_201").html(count_201);
                    $("#count_st").html(count_st);
                    $("#count_sf").html(count_sf);
                    $("#count_tt").html(count_tt);

                    $("#count_tt").html(count_tt);
                    var count_count = Math.ceil((count_st+count_sf+count_tt)*8/3000+1);
                    $("#count_count").html(count_count);

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
                        credits: {enabled: false},
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
                            data: arr
                        }]
                    }

                    options_chart = {
                        chart: {
                            type: 'column'
                        },
                        title: {
                            text: dataBean_title
                        },
                        credits: {enabled: false},
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
                        credits: {enabled: false},
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
                error: function () {
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

    <div class="divcss5">
        <h3 class="panel-title">
            <span>总订单:(</span>
            <a href="${ctx}/pdd/pddOrder/list?id=${pddPlatform.id}">
                <div id="allCount" class="div-inline"></div>
            </a>
            <span>)&nbsp;&nbsp;</span>

            <span>待揽收:(</span>
            <a href="${ctx}/pdd/pddOrder/list?packageStatus=0&id=${pddPlatform.id}">
                <div id="count_0" class="div-inline"></div>
            </a>
            <span>)&nbsp;&nbsp;</span>

            <span>待中转:(</span>
            <a href="${ctx}/pdd/pddOrder/list?packageStatus=1&id=${pddPlatform.id}">
                <div id="count_1" class="div-inline"></div>
            </a>
            <span>)&nbsp;&nbsp;</span>

            <span>运输途中:(</span>
            <a href="${ctx}/pdd/pddOrder/list?packageStatus=2&id=${pddPlatform.id}">
                <div id="count_2" class="div-inline"></div>
            </a>
            <span>)&nbsp;&nbsp;</span>

            <span>已签收:(</span>
            <span></span>
            <a href="${ctx}/pdd/pddOrder/list?packageStatus=3&id=${pddPlatform.id}">
                <div id="count_3" class="div-inline"></div>
            </a>
            <span>)&nbsp;&nbsp;</span>

            <span>到达待取:(</span>
            <a href="${ctx}/pdd/pddOrder/list?packageStatus=201&id=${pddPlatform.id}">
                <div id="count_201" class="div-inline"></div>
            </a>
            <span>)&nbsp;&nbsp;</span>
        </h3>
    </div>

    <div class="divcss5">
        <h2 class="panel-title">
            <%--<div class="div-inline">--%>
                <span style="color:red">揽件预警订单:(</span>
                <a href="${ctx}/pdd/pddOrder/list?packageStatus=5&id=${pddPlatform.id}">
                    <div id="count_5" class="div-inline"></div>
                </a>
                <span style="color:red">)&nbsp;&nbsp;</span>
            <%--</div>--%>

            <span style="color:red">二次揽件预警订单:(</span>
            <a href="${ctx}/pdd/pddOrder/list?packageStatus=6&id=${pddPlatform.id}">
                <div id="count_6" class="div-inline"></div>
            </a>
            <span style="color:red">)&nbsp;&nbsp;</span>

            <span style="color:red">问题件:(</span>
            <a href="${ctx}/pdd/pddOrder/list?packageStatus=4&id=${pddPlatform.id}">
                <div id="count_4" class="div-inline"></div>
            </a>
            <span style="color:red">)</span>
        </h2>
    </div>

    <div class="divcss5">
        <h4 class="panel-title">
            <span style="color:blue">快递鸟个数=(</span>
            <%--<div class="div-inline">--%>
            <span style="color:blue">申通未签件:(</span>
            <%--<a href="${ctx}/pdd/pddOrder/list?packageStatus=5&id=${pddPlatform.id}">--%>
            <div id="count_st" class="div-inline"></div>
            <%--</a>--%>
            <span style="color:blue">)&nbsp;&nbsp;+</span>
            <%--</div>--%>

            <span style="color:blue">顺风未签件:(</span>
            <%--<a href="${ctx}/pdd/pddOrder/list?packageStatus=5&id=${pddPlatform.id}">--%>
            <div id="count_sf" class="div-inline"></div>
            <%--</a>--%>
            <span style="color:blue">)&nbsp;&nbsp;+</span>

            <span style="color:blue">天天未签件:(</span>
            <%--<a href="${ctx}/pdd/pddOrder/list?packageStatus=5&id=${pddPlatform.id}">--%>
            <div id="count_tt" class="div-inline"></div>
            <%--</a>--%>
            <span style="color:blue">))*8/3000+1=</span>

            <div id="count_count" class="div-inline"></div>
        </h4>
    </div>




    <div class="container">
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