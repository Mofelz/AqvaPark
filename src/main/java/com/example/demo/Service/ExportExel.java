package com.example.demo.Service;

import com.example.demo.models.Booking;
import com.example.demo.models.Cart;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.swing.*;
import java.io.IOException;
import java.util.List;

public class ExportExel {
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private List<Booking> listReport;



    public ExportExel(List<Booking> listReport) {
        this.listReport = listReport;
        workbook = new XSSFWorkbook();
    }

    private void writeHeaderLine() {
        sheet = workbook.createSheet("Отчеты");

        Row row = sheet.createRow(0);

        CellStyle style = workbook.createCellStyle();

        XSSFCellStyle cellStyle = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontName(XSSFFont.DEFAULT_FONT_NAME);
        font.setFontHeightInPoints((short)10);
        font.setColor(IndexedColors.BLUE.getIndex());
        cellStyle.setFont(font);
        style.setFont(font);

        createCell(row, 0, "Номер заказа", style);
        createCell(row, 1, "Пользователь", style);
        createCell(row, 2, "Номер столика", style);
        createCell(row, 3, "Статус заказа", style);
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
        int fullPrice = 0;
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(14);
        style.setFont(font);


        for (Booking booking : listReport) {
            int fullPriceOneOrder = 0;
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;
            int columnCount1 = 0;
            Row row1 = sheet.createRow(rowCount++);

            createCell(row, columnCount++, booking.getId().toString(), style);
            createCell(row, columnCount++, booking.getUser().getUsername().toString(), style);
            createCell(row, columnCount++, booking.getTableNumber().toString(), style);
            createCell(row, columnCount++, booking.getStatus().getNameStatus().toString(), style);

            createCell(row1,columnCount1++,"Наименование блюда",style);
            createCell(row1,columnCount1++,"Категория",style);
            createCell(row1,columnCount1++,"Вес",style);
            createCell(row1,columnCount1++,"Цена",style);
            createCell(row1,columnCount1++,"Количество",style);
            createCell(row1,columnCount1++,"Итоговая цена",style);

            for (Cart cart : booking.getCarts()) {

                Row row2 = sheet.createRow(rowCount++);
                int columnCount2 = 0;
                fullPriceOneOrder+=cart.getCount()*cart.getProduct().getPrice();
                fullPrice+=cart.getCount()*cart.getProduct().getPrice();
                createCell(row2, columnCount2++, cart.getProduct().getNameProduct().toString(), style);
                createCell(row2, columnCount2++, cart.getProduct().getCategory().getNameCategory().toString(), style);
                createCell(row2, columnCount2++, cart.getProduct().getWeight().toString(), style);
                createCell(row2, columnCount2++, cart.getProduct().getPrice().toString(), style);
                createCell(row2, columnCount2++, cart.getCount(), style);
                createCell(row2, columnCount2++, cart.getCount() * cart.getProduct().getPrice(), style);
            }

            row1 = sheet.createRow(rowCount++);
            columnCount1 = 0;
            createCell(row1, columnCount1++, "", style);
            createCell(row1, columnCount1++, "", style);
            createCell(row1, columnCount1++, "", style);
            createCell(row1, columnCount1++, "", style);
            createCell(row1, columnCount1++, "Итого", style);
            createCell(row1, columnCount1++, fullPriceOneOrder, style);
        }
        Row row1 = sheet.createRow(rowCount++);
        int columnCount1 = 0;
        createCell(row1, columnCount1++, "", style);
        createCell(row1, columnCount1++, "", style);
        createCell(row1, columnCount1++, "", style);
        createCell(row1, columnCount1++, "", style);
        createCell(row1, columnCount1++, "ИТОГ ПО ВСЕМ ЗАКАЗАМ", style);
        createCell(row1, columnCount1++, fullPrice, style);
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
