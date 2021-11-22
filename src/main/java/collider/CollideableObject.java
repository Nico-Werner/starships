package collider;

import javafx.scene.shape.Shape;
import lombok.AllArgsConstructor;
import model.GameObject;
import org.jetbrains.annotations.NotNull;

@AllArgsConstructor
public class CollideableObject implements MyCollider {
    private GameObject gameObject;
    private Shape shape;

    @Override
    public @NotNull Shape getShape() {
        return shape;
    }

    @Override
    public void handleCollisionWith(@NotNull MyCollider collider) {
        gameObject.handleCollisionWith(collider);
    }

    public GameObject getGameObject() {
        return gameObject;
    }
}
