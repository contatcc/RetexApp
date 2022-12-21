package com.example.esboco;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;

public class Texto implements Serializable {

    private String id;
    private String titulo;
    private String autor;
    private String tipologia;
    private String genero;
    private String texto;

    public Texto() {
    }

    public Texto(String id, String titulo, String autor, String tipologia, String genero, String texto) {
        this.id = id;
        this.titulo = titulo;
        this.autor = autor;
        this.tipologia = tipologia;
        this.texto = texto;
        this.genero = genero;
    }

    public Texto(String titulo) {
        this.titulo = titulo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getTipologia() {
        return tipologia;
    }

    public void setTipologia(String tipologia) {
        this.tipologia = tipologia;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    @Override
    public String toString() {
        return "Título: " +titulo+ "\nAutor: " +autor+ "\nTipo: " +tipologia + "\nGênero:" + genero;
    }
}
