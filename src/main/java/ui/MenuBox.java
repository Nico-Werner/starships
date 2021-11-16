package ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class MenuBox extends StackPane {
    public MenuBox(String title, MenuItem... items) {
        Rectangle bg = new Rectangle(300, 1080);
        bg.setOpacity(0.2);

        DropShadow shadow = new DropShadow(7, 5, 0, Color.BLACK);
        shadow.setSpread(0.8);

        bg.setEffect(shadow);

        Text text = new Text(title + " ");
        text.setFont(Font.font("Verdana", FontWeight.BOLD, 30));
        text.setFill(Color.WHITE);

        Line hSep = new Line();
        hSep.setEndX(250);
        hSep.setStroke(Color.DARKRED);
        hSep.setOpacity(0.4);

        Line vSep = new Line();
        vSep.setStartX(300);
        vSep.setEndX(300);
        vSep.setEndY(1080);
        vSep.setStroke(Color.DARKRED);
        vSep.setOpacity(0.4);

        VBox vBox = new VBox();
        vBox.setAlignment(Pos.TOP_RIGHT);
        vBox.setPadding(new Insets(60, 0, 0, 0));
        vBox.getChildren().addAll(text, hSep);
        vBox.getChildren().addAll(items);

        setAlignment(Pos.TOP_RIGHT);
        getChildren().addAll(bg, vSep, vBox);
    }
}
