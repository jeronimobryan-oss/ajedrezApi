package com.example.ajedrezApi.Servicio;

import com.example.ajedrezApi.Modelo.PartidaModelo;
import com.example.ajedrezApi.Repositorio.PartidaRepositorio;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class PartidaServicio {
    private final PartidaRepositorio partidaRepositorio;
    private final JugadorServicio jugadorServicio;
    public PartidaServicio(PartidaRepositorio partidaRepositorio, JugadorServicio jugadorServicio) {
        this.partidaRepositorio = partidaRepositorio;
        this.jugadorServicio = jugadorServicio;
    }
    public PartidaModelo crearPartida(PartidaModelo partida) throws Exception {
        // Validar jugadores usando el servicio inyectado
        if (jugadorServicio.buscarPorId(partida.getJugadorBlancasId()).isEmpty() ||
                jugadorServicio.buscarPorId(partida.getJugadorNegrasId()).isEmpty()) {
            throw new Exception("Uno o ambos jugadores no existen.");
        }
        if (partida.getJugadorBlancasId().equals(partida.getJugadorNegrasId())) {
            throw new Exception("Un jugador no puede jugar contra sí mismo.");
        }
        partida.setEstado("En curso");
        return partidaRepositorio.save(partida);
    }
    public Optional<PartidaModelo> obtenerPorId(long id) {
        return partidaRepositorio.findById(id);
    }
    public PartidaModelo finalizarPartida(long id, PartidaModelo datosFinales) throws Exception {
        Optional<PartidaModelo> partidaOpt = partidaRepositorio.findById(id);

        if (partidaOpt.isEmpty()) {
            throw new Exception("La partida no existe.");
        }
        PartidaModelo partida = partidaOpt.get();
        if (!esTiempoCoherente(partida.getRitmo(), datosFinales.getTiempoTotal())) {
            throw new Exception("El tiempo no es coherente con el ritmo: " + partida.getRitmo());
        }
        partida.setResultado(datosFinales.getResultado());
        partida.setEstado("Finalizada");
        return partidaRepositorio.save(partida);
    }
    private boolean esTiempoCoherente(String ritmo, int tiempo) {
        return switch (ritmo) {
            case "Bala" -> tiempo <= 3;
            case "Blitz" -> tiempo <= 10;
            case "Rapid" -> tiempo <= 60;
            case "Classic" -> tiempo > 60;
            default -> false;
        };
    }
}