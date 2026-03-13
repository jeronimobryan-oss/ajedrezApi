package com.example.ajedrezApi.Modelo;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data

public class PartidaModelo {
    private long idPartida;
    private String ritmo; // Bala, Blitz, Rapid, Classic
    private Long jugadorBlancasId;
    private Long jugadorNegrasId;
    private String apertura;
    private Integer numeroJugadas;
    private String estado; // pendiente, en curso, finalizada
    private int resultado;
    private Integer tiempoTotal;

}
