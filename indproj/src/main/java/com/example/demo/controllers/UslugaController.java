package com.example.demo.controllers;

import com.example.demo.models.ContactInfo;
import com.example.demo.models.Usluga;
import com.example.demo.repo.UslugaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class UslugaController {
    @Autowired
    private UslugaRepository uslugaRepository;

    @GetMapping("/usluga")
    public String uslugaMain(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Iterable<Usluga> uslugas = uslugaRepository.findAll();
        model.addAttribute("uslugas", uslugas);
        model.addAttribute("isAdmin", authentication.getAuthorities().toString().contains("ADMIN"));
        return "usluga";
    }

    @GetMapping("/usluga/add")
    public String uslugaAdd(@ModelAttribute("usluga") Usluga usluga, Model model) {
        return "usluga-add";
    }

    @PostMapping("/usluga/add")
    public Object uslugaPostAdd(@ModelAttribute("usluga") @Validated Usluga usluga, BindingResult bindingResult, Model model) {
        Iterable<Usluga> uslugas = uslugaRepository.findAll();
        model.addAttribute("uslugas", uslugas);
        if (bindingResult.hasErrors()) return "usluga-add";
        uslugaRepository.save(usluga);
        return "redirect:/usluga";
    }

    @PostMapping("/usluga/filter/result")
    public String uslugaResult(@RequestParam String usluga, Model model) {
        List<Usluga> result = uslugaRepository.findByNameusluga(usluga);
        model.addAttribute("result", result);
        return "usluga-filter";
    }
    @GetMapping("/usluga/{id}")
    public String uslugaDetails(@PathVariable(value = "id") long id, Model model) {
        Optional<Usluga> usluga = uslugaRepository.findById(id);
        ArrayList<Usluga> res = new ArrayList<>();
        usluga.ifPresent(res::add);
        model.addAttribute("usluga", res);
        return "usluga";
    }

    @GetMapping("/usluga/{id}/edit")
    public String uslugaEdit(@PathVariable("id") long id, Model model) {
        Usluga res = uslugaRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Неверный id: " + id));
        model.addAttribute("usluga", res);
        return "usluga-edit";
    }

    @PostMapping("/usluga/{id}/edit")
    public String uslugaUpdate(@PathVariable("id") long id,
                                     @ModelAttribute("usluga")
                                     @Validated Usluga usluga, BindingResult bindingResult) {
        usluga.setId(id);
        if (bindingResult.hasErrors()) {
            return "usluga-edit";
        }
        uslugaRepository.save(usluga);
        return "redirect:/";
    }

    @PostMapping("/usluga/{id}/remove")
    public String uslugaRemove(@PathVariable("id") long id, Model model) {
        Usluga usluga = uslugaRepository.findById(id).orElseThrow();
        uslugaRepository.delete(usluga);
        return "redirect:/";
    }
}