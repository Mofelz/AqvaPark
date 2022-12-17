package com.example.demo.controllers;

import com.example.demo.models.ContactInfo;
import com.example.demo.models.Stocks;
import com.example.demo.repo.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class StocksController {

    @Autowired
    private final StockRepository stockRepository;

    public StocksController(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    @GetMapping("/stocks")
    public String stocksMain(Model model) {
        Iterable<Stocks> stocks = stockRepository.findAll();
        model.addAttribute("stocks", stocks);
        return "stocks";
    }

    @GetMapping("/stocks/add")
    public String conntactinfoAdd(@ModelAttribute("stocks") Stocks stocks, Model model) {
        return "stocks-add";
    }

    @PostMapping("/stocks/add")
    public Object stocksAdd(@ModelAttribute("stocks") @Validated Stocks stocks, BindingResult bindingResult, Model model) {
        Iterable<Stocks> stocks1 = stockRepository.findAll();
        model.addAttribute("stocks", stocks1);
        if (bindingResult.hasErrors()) return "stocks";
        stockRepository.save(stocks);
        return "redirect:/stocks";
    }

    @GetMapping("/stocks/filter")
    public String stocksFilter(Model model) {
        return "stocks-filter";
    }

    @PostMapping("/stocks/filter/result")
    public String stocksResult(@RequestParam String id, Model model) {
        List<Stocks> result = stockRepository.findById(id);
        model.addAttribute("result", result);
        return "stocks-filter";
    }

    @GetMapping("/stocks/{id}")
    public String stocksDetails(@PathVariable(value = "id") long id, Model model) {
        Optional<Stocks> post = stockRepository.findById(id);
        ArrayList<Stocks> res = new ArrayList<>();
        post.ifPresent(res::add);
        model.addAttribute("stocks", res);
        return "stocks";
    }

    @GetMapping("/stocks/{id}/edit")
    public String stocksEdit(@PathVariable("id") long id, Model model) {
        Stocks res = stockRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Неверный id: " + id));
        model.addAttribute("stocks", res);
        return "stocks-edit";
    }

    @PostMapping("/stocks/{id}/edit")
    public String stocksPostUpdate(@PathVariable("id") long id,
                                 @ModelAttribute("stocks")
                                 @Validated Stocks stocks, BindingResult bindingResult) {
        stocks.setId(id);
        if (bindingResult.hasErrors()) {
            return "stocks-edit";
        }
        stockRepository.save(stocks);
        return "redirect:/stocks";
    }

    @PostMapping("/stocks/{id}/remove")
    public String stocksPostRemove(@PathVariable("id") long id, Model model) {
        Stocks stocks = stockRepository.findById(id).orElseThrow();
        stockRepository.delete(stocks);
        return "redirect:/stocks";
    }
}
