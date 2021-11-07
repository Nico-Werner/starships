package player;

import controller.ShipController;
import edu.austral.dissis.starships.game.KeyTracker;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class Player {
    private int id;
    private int score;
    private int lives;
    private ShipController shipController;

    KeyCode keyForward;
    KeyCode keyRotateLeft;
    KeyCode keyBackward;
    KeyCode keyRotateRight;
    KeyCode keyShoot;



    public void updateInput(Pane pane, KeyTracker keyTracker, double secondsSinceLastFrame) {
        keyTracker.getKeySet().forEach(keyCode -> {
            if (keyCode == keyForward) shipController.forward(secondsSinceLastFrame, pane);
            else if (keyCode == keyBackward) shipController.backward(secondsSinceLastFrame, pane);
            else if (keyCode == keyRotateLeft) shipController.rotateLeft(secondsSinceLastFrame, pane);
            else if (keyCode == keyRotateRight) shipController.rotateRight(secondsSinceLastFrame, pane);
            else if (keyCode == keyShoot) {
                shipController.fire();
                List<ImageView> imageViews = shipController.getBulletController().renderBullets();
                pane.getChildren().addAll(imageViews);
            }
        });
    }
}
