package ui;

import javafx.geometry.Pos;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class MenuItem extends StackPane {
    public MenuItem(String name) {
        Rectangle bg = new Rectangle(300, 24);

        LinearGradient gradient = new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.BLACK),
                new Stop(0.2, Color.DARKGRAY));

        bg.setFill(gradient);
        bg.setVisible(false);
        bg.setEffect(new DropShadow(5, 0, 5, Color.BLACK));

        Text text = new Text(name + "    ");
        text.setFill(Color.LIGHTGRAY);
        text.setFont(Font.font(20));

        setAlignment(Pos.CENTER_RIGHT);
        getChildren().addAll(bg, text);

        setOnMouseEntered(event -> {
            bg.setVisible(true);
            text.setFill(Color.WHITE);
        });

        setOnMouseExited(event -> {
            bg.setVisible(false);
            text.setFill(Color.LIGHTGRAY);
        });

        setOnMousePressed(event -> {
            bg.setFill(Color.WHITE);
            text.setFill(Color.BLACK);
        });

        setOnMouseReleased(event -> {
            bg.setFill(gradient);
            text.setFill(Color.WHITE);
        });
    }
}