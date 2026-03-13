package com.example.ajedrezApi.Modelo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class JugadorModelo {
    private long id;
    private String Nombre;
    private String Apellido;
    private String genero;
    private int ELO;
    private Integer edad;
    private String nacionalidad;
    public JugadorModelo() {
    }
    public JugadorModelo(long id, String nombre, String apellido, String genero, int ELO, Integer edad, String nacionalidad) {
        this.id = id;
        this.Nombre = nombre;
        this.Apellido = apellido;
        this.genero = genero;
        this.ELO = ELO;
        this.edad = edad;
        this.nacionalidad = nacionalidad;
    }
}


