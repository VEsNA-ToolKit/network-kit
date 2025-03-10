package networkkit.websocket;

import java.net.InetSocketAddress;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

public class WsServer extends WebSocketServer {

    private WsServerMsgHandler msgHandler;

    // Constructor
    public WsServer( InetSocketAddress addr ) {
        super( addr );
    }

    @Override
    public void onStart() {
        System.out.println( "[WsServer] server started correctly" );
    }

    @Override
    public void onOpen( WebSocket conn, ClientHandshake handshakedata ) {
        System.out.println( "[WsServer] new connection from client " + conn.getRemoteSocketAddress() );
    }

    @Override
    public void onClose( WebSocket conn, int code, String reason, boolean remote ) {
        System.out.println( "[WsServer] connection from " + conn.getRemoteSocketAddress() + " closed with code " + code + " and reason: " + reason );
    }

    public void setMsgHandler( WsServerMsgHandler handler ) {
        this.msgHandler = handler;
    }

    @Override
    public void onMessage( WebSocket conn, String msg ) {
        if ( msgHandler != null )
            msgHandler.handleMsg( conn, msg );
    }

    @Override
    public void onError( WebSocket conn, Exception e ) {
        if ( msgHandler != null )
            msgHandler.handleError( conn, e );
    }
}