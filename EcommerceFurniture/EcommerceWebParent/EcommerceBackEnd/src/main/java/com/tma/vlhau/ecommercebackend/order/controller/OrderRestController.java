package com.tma.vlhau.ecommercebackend.order.controller;

import com.tma.vlhau.ecommercebackend.order.repository.OrderTrackRepository;
import com.tma.vlhau.ecommercebackend.order.service.OrderService;
import com.tma.vlhau.ecommercecommon.entity.order.Order;
import com.tma.vlhau.ecommercecommon.entity.order.OrderStatus;
import com.tma.vlhau.ecommercecommon.entity.order.OrderTrack;
import com.tma.vlhau.ecommercecommon.exception.OrderNotFoundException;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Date.*;
import java.util.List;


@RestController
public class OrderRestController {

	@Autowired
	private OrderService orderService;

	@Autowired
	private OrderTrackRepository orderTrackRepository;

	@PostMapping("/orders_shipper/update/{id}/{status}")
	public Response updateOrderStatus(@PathVariable("id") Integer orderId, @PathVariable("status") String status) {
		orderService.updateStatus(orderId, status);
		return new Response(orderId, status);
	}

	@PostMapping("/addOrders")
	public String addNewStatus(Integer orderId, String rowId, Integer nextCount, String emptyLineId, String trackNoteId, String currentDateTime) throws OrderNotFoundException  {

		List<OrderTrack> listOrderTrack = orderTrackRepository.getOrderTrackByOrder(orderService.get(orderId));

		List<OrderStatus> listOrderStatus = new ArrayList<>();
		listOrderStatus.add(OrderStatus.NEW);
		listOrderStatus.add(OrderStatus.CANCELLED);
		listOrderStatus.add(OrderStatus.PROCESSING);
		listOrderStatus.add(OrderStatus.PACKAGED);
		listOrderStatus.add(OrderStatus.PICKED);
		listOrderStatus.add(OrderStatus.SHIPPING);
		listOrderStatus.add(OrderStatus.DELIVERED);
		listOrderStatus.add(OrderStatus.RETURN_REQUESTED);
		listOrderStatus.add(OrderStatus.RETURNED);
		listOrderStatus.add(OrderStatus.PAID);
		listOrderStatus.add(OrderStatus.REFUNDED);

		List<OrderStatus> list = new ArrayList<>();

		if(listOrderTrack.get(listOrderTrack.size()-1).getStatus().equals(OrderStatus.PROCESSING)){
			list.add(OrderStatus.PACKAGED);
		}
		else if(listOrderTrack.get(listOrderTrack.size()-1).getStatus().equals(OrderStatus.PACKAGED)){
			list.add(OrderStatus.PICKED);
		}
		else if(listOrderTrack.get(listOrderTrack.size()-1).getStatus().equals(OrderStatus.PICKED)){
			list.add(OrderStatus.SHIPPING);
		}
		else if(listOrderTrack.get(listOrderTrack.size()-1).getStatus().equals(OrderStatus.SHIPPING)){
			list.add(OrderStatus.DELIVERED);
		}
		else if(listOrderTrack.get(listOrderTrack.size()-1).getStatus().equals(OrderStatus.RETURN_REQUESTED)){
			list.add(OrderStatus.RETURNED);
		}
		else if(listOrderTrack.get(listOrderTrack.size()-1).getStatus().equals(OrderStatus.RETURNED)){
			list.add(OrderStatus.REFUNDED);
		}
		else if(listOrderTrack.get(listOrderTrack.size()-1).getStatus().equals(OrderStatus.CANCELLED)){
			list.add(OrderStatus.REFUNDED);
		}

		String year = currentDateTime.substring(0,4);
		String month = currentDateTime.substring(5,7);
		String day = currentDateTime.substring(8,10);
		String time = currentDateTime.substring(11);

		String date = day + "-" + month + "-" + year + " " + time;

		String htmlCode =
			"<div class='row border p-3 mx-2' id='"+rowId+"'>" +
				"<input type='hidden' name='trackId' value='0' class='hiddenTrackId' />"+
				"<div class='col-1 text-center'>" +
					"<div class='divCountTrack'>"+nextCount+"</div>" +
					"<div class='mt-1'><a class='fas fa-trash icon-purple fa-2x linkRemoveTrack' href='' rowNumber='"+nextCount+"'></a></div>"+
				"</div>" +
				"<div class='col-11'>" +
					"<div class='form-group row'>" +
						"<label class='col-form-label col-sm-3'>Time:</label>" +
						"<div class='col-sm-9'>" +
							"<input readonly class='form-control' name='trackDate' value='"+date+"'/>" +
						"</div>" +
					"</div>" +
					"<div class='form-group row'>" +
						"<label class='col-form-label col-sm-3'>Status:</label>" +
						"<div class='col-sm-9'>";
						for(OrderStatus status : list){
							htmlCode += "<input  name='trackStatus' class='form-control dropDownStatus' style='max-width: 250px'" +
										"rowNumber='"+nextCount+"' readonly='' value='"+status+"'/>" +
										"</div>" +
									"</div>" +

									"<div class='form-group row'>" +
										"<label class='col-form-label col-sm-3'>Notes:</label>" +
										"<div class='col-sm-9'>" +
											"<textarea rows='2' cols='10' class='form-control' name='trackNotes' id='"+trackNoteId+"' readonly>"+status.defaultDescription()+"</textarea>" +
										"</div>" +
									"</div>";
						}

					htmlCode +=
				"</div>" +
			"</div>" +
		"<div id='"+emptyLineId+"' class='row'>&nbsp;</div>";

		return htmlCode;
	}
}

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
class Response {
	private Integer orderId;
	private String status;


}