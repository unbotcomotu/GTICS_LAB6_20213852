package com.example.lab6_20213852.Repository;

import com.example.lab6_20213852.Entity.Mesa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface MesaRepository extends JpaRepository<Mesa, Integer> {
    @Query(nativeQuery = true,value = "select * from mesa where esta_eliminado=0")
    List<Mesa> obtenerMesasNoEliminadas();

    @Query(nativeQuery = true,value = "select * from mesa where esta_eliminado=0 and disponibilidad>0")
    List<Mesa> obtenerMesasDisponibles();

    @Transactional
    @Modifying
    @Query(nativeQuery = true,value = "insert into mesa(nombre,direccion,capacidad,disponibilidad,esta_eliminado) values(?1,?2,?3,?3,false)")
    void agregarMesa(String mesa,String direccion,Integer capacidad);

    @Transactional
    @Modifying
    @Query(nativeQuery = true,value = "update mesa set nombre=?2,disponibilidad=disponibilidad-(capacidad-?3),capacidad=?3,direccion=?4 where id_mesa=?1")
    void editarMesa(Integer idMesa,String nombre,Integer cantidad,String direccion);

    @Query(nativeQuery = true,value = "select m.* from mesa m inner join usuario_has_mesa uhm on m.id_mesa=uhm.id_mesa where uhm.id_usuario_has_mesa=?1")
    Mesa mesaPorMesaPorCliente(Integer idUsuarioHasMesa);

    @Query(nativeQuery = true,value = "select disponibilidad from mesa where id_mesa=?1")
    Integer disponibilidadPorMesa(Integer idMesa);

    @Transactional
    @Modifying
    @Query(nativeQuery = true,value = "update mesa set esta_eliminado=1 where id_mesa=?1")
    void eliminarMesa(Integer idMesa);

    @Transactional
    @Modifying
    @Query(nativeQuery = true,value = "update mesa set disponibilidad=disponibilidad+?2 where id_mesa=?1")
    void aumentarDisponibilidad(Integer idMesa,Integer aumento);
}