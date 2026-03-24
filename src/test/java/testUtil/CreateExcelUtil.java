package testUtil;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;

public class CreateExcelUtil {
    public static XSSFColor getXSSFColor(CellStyle cellStyle) {
        XSSFCellStyle xssfStyle = (XSSFCellStyle) cellStyle;
        return xssfStyle.getFillForegroundXSSFColor();
    }

    public static BorderStyle[] getBorderStyle(CellStyle cellStyle) {
        return new BorderStyle[]{
                cellStyle.getBorderTop(),
                cellStyle.getBorderBottom(),
                cellStyle.getBorderLeft(),
                cellStyle.getBorderRight()
        };
    }
}
