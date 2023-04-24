package com.example.demo.controllers;

import com.example.demo.Service.ExportExel;
import com.example.demo.Service.ReportService;
import com.example.demo.models.*;
import com.example.demo.repo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
public class BookingController {

    private final BookingRepository bookingRepository;

    private final ProductRepository productRepository;

    private final SnackbarRepository snackbarRepository;
    private final CategoryRepository categoryRepository;
    private final StatusRepository statusRepository;

    private final UserRepos userRepos;
    @Autowired
    private ReportService reportService;

    private CartRepository cartRepository;

    public BookingController(BookingRepository bookingRepository, CategoryRepository categoryRepository, ProductRepository productRepository, StatusRepository statusRepository, SnackbarRepository snackbarRepository, UserRepos userRepos, CartRepository cartRepository) {
        this.bookingRepository = bookingRepository;
        this.productRepository = productRepository;
        this.snackbarRepository = snackbarRepository;
        this.userRepos = userRepos;
        this.categoryRepository = categoryRepository;
        this.statusRepository = statusRepository;
        this.cartRepository = cartRepository;
    }

    @RequestMapping("/")
    public String blogMain(@ModelAttribute("booking") Booking booking, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Iterable<Booking> bookings = bookingRepository.findAll();
        Iterable<Snackbar> snackbars = snackbarRepository.findAll();
        Iterable<Product> products = productRepository.findAll();
        Iterable<User> users = userRepos.findAll();

        model.addAttribute("products", products);
        model.addAttribute("snackbars", snackbars);
        model.addAttribute("User", users);
        model.addAttribute("bookings", bookings);

        model.addAttribute("isAdmin", authentication.getAuthorities().toString().contains("ADMIN"));
        model.addAttribute("isSaler", authentication.getAuthorities().toString().contains("SALER"));
        model.addAttribute("isUser", authentication.getAuthorities().toString().contains("USER"));

        return "orders/orders";
    }

    @GetMapping("/orders/add")
    public String blogAdd(@ModelAttribute("booking") @Validated Booking booking, Model model) {
        Iterable<Category> categories = categoryRepository.findAll();
        model.addAttribute("categories", categories);
        Iterable<Product> products = productRepository.findAll();
        model.addAttribute("products", products);

        return "orders/orders-add";
    }

    @PostMapping("/orders/add")
    public String blogAdd(@ModelAttribute("booking") Booking booking, BindingResult result, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Booking booking1 = bookingRepository.findTopByTableNumberOrderByIdDesc(booking.getTableNumber());
        if (booking1 != null && booking1.getTimeDeparture() == null) {
            result.addError(new ObjectError("tableNumber", "Данный стол уже занят!"));
            model.addAttribute("ErrorMassage", "Данный стол уже занят!");
        }

        if (result.hasErrors()) {

            Iterable<Booking> bookings = bookingRepository.findAll();
            Iterable<Snackbar> snackbars = snackbarRepository.findAll();
            Iterable<Product> products = productRepository.findAll();
            Iterable<User> users = userRepos.findAll();

            Iterable<Category> categories = categoryRepository.findAll();
            model.addAttribute("categories", categories);

            model.addAttribute("products", products);
            model.addAttribute("snackbars", snackbars);
            model.addAttribute("User", users);
            model.addAttribute("bookings", bookings);

            model.addAttribute("isAdmin", auth.getAuthorities().toString().contains("ADMIN"));
            model.addAttribute("isSaler", auth.getAuthorities().toString().contains("SALER"));
            model.addAttribute("isUser", auth.getAuthorities().toString().contains("USER"));

            return "orders/orders";
        }
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        model.addAttribute("isAuth", userDetails.getUsername());
        Iterable<Snackbar> snackbars = snackbarRepository.findAll();
        Iterable<Product> products = productRepository.findAll();
        Iterable<User> users = userRepos.findAll();
        Iterable<Category> categories = categoryRepository.findAll();
        model.addAttribute("categories", categories);
        model.addAttribute("users", users);
        model.addAttribute("snackbars", snackbars);
        model.addAttribute("products", products);

        booking.setStatus(statusRepository.findFirstByOrderByIdAsc());
        booking.setTimeArrival(new Date());
        bookingRepository.save(booking);
        Iterable<Cart> carts = cartRepository.findAllByBooking(booking);
        int cartCount = 0;
        for(Cart cart : carts){
            cartCount += cart.getCount();
        }
//        if(cartCount == 0){
//
//        }
        model.addAttribute("cartCount", cartCount);
        return "orders/orders-add";
    }
    @GetMapping("/orders/{id}/edit")
    public String blogEdit(@PathVariable("id") long id, Model model) {
        Iterable<Product> products = productRepository.findAll();
        Booking res = bookingRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Неверный id: " + id));
        model.addAttribute("booking", res);
        model.addAttribute("products", products);
        Iterable<Status> status = statusRepository.findAll();
        model.addAttribute("statuses", status);
        model.addAttribute("carts", cartRepository.findAllByBooking(res));

        return "orders/orders-edit";
    }

    @PostMapping("/orders/{id}/edit")
    public String blogPostUpdate(@PathVariable("id") long id,
                                 @ModelAttribute("booking")
                                 Booking booking, Model model) {
        booking.setId(id);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepos.findByUsername(authentication.getName());
        Iterable<Product> products = productRepository.findAll();
        model.addAttribute("products", products);
        Iterable<Status> status = statusRepository.findAll();
        model.addAttribute("statuses", status);
        booking.setUser(user);
        model.addAttribute("carts", cartRepository.findAllByBooking(booking));
        bookingRepository.save(booking);
        return "redirect:/login";
    }
    @GetMapping("/withdrawals")
    public String profileMain(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Iterable<Booking> bookings = bookingRepository.findAllByUser(userRepos.findByUsername(auth.getName()));

        if(auth.getAuthorities().toString().contains("SALER"))
            bookings = bookingRepository.findAll();

        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        model.addAttribute("isAuth", userDetails.getUsername());
        List<User> users = (List<User>) userRepos.findAll();
        model.addAttribute("users", users);
        model.addAttribute("isSaler", auth.getAuthorities().toString().contains("SALER"));
        model.addAttribute("isUser", auth.getAuthorities().toString().contains("USER"));
        model.addAttribute("bookings", bookings);

        Iterable<Product> products = productRepository.findAll();
        model.addAttribute("products", products);

        Iterable<Status> statuses = statusRepository.findAll();
        model.addAttribute("statuses", statuses);

        return "orders/withdrawals";
    }

    @PostMapping("/withdrawals")
    public String wildMain(@PathVariable("id") long id,
                           @ModelAttribute("booking")
                           Booking booking, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Iterable<Booking> bookings = bookingRepository.findAllByUser(userRepos.findByUsername(auth.getName()));

        if(auth.getAuthorities().toString().contains("SALER"))
            bookings = bookingRepository.findAll();

        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        model.addAttribute("isAuth", userDetails.getUsername());
        List<User> users = (List<User>) userRepos.findAll();
        model.addAttribute("users", users);
        model.addAttribute("isSaler", auth.getAuthorities().toString().contains("SALER"));
        model.addAttribute("isUser", auth.getAuthorities().toString().contains("USER"));
        model.addAttribute("bookings", bookings);

        Iterable<Product> products = productRepository.findAll();
        model.addAttribute("products", products);

        Iterable<Status> status = statusRepository.findAll();
        model.addAttribute("statuses", status);

        booking.setUser((User) users);
        bookingRepository.save(booking);

        return "orders/withdrawals";
    }

    @GetMapping("/report/excel")
    public void exportToExcel(HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=reports_" + currentDateTime + ".xlsx";
        response.setHeader(headerKey, headerValue);

        List<Booking> listReport = reportService.listAll();

        ExportExel excelExporter = new ExportExel(listReport);

        excelExporter.export(response);
    }

    @PostMapping("/orders/{id}/remove")
    public String deleteOrder(@PathVariable("id") long id, Model model) {
        Booking booking = bookingRepository.findById(id).orElseThrow();
        bookingRepository.delete(booking);
        return "redirect:/withdrawals";
    }
    @PostMapping("/orders/save")
    public String saveStatus(@RequestParam Status status,@RequestParam Booking booking) {
        booking.setStatus(status);
        bookingRepository.save(booking);
        return "redirect:/withdrawals";
    }
}