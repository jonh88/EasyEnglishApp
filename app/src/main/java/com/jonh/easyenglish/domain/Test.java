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

public class Test implements Serializable {

    private int id;
    //private int idUsr;
    private int fecha;
    private int numPreguntas;
    private int numFallos;
    
    public Test(){}
    
    public Test (int date, int preguntas, int fallos){
        //this.idUsr = usr;
        this.fecha = date;
        this.numPreguntas = preguntas;
        this.numFallos = fallos;
    }

    public int getId() { return id; }

    public void setId(int idTest) { this.id = idTest; }

    //public int getIdUsr() { return idUsr; }

    //public void setIdUsr(int idUsr) { this.idUsr = idUsr; }

    public int getFecha() { return fecha; }

    public void setFecha(int fecha) { this.fecha = fecha; }

    public int getNumPreguntas() { return numPreguntas; }

    public void setNumPreguntas(int numPreguntas) { this.numPreguntas = numPreguntas; }

    public int getNumFallos() { return numFallos; }

    public void setNumFallos(int numFallos) { this.numFallos = numFallos; }

    private String convertFecha(int fecha){
        String año;
        String mes;
        String dia;
        String result = "";
        String sFecha = String.valueOf(fecha);
        año = sFecha.substring(0,4);
        mes = sFecha.substring(4,6);
        dia = sFecha.substring(6,sFecha.length());

        result = dia+"/"+mes+"/"+año;
        return result;
    }

    @Override
    public String toString(){
        return "Numero de preguntas: "+this.numPreguntas + ".\nFallos: "+this.numFallos+".\nTest realizado el: "+convertFecha(this.fecha)+ ".";
    }
    
}
