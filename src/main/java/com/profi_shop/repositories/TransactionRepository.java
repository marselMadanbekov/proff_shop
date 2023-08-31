package com.profi_shop.repositories;

import com.profi_shop.model.Store;
import com.profi_shop.model.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Page<Transaction> findBySender(Store store, Pageable pageable);

    Page<Transaction> findByTarget(Store store, Pageable pageable);

    Page<Transaction> findByToMainStore(boolean b, Pageable pageable);

    @Query("SELECT SUM(t.amount) FROM Transaction t " +
            "WHERE (t.sender = :store OR t.target = :store) " +
            "AND t.date >= :startDate " +
            "AND t.date <= :endDate")
    Optional<Integer> getTotalTransactionAmountForStore(Store store, Date startDate, Date endDate);
}
