package com.tma.vlhau.ecommercebackend.category.exporter;

import com.tma.vlhau.ecommercebackend.AbstractExporter;
import com.tma.vlhau.ecommercecommon.entity.Category;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.usermodel.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class CategoryExcelExporter extends AbstractExporter<Category> {

    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    String[] headers = {"Category ID", "Category Name", "Alias", "Enabled", "Parent Name"};
    String[] fieldMapping = {"id", "name", "alias", "enabled", "parent"};

    public CategoryExcelExporter() {
        workbook = new XSSFWorkbook();
    }

    @Override
    public void export(List<Category> listCategories, HttpServletResponse response) throws IOException {
        super.setResponseHeader(response, "application/octet-stream", ".xlsx", "category");

        writeHeaderLine();
        writeDataLine(listCategories);

        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();

    }

    private void writeHeaderLine() {
        sheet = workbook.createSheet("Categories");
        XSSFRow row = sheet.createRow(0);

        XSSFCellStyle headerStyle = createCellStyle(true, 16);

        writeRow(headers, row, headerStyle);
    }


    private void writeRow(String[] cells,XSSFRow row, XSSFCellStyle cellStyle) {
        int cellIndex = 0;
        for (String cell : cells){
            createCell(row, cellIndex++, cell, cellStyle);
        }
    }

    private void writeDataLine(List<Category> listCategories) {

        XSSFCellStyle normalStyle = createCellStyle(false, 13);

        int rowIndex = 1;
        for (Category category : listCategories){
            XSSFRow row = sheet.createRow(rowIndex++);

            int columnIndex = 0;
            createCell(row, columnIndex++, category.getId(), normalStyle);
            if (category.getName().contains("-")) {
                String categoryName = category.getName();
                categoryName = categoryName.substring(categoryName.lastIndexOf("-")+1, categoryName.length());
                category.setName(categoryName);
                createCell(row, columnIndex++, categoryName, normalStyle);
            }
            else {
                createCell(row, columnIndex++, category.getName(), normalStyle);
            }

            createCell(row, columnIndex++, category.getAlias(), normalStyle);
            createCell(row, columnIndex++, category.isEnabled(), normalStyle);
            if(category.getParent()==null){
                createCell(row, columnIndex++, "NULL", normalStyle);
            }
            else {
                createCell(row, columnIndex++, category.getParent(), normalStyle);
            }
        }
    }

    private XSSFCellStyle createCellStyle(Boolean isBold, int fontHeight) {
        XSSFCellStyle cellStyle = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(isBold);
        font.setFontHeight(fontHeight);
        cellStyle.setFont(font);
        return cellStyle;
    }

    private void createCell(XSSFRow row, int columnIndex, Object value, CellStyle cellStyle) {
        XSSFCell cell = row.createCell(columnIndex);
        sheet.autoSizeColumn(columnIndex);

        if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else {
            cell.setCellValue(value.toString());
        }
        cell.setCellStyle(cellStyle);
    }
}
