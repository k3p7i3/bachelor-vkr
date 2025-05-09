db = db.getSiblingDB("order_database");

db.createUser({
  user: "order-service",
  pwd: "order-service",
  roles: [{ role: "readWrite", db: "order_database" }]
});

db.createCollection("orders");