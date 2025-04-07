/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.model;

import jakarta.persistence.*;
import java.io.Serializable;

/**
 *
 * @author Personal PC
 */
@Entity
@Table(name = "oauth")  // Table name in MySQL
public class Oauth implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int sn;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "code")
    private String code;

    @Column(name = "request_code")
    private String request_code;

    @Column(name = "access_code")
    private String access_code;

    @Column(name = "token_type")
    private String token_type;

    @Column(name = "scope")
    private String scope;

    @Column(name = "exipiration")
    private String exipiration;

    public Oauth(String email, String code, String request_code, String access_code, String token_type, String scope, String exipiration) {
        this.email = email;
        this.code = code;
        this.request_code = request_code;
        this.access_code = access_code;
        this.token_type = token_type;
        this.scope = scope;
        this.exipiration = exipiration;
    }

    public int getSn() {
        return sn;
    }

    public void setSn(int sn) {
        this.sn = sn;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getRequest_code() {
        return request_code;
    }

    public void setRequest_code(String request_code) {
        this.request_code = request_code;
    }

    public String getAccess_code() {
        return access_code;
    }

    public void setAccess_code(String access_code) {
        this.access_code = access_code;
    }

    public String getToken_type() {
        return token_type;
    }

    public void setToken_type(String token_type) {
        this.token_type = token_type;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getExipiration() {
        return exipiration;
    }

    public void setExipiration(String exipiration) {
        this.exipiration = exipiration;
    }
    /*
    @Override
    public String toString() {
        return "User{id=" + id + ", name='" + name + "', email='" + email + "'}";
    }*/
}
