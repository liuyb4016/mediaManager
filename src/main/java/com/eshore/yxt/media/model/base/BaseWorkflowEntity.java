package com.eshore.yxt.media.model.base;

import java.util.Map;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * 描述：工作流状态跟踪的基类
 * 
 * @author yxt_majintao
 * @version 1.0
 * @see 参考的JavaDoc
 */
@MappedSuperclass
public class BaseWorkflowEntity {
	
	@Id
	@Column(name="id",columnDefinition="bigint COMMENT '主键ID'")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

	@Column(name="processinstanceid",columnDefinition="varhcar(50) COMMENT '当前流程的实例ID'")
	private String processInstanceId;   //流程实例ID
	
	@Column(name="applyState",columnDefinition="varhcar(255) COMMENT '当前所处节点的名称'")
	private String applyState;          //当前所处的节点（记录当前所处节点的名称）
	
	@Column(name="taskKey",columnDefinition="varhcar(50) COMMENT '当前所处节点的ID(即Key)'")
	private String taskKey;             //当前节点的任务Key（记录当前所处节点的Key）
	
	@Column(name="taskId",columnDefinition="varhcar(50) COMMENT '所处任务ID(审批时使用)'")
	private String taskId;              //所处任务ID(审批时使用)
	
	@Column(name="prevReason",columnDefinition="varhcar(255) COMMENT '上一级审批的理由'")
	private String prevReason;          //上一级审批的理由
	
	// 流程任务
	@JsonIgnore
	@Transient
	private Task task;
	
	@Transient
	@JsonIgnore
	private Map<String, Object> variables;
	
	// 运行中的流程实例
	@Transient
	@JsonIgnore
	private ProcessInstance processInstance;
	
	
	// 流程定义
	@Transient
	@JsonIgnore
	private ProcessDefinition processDefinition;
	
	// 历史的流程实例
	 @Transient
	 @JsonIgnore
	private HistoricProcessInstance historicProcessInstance;

	public String getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

	public Task getTask() {
		return task;
	}

	public void setTask(Task task) {
		this.task = task;
	}

	public Map<String, Object> getVariables() {
		return variables;
	}

	public void setVariables(Map<String, Object> variables) {
		this.variables = variables;
	}

	public ProcessInstance getProcessInstance() {
		return processInstance;
	}

	public void setProcessInstance(ProcessInstance processInstance) {
		this.processInstance = processInstance;
	}

	public ProcessDefinition getProcessDefinition() {
		return processDefinition;
	}

	public void setProcessDefinition(ProcessDefinition processDefinition) {
		this.processDefinition = processDefinition;
	}

	public HistoricProcessInstance getHistoricProcessInstance() {
		return historicProcessInstance;
	}

	public void setHistoricProcessInstance(
			HistoricProcessInstance historicProcessInstance) {
		this.historicProcessInstance = historicProcessInstance;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BaseWorkflowEntity other = (BaseWorkflowEntity) obj;
		if (id != other.id)
			return false;
		return true;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getApplyState() {
		return applyState;
	}

	public void setApplyState(String applyState) {
		this.applyState = applyState;
	}

	public String getTaskKey() {
		return taskKey;
	}

	public void setTaskKey(String taskKey) {
		this.taskKey = taskKey;
	}

	public String getPrevReason() {
		return prevReason;
	}

	public void setPrevReason(String prevReason) {
		this.prevReason = prevReason;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

}
