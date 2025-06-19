package com.billing_system.orders.Repositories;

import com.billing_system.orders.Entities.Orders;
import com.billing_system.orders.Entities.OrdersProducts;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrdersProductsRepository extends JpaRepository<OrdersProducts, Long> {
    void deleteByOrders(Orders orders);
    List<OrdersProducts> findByOrders(Orders orders);
}
