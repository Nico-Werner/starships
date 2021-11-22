package collider;

import edu.austral.dissis.starships.collision.Collider;
import model.GameObject;

public interface MyCollider extends Collider<MyCollider> {

    GameObject getGameObject();

}
