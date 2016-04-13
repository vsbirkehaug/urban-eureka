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
public class Charge {
    
    private int id;
    private int userId;
    private float amount;
    private String status;
    private String note;
    private String name;
    private int paymentId;
    private String paymentType;
    private Timestamp paymentDate;

    public Timestamp getPaymentDate() {
        return paymentDate;
    }

    public int getPaymentId() {
        return paymentId;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public String getName() {
        return name;
    }

    public Charge(int id, String name, int paymentId, String paymentType, Timestamp paymentDate, float amount, String status, String note) {
        this.id = id;
        this.name = name;
        this.amount = amount;
        this.status = status;
        this.note = note;
        this.paymentId = paymentId;
        this.paymentType = paymentType;
        this.paymentDate = paymentDate;
    }

    
    public Charge(int id, int userId, float amount, String status, String note) {
        this.id = id;
        this.userId = userId;
        this.amount = amount;
        this.status = status;
        this.note = note;
    }

    public Charge(int userId, float amount, String status, String note) {
        this.userId = userId;
        this.amount = amount;
        this.status = status;
        this.note = note;
    }

    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public float getAmount() {
        return amount;
    }

    public String getStatus() {
        return status;
    }

    public String getNote() {
        return note;
    }
    
    
    
}
