package networkkit.websocket;

// The WsClientMsgHandler is just an interface for methods that must
// be implemented inside the artifact/agent, in particular:
//  - handleMsg     makes the artifact/agent able to manage the incoming messages
//  - handleError   makes the artifact/agent able to manage connection errors
public interface WsClientMsgHandler {

    public void handleMsg( String msg );
    public void handleError( Exception e );

}