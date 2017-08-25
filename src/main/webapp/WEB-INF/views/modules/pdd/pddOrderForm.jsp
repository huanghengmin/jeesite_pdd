<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>订单管理管理</title>
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
		});
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/pdd/pddOrder/">订单管理列表</a></li>
		<li class="active"><a href="${ctx}/pdd/pddOrder/form?id=${pddOrder.id}">订单管理<shiro:hasPermission name="pdd:pddOrder:edit">${not empty pddOrder.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="pdd:pddOrder:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="pddOrder" action="${ctx}/pdd/pddOrder/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>		
		<div class="control-group">
			<label class="control-label">订单编号：</label>
			<div class="controls">
				<form:input path="orderSn" htmlEscape="false" maxlength="30" class="input-xlarge required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">收件人：</label>
			<div class="controls">
				<form:input path="receiverName" htmlEscape="false" maxlength="45" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">收件人电话：</label>
			<div class="controls">
				<form:input path="receiverPhone" htmlEscape="false" maxlength="45" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">快递编号：</label>
			<div class="controls">
				<form:input path="trackingNumber" htmlEscape="false" maxlength="45" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">发货状态：</label>
			<div class="controls">
				<form:radiobuttons path="orderStatus" items="${fns:getDictList('order_status')}" itemLabel="label" itemValue="value" htmlEscape="false" class=""/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">退款状态：</label>
			<div class="controls">
				<form:radiobuttons path="refundStatus" items="${fns:getDictList('refund_status')}" itemLabel="label" itemValue="value" htmlEscape="false" class=""/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">所属平台：</label>
			<div class="controls">
				<form:select path="pddPlatform.id" htmlEscape="false" class="input-mini">
					<form:option value="" label="请选择" htmlEscape="false" />
					<form:options items="${pddPlatformList}" itemLabel="shopName" itemValue="id" htmlEscape="false" />
				</form:select>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">最后更新时间：</label>
			<div class="controls">
				<input name="updatedAt" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate "
					value="<fmt:formatDate value="${pddOrder.updatedAt}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">商品详细：</label>
			<div class="controls">
				<form:textarea path="goodInfo" htmlEscape="false" rows="4" maxlength="2048" class="input-xxlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">快递详细：</label>
			<div class="controls">
				<form:textarea path="logisticInfo" htmlEscape="false" rows="4" maxlength="2048" class="input-xxlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">包裹状态：</label>
			<div class="controls">
				<form:select path="packageStatus" class="input-xlarge ">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('package_status')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</div>
		</div>
		<div class="form-actions">
			<shiro:hasPermission name="pdd:pddOrder:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>