package controller;

import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@NoArgsConstructor
public class PickupController {
    private final List<Pickup> pickups = new ArrayList<>();

    public PickupController(List<Pickup> pickups) {
        this.pickups.addAll(pickups);
    }

    @SneakyThrows
    public void spawnPickup(double x, double y) {
        Random random = new Random();

        switch (random.nextInt(4)) {
            case 0 -> {
                int xPos = random.nextInt((int) x - 50);
                int yPos = random.nextInt((int) y - 50);
                pickups.add(new HealthPickup(xPos, yPos));
            }
            case 1 -> {
                int xPos = random.nextInt((int) x - 50);
                int yPos = random.nextInt((int) y - 50);
                pickups.add(new TripleShootingPickup(xPos, yPos));
            }
            case 2 -> {
                int xPos = random.nextInt((int) x - 50);
                int yPos = random.nextInt((int) y - 50);
                pickups.add(new RapidFirePickup(xPos, yPos));
            }
            default -> {
                int xPos = random.nextInt((int) x - 50);
                int yPos = random.nextInt((int) y - 50);
                pickups.add(new SpeedPickup(xPos, yPos));
            }
        }
    }

    public void getInactive() {
        List<Pickup> toRemove = new ArrayList<>();
        for(Pickup pickup : pickups) {
            if(!pickup.isActive()) {
                toRemove.add(pickup);
            }
        }
        toRemove.forEach(pickups::remove);
    }

    public List<Pickup> getPickups() {
        return pickups;
    }

}
