package com.alura.ForoHubYpe.controller;

import com.alura.ForoHubYpe.domain.topico.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/topicos")
public class TopicoController {

    @Autowired
    private TopicoRepository repository;

    @PostMapping
    public ResponseEntity<Topico> registrar(@RequestBody @Validated DatosRegistroTopico datos) {
        if (repository.existsByTituloAndMensaje(datos.titulo(), datos.mensaje())) {
            return ResponseEntity.status(409).build();
        }
        Topico topico = new Topico(datos);
        repository.save(topico);
        return ResponseEntity.ok(topico);
    }

    @GetMapping
    public List<Topico> listar(@RequestParam(defaultValue = "titulo") String orden) {
        return repository.findAll(Sort.by(Sort.Direction.ASC, orden));
    }



    @GetMapping("/{id}")
    public ResponseEntity<Topico> detalle(@PathVariable Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity actualizar(@PathVariable Long id, @RequestBody @Validated DatosActualizarTopico datos) {
        return repository.findById(id).map(topico -> {
            topico.actualizarDatos(datos);
            repository.save(topico);
            return ResponseEntity.ok(topico);
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity eliminar(@PathVariable Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
