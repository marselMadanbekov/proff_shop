package com.profi_shop.services;

import com.profi_shop.exceptions.AccessDeniedException;
import com.profi_shop.exceptions.SearchException;
import com.profi_shop.model.MainStore;
import com.profi_shop.model.Store;
import com.profi_shop.model.Transaction;
import com.profi_shop.model.User;
import com.profi_shop.model.enums.Role;
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
    private final NotificationService notificationService;
    private final StoreRepository storeRepository;
    private final MainStoreService mainStoreService;

    @Autowired
    public TransactionService(TransactionRepository transactionRepository, UserRepository userRepository, NotificationService notificationService, StoreRepository storeRepository, MainStoreService mainStoreService) {
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
        this.notificationService = notificationService;
        this.storeRepository = storeRepository;
        this.mainStoreService = mainStoreService;
    }

    public void createTransaction(Long senderStoreId, Long targetStoreId, String username, Integer amount) {
        User sender = getUserByUsername(username);
        Store senderStore = getStoreById(senderStoreId);
        if (!sender.equals(senderStore.getAdmin()))
            throw new AccessDeniedException(AccessDeniedException.FOREIGN_BRANCH);


        Transaction transaction = new Transaction();
        if (targetStoreId == 0)
            transaction.setToMainStore(true);
        else {
            Store targetStore = getStoreById(targetStoreId);
            transaction.setToMainStore(false);
            transaction.setTarget(targetStore);
        }
        transaction.setAmount(amount);
        transaction.setSender(senderStore);
        transaction.setAllowed(false);
        transactionRepository.save(transaction);
        createNotificationToTransaction(transaction);
    }

    public void allowTransaction(Long transactionId, String username) {
        User user = getUserByUsername(username);
        Transaction transaction = getTransactionById(transactionId);
        Store sender = transaction.getSender();

        if (!transaction.isToMainStore()) {
            if (!transaction.getTarget().getAdmin().equals(user))
                throw new AccessDeniedException(AccessDeniedException.FOREIGN_TRANSACTION);

            transaction.setAllowed(true);
            Store target = transaction.getTarget();

            sender.balanceDown(transaction.getAmount());
            target.balanceUp(transaction.getAmount());
            storeRepository.save(target);
        } else {
            if (!user.getRole().equals(Role.ROLE_SUPER_ADMIN))
                throw new AccessDeniedException(AccessDeniedException.ONLY_SUPER_ADMIN);
            MainStore mainStore = mainStoreService.getMainStore();
            mainStore.balanceUp(transaction.getAmount());
            transaction.setAllowed(true);
            mainStoreService.save(mainStore);
            sender.balanceDown(transaction.getAmount());
        }
        storeRepository.save(sender);
        transactionRepository.save(transaction);
    }

    private Transaction getTransactionById(Long transactionId) {
        return transactionRepository.findById(transactionId).orElseThrow(() -> new SearchException(SearchException.TRANSACTION_NOT_FOUND));
    }

    private void createNotificationToTransaction(Transaction transaction) {
        notificationService.createLocalNewTransactionNotification(transaction.getTarget(), transaction.getSender(), transaction.getAmount());
    }

    public Page<Transaction> getPagedSendTransactionsByStore(Long storeId, Integer page, Integer sort) {
        Store store = getStoreById(storeId);
        Pageable pageable = getPageableByParams(sort, page);
        return transactionRepository.findBySender(store, pageable);

    }

    public Page<Transaction> getPagedReceivedTransactionsByStore(Long storeId, Integer page, Integer sort) {
        Store store = getStoreById(storeId);
        Pageable pageable = getPageableByParams(sort, page);
        return transactionRepository.findByTarget(store, pageable);
    }

    private Store getStoreById(Long senderStoreId) {
        return storeRepository.findById(senderStoreId).orElseThrow(() -> new SearchException(SearchException.STORE_NOT_FOUND));
    }

    private User getUserByUsername(String username) {
        return userRepository.findUserByUsername(username).orElseThrow(() -> new SearchException(SearchException.USER_NOT_FOUND));
    }


    public Page<Transaction> mainStoreTransactions(Integer sort, Integer page) {
        Pageable pageable = getPageableByParams(sort, page);
        return transactionRepository.findByToMainStore(true, pageable);
    }

    private Pageable getPageableByParams(int sort, int page) {
        return switch (sort) {
            case 0 -> PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "date"));
            case 1 -> PageRequest.of(page, 10, Sort.by(Sort.Direction.ASC, "date"));
            case 2 -> PageRequest.of(page, 10, Sort.by(Sort.Direction.ASC, "amount"));
            default -> PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "amount"));
        };
    }
}
