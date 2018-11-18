# Supermoney
A RESTful API for money transfers between accounts.

Developed using Java 8 and [Vert.x](http://vertx.io).

## Data Model and Backing implementation

The software is a stand-alone java in-app memory with no database.
The accounts get lost if the app shutdown or crash, but all the internal operations in the app
are very fast.

This is possible thanks to a linkedHashMap where the accounts are stored.
The linked hash map has predictable iteration order of the elements in the map give the possibility
to enumerate and search easily the accounts.

## API Usage

### Accounts

#### Retrieve all accounts
The folowing request retrieves all accounts in the datastore
```
    GET localhost:8080/api/accounts
```
Response:
```
    HTTP 200 OK
    [
    	{
        	"id": 0,
        	"name": "Mario Rossi",
        	"user": "supermario",
        	"money": 2000
    	},
    	{
        	"id": 1,
        	"name": "Roberto Bianchi",
        	"user": "kapusta",
        	"money": 100000
    	}
	]
```
#### Retrieve one account
The following request retrieves one account in the datastore
```
    GET localhost:8080/api/accounts/0
```
Response:
```
    HTTP 200 OK
    {
    	"id": 0,
        "name": "Mario Rossi",
        "user": "supermario",
        "money": 2000
   	},
```

#### Create an account
The following request creates an account and returns it:
```
    POST localhost:8080/api/accounts
    {
    	"name": "Mario Rossi",
    	"user": "supermario",
    	"money": 2000
	}
```
Response:
```
    HTTP 201 Created
    {	
    	"id": 1,
    	"name": "Mario Rossi",
    	"user": "supermario",
    	"money": 2000
	}
```

### Transfers
#### Create a transfer
The following request creates a transfer and returns it:
```
    POST localhost:8080/api/transfers/0/to/1/200
    
```
Response:
```
    HTTP 201 Created
    {	
    	"id": 1,
    	"name": "Mario Rossi",
    	"user": "supermario",
    	"money": 2200
	}
	
```
where

	- 0 = id account sender
	- 1 = id account receiver
	- 200 = 200 euro to transfer
