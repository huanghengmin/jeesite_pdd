package com.thinkgem.jeesite.modules.pdd.web;

import com.thinkgem.jeesite.modules.pdd.bean.DataBean;
import com.thinkgem.jeesite.modules.pdd.service.ChartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HighChartsController {

    @Autowired
    ChartService chartService;


    @RequestMapping({"/", "/charts"})
    public String showCharts() {
        return "charts";
    }

    @RequestMapping({"/linechart1"})
    @ResponseBody
    public DataBean showLineChart1() {
        return chartService.getLineChartData1();
    }

    @RequestMapping({"/linechart2"})
    @ResponseBody
    public DataBean showLineChart2() {
        return chartService.getLineChartData2();
    }


    @RequestMapping({"/linechart3"})
    @ResponseBody
    public DataBean showLineChart3() {
        return chartService.getLineChartData3();
    }

  @RequestMapping({"/linechart4"})
    @ResponseBody
    public DataBean showLineChart4() {
        return chartService.getLineChartData4();
    }


}
