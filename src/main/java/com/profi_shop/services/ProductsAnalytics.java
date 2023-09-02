package com.profi_shop.services;

import com.profi_shop.exceptions.SearchException;
import com.profi_shop.model.Product;
import com.profi_shop.model.ProductVariation;
import com.profi_shop.model.Receipt;
import com.profi_shop.model.SalesData;
import com.profi_shop.model.enums.OrderStatus;
import com.profi_shop.repositories.OrderRepository;
import com.profi_shop.repositories.ProductRepository;
import com.profi_shop.repositories.ProductVariationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class ProductsAnalytics {
    private final OrderRepository orderRepository;

    private final ProductRepository productRepository;
    private final ProductVariationRepository productVariationRepository;
    private final ReceiptService receiptService;

    @Autowired
    public ProductsAnalytics(OrderRepository orderRepository, ProductRepository productRepository, ProductVariationRepository productVariationRepository, ReceiptService receiptService) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.productVariationRepository = productVariationRepository;
        this.receiptService = receiptService;
    }

    public List<SalesData> getAnalyticsByProductId(Long productId) {
        Product product = getProductById(productId);
        List<Object[]> results = orderRepository.getSalesDataForProductLast4Months(product, Date.valueOf(LocalDate.now().minusMonths(4)), OrderStatus.FINISHED);


        List<SalesData> salesDataList = new ArrayList<>();

        for (Object[] result : results) {
            String month = result[0].toString(); // Первый столбец - месяц
            Integer sales = Integer.parseInt(result[1].toString()); // Второй столбец - количество продаж

            SalesData salesData = new SalesData(month, sales);
            salesDataList.add(salesData);
        }
        return salesDataList;
    }

    private ProductVariation getProductVariationById(Long productVariationId){
        return productVariationRepository.findById(productVariationId).orElseThrow(() -> new SearchException(SearchException.PRODUCT_VARIATION_NOT_FOUND));
    }
    public List<SalesData> getMonthlyAnalyticsByProduct(Long productId, Integer month) {
        ProductVariation product = getProductVariationById(productId);

        List<SalesData> salesData = new ArrayList<>();

        for (int i = month + 3; i >= month; i--) {
            LocalDate startDate = getStartOfMonthAgo(i);
            LocalDate endDate = getEndOfMonthAgo(i).plusDays(1);
            System.out.println("startDate -> " + startDate);
            System.out.println("endDate -> " + endDate);

            int count = orderRepository.getCoundSoldProductBetweenDates(product,Date.valueOf(startDate), Date.valueOf(endDate),OrderStatus.FINISHED).orElse(0);
            SalesData salesData1 = new SalesData(startDate.format(DateTimeFormatter.ofPattern("MMMM-yyyy")),count);
            salesData.add(salesData1);
        }
        return salesData;
    }

    public Map<String,Long> getSalesAmountForProductBetweenDates(Long productVariationId,Integer month){
        LocalDate startDate = getStartOfMonthAgo(month);
        LocalDate endDate = getEndOfMonthAgo(month).plusDays(1);
        ProductVariation productVariation = getProductVariationById(productVariationId);
        Map<String, Long> salesData = orderRepository.getTotalSalesAndQuantityForProductBetweenDates(productVariation, Date.valueOf(startDate), Date.valueOf(endDate),OrderStatus.FINISHED);

        if (salesData != null) {
            Long totalSales = salesData.get("totalSales");
            Long totalQuantity = salesData.get("totalQuantity");

            salesData = new HashMap<>();

            salesData.put("totalSales", Objects.requireNonNullElse(totalSales, 0L));

            if(totalQuantity != null)
                salesData.put("targetAmount", totalQuantity * productVariation.getParent().getPrice());
            else
                salesData.put("targetAmount", 0L);
        } else {
            salesData = new HashMap<>();
            salesData.put("totalSales", 0L);
            salesData.put("totalAmount", 0L);
        }
        return salesData;
    }

    public LocalDate getStartOfMonthAgo(int count) {
        return LocalDate.now().minusMonths(count).withDayOfMonth(1);
    }

    public LocalDate getEndOfMonthAgo(int count) {
        LocalDate startDate = getStartOfMonthAgo(count);
        YearMonth yearMonth = YearMonth.from(startDate);
        return yearMonth.atEndOfMonth();

    }


    private Product getProductById(Long productId) {
        return productRepository.findById(productId).orElseThrow(() -> new SearchException(SearchException.PRODUCT_NOT_FOUND));
    }

    public List<Receipt> getMonthlyReceiptsByProduct(Long productId, int monthAgo) {
        LocalDate startDate = getStartOfMonthAgo(monthAgo);
        LocalDate endDate = getEndOfMonthAgo(monthAgo).plusDays(1);

        ProductVariation productVariation = getProductVariationById(productId);

        return receiptService.getReceiptsBetween(productVariation,Date.valueOf(startDate),Date.valueOf(endDate));
    }

    public String getEndOfMonthName(Integer month) {
        LocalDate startDate = getStartOfMonthAgo(month);
        return startDate.format(DateTimeFormatter.ofPattern("MMMM-yyyy"));
    }
}
