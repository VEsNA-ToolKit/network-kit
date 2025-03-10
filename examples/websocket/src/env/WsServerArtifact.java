package networkkit.websocket;

import cartago.Artifact;
import org.java_websocket.WebSocket;
import java.net.InetSocketAddress;

public class WsServerArtifact extends Artifact {

    private WsServer server;

    void init( int port ) {
        server = new WsServer( new InetSocketAddress( port ) );

        server.setMsgHandler( new WsServerMsgHandler() {

            @Override
            public void handleMsg( WebSocket conn, String msg ) {
                System.out.println( "[Server] received " + msg );
            }

            @Override
            public void handleError( WebSocket conn, Exception e ) {
                e.printStackTrace();
            }

        });

        server.start();
    }


}