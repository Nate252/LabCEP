package com.Dev.CreateEstimatingPlan.controller;

import com.Dev.CreateEstimatingPlan.exception.ExcelReadHandlingException;
import com.Dev.CreateEstimatingPlan.exception.GroupNotFoundException;
import com.Dev.CreateEstimatingPlan.serviceImpl.ExcelFileCreationService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


@Controller
public class CreateExcelController {
    private final ExcelFileCreationService excelFileCreationService;
    private final Map<String, File> tempFiles = new ConcurrentHashMap<>();

    public CreateExcelController(ExcelFileCreationService excelFileCreationService) {
        this.excelFileCreationService = excelFileCreationService;
    }

    private static final String MK_DIR = "uploadRemind/";

    static {
        new File(MK_DIR).mkdirs();
    }

    @GetMapping("/UploadFile")
    public String uploadFileShow() {
        return "CreateExcelFile/CreateExcel";
    }

    @PostMapping("/UploadFile")
    public String uploadFile(@RequestParam("file-upl") MultipartFile file, RedirectAttributes redirectAttributes, Model model) {
        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Файл не вибрано!");
            return "redirect:/UploadFile";
        }

        try {
            File tempFile = File.createTempFile("generated_", ".xlsx");
            byte[] excelData = excelFileCreationService.createExcelTable(file);

            try (FileOutputStream fos = new FileOutputStream(tempFile)) {
                fos.write(excelData);
            }

            String fileId = UUID.randomUUID().toString();
            tempFiles.put(fileId, tempFile);
            model.addAttribute("fileId", fileId);
            return "CreateExcelFile/CreateExcel";


        } catch (ExcelReadHandlingException e) {
            redirectAttributes.addFlashAttribute("error", e);
        } catch (GroupNotFoundException e) {
            redirectAttributes.addFlashAttribute("error", "Помилка читання файлу: " + e.getMessage());

        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("error", "Помилка створення файлу: " + e.getMessage());
        }

        return "redirect:/UploadFile";
    }

    @GetMapping("/DownloadFile")
    public ResponseEntity<?> downloadFile(@RequestParam("fileId") String fileId) {
        File tempFile = tempFiles.remove(fileId);
        if (tempFile == null || !tempFile.exists()) {
            return ResponseEntity.badRequest().body("Файл не знайдено або він уже видалений.");
        }

        try {
            ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(tempFile.toPath()));

            Executors.newSingleThreadScheduledExecutor().schedule(() -> {
                tempFile.delete();
            }, 5, TimeUnit.SECONDS);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=Plan.xlsx")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("Помилка при завантаженні файлу.");
        }
    }

    @GetMapping("/DownloadTemplateUk")
    public ResponseEntity<Resource> downloadTemplateUk() {
        ClassPathResource resource = new ClassPathResource("excelTemplates/ПланВивченняНД_uk.xlsm");

        if (!resource.exists()) {
            return ResponseEntity.notFound().build();
        }

        String contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
        String encodedFilename = URLEncoder.encode(resource.getFilename(), StandardCharsets.UTF_8);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename*=UTF-8''" + encodedFilename)
                .header(HttpHeaders.CONTENT_TYPE, contentType)
                .body(resource);
    }

    @GetMapping("/DownloadTemplateEn")
    public ResponseEntity<Resource> downloadTemplateEn() {
        ClassPathResource resource = new ClassPathResource("excelTemplates/ADStudyPlan_en.xlsm");

        if (!resource.exists()) {
            return ResponseEntity.notFound().build();
        }

        String contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
        String encodedFilename = URLEncoder.encode(resource.getFilename(), StandardCharsets.UTF_8);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename*=UTF-8''" + encodedFilename)
                .header(HttpHeaders.CONTENT_TYPE, contentType)
                .body(resource);
    }
}