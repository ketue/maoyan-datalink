package com.maoyan.bigdata.datalink.core;


public class DataLinkModelBean {
    private String key;
    private String model;
    private String craneCron;
    private String craneJobId;


    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getCraneCron() {
        return craneCron;
    }

    public void setCraneCron(String craneCron) {
        this.craneCron = craneCron;
    }

    public String getCraneJobId() {
        return craneJobId;
    }

    public void setCraneJobId(String craneJobId) {
        this.craneJobId = craneJobId;
    }
}
