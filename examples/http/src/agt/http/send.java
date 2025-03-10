package networkkit.http;

import jason.asSemantics.*;
import jason.asSyntax.*;

public class send extends DefaultInternalAction {

    @Override
    public Object execute( TransitionSystem ts, Unifier un, Term[] args ) {

        String msg = args[0].toString();
        HttpAgent ag = (HttpAgent) ts.getAg();
        ag.send( msg );

        return true;
    }
}