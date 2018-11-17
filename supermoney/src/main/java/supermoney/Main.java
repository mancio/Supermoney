package supermoney;



import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Launcher;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.StaticHandler;

public class Main extends AbstractVerticle {

	
	public static void main(final String[] args) {
        Launcher.executeCommand("run", Main.class.getName());
    }
	
	@Override
    public void start(Future<Void> fut) {


        Router router = Router.router(vertx);

        router.route("/").handler(routingContext -> {
            HttpServerResponse response = routingContext.response();
            response
                    .putHeader("content-type", "text/html")
                    .end("<h1>Supermoney</h1>");
        });

        //router.route("/assets/*").handler(StaticHandler.create("assets"));

        router.route().handler(BodyHandler.create());

        
        // get methods 
        router.get("/api/accounts/:id"):handler(this::accountById);
        router.get("/api/accounts/:user"):handler(this::accountByUser);
        
        //post methods
        router.post("/api/transfers").handler(this::makeTranfer);
        
        //put methods
        //router.put("/api/transfers/:id").handler(this::updateTransfer);

        vertx
                .createHttpServer()
                .requestHandler(router::accept)
                .listen(
                        8080,
                        result -> {
                            if (result.succeeded()) {
                                fut.complete();
                            } else {
                                fut.fail(result.cause());
                            }
                        }
                );
    }



	private void getAccount(RoutingContext routingContext) {
        final String id = routingContext.request().getParam("id");
        if (id == null) {
            routingContext.response().setStatusCode(400).end();
        } else {
            final Integer idAsInteger = Integer.valueOf(id);
            Account account = accounts.get(idAsInteger);
            if (account == null) {
                routingContext.response().setStatusCode(404).end();
            } else {
                routingContext.response()
                        .putHeader("content-type", "application/json; charset=utf-8")
                        .end(Json.encodePrettily(account));
            }
        }
    })
