# VEsNA Network Kit

In this repository are stored different communication channels both for artifacts and agents.
In particular, you will find

 - WebSocket communication;
 - HTTP communication.

## Usage

> [!IMPORTANT]
>
> **Requirements**
>
> - Java;
> - Gradle;

The framework provides a new package `networkkit` with:

- classes for HTTP connections (both client and server) - `networkkit.http`;
- classes for WebSocket connections (both client and server) - `networkkit.websocket`.

The classes can be used either extending a CArtAgO Artifact or a Jason Agent. Examples are provided in examples folder.

In the examples, an Artifact works as server and agents are clients.

To run the examples just go in the corresponding folder and run the gradle build:

```bash
cd examples/websocket
gradle run

# or
cd examples/http
gradle run
```

## API

### WebSocket Client

#### Artifact/Agent

To create a new WebSocket client extending an Artifact or an Agent:

```java
public class WsClientExample extends Artifact/Agent {
    // declare a WsClient object
    private WsClient client;
    
    // the connect function initializes the connection
    void connect( String address, int port ){
        URI addrUri = new URI( "ws://" + address + ":" + port );
        client = new WsClient( addrUri );
        
        client.setMsgHandler( new WsClientMsgHandler() {
        	// here it goes the msg handler code   
        }
        // create the connection
        client.connect();
    }
	
	// to send a message client has its own send method
    void send( String msg ){
        client.send( msg );
    }
}
```

You can then call the `connect` inside every function you want to overwrite, create a new `DefaultInternalAction` that calls it when the agent wants (if you are extending the agent) or make it an `@OPERATION`.

#### WsClientMsgHandler

The `WsClientMsgHandler` is an Interface and needs to be implemented. This Interface creates the bridge between the WebSocket client and the artifact/agent making the client use artifact/agent methods that have access to interesting things (`ObsProperty`, `addBell` and other internal actions for example). Doing so, when a message is received the artifact/agent can modify the environment or its state.

It should implement the two functions as follows:

```java
public class MyWsClientMsgHandler implements WsClientMsgHandler {
    
    // handle the income of new string messages
    @Override
    public void handleMsg( String msg ) {
        System.out.println( "[Client] received " + msg );
        // call artifact/agent method
    }

    // handle connection errors
    @Override
    public void handleError( Exception e ) {
        e.printStackTrace();
        // call artifact/agent method
    }
}
```

Then you can set the new `WsClientMsgHandler` with the `setMsgHandler` method of the WebSocket client. If you define the class outside the artifact/agent you should call in the callback the corresponding artifact/agent method. Otherwise you can implement the `WsClientMsgHandler` inside the artifact/agent as shown in the examples above and use already there the artifact/agent methods.

### WebSocket Server

#### Artifact/Agent

To create a new WebSocket server extending an Artifact or an Agent:

```java
public class WsServerExample extends Artifact/Agent {
    // declare a WsServer object
    private WsServer server;
    
    // the function startServer intialize the server
    void startServer( int port ){
        // create the server (the InetSocketAddres can also take an address, default is localhost)
        server = new WsServer( new InetSocketAddress( port ) );

        server.setMsgHandler( new WsServerMsgHandler() {
			// here it goes the msg handler code
        });
		
        // start the server
        server.start();
    }

}
```

#### WsServerMsgHandler

The `WsServerMsgHandler` is an Interface and needs to be implemented. This Interface creates the bridge between the WebSocket server and the artifact/agent making the client use artifact/agent methods that have access to interesting things (`ObsProperty`, `addBell` and other internal actions for example). Doing so, when a message is received the artifact/agent can modify the environment or its state.

It should implement the two functions as follows:

```java
public class MyWsServerMsgHandler implements WsServerMsgHandler {
    
    // handle the income of new string messages
    @Override
    public void handleMsg( WebSocket conn, String msg ) {
        System.out.println( "[Client] received " + msg );
        // call artifact/agent method
    }

    // handle connection errors
    @Override
    public void handleError( WebSocket conn, Exception e ) {
        e.printStackTrace();
        // call artifact/agent method
    }
}
```

Then you can set the new `WsServerMsgHandler` with the `setMsgHandler` method of the WebSocket client. If you define the class outside the artifact/agent you should call in the callback the corresponding artifact/agent method. Otherwise you can implement the `WsServerMsgHandler` inside the artifact/agent as shown in the examples above and use already there the artifact/agent methods.

Notice that, with respect to the `WsClientMsgHandler` Interface, the Server one has also the connection information to know which client connection sent the message or raised the error. 

### HTTP Client

#### Artifact/Agent

To create a new WebSocket client extending an Artifact or an Agent:

```java
public class HttpClientExample extends Artifact/Agent {
    // declare a HttpClientConn object
    private HttpClientConn client;
    
    void connect( String address, int port ) {
        // create the connection
        client = new HttpClientConn( address, port );
        // set the message handler
        client.setMsgHandler( new HttpClientMsgHandler() {
            // msg handler code goes here
        });
    }
    
    // to send a msg client has its own send method
    void send( String msg ) {
        client.send( msg );
    }
}
```

#### HttpClientMsgHandler

The `HttpClientMsgHandler` is an Interface and needs to be implemented. This Interface creates the bridge between the HTTP client and the artifact/agent making the client use artifact/agent methods that have access to interesting things (`ObsProperty`, `addBell` and other internal actions for example). Doing so, when a message is received the artifact/agent can modify the environment or its state.

It should implement the two functions as follows:

```java
public class MyWsClientMsgHandler implements WsClientMsgHandler {
    
    // handle the income of new string messages
   @Override
    public void handleMsg( HttpResponse<String> msg ) {
        System.out.println( msg.body() );
        // call the agent/artifact method
    }
}
```

Then you can set the new `WsClientMsgHandler` with the `setMsgHandler` method of the WebSocket client. If you define the class outside the artifact/agent you should call in the callback the corresponding artifact/agent method. Otherwise you can implement the `HttpClientMsgHandler` inside the artifact/agent as shown in the examples above and use already there the artifact/agent methods.

With respect to the WebSocket version it does not exist a `onError` callback since the eventual error is inside the `HttpResponse` object.

### HTTP Server

#### Artifact/Agent

To create a new HTTP server extending an Artifact or an Agent:

```java
public class HttpServerExample extends Artifact/Agent {
    // declare a HttpServerConn object
    private HttpServerConn server;
    
    // the function startServer intialize the server
    void startServer( int port ){
        try {
            server = new HttpServerConn( port, new HttpHandler() {
                // here gose the handle msg code
            } );
        } catch ( IOException e ) {
            e.printStackTrace();
        }
    }

}
```

#### Handler

In this case the handler interface is provided directly from the Server implementation.

``` java
 public class MyHttpHandler implements HttpHandler {

        @Override
        public void handle( HttpExchange exchange ) throws IOException {
            // get the input
            InputStream istream = exchange.getRequestBody();
            String msg = new String( istream.readAllBytes(), StandardCharsets.UTF_8 );

            System.out.println( "[Server] received " + msg );
			
            // send something back
            String ans = "ok";
            exchange.sendResponseHeaders( 200, ans.getBytes().length );
            OutputStream ostream = exchange.getResponseBody();
            ostream.write( ans.getBytes() );
            ostream.close();
        }
    }
```

If you define the class outside the artifact/agent you should call in the callback the corresponding artifact/agent method. Otherwise you can implement the `HttpHandler` inside the artifact/agent as shown in the examples above and use already there the artifact/agent methods. In this case there is not a `setMsgHandler` method but the object is passed during server creation.
