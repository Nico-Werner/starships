package visitor;

import edu.austral.dissis.starships.file.ImageLoader;
import javafx.scene.image.Image;
import lombok.Getter;
import model.Asteroid;
import model.Bullet;
import model.Pickup;
import model.Ship;
import view.*;

import java.io.IOException;

public class ImageVisitor implements GameObjectVisitor {
    private Image asteroidImage;
    private Image shipImage;
    private Image bulletImage;
    private Image healthPickupImage;
    private Image speedPickupImage;
    private Image tripleShootingPickupImage;
    private Image rapidFirePickupImage;

    @Getter
    private View result;

    public ImageVisitor() {
        ImageLoader imageLoader = new ImageLoader();
        try {
            this.asteroidImage = imageLoader.loadFromResources("asteroid.png", 500, 500);
            this.shipImage = imageLoader.loadFromResources("starship.gif", 100, 100);
            this.bulletImage = imageLoader.loadFromResources("bullet.png", 20, 20);
            this.healthPickupImage = imageLoader.loadFromResources("health.png", 50, 50);
            this.speedPickupImage = imageLoader.loadFromResources("speed.png", 50, 50);
            this.tripleShootingPickupImage = imageLoader.loadFromResources("triple-shot.png", 50, 50);
            this.rapidFirePickupImage = imageLoader.loadFromResources("rapid-fire.png", 50, 50);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void visitAsteroid(Asteroid asteroid) {
        result = new AsteroidView(asteroidImage, asteroid.getPosition().getX(), asteroid.getPosition().getY(), asteroid.getDirection());
        result.getImageView().setFitWidth(asteroid.getHealth());
        result.getImageView().setFitHeight(asteroid.getHealth());
    }

    @Override
    public void visitShip(Ship ship) {
        result = new ShipView(shipImage, (int) ship.getPosition().getX(), (int) ship.getPosition().getY());
    }

    @Override
    public void visitBullet(Bullet bullet) {
        result = new BulletView(bulletImage, bullet.getPosition().getX(), bullet.getPosition().getY(), bullet.getDirection());
    }

    @Override
    public void visitHealthPickup(Pickup pickup) {
        result = new PickupView(healthPickupImage, pickup.getPosition().getX(), pickup.getPosition().getY());
    }

    @Override
    public void visitSpeedPickup(Pickup pickup) {
        result = new PickupView(speedPickupImage, pickup.getPosition().getX(), pickup.getPosition().getY());
    }

    @Override
    public void visitRapidFirePickup(Pickup pickup) {
        result = new PickupView(rapidFirePickupImage, pickup.getPosition().getX(), pickup.getPosition().getY());
    }

    @Override
    public void visitTripleShootingPickup(Pickup pickup) {
        result = new PickupView(tripleShootingPickupImage, pickup.getPosition().getX(), pickup.getPosition().getY());
    }
}
