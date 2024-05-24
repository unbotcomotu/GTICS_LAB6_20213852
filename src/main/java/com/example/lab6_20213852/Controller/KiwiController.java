package com.example.lab6_20213852.Controller;

import com.example.lab6_20213852.Entity.Mesa;
import com.example.lab6_20213852.Entity.Usuario;
import com.example.lab6_20213852.Entity.UsuarioHasMesa;
import com.example.lab6_20213852.Repository.MesaRepository;
import com.example.lab6_20213852.Repository.UsuarioHasMesaRepository;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Controller
public class KiwiController {

    private final MesaRepository mesaRepository;
    private final UsuarioHasMesaRepository usuarioHasMesaRepository;

    public KiwiController(MesaRepository mesaRepository,
                          UsuarioHasMesaRepository usuarioHasMesaRepository) {
        this.mesaRepository = mesaRepository;
        this.usuarioHasMesaRepository = usuarioHasMesaRepository;
    }

    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @GetMapping("/mesas")
    public String mesas(Model model,HttpSession session){
        Usuario usuario=(Usuario) session.getAttribute("usuario");
        if(usuario!=null){
            model.addAttribute("listaMesas",mesaRepository.obtenerMesasNoEliminadas());
        }else {
            model.addAttribute("listaMesas",mesaRepository.obtenerMesasDisponibles());
        }
        return "mesas";
    }

    @GetMapping("/reservas")
    public String prestamos(Model model, HttpSession session){
        Usuario usuario=(Usuario) session.getAttribute("usuario");
        if(usuario.getRol().getId()==3){
            model.addAttribute("listaReservas",usuarioHasMesaRepository.listaReservasMesasPorCliente(usuario.getId()));
        }else if(usuario.getRol().getId()==2){
            model.addAttribute("listaReservas",usuarioHasMesaRepository.findAll());
        }
        return "reservas";
    }


    @GetMapping("/vistaAgregarMesa")
    public String vistaAgregarMesa(Model model, @ModelAttribute("mesa") Mesa mesa){
        return "agregarEditarMesa";
    }

    @GetMapping("/vistaEditarMesa")
    public String vistaEditarMesa(Model model, @ModelAttribute("mesa") Mesa mesa, HttpSession session,
                                         @RequestParam("id")Integer id){
        Optional<Mesa> optMesa=mesaRepository.findById(id);
        if(optMesa.isPresent()){
            mesa=optMesa.get();
            model.addAttribute("mesa",optMesa);
            return "agregarEditarMesa";
        }else {
            return "redirect:/mesas";
        }
    }

    @PostMapping("/actualizarListaMesas")
    public String actualizarListaMesas(@ModelAttribute("mesa") @Valid Mesa mesa, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return "agregarEditarMesa";
        }else {
            if(mesa.getId()!=null){
                mesaRepository.editarMesa(mesa.getId(),mesa.getNombre(),mesa.getCapacidad(),mesa.getDireccion());
            }else{
                mesaRepository.agregarMesa(mesa.getNombre(),mesa.getDireccion(),mesa.getCapacidad());
            }
            return "redirect:/mesas";
        }
    }

    @GetMapping("/eliminarMesa")
    public String eliminarMesa(@RequestParam("id")Integer id){
        mesaRepository.eliminarMesa(id);
        return "redirect:/mesas";
    }

    @GetMapping("/vistaAgregarReserva")
    public String vistaAgregarReserva(Model model,@ModelAttribute("mesaPorUsuario") UsuarioHasMesa mesaPorUsuario){
        model.addAttribute("listaMesas",mesaRepository.obtenerMesasNoEliminadas());
        return "agregarReserva";
    }

    @PostMapping("/agregarReserva")
    public String agregarReserva(HttpSession session,Model model,@ModelAttribute("mesaPorUsuario") @Valid UsuarioHasMesa mesaPorUsuario, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            model.addAttribute("listaMesas",mesaRepository.obtenerMesasNoEliminadas());
            return "agregarReserva";
        }
        Usuario cliente=(Usuario) session.getAttribute("usuario");
        Mesa mesa=mesaPorUsuario.getMesa();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        Timestamp fechaInicio= Timestamp.valueOf(LocalDateTime.parse(mesaPorUsuario.getFechaInicio(), formatter));
        Timestamp fechaFin= Timestamp.valueOf(LocalDateTime.parse(mesaPorUsuario.getFechaFin(), formatter));
        Integer disponibilidad=mesaRepository.disponibilidadPorMesa(mesa.getId());
        Boolean esValido=true;
        if(fechaFin.before(fechaInicio)){
            model.addAttribute("errorFecha","La fecha de inicio no puede ser después de la de culminación");
            esValido=false;
        }
        if(disponibilidad==0){
            model.addAttribute("errorStock","No hay disponibilidad suficiente");
            esValido=false;
        }
        if(esValido){
            usuarioHasMesaRepository.reservarMesaPorCliente(cliente.getId(),mesa.getId(),fechaInicio.toString(),fechaFin.toString());
            mesaRepository.aumentarDisponibilidad(mesa.getId(),-1);
            return "redirect:/reservas";
        }else {
            model.addAttribute("listaMesas",mesaRepository.obtenerMesasNoEliminadas());
            return "agregarReserva";
        }
    }

    @GetMapping("/cancelarReserva")
    public String cancelarReserva(@RequestParam("id")Integer id){
        Mesa mesa= mesaRepository.mesaPorMesaPorCliente(id);
        usuarioHasMesaRepository.eliminarReservaMesaPorCliente(id);
        mesaRepository.aumentarDisponibilidad(mesa.getId(),1);
        return "redirect:/reservas";
    }
}

