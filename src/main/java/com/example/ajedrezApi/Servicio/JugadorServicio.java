package com.example.ajedrezApi.Servicio;

import com.example.ajedrezApi.Modelo.JugadorModelo;
import com.example.ajedrezApi.Repositorio.JugadorRepositorio;
import com.example.ajedrezApi.Repositorio.PartidaRepositorio;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class JugadorServicio {

    private final JugadorRepositorio jugadorRepo;
    private final PartidaRepositorio partidaRepo;

    public JugadorServicio(JugadorRepositorio jugadorRepo, PartidaRepositorio partidaRepo) {
        this.jugadorRepo = jugadorRepo;
        this.partidaRepo = partidaRepo;
    }
    public List<JugadorModelo> listarTodos() {
        return jugadorRepo.findAll();
    }
    public Optional<JugadorModelo> buscarPorId(long id) {
        return jugadorRepo.findById(id);
    }
    public JugadorModelo guardar(JugadorModelo jugador) throws Exception {
        if (jugador.getNombre() == null || jugador.getNombre().isEmpty()) {
            throw new Exception("El nombre completo es obligatorio.");
        }
        if (jugador.getELO() < 0) {
            throw new Exception("El ELO no puede ser negativo.");
        }
        return jugadorRepo.save(jugador);
    }
    public boolean eliminar(long id) throws Exception {
        if (partidaRepo.tienePartidasActivas(id)) {
            throw new Exception("No se puede eliminar: el jugador tiene partidas en curso.");
        }
        jugadorRepo.deleteById(id);
        return true;
    }
}