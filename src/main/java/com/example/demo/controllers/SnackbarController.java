package com.example.demo.controllers;

import com.example.demo.models.Snackbar;
import com.example.demo.repo.SnackbarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Controller
public class SnackbarController {

    @Autowired
    private final SnackbarRepository snackbarRepository;

    public SnackbarController(SnackbarRepository snackbarRepository) {
        this.snackbarRepository = snackbarRepository;
    }

    @GetMapping("/snackbar")
    public String stocksMain(Model model) {
        Iterable<Snackbar> snackbars = snackbarRepository.findAll();
        model.addAttribute("snackbar", snackbars);
        return "snackbar/snackbar";
    }

    @GetMapping("/snackbar/add")
    public String conntactinfoAdd(@ModelAttribute("snackbar") Snackbar snackbar, Model model) {
        return "snackbar/snackbar-add";
    }

    @PostMapping("/snackbar/add")
    public Object stocksAdd(@ModelAttribute("snackbar") @Validated Snackbar snackbar, BindingResult bindingResult, Model model) {
        Iterable<Snackbar> snackbars = snackbarRepository.findAll();
        model.addAttribute("snackbar", snackbars);
        if (bindingResult.hasErrors()) return "snackbar";
        snackbarRepository.save(snackbar);
        return "redirect:/snackbar";
    }

    @GetMapping("/snackbar/{id}/edit")
    public String stocksEdit(@ModelAttribute("snackbar") @PathVariable("id") long id, Model model) {
        Snackbar res = snackbarRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Неверный id: " + id));
        model.addAttribute("snackbar", res);
        return "snackbar/snackbar-edit";
    }

    @PostMapping("/snackbar/{id}/edit")
    public String stocksPostUpdate(@PathVariable("id") long id, @ModelAttribute("snackbar")
    Snackbar snackbar, BindingResult bindingResult) {
        snackbar.setId(id);
        if (bindingResult.hasErrors()) {
            return "snackbar/snackbar-edit";
        }
        snackbarRepository.save(snackbar);
        return "redirect:/snackbar";
    }

    @PostMapping("/snackbar/{id}/remove")
    public String stocksPostRemove(@PathVariable("id") long id, Model model) {
        Snackbar snackbar = snackbarRepository.findById(id).orElseThrow();
        snackbarRepository.delete(snackbar);
        return "redirect:/snackbar";
    }
}
