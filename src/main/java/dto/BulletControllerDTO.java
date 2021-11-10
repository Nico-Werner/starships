package dto;

import controller.BulletController;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
public class BulletControllerDTO implements Serializable {
    List<BulletDTO> bullets;

    public BulletController toBulletController() {
        return new BulletController(this.bullets.stream().map(BulletDTO::toBullet).collect(Collectors.toList()));
    }
}
