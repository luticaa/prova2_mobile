package com.exercicioPratico.LabProgramacao.controllers;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.exercicioPratico.LabProgramacao.model.AlbumModel;
import com.exercicioPratico.LabProgramacao.model.Usuario;
import com.exercicioPratico.LabProgramacao.service.AlbumService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/*ANTES DE DEFINIRMOS AS CAMADAS É BOM SABER PRA NAO CONFUNDIR:

************************************************************************************************************************
No padrão MVC (Model-View-Controller) puro:

Model → contém dados e possivelmente alguma lógica de negócio simples.
View → a interface do usuário, ex.: HTML, Thymeleaf, JSP.
Controller → recebe requisições, atualiza o Model e decide qual View exibir.
Em MVC puro, muitas vezes o Model já carrega um pouco de lógica, ou o Controller interage diretamente com o banco via DAO.
************************************************************************************************************************
Arquitetura em camadas (Layered):

No Spring Boot moderno, mesmo usando MVC:
Controller → continua a mesma função (apresentação).
Service → nova camada de negócio: validações, regras, processamento, decisões complexas.
Repository → camada de persistência separada (acesso a banco de dados).
Domain / Model → representa o objeto de negócio, mas não tem código de persistência ou lógica de Controller.
Aqui, o "Model" do MVC é mais ou menos equivalente à Domain Layer + DTOs no Spring, mas o Service separa a lógica de
negócio, que no MVC puro muitas vezes ficaria no Controller ou Model.
************************************************************************************************************************

CAMADA DE APRESENTAÇÃO, PRESENTATION LAYER - recebe requisicoes e retorna respostas*/

@Controller//classe controller web
@RequestMapping("/albuns")//prefixo para endpoints
public class AlbumController {//declaracao de classe

    private final AlbumService albumService;
    //declara a dependencia album service, final faz uma vez construida imutavel, service define logicas de negocio

    public AlbumController(AlbumService albumService) {
        this.albumService = albumService; //construtor da classe, Ioc e DI
    }

    // Método para verificar se usuário está logado
    private boolean isUsuarioLogado(HttpSession session) {
        return session.getAttribute("usuarioLogado") != null;
    }

    // Método para redirecionar para login se não estiver logado
    private String verificarLogin(HttpSession session, RedirectAttributes redirectAttrs) {
        if (!isUsuarioLogado(session)) {
            redirectAttrs.addFlashAttribute("erro", "Você precisa fazer login para acessar esta página!");
            return "redirect:/login";
        }
        return null;
    }

    // Galeria com filtro por sessão
    @GetMapping("/galeria")
    public String listarAlbuns(HttpSession session, Model model, RedirectAttributes redirectAttrs) {
        String redirect = verificarLogin(session, redirectAttrs);
        if (redirect != null) return redirect;
        
        String nome = (String) session.getAttribute("filtro");
        List<AlbumModel> albuns;
        if (nome != null && !nome.isEmpty()) {
            albuns = albumService.buscarPorBanda(nome);
        } else {
            albuns = albumService.listar();
        }
        model.addAttribute("albuns", albuns);
        model.addAttribute("usuarioLogado", session.getAttribute("usuarioLogado"));
        return "albuns/galeria";
    }

    // Filtro de álbuns por banda
    @GetMapping("/galeria/filtro")
    public String filtrarAlbuns(@RequestParam String nome, HttpSession session, Model model, RedirectAttributes redirectAttrs) {
        String redirect = verificarLogin(session, redirectAttrs);
        if (redirect != null) return redirect;
        
        session.setAttribute("filtro", nome);
        List<AlbumModel> filtrados = albumService.buscarPorBanda(nome);
        model.addAttribute("albuns", filtrados);
        model.addAttribute("usuarioLogado", session.getAttribute("usuarioLogado"));
        return "albuns/galeria";
    }

    // Limpar filtro da sessão
    @GetMapping("/limpar-filtro")
    public String limparFiltro(HttpSession session, RedirectAttributes redirectAttrs) {
        String redirect = verificarLogin(session, redirectAttrs);
        if (redirect != null) return redirect;
        
        session.removeAttribute("filtro");
        return "redirect:/albuns/galeria";
    }

    // Tela de edição - MOSTRA A TELA
    @GetMapping("/editar/{id}")//endpoint que chama a funcao edicao
    public String editar(@PathVariable Long id, Model model, HttpSession session, RedirectAttributes redirectAttrs) {
        String redirect = verificarLogin(session, redirectAttrs);
        if (redirect != null) return redirect;
        
        //path variable atribui o valor recebido a variavel id!
        AlbumModel album = albumService.buscarPorId(id);
        model.addAttribute("album", album);
        model.addAttribute("usuarioLogado", session.getAttribute("usuarioLogado"));
        //adiciona o atributo ao objeto localizado
        return "albuns/editar"; // TELA baseada no editar.html
    }

    // Salvar edição - SALVA OS DADOS
    @PostMapping("/editar/{id}")//funcao salvar edicao MESMO ENDPOINT respondendo a outra requisicao http
    public String salvarEdicao(@PathVariable Long id, @ModelAttribute AlbumModel album, RedirectAttributes redirectAttrs, HttpSession session) {
        String redirect = verificarLogin(session, redirectAttrs);
        if (redirect != null) return redirect;
        
        //ModelAttribute insere os dados recebidos no post no objeto do tipo AlbumModel
        album.setId(id);
        albumService.salvar(album);
        redirectAttrs.addFlashAttribute("sucessoEdicao", true);//atributo pos o redirect informa sucesso
        return "redirect:/albuns/galeria";//leva de volta a tela de galeria apos edicao concluida
    }

    // Excluir álbum
    @GetMapping("/excluir/{id}")//novo endpoint para exclusao pelo id
    public String excluir(@PathVariable Long id, RedirectAttributes redirectAttrs, HttpSession session) {
        String redirect = verificarLogin(session, redirectAttrs);
        if (redirect != null) return redirect;
        
        albumService.excluir(id);
        redirectAttrs.addFlashAttribute("sucessoExclusao", true);
        /*adiciona um atributo para a exclusao pos redirect flash garante uma vez so
        temos uma div no html principal que mostra esse atributo*/
        return "redirect:/albuns/galeria";//metodo por fim conduz a galeria
    }

    @GetMapping("/novo")//endpoint para novos albuns, atende quem pede esse endpoint
    public String novo(Model model, HttpSession session, RedirectAttributes redirectAttrs) {//onjeto model do spring conecta a controller com a view
        String redirect = verificarLogin(session, redirectAttrs);
        if (redirect != null) return redirect;
        
        model.addAttribute("album", new AlbumModel());// novo objeto criado, adiciona ao model
        model.addAttribute("usuarioLogado", session.getAttribute("usuarioLogado"));
        return "albuns/novo"; // retorna a tela de cadastro ao acessar o endpoint
    }

    @PostMapping("/novo")
    public String salvarNovo(@ModelAttribute AlbumModel album, RedirectAttributes redirectAttrs, HttpSession session) {
        String redirect = verificarLogin(session, redirectAttrs);
        if (redirect != null) return redirect;
        
        //mesmo pensamento anterior adicionamos um atributo redirecionado para mostrar sucesso
        albumService.salvar(album);//service salva o album atribui os dados ao objeto
        redirectAttrs.addFlashAttribute("sucessoNovo", true);//adiciona um flash unico pra informar
        return "redirect:/albuns/galeria";//redireciona pro endpoint principal
    }
}

