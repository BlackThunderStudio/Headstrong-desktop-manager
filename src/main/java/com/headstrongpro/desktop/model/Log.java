package com.headstrongpro.desktop.model;

import java.sql.Date;
import java.sql.Timestamp;

/**
 * Created by rajmu on 17.05.09.
 */
public class Log {
    private int id, headstrongEmpID;
    private String tableName;
    private int itemID;
    private Date date;
    private String timestamp, actionType;


    public Log(int id, int headstrongEmpID, String tableName, int itemID, Date date, String timestamp, String actionType) {
        this.id = id;
        this.headstrongEmpID = headstrongEmpID;
        this.tableName = tableName;
        this.itemID = itemID;
        this.date = date;
        this.timestamp = timestamp;
        this.actionType = actionType;
    }

    public Log(int headstrongEmpID, String tableName, int itemID, String actionType) {
        this.headstrongEmpID = headstrongEmpID;
        this.tableName = tableName;
        this.itemID = itemID;
        this.actionType = actionType;
    }

    public Log(int headstrongEmpID, String tableName, int itemID, Date date, String timestamp, String actionType) {
        this.headstrongEmpID = headstrongEmpID;
        this.tableName = tableName;
        this.itemID = itemID;
        this.date = date;
        this.timestamp = timestamp;
        this.actionType = actionType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getHeadstrongEmpID() {
        return headstrongEmpID;
    }

    public void setHeadstrongEmpID(int headstrongEmpID) {
        this.headstrongEmpID = headstrongEmpID;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public int getItemID() {
        return itemID;
    }

    public void setItemID(int itemID) {
        this.itemID = itemID;
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
}
