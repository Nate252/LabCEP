package testUtil.TestWriters;

import com.Dev.CreateEstimatingPlan.entity.students.Group;
import com.Dev.CreateEstimatingPlan.entity.students.Student;
import com.Dev.CreateEstimatingPlan.locale.ExcelLocaleProvider;
import com.Dev.CreateEstimatingPlan.writerExcel.ExcelWriters.ListStudentsFileExcel;
import com.Dev.CreateEstimatingPlan.writerExcel.ExcelWriters.SharedData;
import org.antlr.v4.runtime.misc.Pair;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestListStudentsFileExcel {
    public static final String[] typesMarker = {
            ExcelLocaleProvider.getMessage("sheet.writer.list_of_students.drop_down.yes"),
            ExcelLocaleProvider.getMessage("sheet.writer.list_of_students.drop_down.no")};
    public static final String[] countries = {
            ExcelLocaleProvider.getMessage("sheet.writer.list_of_students.drop_down.abroad"),
            ExcelLocaleProvider.getMessage("sheet.writer.list_of_students.drop_down.uk")};
    public static final String[] headers = {"#",
            ExcelLocaleProvider.getMessage("sheet.writer.list_of_students.header.group"),
            ExcelLocaleProvider.getMessage("sheet.writer.list_of_students.header.surname"),
            ExcelLocaleProvider.getMessage("sheet.writer.list_of_students.header.name"),
            ExcelLocaleProvider.getMessage("sheet.writer.list_of_students.header.middle"),
            ExcelLocaleProvider.getMessage("sheet.writer.list_of_students.header.email"),
            ExcelLocaleProvider.getMessage("sheet.writer.list_of_students.header.phone"),
            ExcelLocaleProvider.getMessage("sheet.writer.list_of_students.header.contract"),
            ExcelLocaleProvider.getMessage("sheet.writer.list_of_students.header.campus"),
            ExcelLocaleProvider.getMessage("sheet.writer.list_of_students.header.country")};

    public static List<Group> createGroups() {
        Group groupB = new Group("КН-222б");
        Group groupV = new Group("КН-222в");
        Group groupG = new Group("КН-222г");

        groupB.setStudents(new ArrayList<>(List.of(
                Student.builder()
                        .surname("АЛАФІНА")
                        .name("Маргарита")
                        .middleName("Русланівна")
                        .email("marharyta.alafina@cs.khpi.edu.ua")
                        .group(groupB)
                        .build(),
                Student.builder()
                        .surname("АНТИПЕНКО")
                        .name("Катерина")
                        .middleName("Віталіївна")
                        .email("antipenkokaterina84@gmail.com")
                        .phoneNumber("0636449541")
                        .secondPhoneNumber("0995062924")
                        .group(groupB)
                        .build()
        )));

        groupV.setStudents(new ArrayList<>(List.of(
                Student.builder()
                        .surname("БАБАРИЦЬКИЙ")
                        .name("Юрій")
                        .middleName("Вікторович")
                        .email("yura@gmail.com")
                        .phoneNumber("0500382362")
                        .secondPhoneNumber("0955266102")
                        .group(groupV)
                        .build(),
                Student.builder()
                        .surname("БАРАНОВ")
                        .name("Максим")
                        .middleName("Дмитрович")
                        .email("maksym.baranov2015@gmail.com")
                        .phoneNumber("0500136169")
                        .group(groupV)
                        .build()
        )));

        groupG.setStudents(new ArrayList<>(List.of(
                Student.builder()
                        .surname("ВЛАДІМІРОВА")
                        .name("Тетяна")
                        .middleName("Петрівна")
                        .email("vladimirovatanya1107@gmail.com")
                        .group(groupG)
                        .build(),
                Student.builder()
                        .surname("ГОЛОВАЩЕНКО")
                        .name("Володимир")
                        .middleName("Володимирович")
                        .email("Volodymyr.Holovashchenko@gmail.com")
                        .group(groupG)
                        .build()
        )));

        return new ArrayList<>(List.of(groupB, groupV, groupG));
    }

    public static Map<String, Pair<Integer, Integer>> createTestSharedData() {
        Map<String, Pair<Integer, Integer>> listNumGroups = new HashMap<>();
        listNumGroups.put("КН-222б", new Pair<>(3, 4));
        listNumGroups.put("КН-222в", new Pair<>(5, 6));
        listNumGroups.put("КН-222г", new Pair<>(7, 8));
        return listNumGroups;
    }

    private static SharedData createSharedData() {
        return new SharedData(new XSSFWorkbook());
    }

    public static ListStudentsFileExcel createListStudentsFileExcel() {
        String nameDiscipline = "АППЗ";

        return ListStudentsFileExcel.builder()
                .nameDiscipline(nameDiscipline)
                .groups(createGroups())
                .sharedData(createSharedData())
                .build();
    }
}
