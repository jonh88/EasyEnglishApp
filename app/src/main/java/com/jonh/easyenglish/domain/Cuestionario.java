package com.jonh.easyenglish.domain;

/**
 * Created by jonh on 17/08/16.
 */
public class Cuestionario {

    private int id;
    //private Usuario user;
    private int fecha;
    private int numPreguntas;
    private int numFallos;

    public Cuestionario (){}

    public Cuestionario (int date, int preguntas, int fallos){
        this.fecha = date;
        this.numPreguntas = preguntas;
        this.numFallos = fallos;
    }

    public int getId() { return this.id; }

    public void setId(int idCuestionario) { this.id = idCuestionario; }

    public int getFecha() { return fecha; }

    public void setFecha(int fecha) { this.fecha = fecha; }

    public int getNumPreguntas() { return this.numPreguntas; }

    public void setNumPreguntas(int numPreguntas) { this.numPreguntas = numPreguntas; }

    public int getNumFallos() { return this.numFallos; }

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
        return "Numero de preguntas: "+this.numPreguntas + ".\nFallos: "+this.numFallos+".\nCuestionario realizado el: "+convertFecha(this.fecha)+ ".";
    }

}
