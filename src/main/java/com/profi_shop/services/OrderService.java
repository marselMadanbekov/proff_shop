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
    private final CouponService couponService;
    private final OrderItemRepository orderItemRepository;
    private final ProductVariationRepository productVariationRepository;
    private final ProductRepository productRepository;
    private final ShipmentRepository shipmentRepository;
    private final StoreRepository storeRepository;
    private final NotificationService notificationService;
    private final StoreHouseRepository storeHouseRepository;
    private final UserRepository userRepository;
    private final CartService cartService;
    private final PriceService priceService;

    @Autowired
    public OrderService(OrderRepository orderRepository, CouponService couponService, OrderItemRepository orderItemRepository, ProductVariationRepository productVariationRepository, ProductRepository productRepository, ShipmentRepository shipmentRepository, StoreRepository storeRepository, NotificationService notificationService, StoreHouseRepository storeHouseRepository, UserRepository userRepository, CartService cartService, PriceService priceService) {
        this.orderRepository = orderRepository;
        this.couponService = couponService;
        this.orderItemRepository = orderItemRepository;
        this.productVariationRepository = productVariationRepository;
        this.productRepository = productRepository;
        this.shipmentRepository = shipmentRepository;
        this.storeRepository = storeRepository;
        this.notificationService = notificationService;
        this.storeHouseRepository = storeHouseRepository;
        this.userRepository = userRepository;
        this.cartService = cartService;
        this.priceService = priceService;
    }

    public Page<Order> getFilteredOrders(Integer status, Integer sort, Long storeId, Integer page) {
        Pageable pageable;
        if (sort == 0) pageable = PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "date"));
        else if (sort == 1) pageable = PageRequest.of(page, 10, Sort.by(Sort.Direction.ASC, "date"));
        else if (sort == 2) pageable = PageRequest.of(page, 10, Sort.by(Sort.Direction.ASC, "totalPrice"));
        else pageable = PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "totalPrice"));

        Store store = null;
        if (!storeId.equals(0L))
            store = getStoreById(storeId);

        switch (status) {
            case 1:
                if (store == null) return orderRepository.findByStatus(OrderStatus.CANCELED, pageable);
                else return orderRepository.findByStatusAndStore(OrderStatus.CANCELED, store, pageable);
            case 2:
                if (store == null) return orderRepository.findByStatus(OrderStatus.REQUEST, pageable);
                return orderRepository.findByStatusAndStore(OrderStatus.REQUEST, store, pageable);
            case 3:
                if (store == null) return orderRepository.findByStatus(OrderStatus.PAID, pageable);
                return orderRepository.findByStatusAndStore(OrderStatus.PAID, store, pageable);
            case 4:
                if (store == null) return orderRepository.findByStatus(OrderStatus.DELIVERING, pageable);
                return orderRepository.findByStatusAndStore(OrderStatus.DELIVERING, store, pageable);
            case 5:
                if (store == null) return orderRepository.findByStatus(OrderStatus.FINISHED, pageable);
                return orderRepository.findByStatusAndStore(OrderStatus.FINISHED, store, pageable);
            default:
                if (store == null) return orderRepository.findAll(pageable);
                return orderRepository.findByStore(store, pageable);
        }
    }

    public void createOrderByUnknown(OrderRequest orderRequest, HttpServletRequest request) throws Exception {
        Cart cart = cartService.getCartByRequestCookies(request);
        Order order = createBaseOrder(orderRequest, cart);
        orderRepository.save(order);
        notificationService.createLocalNewOrderNotification(order.getStore().getAdmin(), order.getId());
    }

    public void createOrderByUsername(OrderRequest orderRequest, String username) throws Exception {
        User user = getUserByUsername(username);
        Cart cart = cartService.getCartByUsername(username);
        Order order = createBaseOrder(orderRequest, cart);
        order.setUser(user);
        orderRepository.save(order);
        cartService.clearCart(cart);
        notificationService.createLocalNewOrderNotification(order.getStore().getAdmin(), order.getId());
    }

    private Order createBaseOrder(OrderRequest orderRequest, Cart cart) throws Exception {
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
            orderItem.setPrice(cartItem.getAmountWithDiscount());
            orderItemRepository.save(orderItem);
            order.addOrderItem(orderItem);
        }

        if(cart.getCoupon() != null){
            if(!couponService.isCouponAvailable(cart.getCoupon())) {
                throw new CouponException(CouponException.COUPON_ALREADY_USED);
            }
            order.setCoupon(cart.getCoupon());
        }
        totalPrice += cart.cartAmountWithDiscount();
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

    private void checkOrderItemEnough(OrderItem orderItem) throws NotEnoughException {
        int countOnStore = countOfProductVariationInStore(orderItem.getProductVariation());
        if(countOnStore >= orderItem.getQuantity()) return;
        throw new NotEnoughException(orderItem.getProduct().getName() + " ( " + orderItem.getProductVariation().getSize() + " ) ", countOnStore);
    }

    public void updateOrderItemSizes(List<OrderUpdateRequest> orderItems) throws NotEnoughException, ExistException {
        for (OrderUpdateRequest oi : orderItems) {
            if (oi.getOrderItemId() == null || oi.getProductVariationId() == null) continue;
            OrderItem orderItem = getOrderItemById(oi.getOrderItemId());
            ProductVariation pv = productVariationRepository.findById(oi.getProductVariationId()).orElseThrow(() -> new SearchException(SearchException.PRODUCT_VARIATION_NOT_FOUND));

            if (pv.equals(orderItem.getProductVariation())) continue;
            if (isOrderContainsProductVariation(pv, orderItem))
                throw new ExistException(ExistException.ORDER_ITEM_EXISTS);

            checkOrderItemEnough(orderItem);

            orderItem.setProductVariation(pv);
            orderItemRepository.save(orderItem);
        }
    }

    private boolean isOrderContainsProductVariation(ProductVariation productVariation, OrderItem orderItem) {
        Order order = getOrderByOrderItem(orderItem);
        for (OrderItem oi : order.getOrderItems()) {
            if (productVariation.equals(oi.getProductVariation())) return true;
        }
        return false;
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

    public void orderStatusUp(Long orderId) throws Exception {
        Order order = getOrderById(orderId);
        if (order.getStatus().equals(OrderStatus.CANCELED))
            throw new Exception("MESSAGE_CHANGING_STATUS_OF_CANCELED_ORDER");
        order.setStatus(OrderStatus.values()[order.getStatus().ordinal() + 1]);
        if (order.getStatus().equals(OrderStatus.PAID)) {
            Store store = order.getStore();
            for (OrderItem orderItem : order.getOrderItems()) {
                checkOrderItemEnough(orderItem);
                ProductVariation productVariation = orderItem.getProductVariation();
                if (productVariation == null)
                    throw new Exception("Не указан размер для продукта " + orderItem.getProduct().getName() + " . Для воспроизведения оплаты выберите размер.");
                StoreHouse storeHouse = getStoreHouseByProductVariationAndStore(productVariation, order.getStore());
                if (orderItem.getQuantity() <= storeHouse.getQuantity()) {
                    storeHouse.quantityDown(orderItem.getQuantity());
                } else {
                    List<StoreHouse> donors = getOptimalStoreHousesByProductAndStore(productVariation, store, orderItem.getQuantity());
                    int quantity = orderItem.getQuantity() - storeHouse.getQuantity();

                    storeHouse.setQuantity(0);
                    for (StoreHouse donor : donors) {
                        if (quantity > donor.getQuantity()) {
                            quantity -= donor.getQuantity();
                            donor.setQuantity(0);
                            notificationService.createNotEnoughNotificationInOneTargetStoreOfOrder(store, donor.getStore(), productVariation);
                        } else {
                            notificationService.createNotEnoughNotificationInOneTargetStoreOfOrder(store, donor.getStore(), productVariation);
                            donor.quantityDown(quantity);
                            break;
                        }
                        storeHouseRepository.save(donor);
                    }
                    storeHouseRepository.save(storeHouse);
                }
            }
            if (order.getShipment() == null) store.balanceUp(order.getTotalPrice());
            else store.balanceUp(order.getTotalPrice() - order.getShipment().getCost());
            couponService.createCouponIfNeeded(order);
        }
        orderRepository.save(order);
    }



    public void orderStatusDown(Long orderId) throws Exception {
        Order order = getOrderById(orderId);
        if (order.getStatus().equals(OrderStatus.CANCELED))
            throw new Exception("MESSAGE_CHANGING_STATUS_OF_CANCELED_ORDER");
        if (order.getStatus().equals(OrderStatus.PAID)) {
            Store store = order.getStore();
            if (order.getShipment() == null) store.balanceDown(order.getTotalPrice());
            else store.balanceDown((order.getTotalPrice() - order.getShipment().getCost()));

            for(OrderItem orderItem : order.getOrderItems()){
                StoreHouse storeHouse = getStoreHouseByProductVariationAndStore(orderItem.getProductVariation(), store);
                storeHouse.quantityUp(orderItem.getQuantity());
                storeHouseRepository.save(storeHouse);
            }
        }
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
        } else {
            throw new Exception("Нельзя уменьшить количество товара оно минимально");
        }
    }

    private StoreHouse getStoreHouseByProductVariationAndStore(ProductVariation productVariation, Store store) {
        return storeHouseRepository.findByProductAndStore(productVariation, store).orElseThrow(() -> new SearchException(SearchException.STORE_HOUSE_NOT_FOUND));
    }

    private List<StoreHouse> getOptimalStoreHousesByProductAndStore(ProductVariation productVariation, Store acceptor, int count) {
        List<StoreHouse> allStoreHouses = storeHouseRepository.findStoreHouseByProduct(productVariation);
        StoreHouse targetStoreHouse = getStoreHouseByProductVariationAndStore(productVariation, acceptor);
        count -= targetStoreHouse.getQuantity();
        allStoreHouses.remove(targetStoreHouse);
        allStoreHouses.sort((s1, s2) -> Integer.compare(s2.getQuantity(), s1.getQuantity()));

        List<StoreHouse> donors = new ArrayList<>();
        for (StoreHouse donor : allStoreHouses) {
            if (donor.getQuantity() > count) {
                donors.add(donor);
                break;
            }
            if (donor.getQuantity() > 0) {
                donors.add(donor);
                count -= donor.getQuantity();
            }
            if (count <= 0) break;
        }
        return donors;
    }

    public void itemQuantityUp(Long orderItemId) throws Exception {
        OrderItem orderItem = getOrderItemById(orderItemId);
        int priceOfOne = orderItem.getPrice() / orderItem.getQuantity();
        int count = 0;
        if (orderItem.getProductVariation() != null) {
            count = countOfProductVariationInStore(orderItem.getProductVariation());
        } else {
            throw new Exception("Выберите размер товара перед увеличением количества");
        }
        if (orderItem.getQuantity() + 1 <= count) {
            orderItem.setQuantity(orderItem.getQuantity() + 1);
            orderItem.setPrice(orderItem.getPrice() + priceOfOne);
            orderItemRepository.save(orderItem);
            Order order = getOrderByOrderItem(orderItem);
            updateOrderData(order);
        } else {
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
        } catch (Exception e) {
            throw new AccessDeniedException(AccessDeniedException.ONLY_ADMIN_OF_SHOP);
        }
    }



    private Store getStoreByAdmin(User admin) {
        return storeRepository.findByAdmin(admin).orElseThrow(() -> new SearchException(SearchException.STORE_NOT_FOUND));
    }

    public void addProductToOrder(Long orderId, Long productId) throws ExistException, NotEnoughException {
        Order order = getOrderById(orderId);
        boolean orderOfAuthenticated = order.getUser() != null;
        Product product = getProductById(productId);
        if (countOfProductInStore(product) < 1) {
            throw new NotEnoughException(product.getName(), 0);
        }
        int discount = priceService.getDiscountForProductForUser(product,orderOfAuthenticated);
        if(discount == 0 && order.getCoupon() != null){
            discount = order.getCoupon().getDiscount();
        }
        OrderItem orderItem = new OrderItem();
        orderItem.setProduct(product);
        orderItem.setQuantity(1);
        orderItem.setPrice((int) (product.getPrice() - (product.getPrice() * discount) / 100.0));

        List<ProductVariation> productVariations = productVariationRepository.findByParent(product);
        if (productVariations.size() == 1) {
            orderItem.setProductVariation(productVariations.get(0));
        }
        orderItemRepository.save(orderItem);
        order.addOrderItem(orderItem);
        orderRepository.save(order);
        updateOrderData(order);
    }

    private Product getProductById(Long productId) {
        return productRepository.findById(productId).orElseThrow(() -> new SearchException(SearchException.PRODUCT_NOT_FOUND));
    }

    public void redirectOrder(Long orderId, Long targetStoreId) {
        Order order = getOrderById(orderId);
        Store store = getStoreById(targetStoreId);

        if (!order.getStore().equals(store)) {
            notificationService.createLocalOrderRedirectNotification(store.getAdmin(), order.getStore().getAdmin().getUsername(), orderId);
            order.setStore(store);
            orderRepository.save(order);
        }
    }

    private Store getStoreById(Long targetStoreId) {
        return storeRepository.findById(targetStoreId).orElseThrow(() -> new SearchException(SearchException.STORE_NOT_FOUND));
    }

    public void cancelOrder(Long orderId, String username) {
        User user = getUserByUsername(username);

        Order order = getOrderById(orderId);
        Store store = order.getStore();

        if (order.getStatus().ordinal() >= OrderStatus.PAID.ordinal()) {
            for (OrderItem orderItem : order.getOrderItems()) {
                ProductVariation pv = orderItem.getProductVariation();
                StoreHouse storeHouse = getStoreHouseByProductVariationAndStore(pv, store);
                storeHouse.quantityUp(orderItem.getQuantity());
            }

            if (order.getShipment() == null)
                store.balanceDown(order.getTotalPrice());
            else
                store.balanceDown(order.getTotalPrice() - order.getShipment().getCost());

        }
        order.setStatus(OrderStatus.CANCELED);
        orderRepository.save(order);
    }

    public List<Order> getRecentOrdersByUsername(String name) {
        return orderRepository.findTop10ByUserOrderByDateDesc(getUserByUsername(name));
    }
}
