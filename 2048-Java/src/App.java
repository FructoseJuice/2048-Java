import java.util.HashMap;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class App extends Application {
    public static void main(String[] args) {
        launch(args);
    }

//SQUARE DIMENSIONS 105x105
//BOARD DIMENSIONS 500x500
//SPACE BETWEEN SQUARES 15px
    @Override
    public void start(Stage primaryStage) {
        //Vbox to hold all pane Nodes
        VBox vBox = new VBox();
        //Hud to lay buttons on
        HBox hud = new HBox();
        //StackPane to layer board and squares on
        StackPane stackPane = new StackPane();

        //Initialize Spaces
        HashMap<Integer, Square[]> spaces = initializeSpacesMap();


        Scene root = new Scene(stackPane);
        primaryStage.setScene(root);
        primaryStage.show();
    }

    /*
     * Initialize all valid spaces on board with empty values
     * Key = the hash of each CoorPair
     */
    private HashMap<Integer, Square[]> initializeSpacesMap() {
        HashMap<Integer, Square[]> spaces = new HashMap<>();
        final int SQUARE_SIZE = 105;
        final int BORDER_SIZE = 15;
        CoorPair tempPair;
        int x;
        int y;

        for ( int i = 0; i < 4; i++ ) {
            for ( int j = 0; j < 4; j++ ) {
                x = (BORDER_SIZE * i+1) + (SQUARE_SIZE * j) + (int) Math.ceil(SQUARE_SIZE/2);
                y = (BORDER_SIZE * j+1) + (SQUARE_SIZE * i) + (int) Math.ceil(SQUARE_SIZE/2);
                tempPair = new CoorPair(x, y);
                System.out.print(tempPair.toString() + " - ");
                //Add new CoorPair hash and empty Square[] of size 2
                Square[] tempArray = new Square[2];
                tempArray[0] = null;
                tempArray[1] = null;
                spaces.put(tempPair.hashCode(), tempArray);
            }
            System.out.println();
        }

        return spaces;
    }
}
