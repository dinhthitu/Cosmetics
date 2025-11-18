package com.shop.cosmetics.repository;

import com.shop.cosmetics.entity.Order;
import com.shop.cosmetics.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Collection<Object> findByUser(User user);
}
