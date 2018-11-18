package com.andreamancini.supermoney.api;



import java.math.BigDecimal;

import java.util.LinkedHashMap;
import java.util.Map;



import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Launcher;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
//import io.vertx.ext.web.handler.StaticHandler;

public class Main extends AbstractVerticle {
	
	//store accounts and transfers in a linked hash table for a predictable iteration order 
	private final Map<Integer, Account> accounts = new LinkedHashMap<>();
	
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
        
        //put method
        
        //update name and username
        router.put("/api/accounts/:id").handler(this::updateAccount);
        
        //delete method
        
        //delete account
        router.delete("/api/accounts/:id").handler(this::deleteAccount);
       
        
        //post methods
                
        //make a transfer 
        router.post("/api/transfer/:id/to/:id2/:eur").handler(this::makeTranfer);
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
		
		
		
		
		final String id = routingContext.request().getParam("id");
		final String id2 = routingContext.request().getParam("id2");
		final String eur = routingContext.request().getParam("eur");
		
		final Integer idint = Integer.valueOf(id);
		final Integer id2int = Integer.valueOf(id2);
		final BigDecimal bigeur = new BigDecimal(eur);
		
		
		if(id == null || id2 == null) {
			routingContext.response().setStatusCode(400).end();
		} else if (accounts.get(idint).getmoney().compareTo(bigeur) < 0){
			
			routingContext.response().setStatusCode(400).end();
			
		}else {
			
			accounts.get(idint).sub(bigeur);
			accounts.get(id2int).add(bigeur);
			
			Account account = accounts.get(idint);
			
			routingContext.response()
            .putHeader("content-type", "application/json; charset=utf-8")
            .end(Json.encodePrettily(account));
			
		}
		
	}
	
	
	private void makeAccount(RoutingContext routingContext) {
		
		try {
            final Account account = Json.decodeValue(routingContext.getBodyAsString(),
                    Account.class);
            accounts.put(account.getId(), account);
            routingContext.response()
                    .setStatusCode(201)
                    .putHeader("content-type", "application/json; charset=utf-8")
                    .end(Json.encodePrettily(account));
        } catch (Exception e) {
            routingContext.response().setStatusCode(400).end();
        }
		
	}
	
	
	
	private void updateAccount(RoutingContext routingContext) {
        final String id = routingContext.request().getParam("id");
        JsonObject json = routingContext.getBodyAsJson();
        if (id == null || json == null) {
            routingContext.response().setStatusCode(400).end();
        } else {
            final Integer idAsInteger = Integer.valueOf(id);
            Account account = accounts.get(idAsInteger);
            if (account == null) {
                routingContext.response().setStatusCode(404).end();
            } else {
                boolean updated = false;
                if (json.getString("name") != null && !json.getString("name").isEmpty()) {
                    account.setName(json.getString("name"));
                    updated = true;
                }
                if (json.getString("user") != null && !json.getString("user").isEmpty()) {
                    account.setUser(json.getString("user"));
                    updated = true;
                }
                if (json.getString("money") != null && !json.getString("money").isEmpty() && (new BigDecimal(json.getString("money"))).compareTo(BigDecimal.ZERO) >= 0) {
                    account.setBalance(new BigDecimal(json.getString("money")));
                    updated = true;
                }
                
                if (!updated) {
                    routingContext.response().setStatusCode(400).end();
                } else {
                    routingContext.response()
                            .putHeader("content-type", "application/json; charset=utf-8")
                            .end(Json.encodePrettily(account));
                }
            }
        }
    }

    private void deleteAccount(RoutingContext routingContext) {
        String id = routingContext.request().getParam("id");
        if (id == null) {
            routingContext.response().setStatusCode(400).end();
        } else if (accounts.get(Integer.valueOf(id)) == null) {
            routingContext.response().setStatusCode(404).end();
        } else {
            Integer idAsInteger = Integer.valueOf(id);
            accounts.remove(idAsInteger);
            routingContext.response().setStatusCode(204).end();
        }
    }
		
	
	
	
	private void testdata() {
		
		Account mario = new Account("Mario Rossi", "supermario", new BigDecimal("2000"));
		accounts.put(mario.getId(), mario);
		
		Account roberto = new Account("Roberto Bianchi", "kapusta", new BigDecimal("100000"));
		accounts.put(roberto.getId(), roberto);
		
	}
	
	
	
	
}
