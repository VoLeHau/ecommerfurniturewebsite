package com.tma.vlhau.ecommercefrontend.checkout;

import com.tma.vlhau.ecommercecommon.entity.CartItem;
import com.tma.vlhau.ecommercecommon.entity.ShippingRate;
import com.tma.vlhau.ecommercecommon.entity.product.Product;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CheckoutService {

    private static final float DIM_DIVISOR =  10000;

    public CheckoutInfo prepareCheckout(List<CartItem> cartItems, ShippingRate shippingRate) {
        CheckoutInfo checkoutInfo = new CheckoutInfo();

        float productCost = calculateProductCost(cartItems);
        float productTotal = calculateProductTotal(cartItems);
        float shippingCostTotal = calculateShippingCost(cartItems, shippingRate);
        float paymentTotal = productTotal + shippingCostTotal;

        checkoutInfo.setProductCost(productCost);
        checkoutInfo.setProductTotal(productTotal);
        checkoutInfo.setDeliverDays(shippingRate.getDays());
        checkoutInfo.setCodSupported(shippingRate.isCodSupported());
        checkoutInfo.setShippingCostTotal(shippingCostTotal);
        checkoutInfo.setPaymentTotal(paymentTotal);

        return checkoutInfo;
    }

    private float calculateShippingCost(List<CartItem> cartItems, ShippingRate shippingRate) {
        float shippingCostTotal = 0.0f;

        for (CartItem item : cartItems) {
            Product product = item.getProduct();
            float productLength = product.getLength();
            float productWidth = product.getWidth();
            float productHeight = product.getHeight();
            
            float dimWeight = (productLength * 100 * productWidth * 100 * productHeight * 100 ) / DIM_DIVISOR;

            float productWeight = product.getWeight();
            float finalWeight = productWeight > dimWeight ? productWeight : dimWeight;
            
            float shippingCost = finalWeight * item.getQuantity() * shippingRate.getRate();

            item.setShippingCost(shippingCost);

            shippingCostTotal += shippingCost;
        }

        return shippingCostTotal;
    }

    private float calculateProductTotal(List<CartItem> cartItems) {
        float total = 0.0f;

        for (CartItem item : cartItems) {
            total += item.getSubtotal();
        }

        return total;
    }

    private float calculateProductCost(List<CartItem> cartItems) {
        float cost = 0.0f;

        for (CartItem item : cartItems) {
            cost += item.getQuantity() * item.getProduct().getCost();
        }

        return cost;
    }

}
