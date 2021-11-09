package dto;

import controller.BulletController;
import controller.ShipController;
import edu.austral.dissis.starships.file.ImageLoader;
import javafx.scene.image.Image;
import lombok.Builder;
import lombok.Data;
import lombok.SneakyThrows;
import view.ShipView;

import java.io.Serializable;

@Data
@Builder
public class ShipControllerDTO implements Serializable {
    private String imageName;
    private ShipDTO ship;

    @SneakyThrows
    public ShipController toShipController() {
        ImageLoader imageLoader = new ImageLoader();
        ShipView shipView = new ShipView(imageLoader.loadFromResources(imageName, 100, 100), (int) ship.getPosX(), (int) ship.getPosY());
        shipView.getImageView().setRotate(ship.getAngle());
        return ShipController.builder()
                .shipView(shipView)
                .ship(ship.toShip())
                .bulletController(new BulletController())
                .build();
    }
}
