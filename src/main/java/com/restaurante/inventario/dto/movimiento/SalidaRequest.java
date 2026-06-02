package com.restaurante.inventario.dto.movimiento;

import jakarta.validation.constraints.NotNull;

public class SalidaRequest {

    @NotNull(message = "El ID del plato es obligatorio")
    private Long platoId;

    public Long getPlatoId() { return platoId; }
    public void setPlatoId(Long platoId) { this.platoId = platoId; }
}
