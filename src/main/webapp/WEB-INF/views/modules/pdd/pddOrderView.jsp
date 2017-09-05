<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>订单管理管理</title>
	<meta name="decorator" content="default"/>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/pdd/pddOrder/">订单管理列表</a></li>
		<li class="active"><a href="${ctx}/pdd/pddOrder/view?id=${pddOrder.id}">订单管理<shiro:hasPermission name="pdd:pddOrder:view">查看</shiro:hasPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="pddOrder" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>		
		<div class="control-group">
			<label class="control-label">订单编号：</label>
			<div class="controls">
				<form:input readonly="true" path="orderSn" htmlEscape="false" maxlength="30" class="input-xlarge required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">收件人：</label>
			<div class="controls">
				<form:input readonly="true" path="receiverName" htmlEscape="false" maxlength="45" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">收件人电话：</label>
			<div class="controls">
				<form:input readonly="true" path="receiverPhone" htmlEscape="false" maxlength="45" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">快递编号：</label>
			<div class="controls">
				<form:input readonly="true" path="trackingNumber" htmlEscape="false" maxlength="45" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">发货状态：</label>
			<div class="controls">
				<form:radiobuttons readonly="true" path="orderStatus" items="${fns:getDictList('order_status')}" itemLabel="label" itemValue="value" htmlEscape="false" class=""/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">退款状态：</label>
			<div class="controls">
				<form:radiobuttons readonly="true" path="refundStatus" items="${fns:getDictList('refund_status')}" itemLabel="label" itemValue="value" htmlEscape="false" class=""/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">所属平台：</label>
			<div class="controls">
				<form:select readonly="true" path="pddPlatform.id" htmlEscape="false" class="input-mini">
					<form:option value="" label="请选择" htmlEscape="false" />
					<form:options items="${pddPlatformList}" itemLabel="shopName" itemValue="id" htmlEscape="false" />
				</form:select>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">快递公司：</label>
			<div class="controls">
				<form:select path="pddLogistics.logisticsId" htmlEscape="false" class="input-mini">
					<form:option value="" label="请选择" htmlEscape="false" />
					<form:options items="${pddLogisticses}" itemLabel="logisticsCompany" itemValue="logisticsId" htmlEscape="false" />
				</form:select>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">最后更新时间：</label>
			<div class="controls">
				<input readonly="true" name="updatedAt" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate "
					value="<fmt:formatDate value="${pddOrder.updatedAt}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">商品详细：</label>
			<div class="controls">
				<form:textarea readonly="true" path="goodInfo" htmlEscape="false" rows="4" maxlength="2048" class="input-xxlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">快递详细：</label>
			<div class="controls">
				<form:textarea readonly="true" path="logisticInfo" htmlEscape="false" rows="4" maxlength="2048" class="input-xxlarge "/>
			</div>
		</div>
	</form:form>
</body>
</html>