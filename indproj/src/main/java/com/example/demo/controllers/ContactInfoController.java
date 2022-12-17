package com.example.demo.controllers;

import com.example.demo.models.ContactInfo;
import com.example.demo.models.Post;
import com.example.demo.models.Usluga;
import com.example.demo.repo.ContactInfoRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class ContactInfoController {
    private final ContactInfoRepository contactInfoRepository;

    public ContactInfoController(ContactInfoRepository contactInfoRepository) {
        this.contactInfoRepository = contactInfoRepository;
    }

    @GetMapping("/conntactinfo")
    public String conntactinfoMain(Model model) {
        Iterable<ContactInfo> contactInfos = contactInfoRepository.findAll();
        model.addAttribute("conntactinfo", contactInfos);
        return "conntactinfo";
    }

    @GetMapping("/conntactinfo/add")
    public String conntactinfoAdd(@ModelAttribute("conntactinfo") ContactInfo contactInfo, Model model) {
        return "contactinfo-add";
    }

    @PostMapping("/conntactinfo/add")
    public Object conntactinfoPostAdd(@ModelAttribute("conntactinfo") @Validated ContactInfo contactInfo, BindingResult bindingResult, Model model) {
        Iterable<ContactInfo> contactInfos = contactInfoRepository.findAll();
        model.addAttribute("conntactinfo", contactInfos);
        if (bindingResult.hasErrors()) return "conntactinfo";
        contactInfoRepository.save(contactInfo);
        return "redirect:/conntactinfo";
    }

    @PostMapping("/conntactinfo/filter/result")
    public String conntactinfoResult(@RequestParam String pochta, Model model) {
        List<ContactInfo> result = contactInfoRepository.findByPochta(pochta);
        model.addAttribute("result", result);
        return "conntactinfo-filter";
    }
    @GetMapping("/conntactinfo/{id}")
    public String conntactinfoDetails(@PathVariable(value = "id") long id, Model model) {
        Optional<ContactInfo> contactInfo = contactInfoRepository.findById(id);
        ArrayList<ContactInfo> res = new ArrayList<>();
        contactInfo.ifPresent(res::add);
        model.addAttribute("conntactinfo", res);
        return "conntactinfo";
    }

    @GetMapping("/conntactinfo/{id}/edit")
    public String conntactinfoEdit(@PathVariable("id") long id, Model model) {
        ContactInfo res = contactInfoRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Неверный id: " + id));
        model.addAttribute("conntactinfo", res);
        return "contactinfo-edit";
    }

    @PostMapping("/conntactinfo/{id}/edit")
    public String conntactinfoUpdate(@PathVariable("id") long id,
                                 @ModelAttribute("contactinfo")
                                 @Validated ContactInfo contactInfo, BindingResult bindingResult) {
        contactInfo.setId(id);
        if (bindingResult.hasErrors()) {
            return "contactinfo-edit";
        }
        contactInfoRepository.save(contactInfo);
        return "redirect:/";
    }

    @PostMapping("/conntactinfo/{id}/remove")
    public String conntactinfoRemove(@PathVariable("id") long id, Model model) {
        ContactInfo contactInfo = contactInfoRepository.findById(id).orElseThrow();
        contactInfoRepository.delete(contactInfo);
        return "redirect:/";
    }
}
