package com.example.ajedrezApi.Repositorio;

import com.example.ajedrezApi.Modelo.JugadorModelo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class JugadorRepositorio {
    private List<JugadorModelo>jugadores;
    private long idActual;

    public JugadorRepositorio() {
        this.jugadores = new ArrayList<>();
        this.idActual = 1;
        this.initData();
    }

    private void initData() {
        JugadorModelo j1=new JugadorModelo();
        j1.setNombre("Enrique Prieto");
        j1.setApellido("Peñaprieto");
        j1.setNacionalidad("Enriquense");
        j1.setGenero("Genero fluido");
        j1.setEdad(67);
        j1.setELO(3000);
        this.save(j1);

        JugadorModelo j2=new JugadorModelo();
        j2.setNombre("javier");
        j2.setApellido("Zuzunaga");
        j2.setNacionalidad("Libano");
        j2.setGenero("No binario");
        j2.setEdad(79);
        j2.setELO(10);
        this.save(j2);

        JugadorModelo j3 = new JugadorModelo();
        j3.setNombre("Magnus");
        j3.setApellido("Carlsen");
        j3.setELO(2853);
        j3.setNacionalidad("Noruega");
        j3.setGenero("Masculino");
        j3.setEdad(35);
        this.save(j3);

        JugadorModelo j4 = new JugadorModelo();
        j4.setNombre("pin pon es un muñeco");
        j4.setApellido("Muy guapo y de carton se lava la carita con agua y con jabon ¿Se lava la carita? si se lava la carita ");
        j4.setELO(9999);
        j4.setNacionalidad("China");
        j4.setGenero("Therian");
        j4.setEdad(125);
        this.save(j4);
    }
    public List<JugadorModelo> findAll() {
        return this.jugadores;
    }public Optional<JugadorModelo> findById(long id) {
        return jugadores.stream()
                .filter(j -> j.getId() == id)
                .findFirst();
    }
    public void deleteById(long id) {
        jugadores.removeIf(j -> j.getId() == id);
    }
    public JugadorModelo save(JugadorModelo jugador) {
        if (jugador.getId() == 0) {
            jugador.setId(this.idActual);
            this.idActual++;
            this.jugadores.add(jugador);
        } else {
            for (int i = 0; i < jugadores.size(); i++) {
                if (jugadores.get(i).getId() == jugador.getId()) {
                    jugadores.set(i, jugador);
                    return jugador;
                }
            }
        }
        return jugador;
    }
}
