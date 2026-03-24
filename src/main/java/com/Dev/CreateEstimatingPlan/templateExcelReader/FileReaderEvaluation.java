package com.Dev.CreateEstimatingPlan.templateExcelReader;


import com.Dev.CreateEstimatingPlan.entity.StudyPlan.EvaluationSystem.EducationComponent;
import com.Dev.CreateEstimatingPlan.entity.StudyPlan.EvaluationSystem.MainEvaluation;
import com.Dev.CreateEstimatingPlan.entity.StudyPlan.EvaluationSystem.practice.PracticeComponent;
import com.Dev.CreateEstimatingPlan.entity.StudyPlan.EvaluationSystem.practice.Tasks;
import com.Dev.CreateEstimatingPlan.entity.StudyPlan.EvaluationSystem.theory.TheoryComponent;
import com.Dev.CreateEstimatingPlan.exception.ExcelReadHandlingException;
import com.Dev.CreateEstimatingPlan.locale.ExcelLocaleProvider;
import com.Dev.CreateEstimatingPlan.validator.ExcelCellValidator;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.*;
import java.util.function.Function;

public class FileReaderEvaluation implements Iterator<Row> {
    private final Iterator<Row> rowIterator;
    private Row currentRow;

    public FileReaderEvaluation(Sheet sheet) {
        this.rowIterator = sheet.iterator();
        if (rowIterator.hasNext()) {
            currentRow = rowIterator.next();
        }
    }

    @Override
    public boolean hasNext() {
        return rowIterator.hasNext();
    }

    @Override
    public Row next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        currentRow = rowIterator.next();
        return currentRow;
    }


    public MainEvaluation readEducationComponentMain() {
        next();

        TheoryComponent theoryComponent = getTheoryComponent();
        PracticeComponent practiceComponent = getPracticeComponent();
        int perSemester = getPerSemester();
        int perCreditOrExam = getFinalEvaluation();
        int additionScopeForTheory = getAdditionScopeForTheory();
        int additionScopeForPractice = getAdditionScopeForPractice();
        int sumScopesPerSemester = theoryComponent.getScope() + practiceComponent.getScope();


        if (!theoryComponent.compareSumTheory()) {
            generateExceptionExcel("Сумма балів елементів які входять до розділу ТЕОРІЯ не дорівнює загальним балам за ТЕОРІЯ");
        }
        if (practiceComponent.getTasks() != null) {
            if (!practiceComponent.compareSumPractice()) {
                generateExceptionExcel("Сумма балів елементів які входять до розділу ПРАКТИКА не дорівнює загальним балам за ПРАКТИКА");
            }
            if (practiceComponent.checkDeadlinesZero()) {
                generateExceptionExcel("Дедлайни вказано не для всіх завдань");
            }
        }

        if (sumScopesPerSemester != perSemester) {
            generateExceptionExcel("Сумма балів за ТЕОРІЯ та ПРАКТИКА не дорівнє балам ЗА СЕМЕСТР");
        }
        if (perSemester > 100) {
            generateExceptionExcel("Бали ЗА СЕМЕСТР не можуть бути більше 100");
        }

        if (perSemester + additionScopeForPractice + additionScopeForTheory > 100) {
            generateExceptionExcel("Сумма балів за семестр разом із додатковими балами перевищують 100");
        }
        if (perSemester + perCreditOrExam > 100) {
            generateExceptionExcel("Сумма балів за семестр разом із балами за ЕКЗАМЕН/ЗАЛІК перевищують 100");
        }
        return MainEvaluation.builder()
                .theoryComponent(theoryComponent)
                .practiceComponent(practiceComponent)
                .perSemester(perSemester)
                .perDisciplineCreditOrExam(perCreditOrExam)
                .additionScopeForTheory(additionScopeForTheory)
                .additionScopeForPractice(additionScopeForPractice)
                .build();
    }


    private TheoryComponent getTheoryComponent() {
        EducationComponent component = getMainEducationComponent();

        if (component.getScope() == 0) {
            while (currentRow.getCell(0) == null ||
                    !Objects.equals(currentRow.getCell(0).getStringCellValue(),
                            ExcelLocaleProvider.getMessage("sheet.evaluation_structure.practice"))) {
                next();
            }
            return new TheoryComponent(component, null);
        }
        return new TheoryComponent(component, getTheoryEducationComponents());
    }


    private PracticeComponent getPracticeComponent() {
        EducationComponent component = getMainEducationComponent();

        if (component.getScope() == 0) {
            while (currentRow.getCell(0) == null ||
                    !Objects.equals(currentRow.getCell(0).getStringCellValue(),
                            ExcelLocaleProvider.getMessage("sheet.evaluation_structure.perSemester"))) {
                next();
            }
            return new PracticeComponent(component, null);
        }
        return new PracticeComponent(component, getPracticeComponents());
    }

    private EducationComponent getMainEducationComponent() {
        String name = currentRow.getCell(0).getStringCellValue();
        int scope;

        Cell scopeCell = currentRow.getCell(1);
        if (ExcelCellValidator.checkNumCellWithZero(scopeCell)) {
            generateExceptionExcel(1, "Некоректно задані бали");
        }
        scope = (int) scopeCell.getNumericCellValue();

        next();
        return new EducationComponent(name, scope);
    }

    private EducationComponent getEducationComponent(String prefixToRemove) {
        String name;
        int scope;

        Cell nameCell = currentRow.getCell(0);
        Cell scopeCell = currentRow.getCell(1);


        if (ExcelCellValidator.checkIsCellEmptyWithCellTypeString(nameCell)) {
            generateExceptionExcel(0, "Пропущено назву");

        }
        if (ExcelCellValidator.checkNumCellWithoutZero(scopeCell)) {
            generateExceptionExcel(1, "Некоректно задані бали");
        }

        name = nameCell.getStringCellValue();
        scope = (int) scopeCell.getNumericCellValue();

        if (prefixToRemove != null && name.startsWith(prefixToRemove)) {
            name = name.substring(1);
        }

        next();
        return new EducationComponent(name, scope);
    }

    private EducationComponent getEducationComponent() {
        return getEducationComponent(null);
    }

    private List<EducationComponent> getTheoryEducationComponents() {
        List<EducationComponent> theoryComponents = new ArrayList<>();
        while (!Objects.equals(currentRow.getCell(0).getStringCellValue(),
                ExcelLocaleProvider.getMessage("sheet.evaluation_structure.practice"))) {
            theoryComponents.add(getEducationComponent());
        }
        return theoryComponents;
    }

    // add method read and return deadline
    private List<Tasks> getPracticeComponents() {
        String prefixLab = "#";
        String prefixTask = "*";
        EducationComponent component;
        List<Tasks> practiceTasks = new ArrayList<>();
        int deadline = 0;
        Cell startCell = currentRow.getCell(0);
//
//        if (NameValidator.nameIsEmpty(currentRow.getCell(0).getStringCellValue())) {
//            generateExceptionExcel(0,   "Пропущено назву");
//        }

        if (ExcelCellValidator.checkIsInvalidStringCellWithPrefix(startCell, prefixLab)) {
            String message = "Неправильно задано назву практичного завдання";

            if (Objects.equals(startCell.getStringCellValue(),
                    ExcelLocaleProvider.getMessage("sheet.evaluation_structure.perSemester"))) {
                message = "Відсутні елементи які входять до ПРАКТИКА";
            }
            generateExceptionExcel(0, message);

        }

        while (currentRow.getCell(0).getStringCellValue().startsWith("#")) {
//            String nameLab = currentRow.getCell(0).getStringCellValue();
//
//            if (NameValidator.nameIsEmpty(nameLab)) {
//                generateExceptionExcel(0,   "1Пропущено назву лабораторної роботи");
//            }

            List<String> listDescription = new ArrayList<>();
            deadline = getDeadLine();
            component = getEducationComponent("#");

//            if (NameValidator.nameIsEmpty(currentRow.getCell(0).getStringCellValue())) {
//                generateExceptionExcel(0,   "2Пропущено назву лабораторної роботи");
//            }
            while (currentRow.getCell(0).getStringCellValue().startsWith("*")
                    &&
                    !Objects.equals(currentRow.getCell(0).getStringCellValue(),
                            ExcelLocaleProvider.getMessage("sheet.evaluation_structure.perSemester"))) {

                listDescription.add(getDescription());
            }
            practiceTasks.add(new Tasks(component, listDescription, deadline));
        }
        if (!Objects.equals(currentRow.getCell(0).getStringCellValue(),
                ExcelLocaleProvider.getMessage("sheet.evaluation_structure.perSemester"))) {
            generateExceptionExcel(0, "Пропущено назву лабораторної роботи або завдання");
        }
        return practiceTasks;
    }

    private String getDescription() {
        Cell descriptionName = currentRow.getCell(1);
        if (ExcelCellValidator.checkIsCellEmptyWithCellTypeString(descriptionName)) {
            generateExceptionExcel(1, "Пропущено назву опису завдання");
        }
        String description = descriptionName.getStringCellValue();
        next();
        return description;
    }

    private int getDeadLine() {
        Cell deadLine = currentRow.getCell(2);
        if (ExcelCellValidator.checkIsCellStringNull(deadLine)) {
            return 0;
        } else if (ExcelCellValidator.checkNumCellWithoutZero(deadLine)) {
            generateExceptionExcel(2, "Некоректно задане значення дедлайну");
        }

        return (int) currentRow.getCell(2).getNumericCellValue();
    }

    private int getNumericValue(Function<Cell, Boolean> checkCellScope) {
        Cell numberCell = currentRow.getCell(1);
        if (checkCellScope.apply(numberCell)) {
            generateExceptionExcel(1, "Некоректно задані бали");
        }
        int value = (int) numberCell.getNumericCellValue();

        next();
        return value;
    }

    private int getPerSemester() {
        return getNumericValue(ExcelCellValidator::checkNumCellWithoutZero);
    }

    private int getFinalEvaluation() {
        return getNumericValue(ExcelCellValidator::checkNumCellWithZero);
    }

    private int getAdditionScopeForTheory() {
        return getNumericValue(ExcelCellValidator::checkNumCellWithZero);
    }

    private int getAdditionScopeForPractice() {
        return getNumericValue(ExcelCellValidator::checkNumCellWithZero);
    }

//    public int getTotalExtraPoints() {
//        return getNumericValue(NameValidator::checkCellNumWithZero);
//    }

    private void generateExceptionExcel(String message) {
        throw new ExcelReadHandlingException(
                currentRow.getSheet().getSheetName(),
                message
        );
    }

    private void generateExceptionExcel(int cell, String message) {
        throw new ExcelReadHandlingException(
                currentRow.getSheet().getSheetName(),
                currentRow.getCell(cell).getAddress().formatAsString(),
                message
        );
    }
}
