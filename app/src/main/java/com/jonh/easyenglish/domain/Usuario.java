package com.jonh.easyenglish.domain;

import java.io.Serializable;

/**
 * Created by Jonh on 27/12/2015.
 */
public class Usuario implements Serializable {

    private int id;
    private String name;
    private String lastName;
    private String email;
    private String pwd;

    public Usuario() {
    }

    public Usuario(String nombre, String apellidos, String email, String pwd) {
        this.email = email;
        this.name = nombre;
        this.lastName = apellidos;
        this.pwd = pwd;
    }

    public int getId() {
        return id;
    }

    public void setId(int i) {
        this.id = i;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

}
