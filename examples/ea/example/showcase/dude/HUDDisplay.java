package ea.example.showcase.dude;

import ea.Scene;
import ea.actor.ActorGroup;
import ea.actor.TileContainer;

public class HUDDisplay extends ActorGroup {

    private static final int HUD_VALUE_LENGTH = 10;

    private final TileContainer background;
    private final TileContainer lines;

    private final String[] lineSources = {"game-assets/dude/hud/orb_red.png", //Line 0 -> Health
            "game-assets/dude/hud/orb_blue.png", //Line 1 -> Power
            "game-assets/dude/hud/orb_green.png" //Line 2 -> XP
    };

    /**
     * Konstruktor für Objekte der Klasse ActorGroup
     */
    public HUDDisplay(Scene scene) {
        super(scene);

        background = new TileContainer(scene, HUD_VALUE_LENGTH + 7, 4, 8, 16);

        //Initialize Standard-Parts of backgrounds.
        for (int i = 0; i < 4; i++) {
            background.setTileAt(0, i, "game-assets/dude/hud/orb_orange.png", 0, 0);
            background.setTileAt(1, i, "game-assets/dude/hud/orb_orange.png", 1, 0);
            background.setTileAt(4, i, "game-assets/dude/hud/orb_orange.png", 1, 0);
            for (int j = 0; j < HUD_VALUE_LENGTH; j++) {
                background.setTileAt(5 + j, i, "game-assets/dude/hud/back.png");
            }
            background.setTileAt(HUD_VALUE_LENGTH + 5, i, "game-assets/dude/hud/orb_orange.png", 1, 0);
            background.setTileAt(HUD_VALUE_LENGTH + 6, i, "game-assets/dude/hud/orb_orange.png", 2, 0);
        }

        //Line 0: HEALTH
        background.setTileAt(2, 0, "game-assets/dude/hud/num_black.png", 6, 2);
        background.setTileAt(3, 0, "game-assets/dude/hud/num_black.png", 7, 2);

        //Line 1: POWER
        background.setTileAt(2, 1, "game-assets/dude/hud/num_black.png", 8, 2);
        background.setTileAt(3, 1, "game-assets/dude/hud/num_black.png", 9, 2);

        //Line 2: XP
        background.setTileAt(2, 2, "game-assets/dude/hud/num_black.png", 4, 2);
        background.setTileAt(3, 2, "game-assets/dude/hud/num_black.png", 5, 2);

        //Line 3: Monneeeeyyy
        background.setTileAt(2, 3, "game-assets/dude/hud/num_black.png", 2, 2);
        background.setTileAt(3, 3, "game-assets/dude/hud/num_black.png", 3, 2);

        //LINE CONTENT
        lines = new TileContainer(scene, HUD_VALUE_LENGTH, 4, 8, 16);
        lines.position.move(5 * 8, 0);

        setLineValue(0, 10);
        setLineValue(1, 7);
        setLineValue(2, 3);
        for (int k = 0; k < 5; k++) {
            setNumberOf(k, 0);
        }

        add(background, lines);
    }

    private void setNumberOf(int numIndex, int numValue) {
        if (numValue < 0 || numValue > 9) {
            return;
        }
        int x = (numValue % 5) * 2;
        int y = numValue / 5;
        lines.setTileAt(numIndex * 2, 3, "game-assets/dude/hud/num_black.png", x, y);
        lines.setTileAt(numIndex * 2 + 1, 3, "game-assets/dude/hud/num_black.png", x + 1, y);
    }

    private void setLineValue(int lineIndex, int lineValue) {
        for (int i = 0; i < HUD_VALUE_LENGTH; i++) {
            if (i < lineValue) {
                //Voll ausgemaltes HUD
                lines.setTileAt(i, lineIndex, lineSources[lineIndex], 1, 0);
            } else if (i == lineValue) {
                //Ende des Striches
                lines.setTileAt(i, lineIndex, lineSources[lineIndex], 2, 0);
            } else {
                //Nicht gebraucht -> unsichtbar
                lines.setTileAt(i, lineIndex, null);
            }
        }
    }

    //public void setNumberA
}