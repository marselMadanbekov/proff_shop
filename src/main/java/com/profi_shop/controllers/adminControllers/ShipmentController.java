package com.profi_shop.controllers.adminControllers;

import com.profi_shop.exceptions.ExistException;
import com.profi_shop.model.Shipment;
import com.profi_shop.model.requests.ShipmentCreateRequest;
import com.profi_shop.services.ShipmentService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class ShipmentController {
    private final ShipmentService shipmentService;

    public ShipmentController(ShipmentService shipmentService) {
        this.shipmentService = shipmentService;
    }

    @GetMapping("/shipments")
    public String shipments(@RequestParam(value = "page", required = false) Optional<Integer> page,
                            Model model){
        Page<Shipment> shipments = shipmentService.getPagedShipments(page.orElse(0));
        model.addAttribute("shipments", shipments);
        return "admin/shipment/shipment";
    }

    @PostMapping("/shipment/delete")
    public ResponseEntity<String> deleteShipment(@RequestParam("shipmentId") Long id){
        try{
            shipmentService.deleteShipmentById(id);
            return new ResponseEntity<>("Доставка успешно удалена", HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/create-shipment")
    public ResponseEntity<String> createShipment(ShipmentCreateRequest shipment){
        try{
            shipmentService.createShipment(shipment);
            return new ResponseEntity<>("Доставка успешно создана", HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }
}
