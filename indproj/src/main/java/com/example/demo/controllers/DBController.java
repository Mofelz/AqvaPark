package com.example.demo.controllers;

import com.example.demo.Service.ConsoleService;
import com.example.demo.config.DBConfig;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Controller

@PreAuthorize("hasAnyAuthority('ADMIN')")
public class DBController {

    private final ResourceLoader resourceLoader;

    public DBController(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

@GetMapping( "/database/index")
    public String databaseIndex(Model model) {
        model.addAttribute("files", getBackupsList());
        return "backup/main";
    }

    @GetMapping("/database/downloadScript")
    public ResponseEntity<Resource> databaseDownloadScript(@RequestParam (value = "withData", required = false) Boolean withData) {
        DBConfig.initField();
        String command = String.format("mysqldump --column-statistics=0 --port=3307 -u%s -p%s -h%s %s > %s",
                DBConfig.username, DBConfig.password, DBConfig.host, DBConfig.dbname, "scripts/" + DBConfig.dbname + ".sql");
        if (withData == null)
            command += " —no-data";
        try {
            ConsoleService.exec(command);
            Resource resource = resourceLoader.getResource( Paths.get("scripts/" + DBConfig.dbname + ".sql").toUri().toString());
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + resource.getFilename())
                    .contentLength(resource.contentLength())
                    .body(resource);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/database/createBackup")
    public String databaseCreateBackup() {
        DBConfig.initField();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss_");
        String filename = simpleDateFormat.format(new Date()) + DBConfig.dbname+".sql";
        String command = String.format("mysqldump —column-statistics=0 -u%s -p%s -h%s —databases %s > %s",
                DBConfig.username,
                DBConfig.password,
                DBConfig.host,
                DBConfig.dbname,
                "backups/" + filename);
        try {
            ConsoleService.exec(command);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        return "redirect:/database/index";
    }

@PostMapping("/database/deleteBackup")
    public String databaseDeleteBackup(@RequestParam ("filename") String fileName) throws IOException {
        File file = new File(Paths.get("backups/" + fileName).toUri());
        Files.deleteIfExists(file.toPath());
        return "redirect:/database/index";
    }

    @PostMapping("/database/restoreData")
    public String databaseRestoreData(@RequestParam ("filename") String filename) {
        restoreData(filename, "backups");
        return "redirect:/database/index";
    }

    @PostMapping("/database/uploadBackup")
    public String databaseUploadBackup(@RequestParam ("file") MultipartFile file) throws IOException {
        Path path = Paths.get("backups/" + "customBackup.sql");
        File customBackup = new File(path.toUri());
        FileOutputStream fileOutputStream = new FileOutputStream(customBackup);
        fileOutputStream.write(file.getBytes());
        fileOutputStream.close();
        restoreData("customBackup.sql", "scripts");
        return "redirect:/database/index";
    }

    public List<String> getBackupsList() {
        Path path = Paths.get("backups");
        File folder = new File(path.toUri());
        List<String> backups = new ArrayList<>();
        for (File backup : folder.listFiles())
            if (backup.isFile())
                backups.add(backup.getName());
        Collections.reverse(backups);
        return backups;
    }

    private void restoreData(String fileName, String folder) {
        DBConfig.initField();
        File file = new File(Paths.get(folder + "/" + fileName).toUri());
        String command = String.format("mysql -u%s -p%s -h%s %s < %s",
                DBConfig.username,
                DBConfig.password,
                DBConfig.host,
                DBConfig.dbname,
                file.toPath());
        try {
            ConsoleService.exec(command);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}