<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<script type="text/javascript">


	$(function() {
		
		/* $('#organizationId').combotree({
			url : '${ctx}/organization/tree',
			parentField : 'pid',
			lines : true,
			panelHeight : 'auto'
		}); */
		
	/* 	$('#roleIds').combotree({
		    url: '${ctx}/role/tree',
		    multiple: true,
		    required: true,
		    panelHeight : 'auto'
		}); */
		
		
		$('#userAddForm').form({
			url : '${ctx}/user/add.do',
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
					parent.$.messager.alert('提示', result.msg, 'warning');
				}
			}
		});
		
		/* $('#prov').combobox({
			required: true,
			size : '10',
			onSelect : function() {
				alert(this.id);
			}
		}); */
		
		 $.extend($.fn.validatebox.defaults.rules, {
		      /*必须和某个字段相等*/
		     equalTo : { validator: function (value, param) { return $(param[0]).val() == value; }, message: '字段不匹配' },		 
		     /*验证手机号码的唯一性及正确性验证(多重验证的实现)*/
			 validateTel : {
				 validator: function (value, param) {
					var rules = $.fn.validatebox.defaults.rules;
					var flag = false;
					if(value.length == 0) {
						rules.validateTel.message = '账号不能为空';
						return flag;
					}					
					debugger;
				 	$.ajax({
				 		url : '${ctx}/user/validateTelphone.do',
				 		type :'post',
				 		async: false,
				 		dataType : 'json',
				 		data : {tel:value},
				 		success : function(result) {
				 			if(result.msg == '1') {  
				 				flag =  true;
				 			}
				 		}
				 	});
				 	
				 	if(!flag) {
				 		rules.validateTel.message = '该账号已被使用！';
				 	}
					return flag;
				 },  
				 message: '账号已被使用！'
			}
		 });
		
		//实现多重验证的例子
		/* $.extend($.fn.validatebox.defaults.rules, {
			minLength : {
				validator : function (value, param) {
					var rules = $.fn.validatebox.defaults.rules;
					rules.minLength.message = 'Please enter at least {0} characters.';
					if(!rules.email.validator(value)){
						rules.minLength.message = rules.email.message;
						return false;
					}
					if(!rules.length.validator(value,param)){
						rules.minLength.message = rules.length.message;
						return false;
					}
					return value.length >= param[0];
				},
				message : ''
			}
		}); */
		
		
		//省级联动触发
		$("#prov").combobox({
			editable:false,
			onChange: function(n) {	
				$.ajax({
					url : '${ctx}/region/getCityByProvCode.do?pCode='+n,
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
		<form id="userAddForm" method="post">
			<table class="grid">
				<tr>
					<td>真实姓名</td>
					<td><input name="username" type="text" placeholder="真实姓名" class="easyui-validatebox" data-options="required:true" value=""></td>
					<td>登录账号</td>
					<td><input name="telephone" type="text" placeholder="登录账号" class="easyui-validatebox" data-options="required:true" validType="validateTel" value=""></td>
				</tr>
				<tr>
					<td>密码</td>
					<td><input id="password" name="password"  type="password" placeholder="请输入密码" class="easyui-validatebox" data-options="required:true"></td>
					<td>确认密码</td>
					<td>
						<input placeholder=""  type="password" required="true" class="easyui-validatebox"  validType="equalTo['#password']" invalidMessage="两次输入密码不匹配">
					</td>
				</tr>
				<tr>
					<td>省份</td>
					<td>
						<select id="prov" name="provCode" class="easyui-combobox" style="width:100px">
							<c:forEach items="${regions}" var="region">
								<option value="${region.code}">${region.name}</option>
							</c:forEach>
						</select>
					</td>
					<td>地市</td>
					<td>
						<select id="city" name="cityCode" required="true" data-options="valueField:'code', textField:'name'" class="easyui-combobox" style="width:100px">
							<c:forEach items="${regions2}" var="region">
								<option value="${region.code}">${region.name}</option>
							</c:forEach>
						</select>
					</td>
				</tr>
				<tr>
					<td>电子邮件</td>
					<td>
						<input name="email" class="easyui-validatebox"  missingMessage="邮件必须填写" 
							validType="email" invalidMessage="请填写正确的邮件格式" data-options="required:true"/>
					</td>
					<td>拥有角色</td>
					<td>
						<select name="role.id" class="easyui-combobox" style="size:10" data-options="width:140,height:29,editable:false,panelHeight:'auto'">
							<c:forEach items="${roles}" var="role">
								<option value="${role.id}" >${role.roleName}</option>
							</c:forEach>
						</select>
					</td>
				</tr>
			<!-- 	<tr>
					<td>部门</td>
					<td><select id="organizationId" name="organizationId" style="width: 140px; height: 29px;" class="easyui-validatebox" data-options="required:true"></select></td>
					<td>角色</td>
					<td><select id="roleIds"  name="roleIds"   style="width: 140px; height: 29px;"></select></td>
				</tr> -->
			</table>
		</form>
	</div>
</div>