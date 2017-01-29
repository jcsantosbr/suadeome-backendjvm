package com.jcs.suadeome.main;

import com.google.gson.Gson;

import com.jcs.suadeome.generators.EntityType;
import com.jcs.suadeome.generators.IdGenerator;
import com.jcs.suadeome.types.Id;

import org.jetbrains.annotations.NotNull;
import org.postgresql.ds.PGPoolingDataSource;
import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.Handle;

import java.util.Date;
import java.util.List;
import java.util.Map;

import spark.ResponseTransformer;
import spark.Spark;

import static java.lang.Integer.parseInt;
import static java.util.Optional.ofNullable;
import static spark.Spark.get;
import static spark.Spark.post;

public class App {

  private static ResponseTransformer toJson = model -> new Gson().toJson(model);

  public static void main(String[] args) {
    configureSpark();

    DBI dbi = configureDB();

    defineRoutes(dbi);
  }

  private static void configureSpark() {
    String ipAddress = getEnv("OPENSHIFT_DIY_IP", "localhost");
    int port = getIntEnv("OPENSHIFT_DIY_PORT",4567);

    Spark.ipAddress(ipAddress);
    Spark.port(port);
  }

  private static void defineRoutes(DBI dbi) {

    IdGenerator generator = new IdGenerator(() -> "" + System.nanoTime());

    get("/professionals", (request, response) -> {
      Handle h = dbi.open();
      List<Map<String, Object>> allProfessionals = h.createQuery("SELECT * FROM professionals ORDER BY name").list();
      h.close();

      return allProfessionals;
    }, toJson);

    post("/professionals", (request, response) -> {

      try {

        Handle h = dbi.open();

        Id id = generator.generate(EntityType.PROFESSIONAL);
        String name = request.queryParams("name");
        String phone = request.queryParams("phone");
        String service = request.queryParams("service");

        h.createStatement("INSERT INTO professionals (id, name, phone, service, created_by, created_at) " +
            "VALUES (:id, :name, :phone, :service, :user, :now)")
            .bind("id", id.getValue())
            .bind("name", name)
            .bind("phone", phone)
            .bind("service", service)
            .bind("user", 1)
            .bind("now", new Date())
            .execute();

        h.close();
      } catch (Exception e) {
        e.printStackTrace();
      }

      return response;
    });
  }

  @NotNull
  private static DBI configureDB() {
    PGPoolingDataSource connectionPool = new PGPoolingDataSource();
    connectionPool.setApplicationName("suadeome");
    connectionPool.setServerName( getEnv("OPENSHIFT_POSTGRESQL_DB_HOST",  "localhost"));
    connectionPool.setPortNumber( getIntEnv("OPENSHIFT_POSTGRESQL_DB_PORT", 5432));
    connectionPool.setDatabaseName("suadeome");
    connectionPool.setUser( getEnv("OPENSHIFT_POSTGRESQL_DB_APP_USER", "suadeome"));
    connectionPool.setPassword(getEnv("OPENSHIFT_POSTGRESQL_DB_APP_PASSWORD", "suadeome"));
    connectionPool.setMaxConnections(10);

    return new DBI(connectionPool);
  }

  private static int getIntEnv(String property, int defaultValue) {
    return parseInt(ofNullable(System.getenv(property)).orElse(Integer.toString(defaultValue)));
  }

  private static String getEnv(String property, String defaultValue) {
    return ofNullable(System.getenv(property)).orElse(defaultValue);
  }

}

