package com.joselumartos.healthapp.controller;

import com.joselumartos.healthapp.dto.LoginDTO;
import com.joselumartos.healthapp.dto.RegistroDTO;
import com.joselumartos.healthapp.dto.UsuarioDTO;
import com.joselumartos.healthapp.security.JwtUtil;
import com.joselumartos.healthapp.service.UsuarioService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@Controller
public class AuthController {

    private final UsuarioService usuarioService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public AuthController(UsuarioService usuarioService,
                          AuthenticationManager authenticationManager,
                          JwtUtil jwtUtil) {
        this.usuarioService = usuarioService;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    // =========================
    // LOGIN
    // =========================

    @GetMapping("/login")
    public String loginForm(Model model) {
        model.addAttribute("loginDTO", new LoginDTO("", ""));
        return "login";
    }

    @GetMapping("/")
    public String redireccionRaiz() {
        return "redirect:/login";
    }

    @PostMapping("/login")
    public String login(@Valid @ModelAttribute("loginDTO") LoginDTO loginDTO, HttpServletResponse response) {

        // Usamos el email del DTO para autenticar
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDTO.email(), loginDTO.password())
            );

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String token = jwtUtil.generateToken(userDetails);

            // Creamos la cookie con el token
            Cookie cookie = new Cookie("JWT", token);
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            cookie.setMaxAge(60 * 60); // 1 hora
            // cookie.setSecure(true); // activar en HTTPS

            response.addCookie(cookie);

            return "redirect:/home";
        } catch (Exception e) {
            return "redirect:/login?error";
        }
    }

    // =========================
    // REGISTRO
    // =========================

    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("registroDTO", new RegistroDTO("", "", "", "", "ROLE_PACIENTE"));
        return "register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("registroDTO") RegistroDTO registroDTO) {
        usuarioService.registrarNuevoUsuario(registroDTO);

        return "redirect:/login?success";
    }

    // =========================
    // LOGOUT
    // =========================

    @GetMapping("/logout")
    public String logout(HttpServletResponse response) {

        Cookie cookie = new Cookie("JWT", null);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(0); // elimina cookie

        response.addCookie(cookie);

        return "redirect:/login?logout";
    }
}
