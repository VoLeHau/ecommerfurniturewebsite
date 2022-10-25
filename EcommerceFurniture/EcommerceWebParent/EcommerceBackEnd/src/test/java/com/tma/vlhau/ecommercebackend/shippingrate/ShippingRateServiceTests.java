package com.tma.vlhau.ecommercebackend.shippingrate;


import com.tma.vlhau.ecommercebackend.product.repository.ProductRepository;
import com.tma.vlhau.ecommercebackend.shippingrate.exception.ShippingRateNotFoundException;
import com.tma.vlhau.ecommercebackend.shippingrate.repository.ShippingRateRepository;
import com.tma.vlhau.ecommercebackend.shippingrate.service.ShippingRateService;
import com.tma.vlhau.ecommercecommon.entity.ShippingRate;
import com.tma.vlhau.ecommercecommon.entity.product.Product;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
public class ShippingRateServiceTests {

}
