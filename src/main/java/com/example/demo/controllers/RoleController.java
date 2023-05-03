package com.example.demo.controllers;

import com.example.demo.models.Booking;
import com.example.demo.models.Role;
import com.example.demo.models.User;
import com.example.demo.repo.BookingRepository;
import com.example.demo.repo.UserRepos;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RoleController {
    private final UserRepos userRepos;
    private final BookingRepository bookingRepository;

    public RoleController(UserRepos userRepos, BookingRepository bookingRepository) {
        this.userRepos = userRepos;
        this.bookingRepository = bookingRepository;
    }
    @PostMapping("/main")
    public String roleMain(){
        if (getRole(SecurityContextHolder.getContext().getAuthentication()) == Role.USER) {
            Booking booking = bookingRepository.findFirstByOrderByIdDesc();
            User user = userRepos.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
            if (booking.getUser() == null) booking.setUser(user);
            bookingRepository.save(booking);
        }
        return "redirect:/";
    }
    public Role getRole(Authentication authentication){
        User user = userRepos.findByUsername(authentication.getName());
        return (Role) user.getRoles().toArray()[0];
    }
}
