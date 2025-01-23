package com.alura.ForoHubYpe.domain.topico;

public record DatosDetalleTopico(
        Long id,
        String titulo,
        String mensaje,
        String fechaCreacion,
        String estado,
        String autor,
        String curso
) {}
