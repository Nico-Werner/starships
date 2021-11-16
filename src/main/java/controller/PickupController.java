package controller;

import edu.austral.dissis.starships.file.ImageLoader;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import model.*;
import view.PickupView;

import java.io.IOException;
import java.util.*;

@NoArgsConstructor
public class PickupController {
    private final Map<Pickup, PickupView> pickups = new HashMap<>();

    public PickupController(List<Pickup> pickups) {
        ImageLoader imageLoader = new ImageLoader();
        pickups.forEach(pickup -> {
            Image image = null;
            try {
                switch (pickup.getType()){
                    case HEALTH -> image = imageLoader.loadFromResources("health.png", 50, 50);
                    case SPEED -> image = imageLoader.loadFromResources("speed.png", 50, 50);
                    case RAPID_FIRE -> image = imageLoader.loadFromResources("rapid-fire.png", 50, 50);
                    case TRIPLE_SHOOTING -> image = imageLoader.loadFromResources("triple-shot.png", 50, 50);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.pickups.put(pickup, new PickupView(image, pickup.getShape().getLayoutX(), pickup.getShape().getLayoutY()));
        });
    }

    @SneakyThrows
    public ImageView spawnPickup(ImageLoader imageLoader, double x, double y) {
        Random random = new Random();

        switch (random.nextInt(4)) {
            case 0 -> {
                Image image = imageLoader.loadFromResources("health.png", 50, 50);
                int xPos = random.nextInt((int) x - 50);
                int yPos = random.nextInt((int) y - 50);
                PickupView pickupView = new PickupView(image, xPos, yPos);
                pickups.put(new HealthPickup(xPos, yPos), pickupView);
                return pickupView.getImageView();
            }
            case 1 -> {
                Image image = imageLoader.loadFromResources("triple-shot.png", 50, 50);
                int xPos = random.nextInt((int) x - 50);
                int yPos = random.nextInt((int) y - 50);
                PickupView pickupView = new PickupView(image, xPos, yPos);
                pickups.put(new TripleShootingPickup(xPos, yPos), pickupView);
                return pickupView.getImageView();
            }
            case 2 -> {
                Image image = imageLoader.loadFromResources("rapid-fire.png", 50, 50);
                int xPos = random.nextInt((int) x - 50);
                int yPos = random.nextInt((int) y - 50);
                PickupView pickupView = new PickupView(image, xPos, yPos);
                pickups.put(new RapidFirePickup(xPos, yPos), pickupView);
                return pickupView.getImageView();
            }
            default -> {
                Image image = imageLoader.loadFromResources("speed.png", 50, 50);
                int xPos = random.nextInt((int) x - 50);
                int yPos = random.nextInt((int) y - 50);
                PickupView pickupView = new PickupView(image, xPos, yPos);
                pickups.put(new SpeedPickup(xPos, yPos), pickupView);
                return pickupView.getImageView();
            }
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

    public List<ImageView> getViews() {
        return pickups.values().stream().map(PickupView::getImageView).toList();
    }
}
