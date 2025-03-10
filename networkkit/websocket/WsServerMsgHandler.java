package networkkit.websocket;

import org.java_websocket.WebSocket;

public interface WsServerMsgHandler {
    
    public void handleMsg( WebSocket conn, String msg );
    public void handleError( WebSocket conn, Exception e );

}