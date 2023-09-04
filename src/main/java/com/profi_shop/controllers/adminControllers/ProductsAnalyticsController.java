package com.profi_shop.controllers.adminControllers;

import com.profi_shop.dto.ReceiptDTO;
import com.profi_shop.dto.ReceiptWrapperDto;
import com.profi_shop.model.Product;
import com.profi_shop.model.ProductVariation;
import com.profi_shop.model.Receipt;
import com.profi_shop.model.SalesData;
import com.profi_shop.services.ProductService;
import com.profi_shop.services.ProductsAnalytics;
import com.profi_shop.services.facade.ReceiptFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class ProductsAnalyticsController {

    private final ProductService productService;
    private final ProductsAnalytics productsAnalytics;

    private final ReceiptFacade receiptFacade;

    @Autowired
    public ProductsAnalyticsController(ProductService productService, ProductsAnalytics productsAnalytics, ReceiptFacade receiptFacade) {
        this.productService = productService;
        this.productsAnalytics = productsAnalytics;
        this.receiptFacade = receiptFacade;
    }

    @GetMapping("/product-analytics")
    public String productAnalytics(@RequestParam Long productId,
                                   Model model) {
        try {

            Product product = productService.getProductById(productId);
            List<ProductVariation> productVariations = productService.getProductVariations(productId);
            List<Receipt> receipts = productsAnalytics.getMonthlyReceiptsByProduct(productVariations.get(0).getId(), 0);
            model.addAttribute("product", product);
            model.addAttribute("receipts", receipts);
            model.addAttribute("productVariations", productVariations);
        }catch (Exception e){
            e.printStackTrace();
        }
        return "admin/product/productAnalytics";
    }

    @GetMapping("/sales/product/{productId}/{month}")
    public ResponseEntity<List<SalesData>> getSalesData(@PathVariable Long productId,
                                                        @PathVariable Integer month) {
        try {
            List<SalesData> sales = productsAnalytics.getMonthlyAnalyticsByProduct(productId, month);
            return new ResponseEntity<>(sales, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/product/receipts/{productId}/{month}")
    public ResponseEntity<ReceiptWrapperDto> getReceipts(@PathVariable Long productId,
                                                         @PathVariable Integer month) {
        try {
            List<ReceiptDTO> receipts = receiptFacade.mapToReceiptsDTOList(productsAnalytics.getMonthlyReceiptsByProduct(productId, month));
            String monthName = productsAnalytics.getEndOfMonthName(month); // Здесь получите название месяца

            ReceiptWrapperDto responseData = new ReceiptWrapperDto(receipts, monthName);
            return new ResponseEntity<>(responseData, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/product/sales/{productId}/{month}")
    public ResponseEntity<Map<String, Long>> getSalesAmount(@PathVariable Long productId,
                                                            @PathVariable Integer month) {
        try {
            Map<String, Long> response = productsAnalytics.getSalesAmountForProductBetweenDates(productId, month);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
