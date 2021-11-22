package model;

import dto.PickupDTO;
import dto.PickupType;
import edu.austral.dissis.starships.vector.Vector2;

public interface Pickup extends GameObject {
    boolean isActive();
    void setActive(boolean active);

    PickupType getType();

    PickupDTO toDTO();

    @Override
    default double getDirection() {return 0;}

    @Override
    default boolean shouldBeRemoved() {return !isActive();}

    @Override
    default double getSpeed() {return 0;}

    @Override
    default void move(Vector2 vector) {}
}
