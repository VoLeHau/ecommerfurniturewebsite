package com.tma.vlhau.ecommercebackend.customer.controller;

import com.tma.vlhau.ecommercebackend.address.AddressService;
import com.tma.vlhau.ecommercebackend.customer.exporter.CustomerCSVExporter;
import com.tma.vlhau.ecommercebackend.customer.service.CustomerService;
import com.tma.vlhau.ecommercecommon.entity.Address;
import com.tma.vlhau.ecommercecommon.entity.Country;
import com.tma.vlhau.ecommercecommon.entity.Customer;
import com.tma.vlhau.ecommercecommon.exception.CustomerNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class CustomerController {

    public static final Logger LOGGER = LoggerFactory.getLogger(CustomerController.class);

    @Autowired
    private CustomerService customerService;

    @Autowired
    private AddressService addressService;

    @GetMapping("/customers")
    public String listFirstPage(Model model) {
        return listByPage(model, 1, "firstName", "asc", null);
    }

    @GetMapping("/customers/page/{pageNum}")
    public String listByPage(Model model, @PathVariable(name = "pageNum") int pageNum,
                             @Param("sortField") String sortField,
                             @Param("sortDir") String sortDir,
                             @Param("keyword") String keyword) {

        Page<Customer> listCustomersPage = customerService.listByPage(pageNum, sortField, sortDir, keyword);
        List<Customer> listCustomers = listCustomersPage.getContent();

        List<Address> listAddresses = new ArrayList<>();

        for(Customer customer : listCustomers){
            if(addressService.getAddressByCustomer(customer)!=null){
                listAddresses.add(addressService.getAddressByCustomer(customer));
            }
        }


        long startCount = (long) (pageNum - 1) * CustomerService.CUSTOMERS_PER_PAGE + 1;
        long endCount = startCount + CustomerService.CUSTOMERS_PER_PAGE - 1;

        if (endCount > listCustomersPage.getTotalElements()) {
            endCount = listCustomersPage.getTotalElements();
        }

        String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";

        model.addAttribute("sortOrder", sortDir);
        model.addAttribute("reverseSortOrder", reverseSortDir);
        model.addAttribute("keyword", keyword);
        model.addAttribute("listCustomers", listCustomers);
        model.addAttribute("startCount", startCount);
        model.addAttribute("endCount", endCount);
        model.addAttribute("currentPage", pageNum);
        model.addAttribute("totalItems", listCustomersPage.getTotalElements());
        model.addAttribute("totalPages", listCustomersPage.getTotalPages());
        model.addAttribute("sortField", sortField);
        model.addAttribute("listAddresses", listAddresses);
        
        model.addAttribute("module", "customers");

        return "customer/customers";
    }

//    @PostMapping("/customers/save")
//    public String saveCustomer(Customer customer, RedirectAttributes redirectAttributes, Model model) {
//
//        customerService.save(customer);
//
//        redirectAttributes.addFlashAttribute("message", "The customer was saved successfully");
//
//        return getRedirectURLToAffectedCustomer(customer);
//    }
//
//    private String getRedirectURLToAffectedCustomer(Customer customer) {
//        return "redirect:/customers/page/1?sortField=id&sortDir=asc&keyword=" + customer.getFirstName();
//    }

//    @GetMapping("/customers/edit/{id}")
//    public String editCustomer(@PathVariable(name = "id") Integer id,
//                               Model model,
//                               RedirectAttributes redirectAttributes) throws CustomerNotFoundException {
//        try {
//            Customer customer = customerService.get(id);
//            List<Country> listCountries = customerService.listAllCountries();
//
//            model.addAttribute("listCountries", listCountries);
//            model.addAttribute("customer", customer);
//            model.addAttribute("pageTitle", "Edit Customer (ID: " + customer.getId() + ")");
//
//            return "/customer/customer_form";
//        } catch (CustomerNotFoundException exception) {
//            redirectAttributes.addFlashAttribute("message", exception.getMessage());
//            return "redirect:/customers";
//        }
//
//    }

    @GetMapping("/customers/{id}/enabled/{status}")
    public String updateEnabledStatus(@PathVariable(name = "id") Integer id,
                                      @PathVariable(name = "status") boolean enabled,
                                      RedirectAttributes redirectAttributes) {
        customerService.updateCustomerEnabledStatus(id, enabled);
        String status = (enabled == true) ? "enabled" : "disabled";
        redirectAttributes.addFlashAttribute("message", "The customer ID " + id + " has been " + status);
        return "redirect:/customers";
    }

//    @GetMapping("/customers/detail/{id}")
//    public String viewCustomerDetails(@PathVariable(name = "id") Integer id,
//                              Model model,
//                              RedirectAttributes redirectAttributes) throws CustomerNotFoundException {
//        try {
//            Customer customer = customerService.get(id);
//            List<Address> listAddresses = addressService.getListAddressByCustomer(customer);
//
//            model.addAttribute("customer", customer);
//            model.addAttribute("listAddresses", listAddresses);
//
//            return "/customer/customer_detail_modal";
//        } catch (CustomerNotFoundException exception) {
//            redirectAttributes.addFlashAttribute("message", exception.getMessage());
//            return "redirect:/customers";
//        }
//    }

//    @GetMapping("/customers/delete/{id}")
//    public String deleteCustomer(@PathVariable(name = "id") Integer id,
//                                Model model,
//                                RedirectAttributes redirectAttributes) throws CustomerNotFoundException {
//        try {
//            customerService.delete(id);
//
//            redirectAttributes.addFlashAttribute("message", "Customer by ID = " + id + " has been deleted");
//        } catch (CustomerNotFoundException exception) {
//            redirectAttributes.addFlashAttribute("message", exception.getMessage());
//        }
//        return "redirect:/customers";
//    }

    @GetMapping("/customers/export/csv")
    public void exportToCSV(HttpServletResponse response) throws IOException {
        List<Customer> listCustomers = customerService.listALl();
        CustomerCSVExporter exporter = new CustomerCSVExporter();
        exporter.export(listCustomers, response);
    }
    
}
