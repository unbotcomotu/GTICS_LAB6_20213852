package com.example.lab6_20213852.Repository;

import com.example.lab6_20213852.Entity.UsuarioHasMesa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UsuarioHasMesaRepository extends JpaRepository<UsuarioHasMesa, Integer> {
    @Query(nativeQuery = true,value = "select * from usuario_has_mesa where id_cliente=?1")
    List<UsuarioHasMesa> listaReservasMesasPorCliente(Integer idCliente);


    @Modifying
    @Transactional
    @Query(nativeQuery = true,value = "insert into usuario_has_mesa(id_cliente, id_mesa, fecha_inicio, fecha_fin) values(?1,?2,?3,?4)")
    void reservarMesaPorCliente(Integer idCliente,Integer idMesa,String fechaInicio,String fechaFin);

    @Modifying
    @Transactional
    @Query(nativeQuery = true,value = "delete from usuario_has_mesa where id_usuario_has_mesa=?1")
    void eliminarReservaMesaPorCliente(Integer idMesaPorUsuario);
}