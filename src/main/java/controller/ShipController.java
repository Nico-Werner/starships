package controller;

import edu.austral.dissis.starships.vector.Vector2;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import lombok.AllArgsConstructor;
import lombok.Getter;
import model.Ship;
import view.ShipView;

@AllArgsConstructor
@Getter
public class ShipController {
    private ShipView shipView;
    private Ship ship;
    private BulletController bulletController;

    public void forward(Double movement, Pane pane) {
        Vector2 movementVector = Vector2.vectorFromModule(movement, (Math.toRadians(shipView.getRotate()) - Math.PI/2));
        Vector2 from = Vector2.vector((float) shipView.getLayoutX(), (float) shipView.getLayoutY());
        moveShip(pane, movementVector, from);
    }

    public void backward(Double movement, Pane pane) {
        Vector2 movementVector = Vector2.vectorFromModule(-movement, (Math.toRadians(shipView.getRotate()) - Math.PI/2));
        Vector2 from = Vector2.vector(shipView.getLayoutX(), shipView.getLayoutY());
        moveShip(pane, movementVector, from);
    }

    public void rotateLeft(Double movement, Pane pane) {
        shipView.setRotate(shipView.getRotate() - movement);
        ship.getShape().setRotate(shipView.getRotate() - movement);
    }

    public void rotateRight(Double movement, Pane pane) {
        shipView.setRotate(shipView.getRotate() + movement);
        ship.getShape().setRotate(shipView.getRotate() + movement);
    }

    public ImageView updateDeath() {
        if(ship.getHealth() <= 0) return shipView.getImageView();
        else return null;
    }

    public void fire(BulletController bulletController) {
        ship.fire(bulletController);
    }

    public void fire() {
        ship.fire(bulletController);
    }

    private void moveShip(Pane pane, Vector2 movementVector, Vector2 from) {
        Vector2 to = from.add(movementVector);

        if(to.getX() > 0 && to.getX() < pane.getWidth() - 100 && to.getY() > 0 && to.getY() < pane.getHeight() - 100) {
            shipView.move(to);
            ship.move(to);
        }
    }
}
