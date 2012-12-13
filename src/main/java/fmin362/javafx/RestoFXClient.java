package fmin362.javafx;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import javax.ws.rs.core.MediaType;

/**
 * Jersey based HTTP Client.
 */
public class RestoFXClient
{

    private final WebResource rootResource;

    public RestoFXClient( String rootUrl )
        throws IOException, URISyntaxException
    {
        ClientConfig clientConfig = new DefaultClientConfig();
        clientConfig.getFeatures().put( JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE );
        Client client = Client.create( clientConfig );
        URL url = new URL( rootUrl );
        rootResource = client.resource( url.toURI() );
    }

    // Récupération du JSON brut
    public String getMenu()
    {
        return rootResource.path( "menu" ).accept( MediaType.APPLICATION_JSON ).get( String.class );
    }

}
