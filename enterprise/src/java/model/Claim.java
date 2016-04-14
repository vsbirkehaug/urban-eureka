/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.sql.Date;

/**
 *
 * @author Vilde
 */
public class Claim {

    protected int userId;
    protected float amount;
    protected java.sql.Date date;
    protected String rationale;
    protected String status;
    protected int id;
    protected String name;

        /*
    DB STRUCTURE - main constructor
     */
    public Claim(int id, int userId, float amount, java.sql.Date date, String rationale, String status) {
        this.userId = userId;
        this.amount = amount;
        this.date = date;
        this.rationale = rationale;
        this.id = id;
        this.status = status;
    }

    /*
    Slightly altered DB STRUCTURE - takes name of member instead of ID, used for AdminClaim
     */
    public Claim(int id, String name, float amount, java.sql.Date date, String rationale, String status) {
        this.name = name;
        this.amount = amount;
        this.date = date;
        this.rationale = rationale;
        this.id = id;
        this.status = status;
    }
    
    /*
    Used when creating new claims - will insert default status in db
     */
    public Claim(int userId, float amount, java.sql.Date date, String rationale) {
        this.userId = userId;
        this.amount = amount;
        this.date = date;
        this.rationale = rationale;
        this.id = -1;
        this.status = "";
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getUserId() {
        return userId;
    }

    public float getAmount() {
        return amount;
    }

    public Date getDate() {
        return date;
    }

    public String getRationale() {
        return rationale;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
