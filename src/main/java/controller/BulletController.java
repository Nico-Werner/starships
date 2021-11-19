package controller;

import dto.BulletControllerDTO;
import edu.austral.dissis.starships.file.ImageLoader;
import edu.austral.dissis.starships.vector.Vector2;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import lombok.SneakyThrows;
import model.Bullet;
import view.BulletView;

import java.util.*;


/* TODO unificar tema controllers, armar una clase que se encargue de renderizar todos los "game objects" y usar
    visitor para poder especificar la imagen a renderizar para cada tipo de objeto, manejar el tamaño y demás...
 */
public class BulletController {
    List<Bullet> bullets = new ArrayList<>();
    List<BulletView> bulletViews = new ArrayList<>();

    public BulletController() {}

    public BulletController(List<Bullet> bullets) {
        this.bullets = bullets;
        this.bulletViews = new ArrayList<>();
        bullets.forEach(bullet -> bulletViews.add(null));
    }

    public void addBullet(Bullet bullet) {
        bullets.add(bullet);
        bulletViews.add(null);
    }

    @SneakyThrows
    public List<ImageView> renderBullets() {
        ImageLoader imageLoader = new ImageLoader();

        Image image = imageLoader.loadFromResources("bullet.png", 20, 20);
        List<ImageView> result = new ArrayList<>();

        for (int i = 0; i < bullets.size(); i++) {
            Bullet bullet = bullets.get(i);
            BulletView bulletView = bulletViews.get(i);
            if (bulletView == null) {
                bulletView = new BulletView(image, bullet.getShape().getLayoutX(), bullet.getShape().getLayoutY(), bullet.getShape().getRotate());
                bulletViews.set(i, bulletView);
                result.add(bulletView.getImageView());
            }
        }

        return result;
    }

    public void updatePositions(Double secondsSinceLastFrame) {
        for (int i = 0; i < bullets.size(); i++) {
            Bullet bullet = bullets.get(i);
            BulletView bulletView = bulletViews.get(i);
            Vector2 movementVector = Vector2.vectorFromModule((bullet.getSpeed() * secondsSinceLastFrame), (Math.toRadians(bullet.getShape().getRotate())));
            Vector2 from = Vector2.vector(bullet.getShape().getLayoutX(), bullet.getShape().getLayoutY());
            Vector2 to = from.add(movementVector);
            bulletView.move(to);
            bullet.move(to);
        }
    }

    public List<Bullet> getBullets() {
        return bullets;
    }

    public List<ImageView> removeDeadBullets(double width, double height) {
        List<ImageView> result = new ArrayList<>();

        for (int i = 0; i < bullets.size(); i++) {
            Bullet bullet = bullets.get(i);
            BulletView bulletView = bulletViews.get(i);
            if (bullet.getSpeed() == 0) {
                result.add(bulletView.getImageView());
                bullets.remove(bullet);
                bulletViews.remove(bulletView);
            }

            if(bullet.getShape().getLayoutX() < -100 || bullet.getShape().getLayoutX() > width + 100 || bullet.getShape().getLayoutY() < -100 || bullet.getShape().getLayoutY() > height + 100) {
                result.add(bulletView.getImageView());
                bullets.remove(bullet);
                bulletViews.remove(bulletView);
            }
        }
        return result;
    }

    public BulletControllerDTO toDTO() {
        return BulletControllerDTO.builder()
                .bullets(bullets.stream().map(Bullet::toDTO).toList())
                .build();
    }
}
