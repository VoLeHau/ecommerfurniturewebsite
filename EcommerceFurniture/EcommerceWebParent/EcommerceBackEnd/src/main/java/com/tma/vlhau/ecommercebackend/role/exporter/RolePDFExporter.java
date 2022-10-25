package com.tma.vlhau.ecommercebackend.role.exporter;

import com.lowagie.text.Font;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.tma.vlhau.ecommercebackend.AbstractExporter;
import com.tma.vlhau.ecommercecommon.entity.Role;
import com.tma.vlhau.ecommercecommon.entity.User;

import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.util.List;

public class RolePDFExporter extends AbstractExporter<Role> {

    String[] headers = {"Role ID", "Role Name", "Description"};
    String[] fieldMapping = {"id", "name", "description"};

    @Override
    public void export(List<Role> listRoles, HttpServletResponse response) throws IOException {
        super.setResponseHeader(response, "application/pdf",".pdf", "Role");

        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document,response.getOutputStream());

        document.open();
        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        font.setSize(18);
        font.setColor(Color.BLUE);

        Paragraph paragraph = new Paragraph("List of roles", font);
        paragraph.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(paragraph);

        PdfPTable table = new PdfPTable(3);
        table.setWidths(new float[]{1.5f, 3.5f, 5.0f});
        table.setWidthPercentage(100f);
        table.setSpacingBefore(12);
        writeTableHeader(table);
        writeTableData(table, listRoles);
        document.add(table);

        document.close();
    }

    private void writeTableHeader(PdfPTable table) {
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(Color.ORANGE);
        cell.setPadding(5);

        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        font.setSize(16);
        font.setColor(Color.BLACK);

        cell.setPhrase(new Phrase("Role ID", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Role Name", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Description", font));
        table.addCell(cell);


    }

    private void writeTableData(PdfPTable table, List<Role> listRoles){
        for(Role role : listRoles){
            table.addCell(String.valueOf(role.getId()));
            table.addCell(role.getName());
            table.addCell(role.getDescription());

        }
    }

}
