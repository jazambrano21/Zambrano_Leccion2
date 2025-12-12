package com.espe.purchaseorder.services;

import com.espe.purchaseorder.models_entities.PurchaseOrder;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Interfaz del servicio para la lógica de negocio CRUD de PurchaseOrder.
 */
public interface PurchaseOrderService {
    // Guarda una orden de compra
    <S extends PurchaseOrder> S save(S entity);

    // Guarda una lista de órdenes de compra
    <S extends PurchaseOrder> Iterable<S> saveAll(Iterable<S> entities);

    // Busca una orden de compra por ID
    Optional<PurchaseOrder> findById(Long id);

    // Busca una orden de compra por número de orden
    Optional<PurchaseOrder> findByOrderNumber(String orderNumber);

    // Verifica si existe una orden de compra por ID
    boolean existsById(Long id);

    // Verifica si existe una orden de compra por número de orden
    boolean existsByOrderNumber(String orderNumber);

    // Obtiene todas las órdenes de compra
    Iterable<PurchaseOrder> findAll();

    // Obtiene todas las órdenes de compra por una lista de IDs
    Iterable<PurchaseOrder> findAllById(Iterable<Long> ids);

    // Obtiene órdenes de compra por estado
    List<PurchaseOrder> findByStatus(PurchaseOrder.OrderStatus status);

    // Obtiene órdenes de compra por nombre de proveedor (contiene)
    List<PurchaseOrder> findBySupplierNameContaining(String supplierName);

    // Obtiene órdenes de compra por rango de fechas de entrega estimada
    List<PurchaseOrder> findByExpectedDeliveryDateBetween(LocalDate startDate, LocalDate endDate);

    // Cuenta el total de órdenes de compra
    long count();

    // Cuenta órdenes de compra por estado
    long countByStatus(PurchaseOrder.OrderStatus status);

    // Búsqueda con filtros combinados
    List<PurchaseOrder> findWithFilters(String q, PurchaseOrder.OrderStatus status, PurchaseOrder.Currency currency, 
                                        java.math.BigDecimal minTotal, java.math.BigDecimal maxTotal, 
                                        java.time.LocalDateTime from, java.time.LocalDateTime to);

    // Elimina una orden de compra por ID
    void deleteById(Long id);

    // Elimina una orden de compra por entidad
    void delete(PurchaseOrder entity);

    // Elimina todas las órdenes de compra por una lista de IDs
    void deleteAllById(Iterable<? extends Long> ids);

    // Elimina todas las órdenes de compra por una lista de entidades
    void deleteAll(Iterable<? extends PurchaseOrder> entities);

    // Elimina todas las órdenes de compra
    void deleteAll();
}
