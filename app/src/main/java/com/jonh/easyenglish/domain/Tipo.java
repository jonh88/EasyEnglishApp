/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.jonh.easyenglish.domain;

import java.io.Serializable;


public class Tipo implements Serializable {

    private int id;
    private String type;
    
    public Tipo(){} 
    
    public Tipo (String t){
        this.type = t;
    }    

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public String getType() { return type; }

    public void setType(String type) { this.type = type; }
    
    
    
}
