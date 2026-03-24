package com.Dev.CreateEstimatingPlan.exception;

import lombok.Data;
import lombok.Getter;

@Getter
public class ExcelReadHandlingException extends RuntimeException {
    private final String sheetName;
    private final String cellAddress;

    public ExcelReadHandlingException(String sheetName, String cellAddress, String message) {
        super(message);
        this.sheetName = sheetName;
        this.cellAddress = cellAddress;
    }

    public ExcelReadHandlingException(String sheetName, String message) {
        super(message);
        this.sheetName = sheetName;
        this.cellAddress = "";
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Помилка");

        if (sheetName != null && !sheetName.isEmpty()) {
            sb.append(" в аркуші '").append(sheetName).append("'");
        }

        if (cellAddress != null && !cellAddress.isEmpty()) {
            sb.append(", комірка ").append(cellAddress);
        }

        if (getMessage() != null && !getMessage().isEmpty()) {
            sb.append(" — ").append(getMessage());
        }

        return sb.toString();
    }
}
