<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>订单管理管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			
		});
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
        	return false;
        }
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/pdd/pddOrder/">订单管理列表</a></li>
		<shiro:hasPermission name="pdd:pddOrder:edit"><li><a href="${ctx}/pdd/pddOrder/form">订单管理添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="pddOrder" action="${ctx}/pdd/pddOrder/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>订单编号：</label>
				<form:input path="orderSn" htmlEscape="false" maxlength="30" class="input-medium"/>
			</li>
			<li><label>创建时间：</label>
				<input name="beginCreatedTime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${pddOrder.beginCreatedTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/> - 
				<input name="endCreatedTime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${pddOrder.endCreatedTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
			</li>
			<li><label>地址：</label>
				<form:input path="address" htmlEscape="false" maxlength="100" class="input-medium"/>
			</li>
			<li><label>收件人：</label>
				<form:input path="receiverName" htmlEscape="false" maxlength="45" class="input-medium"/>
			</li>
			<li><label>收件人电话：</label>
				<form:input path="receiverPhone" htmlEscape="false" maxlength="45" class="input-medium"/>
			</li>
			<li><label>快递编号：</label>
				<form:input path="trackingNumber" htmlEscape="false" maxlength="45" class="input-medium"/>
			</li>
			<li><label>发货时间：</label>
				<input name="beginShippingTime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${pddOrder.beginShippingTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/> - 
				<input name="endShippingTime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${pddOrder.endShippingTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
			</li>
			<li><label>订单类型：</label>
				<form:radiobuttons path="isLucky" items="${fns:getDictList('is_lucky_flag')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
			</li>
			<li><label>发货状态：</label>
				<form:radiobuttons path="orderStatus" items="${fns:getDictList('order_status')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
			</li>
			<li><label>退款状态：</label>
				<form:radiobuttons path="refundStatus" items="${fns:getDictList('refund_status')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
			</li>
			<li><label>所属平台：</label>
				<form:select path="pddPlatform.id" class="input-mini">
					<form:option value="" label="全部平台" htmlEscape="false" />
					<form:options items="${pddPlatformList}" itemLabel="shopName" itemValue="id" htmlEscape="false" />
				</form:select>
			</li>
			<li><label>最后更新：</label>
				<input name="beginUpdatedAt" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${pddOrder.beginUpdatedAt}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/> - 
				<input name="endUpdatedAt" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${pddOrder.endUpdatedAt}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
			</li>
			<li><label>包裹状态：</label>
				<form:select path="packageStatus" class="input-medium">
					<form:option value="" label="请选择" htmlEscape="false" />
					<form:options items="${fns:getDictList('package_status')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>更新时间</th>
				<th>订单编号</th>
			<%--	<th>创建时间</th>--%>
				<th>地址</th>
				<th>收件人</th>
				<th>收件人电话</th>
				<th>快递编号</th>
				<th>发货时间</th>
				<th>发货状态</th>
			<%--	<th>退款状态</th>--%>
				<th>所属平台</th>
			<%--	<th>最后更新时间</th>--%>
				<th>包裹状态</th>
				<shiro:hasPermission name="pdd:pddOrder:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="pddOrder">
			<tr>
				<td><a href="${ctx}/pdd/pddOrder/form?id=${pddOrder.id}">
					<fmt:formatDate value="${pddOrder.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</a></td>
				<td>
					${pddOrder.orderSn}
				</td>
				<%--<td>
					<fmt:formatDate value="${pddOrder.createdTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>--%>
				<td>
					${pddOrder.address}
				</td>
				<td>
					${pddOrder.receiverName}
				</td>
				<td>
					${pddOrder.receiverPhone}
				</td>
				<td>
					${pddOrder.trackingNumber}
				</td>
				<td>
					<fmt:formatDate value="${pddOrder.shippingTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${fns:getDictLabel(pddOrder.orderStatus, 'order_status', '')}
				</td>
				<%--<td>
					${fns:getDictLabel(pddOrder.refundStatus, 'refund_status', '')}
				</td>--%>
				<td>
					${pddOrder.pddPlatform.shopName}
				</td>
				<%--<td>
					<fmt:formatDate value="${pddOrder.updatedAt}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>--%>
				<td>
					${fns:getDictLabel(pddOrder.packageStatus, 'package_status', '')}
				</td>
				<shiro:hasPermission name="pdd:pddOrder:edit"><td>
				<a href="${ctx}/pdd/pddOrder/express?id=${pddOrder.id}"><img src="${ctxStatic}/images/sync.png" alt="刷新快递" title="刷新快递"></a>
    				<a href="${ctx}/pdd/pddOrder/form?id=${pddOrder.id}">修改</a>
    				<a href="${ctx}/pdd/pddOrder/view?id=${pddOrder.id}">查看</a>
					<a href="${ctx}/pdd/pddOrder/delete?id=${pddOrder.id}" onclick="return confirmx('确认要删除该订单管理吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>