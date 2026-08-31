package com.ribeiroautomoveis.ecommercecarros;

import com.ribeiroautomoveis.ecommercecarros.model.Carro;
import com.ribeiroautomoveis.ecommercecarros.repository.CarroRepository;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private CarroRepository carroRepository;

    @GetMapping("/")
    public String home(
            @RequestParam(required = false) String pesquisa,
            Model model) {

        List<Carro> carros;

        if (pesquisa != null && !pesquisa.isEmpty()) {

            carros = carroRepository
                    .findByModeloContainingIgnoreCase(pesquisa);

        } else {

            carros = carroRepository.findAll();
        }

        model.addAttribute("carros", carros);

        return "index";
    }

    @GetMapping("/admin")
    public String admin(HttpSession session, Model model) {

        if (session.getAttribute("usuarioLogado") == null) {
            return "redirect:/login";
        }

        List<Carro> carros = carroRepository.findAll();

        model.addAttribute("carros", carros);
        model.addAttribute("carro", new Carro());

        return "admin";
    }

    @PostMapping("/salvar")
    public String salvar(
            @Valid Carro carro,
            BindingResult resultado,
            Model model) {

        if (resultado.hasErrors()) {

            model.addAttribute(
                    "carros",
                    carroRepository.findAll());

            model.addAttribute(
                    "carro",
                    carro);

            return "admin";
        }

        carroRepository.save(carro);

        return "redirect:/admin";
    }

    @GetMapping("/login")
    public String login() {

        return "login";
    }

    @PostMapping("/autenticar")
    public String autenticar(
            @RequestParam String usuario,
            @RequestParam String senha,
            HttpSession session) {

        if (usuario.equals("admin")
                && senha.equals("123")) {

            session.setAttribute(
                    "usuarioLogado",
                    usuario);

            return "redirect:/admin";
        }

        return "redirect:/login";
    }

    @GetMapping("/excluir/{id}")
    public String excluir(
            @PathVariable Long id) {

        carroRepository.deleteById(id);

        return "redirect:/admin";
    }

    @GetMapping("/editar/{id}")
    public String editar(
            @PathVariable Long id,
            Model model) {

        Carro carro =
                carroRepository.findById(id)
                        .orElse(null);

        if(carro == null){
            return "redirect:/admin";
        }

        model.addAttribute("carro", carro);

        return "editar";
    }

    @GetMapping("/carro/{id}")
    public String detalhes(
            @PathVariable Long id,
            Model model) {

        Carro carro =
                carroRepository.findById(id)
                        .orElse(null);

        if(carro == null){
            return "redirect:/";
        }

        model.addAttribute("carro", carro);

        return "carro";
    }

    @GetMapping("/logout")
    public String logout(
            HttpSession session) {

        session.invalidate();

        return "redirect:/login";
    }
}