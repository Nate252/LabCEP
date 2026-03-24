package com.Dev.CreateEstimatingPlan.serviceImpl;

import com.Dev.CreateEstimatingPlan.locale.ExcelLocaleProvider;
import com.Dev.CreateEstimatingPlan.templateExcelReader.FileReaderMain;
import com.Dev.CreateEstimatingPlan.entity.StudyPlan.Shcedule.Lesson;
import com.Dev.CreateEstimatingPlan.entity.StudyPlan.mainEntity.StudyPlan;
import com.Dev.CreateEstimatingPlan.entity.students.Group;
import com.Dev.CreateEstimatingPlan.entity.students.Student;
import com.Dev.CreateEstimatingPlan.writerExcel.*;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.Collator;
import java.time.LocalDate;
import java.util.*;


@Service
public class ExcelFileCreationService {
   private final GroupServiceImpl groupService;

    public ExcelFileCreationService(GroupServiceImpl groupService) {
        this.groupService = groupService;
    }
    public static void setLocaleExcel(String fileName) {
        System.out.println(fileName);
        if (fileName.endsWith("en.xlsm")) {
            ExcelLocaleProvider.setLocale(Locale.ENGLISH);
        } else if (fileName.endsWith("uk.xlsm")) {
            ExcelLocaleProvider.setLocale(new Locale("uk"));
        }
        else {
            throw new NoSuchElementException("В назві файлу шаблону не вказано мову \"uk\" або \"en\"");
        }
    }
    public byte[] createExcelTable(MultipartFile file) throws IOException {
        setLocaleExcel(file.getOriginalFilename());

        Workbook workbook = new XSSFWorkbook(file.getInputStream());
        FileReaderMain fileReaderMain = new FileReaderMain(workbook);
        StudyPlan studyPlan = fileReaderMain.createPlan();

        FileExcelCreator fileExcelCreator = new FileExcelCreator(studyPlan);
        List<String> groupsNames = studyPlan.getNamesGroupsStudent();
        List<Group> groups = groupService.getAllGroups(groupsNames);
        return fileExcelCreator.createExcel(groups);
    }
}
