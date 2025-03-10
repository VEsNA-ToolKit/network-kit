package networkkit.websocket;

import jason.asSemantics.*;
import jason.asSyntax.*;
import static jason.asSyntax.ASSyntax.*;
import java.net.URI;

public class WsAgent extends Agent {

    private WsClient client;

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

        URI addrUri = new URI( "ws://" + address + ":" + port );
        client = new WsClient( addrUri );

        client.setMsgHandler( new WsClientMsgHandler() {

            @Override
            public void handleMsg( String msg ) {
                System.out.println( "[" + getTS().getAgArch().getAgName() + "] received " + msg );
            }

            @Override
            public void handleError( Exception e ) {
                e.printStackTrace();
            }

        });
        

    }

    public void send( String msg ) {
        if ( !client.isOpen() )
            client.connect();
        while( !client.isOpen() );
        client.send( msg );
    }
}