db = db.getSiblingDB("tariff_database");

db.createUser({
  user: "tariff-service",
  pwd: "tariff-service",
  roles: [{ role: "readWrite", db: "tariff_database" }]
});

db.createCollection("tariffs");
db.createCollection("currency_rates");