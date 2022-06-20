package proyecto.vertx;
 
import io.vertx.core.Future;
import io.netty.handler.codec.mqtt.MqttConnectReturnCode;
import io.netty.handler.codec.mqtt.MqttQoS;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.mqtt.MqttClient;
import io.vertx.mqtt.MqttClientOptions;
import io.vertx.mqtt.impl.MqttClientImpl;
import io.vertx.mysqlclient.MySQLClient;
import io.vertx.mysqlclient.MySQLConnectOptions;
import io.vertx.mysqlclient.MySQLPool;
import io.vertx.sqlclient.PoolOptions;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;
import io.vertx.sqlclient.Tuple;

public class DBVerticle extends AbstractVerticle{
	 
	MySQLPool mySqlClient;
	MqttClient mqttClient;
	
	public void start(Promise<Void> startFuture) {
		MySQLConnectOptions connectOptions = new MySQLConnectOptions().setPort(3306).setHost("localhost")
				.setDatabase("dad_radar").setUser("root").setPassword("root");

		PoolOptions poolOptions = new PoolOptions().setMaxSize(5);

		mySqlClient = MySQLPool.pool(vertx, connectOptions, poolOptions);
		
		Router router = Router.router(vertx);
		router.route().handler(BodyHandler.create());
		
		vertx.createHttpServer().requestHandler(router::handle).listen(4040, result -> {
			if(result.succeeded()) {
				startFuture.complete();
			}else {
				startFuture.fail(result.cause());
				
				
			} 
		});
		
		//Velocidad
		router.get("/api/velocidad/").handler(this::getVelocidad); // ver todas las velocidades
		router.get("/api/velocidad/:IdVelocidad").handler(this::getVelocidadInfo); // ver velocidades según id
		router.post("/api/velocidad/registrar").handler(this::postVelocidad); //registrar velocidad
		router.delete("/api/velocidad/eliminar").handler(this::deleteVelocidad); // borrar velocidades
		
		
		//Sensores
		router.get("/api/sensor/").handler(this::getSensores); //ver sensores
		router.post("/api/sensor/registrar").handler(this::postSensor); //registrar sensores
		router.put("/api/sensor/actualizarEstado/:IdSensor").handler(this::putSensor);//actualizar estado sensor
		router.delete("/api/sensor/eliminar/:IdSensor").handler(this::deleteSensor);
		
		
		//Administrador
		router.get("/api/administrador/").handler(this::getAdministradorInfo);	//info administrador
		router.put("/api/administrador/actualizarPassword").handler(this::putAdministrador);//actualizar password
		router.put("/api/administrador/actualizarUsername").handler(this::putAdministrador2);//actualizar username	
		router.post("/api/login").handler(this::postLogin); // Login
		
		
		//Led
		router.get("/api/led/").handler(this::getLeds); //ver sensores
		router.post("/api/led/registrar").handler(this::postLed); //registrar sensores
		router.put("/api/led/actualizarEstadoLed/:IdLed").handler(this::putLed); //Actualizar estado led
		router.delete("/api/led/eliminar/:IdLed").handler(this::deleteLed); // Borrar led
		

	}
	
	//------------------------------VELOCIDAD---------------------------------------------------------
	
	private void getVelocidad(RoutingContext routingContext) {
		mySqlClient.query("SELECT * FROM dad_radar.velocidad", 
				res -> {
					if (res.succeeded()) {	
						RowSet<Row> resultSet = res.result();
						JsonArray result = new JsonArray();
						
						for (Row row : resultSet) {
							result.add(JsonObject.mapFrom(new VelocidadImpl(row.getInteger("IdVelocidad"),
									row.getDouble("Speed"),
									row.getValue("Fecha").toString())));
							
						}
						routingContext.response().setStatusCode(200).putHeader("content-type", "application/json")
						.end(result.encodePrettily());
						}else {
							routingContext.response().setStatusCode(401).putHeader("content-type", "application/json")
							.end((JsonObject.mapFrom(res.cause()).encodePrettily()));
					}
				});
	}
	
	
	
	
	private void getVelocidadInfo(RoutingContext routingContext) {
		mySqlClient.query("SELECT * FROM dad_radar.velocidad WHERE IdVelocidad = '" + routingContext.request().getParam("IdVelocidad") + "'", 
				res -> {
					if (res.succeeded()) {	
						RowSet<Row> resultSet = res.result();
						JsonArray result = new JsonArray();
						
						for (Row row : resultSet) {
							result.add(JsonObject.mapFrom(new VelocidadImpl(row.getInteger("IdVelocidad"),
									row.getDouble("Speed"),
									row.getValue("Fecha").toString())));
							
						}
						routingContext.response().setStatusCode(200).putHeader("content-type", "application/json")
						.end(result.encodePrettily());
						}else {
							routingContext.response().setStatusCode(401).putHeader("content-type", "application/json")
							.end((JsonObject.mapFrom(res.cause()).encodePrettily()));
					}
				});
	}
	
	
	private void postVelocidad(RoutingContext routingContext) {
		VelocidadImpl velocidad = Json.decodeValue(routingContext.getBodyAsString(), VelocidadImpl.class);	
		
		mySqlClient.preparedQuery("INSERT INTO dad_radar.velocidad (IdVelocidad, Speed, Fecha) VALUES (?,?,?)",
				Tuple.of(velocidad.getIdVelocidad(),velocidad.getSpeed(), velocidad.getFecha()),
				handler -> {
					
					if (handler.succeeded()) {
						
						routingContext.response().setStatusCode(200).putHeader("content-type", "application/json")
								.end(JsonObject.mapFrom(velocidad).encodePrettily());
						
						}else {
							routingContext.response().setStatusCode(401).putHeader("content-type", "application/json")
							.end((JsonObject.mapFrom(handler.cause()).encodePrettily()));
					}
				});
	} 
	
	
	private void deleteVelocidad(RoutingContext routingContext) {
		
		mySqlClient.query("DELETE FROM dad_radar.velocidad WHERE Speed <= 25",
				handler -> {			
				 if (handler.succeeded()) {						
						routingContext.response().setStatusCode(200).putHeader("content-type", "application/json")
								.end("Velocidad/es eliminado correctamente");
						
						}else {
							routingContext.response().setStatusCode(401).putHeader("content-type", "application/json")
							.end((JsonObject.mapFrom(handler.cause()).encodePrettily()));
					}
				});
	
	}
	
	//--------------------------------ADMINISTRADOR------------------------------------------
	
	private void getAdministradorInfo(RoutingContext routingContext) {
		mySqlClient.query("SELECT * FROM dad_radar.administrador" , 
				res -> {
					if (res.succeeded()) {	
						RowSet<Row> resultSet = res.result();
						JsonArray result = new JsonArray();
						
						for (Row row : resultSet) {
							result.add(JsonObject.mapFrom(new AdministradorImpl(row.getInteger("IdAdministrador"),
									row.getString("Username"),
									row.getString("Password"))));
							
						}
						routingContext.response().setStatusCode(200).putHeader("content-type", "application/json")
						.end(result.encodePrettily());
						}else {
							routingContext.response().setStatusCode(401).putHeader("content-type", "application/json")
							.end((JsonObject.mapFrom(res.cause()).encodePrettily()));
					}
				});
	}
	
	private void postLogin(RoutingContext routingContext) { 
		mySqlClient.query( "SELECT * FROM dad_radar.administrador WHERE Username = '" + routingContext.getBodyAsJson().getString("username") + 
				"' AND Password = '" + routingContext.getBodyAsJson().getString("password") + "'", 
				res -> {
					if (res.succeeded()) {
						if(res.result().size() == 1) {
							routingContext.response().setStatusCode(200).putHeader("content-type", "application/json")
							.end("Sesión iniciada");
						}else {
							routingContext.response().setStatusCode(201).putHeader("content-type", "application/json")
							.end("Error! Usuario o contraseña incorrectos");
						}
					}else {
						routingContext.response().setStatusCode(401).putHeader("content-type", "application/json")
						.end("Error!");
					}
				});
	}
	
	private void putAdministrador(RoutingContext routingContext) {
		AdministradorImpl administrador = Json.decodeValue(routingContext.getBodyAsString(), AdministradorImpl.class);
		mySqlClient.preparedQuery(
				"UPDATE dad_radar.administrador SET Password = ?",
				Tuple.of(administrador.getPassword()),
				handler -> {
					if (handler.succeeded()) {
						routingContext.response().setStatusCode(200).putHeader("content-type", "application/json")
								.end(JsonObject.mapFrom(administrador).encodePrettily());
					} else {
						routingContext.response().setStatusCode(401).putHeader("content-type", "application/json")
								.end((JsonObject.mapFrom(handler.cause()).encodePrettily()));
					}
				});
	}
	
	private void putAdministrador2(RoutingContext routingContext) { 
		AdministradorImpl administrador = Json.decodeValue(routingContext.getBodyAsString(), AdministradorImpl.class);
		mySqlClient.preparedQuery(
				"UPDATE dad_radar.administrador SET Username = ?",
				Tuple.of(administrador.getUsername()),
				handler -> {
					if (handler.succeeded()) {
						routingContext.response().setStatusCode(200).putHeader("content-type", "application/json")
								.end(JsonObject.mapFrom(administrador).encodePrettily());
					} else {
						routingContext.response().setStatusCode(401).putHeader("content-type", "application/json")
								.end((JsonObject.mapFrom(handler.cause()).encodePrettily()));
					}
				});
	}
	
	
	
	//---------------------------------------SENSORES---------------------------------------------------
	
	private void getSensores(RoutingContext routingContext) {
		mySqlClient.query("SELECT * FROM dad_radar.sensor", 
				res -> {
					if (res.succeeded()) {	
						RowSet<Row> resultSet = res.result();
						JsonArray result = new JsonArray();
						
						for (Row row : resultSet) {
							result.add(JsonObject.mapFrom(new SensorImpl(row.getInteger("IdSensor"),
									row.getString("Nombre"),
									row.getInteger("Estado"))));
							
						}
						routingContext.response().setStatusCode(200).putHeader("content-type", "application/json")
						.end(result.encodePrettily());
						}else {
							routingContext.response().setStatusCode(401).putHeader("content-type", "application/json")
							.end((JsonObject.mapFrom(res.cause()).encodePrettily()));
					}
				});
	}
	
	private void postSensor(RoutingContext routingContext) {
		SensorImpl sensor = Json.decodeValue(routingContext.getBodyAsString(), SensorImpl.class);	
		
		mySqlClient.preparedQuery("INSERT INTO dad_radar.sensor (IdSensor, Nombre, Estado) VALUES (?,?,?)",
				Tuple.of(sensor.getIdSensor(),sensor.getNombre(), sensor.getEstado()),
				handler -> {
					
					if (handler.succeeded()) {
						
						routingContext.response().setStatusCode(200).putHeader("content-type", "application/json")
								.end(JsonObject.mapFrom(sensor).encodePrettily());
						
						}else {
							routingContext.response().setStatusCode(401).putHeader("content-type", "application/json")
							.end((JsonObject.mapFrom(handler.cause()).encodePrettily()));
					}
				});
	}
	
	private void putSensor(RoutingContext routingContext) {
		SensorImpl led = Json.decodeValue(routingContext.getBodyAsString(), SensorImpl.class);
		mySqlClient.preparedQuery(
				"UPDATE dad_radar.sensor SET Estado = ? WHERE IdSensor = " + routingContext.request().getParam("IdSensor"),
				Tuple.of(led.getEstado()),
				handler -> {
					if (handler.succeeded()) {
						routingContext.response().setStatusCode(200).putHeader("content-type", "application/json")
								.end(JsonObject.mapFrom(led).encodePrettily());
					} else {
						routingContext.response().setStatusCode(401).putHeader("content-type", "application/json")
								.end((JsonObject.mapFrom(handler.cause()).encodePrettily()));
					}
				});
	}
	
	private void deleteSensor(RoutingContext routingContext) {
		
		mySqlClient.query("DELETE FROM dad_radar.sensor WHERE IdSensor =  " + routingContext.request().getParam("IdSensor"),
				handler -> {
					
					if (handler.succeeded()) {						
						routingContext.response().setStatusCode(200).putHeader("content-type", "application/json")
								.end("Sensor eliminado correctamente");
						
						}else {
							routingContext.response().setStatusCode(401).putHeader("content-type", "application/json")
							.end((JsonObject.mapFrom(handler.cause()).encodePrettily()));
					}
				});
	}
	

	//-----------------------------------------LEDS----------------------------------------

	
	private void getLeds(RoutingContext routingContext) {
		mySqlClient.query("SELECT * FROM dad_radar.led", 
				res -> {
					if (res.succeeded()) {	
						RowSet<Row> resultSet = res.result();
						JsonArray result = new JsonArray();
						
						for (Row row : resultSet) {
							result.add(JsonObject.mapFrom(new LedImpl(row.getInteger("IdLed"),
									row.getString("Nombre"),
									row.getInteger("Estado"))));
							
						}
						routingContext.response().setStatusCode(200).putHeader("content-type", "application/json")
						.end(result.encodePrettily());
						}else {
							routingContext.response().setStatusCode(401).putHeader("content-type", "application/json")
							.end((JsonObject.mapFrom(res.cause()).encodePrettily()));
					}
				});
	}
	
	private void postLed(RoutingContext routingContext) {
		LedImpl led = Json.decodeValue(routingContext.getBodyAsString(), LedImpl.class);	
		
		mySqlClient.preparedQuery("INSERT INTO dad_radar.led (IdLed, Nombre, Estado) VALUES (?,?,?)",
				Tuple.of(led.getIdLed(),led.getNombre(), led.getEstado()),
				handler -> {
					
					if (handler.succeeded()) {
						
						routingContext.response().setStatusCode(200).putHeader("content-type", "application/json")
								.end(JsonObject.mapFrom(led).encodePrettily());
						
						}else {
							routingContext.response().setStatusCode(401).putHeader("content-type", "application/json")
							.end((JsonObject.mapFrom(handler.cause()).encodePrettily()));
					}
				});
	}
	
	private void putLed(RoutingContext routingContext) {
		LedImpl led = Json.decodeValue(routingContext.getBodyAsString(), LedImpl.class);
		mySqlClient.preparedQuery(
				"UPDATE dad_radar.led SET Estado = ? WHERE IdLed = " + routingContext.request().getParam("IdLed"),
				Tuple.of(led.getEstado()),
				handler -> {
					if (handler.succeeded()) {
						routingContext.response().setStatusCode(200).putHeader("content-type", "application/json")
								.end(JsonObject.mapFrom(led).encodePrettily());
					} else {
						routingContext.response().setStatusCode(401).putHeader("content-type", "application/json")
								.end((JsonObject.mapFrom(handler.cause()).encodePrettily()));
					}
				});
	}
	
private void deleteLed(RoutingContext routingContext) {
		
		mySqlClient.query("DELETE FROM dad_radar.led WHERE IdLed =  " + routingContext.request().getParam("IdLed"),
				handler -> {
					
					if (handler.succeeded()) {						
						routingContext.response().setStatusCode(200).putHeader("content-type", "application/json")
								.end("Led eliminado correctamente");
						
						}else {
							routingContext.response().setStatusCode(401).putHeader("content-type", "application/json")
							.end((JsonObject.mapFrom(handler.cause()).encodePrettily()));
					}
				});
	}
	

	@Override
	public void stop(Future<Void> stopFuture) throws Exception {
		super.stop(stopFuture);
	}
		

}
