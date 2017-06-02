package com.headstrongpro.desktop.DbLayer.util;

import com.headstrongpro.desktop.core.exception.ModelSyncException;

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
