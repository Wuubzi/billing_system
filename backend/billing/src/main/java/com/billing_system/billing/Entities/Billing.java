package com.billing_system.billing.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "billings")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Billing {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_billing;
    @Column(name = "status")
    private String status = "Generated";
    @Column(name = "created_at")
    private Date created_at = new Date();

    @ManyToMany
    @JoinTable(name = "orders_billings",
            joinColumns = @JoinColumn(name = "id_billing"),
            inverseJoinColumns = @JoinColumn(name = "id_order")
    )
    @ToString.Exclude
    private List<Orders> orders = new ArrayList<>();
    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Billing billing = (Billing) o;
        return getId_billing() != null && Objects.equals(getId_billing(), billing.getId_billing());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
