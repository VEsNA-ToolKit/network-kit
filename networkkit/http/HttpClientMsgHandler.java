package networkkit.http;

import java.net.http.HttpResponse;

public interface HttpClientMsgHandler {

    public void handleMsg( HttpResponse<String> msg );

}