package com.mohit.varma.apnimandiadmin.interfaces;

import com.mohit.varma.apnimandiadmin.model.Orders;

public interface IPhoneCallBackPermission {
    void checkPermissionAndProvideOrders(Orders orders);
}
