package player;

public interface Input<T> {
    T getKeyForward();

    T getKeyBackward();

    T getKeyRotateLeft();

    T getKeyRotateRight();

    T getKeyShoot();
}
