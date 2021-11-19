package player;

import javafx.scene.input.KeyCode;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Input {
    private KeyCode keyForward;
    private KeyCode keyRotateLeft;
    private KeyCode keyBackward;
    private KeyCode keyRotateRight;
    private KeyCode keyShoot;
}
