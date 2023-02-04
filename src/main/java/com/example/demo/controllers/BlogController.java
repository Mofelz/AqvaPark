package com.example.demo.controllers;

import com.example.demo.models.*;
import com.example.demo.repo.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
//@PreAuthorize("hasAnyAuthority('USER')")
public class BlogController {
    private final PostRepository postRepository;

    private final UslugaRepository uslugaRepository;

    private final TarifRepository tarifRepository;
    private final InventoryRepository inventoryRepository;

    private final ContactInfoRepository contactInfoRepository;

    private final StockRepository stockRepository;

    private final ScheduleRepository scheduleRepository;

    private final UserRepos userRepos;

    public BlogController(ScheduleRepository scheduleRepository, PostRepository postRepository, UslugaRepository uslugaRepository, TarifRepository tarifRepository, InventoryRepository inventoryRepository, ContactInfoRepository contactInfoRepository, StockRepository stockRepository, UserRepos userRepos) {
        this.postRepository = postRepository;
        this.uslugaRepository = uslugaRepository;
        this.tarifRepository = tarifRepository;
        this.inventoryRepository = inventoryRepository;
        this.contactInfoRepository = contactInfoRepository;
        this.stockRepository = stockRepository;
        this.userRepos = userRepos;
        this.scheduleRepository = scheduleRepository;
    }

    @GetMapping("/")
    public String blogMain(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Iterable<Post> posts = postRepository.findAll();
        Iterable<Stocks> stocks = stockRepository.findAll();
        Iterable<ContactInfo> contactInfos = contactInfoRepository.findAll();
        Iterable<Tarif> tarifs = tarifRepository.findAll();
        Iterable<Usluga> uslugas = uslugaRepository.findAll();
        Iterable<Inventory> inventories = inventoryRepository.findAll();
        Iterable<User> users = userRepos.findAll();
        Iterable<Schedule> schedules = scheduleRepository.findAll();
        model.addAttribute("schedules", schedules);
        model.addAttribute("tarifs", tarifs);
        model.addAttribute("stocks", stocks);
        model.addAttribute("User", users);
        model.addAttribute("contactinfo", contactInfos);
        model.addAttribute("posts", posts);
        model.addAttribute("usluga", uslugas);
        model.addAttribute("inventory", inventories);
        model.addAttribute("isAdmin", authentication.getAuthorities().toString().contains("ADMIN"));
        model.addAttribute("isSotrudnik", authentication.getAuthorities().toString().contains("SOTRUDNIK"));
        model.addAttribute("isSaler", authentication.getAuthorities().toString().contains("SALER"));
        model.addAttribute("isSkladovshik", authentication.getAuthorities().toString().contains("SKLADOVSHIK"));
        model.addAttribute("isUser", authentication.getAuthorities().toString().contains("USER"));

        return "blog-main";
    }

    @GetMapping("/blog/add")
    public String blogAdd(@ModelAttribute("post") Post post, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        model.addAttribute("isAuth", userDetails.getUsername());
        Iterable<Usluga> uslugas = uslugaRepository.findAll();
        Iterable<Stocks> stocks = stockRepository.findAll();
        Iterable<Tarif> tarifs = tarifRepository.findAll();
        List<User> users = userRepos.findAll();
        model.addAttribute("users", users);
        model.addAttribute("uslugas", uslugas);
        model.addAttribute("stocks", stocks);
        model.addAttribute("tarifs", tarifs);
        return "blog-add";
    }

    @PostMapping("/blog/add")
    public Object blogPostAdd(@ModelAttribute("post") @Validated Post post, BindingResult bindingResult, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        model.addAttribute("isAuth", userDetails.getUsername());

        Iterable<Usluga> uslugas = uslugaRepository.findAll();
        Iterable<Stocks> stocks = stockRepository.findAll();
        Iterable<Tarif> tarifs = tarifRepository.findAll();
        model.addAttribute("uslugas", uslugas);
        model.addAttribute("stocks", stocks);
        model.addAttribute("tarifs", tarifs);
//        if (bindingResult.hasErrors()) {
//            model.addAttribute("users", userRepos.findAll());
//            return "blog-add";
//        }
        postRepository.save(post);
        return "bilets";
    }

    @GetMapping("/blog/filter")
    public String blogFilter(Model model) {
        return "blog-filter";
    }

    @PostMapping("/blog/filter/result")
    public String blogResult(@RequestParam String datapos, Model model) {
        List<Post> result = postRepository.findByDatapos(datapos);
        model.addAttribute("result", result);
        return "blog-filter";
    }

    @GetMapping("/bilets/{id}")
    public String biletDetails(@PathVariable(value = "id") long id, Model model) {


        Optional<Post> post = postRepository.findById(id);
        ArrayList<Post> res = new ArrayList<>();
        post.ifPresent(res::add);
        model.addAttribute("post", res);

        return "bilets";
    }

    @GetMapping("/blog/{id}/edit")
    public String blogEdit(@PathVariable("id") long id, Model model) {


        Iterable<Usluga> uslugas = uslugaRepository.findAll();
        Iterable<Tarif> tarifs = tarifRepository.findAll();
        Post res = postRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Неверный id: " + id));
        model.addAttribute("uslugas", uslugas);
        model.addAttribute("post", res);
        model.addAttribute("tarifs", tarifs);


        return "blog-edit";
    }

    @PostMapping("/blog/{id}/edit")
    public String blogPostUpdate(@PathVariable("id") long id,
                                 @ModelAttribute("post")
                                 @Validated Post post, Model model) {

        post.setId(id);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepos.findByLogin(authentication.getName());
        Iterable<Tarif> tarifs = tarifRepository.findAll();
        model.addAttribute("tarifs", tarifs);
        post.setUser(user);
        postRepository.save(post);
        return "redirect:/";
    }

    @PostMapping("/blog/{id}/remove")
    public String blogPostRemove(@PathVariable("id") long id, Model model) {
        Post post = postRepository.findById(id).orElseThrow();
        postRepository.delete(post);
        return "redirect:/";
    }
}