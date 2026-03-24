package com.Dev.CreateEstimatingPlan.writerExcel.ExcelWriters;

import com.Dev.CreateEstimatingPlan.entity.StudyPlan.EvaluationSystem.EducationComponent;
import com.Dev.CreateEstimatingPlan.entity.StudyPlan.EvaluationSystem.MainEvaluation;
import com.Dev.CreateEstimatingPlan.entity.StudyPlan.EvaluationSystem.practice.PracticeComponent;
import com.Dev.CreateEstimatingPlan.entity.StudyPlan.EvaluationSystem.practice.Tasks;
import com.Dev.CreateEstimatingPlan.entity.StudyPlan.EvaluationSystem.theory.TheoryComponent;
import com.Dev.CreateEstimatingPlan.locale.ExcelLocaleProvider;
import com.Dev.CreateEstimatingPlan.writerExcel.ExcelBaseStyles;
import lombok.Builder;
import lombok.Getter;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;

import java.io.IOException;
import java.util.List;
@Getter
public class FinalEvaluationExcel extends AbstractTemplate {
    private final Sheet sheet;
    private final List<String> studentsGroupsNames;
    private final MainEvaluation mainEvaluation;

    @Builder
    public FinalEvaluationExcel(SharedData sharedData,
                                List<String> studentsGroupsNames, MainEvaluation mainEvaluation) {
        super(sharedData);
        sheet = workbook.createSheet(ExcelLocaleProvider.getMessage("sheet.writer.results.name"));
        this.studentsGroupsNames = studentsGroupsNames;
        this.mainEvaluation = mainEvaluation;
    }

    @Override
    void createHeader() {
        CellStyle headerStyle = sharedData.getStyle(ExcelBaseStyles.HEADER_12);
        CellStyle headerLGreenStyle = sharedData.getStyle(ExcelBaseStyles.HEADER_STYLE_LIGHT_GREEN_12);
        CellStyle headerGreenStyle = sharedData.getStyle(ExcelBaseStyles.HEADER_STYLE_GREEN_12);
        CellStyle headerBlueStyle = sharedData.getStyle(ExcelBaseStyles.HEADER_STYLE_BLUE_12);
        CellStyle headerBorrowStyle = sharedData.getStyle(ExcelBaseStyles.HEADER_STYLE_BORROW_12);


        writeStudentsHeader(sheet, 1);

        TheoryComponent theoryComponent = mainEvaluation.getTheoryComponent();
        PracticeComponent practiceComponent = mainEvaluation.getPracticeComponent();
        int counter = 4;
        Row firstRow = sheet.getRow(0);
        Row secondRow = sheet.getRow(1);

        if (theoryComponent.getComponent().getScope() != 0) {
            writeEducationComponent(
                    theoryComponent.getName(),
                    theoryComponent.getScope(),
                    counter, firstRow, secondRow,headerLGreenStyle);
            counter++;
            for (EducationComponent component : theoryComponent.getList()) {
                writeEducationComponent(
                        component.getName(),
                        component.getScope(),
                        counter, firstRow, secondRow);
                counter++;
            }
        }

        if (practiceComponent.getScope() != 0) {
            writeEducationComponent(
                    practiceComponent.getName(),
                    practiceComponent.getScope(),
                    counter, firstRow, secondRow, headerLGreenStyle);
            counter++;

            for (Tasks tasks : practiceComponent.getTasks()) {
                String name = tasks.getName();
                int scope = tasks.getScope();

                writeEducationComponent(name, scope, counter, firstRow, secondRow);
                counter++;
            }
        }

//        int sumAdditionScopes = mainEvaluation.getAdditionScopeForPractice() + mainEvaluation.getAdditionScopeForTheory();
        String perSemester = ExcelLocaleProvider.getMessage("sheet.evaluation_structure.perSemester");
        writeEducationComponent(perSemester + " (+ " + mainEvaluation.getSumAddition() + ")",
                mainEvaluation.getPerSemester(), counter,
                firstRow, secondRow, headerGreenStyle);

        counter++;


        writeEducationComponent(
                ExcelLocaleProvider.getMessage("sheet.writer.results.header.bonuses"),
                0,counter,firstRow,secondRow,headerBorrowStyle);
//        Cell cellBonuses = firstRow.createCell(counter);
//        cellBonuses.setCellValue("Бонуси");
//        cellBonuses.setCellStyle(createColorStyle(Colors.BORROW));

        counter++;
        writeEducationComponent(
                ExcelLocaleProvider.getMessage("sheet.evaluation_structure.perEXAMOrCERTIFICATION"),
                mainEvaluation.getPerDisciplineCreditOrExam(),
                counter,firstRow,secondRow,headerBorrowStyle);
//        Cell cellFinalEstim = firstRow.createCell(counter);
//        cellFinalEstim.setCellValue("Екзамен/Залік");
//        cellFinalEstim.setCellStyle(createColorStyle(Colors.BORROW));

        counter++;


        writeEducationComponent(
                ExcelLocaleProvider.getMessage("sheet.evaluation_structure.result"),
                100,counter,firstRow,secondRow,headerBlueStyle);
        counter++;


        Cell cellGrade = firstRow.createCell(counter);
        cellGrade.setCellValue( ExcelLocaleProvider.getMessage("sheet.writer.results.header.grade"));
        cellGrade.setCellStyle(headerBlueStyle);

        Cell cellGradeScope = secondRow.createCell(counter);
        cellGradeScope.setCellValue("5A");
        cellGradeScope.setCellStyle(headerBlueStyle);
        counter++;
    }

    @Override
    void createContent() {
        CellStyle headerLGreenStyle = sharedData.getStyle(ExcelBaseStyles.HEADER_STYLE_LIGHT_GREEN_12);
        CellStyle headerGreenStyle = sharedData.getStyle(ExcelBaseStyles.HEADER_STYLE_GREEN_12);
        CellStyle headerBlueStyle = sharedData.getStyle(ExcelBaseStyles.HEADER_STYLE_BLUE_12);
        CellStyle headerBorrowStyle = sharedData.getStyle(ExcelBaseStyles.HEADER_STYLE_BORROW_12);
        CellStyle headerPipiStyle = sharedData.getStyle(ExcelBaseStyles.BASE_STYLE_PIPI_12);
        CellStyle baseStyle = sharedData.getStyle(ExcelBaseStyles.BASE_STYLE_12);

        String sheetLecture = ExcelLocaleProvider.getMessage("name.component.lecture");
        String sheetLaboratory = ExcelLocaleProvider.getMessage("sheet.writer.laboratory.header.name");
        int lectureInd = 4;
        int maxLectureClasses = sharedData.getMaxLectureClasses();
        String addressLetter = sharedData.getLastCellLectureScopeAddress();


        writeStudentsLinks(studentsGroupsNames, sheet, 2);


        for (int i = 2; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);

            int counter = 4;
            Cell theoryMain = null;
            Cell practiceMain = null;
            if (mainEvaluation.getTheoryComponent().getScope() != 0) {
                int theoryMainIndex = counter;
                String lastCellScopeAddress;
                String firstCellScopeAddress;
                int firstCellScopeIndex;

                Cell cell = row.createCell(counter);
                cell.setCellStyle(headerLGreenStyle);
                counter++;
                firstCellScopeIndex = counter;
                for (int j = 0; j < mainEvaluation.getTheoryComponent().getList().size(); j++, counter++) {

                        Cell cellComponent = row.createCell(counter);
                        cellComponent.setCellStyle(headerPipiStyle);
                    // check lecture set formula
                    if (mainEvaluation.isLecture(j)) {

                        String addressLecture =  addressLetter + lectureInd; // =IF(Lectures!U4>0, (Lectures!U4 / 15) * 10, "")
//                        cellComponent.setCellFormula("IF(Лекції!" + addressLecture +
//                                ">0, (Лекції!" + addressLecture + "/" + maxLectureClasses + ") *"
//                                + mainEvaluation.getTheoryComponent().getList().get(0).getScope() + ", \"\")");
                        cellComponent.setCellFormula("IF(" + sheetLecture + "!" + addressLecture +
                                ">0, ROUNDUP((" + sheetLecture + "!" + addressLecture + "/" + maxLectureClasses + ") *" +
                                mainEvaluation.getTheoryComponent(j).getScope() + ", 1), \"\")");
                        formulaEvaluate(cellComponent);
                        lectureInd++;
                    }
                }
                // set default Sum theory Components
                lastCellScopeAddress = row.getCell(counter - 1).getAddress().formatAsString();
                firstCellScopeAddress = row.getCell(firstCellScopeIndex).getAddress().formatAsString();

                theoryMain = row.getCell(theoryMainIndex);

                // if formula with addition scopes or not
                if (mainEvaluation.getAdditionScopeForTheory() == 0){
                    theoryMain.setCellFormula("SUM(" + firstCellScopeAddress + ":" + lastCellScopeAddress + ")");
                }
                else {
                    theoryMain.setCellFormula(
                            "IF(SUM(" + firstCellScopeAddress + ":" + lastCellScopeAddress + ")>="
                                    + mainEvaluation.getTheoryComponent().getScope() + "," +
                            "SUM(" + firstCellScopeAddress + ":" + lastCellScopeAddress + ")+"
                                    + mainEvaluation.getAdditionScopeForTheory() + ","
                + "SUM(" + firstCellScopeAddress + ":" + lastCellScopeAddress + "))");
                }
                formulaEvaluate(theoryMain);

            }
            if (mainEvaluation.getPracticeComponent().getScope() != 0) {
                int practiceMainIndex = counter;
                int practiceComponentStartIndex;
                String lastCellScopeAddress;
                String firstCellPracticeComponent;

                Cell cellPr = row.createCell(counter);
                cellPr.setCellStyle(headerLGreenStyle);
                counter++;
                practiceComponentStartIndex = counter;

                for (int j = 0; j < mainEvaluation.getPracticeComponent().getTasks().size(); j++, counter++) {
                    // write cells laboratory
                    Cell cellComponent = row.createCell(counter);
                    cellComponent.setCellStyle(headerPipiStyle);

                    // write cells laboratory Formula with laboratory Sheet
                    Sheet labSheet = workbook.getSheet(sheetLaboratory);
                    Row rowLabScope = labSheet.getRow(i);
                    int cellIndexLabScope = sharedData.getListNumCellsLaboratoryScope().get(j);
                    String addressCell = rowLabScope.getCell(cellIndexLabScope).getAddress().formatAsString();

                    String addressSumLabScope = sheet.getRow(1).getCell(counter).getAddress().formatAsString();

                    cellComponent.setCellFormula("IF(" + sheetLaboratory +
                            "!" + addressCell + ">0, ROUNDUP("+addressSumLabScope +
                            "*REPLACE(" + sheetLaboratory +
                            "!" + addressCell + ", FIND(\".\"," + sheetLaboratory +
                            "!" + addressCell + "),1,\",\"), 0),\"\")");  //=IF(Laboratory!E3>0, CEILING($J$2*SUBSTITUTE(Laboratory!E3, FIND(".", Laboratory!E3), ","), 0), "")

                    XSSFFormulaEvaluator formulaEvaluator =
                            (XSSFFormulaEvaluator) workbook.getCreationHelper().createFormulaEvaluator();
                    formulaEvaluator.evaluateFormulaCell(cellComponent);
                }

                lastCellScopeAddress = row.getCell(counter - 1).getAddress().formatAsString();
                firstCellPracticeComponent = row.getCell(practiceComponentStartIndex).getAddress().formatAsString();
                practiceMain = row.getCell(practiceMainIndex);

                // set default formula
                if (mainEvaluation.getAdditionScopeForPractice() == 0) {
                    practiceMain.setCellFormula("SUM(" + firstCellPracticeComponent + ":" + lastCellScopeAddress + ")");
                }
                else {
                    practiceMain.setCellFormula(
                            "IF(SUM(" + firstCellPracticeComponent + ":" + lastCellScopeAddress + ")>="
                                    + mainEvaluation.getPracticeComponent().getScope() + "," +
                                    "SUM(" + firstCellPracticeComponent + ":" + lastCellScopeAddress + ")+"
                                    + mainEvaluation.getAdditionScopeForPractice() + ","
                                    + "SUM("+ firstCellPracticeComponent + ":" + lastCellScopeAddress + "))");
                }


                formulaEvaluate(practiceMain);
            }
            Cell perSemester = row.createCell(counter);
            perSemester.setCellStyle(headerGreenStyle);
            if (theoryMain != null && practiceMain != null) {
                perSemester.setCellFormula(theoryMain.getAddress().formatAsString() + "+" + practiceMain.getAddress().formatAsString());
            } else if (theoryMain != null) {
                perSemester.setCellFormula(theoryMain.getAddress().formatAsString());
            }
            else if (practiceMain != null){
                perSemester.setCellFormula(practiceMain.getAddress().formatAsString());
            }
            counter++;

          Cell bonusesCell = row.createCell(counter);
          bonusesCell.setCellStyle(baseStyle);
           counter++;
            Cell FinalScopeCellEX =  row.createCell(counter);
            FinalScopeCellEX.setCellStyle(baseStyle);
           counter++;

            Cell finalSumScopes = row.createCell(counter);
            finalSumScopes.setCellStyle(headerBlueStyle);
            finalSumScopes.setCellFormula(
                    row.getCell(counter - 1).getAddress().formatAsString()
                    + "+" + row.getCell(counter - 2).getAddress().formatAsString()
                    + "+" + row.getCell(counter - 3).getAddress().formatAsString());
            formulaEvaluate(finalSumScopes);

            counter++;

            Cell gradeCell = row.createCell(counter);
            gradeCell.setCellStyle(baseStyle);
            gradeCell.setCellFormula("IFERROR(CHOOSE(MATCH(" + finalSumScopes.getAddress().formatAsString()
                    + ",{60,64,75,82,90},1),\"3E\",\"3D\",\"4C\",\"4B\",\"5A\"),\"\")");
            formulaEvaluate(gradeCell);
            counter++;
        }

    }


    private void writeEducationComponent(String name, int scope, int cellIndex, Row firstRow, Row secondRow) {
        writeEducationComponent(name, scope, cellIndex, firstRow, secondRow,sharedData.getStyle(ExcelBaseStyles.HEADER_12));
    }

    private void writeEducationComponent(String name, int scope, int cellIndex, Row firstRow, Row secondRow, CellStyle cellStyle) {
        Cell cell = firstRow.createCell(cellIndex);
        cell.setCellValue(name);
        cell.setCellStyle(cellStyle);


        Cell theoryScope = secondRow.createCell(cellIndex);
        if (scope > 0) {
            theoryScope.setCellValue(scope);
        }
        theoryScope.setCellStyle(cellStyle);
    }

    private void formulaEvaluate(Cell cell) {
        XSSFFormulaEvaluator formulaEvaluator =
                (XSSFFormulaEvaluator) workbook.getCreationHelper().createFormulaEvaluator();
        formulaEvaluator.evaluateFormulaCell(cell);
    }
}
