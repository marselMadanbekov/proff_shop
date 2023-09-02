package com.profi_shop.services.facade;

import com.profi_shop.dto.ReceiptDTO;
import com.profi_shop.model.Receipt;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReceiptFacade {
    public ReceiptDTO receiptToReceiptDto(Receipt receipt){
        ReceiptDTO receiptDTO = new ReceiptDTO();
        receiptDTO.setQuantity(receipt.getQuantity());
        receiptDTO.setDate(receipt.getDate().toString());
        receiptDTO.setActorInfo(receipt.getActorInfo());
        receiptDTO.setProductSize(receipt.getProductVariation().getSize());
        receiptDTO.setProductName(receipt.getProductVariation().getParent().getName());
        receiptDTO.setStoreTown(receipt.getStore().getTown());
        return receiptDTO;
    }
    public List<ReceiptDTO> mapToReceiptsDTOList(List<Receipt> receipts) {
        return receipts.stream()
                .map(this::receiptToReceiptDto)
                .collect(Collectors.toList());
    }
}
