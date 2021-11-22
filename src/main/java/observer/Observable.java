package observer;

public interface Observable {
    void register(BulletObserver observer);
    void unregister(BulletObserver observer);
}
