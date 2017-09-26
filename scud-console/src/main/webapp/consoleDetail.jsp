<%@page language="java" trimDirectiveWhitespaces="true" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8"/>
    <title>Scud - 控制台</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <!-- basic styles -->
    <link href="assets/css/bootstrap.min.css" rel="stylesheet"/>
    <link rel="stylesheet" href="assets/css/font-awesome.min.css"/>

    <!--[if IE 7]>
    <link rel="stylesheet" href="assets/css/font-awesome-ie7.min.css"/>
    <![endif]-->

    <link rel="stylesheet" href="assets/css/ace.min.css"/>
    <link rel="stylesheet" href="assets/css/ace-rtl.min.css"/>
    <link rel="stylesheet" href="assets/css/ace-skins.min.css"/>

    <!--[if lte IE 8]>
    <link rel="stylesheet" href="assets/css/ace-ie.min.css"/>
    <![endif]-->

    <!-- inline styles related to this page -->

    <!-- ace settings handler -->

    <script src="assets/js/ace-extra.min.js"></script>

    <!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->

    <!--[if lt IE 9]>
    <script src="assets/js/html5shiv.js"></script>
    <script src="assets/js/respond.min.js"></script>
    <![endif]-->
</head>

<body>
<div class="navbar navbar-default" id="navbar">
    <script type="text/javascript">
        try {
            ace.settings.check('navbar', 'fixed')
        } catch (e) {
        }
    </script>

    <div class="navbar-container" id="navbar-container">
        <div class="navbar-header pull-left">
            <a href="#" class="navbar-brand">
                <small>
                    <i class="icon-leaf"></i>
                    Scud控制台
                </small>
            </a><!-- /.brand -->
        </div><!-- /.navbar-header -->

        <div class="navbar-header pull-right" role="navigation">
            <ul class="nav ace-nav">


                <li class="light-blue">
                    <a data-toggle="dropdown" href="#" class="dropdown-toggle">
                        <img class="nav-user-photo" src="assets/avatars/user.jpg" alt="Jason's Photo"/>
                        夏先生
                        <i class="icon-caret-down"></i>
                    </a>

                    <ul class="user-menu pull-right dropdown-menu dropdown-yellow dropdown-caret dropdown-close">
                        <li>
                            <a href="#">
                                <i class="icon-cog"></i>
                                设置
                            </a>
                        </li>

                        <li>
                            <a href="#">
                                <i class="icon-user"></i>
                                个人资料
                            </a>
                        </li>

                        <li class="divider"></li>

                        <li>
                            <a href="#">
                                <i class="icon-off"></i>
                                退出
                            </a>
                        </li>
                    </ul>
                </li>
            </ul><!-- /.ace-nav -->
        </div><!-- /.navbar-header -->
    </div><!-- /.container -->
</div>

<div class="main-container" id="main-container">
    <script type="text/javascript">
        try {
            ace.settings.check('main-container', 'fixed')
        } catch (e) {
        }
    </script>

    <div class="main-container-inner">
        <a class="menu-toggler" id="menu-toggler" href="#">
            <span class="menu-text"></span>
        </a>

        <div class="sidebar" id="sidebar">
            <script type="text/javascript">
                try {
                    ace.settings.check('sidebar', 'fixed')
                } catch (e) {
                }
            </script>

            <div class="sidebar-shortcuts" id="sidebar-shortcuts">
                <div class="sidebar-shortcuts-large" id="sidebar-shortcuts-large">
                    <button class="btn btn-success">
                        <i class="icon-signal"></i>
                    </button>

                    <button class="btn btn-info">
                        <i class="icon-pencil"></i>
                    </button>

                    <button class="btn btn-warning">
                        <i class="icon-group"></i>
                    </button>

                    <button class="btn btn-danger">
                        <i class="icon-cogs"></i>
                    </button>
                </div>

                <div class="sidebar-shortcuts-mini" id="sidebar-shortcuts-mini">
                    <span class="btn btn-success"></span>

                    <span class="btn btn-info"></span>

                    <span class="btn btn-warning"></span>

                    <span class="btn btn-danger"></span>
                </div>
            </div><!-- #sidebar-shortcuts -->

            <ul class="nav nav-list">
                <li>
                    <a href="index.html">
                        <i class="icon-desktop"></i>
                        <span class="menu-text"> 运用中心 </span>
                    </a>
                </li>
                <li class="active">
                    <a href="consoleList.htm">
                        <i class="icon-dashboard"></i>
                        <span class="menu-text"> 监控中心 </span>
                    </a>
                </li>
                <li>
                    <a href="#" class="dropdown-toggle">
                        <i class="icon-tag"></i>
                        <span class="menu-text"> 更多页面 </span>

                        <b class="arrow icon-angle-down"></b>
                    </a>

                    <ul class="submenu">
                        <li>
                            <a href="profile.html">
                                <i class="icon-double-angle-right"></i>
                                用户信息
                            </a>
                        </li>

                        <li>
                            <a href="login.html">
                                <i class="icon-double-angle-right"></i>
                                登录 &amp; 注册
                            </a>
                        </li>
                    </ul>
                </li>
            </ul><!-- /.nav-list -->

            <div class="sidebar-collapse" id="sidebar-collapse">
                <i class="icon-double-angle-left" data-icon1="icon-double-angle-left" data-icon2="icon-double-angle-right"></i>
            </div>

            <script type="text/javascript">
                try {
                    ace.settings.check('sidebar', 'collapsed')
                } catch (e) {
                }
            </script>
        </div>

        <div class="main-content">
            <div class="breadcrumbs" id="breadcrumbs">
                <script type="text/javascript">
                    try {
                        ace.settings.check('breadcrumbs', 'fixed')
                    } catch (e) {
                    }
                </script>

                <ul class="breadcrumb">
                    <li>
                        <i class="icon-home home-icon"></i>
                        <a href="consoleList.htm">监控中心</a>
                    </li>
                    <li>
                        <a href="consoleMethodList.htm?appName=${requestScope.appName}">${requestScope.appName}</a>
                    </li>
                    <li class="active">监控图表</li>
                </ul><!-- .breadcrumb -->

                <div class="nav-search" id="nav-search">
                    <form class="form-search">
                        <span class="input-icon">
                            <input type="text" placeholder="Search ..." class="nav-search-input" id="nav-search-input" autocomplete="off"/>
                            <i class="icon-search nav-search-icon"></i>
                        </span>
                    </form>
                </div><!-- #nav-search -->
            </div>

            <div class="page-content">
                <div class="row">
                    <div class="col-xs-12" id="chart" style="height:300px;"></div>
                </div>
            </div>
        </div>
        <a href="#" id="btn-scroll-up" class="btn-scroll-up btn btn-sm btn-inverse">
            <i class="icon-double-angle-up icon-only bigger-110"></i>
        </a>
    </div>
</div>
<!-- basic scripts -->

<!--[if !IE]> -->

<script src="assets/js/jquery-2.0.3.min.js"></script>

<!-- <![endif]-->

<!--[if IE]>
<script src="assets/js/jquery-2.0.3.min.js"></script>
<![endif]-->

<!--[if !IE]> -->

<script type="text/javascript">
    window.jQuery || document.write("<script src='assets/js/jquery-2.0.3.min.js'>" + "<" + "script>");
</script>

<!-- <![endif]-->

<!--[if IE]>
<script type="text/javascript">
    window.jQuery || document.write("<script src='assets/js/jquery-1.10.2.min.js'>" + "<" + "script>");
</script>
<![endif]-->

<script type="text/javascript">
    if ("ontouchend" in document) document.write("<script src='assets/js/jquery.mobile.custom.min.js'>" + "<" + "script>");
</script>
<script src="assets/js/bootstrap.min.js"></script>

<!-- page specific plugin scripts -->

<!--[if lte IE 8]>
<script src="assets/js/excanvas.min.js"></script>
<![endif]-->

<script src="assets/js/jquery.slimscroll.min.js"></script>

<!-- ace scripts -->

<script src="assets/js/ace-elements.min.js"></script>
<script src="assets/js/ace.min.js"></script>
<script src="assets/js/echarts.min.js"></script>

<script type="text/javascript">
    jQuery(function ($) {
        $('#recent-box [data-rel="tooltip"]').tooltip({placement: tooltip_placement});
        function tooltip_placement(context, source) {
            var $source = $(source);
            var $parent = $source.closest('.tab-content')
            var off1 = $parent.offset();
            var w1 = $parent.width();
            var off2 = $source.offset();
            if (parseInt(off2.left) < parseInt(off1.left) + parseInt(w1 / 2)) return 'right';
            return 'left';
        }
    })
    myChart = echarts.init(document.getElementById('chart'));
    tp50 = ${requestScope.data.TP50};
    tp90 = ${requestScope.data.TP90};
    tp99 = ${requestScope.data.TP99};
    tp999 =${requestScope.data.TP999};
    tpmax = ${requestScope.data.MAX};
    time = ${requestScope.data.buildTime()};
    lastTime = time[time.length - 1];
    option = {
        title: {
            text: '${requestScope.data.title}'
        },
        tooltip: {
            trigger: 'axis'
        },
        color: ['#48cda6', '#11abff', '#968ade', '#fd87ab', '#c23531'],
        legend: {
            data: ['TP50', 'TP90', 'TP99', 'TP999', 'MAX']
        },
        grid: {
            left: '3%',
            right: '4%',
            bottom: '3%',
            containLabel: true
        },
        toolbox: {
            show: true,
            feature: {
                mark: {show: true},
                dataView: {show: true, readOnly: false},
                saveAsImage: {}
            }
        },
        xAxis: {
            type: 'category',
            boundaryGap: false,
            data: time
        },
        yAxis: {
            type: 'value'
        },
        series: [
            {
                name: 'TP50',
                type: 'line',
                smooth: true,
                data: tp50
            },
            {
                name: 'TP90',
                type: 'line',
                smooth: true,
                data: tp90
            },
            {
                name: 'TP99',
                type: 'line',
                smooth: true,
                data: tp99
            },
            {
                name: 'TP999',
                type: 'line',
                smooth: true,
                data: tp999
            },
            {
                name: 'MAX',
                type: 'line',
                smooth: true,
                data: tpmax
            }
        ]
    };
    myChart.setOption(option);

    setInterval(function () {
        jQuery.ajax({
            url: "getMonitorData.htm?appName=${requestScope.appName}&key=${requestScope.data.title}&time=" + lastTime,
            type: 'get',
            dataType: 'json',
            success: function (jsons) {
                console.log(jsons);
                if (!!jsons.time) {
                    if (tp50.length >= 15) {
                        tp50.shift();
                        tp90.shift();
                        tp99.shift();
                        tp999.shift();
                        tpmax.shift();
                        time.shift();
                    }
                    tp50.push(jsons.TP50[0]);
                    tp90.push(jsons.TP90[0]);
                    tp99.push(jsons.TP99[0]);
                    tp999.push(jsons.TP999[0]);
                    tpmax.push(jsons.MAX[0]);
                    time.push(jsons.time[0]);
                    myChart.setOption({
                        xAxis: {
                            data: time
                        },
                        series: [
                            {data: tp50}, {data: tp90}, {data: tp99}, {data: tp999}, {data: tpmax}
                        ]
                    });
                    lastTime = jsons.time[0];
                }
            }
        });
    }, 30000);
</script>
</body>
</html>


