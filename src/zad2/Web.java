package zad2;

import javafx.beans.value.ChangeListener;
import javafx.application.Application;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.scene.web.WebEngine;
import static javafx.concurrent.Worker.State;

public class Web extends Application
{
    private static String url="https://en.wikipedia.org/wiki/Warsaw";

    public static void start()
    {
        Application.launch();
    }

    @Override
    public void start(final Stage stage)
    {
        // Create the WebView
        WebView webView = new WebView();

        // Create the WebEngine
        final WebEngine webEngine = webView.getEngine();

        // LOad the Start-Page
        webEngine.load(Web.url);
        // Create the VBox
        VBox root = new VBox();
        // Add the WebView to the VBox
        root.getChildren().add(webView);
        // Create the Scene
        Scene scene = new Scene(root);
        // Add  the Scene to the Stage
        stage.setScene(scene);
        // Display the Stage
        stage.show();
    }

    public static String getUrl() {
        return url;
    }

    public static void setUrl(String town) {
        Web.url = "https://en.wikipedia.org/wiki/"+town;
    }
}
