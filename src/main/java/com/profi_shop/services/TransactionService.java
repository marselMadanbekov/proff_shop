package com.profi_shop.services;

import com.profi_shop.exceptions.AccessDeniedException;
import com.profi_shop.exceptions.SearchException;
import com.profi_shop.model.Store;
import com.profi_shop.model.Transaction;
import com.profi_shop.model.User;
import com.profi_shop.repositories.StoreRepository;
import com.profi_shop.repositories.TransactionRepository;
import com.profi_shop.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final StoreRepository storeRepository;
    @Autowired
    public TransactionService(TransactionRepository transactionRepository, UserRepository userRepository, StoreRepository storeRepository) {
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
        this.storeRepository = storeRepository;
    }

    public void createTransaction(Long senderStoreId, Long targetStoreId, String username, Integer amount){
        User sender = getUserByUsername(username);
        Store senderStore = getStoreById(senderStoreId);
        Store targetStore = getStoreById(targetStoreId);
        if(!sender.equals(senderStore.getAdmin()))
            throw new AccessDeniedException(AccessDeniedException.FOREIGN_BRANCH);
        targetStore.balanceUp(amount);
        senderStore.balanceDown(amount);

        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setSender(senderStore);
        transaction.setTarget(targetStore);
        transactionRepository.save(transaction);
    }

    public Page<Transaction> getPagedTransactionsByStore(Long storeId,Integer page, Integer sort){
        Store store = getStoreById(storeId);
        Pageable pageable = switch (sort) {
            case 0 -> PageRequest.of(page, 10, Sort.by(Sort.Direction.ASC, "date"));
            case 1 -> PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "date"));
            case 2 -> PageRequest.of(page, 10, Sort.by(Sort.Direction.ASC, "amount"));
            default -> PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "amount"));
        };
        return transactionRepository.findBySender(store, pageable);

    }

    private Store getStoreById(Long senderStoreId) {
        return storeRepository.findById(senderStoreId).orElseThrow(() -> new SearchException(SearchException.STORE_NOT_FOUND));
    }

    private User getUserByUsername(String username) {
        return userRepository.findUserByUsername(username).orElseThrow(() -> new SearchException(SearchException.USER_NOT_FOUND));
    }
}
