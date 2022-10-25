package com.tma.vlhau.ecommercebackend.role.exporter;

import com.tma.vlhau.ecommercebackend.AbstractExporter;
import com.tma.vlhau.ecommercecommon.entity.Brand;
import com.tma.vlhau.ecommercecommon.entity.Role;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class RoleCSVExporter extends AbstractExporter<Role> {

    String[] headers = {"Role ID", "Role Name", "Description"};
    String[] fieldMapping = {"id", "name", "description"};

    @Override
    public void export(List<Role> listRoles, HttpServletResponse response) throws IOException {
        super.setResponseHeader(response, "text/csv", ".csv", "Role");

        ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);

        csvWriter.writeHeader(headers);

        for (Role role : listRoles) {
            try {
                csvWriter.write(role, fieldMapping);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        csvWriter.close();
    }

}
