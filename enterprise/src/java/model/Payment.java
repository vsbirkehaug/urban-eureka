/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.sql.Timestamp;

/**
 *
 * @author Vilde
 */
public class Payment {

    private int id;
    private int userId;
    private int chargeId;
    private String paymentType;
    private float amount;
    private Timestamp date;
    private String note;
    private String status;

    public String getStatus() {
        return status;
    }

    public String getNote() {
        return note;
    }

    public Payment(int id, int userId, int chargeId, float amount, String paymentType, Timestamp date, String note, String status) {
        this.id = id;
        this.chargeId = id;
        this.userId = userId;
        this.paymentType = paymentType;
        this.amount = amount;
        this.date = date;
        this.note = note;
        this.status = status;
    }

    public Payment(int id, int userId, int chargeId, float amount, String paymentType, Timestamp date) {
        this.id = id;
        this.chargeId = id;
        this.userId = userId;
        this.paymentType = paymentType;
        this.amount = amount;
        this.date = date;
    }

    public Payment(int userId, int chargeId, float amount, String paymentType) {
        this.userId = userId;
        this.chargeId = chargeId;
        this.amount = amount;
        this.paymentType = paymentType;
    }

    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public int getChargeId() {
        return chargeId;
    }

    public float getAmount() {
        return amount;
    }

    public Timestamp getDate() {
        return date;
    }

}
