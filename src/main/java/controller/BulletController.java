package controller;

import dto.BulletControllerDTO;
import model.Bullet;

import java.util.*;


/* TODO unificar tema controllers, armar una clase que se encargue de renderizar todos los "game objects" y usar
    visitor para poder especificar la imagen a renderizar para cada tipo de objeto, manejar el tamaño y demás...
    Podría usar un Map<GameObject, ImageView> y si no la tiene cargarla, si ya la tiene simplemente actualizarla acorde
    a la posición. Cargar la imagen una sola vez.
 */
public class BulletController {
    List<Bullet> bullets = new ArrayList<>();

    public BulletController() {}

    public BulletController(List<Bullet> bullets) {
        this.bullets = bullets;
    }

    public void addBullet(Bullet bullet) {
        bullets.add(bullet);
    }

    public List<Bullet> getBullets() {
        return bullets;
    }

    public void removeDeadBullets(double width, double height) {
        for (int i = 0; i < bullets.size(); i++) {
            Bullet bullet = bullets.get(i);
            if (bullet.getSpeed() == 0) {
                bullets.remove(bullet);
            }
            if(bullet.getShape().getLayoutX() < -100 || bullet.getShape().getLayoutX() > width + 100 || bullet.getShape().getLayoutY() < -100 || bullet.getShape().getLayoutY() > height + 100) {
                bullets.remove(bullet);
            }
        }
    }

    public BulletControllerDTO toDTO() {
        return BulletControllerDTO.builder()
                .bullets(bullets.stream().map(Bullet::toDTO).toList())
                .build();
    }
}
