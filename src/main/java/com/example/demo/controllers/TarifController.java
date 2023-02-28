package com.example.demo.controllers;


import com.example.demo.Service.TariffImageService;
import com.example.demo.models.Tarif;
import com.example.demo.repo.TarifRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class TarifController {
    private final TarifRepository tarifRepository;

    private final TariffImageService tariffImageService;

    public TarifController(TarifRepository tarifRepository, TariffImageService tariffImageService) {
        this.tarifRepository = tarifRepository;
        this.tariffImageService = tariffImageService;
    }

    @GetMapping("/tarifs")
    public String tarifsMain(Model model) {
        Iterable<Tarif> tarifs = tarifRepository.findAll();
        model.addAttribute("tarifs", tarifs);
        return "tarifs";
    }

    @GetMapping("/tarifs/add")
    public String tarifsAdd(@ModelAttribute("tarif") Tarif tarif, Model model) {
        return "tarifs-add";
    }

    @PostMapping("/tarifs/add")
    public Object tarifsPostAdd(@ModelAttribute("tarif") @Validated Tarif tarif, BindingResult bindingResult,
                                @RequestParam("file") MultipartFile file, Model model) throws IOException {
        Iterable<Tarif> tarifIterable = tarifRepository.findAll();
        model.addAttribute("tarifs", tarifIterable);
        if (bindingResult.hasErrors()) return "tarifs-add";
       tariffImageService.saveImageAndTariff(tarif, file);
        return "redirect:/tarifs";
    }

    @PostMapping("/tarif/filter/result")
    public String tarifsResult(@RequestParam String nametarif, Model model) {
        List<Tarif> result = tarifRepository.findByNametarif(nametarif);
        model.addAttribute("result", result);
        return "tarif-filter";
    }
    @GetMapping("/tarif/{id}")
    public String tarifDetails(@PathVariable(value = "id") long id, Model model) {
        Optional<Tarif> tarif = tarifRepository.findById(id);
        ArrayList<Tarif> res = new ArrayList<>();
        tarif.ifPresent(res::add);
        model.addAttribute("tarif", res);
        return "tarif";
    }

    @GetMapping("/tarif/{id}/edit")
    public String tarifEdit(@PathVariable("id") long id, Model model) {
        Tarif res = tarifRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Неверный id: " + id));
        model.addAttribute("tarif", res);
        return "tarif-edit";
    }

    @PostMapping("/tarif/{id}/edit")
    public String tarifUpdate(@PathVariable("id") long id,
                                     @ModelAttribute("tarif")
                                     @Validated Tarif tarif, BindingResult bindingResult) {
        tarif.setId(id);
        if (bindingResult.hasErrors()) {
            return "tarif-edit";
        }
        tarifRepository.save(tarif);
        return "redirect:/tarifs";
    }

    @PostMapping("/tarif/{id}/remove")
    public String tarifRemove(@PathVariable("id") long id, Model model) {
        Tarif tarif = tarifRepository.findById(id).orElseThrow();
        tarifRepository.delete(tarif);
        return "redirect:/tarifs";
    }
}
