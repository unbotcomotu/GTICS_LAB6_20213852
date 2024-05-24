package com.example.lab6_20213852.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "usuario_has_mesa")
public class UsuarioHasMesa implements Serializable  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario_has_mesa", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_cliente", nullable = false)
    private Usuario cliente;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_mesa", nullable = false)
    private Mesa mesa;

    @NotNull(message = "El campo no puede estar vacío")
    @NotBlank(message = "El campo no puede estar vacío")
    @Size(max = 45)
    @Column(name = "fecha_inicio", length = 45)
    private String fechaInicio;

    @NotNull(message = "El campo no puede estar vacío")
    @NotBlank(message = "El campo no puede estar vacío")
    @Size(max = 45)
    @Column(name = "fecha_fin", length = 45)
    private String fechaFin;

}