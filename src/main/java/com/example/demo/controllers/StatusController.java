package com.example.demo.controllers;

import com.example.demo.models.Status;
import com.example.demo.repo.StatusRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class StatusController {

    private final StatusRepository statusRepository;

    public StatusController(StatusRepository statusRepository) {
        this.statusRepository = statusRepository;
    }

    @GetMapping("/status")
    public String statusMain(Model model) {
        Iterable<Status> status = statusRepository.findAll();
        model.addAttribute("status", status);
        return "status/status";
    }

    @GetMapping("/status/add")
    public String statusAdd(@ModelAttribute("status") Status status, Model model) {
        return "status/status-add";
    }

    @PostMapping("/status/add")
    public Object statusAdd(@ModelAttribute("status") @Validated Status status, BindingResult bindingResult, Model model) {
        Iterable<Status> statuses = statusRepository.findAll();
        model.addAttribute("status", statuses);
        if (bindingResult.hasErrors()) return "status";
        statusRepository.save(status);
        return "redirect:/status";
    }

    @GetMapping("/status/{id}/edit")
    public String statusEdit(@ModelAttribute("status") @PathVariable("id") long id, Model model) {
        Status res = statusRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Неверный id: " + id));
        model.addAttribute("status", res);
        return "status/status-edit";
    }

    @PostMapping("/status/{id}/edit")
    public String statusUpdate(@PathVariable("id") long id, @ModelAttribute("status")
    Status status, BindingResult bindingResult) {
        status.setId(id);
        if (bindingResult.hasErrors()) {
            return "status/status-edit";
        }
        statusRepository.save(status);
        return "redirect:/status";
    }

    @PostMapping("/status/{id}/remove")
    public String statusRemove(@PathVariable("id") long id, Model model) {
        Status status = statusRepository.findById(id).orElseThrow();
        statusRepository.delete(status);
        return "redirect:/status";
    }
}
