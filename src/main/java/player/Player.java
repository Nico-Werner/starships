package player;

import controller.ShipController;
import dto.PlayerDTO;
import edu.austral.dissis.starships.game.KeyTracker;
import javafx.scene.layout.Pane;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import model.Asteroid;
import model.Ship;
import observer.BulletObserver;

import java.io.Serializable;

@Data
@AllArgsConstructor
@Builder
public class Player implements Serializable, BulletObserver {
    private int id;
    private int score;
    private int lives;
    private ShipController shipController;

    KeyCodeInput input;

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
    public void onHit(double damage, Asteroid asteroid) {
        score += damage;
        if(asteroid.shouldBeRemoved()) score += damage;
        shipController.getShipView().updatePoints(score);
    }

    @Override
    public void onHit(double damage, Ship ship) {
        score += damage / 10;
        if(ship.shouldBeRemoved()) score += damage / 10;
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

    public void updateDeath() {
        if (shipController.getShip().shouldBeRemoved())
            lives--;
    }

    public boolean isShipDead() {
        return shipController.getShip().getHealth() <= 0;
    }

    public void updateShipStyle(String shipName) {
        shipController.updateShipStyle(shipName);
    }

    public void revive() {
        shipController.resetShip();
    }
}
