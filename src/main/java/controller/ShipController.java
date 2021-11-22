package controller;

import dto.ShipControllerDTO;
import edu.austral.dissis.starships.vector.Vector2;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import model.Bullet;
import model.Ship;
import player.Player;
import view.ShipView;

import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@Getter
@Builder
public class ShipController implements Serializable {
    private ShipView shipView;
    private Ship ship;
    private BulletController bulletController;

    public void forward(Double secondsSinceLastFrame, Pane pane) {
        double movement = secondsSinceLastFrame * ship.getSpeed();
        Vector2 movementVector = Vector2.vectorFromModule(movement, Math.toRadians(ship.getDirection()) - Math.PI/2);
        Vector2 from = Vector2.vector(ship.getPosition().getX(), ship.getPosition().getY());
        Vector2 to = from.add(movementVector);
        if(isInBounds(pane, to)) {
            moveShip(to);
        }
    }

    private boolean isInBounds(Pane pane, Vector2 to) {
        return to.getX() > 0 && to.getX() < pane.getWidth() - shipView.getWidth() && to.getY() > 0 && to.getY() < pane.getHeight() - shipView.getHeight();
    }

    public void backward(Double secondsSinceLastFrame, Pane pane) {
        double movement = secondsSinceLastFrame * ship.getSpeed();
        Vector2 movementVector = Vector2.vectorFromModule(movement, Math.toRadians(ship.getDirection()) + Math.PI/2);
        Vector2 from = Vector2.vector(ship.getPosition().getX(), ship.getPosition().getY());
        Vector2 to = from.add(movementVector);
        if(isInBounds(pane, to)) {
            moveShip(to);
        }
    }

    public void rotateLeft(Double secondsSinceLastFrame) {
        double movement = secondsSinceLastFrame * ship.getSpeed();
        ship.setDirection(ship.getDirection() - movement);
    }

    public void rotateRight(Double secondsSinceLastFrame) {
        double movement = secondsSinceLastFrame * ship.getSpeed();
        ship.setDirection(ship.getDirection() + movement);
    }

    public ImageView updateDeath() {
        if(ship.getHealth() <= 0) {
            shipView.getHealthView().setVisible(false);
            shipView.getPoints().setVisible(false);
            return shipView.getImageView();
        }
        else return null;
    }

    public void fire(Player shooter) {
        List<Bullet> bullets = ship.fire();
        bullets.forEach(bulletController::addBullet);
        bullets.forEach(bullet -> bullet.register(shooter));
    }

    public void moveShip(Vector2 to) {
        ship.move(to);
        //ship.move(Vector2.vector(to.getX() + (shipView.getWidth() - ((Rectangle) ship.getShape()).getWidth())/2, to.getY() + (shipView.getHeight() - ((Rectangle) ship.getShape()).getHeight())/2));
    }

    public void updateHealth() {
        shipView.updateHealth(ship.getHealth());
    }

    public ShipControllerDTO toDTO() {
        return ShipControllerDTO.builder()
                .ship(ship.toDTO())
                .bulletController(bulletController.toDTO())
                .build();
    }

    public void updateShipStyle(String shipName) {
        shipView.updateShipStyle(shipName);
    }
}
