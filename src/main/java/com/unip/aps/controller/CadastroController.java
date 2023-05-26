package com.unip.aps.controller;

import com.unip.aps.model.Usuario;
import com.unip.aps.service.UsuarioService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class CadastroController {
    private final UsuarioService usuarioService;

    public CadastroController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/register")
    public String exibirFormularioCadastro() {
        return "register";
    }


    @PostMapping("/register")
    @ResponseBody
    public String cadastrarUsuario(@RequestParam("nome") String nome,
                                   @RequestParam("email") String email,
                                   @RequestParam("senha") String senha,
                                   @RequestParam("telefone") String telefone,
                                   @RequestParam("tipoUsuario") String tipoUsuario) {

        Usuario usuario = new Usuario();
        usuario.setNome(nome);
        usuario.setEmail(email);
        usuario.setSenha(senha);
        usuario.setTelefone(telefone);

        if (tipoUsuario.equalsIgnoreCase("CONTRATANTE")) {
            usuario.setTipoUsuario("CONTRATANTE");
        } else if (tipoUsuario.equalsIgnoreCase("PRESTADOR_DE_SERVICO")) {
            usuario.setTipoUsuario("PRESTADOR");
        }

        usuarioService.salvarUsuario(usuario);

        return "Usuário cadastrado com sucesso!";
    }

}