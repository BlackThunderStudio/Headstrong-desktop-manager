package com.headstrongpro.desktop.controller;

import com.headstrongpro.desktop.DbLayer.DBStats;
import com.headstrongpro.desktop.core.exception.ModelSyncException;

/**
 * desktop-manager
 * <p>
 * <p>
 * Created by rajmu on 17.05.31.
 */
public class Analytics {

    public static int getCount(Table table){
        try {
            return new DBStats().getAmountOfRecords(table.getName());
        } catch (ModelSyncException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static int getOverduePayments(){
        return new DBStats().getOverduePaymentsCount();
    }

    public static int getActiveSubstriptions(){
        return new DBStats().getActiveSubscriptionsCount();
    }

    public enum Table {
        COMPANIES("companies"),
        CLIENTS("clients"),
        COURSES("courses"),
        RESOURCES("resources"),
        EMPLOYEES("employees_headstrong");

        private String name;

        Table(String name){
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}
