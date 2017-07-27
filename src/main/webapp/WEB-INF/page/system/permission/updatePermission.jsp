<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<script type="text/javascript">
	

	$(function() {
		
		$('#pid').combotree({
			url : '${ctx}/permission/list.do?pid=1',
			parentField : 'pid',
			lines : true,
			panelHeight : 'auto',
			onBeforeExpand:function(node){
		     	// 此处就是异步加载地所在
				  if(node) {
					  $('#pid').combotree("tree").tree("options").url = '${ctx}/permission/list.do?pid='+node.id;
				  }
		    }	
		});
		
		/* if ($(':input[name="id"]').val().length > 0) {
			$.post( '${ctx}/permission/get.do', {
				id : $(':input[name="id"]').val(),
			}, function(result) {
				if (result.id != undefined) {
					$('form').form('load', {
						'id' : result.id,
						'name' : result.name,
						'url' : result.url,
						'resourcetype' : result.resourcetype,
						'description' : result.description,
						'icon' : result.icon,
						'seq' : result.seq,
						'cstate':result.cstate
					});
					$('#pid').combotree('setValue',result.pid);
				}
			}, 'json');
		} */
		$('#pid').combotree('setValue','${permission.parent.name}');
		
		$('#resourceEditForm').form({
			url : '${pageContext.request.contextPath}/permission/update.do',
			onSubmit : function() {
				progressLoad();
				var isValid = $(this).form('validate');
				if (!isValid) {
					progressClose();
				}
				var val = $('#pid').combotree('getValue');
				if(val == '${permission.parent.name}') {
					$('#pid').combotree('setValue','${permission.parent.id}');
				}else if(val == '') {
					$('#pid').combotree('setValue','1');
				}
				return isValid;
			},
			success : function(result) {
				progressClose();
				result = $.parseJSON(result);
				if (result.success) {
					parent.$.modalDialog.openner_treeGrid.treegrid('reload');//之所以能在这里调用到parent.$.modalDialog.openner_treeGrid这个对象，是因为resource.jsp页面预定义好了
					//parent.layout_west_tree.tree('reload');
					parent.$.modalDialog.handler.dialog('close');
				}
			}
		});
		
	});
</script>
<div style="padding: 3px;">
	<form id="resourceEditForm" method="post">
		<input name="id" type="hidden" value="${permission.id}">
	    <input name="createTime" type="hidden" value="${permission.createTime}">
		<table  class="grid">		
			<tr>
				<td>权限名称</td>
				<td><input name="name" value="${permission.name}" type="text" placeholder="请输入权限名称" class="easyui-validatebox span2" style="width: 140px; height: 29px;" data-options="required:true" ></td>
				<td>资源类型</td>
				<td><select name="type" class="easyui-combobox" data-options="width:140,height:29,editable:false,panelHeight:'auto'">
							<option value="1" <c:if test="${permission.type == 1}">selected="selected"</c:if>>菜单</option>
							<option value="2" <c:if test="${permission.type == 2}">selected="selected"</c:if>>按钮</option>
				</select></td>
			</tr>
			<tr>
				<td>请求路径</td>
				<td colspan="3"><input name="action" value="${permission.action}" type="text" placeholder="" class="easyui-validatebox span2"  style="width: 377px; height: 29px;" ></td>
			</tr>
			<tr>
				<td>权限标识符</td>
				<td><input name="pkey" value="${permission.pkey}" style="width: 140px; height: 29px;" required="required" ></td>

				<td>排序</td>	
				<td><input name="seq" value="${permission.seq}"  class="easyui-numberspinner" style="width: 140px; height: 29px;" required="required" data-options="min:1,max:100,editable:true"></td>
			</tr>
			<!-- <tr>
				<td>菜单图标</td>
				<td ><input  name="icon" /></td>
				<td>状态</td>
				<td ><select name="cstate" class="easyui-combobox" data-options="width:140,height:29,editable:false,panelHeight:'auto'">
							<option value="0">正常</option>
							<option value="1">停用</option>
				</select></td>
			</tr> -->
			<tr>
				<td>上级资源</td>
				<td colspan="3">
				<input id="pid" name="parent.id" style="width: 200px; height: 29px;" />(不选默认：根菜单)
				<a class="easyui-linkbutton" href="javascript:void(0)" onclick="$('#pid').combotree('clear');" >清空</a></td> 
			</tr>
			<tr>
				<td>备注</td>
				<td colspan="3"><textarea name="remark"  rows="5" cols="50" >${permission.remark}</textarea></td>
			</tr>
		</table>
	</form>
</div>
