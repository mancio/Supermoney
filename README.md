# Supermoney
A RESTful API for money transfers between accounts.

Developed using Java and [Vert.x](http://vertx.io).

## Data Model and Backing implementation

The software is a stand-alone java in-app memory with no database.
The accounts get lost if the app shutdown or crash, but all the internal operations in the app
are very fast.

This is possible thanks to a linkedHashMap where the accounts are stored.
This structure has predictable iteration order of the elements in the map and give the possibility
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
	}
   	 
   	 
```

#### Create an account
The following request creates an account and returns it:
```
    POST localhost:8080/api/accounts
	{
    	"name": "Mario Rossi",
    	"user": "supermario",
    	"money": "2000"
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

#### Update an account
The following request updates an account and returns it:
```
    PUT localhost:8080/api/accounts/0
	{
        "name": "Mario Neri",
        "user": "ciccio",
        "money": "300"
	}
```
Response:
```
    HTTP 200 OK
	{
        "id": 0,
        "name": "Mario Neri",
        "user": "ciccio",
        "money": 300
	}
```
* do not try to update id (for security reason)
* remember to write the amount inside ""

#### Delete an account
The following request deletes an account:
```
    DELETE localhost:8080/api/accounts/0
```
Response:
```
    HTTP 204 No Content
```

### Transfers
#### Create a transfer
The following request creates a transfer and returns it:
```
    POST localhost:8080/api/transfer/0/to/1/200
    
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

* 0 = id account sender
* 1 = id account receiver
* 200 = 200 euro to transfer (the API is single currency)




## How to test

The testing and building procedures are defined in the pom.xml file. 

The project is been compiled using Maven and tested with Junit in Eclipse

### To test the project please follow the procedure:

1. Compile and build the package by using the command
	
	-DskipTests package
	
2. Execute the fat java file 

	java -jar filename.jar
	
3. In eclipse test with Junit	

4. If you try to GET all the accounts after test you should have the following results

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


-DforkMode=never test (for debug)

clean verify

-DskipTests pre-integration-test

post-integration-test

pre-integration-test



## Troubleshooting

If you see the following message during testing

```

	WARNING: Illegal reflective access by org.codehaus.groovy.reflection.CachedClass

```

and no test is performed, please change environment and use java 8
