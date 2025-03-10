package networkkit.http;

import java.net.URI;
import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.BodyPublishers;

public class HttpClientConn {

    private HttpClient client;
    private URI serverURI;
    private HttpClientMsgHandler msgHandler;
    
    public HttpClientConn( String address, int port ) {
        client = HttpClient.newHttpClient();
        String uri = "http://" + address + ":" + port;
        serverURI = URI.create( uri );
    }

    public HttpClientConn( String address, int port, String context ) {
        client = HttpClient.newHttpClient();
        String uri = "http://" + address + ":" + port + "/" + context;
        serverURI = URI.create( uri );
    }

    public HttpClientConn( String address, int port, String context, boolean secure ) {
        client = HttpClient.newHttpClient();
        String uri = "";
        if ( secure )
            uri += "https://" + address + ":" + port + "/" + context;
        else
            uri += "http://" + address + ":" + port + "/" + context;
        serverURI = URI.create( uri );
    }

    public HttpClientConn( String address, int port, boolean secure ) {
        client = HttpClient.newHttpClient();
        String uri = "";
        if ( secure )
            uri += "https://" + address + ":" + port;
        else
            uri += "http://" + address + ":" + port;
        serverURI = URI.create( uri );
    }

    public void setMsgHandler( HttpClientMsgHandler handler ){
        this.msgHandler = handler;
    }

    public void send( String msg ) {
        HttpRequest req = HttpRequest.newBuilder()
            .uri( serverURI )
            .header( "Content-Type", "text/plain" )
            .POST( BodyPublishers.ofString( msg ) )
            .build();

        try {
            HttpResponse<String> ans = client.send( req, HttpResponse.BodyHandlers.ofString() );
            handleMsg( ans );
        } catch( IOException e ){
            e.printStackTrace();
        } catch ( InterruptedException e ){

        }
    }

    private void handleMsg( HttpResponse<String> msg ) {
        if ( msgHandler != null )
            msgHandler.handleMsg( msg );
    }

}