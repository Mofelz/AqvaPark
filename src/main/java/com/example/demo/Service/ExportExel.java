package com.example.demo.Service;

import com.example.demo.models.Post;
import com.example.demo.models.Profile;
import org.apache.poi.ss.formula.functions.Function;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class ExportExel {
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private List<Post> listReport;

    public ExportExel(List<Post> listReport) {
        this.listReport = listReport;
        workbook = new XSSFWorkbook();
    }

    private void writeHeaderLine() {
        sheet = workbook.createSheet("Отчеты");

        Row row = sheet.createRow(0);

        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(false);
        font.setFontHeight(16);
        style.setFont(font);

        createCell(row, 0, "Код билета", style);
        createCell(row, 1, "Пользователь", style);
        createCell(row, 2, "Дата посещения", style);
        createCell(row, 3, "Посетители", style);
        createCell(row, 4, "Взрослые", style);
        createCell(row, 5, "Дети", style);
        createCell(row, 6, "Услуга", style);
        createCell(row, 8, "Сумма", style);
    }

    private void createCell(Row row, int columnCount, Object value, CellStyle style) {
        sheet.autoSizeColumn(columnCount);
        Cell cell = row.createCell(columnCount);
        if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        }else {
            cell.setCellValue((String) value);
        }
        cell.setCellStyle(style);
    }

    private void writeDataLines() {
        int rowCount = 1;

        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(14);
        style.setFont(font);


        for (Post user : listReport) {
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;

            createCell(row, columnCount++, user.getId().toString(), style);
            createCell(row, columnCount++, user.getUser().getUsername().toString(), style);
            createCell(row, columnCount++, user.getDatapos().toString(), style);
            createCell(row, columnCount++, user.getVisitors().toString(), style);
            createCell(row, columnCount++, user.getAdults().toString(), style);
            createCell(row, columnCount++, user.getChildren().toString(), style);
            createCell(row, columnCount++, user.getUslaga().toString(), style);
            createCell(row, columnCount++, user.getPrice().toString(), style);
        }
    }

    public void export(HttpServletResponse response) throws IOException {
        writeHeaderLine();
        writeDataLines();

        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();

        outputStream.close();
    }
}
