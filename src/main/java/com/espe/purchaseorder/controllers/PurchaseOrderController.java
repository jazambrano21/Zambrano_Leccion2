package com.espe.purchaseorder.controllers;

import com.espe.purchaseorder.models_entities.PurchaseOrder;
import com.espe.purchaseorder.services.PurchaseOrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/purchase-orders")
public class PurchaseOrderController {

    private final PurchaseOrderService purchaseOrderService;

    @Autowired
    public PurchaseOrderController(PurchaseOrderService purchaseOrderService) {
        this.purchaseOrderService = purchaseOrderService;
    }

    @GetMapping
    public ResponseEntity<List<PurchaseOrder>> getAllPurchaseOrders(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) PurchaseOrder.OrderStatus status,
            @RequestParam(required = false) PurchaseOrder.Currency currency,
            @RequestParam(required = false) java.math.BigDecimal minTotal,
            @RequestParam(required = false) java.math.BigDecimal maxTotal,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) java.time.LocalDateTime from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) java.time.LocalDateTime to) {
        
        // Validaciones
        if (minTotal != null && minTotal.compareTo(java.math.BigDecimal.ZERO) < 0) {
            return ResponseEntity.badRequest().body(null);
        }
        if (maxTotal != null && maxTotal.compareTo(java.math.BigDecimal.ZERO) < 0) {
            return ResponseEntity.badRequest().body(null);
        }
        if (from != null && to != null && from.isAfter(to)) {
            return ResponseEntity.badRequest().body(null);
        }
        
        List<PurchaseOrder> orders;
        
        // Si hay filtros, usar búsqueda con filtros
        if (q != null || status != null || currency != null || minTotal != null || maxTotal != null || from != null || to != null) {
            orders = purchaseOrderService.findWithFilters(q, status, currency, minTotal, maxTotal, from, to);
        } else {
            // Si no hay filtros, traer todos
            orders = (List<PurchaseOrder>) purchaseOrderService.findAll();
        }
        
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PurchaseOrder> getPurchaseOrderById(@PathVariable Long id) {
        Optional<PurchaseOrder> order = purchaseOrderService.findById(id);
        return order.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                  .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/order-number/{orderNumber}")
    public ResponseEntity<PurchaseOrder> getPurchaseOrderByOrderNumber(@PathVariable String orderNumber) {
        Optional<PurchaseOrder> order = purchaseOrderService.findByOrderNumber(orderNumber);
        return order.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                  .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<?> createPurchaseOrder(@Valid @RequestBody PurchaseOrder purchaseOrder) {
        try {
            // Validar que el número de orden no exista
            if (purchaseOrder.getOrderNumber() != null && 
                purchaseOrderService.existsByOrderNumber(purchaseOrder.getOrderNumber())) {
                return ResponseEntity.badRequest().body("ERROR: El número de orden " + purchaseOrder.getOrderNumber() + " ya existe");
            }

            PurchaseOrder savedOrder = purchaseOrderService.save(purchaseOrder);
            return new ResponseEntity<>(savedOrder, HttpStatus.CREATED);
        } catch (Exception e) {
            System.out.println("Error al crear orden de compra: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().body("ERROR: Falló al crear la orden de compra - " + e.getMessage());
        }
    }

    @GetMapping("/test/create")
    public ResponseEntity<?> createTestPurchaseOrder() {
        try {
            PurchaseOrder order = new PurchaseOrder();
            order.setOrderNumber("PO-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MMdd-HHmmss")));
            order.setSupplierName("ACME Tools");
            order.setStatus(PurchaseOrder.OrderStatus.DRAFT);
            order.setTotalAmount(new BigDecimal("1500.00"));
            order.setCurrency(PurchaseOrder.Currency.USD);
            order.setExpectedDeliveryDate(LocalDate.now().plusWeeks(2));
            
            PurchaseOrder savedOrder = purchaseOrderService.save(order);
            return new ResponseEntity<>(savedOrder, HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePurchaseOrder(@PathVariable Long id, @Valid @RequestBody PurchaseOrder purchaseOrder) {
        if (!purchaseOrderService.existsById(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // Validar que el número de orden no exista (si es diferente al actual)
        if (purchaseOrder.getOrderNumber() != null) {
            Optional<PurchaseOrder> existingOrder = purchaseOrderService.findByOrderNumber(purchaseOrder.getOrderNumber());
            if (existingOrder.isPresent() && !existingOrder.get().getId().equals(id)) {
                return ResponseEntity.badRequest().body("ERROR: El número de orden " + purchaseOrder.getOrderNumber() + " ya existe");
            }
        }

        purchaseOrder.setId(id);
        PurchaseOrder updatedOrder = purchaseOrderService.save(purchaseOrder);
        return new ResponseEntity<>(updatedOrder, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePurchaseOrder(@PathVariable Long id) {
        if (!purchaseOrderService.existsById(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        purchaseOrderService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/count")
    public ResponseEntity<Long> countPurchaseOrders() {
        long count = purchaseOrderService.count();
        return new ResponseEntity<>(count, HttpStatus.OK);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<PurchaseOrder>> getPurchaseOrdersByStatus(@PathVariable PurchaseOrder.OrderStatus status) {
        List<PurchaseOrder> orders = purchaseOrderService.findByStatus(status);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @GetMapping("/supplier/{supplierName}")
    public ResponseEntity<List<PurchaseOrder>> getPurchaseOrdersBySupplier(@PathVariable String supplierName) {
        List<PurchaseOrder> orders = purchaseOrderService.findBySupplierNameContaining(supplierName);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @GetMapping("/delivery-date-range")
    public ResponseEntity<List<PurchaseOrder>> getPurchaseOrdersByDeliveryDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        
        if (startDate.isAfter(endDate)) {
            return ResponseEntity.badRequest().build();
        }
        
        List<PurchaseOrder> orders = purchaseOrderService.findByExpectedDeliveryDateBetween(startDate, endDate);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @GetMapping("/count/status/{status}")
    public ResponseEntity<Long> countPurchaseOrdersByStatus(@PathVariable PurchaseOrder.OrderStatus status) {
        long count = purchaseOrderService.countByStatus(status);
        return new ResponseEntity<>(count, HttpStatus.OK);
    }
}
