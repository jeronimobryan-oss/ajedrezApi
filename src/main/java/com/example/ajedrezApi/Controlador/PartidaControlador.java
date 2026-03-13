package com.example.ajedrezApi.Controlador;

import com.example.ajedrezApi.Modelo.PartidaModelo;
import com.example.ajedrezApi.Repositorio.PartidaRepositorio;
import com.example.ajedrezApi.Repositorio.JugadorRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/partidas")
public class PartidaControlador {

    @Autowired
    private PartidaRepositorio partidaRepo;

    @Autowired
    private JugadorRepositorio jugadorRepo;
    @PostMapping
    public ResponseEntity<?> crearPartida(@RequestBody PartidaModelo nuevaPartida) {
        if (jugadorRepo.findById(nuevaPartida.getJugadorBlancasId()).isEmpty() ||
                jugadorRepo.findById(nuevaPartida.getJugadorNegrasId()).isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Uno o ambos jugadores no existen.");
        }
        if (nuevaPartida.getJugadorBlancasId().equals(nuevaPartida.getJugadorNegrasId())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Un jugador no puede jugar contra sí mismo.");
        }
        String ritmo = nuevaPartida.getRitmo();
        if (ritmo == null || !(ritmo.equals("Bala") || ritmo.equals("Blitz") || ritmo.equals("Rapid") || ritmo.equals("Classic"))) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Ritmo no válido. Use: Bala, Blitz, Rapid o Classic.");
        }

        nuevaPartida.setEstado("En curso");
        PartidaModelo guardada = partidaRepo.save(nuevaPartida);
        return ResponseEntity.status(HttpStatus.CREATED).body(guardada);
    }
    @PutMapping("/{id}/finalizar")
    public ResponseEntity<?> finalizarPartida(@PathVariable long id, @RequestBody PartidaModelo datosFinales) {
        Optional<PartidaModelo> partidaOpt = partidaRepo.findById(id);

        if (partidaOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("La partida no existe.");
        }

        PartidaModelo partida = partidaOpt.get();

        if (!partida.getEstado().equalsIgnoreCase("En curso")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Solo se pueden finalizar partidas 'En curso'.");
        }

        int tiempo = datosFinales.getTiempoTotal();
        String ritmo = partida.getRitmo();
        boolean tiempoValido = switch (ritmo) {
            case "Bala" -> tiempo <= 3;
            case "Blitz" -> tiempo <= 10;
            case "Rapid" -> tiempo <= 60;
            case "Classic" -> tiempo > 60;
            default -> false;
        };

        if (!tiempoValido) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El tiempo total no es coherente con el ritmo " + ritmo);
        }

        // Actualizar datos finales
        partida.setResultado(datosFinales.getResultado());
        partida.setApertura(datosFinales.getApertura());
        partida.setNumeroJugadas(datosFinales.getNumeroJugadas());
        partida.setTiempoTotal(tiempo);
        partida.setEstado("Finalizada");

        partidaRepo.save(partida);
        return ResponseEntity.ok(partida);
    }

    //Obtener partida por ID
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable long id) {
        return partidaRepo.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
}