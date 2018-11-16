package supermoney;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Launcher;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Router;
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
                    .end("<h1>MoneyTransfer</h1>");
        });

        //router.route("/assets/*").handler(StaticHandler.create("assets"));

        router.route().handler(BodyHandler.create());

       /* router.get("/api/accounts").handler(this::getAllAccounts); 
        router.get("/api/accounts/:id").handler(this::getAccount);
        router.post("/api/accounts").handler(this::addAccount);
        router.put("/api/accounts/:id").handler(this::updateAccount);
        router.delete("/api/accounts/:id").handler(this::deleteAccount);

        router.get("/api/transfers").handler(this::getAllTransfers);
        router.get("/api/transfers/:id").handler(this::getTransfer);
        router.post("/api/transfers").handler(this::addTransfer);
        router.put("/api/transfers/:id").handler(this::updateTransfer);*/

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

}
