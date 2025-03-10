package networkkit.http;

import cartago.Artifact;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class HttpArtifact extends Artifact {

    private HttpServerConn server;

    void init( int port ) {
        try {
            server = new HttpServerConn( port, new MyHttpHandler() );
        } catch ( IOException e ) {
            e.printStackTrace();
        }
    }

    public class MyHttpHandler implements HttpHandler {

        @Override
        public void handle( HttpExchange exchange ) throws IOException {
            InputStream istream = exchange.getRequestBody();
            String msg = new String( istream.readAllBytes(), StandardCharsets.UTF_8 );

            System.out.println( "[Server] received " + msg );

            String ans = "ok";
            exchange.sendResponseHeaders( 200, ans.getBytes().length );
            OutputStream ostream = exchange.getResponseBody();
            ostream.write( ans.getBytes() );
            ostream.close();
        }
    }
}