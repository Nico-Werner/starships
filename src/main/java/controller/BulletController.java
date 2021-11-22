package controller;

import dto.BulletControllerDTO;
import model.Bullet;

import java.util.ArrayList;
import java.util.List;

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
            if(bullet.getPosition().getX() < -100 || bullet.getPosition().getX() > width + 100 || bullet.getPosition().getY() < -100 || bullet.getPosition().getY() > height + 100) {
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
