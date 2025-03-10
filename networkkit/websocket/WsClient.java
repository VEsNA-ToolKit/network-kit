package networkkit.websocket;

import java.net.URI;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
//import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;

public class WsClient extends WebSocketClient {

    private WsClientMsgHandler msgHandler;

    // Class constructors
    public WsClient( URI serverURI, Draft draft ) {
        super( serverURI, draft );
    }

    public WsClient( URI serverURI ) {
        super( serverURI );
    }

    // Setting the message handle functions
    public void setMsgHandler( WsClientMsgHandler handler ) {
        this.msgHandler = handler;
    }

    @Override
    public void onOpen( ServerHandshake handshakedata ) {
        if ( msgHandler == null )
            System.out.println( "[WsClient] ERROR: A websocket client needs a message handler!" );
        System.out.println( "[WsClient] new connection opened" );
    }

    @Override
    public void onClose( int code, String reason, boolean remote ) {
        System.out.println( "[WsClient] closed with exit code " + code + " and reason " + reason );
    }

    @Override
    public void onMessage( String msg ) {
        if ( msgHandler != null )
            msgHandler.handleMsg( msg );
    }

    @Override
    public void onError( Exception e ){
        if ( msgHandler != null )
            msgHandler.handleError( e );
    }

}