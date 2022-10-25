package com.tma.vlhau.ecommercebackend.role.exporter;

import com.tma.vlhau.ecommercebackend.AbstractExporter;
import com.tma.vlhau.ecommercecommon.entity.Role;
import com.tma.vlhau.ecommercecommon.entity.User;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.usermodel.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class RoleExcelExporter extends AbstractExporter<Role> {

    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    String[] headers = {"Role ID", "Role Name", "Description"};
    String[] fieldMapping = {"id", "name", "description"};

    public RoleExcelExporter() {
        workbook = new XSSFWorkbook();
    }

    @Override
    public void export(List<Role> listRoles, HttpServletResponse response) throws IOException {
        super.setResponseHeader(response, "application/octet-stream", ".xlsx", "Role");

        writeHeaderLine();
        writeDataLine(listRoles);

        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();

    }

    private void writeHeaderLine() {
        sheet = workbook.createSheet("Roles");
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

    private void writeDataLine(List<Role> listRoles) {

        XSSFCellStyle normalStyle = createCellStyle(false, 13);

        int rowIndex = 1;
        for (Role role : listRoles){
            XSSFRow row = sheet.createRow(rowIndex++);

            int columnIndex = 0;
            createCell(row, columnIndex++, role.getId(), normalStyle);
            createCell(row, columnIndex++, role.getName(), normalStyle);
            createCell(row, columnIndex++, role.getDescription(), normalStyle);

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
