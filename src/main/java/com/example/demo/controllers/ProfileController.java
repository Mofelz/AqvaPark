package com.example.demo.controllers;

import com.example.demo.Service.ExportExel;
import com.example.demo.Service.ReportService;
import com.example.demo.models.Post;
import com.example.demo.models.Profile;
import com.example.demo.models.User;
import com.example.demo.repo.InventoryRepository;
import com.example.demo.repo.PostRepository;
import com.example.demo.repo.ProfileRepository;
import com.example.demo.repo.UserRepos;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
public class ProfileController {
    private final ProfileRepository profileRepository;

    private final PostRepository postRepository;
    private final InventoryRepository inventoryRepository;


    @Autowired
    private ReportService reportService;

    private final UserRepos userRepos;
    private SessionFactory sessionFactory;

    public ProfileController(ProfileRepository profileRepository, PostRepository postRepository, InventoryRepository inventoryRepository, UserRepos userRepos) {
        this.profileRepository = profileRepository;
        this.postRepository = postRepository;
        this.inventoryRepository = inventoryRepository;
        this.userRepos = userRepos;

    }

    @GetMapping("/profile")
    public String profileMain(Model model) {
        Iterable<Post> posts = postRepository.findAll();
        Iterable<Profile> profiles = profileRepository.findAll();
        List<User> users = userRepos.findAll();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        model.addAttribute("isAuth", userDetails.getUsername());
        model.addAttribute("profile", profiles);
        model.addAttribute("post", posts);
        model.addAttribute("users", users);
        model.addAttribute("isAdmin", auth.getAuthorities().toString().contains("ADMIN"));
        model.addAttribute("isSotrudnik", auth.getAuthorities().toString().contains("SOTRUDNIK"));
        model.addAttribute("isSaler", auth.getAuthorities().toString().contains("SALER"));
        model.addAttribute("isSkladovshik", auth.getAuthorities().toString().contains("SKLADOVSHIK"));
        model.addAttribute("isUser", auth.getAuthorities().toString().contains("USER"));

        return "profile";
    }

    @GetMapping("/profile/add")
    public String profileAdd(@ModelAttribute("profile") Profile profile, Model model) {
        return "profile-add";
    }

    @PostMapping("/profile/add")
    public Object profilePostAdd(@ModelAttribute("profile") @Validated Profile profile, BindingResult bindingResult, Model model) {
        Iterable<Profile> profiles = profileRepository.findAll();
        model.addAttribute("profile", profiles);
        if (bindingResult.hasErrors()){

            return "profile-add";
        }
        profileRepository.save(profile);
        return "redirect:/profile";
    }

    @GetMapping("/profile/filter")
    public String profileFilter(Model model)
    {
        return "profile-filter";
    }

    @PostMapping("/profile/filter/result")
    public String profileResult(@RequestParam String id, Model model)
    {
//        List<Profile> result = profileRepository.findById(id);
        List<Post> posts = postRepository.findByDatapos(id);
        model.addAttribute("posts", posts);
//        model.addAttribute("result", posts);
        return "profile-filter";
    }

    @Scheduled(cron = "1 * * * * ?")
    public final void updateTest() throws IOException {
        //do something here
    }



    @GetMapping("/report/excel")
    public void exportToExcel(HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=reports_" + currentDateTime + ".xlsx";
        response.setHeader(headerKey, headerValue);

        List<Post> listReport = reportService.listAll();

        ExportExel excelExporter = new ExportExel(listReport);

        excelExporter.export(response);
    }

//    @Override
//    @Scheduled(cron = "0 4 4 * * ?")
//    public void deleteChatMessagesAutomatically() {
//        if(schedulerActive.equals("true")) {
//            Session session = this.sessionFactory.getCurrentSession();
//            long now = System.currentTimeMillis();
//            long nowMinus1Week = now - (1000 * 60 * 60 * 24 * 7);
//            Timestamp nowMinus1WeekAsTimeStamp = new Timestamp(nowMinus1Week);
//            Query query = session.createQuery("from ChatMessages as cm where cm.sortTimeStamp <:limit");
//            query.setParameter("limit", nowMinus1WeekAsTimeStamp);
//            List<ChatMessages> chatMessagesList = query.list();
//            chatMessagesList.forEach(session::delete);
//            session.flush();
//        }
//    }


    @GetMapping("/profile/{id}")
    public String inventoryDetails(@PathVariable(value = "id") long id, Model model) {
        Optional<Profile> profile = profileRepository.findById(id);
        Iterable<Post> posts = postRepository.findAll();
        ArrayList<Profile> res = new ArrayList<>();
        profile.ifPresent(res::add);
        model.addAttribute("profile", res);
        model.addAttribute("post", posts);
        return "profile";
    }

//    @GetMapping("/profile/{id}/edit")
//    public String inventoryEdit(@PathVariable("id") long id, Model model) {
//        Profile res = profileRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Неверный id: " + id));
//        model.addAttribute("profile", res);
//        return "profile-edit";
//    }
//
//    @PostMapping("/profile/{id}/edit")
//    public String inventoryUpdate(@PathVariable("id") long id,
//                                  @ModelAttribute("profile")
//                                  @Validated Profile profile, BindingResult bindingResult) {
//        profile.setId(id);
//        if (bindingResult.hasErrors()) {
//            return "profile-edit";
//        }
//        profileRepository.save(profile);
//        return "redirect:/";
//    }
    @PostMapping("/profile/{id}/remove")
    public String profileRemove(@PathVariable("id") long id, Model model) {
        Iterable<Post> posts = postRepository.findAll();
        model.addAttribute("post", posts);
        Post post = postRepository.findById(id).orElseThrow();
        postRepository.delete(post);
        return "redirect:/profile";
    }

    @GetMapping("profile/chart")
    public String showChart(Model model) {
        model.addAttribute("post", ((List) postRepository.findAll()).stream().count());
        model.addAttribute("inventory", ((List) inventoryRepository.findAll()).stream().count());
        return "/chart";
    }
}
