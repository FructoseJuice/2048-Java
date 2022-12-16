import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    private final CoorPair[][] spaceCoors = initializeSpaces();
    private final HashMap<Integer, Tile[]> spaces = initializeSpacesMap();
    //Locks game while handling input
    boolean lockGame = false;
    //Determines when to start randomly spawning 4's
    int weight = 5;
    Text score = new Text(Integer.toString(0));

    @Override
    public void start(Stage primaryStage) throws FileNotFoundException {
        //Vbox to hold all pane Nodes
        VBox vBox = new VBox();
        //Hud to lay buttons on
        HBox hud = new HBox();
        Label scoreLabel = new Label("Score: ");
        hud.getChildren().addAll(scoreLabel, score);

        //StackPane to layer board and squares on
        StackPane stackPane = new StackPane();
        //AnchorPane to hold tiles
        AnchorPane anchorPane = new AnchorPane();

        //Initialize Spaces and Squares
        Tile[] tiles = initializeTiles();
        //Initialize board
        Image boardPng = new Image(new FileInputStream("resources/grid.png"));
        ImageView board = new ImageView(boardPng);
        board.setFitHeight(500);
        board.setFitWidth(500);

        //Add board and squares to GUI
        stackPane.getChildren().add(board);
        for ( Tile n : tiles) {anchorPane.getChildren().add(n.getNode().getStack());}
        stackPane.getChildren().add(anchorPane);

        //Set up UI
        vBox.getChildren().add(hud);
        vBox.getChildren().add(stackPane);
        vBox.setPadding(new Insets(20, 20, 20, 20));

        //Spawn 2 initial tiles
        spawnTile(spaces, tiles);
        spawnTile(spaces, tiles);

        /*
         * Set Stage and draw to screen
         */
        Scene root = new Scene(vBox);
        root.setFill(Color.web("#22201a"));


        /*
        HANDLE KEYBOARD EVENT
         */
        root.setOnKeyPressed(keyEvent -> {
            Direction dir = null;
            //Check key pressed
            switch ( keyEvent.getCode() ) {
                case UP, W -> dir = Direction.NORTH;
                case RIGHT, D -> dir = Direction.EAST;
                case DOWN, S -> dir = Direction.SOUTH;
                case LEFT, A -> dir = Direction.WEST;
            }

            if ( dir != null && !lockGame) {
                lockGame = true;
                /*
                Calculate moves for all tiles
                 */
                int col;
                int row;
                Tile tempTile;
                boolean moveMade = false;

                switch ( dir ) {
                    case NORTH -> {
                        //Check tiles from top left to right first
                        for (row = 0; row < 4; row++ ) {
                            for (col = 0; col < 4; col++ ) {
                                //Check for tile at coordinates
                                tempTile = spaces.get(spaceCoors[row][col].hashCode())[0];
                                if (tempTile != null) {
                                    //If tile found
                                    moveMade = computeTileMoves(tempTile, dir);
                                }
                            }
                        }
                    }

                    case EAST -> {
                        //Check tiles from top right to bottom right first
                        for (col = 3; col >= 0; col--) {
                            for (row = 0; row < 4; row++ ) {
                                //Check for tile at coordinates
                                tempTile = spaces.get(spaceCoors[row][col].hashCode())[0];
                                if (tempTile != null) {
                                    //If tile found
                                    moveMade = computeTileMoves(tempTile, dir);
                                }
                            }
                        }

                    }

                    case SOUTH -> {
                        //Check tile moves from bottom left to right first
                        for (row = 3; row >= 0; row--) {
                            for (col = 0; col < 4; col++ ) {
                                //Check for tile at coordinates
                                tempTile = spaces.get(spaceCoors[row][col].hashCode())[0];
                                if (tempTile != null) {
                                    //If tile found
                                    moveMade = computeTileMoves(tempTile, dir);
                                }
                            }
                        }

                    }

                    case WEST -> {
                        //Check tile moves from top left to bottom left first
                        for (col = 0; col < 4; col++ ) {
                            for(row = 0; row < 4; row++ ) {
                                //Check for tile at coordinates
                                tempTile = spaces.get(spaceCoors[row][col].hashCode())[0];
                                if (tempTile != null) {
                                    //If tile found
                                    moveMade = computeTileMoves(tempTile, dir);
                                }
                            }
                        }

                    }
                }

                //Spawn new tile in empty space if move made
                if ( moveMade ) {
                    spawnTile(spaces, tiles);
                }

                lockGame = false;
            }
        });

        //Draw game to screen
        primaryStage.setScene(root);
        primaryStage.setResizable(false);
        primaryStage.setTitle("2048");
        primaryStage.show();
    }

    /**
     * Initializes HashMap of all hashed coordinate spaces on the board and the tiles it holds
     * @key </Integer> = Hashed coordinates of space
     * @value </Tile[]> = Tile[] of size 2 containing the Tiles is contains
     * @return Initialized spaces with empty values
     */
    private HashMap<Integer, Tile[]> initializeSpacesMap() {
        HashMap<Integer, Tile[]> spaces = new HashMap<>();
        Tile[] tempArray;
        //Add new CoorPair hash and empty Square[] of size 2
        for ( int i = 0; i < 4; i++ ) {
            for (int j = 0; j < 4; j++) {
                tempArray = new Tile[1];
                tempArray[0] = null;
                spaces.put(spaceCoors[i][j].hashCode(), tempArray);
            }
        }

        return spaces;
    }

    /**
     * Creates ArrayList<CoorPair></CoorPair> representing each space on the board
     * @return ArrayList<CoorPair></CoorPair> containing each spaces coordinates on the board
     */
    private CoorPair[][] initializeSpaces() {
        final int HALF_TILE = (int)Math.ceil(105.0/2);

        CoorPair[][] spaces = new CoorPair[4][4];

        for ( int i = 0; i < 4; i++ ) {
            for ( int j = 0; j < 4; j++ ) {
                //Calculate and store coordinates for each space
                spaces[i][j] = new CoorPair(
                        (15 * (j+1)) + (105 * j) + HALF_TILE,
                        (15 * (i+1)) + (105 * i) + HALF_TILE
                );
            }
        }

        return spaces;
    }

    /**
     * Initializes all tiles for use in play
     * @return array of all empty initialized Tiles
     */
    private Tile[] initializeTiles() {
        Tile[] tiles = new Tile[16];

        for ( int i = 0; i < 16; i++ ) {
            tiles[i] = new Tile();
        }

        return tiles;
    }

    /**
     * Spawns new tile in random empty space
     * @param spaces all spaces on the board
     * @param tiles list of all tiles
     */
    private void spawnTile(HashMap<Integer, Tile[]> spaces, Tile[] tiles) {
        Random rand = new Random();
        ArrayList<CoorPair> possibleSpawnPoints = new ArrayList<>();

        weight--;

        //Finds all possible coordinates to spawn new tile
        for ( int i = 0; i < 4; i++ ) {
            for (int j = 0; j < 4; j++) {
                if (spaces.get(spaceCoors[i][j].hashCode())[0] == null) {
                    possibleSpawnPoints.add(spaceCoors[i][j]);
                }
            }
        }

        //Place the first tile found that's not on the board already
        for ( Tile tile : tiles ) {
            if (!tile.getStatus()) {
                //Generates new random spawn point
                CoorPair spawnPoint = possibleSpawnPoints.get(rand.nextInt(possibleSpawnPoints.size()));
                //Set status to true
                tile.setOnBoardStatus(true);
                //Set coordinates
                tile.setCoordinates(spawnPoint);
                //Animate spawn by blowing up tile
                AnimationTimer timer = new AnimationTimer() {
                    int dimensions = 0;
                    @Override
                    public void handle(long l) {
                        //Stop once reached tile size
                        if ( dimensions == 105 ) this.stop();
                        tile.setDimensions(dimensions+=5);
                    }
                };
                timer.start();
                //Set value of tile
                if (weight > 0) {
                    tile.setValue(2);
                } else {
                    tile.setValue((rand.nextInt(10) > 5) ? 2 : 4);
                }
                //Register tile in spaces map
                spaces.get(spawnPoint.hashCode())[0] = tile;
                return;
                //return(1);
            }
        }
        //return(0);
    }

    /**
     * Places tile at coordinates specified
     * @param tileToMove Tile to be placed
     * @param move coordinates to place tile at
     */
    private void placeTile(Tile tileToMove, CoorPair move) {
        int hash = move.hashCode();

        //Check if there is a tile where we're moving
        if ( spaces.get(hash)[0] != null ) {
            Tile temp = spaces.get(hash)[0];

            //remove this tile from map
            spaces.get(tileToMove.getCoordinates().hashCode())[0] = null;
            //remove tile we're moving from board
            tileToMove.remove();
            //Double value of tile occupying space
            temp.doubleValue();
            score.setText(Integer.toString(Integer.parseInt(score.getText()) + temp.getValue()));
        } else {
            //Change tile location in map
            spaces.get(tileToMove.getCoordinates().hashCode())[0] = null;
            spaces.get(hash)[0] = tileToMove;
            tileToMove.setCoordinates(move);
        }
    }

    /**
     * Finds valid move if it exists for provided tile and chosen direction
     * @param tile Tile to be moved
     * @param dir Direction to be moved in
     * @return CoorPair that can either represent a CoorPair, or null indicating no valid move
     */
    private CoorPair findValidMove(Tile tile, Direction dir) {
        CoorPair move = null;
        Tile temp;
        int row = 0;
        int col = 0;

        outerloop:
        for (; col < 4; col++ ) {
            for (row = 0; row < 4; row++ ) {
                //Find location of Tile
                if ( tile.getCoordinates().coorEquals(spaceCoors[row][col])) break outerloop;
            }
        }

        //Check based upon direction
        switch (dir) {
            case NORTH -> {
                row--;
                for (; row >= 0; row--) {
                    temp = spaces.get(spaceCoors[row][col].hashCode())[0];
                    if ( temp == null ) {
                        //Update valid move
                        move = spaceCoors[row][col];
                    } else {
                        //Space is occupied
                        if (temp.getValue() == tile.getValue()) move = spaceCoors[row][col];
                        else break;
                    }
                }
            }
            case EAST -> {
                col++;
                for (; col < 4; col++) {
                    temp = spaces.get(spaceCoors[row][col].hashCode())[0];
                    if ( temp == null ) {
                        //Update valid move
                        move = spaceCoors[row][col];
                    } else {
                        //Space is occupied
                        if (temp.getValue() == tile.getValue()) move = spaceCoors[row][col];
                        else break;
                    }
                }
            }
            case SOUTH -> {
                row++;
                for (; row < 4; row++) {
                    temp = spaces.get(spaceCoors[row][col].hashCode())[0];
                    if ( temp == null ) {
                        //Update valid move
                        move = spaceCoors[row][col];
                    } else {
                        //Space is occupied
                        if (temp.getValue() == tile.getValue()) move = spaceCoors[row][col];
                        else break;
                    }
                }

            }
            case WEST -> {
                col--;
                for (; col >= 0; col--) {
                    temp = spaces.get(spaceCoors[row][col].hashCode())[0];
                    if ( temp == null ) {
                        //Update valid move
                        move = spaceCoors[row][col];
                    } else {
                        //Space is occupied
                        if (temp.getValue() == tile.getValue()) move = spaceCoors[row][col];
                        else break;
                    }
                }

            }
        }

        return move;
    }

    /**
     * Calls @findValidMove
     * @param tile Tile to be checking
     * @param dir Direction we're moving in.
     * @return false if no valid move found
     *         true if valid move found
     */
    private boolean computeTileMoves(Tile tile, Direction dir) {
        CoorPair tempPair;

        //System.out.println("TILE IS NULL?: " + (tile == null));
        //Try and find valid move
        tempPair = findValidMove(tile, dir);

        if ( tempPair == null ) return false;

        //Move tile
        placeTile(tile, tempPair);
        return true;
    }
}
