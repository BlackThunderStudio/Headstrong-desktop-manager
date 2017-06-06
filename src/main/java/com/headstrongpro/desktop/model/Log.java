package com.headstrongpro.desktop.model;

import java.sql.Date;

/**
 * Log Entity
 */
public class Log implements IModel {
    private int id, headstrongEmpId;
    private String tableName;
    private int itemId;
    private Date date;
    private String timestamp, actionType;


    public Log(int id, int headstrongEmpId, String tableName, int itemId, Date date, String timestamp, String actionType) {
        this.id = id;
        this.headstrongEmpId = headstrongEmpId;
        this.tableName = tableName;
        this.itemId = itemId;
        this.date = date;
        this.timestamp = timestamp;
        this.actionType = actionType;
    }

    public Log(int headstrongEmpId, String tableName, int itemId, String actionType) {
        this.headstrongEmpId = headstrongEmpId;
        this.tableName = tableName;
        this.itemId = itemId;
        this.actionType = actionType;
    }

    public Log(int headstrongEmpId, String tableName, int itemId, Date date, String timestamp, String actionType) {
        this.headstrongEmpId = headstrongEmpId;
        this.tableName = tableName;
        this.itemId = itemId;
        this.date = date;
        this.timestamp = timestamp;
        this.actionType = actionType;
    }

    public Log(String tableName, int itemId, String actionType) {
        this.tableName = tableName;
        this.itemId = itemId;
        this.actionType = actionType;
        headstrongEmpId = -1;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    public int getHeadstrongEmpId() {
        return headstrongEmpId;
    }

    public void setHeadstrongEmpId(int headstrongEmpId) {
        this.headstrongEmpId = headstrongEmpId;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
