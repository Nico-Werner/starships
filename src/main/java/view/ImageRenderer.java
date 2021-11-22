package view;

import edu.austral.dissis.starships.vector.Vector2;
import javafx.scene.layout.Pane;
import model.GameObject;
import visitor.ImageVisitor;

import java.util.ArrayList;
import java.util.List;

public class ImageRenderer {
    private final List<GameObject> gameObjects = new ArrayList<>();
    private final List<View> views = new ArrayList<>();
    private final ImageVisitor visitor = new ImageVisitor();
    private final Pane pane;

    public ImageRenderer(Pane pane) {
        this.pane = pane;
    }

    public void renderObjects(List<GameObject> toRender) {
        pane.getChildren().removeAll(views.stream().map(View::getImageView).toList());
        views.clear();
        gameObjects.clear();
        toRender.forEach(gameObject -> {
            if (!gameObjects.contains(gameObject)) {
                gameObject.accept(visitor);
                View result = visitor.getResult();
                gameObjects.add(gameObject);
                views.add(result);
                pane.getChildren().add(result.getImageView());
            }
            if(gameObject.shouldBeRemoved()) {
                pane.getChildren().remove(views.get(gameObjects.indexOf(gameObject)).getImageView());
                views.remove(views.get(gameObjects.indexOf(gameObject)));
                gameObjects.remove(gameObject);
                return;
            }
            render(gameObject);
        });
    }

    private void render(GameObject gameObject) {
        View view = views.get(gameObjects.indexOf(gameObject));
        Vector2 position = gameObject.getPosition();
        view.getImageView().setLayoutX(position.getX());
        view.getImageView().setLayoutY(position.getY());
        view.getImageView().setRotate(gameObject.getDirection());
    }
}
