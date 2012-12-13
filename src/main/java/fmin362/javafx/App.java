package fmin362.javafx;

import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class App
    extends Application
{

    private static final int HTTP_PORT = 8000;

    /**
     * Méthode principale.
     */
    public static void main( String[] args )
        throws Exception
    {
        MockHttpServer.start( HTTP_PORT );
        launch( args );
    }

    /**
     * Démarrage de l'application JavaFX.
     */
    @Override
    public void start( Stage primaryStage )
        throws Exception
    {
        init( primaryStage );
        primaryStage.show();
    }

    /**
     * Arrêt de l'application JavaFX.
     */
    @Override
    public void stop()
        throws Exception
    {
        System.exit( 0 );
    }

    private Button notGood;
    private Button good;
    private TextArea text;
    private ProgressBar progress;
    private RestoFXClient restoFXClient;

    /**
     * Initialisation de l'UI.
     */
    private void init( Stage primaryStage )
        throws Exception
    {
        primaryStage.setTitle( "RestoFX" );

        // Scene root
        StackPane root = new StackPane();
        root.setPadding( new Insets( 16 ) );

        // Scene
        Scene scene = new Scene( root, 480, 320 );

        // Buttons
        notGood = new Button();
        notGood.setMaxSize( Double.MAX_VALUE, Double.MAX_VALUE );
        notGood.setText( "Pas bien" );
        notGood.setOnAction( new NotGoodFetchMenuActionHandler() );

        good = new Button();
        good.setMaxSize( Double.MAX_VALUE, Double.MAX_VALUE );
        good.setText( "Bien" );
        good.setOnAction( new GoodFetchMenuActionHandler() );

        // Label
        text = new TextArea();
        text.setMaxSize( Double.MAX_VALUE, Double.MAX_VALUE );
        VBox.setVgrow( text, Priority.ALWAYS );

        // Progress Bar
        progress = new ProgressBar();
        progress.setMaxSize( Double.MAX_VALUE, Double.MAX_VALUE );
        progress.setProgress( 1 );

        // Vertical layout
        VBox vbox = new VBox( 16 );
        vbox.setAlignment( Pos.TOP_LEFT );

        // Scene tree
        vbox.getChildren().addAll( notGood, good, text, progress );
        root.getChildren().add( vbox );

        // Set stage scene
        primaryStage.setScene( scene );

        // Setup RestoFX HTTP Client
        restoFXClient = new RestoFXClient( "http://127.0.0.1:" + HTTP_PORT + "/" );
    }

    /**
     * Mets l'UI en état de chargement.
     */
    private void nowLoading()
    {
        notGood.setDisable( true );
        good.setDisable( true );
        text.setDisable( true );
        text.setText( "" );
        progress.setProgress( -1 );
    }

    /**
     * Mets l'UI en état chargé.
     */
    private void nowLoaded()
    {
        notGood.setDisable( false );
        good.setDisable( false );
        text.setDisable( false );
        progress.setProgress( 1 );
    }

    /**
     * Mauvais exemple !
     */
    private class NotGoodFetchMenuActionHandler
        implements EventHandler<ActionEvent>
    {

        public void handle( ActionEvent t )
        {
            nowLoading();
            String menu = restoFXClient.getMenu();
            text.setText( formatJsonString( menu ) );
            nowLoaded();
        }

    }

    /**
     * Bon exemple !
     */
    private class GoodFetchMenuActionHandler
        implements EventHandler<ActionEvent>
    {

        public void handle( ActionEvent t )
        {
            nowLoading();
            Task<String> task = new Task<String>()
            {

                @Override
                protected String call()
                    throws Exception
                {
                    return restoFXClient.getMenu();
                }

            };
            task.setOnSucceeded( new EventHandler<WorkerStateEvent>()
            {

                public void handle( WorkerStateEvent success )
                {
                    String menu = (String) success.getSource().getValue();
                    System.out.println( menu );
                    text.setText( formatJsonString( menu ) );
                    nowLoaded();
                }

            } );
            new Thread( task, "Fetch Menu Thread" ).start();
        }

    }

    /**
     * Simple méthode utilitaire.
     */
    private static String formatJsonString( String json )
    {
        try
        {
            return new JSONObject( json ).toString( 2 );
        }
        catch( JSONException ex )
        {
            throw new RuntimeException( ex.getMessage(), ex );
        }
    }

}
