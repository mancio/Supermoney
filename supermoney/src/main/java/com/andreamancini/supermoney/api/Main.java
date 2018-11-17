package com.andreamancini.supermoney.api;



import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;



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
	
	//store accounts and transfers in a linked hash table for a predictable iteration order 
	private final Map<Integer, Account> accounts = new LinkedHashMap<>();
    private final Map<Integer, Transfer> transfers = new LinkedHashMap<>();
	
	public static void main(final String[] args) {
        Launcher.executeCommand("run", Main.class.getName());
    }
	
	@Override
    public void start(Future<Void> fut) {
		
		//generate some test accounts
		testdata();


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
        
        // get account by id
        router.get("/api/accounts/:id").handler(this::accountById);
        
        // get all accounts
        router.get("/api/accounts/").handler(this::allAccounts);
       
        
        //post methods
        
        //make a transfer 
        router.post("/api/transfer/:id/to/:id2/:ammount").handler(this::makeTranfer);
        
        // make a new account
        router.post("/api/accounts").handler(this::makeAccount);

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


	private void allAccounts(RoutingContext routingContext) {
        routingContext.response()
                .putHeader("content-type", "application/json; charset=utf-8")
                .end(Json.encodePrettily(accounts.values()));
    }

	private void accountById(RoutingContext routingContext) {
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
    }
	
	private void makeTranfer(RoutingContext routingContext) {
		
		
		
	}
	
	
	private void makeAccount(RoutingContext routingContext) {
		
	}
		
	
	
	
	private void testdata() {
		
		Account mario = new Account("Mario Rossi", "supermario", new BigDecimal("2000"));
		accounts.put(mario.getId(), mario);
		
		Account roberto = new Account("Roberto Bianchi", "kapusta", new BigDecimal("10000"));
		accounts.put(mario.getId(), roberto);
		
	}
	
	
	
	
}
