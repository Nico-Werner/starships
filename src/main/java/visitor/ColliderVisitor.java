package visitor;

import collider.CollideableObject;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import model.Asteroid;
import model.Bullet;
import model.Pickup;
import model.Ship;

public class ColliderVisitor implements GameObjectVisitor{

    CollideableObject result;

    @Override
    public void visitAsteroid(Asteroid asteroid) {
        double colliderReduction = asteroid.getHealth() - 40 > 0 ? 40 : 10;
        Shape shape = new Rectangle(asteroid.getHealth() - colliderReduction, asteroid.getHealth() - colliderReduction);
        setPosition(shape, asteroid.getPosition().getX() + colliderReduction / 2, asteroid.getPosition().getY() + colliderReduction / 2, asteroid.getDirection());
        result = new CollideableObject(asteroid, shape);
    }

    @Override
    public void visitShip(Ship ship) {
        Shape shape = new Rectangle(70, 45);
        setPosition(shape, ship.getPosition().getX() + 15, ship.getPosition().getY() + 17.5, ship.getDirection());
        result = new CollideableObject(ship, shape);
    }

    @Override
    public void visitBullet(Bullet bullet) {
        Shape shape = new Circle(5);
        setPosition(shape, bullet.getPosition().getX(), bullet.getPosition().getY(), bullet.getDirection());
        result = new CollideableObject(bullet, shape);
    }

    @Override
    public void visitHealthPickup(Pickup pickup) {
        visitPickup(pickup);
    }

    @Override
    public void visitSpeedPickup(Pickup pickup) {
        visitPickup(pickup);
    }

    @Override
    public void visitRapidFirePickup(Pickup pickup) {
        visitPickup(pickup);
    }

    @Override
    public void visitTripleShootingPickup(Pickup pickup) {
        visitPickup(pickup);
    }

    private void visitPickup(Pickup pickup) {
        Shape shape = new Rectangle(50, 50);
        setPosition(shape, pickup.getPosition().getX(), pickup.getPosition().getY(), pickup.getDirection());
        result = new CollideableObject(pickup, shape);
    }

    private void setPosition(Shape shape, double x, double y, double direction) {
        shape.setLayoutX(x);
        shape.setLayoutY(y);
        shape.setRotate(Math.toDegrees(direction));
    }

    public CollideableObject getResult() {
        return result;
    }
}
