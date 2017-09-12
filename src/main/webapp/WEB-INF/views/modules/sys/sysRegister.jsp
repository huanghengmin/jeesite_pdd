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

            // 手机号码验证
            jQuery.validator.addMethod("isMobile", function(value, element) {
                var length = value.length;
                var mobile = /^(13[0-9]{9})|(18[0-9]{9})|(14[0-9]{9})|(17[0-9]{9})|(15[0-9]{9})$/;
                return this.optional(element) || (length == 11 && mobile.test(value));
            }, "请正确填写您的手机号码");

            $( "#RegForm" ).validate({
                rules: {
                    password: "required",
                    password_again: {
                        equalTo: "#password"
                    },
                    phone : {
                        required : true,
                        minlength : 11,
                        // 自定义方法：校验手机号在数据库中是否存在
                        // checkPhoneExist : true,
                        isMobile : true
                    }, vcode : {
                        required : true
                    }
                },
                messages:{
                    phone : {
                        required : "请输入手机号",
                        minlength : "确认手机不能小于11个字符",
                        isMobile : "请正确填写您的手机号码"
                    },password:{
                        required: "密码不能为空"
                    },
                    password_again:{
                        equalTo:"两次密码输入不一致",
                        required: "重复密码不能为空"
                    },
                    vcode:{
                        required:"验证码不能为空"
                    }
                }
            });
        });

        //jquery验证手机号码
        function checkSubmitMobil() {
            if ($("#phone").val() == "") {
                alert("手机号码不能为空！");
                $("#phone").focus();
                return false;
            }else {
                if (!$("#phone").val().match(/^(13[0-9]{9})|(18[0-9]{9})|(14[0-9]{9})|(17[0-9]{9})|(15[0-9]{9})$/)) {
                    alert("手机号码格式不正确！");
                    $("#phone").focus();
                    return false;
                }
                return true;
            }
        }

            function ajaxSend(){
                $.ajax({
                    type: "post",
                    url:"${ctx}/register/vcode",
                    cache: false,
                    data:'phone=' + $("#phone").val(),
                    success: function(response){
                        var obj = JSON.parse(response);
                        if(obj.success==false){
                            alert(obj.msg);
                        }
                    },
                    error: function(){
                        alert('请求服务器出错..');
                    }
                });
        }

        function showRequest(formData,jqForm,options){
            return $("#RegForm").valid();
        }

        function submit(){
            $('#RegForm').ajaxSubmit({
                type:"post",
                url:"${ctx}/register/register",
                data:"'phone'='" + $("#phone").val()+"','vcode'='"+ $("#vcode").val()+"','password'='"+$("#password").val()+"'",
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



        function shortMessagraxc() {

            //发送请求
            if(checkSubmitMobil()){
                $('#vcode_btn').attr('disabled',"true");
                    var count = 60;
                    var countdown = setInterval(CountDown, 1000);

                    function CountDown() {
                        $("#vcode_btn").val("" + count + "s");
                        if (count == 0) {
                            $("#vcode_btn").val("重新获取").removeAttr("disabled");
                            clearInterval(countdown);
                        }
                        count--;

                    }

                    ajaxSend();
            }
        };




    </script>
</head>
<body id="user-index" class="dark">
<div class="container">
    <div class="span10 offset1">
        <div class="main-content dark">
            <div align="center"><label id="loginError" class="error">${message}</label></div>
            <div class="span5"><h3 class="strong highlight">免费注册新账户</h3>


                <form name="RegForm" method="post" onsubmit="submit()" id="RegForm"
                       class="form-horizontal">

                    <div class="control-group"><input for="phone" id="phone" name="phone" class="text-large span4"
                                                      placeholder="手机号码"
                                                       class="text-large span4"
                                                      <%--data-com.agilebits.onepassword.user-edited="yes"--%>
                                                      type="text"></div>

                    <div class="control-group">
                        <input autocomplete="off"  class="btn btn-action btn-large btn-block" onclick="shortMessagraxc()"
                               type="button"  value="获取短信验证码" id="vcode_btn" class="axc_yzm02 fr"/>
                    </div>

                    <div class="control-group">
                        <input id="vcode" name="vcode" for="vcode" class="text-large span4" class="g-input w300" placeholder="请输入验证码"
                               type="text">
                    </div>




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