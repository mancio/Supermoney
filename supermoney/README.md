# MoneyTransfer
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

