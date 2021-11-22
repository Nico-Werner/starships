package player;

import controller.ShipController;
import dto.PlayerDTO;
import edu.austral.dissis.starships.game.KeyTracker;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import model.Ship;
import observer.BulletObserver;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class Player implements Serializable, BulletObserver {
    private int id;
    private int score;
    private int lives;
    private ShipController shipController;

    // TODO unificarlo en una clase "input" para no manejar keycodes directamente.
    // (idea: podría pasar el update al input y que devuelva un command...)
    // podria ser con generics y despues en una implementacion especificar que es para keyCode.
    Input input;

    public void updateInput(Pane pane, KeyTracker keyTracker, double secondsSinceLastFrame) {
        if(isDead()) return;
        keyTracker.getKeySet().forEach(keyCode -> {
            if (keyCode == input.getKeyForward()) shipController.forward(secondsSinceLastFrame, pane);
            else if (keyCode == input.getKeyBackward()) shipController.backward(secondsSinceLastFrame, pane);
            else if (keyCode == input.getKeyRotateLeft()) shipController.rotateLeft(secondsSinceLastFrame);
            else if (keyCode == input.getKeyRotateRight()) shipController.rotateRight(secondsSinceLastFrame);
            else if (keyCode == input.getKeyShoot()) {
                shipController.fire(this);
            }
        });
    }

    @Override
    public void updateScore(double points) {
        score += points;
        shipController.getShipView().updatePoints(score);
    }

    @Override
    public boolean isSelf(Ship ship) {
        return ship.equals(shipController.getShip());
    }

    public PlayerDTO toDTO() {
        return PlayerDTO.builder()
                .id(id)
                .score(score)
                .lives(lives)
                .shipController(shipController.toDTO())
                .keyForward(input.getKeyForward())
                .keyRotateLeft(input.getKeyRotateLeft())
                .keyBackward(input.getKeyBackward())
                .keyRotateRight(input.getKeyRotateRight())
                .keyShoot(input.getKeyShoot())
                .build();
    }

    public boolean isDead() {
        return shipController.getShip().getHealth() <= 0 && lives <= 0;
    }

    public ImageView updateDeath() {
        ImageView imageView = shipController.updateDeath();
        if(imageView == null) return null;
        lives--;
        return imageView;
    }

    public boolean isShipDead() {
        return shipController.getShip().getHealth() <= 0;
    }

    public void updateShipStyle(String shipName) {
        shipController.updateShipStyle(shipName);
    }
}
