package com.tma.vlhau.ecommercebackend.order.controller;
import com.tma.vlhau.ecommercebackend.order.repository.OrderTrackRepository;
import com.tma.vlhau.ecommercebackend.order.service.OrderService;
import com.tma.vlhau.ecommercebackend.paging.PagingAndSortingHelper;
import com.tma.vlhau.ecommercebackend.paging.PagingAndSortingParam;
import com.tma.vlhau.ecommercebackend.security.UserDetailsImp;
import com.tma.vlhau.ecommercebackend.setting.service.SettingService;
import com.tma.vlhau.ecommercecommon.entity.Country;
import com.tma.vlhau.ecommercecommon.entity.order.Order;
import com.tma.vlhau.ecommercecommon.entity.order.OrderDetail;
import com.tma.vlhau.ecommercecommon.entity.order.OrderStatus;
import com.tma.vlhau.ecommercecommon.entity.order.OrderTrack;
import com.tma.vlhau.ecommercecommon.entity.product.Product;
import com.tma.vlhau.ecommercecommon.entity.setting.Setting;
import com.tma.vlhau.ecommercecommon.exception.OrderNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Controller
public class OrderController {
	private String defaultRedirectURL = "redirect:/orders/page/1?sortField=orderTime&sortDir=desc";
	
	@Autowired private OrderService orderService;
	@Autowired private SettingService settingService;

	@Autowired private OrderTrackRepository orderTrackRepository;

	@GetMapping("/orders")
	public String listFirstPage() {
		return defaultRedirectURL;
	}
	
	@GetMapping("/orders/page/{pageNum}")
	public String listByPage(
			@PagingAndSortingParam(listName = "listOrders", moduleURL = "/orders", module="orders") PagingAndSortingHelper helper,
			@PathVariable(name = "pageNum") int pageNum,
			HttpServletRequest request,
			@AuthenticationPrincipal UserDetailsImp loggedUser,Model model) {

		orderService.listByPage(pageNum, helper);
		loadCurrencySetting(request);

		if (!loggedUser.hasRole("Admin") && !loggedUser.hasRole("Sales") && loggedUser.hasRole("Shipper")) {
			return "orders/orders_shipper";
		}

		int idOrderPaidCancel = 0;
		for(Order order : orderService.getAll()){

			int statusPaid = 0;
			int statusCancel = 0;
			if(order.getOrderTracks().get(0).getStatus().equals(OrderStatus.PAID)){
				statusPaid = 1;
			}
			if(order.getOrderTracks().get(order.getOrderTracks().size()-1).getStatus().equals(OrderStatus.CANCELLED)){
				statusCancel = 1;
			}

			if((statusPaid==1 && statusCancel ==1)){
				idOrderPaidCancel = order.getId();
			}

		}
		model.addAttribute("statusNew",OrderStatus.NEW);
		model.addAttribute("statusPaid",OrderStatus.PAID);
		model.addAttribute("statusProcessing",OrderStatus.PROCESSING);
		model.addAttribute("statusCancel",OrderStatus.CANCELLED);
		model.addAttribute("statusRefunded",OrderStatus.REFUNDED);
		model.addAttribute("statusDelivered",OrderStatus.DELIVERED);
		model.addAttribute("idOrderPaidCancel",idOrderPaidCancel);
		return "orders/orders";
	}
	
	private void loadCurrencySetting(HttpServletRequest request) {
		List<Setting> currencySettings = settingService.getCurrencySettings();
		
		for (Setting setting : currencySettings) {
			request.setAttribute(setting.getKey(), setting.getValue());
		}	
	}	
	
	
	@GetMapping("/orders/detail/{id}")
	public String viewOrderDetails(@PathVariable("id") Integer id, Model model, 
			RedirectAttributes ra, HttpServletRequest request,
			@AuthenticationPrincipal UserDetailsImp loggedUser) {
		try {
			Order order = orderService.get(id);
			loadCurrencySetting(request);			
			
			boolean isVisibleForAdminOrSales = false;
			
			if (loggedUser.hasRole("Admin") || loggedUser.hasRole("Sales")) {
				isVisibleForAdminOrSales = true;
			}
			
			model.addAttribute("isVisibleForAdminOrSales", isVisibleForAdminOrSales);
			model.addAttribute("order", order);
			List<OrderTrack> listOrderTrack = orderTrackRepository.getOrderTrackByOrder(order);
			OrderTrack orderTrack = listOrderTrack.get(listOrderTrack.size()-1);
			model.addAttribute("orderStatus",orderTrack.getStatus());
			
			return "orders/order_details_modal";
			
		} catch (OrderNotFoundException ex) {
			ra.addFlashAttribute("message", ex.getMessage());
			return defaultRedirectURL;
		}
		
	}

	@GetMapping("/orders/delete/{id}")
	public String deleteOrder(@PathVariable("id") Integer id, Model model, RedirectAttributes ra) {
		try {
			orderService.delete(id);;
			ra.addFlashAttribute("message", "The order ID " + id + " has been deleted.");
		} catch (OrderNotFoundException ex) {
			ra.addFlashAttribute("message", ex.getMessage());
		}
		
		return defaultRedirectURL;
	}

	@GetMapping("/orders/confirm/{id}")
	public String confirmOrder(@PathVariable("id") Integer id, RedirectAttributes ra) {

		try {
			Order order = orderService.get(id);
			OrderTrack trackRecord = new OrderTrack();

			trackRecord.setOrder(order);
			trackRecord.setStatus(OrderStatus.PROCESSING);
			trackRecord.setNotes(OrderStatus.PROCESSING.defaultDescription());
			DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");
			Date date = new Date();
			String da = dateFormatter.format(date);
			date = dateFormatter.parse(da);
			trackRecord.setUpdatedTime(date);

			orderTrackRepository.save(trackRecord);

			ra.addFlashAttribute("message", "The order has been confirmed.");
		} catch (OrderNotFoundException ex) {
			ra.addFlashAttribute("message", ex.getMessage());
		}
		catch (ParseException e){

		}


		return defaultRedirectURL;
	}



	@GetMapping("/orders/cancel/{id}")
	public String cancelOrder(@PathVariable("id") Integer id, RedirectAttributes ra) {
		try {
			Order order = orderService.get(id);
			OrderTrack trackRecord = new OrderTrack();

			trackRecord.setOrder(order);
			trackRecord.setStatus(OrderStatus.CANCELLED);
			trackRecord.setNotes(OrderStatus.CANCELLED.defaultDescription());
			DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");
			Date date = new Date();
			String da = dateFormatter.format(date);
			date = dateFormatter.parse(da);
			trackRecord.setUpdatedTime(date);
			orderTrackRepository.save(trackRecord);

			ra.addFlashAttribute("message", "The order has been canceled.");
		} catch (OrderNotFoundException ex) {
			ra.addFlashAttribute("message", ex.getMessage());
		}
		catch (ParseException e){

		}
		return defaultRedirectURL;
	}
	

	@GetMapping("/orders/edit/{id}")
	public String editOrder(@PathVariable("id") Integer id, Model model, RedirectAttributes ra,
			HttpServletRequest request) {
		try {
			Order order = orderService.get(id);;

			model.addAttribute("pageTitle", "Edit Order (ID: " + id + ")");
			model.addAttribute("order", order);

			return "orders/order_form";
			
		} catch (OrderNotFoundException ex) {
			ra.addFlashAttribute("message", ex.getMessage());
			return defaultRedirectURL;
		}
		
	}	
	
	@PostMapping("/orders/save")
	public String saveOrder(@RequestParam(value = "orderId") Integer orderId, HttpServletRequest request, RedirectAttributes ra) throws OrderNotFoundException{
		Order order = orderService.get(orderId);
//		updateProductDetails(order, request);
		updateOrderTracks(order, request);

		orderService.save(order);

		ra.addFlashAttribute("message", "The order ID " + order.getId() + " has been updated status successfully");

		return defaultRedirectURL;
	}

	private void updateOrderTracks(Order order, HttpServletRequest request) {

		String[] trackIds = request.getParameterValues("trackId");
		String[] trackStatuses = request.getParameterValues("trackStatus");
		String[] trackDates = request.getParameterValues("trackDate");
		String[] trackNotes = request.getParameterValues("trackNotes");
		
		List<OrderTrack> orderTracks = order.getOrderTracks();
		DateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");


		for (int i = 0; i < trackIds.length; i++) {
			OrderTrack trackRecord = new OrderTrack();
			
			Integer trackId = Integer.parseInt(trackIds[i]);
			if (trackId > 0) {
				trackRecord.setId(trackId);
			}
			
			trackRecord.setOrder(order);
			trackRecord.setStatus(OrderStatus.valueOf(trackStatuses[i]));
			trackRecord.setNotes(trackNotes[i]);
			
			try {
				trackRecord.setUpdatedTime(dateFormatter.parse(trackDates[i]));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
			orderTracks.add(trackRecord);
		}
	}

	private void updateProductDetails(Order order, HttpServletRequest request) {
		String[] detailIds = request.getParameterValues("detailId");
		String[] productIds = request.getParameterValues("productId");
		String[] productPrices = request.getParameterValues("productPrice");
		String[] productDetailCosts = request.getParameterValues("productDetailCost");
		String[] quantities = request.getParameterValues("quantity");
		String[] productSubtotals = request.getParameterValues("productSubtotal");
		String[] productShipCosts = request.getParameterValues("productShipCost");
		
		Set<OrderDetail> orderDetails = order.getOrderDetails();
		
		for (int i = 0; i < detailIds.length; i++) {
			
			OrderDetail orderDetail = new OrderDetail();
			Integer detailId = Integer.parseInt(detailIds[i]);
			if (detailId > 0) {
				orderDetail.setId(detailId);
			}
			
			orderDetail.setOrder(order);
			orderDetail.setProduct(new Product(Integer.parseInt(productIds[i])));
			orderDetail.setProductCost(Float.parseFloat(productDetailCosts[i]));
			orderDetail.setSubtotal(Float.parseFloat(productSubtotals[i]));
			orderDetail.setShippingCost(Float.parseFloat(productShipCosts[i]));
			orderDetail.setQuantity(Integer.parseInt(quantities[i]));
			orderDetail.setUnitPrice(Float.parseFloat(productPrices[i]));
			
			orderDetails.add(orderDetail);
			
		}
		
	}


}