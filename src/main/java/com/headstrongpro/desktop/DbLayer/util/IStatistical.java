package com.headstrongpro.desktop.DbLayer.util;

import com.headstrongpro.desktop.core.exception.ModelSyncException;

import java.sql.Date;
import java.util.List;

/**
 * desktop-manager
 * <p>
 * <p>
 * Created by rajmu on 17.05.31.
 */
public interface IStatistical {
    int getAmountOfRecords(String tableName) throws ModelSyncException;
    int getOverduePaymentsCount();
    int getActiveSubscriptionsCount();
}
