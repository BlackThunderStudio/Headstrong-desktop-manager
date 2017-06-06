package com.headstrongpro.desktop.DbLayer.util;

import com.headstrongpro.desktop.core.exception.ModelSyncException;

/**
 * Statistical Interface
 */
public interface IStatistical {
    int getAmountOfRecords(String tableName) throws ModelSyncException;

    int getOverduePaymentsCount();

    int getActiveSubscriptionsCount();
}
