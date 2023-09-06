package com.profi_shop.services.facade;

import com.profi_shop.dto.OrderDetailsDTO;
import com.profi_shop.dto.OrderItemDTO;
import com.profi_shop.dto.ProductDetailsDTO;
import com.profi_shop.model.Order;
import com.profi_shop.model.OrderItem;
import com.profi_shop.model.Product;
import com.profi_shop.model.ProductVariation;
import com.profi_shop.repositories.ProductVariationRepository;
import com.profi_shop.settings.Text;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderFacade {
    private final ProductVariationRepository productVariationRepository;

    @Autowired
    public OrderFacade(ProductVariationRepository productVariationRepository) {
        this.productVariationRepository = productVariationRepository;
    }

    public OrderItemDTO orderItemToDTO(OrderItem orderItem){
        OrderItemDTO orderItemDTO = new OrderItemDTO();
        orderItemDTO.setId(orderItem.getId());
        orderItemDTO.setProduct(orderItem.getProduct());
        orderItemDTO.setProductVariation(orderItem.getProductVariation());
        orderItemDTO.setQuantity(orderItem.getQuantity());
        orderItemDTO.setAvailableSizes(availableSizesForProduct(orderItem.getProduct()));
        return orderItemDTO;
    }


    private List<ProductVariation> availableSizesForProduct(Product product){
        return productVariationRepository.findByParent(product);
    }

    public OrderDetailsDTO orderToOrderDetails(Order order){
        OrderDetailsDTO orderDetailsDTO = new OrderDetailsDTO();
        orderDetailsDTO.setDate(order.getDate());
        orderDetailsDTO.setFirstname(order.getFirstname());
        orderDetailsDTO.setLastname(order.getLastname());
        orderDetailsDTO.setEmail(order.getEmail());
        orderDetailsDTO.setPhone_number(order.getPhone_number());
        orderDetailsDTO.setStatus(order.getStatus());
        orderDetailsDTO.setStore(order.getStore());
        orderDetailsDTO.setCoupon(order.getCoupon());
        orderDetailsDTO.setShipmentType(order.getShipmentType());
        orderDetailsDTO.setShipment(order.getShipment());
        orderDetailsDTO.setTotalPrice(order.getTotalPrice());
        orderDetailsDTO.setId(order.getId());
        orderDetailsDTO.setOrderItems(mapToOrderDetailsDTOList(order.getOrderItems()));
        return orderDetailsDTO;
    }

    public List<OrderItemDTO> mapToOrderDetailsDTOList(List<OrderItem> orderItems) {
        return orderItems.stream()
                .map(this::orderItemToDTO)
                .collect(Collectors.toList());
    }
}
