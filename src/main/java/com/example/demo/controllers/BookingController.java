package com.example.demo.controllers;

import com.example.demo.Service.ExportExel;
import com.example.demo.Service.ReportService;
import com.example.demo.models.*;
import com.example.demo.repo.*;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
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
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Controller
public class BookingController {

    private final BookingRepository bookingRepository;

    private final ProductRepository productRepository;
    private final ResourceLoader resourceLoader;
    private static final String FONT = "fonts\\calibri.ttf";


    private final CategoryRepository categoryRepository;
    private final StatusRepository statusRepository;

    private final UserRepos userRepos;
    @Autowired
    private ReportService reportService;

    private CartRepository cartRepository;

    public BookingController(BookingRepository bookingRepository, ResourceLoader resourceLoader, CategoryRepository categoryRepository, ProductRepository productRepository, StatusRepository statusRepository, UserRepos userRepos, CartRepository cartRepository) {
        this.bookingRepository = bookingRepository;
        this.resourceLoader = resourceLoader;
        this.productRepository = productRepository;
        this.userRepos = userRepos;
        this.categoryRepository = categoryRepository;
        this.statusRepository = statusRepository;
        this.cartRepository = cartRepository;
    }

    @RequestMapping("/")
    public String blogMain(@ModelAttribute("booking") Booking booking, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Iterable<Booking> bookings = bookingRepository.findAll();
        Iterable<Product> products = productRepository.findAll();
        Iterable<User> users = userRepos.findAll();

        model.addAttribute("products", products);
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
        if (booking1 != null && (booking1.getTimeDeparture() == null || booking1.getTimeDeparture().after(new Date()))) {
            result.addError(new ObjectError("tableNumber", "Данный стол уже занят!"));
            model.addAttribute("ErrorMassage", "Данный стол уже занят!");
        }

        if (result.hasErrors()) {

            Iterable<Booking> bookings = bookingRepository.findAll();
            Iterable<Product> products = productRepository.findAll();
            Iterable<User> users = userRepos.findAll();

            Iterable<Category> categories = categoryRepository.findAll();
            model.addAttribute("categories", categories);

            model.addAttribute("products", products);
            model.addAttribute("User", users);
            model.addAttribute("bookings", bookings);

            model.addAttribute("isAdmin", auth.getAuthorities().toString().contains("ADMIN"));
            model.addAttribute("isSaler", auth.getAuthorities().toString().contains("SALER"));
            model.addAttribute("isUser", auth.getAuthorities().toString().contains("USER"));

            return "orders/orders";
        }
//        UserDetails userDetails = (UserDetails) auth.getPrincipal();
//        model.addAttribute("isAuth", userDetails.getUsername());
        Iterable<Product> products = productRepository.findAll();
        Iterable<User> users = userRepos.findAll();
        Iterable<Category> categories = categoryRepository.findAll();
        model.addAttribute("categories", categories);
        model.addAttribute("users", users);
        model.addAttribute("products", products);

        booking.setStatus(statusRepository.findFirstByOrderByIdAsc());
        booking.setTimeArrival(new Date());
        bookingRepository.save(booking);
        Iterable<Cart> carts = cartRepository.findAllByBooking(booking);
        int cartCount = 0;
        for(Cart cart : carts){
            cartCount += cart.getCount();
        }
        model.addAttribute("cartCount", cartCount);
        return "orders/orders-add";
    }
    @GetMapping("/orders/{id}/edit")
    public String ordersEdit(@PathVariable("id") long id, Model model) {
        Iterable<Product> products = productRepository.findAll();
        Booking booking = bookingRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Неверный id: " + id));
        Iterable<Cart> carts = cartRepository.findAllByBooking(booking);

        model.addAttribute("booking", booking);
        model.addAttribute("products", products);
        Iterable<Status> status = statusRepository.findAll();
        model.addAttribute("statuses", status);
        model.addAttribute("carts", cartRepository.findAllByBooking(booking));

        int fullPrice = 0;
        for(Cart cart : carts){
            fullPrice += cart.getCount() * cart.getProduct().getPrice();
        }

        model.addAttribute("fullPrice",fullPrice);
        return "orders/orders-edit";
    }

    @PostMapping("/orders/{id}/edit")
    public String ordersUpdate(@PathVariable("id") long id,
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
        booking.setTimeArrival(new Date());


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
        List<Cart> carts = new ArrayList<>();
        List<String> fullPrices = new ArrayList<>();
        int bookingCount = 0;
        for (Booking booking1 : bookings){
            Iterable<Cart> carts1 = cartRepository.findAllByBooking(booking1);
            carts.addAll((List)carts1);
            int fullPrice = 0;
            for(Cart cart : carts1){
                fullPrice += cart.getCount() * cart.getProduct().getPrice();
            }
            fullPrices.add("" + fullPrice + "");
            if (!booking1.isPayment())bookingCount++;
        }
        model.addAttribute("bookingCount", bookingCount);
        model.addAttribute("fullPrice", fullPrices);
        model.addAttribute("carts", carts);
        return "orders/withdrawals";
    }

    @PostMapping("/withdrawals")
    public String wildMain(@ModelAttribute("booking")
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
    @PostMapping("/pay")
    public ResponseEntity<Resource> exportPDF(@RequestParam Booking booking) throws IOException, DocumentException {
        booking.setPayment(true);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(booking.getTimeArrival());
        calendar.add(Calendar.HOUR_OF_DAY,1);
        booking.setTimeDeparture(calendar.getTime());
        bookingRepository.save(booking);
        Document document = new Document();
        Resource resource = resourceLoader.getResource(Paths.get("fonts\\cheque.pdf").toUri().toString());
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(resource.getFile()));
        document.open();
        BaseFont baseFont = BaseFont.createFont(FONT, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        Font font = new Font(baseFont, 11, Font.NORMAL);

        addTable(document, font, booking);
        document.close();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + resource.getFilename())
                .contentLength(resource.contentLength())
                .body(resource);
    }
    public void addTable(Document document, Font font, Booking booking) throws DocumentException {
        Paragraph paragraph = new Paragraph("Чек о покупке", font);
        paragraph.setAlignment(Element.ALIGN_CENTER);
        document.add(paragraph);
        paragraph = new Paragraph("Whitespace", font);
        font.setColor(BaseColor.WHITE);
        paragraph.setAlignment(Element.ALIGN_CENTER);
        document.add(paragraph);
        font.setColor(BaseColor.BLACK);
        PdfPTable table = new PdfPTable(6);

        font.setSize(11);
        String[] items = {"Логин пользователя", "Код заказа", "Номер столика", "Время выдачи чека"};
        String[] items1 = {"Наименование", "Категория", "Вес", "Цена", "Количество", "Итоговая цена"};
        for (int i = 0; i < 6; i++) {
            if (i == 4 || i == 5) {

                table.addCell(new Paragraph("", font));
                continue;
            }
            table.addCell(new Paragraph(items[i], font));
        }
        for (int i = 0; i < 6; i++) {
            switch (i) {
                case 0 -> table.addCell(new Paragraph(booking.getUser().getUsername(), font));
                case 1 -> table.addCell(new Paragraph(booking.getId().toString(), font));
                case 2 -> table.addCell(new Paragraph(booking.getTableNumber().toString(), font));
                case 3 -> table.addCell(new Paragraph(booking.getTimeArrival().toString().split("\\.")[0], font));
                default -> table.addCell(new Paragraph("", font));
            }
            if (i == 5) {
                for (int j = 0; j < 6; j++) {
                    table.addCell(new Paragraph(items1[j], font));
                }
                for (Cart cart : booking.getCarts()) {
                    table.addCell(new Paragraph(cart.getProduct().getNameProduct(), font));
                    table.addCell(new Paragraph(cart.getProduct().getCategory().getNameCategory(), font));
                    table.addCell(new Paragraph(cart.getProduct().getWeight().toString(), font));
                    table.addCell(new Paragraph(cart.getProduct().getPrice().toString(), font));
                    table.addCell(new Paragraph(String.valueOf(cart.getCount()), font));
                    table.addCell(new Paragraph(String.valueOf(cart.getCount() * cart.getProduct().getPrice()), font));
                }
            }
        }

        font.setStyle(Font.NORMAL);
        document.add(table);
    }
}