package observer;

import model.Ship;

public interface BulletObserver {
    // TODO: agregar uno para Asteroid y para Ship para poder manejar la logica de cuantos puntos agregar
    // renombre a onHit
    void updateScore(double points);
    boolean isSelf(Ship ship);
}
