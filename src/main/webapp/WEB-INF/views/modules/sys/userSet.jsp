<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>

<head>
	<title>平台信息管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			//$("#name").focus();
			$("#inputForm").validate({
				submitHandler: function(form){
					loading('正在提交，请稍等...');
					form.submit();
				},
				errorContainer: "#messageBox",
				errorPlacement: function(error, element) {
					$("#messageBox").text("输入有误，请先更正。");
					if (element.is(":checkbox")||element.is(":radio")||element.parent().is(".input-append")){
						error.appendTo(element.parent().parent());
					} else {
						error.insertAfter(element);
					}
				}
			});

            $( "#slider-range-pullRemand" ).slider({
                min: 3,
                max: 23,
                slide: function( event, ui ) {
                    $( "#amount_pullRemand" ).val( ui.value );
                }
            });

            /!*$( "#amount_pullRemand" ).val( $( "#slider-range-pullRemand" ).slider( "value" ) );*!/


            //$('#percent').slider('option', 'value', value+5);


            $( "#slider-range-shamRemand" ).slider({
                min: 3,
                max: 23,
                slide: function( event, ui ) {
                    $( "#amount_shamRemand" ).val( ui.value );
                }
            });

            //$( "#amount_shamRemand" ).val( $( "#slider-range-shamRemand" ).slider( "value" ) );

            $( "#slider-range-orderCycle" ).slider({
                min: 1,
                max: 23,
                slide: function( event, ui ) {
                    $( "#amount_orderCycle" ).val( ui.value );
                }
            });

        });
	</script>
</head>
<body>
	<%--<ul class="nav nav-tabs">
		<li><a href="${ctx}/sys/user/">用户管理列表</a></li>
		<li class="active"><a href="${ctx}/sys/user/form?id=${pddPhone.id}">手机管理<shiro:hasPermission name="pdd:pddPhone:edit">${not empty pddPhone.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="pdd:pddPhone:edit">查看</shiro:lacksPermission></a></li>

	</ul><br/>--%>
	<form:form id="inputForm" modelAttribute="user" action="${ctx}/sys/user/saveSet" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>
		<div class="control-group">
			<form:checkbox path="enablePullRemand" items="${fns:getDictList('status')}" itemLabel="label" itemValue="value" htmlEscape="false" class=""/>
			<label class="control-label">揽件提醒：</label>
			<div class="controls">
				<p>
					<label>离发货:</label>
					<form:input type="text"  path="pullRemand" id="amount_pullRemand" readonly="true" style="border:0; color:#f6931f; font-weight:bold;"/>
					<label>小时（注：快递公司揽件）</label>
				</p>
				<div class="input-xlarge"  id="slider-range-pullRemand" ></div>
			</div>
		</div>
		<div class="control-group">
			<form:checkbox path="enableSecondRemand" items="${fns:getDictList('status')}" itemLabel="label" itemValue="value" htmlEscape="false" class=""/>
			<label class="control-label">二次揽件提醒：</label>
			<div class="controls">
					<p>
						<label>离发货:</label>
						<form:input type="text"   path="secondRemand" id="amount_shamRemand" readonly="true" style="border:0; color:#f6931f; font-weight:bold;"/>
						<label>小时（注：物流无变化）</label>
					</p>
					<div class="input-xlarge" value  id="slider-range-shamRemand" ></div>
			</div>
		</div>

		<div class="control-group">
			<form:checkbox path="enableOrderCycle" items="${fns:getDictList('status')}" itemLabel="label" itemValue="value" htmlEscape="false" class=""/>
			<label class="control-label">订单获取周期：</label>
			<div class="controls">
				<p>
					<label>每:</label>
					<form:input type="text" path="orderCycle" id="amount_orderCycle" readonly="true" style="border:0; color:#f6931f; font-weight:bold;"/>
					<label>小时（注：指每多少小时同步一次订单）</label>
				</p>
				<div class="input-xlarge"   id="slider-range-orderCycle" ></div>
			</div>
		</div>

		<div class="control-group">
			<label class="control-label">短信提醒：</label>
			<div class="controls">
				<form:radiobuttons path="phoneRemand" items="${fns:getDictList('status')}" itemLabel="label" itemValue="value" htmlEscape="false" class=""/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">邮箱提醒：</label>
			<div class="controls">
				<form:radiobuttons path="emailRemand" items="${fns:getDictList('status')}" itemLabel="label" itemValue="value" htmlEscape="false" class=""/>
			</div>
		</div>

		<div class="control-group">
			<label class="control-label">账号期卡：</label>
			<div class="controls">
				<form:input path="cardNumber" htmlEscape="false" rows="4" maxlength="255" class="input-xxlarge "/>
				<a href="http://www.youka.la/category/3C747E6FCDBFA105?1">购买链接</a>
			</div>
		</div>

		<div class="control-group">
			<label class="control-label">短信数量卡：</label>
			<div class="controls">
				<form:input path="noteNumber" htmlEscape="false" rows="4" maxlength="255" class="input-xxlarge "/>
				<a href="http://www.youka.la/category/B3AE84B4A7ACDA75?1">购买链接</a>
			</div>
		</div>

		<div class="control-group">
			<label class="control-label">店铺数量卡：</label>
			<div class="controls">
				<form:input path="platformNumber" htmlEscape="false" rows="4" maxlength="255" class="input-xxlarge "/>
				<a href="http://www.youka.la/category/397127884A6E9CEF?1">购买链接</a>
			</div>
		</div>
		<div class="form-actions">
			<shiro:hasPermission name="sys:user:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>