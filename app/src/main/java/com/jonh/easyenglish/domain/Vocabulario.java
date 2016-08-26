/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.jonh.easyenglish.domain;

import java.io.Serializable;


/**
 *
 * @author Jonh
 */

public class Vocabulario implements Serializable {
    

    private int id;

    private String english;

    private String spanish;

    private Tipo tipo;
    
    public Vocabulario (){}
    
    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public String getEnglish() { return english; }

    public void setEnglish(String english) { this.english = english; }

    public String getSpanish() { return spanish; }

    public void setSpanish(String spanish) { this.spanish = spanish; }

    public Tipo getTipo() { return tipo; }

    public void setTipo(Tipo tipo) { this.tipo = tipo; }
    
}
