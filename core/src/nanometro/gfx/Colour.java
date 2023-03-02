package nanometro.gfx;

import nanometro.GameScreen;

import java.util.List;

public class Colour {
    static List<Colour> colourList = GameScreen.colourList;
    public String subColour1, subColour2, subColour3;
    public boolean occupied = false;
    public Colour(String colourHex) {
        this.subColour1 = colourHex;
        this.subColour2 = colourHex;
        this.subColour3 = colourHex;
        colourList.add(this);
    }

    public static Colour requestColour() {
        for (Colour c: colourList) {
            if (c.occupied == false) {
                c.occupied = true;
                return c;
            }
        }
        return null;
    }

    public static void releaseColour(Colour c) {
        c.occupied = false;
    }
}
