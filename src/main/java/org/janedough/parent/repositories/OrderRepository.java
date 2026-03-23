package org.janedough.parent.repositories;

import org.janedough.parent.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("SELECT COALESCE(SUM(o.totalPrice), 0) FROM Order o")
    Double getTotalRevenue();
}
