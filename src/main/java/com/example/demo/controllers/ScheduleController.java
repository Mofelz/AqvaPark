package com.example.demo.controllers;

import com.example.demo.models.Schedule;
import com.example.demo.repo.ScheduleRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class ScheduleController {
    private final ScheduleRepository scheduleRepository;

    public ScheduleController(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }
    @GetMapping("/schedules")
    public String ScheduleMain(Model model) {
        Iterable<Schedule> schedules = scheduleRepository.findAll();
        model.addAttribute("schedule", schedules);
        return "/schedules";
    }

    @GetMapping("/schedule/add")
    public String ScheduleAdd(@ModelAttribute("schedule") Schedule schedule, Model model) {
        return "/schedules-add";
    }

    @PostMapping("/schedule/add")
    public Object SchedulesAdd(@ModelAttribute("schedule") @Validated Schedule schedule, BindingResult bindingResult, Model model) {

        scheduleRepository.save(schedule);
        return  "redirect:/schedules";
    }

    @GetMapping("/schedule/filter")
    public String ScheduleFilter(Model model) {
        return "schedule-filter";
    }

    @PostMapping("/schedule/filter/result")
    public String ScheduleResult(@RequestParam String id, Model model) {
        List<Schedule> result = scheduleRepository.findById(id);
        model.addAttribute("result", result);
        return "schedules-filter";
    }

    @GetMapping("/schedule/{id}")
    public String ScheduleDetails(@PathVariable(value = "id") long id, Model model) {
        Schedule schedule = scheduleRepository.findById(id).get();
        model.addAttribute("schedule", schedule);
        return "schedules";
    }

    @GetMapping("/schedule/{id}/edit")
    public String ScheduleEdit(@PathVariable("id") long id, Model model) {
        Schedule schedules = scheduleRepository.findById(id).get();
        model.addAttribute("schedule", schedules);
        return "schedules-edit";
    }

    @PostMapping("/schedule/{id}/edit")
    public String ScheduleUpdate(@PathVariable("id") long id,
                                 @ModelAttribute("schedules")
                                 @Validated Schedule schedule, BindingResult bindingResult) {

        schedule.setId(id);
        if (bindingResult.hasErrors()) {
            return "schedules-edit";
        }
        scheduleRepository.save(schedule);
        return "redirect:/schedules";
    }

    @PostMapping("/schedule/{id}/remove")
    public String ScheduleRemove(@PathVariable("id") long id, Model model) {
        Schedule schedule = scheduleRepository.findById(id).get();
        scheduleRepository.delete(schedule);
        return "redirect:/schedules";
    }
}
