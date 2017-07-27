<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<script type="text/javascript">
	$(function() {
	
		
		$('#userEditForm').form({
			url : '${ctx}/user/update.do',
			onSubmit : function() {
				progressLoad();
				var isValid = $(this).form('validate');
				if (!isValid) {
					progressClose();
				}
				return isValid;
			},
			success : function(result) {
				progressClose();
				result = $.parseJSON(result);
				if (result.success) {
					parent.$.modalDialog.openner_dataGrid.datagrid('reload');//之所以能在这里调用到parent.$.modalDialog.openner_dataGrid这个对象，是因为user.jsp页面预定义好了
					parent.$.modalDialog.handler.dialog('close');
				} else {
					parent.$.messager.alert('错误', result.msg, 'error');
				}
			}
		});
		
		//pubMethod.bind('usertype', 'usertype');
		
		$.extend($.fn.validatebox.defaults.rules, {
		      /*必须和某个字段相等*/
		     equalTo : { validator: function (value, param) { return $(param[0]).val() == value; }, message: '字段不匹配' },		 
		     /*验证手机号码的唯一性及正确性验证(多重验证的实现)*/
			 validateTel : {
				 validator: function (value, param) {
					var rules = $.fn.validatebox.defaults.rules;
					var flag = false;
					if(value.length < 11) {
						rules.validateTel.message = '手机号码不得小于11位';
						return flag;
					}
					
					if(!/^(1)\d{10}$/i.test(value)) {
						rules.validateTel.message = '手机号码格式不对';
						return flag;
					}
					if(value == param[0]) {
						return true;
					}
				 	$.ajax({
				 		url : '${ctx}/user/validateTelphone.do',
				 		type :'post',
				 		async: false,
				 		data : {tel:value},
				 		success : function(result) {
				 			if(result.msg == '1') {  
				 				flag =  true;
				 			}
				 		}
				 	});
				 	
				 	if(!flag) {
				 		rules.validateTel.message = '该号码已被使用！';
				 	}
					return flag;
				 },  
				 message: '手机号码已被使用！'
			}
		 });
		 
		//省级联动触发
			$("#prov").combobox({
				editable:false,
				onChange: function(n) {	
					$.ajax({
						url : '${ctx}/user/getRegion.do?pcode='+n,
						type: "post",
						dataType : 'json',
				       	success: function(data) {
				       		$("#city").combobox({editable:false}).combobox('clear').combobox('loadData',data).combobox('select',data[0].code);
				        },
				        error: function() {
				            alert('城市列表联动异常！');
				        }
					}); 
				}
			});	
	});
</script>
<div class="easyui-layout" data-options="fit:true,border:false">
	<div data-options="region:'center',border:false" title="" style="overflow: hidden;padding: 3px;">
		<form id="userEditForm" method="post">
			<!-- <div class="light-info" style="overflow: hidden;padding: 3px;">
				<div>密码不修改请留空。</div>
			</div> -->
			<input name="id" type="hidden"  value="${user.id}">
			<input name="password" type="hidden"  value="${user.password}">	
			<input name="telephone" type="hidden"  value="${user.telephone}">		
			<table class="grid"> 	
				<tr>
					<td>真实姓名</td>
					<td><input name="username" value="${user.username}" type="text" placeholder="真实姓名" class="easyui-validatebox" data-options="required:true" value=""></td>
					<td>登录账号</td>
					<td>${user.telephone}</td>
				</tr>
				<tr>
					<td>密码(不填则默认原密码)</td>
					<td><input id="password" name="password2"  value="" type="password" placeholder="请输入密码" class="easyui-validatebox" ></td>
					<td>确认密码</td>
					<td>
						<input name="password" placeholder="" type="password" class="easyui-validatebox"  validType="equalTo['#password']" invalidMessage="两次输入密码不匹配">
					</td>
				</tr>
				<tr>
					<td>省份</td>
					<td>
						<select id="prov" name="provCode" class="easyui-combobox" style="width:100px">
							<c:forEach items="${regions}" var="region">
								<option value="${region.code}" <c:if test="${user.provCode == region.code}">selected="selected"</c:if> >${region.name}</option>
							</c:forEach>
						</select>
					</td>
					<td>地市</td>
					<td>
						<select id="city" name="cityCode" required="true" data-options="valueField:'code', textField:'name'" class="easyui-combobox" style="width:100px">
							<c:forEach items="${regions2}" var="region">
								<option value="${region.code}" <c:if test="${user.cityCode == region.code}">selected="selected"</c:if> >${region.name}</option>
							</c:forEach>
						</select>
					</td>
				</tr>
				<tr>
					<td>电子邮件</td>
					<td>
						<input name="email" value="${user.email}" class="easyui-validatebox"  missingMessage="邮件必须填写" 
							validType="email" invalidMessage="请填写正确的邮件格式" data-options="required:true"/>
					</td>
					<td>拥有角色</td>
					<td>
						<select name="role.id" class="easyui-combobox" style="size:10" data-options="width:140,height:29,editable:false,panelHeight:'auto'" data-options="required:true">
							<c:forEach items="${roles}" var="role">
									<option <c:if test="${role.id == user.role.id}">selected="selected"</c:if>value="${role.id}">${role.roleName}</option>
							</c:forEach>
						</select>
					</td>
				</tr>
			</table>
		</form>
	</div>
</div>