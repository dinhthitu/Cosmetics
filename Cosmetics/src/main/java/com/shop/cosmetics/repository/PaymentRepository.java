package com.shop.cosmetics.repository;

import com.shop.cosmetics.entity.Order;
import com.shop.cosmetics.entity.Payment;
import org.hibernate.sql.ast.tree.expression.JdbcParameter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    @Query("select p from Payment p where p.transactionId = :txnId")
    Payment findByTransactionId(@Param("txnId") String txnId);

    @Query("select p from Payment p where p.order = :order")
    Payment findByOrder(@Param("order") Order order);
}
