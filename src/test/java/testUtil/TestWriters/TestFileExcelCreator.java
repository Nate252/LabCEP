package testUtil.TestWriters;

import com.Dev.CreateEstimatingPlan.entity.StudyPlan.mainEntity.StudyPlan;
import com.Dev.CreateEstimatingPlan.entity.students.Group;
import com.Dev.CreateEstimatingPlan.locale.ExcelLocaleProvider;
import com.Dev.CreateEstimatingPlan.serviceImpl.FileExcelCreator;
import com.Dev.CreateEstimatingPlan.writerExcel.ExcelWriters.ScheduleFileExcel;
import org.apache.poi.ss.usermodel.Sheet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Locale;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class TestFileExcelCreator {

    public static final String sheetLectureName = ExcelLocaleProvider.getMessage("name.component.lecture");
    public static final String sheetClassDatesName =    ExcelLocaleProvider.getMessage("sheet.writer.class_dates.name");
    public static final String sheetResultsName =    ExcelLocaleProvider.getMessage("sheet.writer.results.name");
    public static final String sheetLaboratoryName =    ExcelLocaleProvider.getMessage("sheet.writer.laboratory.header.name");
    public static final String sheetListStudentsName =    ExcelLocaleProvider.getMessage("sheet.writer.list_of_students.name");
}

