package com.example.lab6_20213852.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "mesa")
public class Mesa implements Serializable {
    @Id
    @Column(name = "id_mesa", nullable = false)
    private Integer id;

    @Size(max = 45)
    @NotNull
    @Column(name = "nombre", nullable = false, length = 45)
    private String nombre;

    @Size(max = 45)
    @NotNull
    @Column(name = "direccion", nullable = false, length = 45)
    private String direccion;

    @NotNull
    @Digits(integer = 10,fraction = 0)
    @Column(name = "capacidad", nullable = false)
    private Integer capacidad;

    @Column(name = "disponibilidad", nullable = false)
    private Integer disponibilidad;

    @Column(name = "esta_eliminado", nullable = false)
    private Boolean estaEliminado;

}