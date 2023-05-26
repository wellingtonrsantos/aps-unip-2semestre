package com.unip.aps.controller;

import com.unip.aps.model.Servico;
import com.unip.aps.model.Usuario;
import com.unip.aps.service.ServicoService;
import com.unip.aps.service.UsuarioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Objects;

@Controller
public class ServicoController {

    private final ServicoService servicoService;
    private final UsuarioService usuarioService;

    public ServicoController(ServicoService servicoService, UsuarioService usuarioService) {
        this.servicoService = servicoService;
        this.usuarioService = usuarioService;
    }

    @GetMapping("/cadastro-servico")
    public String exibirFormularioCadastroServico() {
        return "cadastro-servico";
    }

    @PostMapping("/cadastro-servico")
    public String cadastrarServico(@RequestParam("titulo") String titulo,
                                   @RequestParam("descricao") String descricao,
                                   @RequestParam("valor") Integer valor,
                                   @RequestParam("emailCadastrado") String emailContratante,
                                   Model model) {

        Servico servico = new Servico();
        servico.setTitulo(titulo);
        servico.setDescricao(descricao);
        servico.setValor(valor);

        Usuario contratante = usuarioService.buscarPorEmail(emailContratante);

        if (Objects.nonNull(contratante)){
            servico.setContratante(contratante);
        } else {
            model.addAttribute("erro", "Email não encontrado");
            return "cadastro-servico";
        }

        servicoService.cadastrar(servico);

        return "redirect:/home";
    }

  /**
   * TODO: Verificar para mandadr os erros dos elses na tela;
   *  ver de usar try catch
   */
  @PostMapping("/selecionar-servico")
  public String salvarPrestadorServico(@RequestParam("servicoId") Integer servicoId,
                                      @RequestParam("emailInput") String emailPrestador) {
      System.out.println("ENTREII: emailPrestador: " + emailPrestador);
      System.out.println("Servico ID: " + servicoId);
        Servico servico = servicoService.buscarPorId(servicoId);

        if (servico != null) {
            Usuario prestador = usuarioService.buscarPorEmail(emailPrestador);

            if (Objects.isNull(prestador)) {
                //model.addAttribute("erro", "Email não encontrado");
                //return "cadastro-servico";
                return "Erro email nao encontrado";
            }

            if (prestador.getTipoUsuario().equalsIgnoreCase("PRESTADOR")) {
                servico.setPrestador(prestador);
                servico.setConcluido(true);
                servicoService.cadastrar(servico);
                return "redirect:/home";
            } else {
                //return "redirect:/servicos-disponiveis?mensagem=Email do prestador inválido!";
                return "Nao é um prestador de servicos!";
            }
        } else {
            //return "redirect:/servicos-disponiveis?mensagem=Serviço não encontrado!";
            return "Serviço não encontrado!";
        }
    }

    @GetMapping("/servicos-disponiveis")
    public ModelAndView exibirServicosDisponiveis() {
        System.out.println("Mostrando Servicos");
        List<Servico> servicosDisponiveis = servicoService.buscarServicosDisponiveis();
        System.out.println("servicosDisponiveis: " + servicosDisponiveis);
        ModelAndView modelAndView = new ModelAndView("servicos-disponiveis");
        modelAndView.addObject("servicosDisponiveis", servicosDisponiveis);
        return modelAndView;
    }

    @GetMapping("/meus-servicos")
    public String exibirFormularioMeusServicos() {
        return "meus-servicos";
    }

    @PostMapping("/meus-servicos")
    public String exibirMeusServicos(@RequestParam("email") String email, Model model) {
        // Aqui você pode usar o email para recuperar as informações relevantes do usuário
        Usuario usuario = usuarioService.buscarPorEmail(email);

        if (usuario != null) {
            // Verifique se o usuário é um contratante ou prestador
            if (usuario.getTipoUsuario().equalsIgnoreCase("contratante")) {
                // Obtém os serviços cadastrados pelo contratante
                List<Servico> servicos = servicoService.obterServicosPorContratante(usuario.getId());
                model.addAttribute("servicos", servicos);
            } else if (usuario.getTipoUsuario().equalsIgnoreCase("prestador")) {
                // Obtém os serviços selecionados pelo prestador
                List<Servico> servicos = servicoService.obterServicosPorPrestador(usuario.getId());
                model.addAttribute("servicos", servicos);
            }
        } else {
            // Usuário não encontrado, você pode adicionar uma mensagem de erro ao modelo
            model.addAttribute("erro", "Usuário não encontrado");
        }

        // Retorne a página que exibe os serviços do usuário
        return "meus-servicos";
    }

    @PostMapping("/atualizar-servico")
    public String atualizarServico(@RequestParam("servicoId") List<Integer> servicoIds,
                                   @RequestParam(value = "concluido", required = false) List<Boolean> concluidos) {

      //Mudar esse if para try catch
      if (servicoIds.size() != concluidos.size()) {
            return "erro";
      }

        for (int i = 0; i < servicoIds.size(); i++) {
            Integer servicoId = servicoIds.get(i);
            Boolean concluido = concluidos.get(i);

            servicoService.atualizarStatusServico(concluido, servicoId);
        }

        return "sucesso";
    }





}
