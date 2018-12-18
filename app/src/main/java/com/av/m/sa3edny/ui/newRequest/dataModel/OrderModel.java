package com.av.m.sa3edny.ui.newRequest.dataModel;

/**
 * Created by Mina on 11/25/2018.
 */

public class OrderModel {

    private String mobile,description,time,totalPrice;
    private OrderAddress address;

    public OrderModel() {
    }

    public OrderModel(String mobile, String description, String time, String totalPrice, OrderAddress address) {
        this.mobile = mobile;
        this.description = description;
        this.time = time;
        this.totalPrice = totalPrice;
        this.address = address;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void setAddress(OrderAddress address) {
        this.address = address;
    }
}
