package com.tma.vlhau.ecommercefrontend.checkout;

import com.tma.vlhau.ecommercecommon.entity.Address;
import com.tma.vlhau.ecommercecommon.entity.CartItem;
import com.tma.vlhau.ecommercecommon.entity.Customer;
import com.tma.vlhau.ecommercecommon.entity.ShippingRate;
import com.tma.vlhau.ecommercecommon.entity.order.Order;
import com.tma.vlhau.ecommercecommon.entity.order.PaymentMethod;
import com.tma.vlhau.ecommercecommon.exception.PayPalApiException;
import com.tma.vlhau.ecommercefrontend.Utility;
import com.tma.vlhau.ecommercefrontend.address.AddressService;
import com.tma.vlhau.ecommercefrontend.checkout.paypal.PayPalService;
import com.tma.vlhau.ecommercefrontend.customer.CustomerService;
import com.tma.vlhau.ecommercefrontend.order.OrderRepository;
import com.tma.vlhau.ecommercefrontend.order.OrderService;
import com.tma.vlhau.ecommercefrontend.setting.CurrencySettingBag;
import com.tma.vlhau.ecommercefrontend.setting.EmailSettingBag;
import com.tma.vlhau.ecommercefrontend.setting.PaymentSettingBag;
import com.tma.vlhau.ecommercefrontend.setting.SettingService;
import com.tma.vlhau.ecommercefrontend.shipping.ShippingRateService;
import com.tma.vlhau.ecommercefrontend.shoppingcart.CartItemRepository;
import com.tma.vlhau.ecommercefrontend.shoppingcart.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

@Controller
public class CheckoutController {

    @Autowired private CheckoutService checkoutService;
    @Autowired private CustomerService customerService;
    @Autowired private AddressService addressService;
    @Autowired private ShoppingCartService shoppingCartService;
    @Autowired private ShippingRateService shippingRateService;
    @Autowired private OrderService orderService;
    @Autowired private SettingService settingService;
    @Autowired private PayPalService payPalService;
    @Autowired private CartItemRepository cartItemRepository;
    @GetMapping("/checkout")
    public String showCheckoutPage(Model model, HttpServletRequest request) {
        Customer customer = getAuthenticatedCustomer(request);

        Address defaultAddress = addressService.getDefaultAddress(customer);
        ShippingRate shippingRate = null;

        String fullAddress = null;

        if (defaultAddress != null) {
            shippingRate = shippingRateService.getShippingRateForAddress(defaultAddress);

            fullAddress = defaultAddress.getAddressDetail() + ", " + defaultAddress.getWard().getName() +
                    ", " + defaultAddress.getWard().getDistrict().getName() + " , " + defaultAddress.getWard().getDistrict().getCity().getName() +
                    ", " + defaultAddress.getWard().getDistrict().getCity().getCountry().getName();

        }

        if (shippingRate == null) {
            return "redirect:/cart";
        }

        List<CartItem> cartItems = shoppingCartService.listCartItem(customer);
        CheckoutInfo checkoutInfo = checkoutService.prepareCheckout(cartItems, shippingRate);

        String currencyCode = settingService.getCurrencyCode();
        PaymentSettingBag paymentSettings = settingService.getPaymentSettings();
        String paypalClientId = paymentSettings.getClientID();
        String paypalClientSecret = paymentSettings.getClientSecret();

        int amountProduct = cartItemRepository.getamountProductByCustomer(customer);

        model.addAttribute("amountProduct",amountProduct);
        model.addAttribute("paypalClientId", paypalClientId);
        model.addAttribute("paypalClientSecret", paypalClientSecret);
        model.addAttribute("customer", customer);
        model.addAttribute("currencyCode", currencyCode);
        model.addAttribute("checkoutInfo", checkoutInfo);
        model.addAttribute("cartItems", cartItems);
        model.addAttribute("fullAddress",fullAddress);
        return "checkout/checkout";
    }

    private Customer getAuthenticatedCustomer(HttpServletRequest request) {
        String email = Utility.getEmailOfAuthenticatedCustomer(request);

        return customerService.getCustomerByEmail(email);
    }

    @PostMapping("/place_order")
    public String placeOrder(HttpServletRequest request) throws MessagingException, UnsupportedEncodingException {
        String paymentType = request.getParameter("paymentMethod");
        PaymentMethod paymentMethod = PaymentMethod.valueOf(paymentType);

        Customer customer = getAuthenticatedCustomer(request);

        Address defaultAddress = addressService.getDefaultAddress(customer);
        ShippingRate shippingRate = null;


        if (defaultAddress != null) {
            shippingRate = shippingRateService.getShippingRateForAddress(defaultAddress);
        }

        List<CartItem> cartItems = shoppingCartService.listCartItem(customer);
        CheckoutInfo checkoutInfo = checkoutService.prepareCheckout(cartItems, shippingRate);

        Order createdOrder = orderService.createOrder(customer, defaultAddress, cartItems, paymentMethod, checkoutInfo);
        shoppingCartService.deleteByCustomer(customer);
        sendOrderConfirmationEmail(request, createdOrder);

        return "checkout/order_completed";
    }

    private void sendOrderConfirmationEmail(HttpServletRequest request, Order order) throws MessagingException, UnsupportedEncodingException {
        EmailSettingBag emailSettingBag = settingService.getEmailSettings();
        JavaMailSenderImpl mailSender = Utility.prepareMailSender(emailSettingBag);
        mailSender.setDefaultEncoding("utf-8");
        String toAddress = order.getCustomer().getEmail();
        String subject = emailSettingBag.getOrderConfirmationSubject();
        String content = emailSettingBag.getOrderConfirmationContent();

        subject = subject.replace("[[orderId]]", String.valueOf(order.getId()));

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(emailSettingBag.getFromAddress(), emailSettingBag.getSenderName());
        helper.setTo(toAddress);
        helper.setSubject(subject);

        DateFormat dateFormatter = new SimpleDateFormat("HH:mm:ss E, dd MMM yyyy");
        String orderTime = dateFormatter.format(order.getOrderTime());

        CurrencySettingBag currencySettingBag = settingService.getCurrencySettings();
        String totalAmount = Utility.formatCurrency(order.getSubtotal() + order.getShippingCost(), currencySettingBag);

        content = content.replace("[[name]]", order.getCustomer().getFullName());
        content = content.replace("[[orderId]]", String.valueOf(order.getId()));
        content = content.replace("[[orderTime]]", orderTime);
        content = content.replace("[[shippingAddress]]", order.getShippingAddress());
        content = content.replace("[[total]]", totalAmount);
        content = content.replace("[[paymentMethod]]", order.getPaymentMethod().toString());
        content = content.replace("[[orderLink]]", "http://localhost:8000/EcommerceFurniture/orders");
        helper.setText(content, true);
        mailSender.send(message);

    }

    @PostMapping("/process_paypal_order")
    public String processPayPalOrder(HttpServletRequest request, Model model) throws UnsupportedEncodingException, MessagingException {
        String orderId = request.getParameter("orderId");
        String pageTitle = "Checkout Failure";
        String message = null;
        try {
            if (payPalService.validateOrder(orderId)) {
                return placeOrder(request);
            } else {
                pageTitle = "Checkout Failure";
                message = "ERROR: Transaction could not be completed because order information is invalid";
            }
        } catch (PayPalApiException e) {
            message = "ERROR: Transaction failed due to error: " + e.getMessage();
        }

        model.addAttribute("pageTitle", pageTitle);
        model.addAttribute("message", message);

        return "message";
    }

}
