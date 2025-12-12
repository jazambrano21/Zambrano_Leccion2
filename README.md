EJECUTAR LA IMAGEN DOCKER
   docker run -p 8005:8005 -e DB_HOST=host.docker.internal -e DB_PORT=3306 -e DB_NAME=sisdb2026_purchase -e DB_USERNAME=root -e DB_PASSWORD=1234 jazambrano21/purchase-order:latest

   O si quieres ejecutarla en segundo plano:
   docker run -d -p 8005:8005 -e DB_HOST=host.docker.internal -e DB_PORT=3306 -e DB_NAME=sisdb2026_purchase -e DB_USERNAME=root -e DB_PASSWORD=1234 --name purchase-order jazambrano21/purchase-order:latest

5. VERIFICAR QUE ESTÁ CORRIENDO
   docker ps

================================================================================
ENDPOINTS PARA PROBAR EN POSTMAN
================================================================================

BASE URL: http://localhost:8005

1. CREAR ORDEN DE PRUEBA (GET)
   Método: GET
   URL: http://localhost:8005/api/v1/purchase-orders/test/create

2. OBTENER TODAS LAS ÓRDENES (GET)
   Método: GET
   URL: http://localhost:8005/api/v1/purchase-orders

3. OBTENER ORDEN POR ID (GET)
   Método: GET
   URL: http://localhost:8005/api/v1/purchase-orders/1

4. CREAR NUEVA ORDEN (POST)
   Método: POST
   URL: http://localhost:8005/api/v1/purchase-orders
   Headers: Content-Type: application/json
   Body (raw JSON):
   {
     "orderNumber": "PO-2025-001",
     "supplierName": "Proveedor ABC",
     "status": "DRAFT",
     "totalAmount": 2500.50,
     "currency": "USD",
     "expectedDeliveryDate": "2025-02-15"
   }

5. ACTUALIZAR ORDEN (PUT)
   Método: PUT
   URL: http://localhost:8005/api/v1/purchase-orders/1
   Headers: Content-Type: application/json
   Body (raw JSON):
   {
     "orderNumber": "PO-2025-001",
     "supplierName": "Proveedor Actualizado",
     "status": "APPROVED",
     "totalAmount": 3000.00,
     "currency": "USD",
     "expectedDeliveryDate": "2025-02-20"
   }

6. ELIMINAR ORDEN (DELETE)
   Método: DELETE
   URL: http://localhost:8005/api/v1/purchase-orders/1

7. CONTAR ÓRDENES (GET)
   Método: GET
   URL: http://localhost:8005/api/v1/purchase-orders/count

8. BUSCAR POR ESTADO (GET)
   Método: GET
   URL: http://localhost:8005/api/v1/purchase-orders/status/DRAFT

9. BUSCAR POR PROVEEDOR (GET)
   Método: GET
   URL: http://localhost:8005/api/v1/purchase-orders/supplier/ACME

10. BUSCAR CON FILTROS (GET)
    Método: GET
    URL: http://localhost:8005/api/v1/purchase-orders?status=DRAFT&minTotal=1000&maxTotal=5000

11. BUSCAR POR RANGO DE FECHAS (GET)
    Método: GET
    URL: http://localhost:8005/api/v1/purchase-orders/delivery-date-range?startDate=2025-02-01&endDate=2025-02-28
