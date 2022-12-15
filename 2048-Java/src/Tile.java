/*
 * Each square object on the board
 */

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class Tile {
    private final int HALF_TILE = (int)Math.ceil(105.0/2);
    private final TileNode node;
    private int value;
    private CoorPair coordinates;
    private boolean onBoard;
    public Tile() { node = new TileNode(); onBoard = false;}

    public void setDimensions(int n) {
        node.getNode().setWidth(n);
        node.getNode().setHeight(n);
    }

    public void setCoordinates(CoorPair coordinates) {
        this.coordinates = coordinates;
        node.getStack().setLayoutX(coordinates.xCoor() - HALF_TILE);
        node.getStack().setLayoutY(coordinates.yCoor() - HALF_TILE);
    }

    public void setValue(int n) {
        value = n;
        node.changeText(n);
    }

    public TileNode getNode() {
        return node;
    }

    public CoorPair getCoordinates() {
        return coordinates;
    }

    public int getValue() {
        return value;
    }

    public void doubleValue() {
        value <<= 1;
        node.changeText(value);
    }

    public void place(CoorPair coordinates) {
        onBoard = true;
        setDimensions(105);
        setCoordinates(coordinates);
    }
    public void remove() {
        onBoard = false;
        setDimensions(0);
        node.changeText(0);
    }

    public boolean getStatus() {
        return onBoard;
    }
    public void setStatus(boolean status) {
        onBoard = status;
    }


    public static class TileNode {
        Text text;
        Rectangle node;
        StackPane stack;
        public TileNode() {
            node = new Rectangle();
            text = new Text();
            text.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 25));
            text.setFill(Color.WHITE);
            stack = new StackPane();
            stack.getChildren().addAll(node, text);
        }

        public Rectangle getNode() {
            return node;
        }
        public StackPane getStack() {
            return stack;
        }
        public void changeText(int v) {
            text.setText((v > 0) ? ""+v : "");
        }
    }
    
}
