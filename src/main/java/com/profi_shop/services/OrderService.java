package com.profi_shop.services;

import com.profi_shop.exceptions.*;
import com.profi_shop.model.*;
import com.profi_shop.model.enums.OrderStatus;
import com.profi_shop.model.enums.ShipmentType;
import com.profi_shop.model.requests.OrderRequest;
import com.profi_shop.model.requests.OrderUpdateRequest;
import com.profi_shop.repositories.*;
import com.profi_shop.validations.Validator;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductVariationRepository productVariationRepository;
    private final ProductRepository productRepository;
    private final ShipmentRepository shipmentRepository;
    private final StoreRepository storeRepository;
    private final NotificationService notificationService;
    private final StoreHouseRepository storeHouseRepository;
    private final UserRepository userRepository;
    private final CartService cartService;

    @Autowired
    public OrderService(OrderRepository orderRepository, OrderItemRepository orderItemRepository, ProductVariationRepository productVariationRepository, ProductRepository productRepository, ShipmentRepository shipmentRepository, StoreRepository storeRepository, NotificationService notificationService, StoreHouseRepository storeHouseRepository, UserRepository userRepository, CartService cartService) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.productVariationRepository = productVariationRepository;
        this.productRepository = productRepository;
        this.shipmentRepository = shipmentRepository;
        this.storeRepository = storeRepository;
        this.notificationService = notificationService;
        this.storeHouseRepository = storeHouseRepository;
        this.userRepository = userRepository;
        this.cartService = cartService;
    }

    public Page<Order> getFilteredOrders(Integer status, Integer sort, Integer page) {
        Pageable pageable;
        if (sort == 0) pageable = PageRequest.of(page, 10, Sort.by(Sort.Direction.ASC, "totalPrice"));
        else if (sort == 1) pageable = PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "totalPrice"));
        else if (sort == 2) pageable = PageRequest.of(page, 10, Sort.by(Sort.Direction.ASC, "date"));
        else pageable = PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "date"));

        return switch (status) {
            case 1 -> orderRepository.findByStatus(OrderStatus.REQUEST, pageable);
            case 2 -> orderRepository.findByStatus(OrderStatus.PAID, pageable);
            case 3 -> orderRepository.findByStatus(OrderStatus.DELIVERING, pageable);
            case 4 -> orderRepository.findByStatus(OrderStatus.FINISHED, pageable);
            default -> orderRepository.findAll(pageable);
        };
    }

    public void createOrderByUnknown(OrderRequest orderRequest, HttpServletRequest request) throws InvalidDataException, NotEnoughException, ExistException {
        Cart cart = cartService.getCartByRequestCookies(request);
        Order order = createBaseOrder(orderRequest, cart);
        orderRepository.save(order);
        notificationService.createLocalNewOrderNotification(order.getStore().getAdmin(), order.getId());
    }

    public void createOrderByUsername(OrderRequest orderRequest, String username) throws InvalidDataException, NotEnoughException, ExistException {
        User user = getUserByUsername(username);
        Cart cart = cartService.getCartByUsername(username);
        Order order = createBaseOrder(orderRequest, cart);
        order.setUser(user);
        orderRepository.save(order);
        notificationService.createLocalNewOrderNotification(order.getStore().getAdmin(), order.getId());

    }

    private Order createBaseOrder(OrderRequest orderRequest, Cart cart) throws InvalidDataException, NotEnoughException, ExistException {
        Order order = new Order();

        int totalPrice = 0;
        if (cart.getCartItems().size() == 0) {
            throw new InvalidDataException(InvalidDataException.EMPTY_CART);
        }
        order.setEmail(orderRequest.getEmail());
        order.setFirstname(orderRequest.getFirstname());
        order.setLastname(orderRequest.getLastname());
        order.setPhone_number(Validator.validNumber(orderRequest.getPhone_number()));
        if (orderRequest.isToShip()) {
            Shipment shipment = shipmentRepository.findByTownAndState(orderRequest.getTown(), orderRequest.getState()).orElseThrow(() -> new SearchException(SearchException.SHIPMENT_NOT_FOUND));
            order.setShipment(shipment);
            totalPrice += shipment.getCost();
            order.setShipmentType(ShipmentType.SHIPMENT);
        } else {
            order.setShipmentType(ShipmentType.SELF);
        }
        order.setStatus(OrderStatus.REQUEST);

        for (CartItem cartItem : cart.getCartItems()) {
            if (cartItem.getProductVariation() != null) {
                int count = countOfProductVariationInStore(cartItem.getProductVariation());
                if (count < cartItem.getQuantity())
                    throw new NotEnoughException(cartItem.getProduct().getName() + " ( " + cartItem.getProductVariation().getSize() + " ) ", count);
            } else {
                int count = countOfProductInStore(cartItem.getProduct());
                if (count < cartItem.getQuantity())
                    throw new NotEnoughException(cartItem.getProduct().getName(), count);
            }
            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setProductVariation(cartItem.getProductVariation());
            orderItem.setPrice(cartItem.getAmount());
            orderItemRepository.save(orderItem);
            order.addOrderItem(orderItem);
        }
        totalPrice += cart.cartAmount();
        order.setTotalPrice(totalPrice);
        order.setStore(chooseStoreToOrder(cart.getCartItems()));
        return orderRepository.save(order);
    }

    private User getUserByUsername(String username) {
        return userRepository.findUserByUsername(username).orElseThrow(() -> new SearchException(SearchException.USER_NOT_FOUND));
    }

    private Store chooseStoreToOrder(List<CartItem> cartItems) {
        List<StoreHouse> storeHouses = new ArrayList<>();
        if (cartItems.get(0).getProductVariation() == null) {
            List<ProductVariation> productVariations = productVariationRepository.findByParent(cartItems.get(0).getProduct());
            for (ProductVariation pv : productVariations) {
                storeHouses.addAll(storeHouseRepository.findStoreHouseByProduct(pv));
            }
        } else {
            storeHouses.addAll(storeHouseRepository.findStoreHouseByProduct(cartItems.get(0).getProductVariation()));
        }
        int max = Integer.MIN_VALUE;
        Store maxStore = null;
        for (StoreHouse sh : storeHouses) {
            if (sh.getQuantity() > max) {
                maxStore = sh.getStore();
                max = sh.getQuantity();
            }
        }
        if (maxStore == null) {
            maxStore = storeRepository.findAll().get(0);
        }
        return maxStore;
    }

    private Integer countOfProductInStore(Product product) {
        List<ProductVariation> productVariations = productVariationRepository.findByParent(product);
        int count = 0;
        for (ProductVariation productVariation : productVariations) {
            count += countOfProductVariationInStore(productVariation);
        }
        return count;
    }

    private Integer countOfProductVariationInStore(ProductVariation productVariation) {
        List<StoreHouse> storeHouses = storeHouseRepository.findStoreHouseByProduct(productVariation);
        int count = 0;
        for (StoreHouse storeHouse : storeHouses) {
            count += storeHouse.getQuantity();
        }
        return count;
    }

    public Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId).orElseThrow(() -> new SearchException(SearchException.ORDER_NOT_FOUND));
    }

    public void deleteOrderItem(Long orderItemId) {
        OrderItem orderItem = getOrderItemById(orderItemId);
        Order order = getOrderByOrderItem(orderItem);
        order.removeOrderItem(orderItem);
        orderRepository.save(order);
        updateOrderData(order);
        orderItemRepository.delete(orderItem);
    }

    private Order getOrderByOrderItem(OrderItem orderItem) {
        return orderRepository.findByOrderItems(orderItem).orElseThrow(() -> new SearchException(SearchException.ORDER_NOT_FOUND));
    }

    public void updateOrderData(Order order) {
        int totalPrice = 0;
        for (OrderItem oi : order.getOrderItems())
            totalPrice += oi.getPrice();
        if (order.getShipment() != null)
            totalPrice += order.getShipment().getCost();
        order.setTotalPrice(totalPrice);
        orderRepository.save(order);
    }

    private OrderItem getOrderItemById(Long orderItemId) {
        return orderItemRepository.findById(orderItemId).orElseThrow(() -> new SearchException(SearchException.ORDER_ITEM_NOT_FOUND));
    }

    public void updateOrderItemSizes(List<OrderUpdateRequest> orderItems) throws NotEnoughException {
        for (OrderUpdateRequest oi : orderItems) {
            if (oi.getOrderItemId() == null || oi.getProductVariationId() == null) continue;
            OrderItem orderItem = getOrderItemById(oi.getOrderItemId());
            ProductVariation pv = productVariationRepository.findById(oi.getProductVariationId()).orElseThrow(() -> new SearchException(SearchException.PRODUCT_VARIATION_NOT_FOUND));
            int count = countOfProductVariationInStore(pv);
            if(count < orderItem.getQuantity())
                throw new NotEnoughException(orderItem.getProduct().getName() + " ( " + pv.getSize() + " ) ", count);
            orderItem.setProductVariation(pv);
            orderItemRepository.save(orderItem);
        }
    }

    public void newShipmentToOrder(Long orderId, String state, String town) {
        Shipment shipment = shipmentRepository.findByTownAndState(town, state).orElseThrow(() -> new SearchException(SearchException.SHIPMENT_NOT_FOUND));
        Order order = getOrderById(orderId);
        order.setShipment(shipment);
        orderRepository.save(order);
        updateOrderData(order);
    }

    public void removeShipmentByOrderId(Long orderId) {
        Order order = getOrderById(orderId);
        order.setShipment(null);
        orderRepository.save(order);
        updateOrderData(order);
    }

    public void orderStatusUp(Long orderId) {
        Order order = getOrderById(orderId);
        order.setStatus(OrderStatus.values()[order.getStatus().ordinal() + 1]);
        orderRepository.save(order);
    }

    public void orderStatusDown(Long orderId) {
        Order order = getOrderById(orderId);
        order.setStatus(OrderStatus.values()[order.getStatus().ordinal() - 1]);
        orderRepository.save(order);
    }

    public void itemQuantityDown(Long orderItemId) throws Exception {
        OrderItem orderItem = getOrderItemById(orderItemId);
        int priceOfOne = orderItem.getPrice() / orderItem.getQuantity();
        if (orderItem.getQuantity() > 1) {
            orderItem.setQuantity(orderItem.getQuantity() - 1);
            orderItem.setPrice(orderItem.getPrice() - priceOfOne);
            orderItemRepository.save(orderItem);
            Order order = getOrderByOrderItem(orderItem);
            updateOrderData(order);
        }else {
            throw new Exception("Нельзя уменьшить количество товара оно минимально");
        }
    }

    public void itemQuantityUp(Long orderItemId) throws Exception {
        OrderItem orderItem = getOrderItemById(orderItemId);
        int priceOfOne = orderItem.getPrice() / orderItem.getQuantity();
        int count = 0;
        if(orderItem.getProductVariation() != null){
            count = countOfProductVariationInStore(orderItem.getProductVariation());
        }else{
            throw new Exception("Выберите размер товара перед увеличением количества");
        }
        if (orderItem.getQuantity() + 1 < count) {
            orderItem.setQuantity(orderItem.getQuantity() + 1);
            orderItem.setPrice(orderItem.getPrice() + priceOfOne);
            orderItemRepository.save(orderItem);
            Order order = getOrderByOrderItem(orderItem);
            updateOrderData(order);
        }else {
            throw new NotEnoughException(orderItem.getProduct().getName() + " ( " + orderItem.getProductVariation().getSize() + " ) ", count);
        }
    }

    public Long createOrderByAdminUsername(String username) {
        try {
            User admin = getUserByUsername(username);
            Order order = new Order();
            Store store = getStoreByAdmin(admin);
            order.setStore(store);
            order.setStatus(OrderStatus.REQUEST);
            orderRepository.save(order);
            return order.getId();
        }catch (Exception e){
            throw new AccessDeniedException(AccessDeniedException.ONLY_ADMIN_OF_SHOP);
        }
    }

    private Store getStoreByAdmin(User admin) {
        return storeRepository.findByAdmin(admin).orElseThrow(() -> new SearchException(SearchException.STORE_NOT_FOUND));
    }

    public void addProductToOrder(Long orderId, Long productId) throws ExistException, NotEnoughException {
        Order order = getOrderById(orderId);
        Product product = getProductById(productId);
        if(countOfProductInStore(product) < 1){
            throw new NotEnoughException(product.getName(), 0);
        }
        OrderItem orderItem = new OrderItem();
        orderItem.setProduct(product);
        orderItem.setQuantity(1);
        orderItem.setPrice(product.getPrice());
        orderItemRepository.save(orderItem);
        order.addOrderItem(orderItem);
        orderRepository.save(order);
        updateOrderData(order);
    }

    private Product getProductById(Long productId){
        return productRepository.findById(productId).orElseThrow(() -> new SearchException(SearchException.PRODUCT_NOT_FOUND));
    }
}
