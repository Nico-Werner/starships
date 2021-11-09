package model;

import collider.MyCollider;

public interface Pickup extends MyCollider {
    boolean isActive();

    @Override
    default void handleCollisionWith(Asteroid asteroid) {

    }

    @Override
    default void handleCollisionWith(Bullet bullet) {

    }

    @Override
    default void handleCollisionWith(Pickup pickup) {

    }
}
