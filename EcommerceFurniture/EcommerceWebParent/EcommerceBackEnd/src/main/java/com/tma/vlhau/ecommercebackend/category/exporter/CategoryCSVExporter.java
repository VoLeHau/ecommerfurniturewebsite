package com.tma.vlhau.ecommercebackend.category.exporter;

import com.tma.vlhau.ecommercebackend.AbstractExporter;
import com.tma.vlhau.ecommercecommon.entity.Category;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class CategoryCSVExporter extends AbstractExporter<Category> {

    String[] headers = {"Category ID", "Category Name", "Category Alias", "Enabled", "Parent Name"};
    String[] fieldMapping = {"id", "name", "alias", "enabled", "parent"};

    @Override
    public void export(List<Category> listCategories, HttpServletResponse response) throws IOException {
        super.setResponseHeader(response, "text/csv", ".csv", "category");

        ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);

        csvWriter.writeHeader(headers);

        for (Category category : listCategories) {
            try {
                if (category.getName().contains("-")) {
                    String categoryName = category.getName();
                    categoryName = categoryName.substring(categoryName.lastIndexOf("-")+1, categoryName.length());
                    category.setName(categoryName);
                }
                csvWriter.write(category, fieldMapping);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        csvWriter.close();
    }

}
