package com.example.paymentapp2.order;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.math.BigDecimal;

import org.hibernate.annotations.processing.SQL;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.paymentapp2.order.OrderEntity;

@Service
public class ManualJpaService {

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public void demoFlow() {

        System.out.println("---- PERSIST ----");

        OrderEntity order = new OrderEntity();
        order.setUserId(99L);
        order.setStatus("NEW");

        em.persist(order);

        System.out.println("Generated ID: " + order.getId());

        System.out.println("---- FIND ----");

        OrderEntity o1 = em.find(OrderEntity.class, order.getId());
        OrderEntity o2 = em.find(OrderEntity.class, order.getId());

        System.out.println("o1 == o2 ? " + (o1 == o2));

        System.out.println("---- DIRTY CHECK ----");

        o1.setStatus("PAID");

        System.out.println("---- END METHOD ----");
    }

    @Transactional
    public void createOrderWithItems() {

        OrderEntity order = new OrderEntity();
        order.setUserId(1L);
        order.setStatus("NEW");

        OrderItemEntity item1 = OrderItemEntity.builder()
                .productName("Laptop")
                .price(BigDecimal.valueOf(1000))
                .quantity(1)
                .build();

        OrderItemEntity item2 = OrderItemEntity.builder()
                .productName("Mouse")
                .price(BigDecimal.valueOf(50))
                .quantity(2)
                .build();

        order.addItem(item1);
        order.addItem(item2);

        em.persist(order); // Items saved automatically (cascade)
    }

    @Transactional
    public void fetchOrder(Long id) {

        OrderEntity order = em.find(OrderEntity.class, id);

        System.out.println(order.getStatus());
        // Important:
        // getItems() triggers a SELECT because of LAZY loading.
        System.out.println(order.getItems().size());
    }
    // 🔥 N+1 Problem Example

    // If you do:

    // List<OrderEntity> orders =
    // em.createQuery("SELECT o FROM OrderEntity o", OrderEntity.class)
    // .getResultList();

    // for(
    // OrderEntity o:orders)
    // {
    // o.getItems().size();
    // }
    // This will cause N+1 problem because each getItems() triggers a separate
    // SELECT.

    // Fix is to use JOIN FETCH:
    // List<OrderEntity> orders =
    // em.createQuery(
    // "SELECT o FROM OrderEntity o LEFT JOIN FETCH o.items",
    // OrderEntity.class
    // ).getResultList();

    // 🧠 Difference Between SQL JOIN and JPA Fetch Join
    // Type Purpose
    // SQL JOIN Combine tables
    // Fetch Join Tell Hibernate to eagerly load relationship

}
