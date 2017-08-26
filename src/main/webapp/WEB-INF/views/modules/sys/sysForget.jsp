<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="org.apache.shiro.web.filter.authc.FormAuthenticationFilter" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<html>
<head>
    <title>${fns:getConfig('productName')} 忘记密码</title>
    <link rel="stylesheet" href="${ctxStatic}/login/login.css" />
</head>
<body id="user-index" class="dark">
<div class="container">
    <div class="row">
        <div class="span10 offset1">
            <div class="main-content dark">
                <div class="">
                    <div class="row">
                        <div class="span5"><h3 class="strong highlight">忘记密码 </h3>
                            <div class="row">
                                <div class="span4"></div>
                            </div>
                            <form name="RegForm" method="post" id="RegForm" action="reg.php?lang=cn"
                                  onsubmit="return InputCheckSigup(this)" class="form-horizontal">

                                <div class="control-group"><input name="email" placeholder="邮箱地址" autocorrect="off"
                                                                  autocapitalize="off" class="text-large span4"
                                                                  type="email"></div>
                                <div class="row">
                                    <div class="span4">
                                        <button id="resetPassword" class="btn btn-action btn-large btn-block">重置密码
                                        </button>
                                        <br>
                                        <a href="${ctx}/register">创建一个新账户</a>&nbsp;|&nbsp;<a
                                            href="${ctx}/login">返回登录页</a></div>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>