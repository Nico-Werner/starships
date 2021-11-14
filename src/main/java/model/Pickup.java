package model;

import collider.MyCollider;
import dto.PickupDTO;
import dto.PickupType;

public interface Pickup extends MyCollider {
    boolean isActive();
    void setActive(boolean active);

    PickupType getType();

    PickupDTO toDTO();
}
