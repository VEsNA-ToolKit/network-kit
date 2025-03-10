package networkkit.http;

import jason.asSemantics.*;
import jason.asSyntax.*;
import static jason.asSyntax.ASSyntax.*;

import java.net.http.HttpResponse;

public class HttpAgent extends Agent {

    private HttpClientConn client;

    @Override
    public void loadInitialAS( String asSrc ) throws Exception {

        super.loadInitialAS( asSrc );

        Unifier address_unifier = new Unifier();
        believes( parseLiteral( "address( Address )"), address_unifier );

        Unifier port_unifier = new Unifier();
        believes( parseLiteral( "port( Port )" ), port_unifier );

        if ( address_unifier == null || port_unifier == null ) {
            System.out.println( "[Agent] ERROR Connection not started. You must insert address(Address) and port(Port) as initial beliefs!");
            return;
        }

        String address = address_unifier.get( "Address" ).toString();
        int port = ( int )( ( NumberTerm ) port_unifier.get( "Port" ) ).solve();

        client = new HttpClientConn( address, port );
        client.setMsgHandler( new HttpClientMsgHandler() {
            @Override
            public void handleMsg( HttpResponse<String> msg ) {
                agHandleMsg( msg.body() );
            }
        });
    }

    private void agHandleMsg( String msg ) {
        System.out.println( "[" + getTS().getAgArch().getAgName() + "] received: " + msg );
    }

    public void send( String msg ) {
        client.send( msg );
    }
}