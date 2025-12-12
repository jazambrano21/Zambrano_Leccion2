package com.espe.purchaseorder.repositories;

import com.espe.purchaseorder.models_entities.PurchaseOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Long> {

    Optional<PurchaseOrder> findByOrderNumber(String orderNumber);

    boolean existsByOrderNumber(String orderNumber);

    List<PurchaseOrder> findByStatus(PurchaseOrder.OrderStatus status);

    List<PurchaseOrder> findBySupplierNameContainingIgnoreCase(String supplierName);

    @Query("SELECT po FROM PurchaseOrder po WHERE po.expectedDeliveryDate BETWEEN :startDate AND :endDate")
    List<PurchaseOrder> findByExpectedDeliveryDateBetween(@Param("startDate") java.time.LocalDate startDate, 
                                                         @Param("endDate") java.time.LocalDate endDate);

    @Query("SELECT COUNT(po) FROM PurchaseOrder po WHERE po.status = :status")
    long countByStatus(@Param("status") PurchaseOrder.OrderStatus status);

    // Filtros combinados
    @Query("SELECT po FROM PurchaseOrder po WHERE " +
           "(:q IS NULL OR :q = '' OR LOWER(po.orderNumber) LIKE LOWER(CONCAT('%', :q, '%')) OR LOWER(po.supplierName) LIKE LOWER(CONCAT('%', :q, '%'))) AND " +
           "(:status IS NULL OR po.status = :status) AND " +
           "(:currency IS NULL OR po.currency = :currency) AND " +
           "(:minTotal IS NULL OR po.totalAmount >= :minTotal) AND " +
           "(:maxTotal IS NULL OR po.totalAmount <= :maxTotal) AND " +
           "(:from IS NULL OR po.createdAt >= :from) AND " +
           "(:to IS NULL OR po.createdAt <= :to)")
    List<PurchaseOrder> findWithFilters(@Param("q") String q,
                                       @Param("status") PurchaseOrder.OrderStatus status,
                                       @Param("currency") PurchaseOrder.Currency currency,
                                       @Param("minTotal") BigDecimal minTotal,
                                       @Param("maxTotal") BigDecimal maxTotal,
                                       @Param("from") LocalDateTime from,
                                       @Param("to") LocalDateTime to);
}
