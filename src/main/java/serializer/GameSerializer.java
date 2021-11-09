package serializer;

import java.io.*;

public class GameSerializer {
    public static void saveGame(GameState state) {
        if(state == null) {
            return;
        }
        try {
            FileOutputStream file = new FileOutputStream("saved-game");
            ObjectOutputStream out = new ObjectOutputStream(file);

            out.writeObject(state);

            out.close();
            file.close();
        }

        catch(IOException ex) {
            ex.printStackTrace();
        }
    }

    public static GameState loadGame() {
        GameState state = null;
        try {
            // Reading the object from a file
            FileInputStream file = new FileInputStream("saved-game");
            ObjectInputStream in = new ObjectInputStream(file);

            // Method for deserialization of object
            state = (GameState) in.readObject();

            in.close();
            file.close();
        }

        catch(IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }

        return state;
    }
}
