package com.Dev.CreateEstimatingPlan.serviceImpl;

import com.Dev.CreateEstimatingPlan.entity.students.Group;
import com.Dev.CreateEstimatingPlan.entity.students.Student;
import com.Dev.CreateEstimatingPlan.repository.studentsRepository.GroupRepository;
import com.Dev.CreateEstimatingPlan.writerExcel.ExcelWriters.ListStudentsFileExcel;
import com.Dev.CreateEstimatingPlan.writerExcel.ExcelWriters.SharedData;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

//@SpringBootTest(properties = "spring.config.location=classpath:/application.properties")
class ExcelFileCreationServiceTest {
    private static ExcelFileCreationService excelService;

//    КН-222г	КН-222б	КН-222в
    @BeforeEach
    void setUp() {
        ListStudentsFileExcel abstractTemplate = new ListStudentsFileExcel(
                "",
                List.of(),
                new SharedData(new XSSFWorkbook())
        );

        // Спрощена імплементація GroupServiceImpl
        GroupRepository mockGroupRepository = Mockito.mock(GroupRepository.class);
        GroupServiceImpl groupService = new GroupServiceImpl(mockGroupRepository) {
            @Override
            public List<Group> getAllGroups(List<String> names) {
                Group group1 = new Group("КН-222б");
                Group group2 = new Group("КН-222в");
                Group group3 = new Group("КН-222г");

                Student student1 = new Student("Маргарита", "Алафіна", "Русланівна",
                        "marharyta.alafina@cs.khpi.edu.ua", group1);
                student1.setPhoneNumber("0981112233");
                student1.setSecondPhoneNumber("0933332211");

                Student student2 = new Student("Іван", "Іванов", "Іванович",
                        "ivan.ivanov@cs.khpi.edu.ua", group1);

                group1.setStudents(List.of(student1, student2));
                group2.setStudents(List.of(
                        new Student("Петро", "Петренко", "Петрович", "petro@cs.khpi.edu.ua", group2),
                        new Student("Олена", "Оленіна", "Олексіївна", "olena@cs.khpi.edu.ua", group2)
                ));
                group3.setStudents(List.of(
                        new Student("Андрій", "Андрієнко", "Андрійович", "andrii@cs.khpi.edu.ua", group3),
                        new Student("Світлана", "Світлична", "Сергіївна", "svitlana@cs.khpi.edu.ua", group3)
                ));

                group1.setStudents(new ArrayList<>(List.of(student1, student2)));
                group2.setStudents(new ArrayList<>(List.of(
                        new Student("Петро", "Петренко", "Петрович", "petro@cs.khpi.edu.ua", group2),
                        new Student("Олена", "Оленіна", "Олексіївна", "olena@cs.khpi.edu.ua", group2)
                )));
                group3.setStudents(new ArrayList<>(List.of(
                        new Student("Андрій", "Андрієнко", "Андрійович", "andrii@cs.khpi.edu.ua", group3),
                        new Student("Світлана", "Світлична", "Сергіївна", "svitlana@cs.khpi.edu.ua", group3)
                )));
                return List.of(group1, group2, group3);
            }
        };

        excelService = new ExcelFileCreationService(groupService);
    }
    @Test
    void createExcelTable() throws IOException {
        ClassPathResource resource = new ClassPathResource("test-files/ПланВивченняНД_uk.xlsm");
        byte[] fileContent = Files.readAllBytes(resource.getFile().toPath());
        MultipartFile multipartFile = new MockMultipartFile("file", "ПланВивченняНД_uk.xlsm", "application/vnd.ms-excel", fileContent);

        // When
        byte[] resultBytes = excelService.createExcelTable(multipartFile);
        try (InputStream inputStream = new ByteArrayInputStream(resultBytes);
             Workbook workbook = WorkbookFactory.create(inputStream)) {

        }

    }
}