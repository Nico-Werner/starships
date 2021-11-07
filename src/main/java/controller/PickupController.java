package controller;

import edu.austral.dissis.starships.file.ImageLoader;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import lombok.SneakyThrows;
import model.HealthPickup;
import model.Pickup;
import model.TripleShootingPickup;
import view.PickupView;

import java.util.*;

public class PickupController {
    Map<Pickup, PickupView> pickups = new HashMap<>();

    @SneakyThrows
    public ImageView spawnPickup(ImageLoader imageLoader, double x, double y) {
        Random random = new Random();

        if(random.nextBoolean()){
            Image image = imageLoader.loadFromResources("health.png", 50, 50);
            int xPos = random.nextInt((int) x - 50);
            int yPos = random.nextInt((int) y - 50);
            PickupView pickupView = new PickupView(image, xPos, yPos);
            pickups.put(new HealthPickup(xPos, yPos), pickupView);
            return pickupView.getImageView();
        } else {
            Image image = imageLoader.loadFromResources("triple-shot.png", 50, 50);
            int xPos = random.nextInt((int) x - 50);
            int yPos = random.nextInt((int) y - 50);
            PickupView pickupView = new PickupView(image, xPos, yPos);
            pickups.put(new TripleShootingPickup(xPos, yPos), pickupView);
            return pickupView.getImageView();
        }
    }

    public List<ImageView> getInactive() {
        List<ImageView> result = new ArrayList<>();
        List<Pickup> toRemove = new ArrayList<>();
        for(Pickup pickup : pickups.keySet()) {
            if(!pickup.isActive()) {
                result.add(pickups.get(pickup).getImageView());
                toRemove.add(pickup);
            }
        }
        toRemove.forEach(pickups::remove);
        return result;
    }

    public List<Pickup> getPickups() {
        return pickups.keySet().stream().toList();
    }
}
