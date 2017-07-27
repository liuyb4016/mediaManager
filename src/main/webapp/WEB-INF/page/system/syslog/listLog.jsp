<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="../../inc.jsp"></jsp:include>
<meta http-equiv="X-UA-Compatible" content="edge" />
	<script type="text/javascript">	
	</script>
<title>角色管理</title>
	<script type="text/javascript">
	var dataGrid;
	$(function() {
		dataGrid = $('#dataGrid').datagrid({
			url : '${ctx}' + '/syslog/list.do',
			striped : true,
			nowrap: false, 
			rownumbers : true,
			pagination : true,
			singleSelect : true,
			idField : 'id',
			pageSize : 10,
			pageList : [ 10, 20, 30, 40],
			frozenColumns : [ [ 
			{
				width : '100',
				title : 'id',
				field : 'id',
				hidden :true
			}, 
			{
				width : '80',
				title : '操作人ID',
				field : 'userId'
			},
			{
				width : '550',
				title : '操作之前的内容',
				field : 'beforeContent'
			},
			{
				width : '300',
				title : '操作之后的内容',
				field : 'afterContent'
			},{
				width : '130',
				title : '操作时间',
				field : 'operateTime'
			} ] ],
			toolbar : '#toolbar'
		});
	});
	
	
	function searchFun() {
		dataGrid.datagrid('load', $.serializeObject($('#searchForm')));
	}
	
	function cleanFun() {
		$('#searchForm input').val('');
		dataGrid.datagrid('load', {});	
	}
		
	</script>
</head>
<body class="easyui-layout" data-options="fit:true,border:false">
	<div data-options="region:'north',border:false" style="height: 30px; overflow: hidden;background-color: #f4f4f4">
		<form id="searchForm">
			<table>
				<tr>
					<th>操作人ID:</th>
					<td><input name="userId" placeholder="请输入操作的用户Id"/></td>
					<th>操作之前的内容:</th>
					<td><input name="beforeContent" placeholder="请输入关键字查询"/></td>
					<th>操作之后的内容:</th>
					<td><input name="afterContent" placeholder="请输入关键字查询"/></td>
					<th>操作时间时间:</th>
					<td><input name="startOpTime" placeholder="点击选择时间" onclick="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd HH:mm:ss'})" readonly="readonly" />至<input  name="endOpTime" placeholder="点击选择时间" onclick="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd HH:mm:ss'})" readonly="readonly" />
					<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon_search',plain:true" onclick="searchFun();">查询</a><a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon_cancel',plain:true" onclick="cleanFun();">清空</a></td>
				</tr>
			</table>
		</form>
	</div>
	
	<div data-options="region:'center',border:false,title:'日志列表'" >
		<table id="dataGrid" data-options="fit:true,border:false"></table>
	</div>
	
	<div id="toolbar" style="display: none;">
	</div>
</body>
</html>