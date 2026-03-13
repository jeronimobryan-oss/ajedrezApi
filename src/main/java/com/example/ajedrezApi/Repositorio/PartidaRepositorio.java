package com.example.ajedrezApi.Repositorio;

import com.example.ajedrezApi.Modelo.PartidaModelo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PartidaRepositorio {
    private List<PartidaModelo> partidas;
    private long idActual;

    public PartidaRepositorio() {
        this.partidas = new ArrayList<>();
        this.idActual = 1;
    }
    //Guardar o Actualizar Partida
    public PartidaModelo save(PartidaModelo partida) {
        // Si el id es 0, es una partida nueva
        if (partida.getIdPartida() == 0) {
            partida.setIdPartida(idActual++);
            this.partidas.add(partida);
        } else {
            // Si ya tiene id, buscamos para actualizar (Endpoint #7: Finalizar)
            for (int i = 0; i < partidas.size(); i++) {
                if (partidas.get(i).getIdPartida() == partida.getIdPartida()) {
                    partidas.set(i, partida);
                    break;
                }
            }
        }
        return partida;
    }
    public List<PartidaModelo> findAll() {
        return this.partidas;
    }
    public Optional<PartidaModelo> findById(long id) {
        return partidas.stream()
                .filter(p -> p.getIdPartida() == id)
                .findFirst();
    }
    public boolean tienePartidasActivas(long jugadorId) {
        return partidas.stream()
                .anyMatch(p -> (p.getJugadorBlancasId() == jugadorId || p.getJugadorNegrasId() == jugadorId)
                        && !p.getEstado().equalsIgnoreCase("finalizada"));
    }
}