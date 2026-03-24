package com.Dev.CreateEstimatingPlan.writerExcel.ExcelWriters;

import com.Dev.CreateEstimatingPlan.writerExcel.CreatorStyles;
import lombok.Data;
import org.antlr.v4.runtime.misc.Pair;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class SharedData {
    private Map<String, Pair<Integer,Integer>> listNumGroups = new HashMap<>();
    private String LastCellLectureScopeAddress;
    private int maxLectureClasses;
    private List<Integer> listNumCellsLaboratoryScope = new ArrayList<>();

    private final Workbook workbook;
    private final Map<String, CellStyle> styles;

    public SharedData(Workbook workbook) {
        this.workbook = workbook;
        this.styles = createStyles();
    }

    private Map<String, CellStyle> createStyles() {
        CreatorStyles creatorStyles = new CreatorStyles(workbook);
        return creatorStyles.createMapStyles();
    }

    public CellStyle getStyle(String nameStyle) {
        return styles.get(nameStyle);
    }

}
