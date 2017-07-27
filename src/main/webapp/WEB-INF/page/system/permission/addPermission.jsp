<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<script type="text/javascript">
	

	$(function() {
		
		$('#pid').combotree({
			url : '${ctx}/permission/list.do?pid=1',			
			parentField :'pid',
			//panelHeight : 'auto',
			lines : true,
			onBeforeExpand:function(node){
		     	// 此处就是异步加载地所在
				  if(node) {
					  $('#pid').combotree("tree").tree("options").url = '${ctx}/permission/list.do?pid='+node.id;
				  }
		    }		
		});
		
		
		$('#resourceAddForm').form({
			url : '${ctx}/permission/add.do',
			onSubmit : function() {
				progressLoad();
				var isValid = $(this).form('validate');
				if (!isValid) {
					progressClose();
				}
			    var val = $('#pid').combotree('getValue');
				if(val == '') {
					$('#pid').combotree('setValue',1);
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
		
		
		//自定义验证规则
		//1.选择菜单时，请求路径可有可无；当请求路径一旦填写，权限标示符也必须填写
		
	});
</script>
<div style="padding: 3px;">
	<form id="resourceAddForm" method="post">
		<table class="grid">
			<tr>
				<td>权限名称</td>
				<td><input name="name" type="text" placeholder="请输入权限名称" class="easyui-validatebox" style="width: 140px; height: 29px;" data-options="required:true" ></td>
				<td>资源类型</td>
				<td><select name="type" class="easyui-combobox" 
						data-options="width:140,height:29,editable:false,panelHeight:'auto'">
							<option value="1">菜单</option>
							<option value="2">按钮</option>
				</select></td>
			</tr>
			<tr>
				<td>请求路径</td>
				<td colspan="3"><input id="action" name="action" type="text" placeholder="请输入请求路径" 
					class="easyui-validatebox span2"   style="width: 377px; height: 29px;" ></td>
			</tr>
			<tr>
				<td>权限标识符(开发人员填写)</td>
				<td><input name="pkey" style="width: 140px; height: 29px;" class="easyui-validatebox"/></td>

				<td>排序</td>
				<td><input name="seq" value="0"  class="easyui-numberspinner" style="width: 140px; height: 29px;"  data-options="min:1,max:100,editable:true"></td>
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
				<input id="pid" name="parent.id"  style="width: 200px; height: 29px;"/>(不选默认：根菜单)
				<a class="easyui-linkbutton" href="javascript:void(0)" onclick="$('#pid').combotree('clear');" >清空</a></td>
			</tr>
			<tr>
				<td>备注</td>
				<td colspan="3"><textarea name="remark" rows="5" cols="50" ></textarea></td>
			</tr>
		</table>
	</form>
</div>