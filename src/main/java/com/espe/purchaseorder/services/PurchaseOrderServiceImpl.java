package com.espe.purchaseorder.services;

import com.espe.purchaseorder.models_entities.PurchaseOrder;
import com.espe.purchaseorder.repositories.PurchaseOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Implementación del servicio para la lógica de negocio CRUD de PurchaseOrder.
 */
@Service
@Transactional
public class PurchaseOrderServiceImpl implements PurchaseOrderService {

    private final PurchaseOrderRepository purchaseOrderRepository;

    @Autowired
    public PurchaseOrderServiceImpl(PurchaseOrderRepository purchaseOrderRepository) {
        this.purchaseOrderRepository = purchaseOrderRepository;
    }

    @Override
    public <S extends PurchaseOrder> S save(S entity) {
        return purchaseOrderRepository.save(entity);
    }

    @Override
    public <S extends PurchaseOrder> Iterable<S> saveAll(Iterable<S> entities) {
        return purchaseOrderRepository.saveAll(entities);
    }

    @Override
    public Optional<PurchaseOrder> findById(Long id) {
        return purchaseOrderRepository.findById(id);
    }

    @Override
    public Optional<PurchaseOrder> findByOrderNumber(String orderNumber) {
        return purchaseOrderRepository.findByOrderNumber(orderNumber);
    }

    @Override
    public boolean existsById(Long id) {
        return purchaseOrderRepository.existsById(id);
    }

    @Override
    public boolean existsByOrderNumber(String orderNumber) {
        return purchaseOrderRepository.existsByOrderNumber(orderNumber);
    }

    @Override
    public Iterable<PurchaseOrder> findAll() {
        return purchaseOrderRepository.findAll();
    }

    @Override
    public Iterable<PurchaseOrder> findAllById(Iterable<Long> ids) {
        return purchaseOrderRepository.findAllById(ids);
    }

    @Override
    public List<PurchaseOrder> findByStatus(PurchaseOrder.OrderStatus status) {
        return purchaseOrderRepository.findByStatus(status);
    }

    @Override
    public List<PurchaseOrder> findBySupplierNameContaining(String supplierName) {
        return purchaseOrderRepository.findBySupplierNameContainingIgnoreCase(supplierName);
    }

    @Override
    public List<PurchaseOrder> findByExpectedDeliveryDateBetween(LocalDate startDate, LocalDate endDate) {
        return purchaseOrderRepository.findByExpectedDeliveryDateBetween(startDate, endDate);
    }

    @Override
    public long count() {
        return purchaseOrderRepository.count();
    }

    @Override
    public long countByStatus(PurchaseOrder.OrderStatus status) {
        return purchaseOrderRepository.countByStatus(status);
    }

    @Override
    public List<PurchaseOrder> findWithFilters(String q, PurchaseOrder.OrderStatus status, PurchaseOrder.Currency currency, 
                                               java.math.BigDecimal minTotal, java.math.BigDecimal maxTotal, 
                                               java.time.LocalDateTime from, java.time.LocalDateTime to) {
        return purchaseOrderRepository.findWithFilters(q, status, currency, minTotal, maxTotal, from, to);
    }

    @Override
    public void deleteById(Long id) {
        purchaseOrderRepository.deleteById(id);
    }

    @Override
    public void delete(PurchaseOrder entity) {
        purchaseOrderRepository.delete(entity);
    }

    @Override
    public void deleteAllById(Iterable<? extends Long> ids) {
        purchaseOrderRepository.deleteAllById(ids);
    }

    @Override
    public void deleteAll(Iterable<? extends PurchaseOrder> entities) {
        purchaseOrderRepository.deleteAll(entities);
    }

    @Override
    public void deleteAll() {
        purchaseOrderRepository.deleteAll();
    }
}
