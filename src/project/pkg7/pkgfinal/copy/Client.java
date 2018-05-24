package project.pkg7.pkgfinal.copy;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * Runs the UI class
 * @author harmanjeetdhillon
 */
public class Client extends Application
{
       
    @Override
    public void start(Stage primaryStage)
    {
        
        // Creates UI for the client
        UI ui = new UI(primaryStage);
        primaryStage.setTitle("Lux Chat Client");
        primaryStage.setScene(ui.basePaneUI());
        primaryStage.setResizable(false); 
        primaryStage.show();
        
        // Closes theard, socket, and everything else once the window is closed 
        primaryStage.setOnCloseRequest((WindowEvent e) ->{
            Platform.exit();
            System.exit(0);
        });
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        try {
        launch(args);
        } catch (NullPointerException e)
        {System.out.println("Happening");}
        }
}
