package proyecto.vertx;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import io.netty.handler.codec.mqtt.MqttQoS;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.mqtt.MqttClient;
import io.vertx.mqtt.MqttClientOptions;

public class MqttVerticle extends AbstractVerticle {
	

	private Map<Integer, VelocidadImpl> users = new HashMap<Integer, VelocidadImpl>();
	//private Map<Integer, SensorImpl> sensors = new HashMap<Integer, SensorImpl>();
	private Gson gson;
	private MqttClient mqttClient;

	public void start(Promise<Void> startFuture){

		gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();

		mqttClient = MqttClient.create(getVertx(),
				new MqttClientOptions().setAutoKeepAlive(true).setUsername("admin").setPassword("admin"));
		mqttClient.connect(1883, "localhost", connection -> {
			if (connection.succeeded()) {
				System.out.println("Client name: " + connection.result().code().name());
				mqttClient.subscribe("/api/velocidad/registrar", MqttQoS.AT_LEAST_ONCE.value(), handler -> {
					if (handler.succeeded()) {
						System.out.println("Client has been subscribed to Velocidad");
					}
				});

				mqttClient.subscribe("/api/sensor/actualizarEstado/1", MqttQoS.AT_LEAST_ONCE.value(), handler -> {
					if (handler.succeeded()) {
						System.out.println("Client has been subscribed to Sensor1");
					}
				});
				
				mqttClient.subscribe("/api/sensor/actualizarEstado/2", MqttQoS.AT_LEAST_ONCE.value(), handler -> {
					if (handler.succeeded()) {
						System.out.println("Client has been subscribed to Sensor2");
					}
				});
				mqttClient.subscribe("/api/sensor/actualizarEstado/3", MqttQoS.AT_LEAST_ONCE.value(), handler -> {
					if (handler.succeeded()) {
						System.out.println("Client has been subscribed to Sensor3");
					}
				});
				mqttClient.subscribe("/api/velocidad/register", MqttQoS.AT_LEAST_ONCE.value(), handler -> {
					if (handler.succeeded()) {
						System.out.println("Client has been subscribed to REGISTER");
					}
				});
				mqttClient.subscribe("/api/velocidad/eliminar", MqttQoS.AT_LEAST_ONCE.value(), handler -> {
					if (handler.succeeded()) {
						System.out.println("Client has been subscribed to Eliminar Velocidades");
					}
				});
				mqttClient.publishHandler(message -> {
					System.out.println("Message published on topic: " + message.topicName());
					System.out.println(message.payload().toString());
					
						try {
							if(message.topicName().equals("/api/velocidad/registrar")) {
							String x = message.payload().toString();
							String[] z = x.split(":");
							String[] y = z[1].split(",");
							String speed = y[0];
							Double x3 = Double.parseDouble(speed); //nuevo
							String[] k = z[2].split("}");
							String fecha = k[0];
							VelocidadImpl v3 = new VelocidadImpl(x3, fecha); //nuevo
							System.out.println(v3.toString()); //nuevo				
						
							}else if(message.topicName().equals("/api/sensor/actualizarEstado/1")) {
								String x2 = message.payload().toString();
								String[] z2 = x2.split(":");
								String[] y2 = z2[1].split("}");
								String estado = y2[0];
								SensorImpl v2 = new SensorImpl(Integer.parseInt(estado));
								System.out.println(v2.toString());
							}
							else if(message.topicName().equals("/api/sensor/actualizarEstado/2")) {
								String x2 = message.payload().toString();
								String[] z2 = x2.split(":");
								String[] y2 = z2[1].split("}");
								String estado = y2[0];
								SensorImpl v2 = new SensorImpl(Integer.parseInt(estado));
								System.out.println(v2.toString());
							}
							else if(message.topicName().equals("/api/sensor/actualizarEstado/3")) {
								String x2 = message.payload().toString();
								String[] z2 = x2.split(":");
								String[] y2 = z2[1].split("}");
								String estado = y2[0];
								SensorImpl v2 = new SensorImpl(Integer.parseInt(estado));
								System.out.println(v2.toString());
							}
						} catch (JsonSyntaxException e) {
							System.out.println("Message content is wrong");
						}
					

				});

			} else {
				System.out.println("Se ha producido un error en la conexión al broker");
			}
		});

		Router router = Router.router(vertx);

		HttpServer httpServer = vertx.createHttpServer();
		httpServer.requestHandler(router::handle).listen(4040, result -> {
			if (result.succeeded()) {
				startFuture.complete();
			} else {
				startFuture.fail(result.cause());
			}
		});
		
		
		router.post("/api/velocidad/registrar").handler(this::notifyUserUpdate);
		router.get("/api/velocidad/ver").handler(this::getSensors);
		router.post("/api/velocidad/registrar/:IdVelocidad").handler(this::notifyUserUpdate);

	}
	
	
	private void addOne(RoutingContext routingContext) {
		final VelocidadImpl user = gson.fromJson(routingContext.getBodyAsString(), VelocidadImpl.class);
		mqttClient.publish("/api/velocidad/register", Buffer.buffer(gson.toJson(user)), MqttQoS.AT_LEAST_ONCE, false, false);
		routingContext.response().setStatusCode(201).putHeader("content-type", "application/json; charset=utf-8")
				.end(gson.toJson(user));
	}
	
	private void getSensors(RoutingContext routingContext) {
		routingContext.response().putHeader("content-type", "application/json").setStatusCode(200)
				.end(gson.toJson(users.values()));
	}
	
	private void notifyUserUpdate(RoutingContext routingContext) {
		int param = Integer.parseInt(routingContext.request().getParam("IdVelocidad"));
		VelocidadImpl user = gson.fromJson(routingContext.getBodyAsString(), VelocidadImpl.class);
		System.out.println(user);
		mqttClient.publish("/api/velocidad/registrar", Buffer.buffer(gson.toJson(user)), MqttQoS.AT_LEAST_ONCE, false, false);
		routingContext.response().setStatusCode(200).end();
	}
	
	
}
