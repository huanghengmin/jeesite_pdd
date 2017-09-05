<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="org.apache.shiro.web.filter.authc.FormAuthenticationFilter" %>
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
        $(document).ready(function() {
            jQuery.validator.setDefaults({
                debug: true,
                success: "valid"
            });

            $( "#RegForm" ).validate({
                submitHandler : function(form) {  //验证通过后的执行方法
                    //当前的form通过ajax方式提交（用到jQuery.Form文件）
                    submit();

                },
                rules: {
                    password: "required",
                    password_again: {
                        equalTo: "#password"
                    },
                    email:{
                        required:true,
                        email:true
                    },
                },
                messages:{
                    email : {
                        required : "请输入邮箱"
                    },password:{
                        required: "密码不能为空"
                    },
                    password_again:{
                        equalTo:"两次密码输入不一致",
                        required: "重复密码不能为空"
                    }
                }
            });
        });


        function showRequest(formData,jqForm,options){
            return $("#RegForm").valid();
        }

        function submit(){
            $('#RegForm').ajaxSubmit({
                type:"post",
                url:"${ctx}/register/emailRegister",
                data:"'email'='" + $("#email").val()+"','password'='"+$("#password").val()+"'",
                beforeSubmit:showRequest,
                success: function(response){
                    //alert(response);
                    var obj = JSON.parse(response);
                    if(obj.success==false){
                        alert(obj.msg);
                    }
                },
                error: function(){
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
            <div class="span5"><h3 class="strong highlight">免费注册新账户</h3>


                <form name="RegForm" method="post" onsubmit="submit()" id="RegForm" action="${ctx}/register/emailRegister"
                       class="form-horizontal">

                    <div class="control-group"><input for="email" id="email" name="email" class="text-large span4"
                                                      placeholder="邮箱地址"
                                                       class="text-large span4"
                                                      <%--data-com.agilebits.onepassword.user-edited="yes"--%>
                                                      type="text"></div>


                    <div class="control-group"><input for="password" name="password" id="password" placeholder="密码"
                                                      class="text-large span4"
                                                      required="true"
                                                      type="password"></div>

                    <div class="control-group"><input for="password_again"  name="password_again" id="password_again" placeholder="确认密码"
                                                      required="true"
                                                      class="text-large span4"
                                                      invalidMessage="两次输入密码不匹配"
                                                      type="password"></div>
                    <div class="row">
                        <div class="span4">
                            <button class="btn btn-action btn-large btn-block"  class="submit" type="submit">注册</button>
                            <br>
                            <span>已经注册过账户？</span><span>&nbsp;<a href="${ctx}/login">现在登录</a></span></div>
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