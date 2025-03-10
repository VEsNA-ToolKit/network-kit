package networkkit.http;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

public class HttpServerConn {

    private HttpServer server;

    public HttpServerConn( int port, HttpHandler handler ) throws IOException {
        server = HttpServer.create( new InetSocketAddress( port ), 0 );
        server.createContext( "/", handler );
        server.setExecutor( null );
        server.start();

        System.out.println( "[HttpServer] server started correctly");
    }

}