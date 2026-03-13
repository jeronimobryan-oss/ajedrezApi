package com.example.ajedrezApi.Controlador;

import com.example.ajedrezApi.Modelo.JugadorModelo;
import com.example.ajedrezApi.Repositorio.JugadorRepositorio;
import com.example.ajedrezApi.Repositorio.PartidaRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/jugadores")
public class JugadorControlador {

    @Autowired
    private JugadorRepositorio jugadorRepo;

    @Autowired
    private PartidaRepositorio partidaRepo;
    @PostMapping
    public ResponseEntity<?> crearJugador(@RequestBody JugadorModelo nuevoJugador) {
        if (nuevoJugador.getNombre() == null || nuevoJugador.getNombre().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El nombre completo no puede ser nulo ni vacío.");
        }
        if (nuevoJugador.getELO() < 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El ELO no puede ser negativo.");
        }
        if (nuevoJugador.getNacionalidad() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("La nacionalidad no puede ser nula.");
        }

        JugadorModelo guardado = jugadorRepo.save(nuevoJugador);
        return ResponseEntity.status(HttpStatus.CREATED).body(guardado);
    }
    @GetMapping
    public List<JugadorModelo> obtenerTodos() {
        return jugadorRepo.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable long id) {
        Optional<JugadorModelo> jugador = jugadorRepo.findById(id);
        if (jugador.isPresent()) {
            return ResponseEntity.ok(jugador.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Jugador no encontrado.");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarJugador(@PathVariable long id, @RequestBody JugadorModelo datosActualizados) {
        Optional<JugadorModelo> existente = jugadorRepo.findById(id);

        if (existente.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("El jugador no existe.");
        }

        if (datosActualizados.getNombre() == null || datosActualizados.getNombre().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El nombre no puede quedar vacío.");
        }

        datosActualizados.setId(id); // Aseguramos que mantenga su ID
        JugadorModelo actualizado = jugadorRepo.save(datosActualizados);
        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarJugador(@PathVariable long id) {
        Optional<JugadorModelo> jugador = jugadorRepo.findById(id);

        if (jugador.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("El jugador no existe.");
        }

        // Regla de negocio: No eliminar si tiene partidas activas
        if (partidaRepo.tienePartidasActivas(id)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No puede eliminarse un jugador con partidas activas.");
        }

        jugadorRepo.deleteById(id);
        return ResponseEntity.ok("Jugador eliminado con éxito.");
    }
}