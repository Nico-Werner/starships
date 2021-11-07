package model;

import collider.MyCollider;

public interface Pickup extends MyCollider {
    boolean isActive();
}
