package com.example.demo.controllers;

import com.example.demo.models.Safers;
import com.example.demo.repo.SafersRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Optional;

@Controller
public class SafersController {
    private final SafersRepository safersRepository;

    public SafersController(SafersRepository safersRepository) {
        this.safersRepository = safersRepository;
    }

    @GetMapping("/safers")
    public String safersMain(Model model) {
        Iterable<Safers> safers = safersRepository.findAll();
        model.addAttribute("safers", safers);
        return "safers";
    }

    @GetMapping("/safers/add")
    public String safersAdd(@ModelAttribute("safers") Safers safers, Model model) {
        return "safers-add";
    }

    @PostMapping("/safers/add")
    public Object safersPostAdd(@ModelAttribute("safers") @Validated Safers safers, BindingResult bindingResult, Model model) {
        Iterable<Safers> safers2 = safersRepository.findAll();
        model.addAttribute("safers", safers2);
        if (bindingResult.hasErrors()) return "safers";
        safersRepository.save(safers);
        return "redirect:/safers";
    }

    @PostMapping("/safers/filter/result")
    public String safersResult(@RequestParam long id, Model model) {
        Optional<Safers> result = safersRepository.findById(id);
        model.addAttribute("result", result);
        return "safers-filter";
    }
    @GetMapping("/safers/{id}")
    public String safersDetails(@PathVariable(value = "id") long id, Model model) {
        Optional<Safers> safers = safersRepository.findById(id);
        ArrayList<Safers> res = new ArrayList<>();
        safers.ifPresent(res::add);
        model.addAttribute("safers", res);
        return "safers";
    }

    @GetMapping("/safers/{id}/edit")
    public String safersEdit(@PathVariable("id") long id, Model model) {
        Safers res = safersRepository.findById(id).get();
        model.addAttribute("safers", res);
        return "safers-edit";
    }

//    @PostMapping("/safers/{id}/edit")
//    public String safersUpdate(@PathVariable("id") long id,
//                                     @ModelAttribute("safers")
//                                     @Validated Safers safers, BindingResult bindingResult) {
//        Safers.setId(id);
//        if (bindingResult.hasErrors()) {
//            return "safers-edit";
//        }
//        safersRepository.save(safers);
//        return "redirect:/";
//    }

    @PostMapping("/safers/{id}/remove")
    public String safersRemove(@PathVariable("id") long id, Model model) {
        Safers safers = safersRepository.findById(id).orElseThrow();
        safersRepository.delete(safers);
        return "redirect:/";
    }
}
