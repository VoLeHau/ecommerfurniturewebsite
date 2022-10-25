package com.tma.vlhau.ecommercefrontend.customer;

import com.tma.vlhau.ecommercecommon.entity.Address;
import com.tma.vlhau.ecommercecommon.entity.Country;
import com.tma.vlhau.ecommercecommon.entity.Customer;
import com.tma.vlhau.ecommercecommon.entity.User;
import com.tma.vlhau.ecommercefrontend.FileUploadUtil;
import com.tma.vlhau.ecommercefrontend.Utility;
import com.tma.vlhau.ecommercefrontend.order.OrderRepository;
import com.tma.vlhau.ecommercefrontend.security.CustomerUserDetails;
import com.tma.vlhau.ecommercefrontend.security.oauth.CustomerOAuth2User;
import com.tma.vlhau.ecommercefrontend.setting.CountryRepository;
import com.tma.vlhau.ecommercefrontend.setting.EmailSettingBag;
import com.tma.vlhau.ecommercefrontend.setting.SettingService;
import com.tma.vlhau.ecommercefrontend.shoppingcart.CartItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Objects;

@Controller
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private SettingService settingService;
    @Autowired private CartItemRepository cartItemRepository;
    @Autowired
    private CountryRepository countryRepository;

    @GetMapping("/register")
    public String showRegisterForm(Model model) {

        model.addAttribute("pageTitle", "Customer Registration");
        model.addAttribute("customer", new Customer());
        model.addAttribute("listCountries",countryRepository.findAll());

        return "/register/register_form";
    }

    @PostMapping("/create_customer")
    public String createCustomer(@Valid Customer customer, BindingResult bindingResult, Model model, HttpServletRequest request,
                                 @RequestParam(value = "ward") int wardId,
                                 @RequestParam(value = "addressDetail") String addressDetail, @RequestParam("image") MultipartFile multipartFile) throws MessagingException, UnsupportedEncodingException, IOException {


        if (bindingResult.hasErrors()) {
            model.addAttribute("listCountries",countryRepository.findAll());
            model.addAttribute("pageTitle", "Customer Registration");
            System.out.println("aaa");

            return "/register/register_form";
        }
        if (!multipartFile.isEmpty()){
            String fileName = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));


            customer.setPhotos(fileName);

            Customer savedCustomer = customerService.registerCustomer(customer,wardId,addressDetail);

            String uploadDir = "../" + FileUploadUtil.CUSTOMER_DIR_NAME + savedCustomer.getId() + "/";

            FileUploadUtil.cleanDirectory(uploadDir);
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
        } else {

            if (customer.getPhotos().isEmpty()) customer.setPhotos(null);
            customerService.registerCustomer(customer,wardId,addressDetail);
        }
//        sendVerificationEmail(request, customer);
        model.addAttribute("pageTitle", "Registration Succeeded!");

        return "/register/register_success";
    }

    private void sendVerificationEmail(HttpServletRequest request, Customer customer) throws MessagingException, UnsupportedEncodingException {
        EmailSettingBag emailSettings = settingService.getEmailSettings();
        JavaMailSenderImpl mailSender = Utility.prepareMailSender(emailSettings);

        String toAddress = customer.getEmail();
        String subject = emailSettings.getCustomerVerifySubject();
        String content = emailSettings.getCustomerVerifyContent();

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(emailSettings.getFromAddress(), emailSettings.getSenderName());
        helper.setTo(toAddress);
        helper.setSubject(subject);

        content = content.replace("[[name]]", customer.getFullName());

        String verifyURL = "http://localhost:8000" + Utility.getSiteURL(request) + "/verify?code=" + customer.getVerificationCode();
        
        content = content.replace("[[URL]]", verifyURL);

        helper.setText(content, true);
        mailSender.send(message);

        System.out.println("to Address: " + toAddress);
        System.out.println("verify URL: " + verifyURL);
    }

    @GetMapping("/verify")
    public String verifyAccount(@Param("code") String code) {
        boolean verified = customerService.verify(code);

        return "register/" + (verified ? "verify_success" : "verify_fail");
    }

    @GetMapping("/account_details")
    public String viewAccountDetails(Model model, HttpServletRequest request) {
        String email = getEmailOfAuthenticatedCustomer(request);
        Customer customer = customerService.getCustomerByEmail(email);
        int amountProduct = cartItemRepository.getamountProductByCustomer(customer);

        model.addAttribute("amountProduct",amountProduct);
        model.addAttribute("customer", customer);



        return "customer/account_form";
    }

    private String getEmailOfAuthenticatedCustomer(HttpServletRequest request) {
        Object principal = request.getUserPrincipal();
        String customerEmail = null;

        if (principal instanceof UsernamePasswordAuthenticationToken
            || principal instanceof RememberMeAuthenticationToken) {
            customerEmail = request.getUserPrincipal().getName();
        } else if (principal instanceof OAuth2AuthenticationToken){
            OAuth2AuthenticationToken oAuth2AuthenticationToken = (OAuth2AuthenticationToken) principal;
            CustomerOAuth2User oAuth2User = (CustomerOAuth2User) oAuth2AuthenticationToken.getPrincipal();
            customerEmail = oAuth2User.getEmail();
        }

        return customerEmail;
    }

    @PostMapping("/update_account_details")
    @Transactional
    public String updateAccountDetails(@Valid Customer customer, BindingResult bindingResult, RedirectAttributes redirectAttributes, HttpServletRequest request, Model model) {

        if(customer.getPassword() != ""){
            if (bindingResult.hasErrors()) {
                int amountProduct = cartItemRepository.getamountProductByCustomer(customer);
                model.addAttribute("amountProduct",amountProduct);
                model.addAttribute("customer", customer);
                return "customer/account_form";
            }
        }

        customerService.update(customer);

        redirectAttributes.addFlashAttribute("message", "Your account details have been updated");

        updateNameForAuthenticatedCustomer(customer ,request);

        String redirectOption = request.getParameter("redirect");
        String redirectURL = "redirect:/account_details";

        if ("address_book".equals(redirectOption)) {
            redirectURL = "redirect:/address_book";
        } else if ("cart".equals(redirectOption)) {
            redirectURL = "redirect:/cart";
        } else if ("checkout".equals(redirectOption)) {
            redirectURL = "redirect:/address_book?redirect=checkout";
        }

        return redirectURL;
    }

    private void updateNameForAuthenticatedCustomer(Customer customer, HttpServletRequest request) {
        Object principal = request.getUserPrincipal();

        if (principal instanceof UsernamePasswordAuthenticationToken
                || principal instanceof RememberMeAuthenticationToken) {
            CustomerUserDetails customerUserDetails = getCustomerUserDetailsObject(principal);
            Customer customerAuthenticated = customerUserDetails.getCustomer();
            customerAuthenticated.setFirstName(customer.getFirstName());
            customerAuthenticated.setLastName(customer.getLastName());
        } else if (principal instanceof OAuth2AuthenticationToken){
            OAuth2AuthenticationToken oAuth2AuthenticationToken = (OAuth2AuthenticationToken) principal;
            CustomerOAuth2User oAuth2User = (CustomerOAuth2User) oAuth2AuthenticationToken.getPrincipal();
            oAuth2User.setFullName(customer.getFullName());
        }
    }

    private CustomerUserDetails getCustomerUserDetailsObject(Object principal) {
        CustomerUserDetails customerUserDetails = null;

        if (principal instanceof UsernamePasswordAuthenticationToken) {
            UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) principal;
            customerUserDetails = (CustomerUserDetails) token.getPrincipal();
        } else if (principal instanceof RememberMeAuthenticationToken) {
            RememberMeAuthenticationToken token = (RememberMeAuthenticationToken) principal;
            customerUserDetails = (CustomerUserDetails) token.getPrincipal();
        }

        return customerUserDetails;
    }

}
