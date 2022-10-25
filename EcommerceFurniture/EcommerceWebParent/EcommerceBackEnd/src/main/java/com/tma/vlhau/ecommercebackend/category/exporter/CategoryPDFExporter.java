package com.tma.vlhau.ecommercebackend.category.exporter;

import com.lowagie.text.Font;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.tma.vlhau.ecommercebackend.AbstractExporter;
import com.tma.vlhau.ecommercecommon.entity.Category;
import com.tma.vlhau.ecommercecommon.entity.Role;

import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.util.List;

public class CategoryPDFExporter extends AbstractExporter<Category> {

    String[] headers = {"Category ID", "Category Name", "Alias", "Enabled", "Parent Name"};
    String[] fieldMapping = {"id", "name", "alias", "enabled", "parent"};

    @Override
    public void export(List<Category> listCategories, HttpServletResponse response) throws IOException {
        super.setResponseHeader(response, "application/pdf",".pdf", "Category");

        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document,response.getOutputStream());

        document.open();
        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        font.setSize(18);
        font.setColor(Color.BLUE);

        Paragraph paragraph = new Paragraph("List of Categories", font);
        paragraph.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(paragraph);

        PdfPTable table = new PdfPTable(5);
        table.setWidths(new float[]{1.5f, 3.0f, 3.0f,1.0f,3.0f});
        table.setWidthPercentage(100f);
        table.setSpacingBefore(12);
        writeTableHeader(table);
        writeTableData(table, listCategories);
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

        cell.setPhrase(new Phrase("Category ID", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Category Name", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Category Alias", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Enable", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Parent Name", font));
        table.addCell(cell);
    }

    private void writeTableData(PdfPTable table, List<Category> listCategories){
        for(Category category : listCategories){
            table.addCell(String.valueOf(category.getId()));
            if (category.getName().contains("-")) {
                String categoryName = category.getName();
                categoryName = categoryName.substring(categoryName.lastIndexOf("-")+1, categoryName.length());
                category.setName(categoryName);
                table.addCell(categoryName);
            }
            else {
                table.addCell(category.getName());
            }

            table.addCell(category.getAlias());
            table.addCell(String.valueOf(category.isEnabled()));
            if(category.getParent()==null){
                table.addCell("NULL");
            }
            else {
                table.addCell(category.getParent().getName());
            }
        }
    }

}
