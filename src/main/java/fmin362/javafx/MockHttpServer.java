package fmin362.javafx;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.util.UUID;

/**
 * Mini serveur HTTP de démonstration.
 * Ne pas utiliser en production.
 */
public final class MockHttpServer
{

    public static void start( int port )
        throws IOException
    {
        InetSocketAddress address = new InetSocketAddress( port );
        final HttpServer httpServer = HttpServer.create( address, 0 );
        HttpHandler handler = new HttpHandler()
        {

            public void handle( HttpExchange exchange )
                throws IOException
            {

                // ATTENTION
                waitSeconds( 4 );
                // ATTENTION

                byte[] response = "{\"categories\":[{\"id\":39,\"name\":\"Desserts\",\"products\":[{\"name\":\"Banana split\",\"price\":4.8,\"quantity\":null,\"quantityUnit\":null},{\"name\":\"Profiterolles\",\"price\":5.2,\"quantity\":3,\"quantityUnit\":null},{\"name\":\"Colonel\",\"price\":4.9,\"quantity\":null,\"quantityUnit\":null}]},{\"id\":34,\"name\":\"Plats\",\"products\":[{\"name\":\"Couscous royal\",\"price\":13.0,\"quantity\":null,\"quantityUnit\":null},{\"name\":\"Cassoulet\",\"price\":12.0,\"quantity\":null,\"quantityUnit\":null},{\"name\":\"Raie au beurre noir\",\"price\":14.0,\"quantity\":null,\"quantityUnit\":null},{\"name\":\"Bouillabaisse\",\"price\":16.0,\"quantity\":null,\"quantityUnit\":null}]},{\"id\":5,\"name\":\"Bières\",\"products\":[{\"name\":\"Blonde\",\"price\":2.35,\"quantity\":25,\"quantityUnit\":\"cl\"},{\"name\":\"Brune\",\"price\":2.4,\"quantity\":25,\"quantityUnit\":\"cl\"},{\"name\":\"Ambrée\",\"price\":2.4,\"quantity\":25,\"quantityUnit\":\"cl\"},{\"name\":\"Rousse\",\"price\":2.45,\"quantity\":25,\"quantityUnit\":\"cl\"},{\"name\":\"Blanche\",\"price\":2.4,\"quantity\":25,\"quantityUnit\":\"cl\"},{\"name\":\"Blonde (Pinte)\",\"price\":2.35,\"quantity\":50,\"quantityUnit\":\"cl\"},{\"name\":\"Brune (Pinte)\",\"price\":2.4,\"quantity\":50,\"quantityUnit\":\"cl\"},{\"name\":\"Ambrée (Pinte)\",\"price\":2.4,\"quantity\":50,\"quantityUnit\":\"cl\"},{\"name\":\"Rousse (Pinte)\",\"price\":2.45,\"quantity\":50,\"quantityUnit\":\"cl\"},{\"name\":\"Blanche (Pinte)\",\"price\":2.4,\"quantity\":50,\"quantityUnit\":\"cl\"}]},{\"id\":23,\"name\":\"Boissons chaudes\",\"products\":[{\"name\":\"Thé noir\",\"price\":3.0,\"quantity\":null,\"quantityUnit\":null},{\"name\":\"Thé vert\",\"price\":3.0,\"quantity\":null,\"quantityUnit\":null},{\"name\":\"Thé rouge\",\"price\":3.0,\"quantity\":null,\"quantityUnit\":null},{\"name\":\"Café\",\"price\":1.35,\"quantity\":null,\"quantityUnit\":null},{\"name\":\"Café double\",\"price\":2.35,\"quantity\":null,\"quantityUnit\":null},{\"name\":\"Café crème\",\"price\":2.55,\"quantity\":null,\"quantityUnit\":null}]},{\"id\":30,\"name\":\"Entrées\",\"products\":[{\"name\":\"Oeuf mayo\",\"price\":1.5,\"quantity\":null,\"quantityUnit\":null},{\"name\":\"Avocat crevette\",\"price\":2.0,\"quantity\":null,\"quantityUnit\":null},{\"name\":\"Terrine de lapin\",\"price\":3.8,\"quantity\":null,\"quantityUnit\":null}]},{\"id\":1,\"name\":\"Softs\",\"products\":[{\"name\":\"Lait fraise\",\"price\":2.4,\"quantity\":25,\"quantityUnit\":\"cl\"},{\"name\":\"Jus de tomate\",\"price\":2.8,\"quantity\":25,\"quantityUnit\":\"cl\"},{\"name\":\"Diabolo menthe\",\"price\":2.2,\"quantity\":25,\"quantityUnit\":\"cl\"}]},{\"id\":16,\"name\":\"Cocktails\",\"products\":[{\"name\":\"Mojito\",\"price\":5.0,\"quantity\":33,\"quantityUnit\":\"cl\"},{\"name\":\"Cuba Libre\",\"price\":5.0,\"quantity\":33,\"quantityUnit\":\"cl\"},{\"name\":\"Long Island\",\"price\":5.0,\"quantity\":33,\"quantityUnit\":\"cl\"},{\"name\":\"White Russian\",\"price\":5.0,\"quantity\":33,\"quantityUnit\":\"cl\"},{\"name\":\"Punch Coco\",\"price\":5.0,\"quantity\":33,\"quantityUnit\":\"cl\"},{\"name\":\"Tequila Sunrise\",\"price\":5.0,\"quantity\":33,\"quantityUnit\":\"cl\"}]}]}".getBytes( "UTF-8" );
                exchange.getResponseHeaders().set( "Content-type", "application/json;charset=utf-8" );
                exchange.sendResponseHeaders( HttpURLConnection.HTTP_OK, response.length );
                exchange.getResponseBody().write( response );
                exchange.close();
            }

        };
        httpServer.createContext( "/menu", handler );
        httpServer.start();
        Runtime.getRuntime().addShutdownHook( new Thread( new Runnable()
        {

            public void run()
            {
                httpServer.stop( 0 );
                System.err.println( "HTTP Server stopped" );
            }

        }, "[" + UUID.randomUUID().toString() + "] HTTP Server Shutdown Hook" ) );
    }

    private static void waitSeconds( int seconds )
    {
        try
        {
            Thread.sleep( seconds * 1000 );
        }
        catch( InterruptedException ignored )
        {
        }
    }

    private MockHttpServer()
    {
    }

}
