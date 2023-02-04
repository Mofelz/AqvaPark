package com.example.demo.controllers;


import com.example.demo.models.ContactInfo;
import com.example.demo.models.Inventory;
import com.example.demo.repo.InventoryRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class InventoryController {
    private final InventoryRepository inventoryRepository;

    public InventoryController(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    @GetMapping("/inventory")
    public String tarifsMain(Model model) {
        Iterable<Inventory> inventories = inventoryRepository.findAll();
        model.addAttribute("inventory", inventories);
        return "inventory";
    }

    @GetMapping("/inventory/add")
    public String inventoryAdd(@ModelAttribute("inventory") Inventory inventory, Model model) {
        return "inventory-add";
    }

    @PostMapping("/inventory/add")
    public Object tarifsPostAdd(@ModelAttribute("inventory") @Validated Inventory inventory, BindingResult bindingResult, Model model) {
        Iterable<Inventory> inventories = inventoryRepository.findAll();
        model.addAttribute("inventories", inventories);
        if (bindingResult.hasErrors()) return "inventory-add";
        inventoryRepository.save(inventory);
        return "redirect:/inventory";
    }

    @PostMapping("/inventory/filter/result")
    public String inventoryResult(@RequestParam String predmet, Model model) {
        List<Inventory> result = inventoryRepository.findByPredmet(predmet);
        model.addAttribute("result", result);
        return "inventory-filter";
    }
    @GetMapping("/inventory/{id}")
    public String inventoryDetails(@PathVariable(value = "id") long id, Model model) {
        Optional<Inventory> inventory = inventoryRepository.findById(id);
        ArrayList<Inventory> res = new ArrayList<>();
        inventory.ifPresent(res::add);
        model.addAttribute("inventory", res);
        return "inventory";
    }

    @GetMapping("/inventory/{id}/edit")
    public String inventoryEdit(@PathVariable("id") long id, Model model) {
        Inventory res = inventoryRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Неверный id: " + id));
        model.addAttribute("inventory", res);
        return "inventory-edit";
    }

    @PostMapping("/inventory/{id}/edit")
    public String inventoryUpdate(@PathVariable("id") long id,
                                     @ModelAttribute("inventory")
                                     @Validated Inventory inventory, BindingResult bindingResult) {
        inventory.setId(id);
        if (bindingResult.hasErrors()) {
            return "inventory-edit";
        }
        inventoryRepository.save(inventory);
        return "redirect:/";
    }

    @PostMapping("/inventory/{id}/remove")
    public String inventoryRemove(@PathVariable("id") long id, Model model) {
        Inventory inventory = inventoryRepository.findById(id).orElseThrow();
        inventoryRepository.delete(inventory);
        return "redirect:/";
    }
}
