<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="org.apache.shiro.web.filter.authc.FormAuthenticationFilter" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<html>
<head>
    <title>${fns:getConfig('productName')} 注册</title>
    <link rel="stylesheet" href="${ctxStatic}/login/login.css" />
</head>
<body id="user-index" class="dark">
<div class="container">
    <%--<div class="row">--%>
        <div class="span10 offset1">
            <div class="main-content dark">
                <%--<div class="">--%>
                    <%--<div class="row">--%>
                    <%--<div class="header">--%>
                        <%--<div id="messageBox" class="alert alert-error ${empty message ? 'hide' : ''}">--%>
                            <%--<button data-dismiss="alert" class="close">×</button>--%>
                            <div align="center"><label id="loginError" class="error">${message}</label></div>
                        <%--</div>--%>
                    <%--</div>--%>
                        <div class="span5"><h3 class="strong highlight">免费注册新账户</h3>
                            <%--<div class="row">--%>
                                <%--<div class="span4"></div>--%>
                            <%--</div>--%>


                            <form name="RegForm" method="post" id="RegForm" action="${ctx}/register"
                                  onsubmit="return InputCheckSigup(this)" class="form-horizontal">
                                <div class="control-group"><input name="phone" placeholder="手机号码" autocorrect="off"
                                                                  autocapitalize="off" class="text-large span4"
                                                                  data-com.agilebits.onepassword.user-edited="yes"
                                                                  type="text"></div>
                                <div class="control-group"><input name="password" placeholder="密码"
                                                                  class="text-large span4"
                                                                  data-com.agilebits.onepassword.user-edited="yes"
                                                                  type="password"></div>
                                <div class="row">
                                    <div class="span4">
                                        <button class="btn btn-action btn-large btn-block" id="signup">注册</button>
                                        <br>
                                        <span>已经注册过账户？</span><span>&nbsp;<a href="${ctx}/login">现在登录</a></span></div>
                                </div>
                            </form>

                        </div>

                    <%--</div>--%>

                <%--</div>--%>
                <div class="footer">
                    Copyright &copy; 2012-${fns:getConfig('copyrightYear')} <ahref="${pageContext.request.contextPath}${fns:getFrontPath()}">${fns:getConfig('productName')}</a> <%--- Powered By <a href="http://jeesite.com" target="_blank">JeeSite</a>--%> ${fns:getConfig('version')}
                </div>
            </div>

        <%--</div>--%>

    </div>
</div>


</body>


</html>