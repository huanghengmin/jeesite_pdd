<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<html>
<head>
    <title>${fns:getConfig('productName')} 注册</title>
    <link rel="stylesheet" href="${ctxStatic}/login/login.css"/>
    <link rel="stylesheet" href="${ctxStatic}/login/style.css"/>
    <script src="${ctxStatic}/jquery/jquery-1.8.3.min.js" type="text/javascript"></script>
    <script src="${ctxStatic}/jquery-validation/1.11.1/jquery.validate.min.js" type="text/javascript"></script>
    <script src="${ctxStatic}/jquery-validation/1.11.1/additional-methods.js" type="text/javascript"></script>
    <script language="javascript" type="text/javascript">
        $(document).ready(function () {

            $("#RegForm").validate({
                submitHandler: function (form) {  //验证通过后的执行方法
                    //当前的form通过ajax方式提交（用到jQuery.Form文件）
                    submit();

                },
                rules: {
                    username: "required",
                    cardnumber: "required"
                },
                messages: {
                    username: {
                        required: "请输入登陆名"
                    }, cardnumber: {
                        required: "卡号不能为空"
                    }
                }
            });
        });

        function submit() {
            $('#RegForm').ajaxSubmit({
                type: "post",
                url: "${ctx}/card/index",
                data: "'username'='" + $("#username").val() + "','cardnumber'='" + $("#cardnumber").val() + "'"
                success: function (response) {
                    var obj = JSON.parse(response);
                    if (obj.success == false) {
                        alert(obj.msg);
                    }
                },
                error: function () {
                    alert('请求出错..');
                }
            });
            return false; //此处必须返回false，阻止常规的form提交
        }


    </script>
</head>
<body id="user-index" class="dark">
<div class="container">
    <div class="span10 offset1">
        <div class="main-content dark">
            <div align="center"><label id="loginError" class="error">${message}</label></div>
            <div class="span5"><h3 class="strong highlight">绑定账号登陆卡</h3>


                <form name="RegForm" method="post" onsubmit="submit()" id="RegForm" action="${ctx}/card/index"
                      class="form-horizontal">

                    <div class="control-group"><input for="username" id="username" name="username"
                                                      class="text-large span4"
                                                      placeholder="登陆名"
                                                      class="text-large span4"
                                                      type="text"></div>

                    <div class="control-group"><input for="cardnumber" id="cardnumber" name="cardnumber"
                                                      class="text-large span4"
                                                      placeholder="卡号"
                                                      class="text-large span4"
                                                      type="text"></div>

                    <div class="row">
                        <div class="span4">
                            <button class="btn btn-action btn-large btn-block" class="submit" type="submit">绑定</button>
                            <br>
                            <span>已经绑定过账户？</span><span>&nbsp;<a href="${ctx}/login">现在登录</a></span></div>
                    </div>
                </form>

                <div align="center" id="footer" class="row-fluid">
                    Copyright &copy;
                    2012-${fns:getConfig('copyrightYear')} ${fns:getConfig('productName')} <%--- Powered By <a href="http://jeesite.com" target="_blank">JeeSite</a>--%> ${fns:getConfig('version')}
                </div>

            </div>
        </div>

    </div>

</div>
</body>


</html>