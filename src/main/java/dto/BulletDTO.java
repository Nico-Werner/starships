package dto;

import edu.austral.dissis.starships.vector.Vector2;
import lombok.Builder;
import lombok.Data;
import model.Bullet;

import java.io.Serializable;

@Data
@Builder
public class BulletDTO implements Serializable {
    double posX;
    double posY;
    double rotate;
    double radius;
    double speed;
    double damage;

    public Bullet toBullet() {
        return new Bullet(Vector2.vector(posX, posY), rotate, speed, damage);
    }
}
