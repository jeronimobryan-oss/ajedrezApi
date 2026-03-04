package com.example.ajedrezApi.Modelo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JugadorModelo {
    private long id;
    private String Nombre;
    private String Apellido;
    private String genero;
    private int ELO;
    private Integer edad;
    private String nacionalidad;

}
