package com.example.paymentapp.order;

import com.example.paymentapp.order.OrderEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}