package collider;

import model.GameObject;
import visitor.ColliderVisitor;

import java.util.List;
import java.util.stream.Collectors;

public class ColliderManager {

    private final ColliderVisitor visitor = new ColliderVisitor();

    public List<CollideableObject> generateColliders(List<GameObject> gameObjects) {
        return gameObjects.stream().map(gameObject -> {
            gameObject.accept(visitor);
            return visitor.getResult();
        }).collect(Collectors.toList());
    }

}
