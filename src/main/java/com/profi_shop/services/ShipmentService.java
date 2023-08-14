package com.profi_shop.services;

import com.profi_shop.exceptions.ExistException;
import com.profi_shop.exceptions.SearchException;
import com.profi_shop.model.Shipment;
import com.profi_shop.model.requests.ShipmentCreateRequest;
import com.profi_shop.repositories.ShipmentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public class ShipmentService {
    private final ShipmentRepository shipmentRepository;

    public ShipmentService(ShipmentRepository shipmentRepository) {
        this.shipmentRepository = shipmentRepository;
    }


    public void createShipment(ShipmentCreateRequest request) throws ExistException {
        Shipment shipment = new Shipment();
        shipment.setCost(request.getCost());
        shipment.setTown(request.getTown());
        shipment.setTownCAPS(request.getTown().toUpperCase());
        shipment.setState(request.getState());
        shipment.setStateCAPS(request.getState().toUpperCase());
        try {
            shipmentRepository.save(shipment);
        }catch (Exception e){
            throw new ExistException(ExistException.SHIPMENT_EXISTS);
        }
    }

    public List<String> getUniqueStates() {
        return shipmentRepository.findDistinctStates();
    }
    public List<String> getUniqueTowns() {
        return shipmentRepository.findDistinctTown();
    }

    public Page<Shipment> getPagedShipments(Integer page) {
        Pageable pageable = PageRequest.of(page,15);
        return shipmentRepository.findAll(pageable);
    }

    public void deleteShipmentById(Long id) {
        shipmentRepository.delete(getShipmentById(id));
    }

    private Shipment getShipmentById(Long id){
        return shipmentRepository.findById(id).orElseThrow(() ->new SearchException(SearchException.SHIPMENT_NOT_FOUND));
    }

    public List<String> getTownsByState(String state) {
        return shipmentRepository.findTownsByState(state);
    }

    public Integer calculateShipping(String state, String town) {
        return getShippingByStateAndTown(state,town).getCost();
    }

    public Shipment getShippingByStateAndTown(String state, String town){
        return shipmentRepository.findByTownAndState(town,state).orElseThrow(() -> new SearchException(SearchException.SHIPMENT_NOT_FOUND));
    }
}
