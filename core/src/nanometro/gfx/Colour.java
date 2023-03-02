package nanometro.gfx;

import nanometro.GameScreen;

import java.util.List;

public class Colour {
    static List<Colour> colourList = GameScreen.colourList;
    public String subColour1, subColour2, subColour3;
    public boolean occupied = false;
    private static final float ratio2 = 0.5f;
    private static final float ratio3 = 0.8f;
    public Colour(String colourHex) {
        this.subColour1 = colourHex;
        int r, g, b;
        r = Integer.parseInt(colourHex.substring(1, 3), 16);
        g = Integer.parseInt(colourHex.substring(3, 5), 16);
        b = Integer.parseInt(colourHex.substring(5, 7), 16);
        r = (int) (r * ratio2);
        g = (int) (g * ratio2);
        b = (int) (b * ratio2);
        this.subColour2 = String.format("#%02x%02x%02x", r, g, b);
        r = Integer.parseInt(colourHex.substring(1, 3), 16);
        g = Integer.parseInt(colourHex.substring(3, 5), 16);
        b = Integer.parseInt(colourHex.substring(5, 7), 16);
        r = (int) (255 - (255-r) * ratio3);
        g = (int) (255 - (255-g) * ratio3);
        b = (int) (255 - (255-b) * ratio3);
        this.subColour3 = String.format("#%02x%02x%02x", r, g, b);
        colourList.add(this);
    }

    public static Colour requestColour() {
        for (Colour c: colourList) {
            if (!c.occupied) {
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
