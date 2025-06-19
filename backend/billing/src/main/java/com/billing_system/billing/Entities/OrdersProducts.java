package com.billing_system.billing.Entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import java.util.Objects;

@Entity
@Table(name = "product_orders")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class OrdersProducts {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_product_orders;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_product")
    private Products products;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_order")
    @ToString.Exclude
    @JsonIgnore
    private Orders orders;
    @Column(name = "quantity")
    private Integer quantity;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        OrdersProducts that = (OrdersProducts) o;
        return getId_product_orders() != null && Objects.equals(getId_product_orders(), that.getId_product_orders());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
