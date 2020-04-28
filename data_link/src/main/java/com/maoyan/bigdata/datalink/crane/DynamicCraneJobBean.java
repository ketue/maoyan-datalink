package com.maoyan.bigdata.datalink.crane;

public class DynamicCraneJobBean {
    private String name = "";
    private String cronExpr ="";
    private String creator ="zhaobin04";
    private String taskClass ="";
    private String taskMethod="";
    private String taskItem="";
    private String description="default description";
    private String jobId;

    public DynamicCraneJobBean() {
    }

    public DynamicCraneJobBean(String name, String cronExpr, String creator, String taskClass, String taskMethod, String taskItem, String description) {
        this.name = name;
        this.cronExpr = cronExpr;
        this.creator = creator;
        this.taskClass = taskClass;
        this.taskMethod = taskMethod;
        this.taskItem = taskItem;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCronExpr() {
        return cronExpr;
    }

    public void setCronExpr(String cronExpr) {
        this.cronExpr = cronExpr;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getTaskClass() {
        return taskClass;
    }

    public void setTaskClass(String taskClass) {
        this.taskClass = taskClass;
    }

    public String getTaskMethod() {
        return taskMethod;
    }

    public void setTaskMethod(String taskMethod) {
        this.taskMethod = taskMethod;
    }

    public String getTaskItem() {
        return taskItem;
    }

    public void setTaskItem(String taskItem) {
        this.taskItem = taskItem;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }
}
