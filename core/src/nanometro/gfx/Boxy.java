package nanometro.gfx;


/*
Path-like class
 */

import com.badlogic.gdx.math.Vector2;

public class Boxy {

    Vector2 startingPoint;
    Vector2 endingPoint;
    Vector2 controlPoint;
    Vector2 cornerPoint;

    public Boxy(Station upper, Station lower) {
//        startingPoint = upper.getPosition();
//        endingPoint = lower.getPosition();

        startingPoint = upper.getPlatform();
        endingPoint = lower.getPlatform();


        cornerPoint = new Vector2(startingPoint.x, endingPoint.y);
        if (startingPoint.dst(cornerPoint) >= endingPoint.dst(cornerPoint)) {
            controlPoint = startingPoint.cpy().lerp(cornerPoint, ((startingPoint.dst(cornerPoint) - endingPoint.dst(cornerPoint))/startingPoint.dst(cornerPoint)));
        } else {
            controlPoint = cornerPoint.cpy().lerp(endingPoint, (startingPoint.dst(cornerPoint)/endingPoint.dst(cornerPoint)));
        }
    }


    public Boxy(Vector2 u, Vector2 l) {
        startingPoint = u;
        endingPoint = l;

        cornerPoint = new Vector2(startingPoint.x, endingPoint.y);
        if (startingPoint.dst(cornerPoint) >= endingPoint.dst(cornerPoint)) {
            controlPoint = startingPoint.cpy().lerp(cornerPoint, ((startingPoint.dst(cornerPoint) - endingPoint.dst(cornerPoint))/startingPoint.dst(cornerPoint)));
        } else {
            controlPoint = cornerPoint.cpy().lerp(endingPoint, (startingPoint.dst(cornerPoint)/endingPoint.dst(cornerPoint)));
        }
    }

    public float ratio;
    public void valueAt(Vector2 v, float t) {
        ratio = (startingPoint.dst(controlPoint) / (startingPoint.dst(controlPoint) + endingPoint.dst(controlPoint)));
        if (ratio == 1) {
            v.set(startingPoint.cpy().lerp(endingPoint, t));
            return;
        }
        if (t <= ratio) {
            float r = this.approxLength() * t / startingPoint.dst(controlPoint);
            v.set(startingPoint.cpy().lerp(controlPoint, r));
        } else {
            float r = (this.approxLength() * t - startingPoint.dst(controlPoint)) / endingPoint.dst(controlPoint);
            v.set(controlPoint.cpy().lerp(endingPoint, r));
        }
//        v.set(startingPoint.cpy().lerp(endingPoint, t));
    }

    public float approxLength() {
        return (float) (
                Math.sqrt((startingPoint.x - controlPoint.x) * (startingPoint.x - controlPoint.x) + (startingPoint.y - controlPoint.y) * (startingPoint.y - controlPoint.y)) +
                Math.sqrt((controlPoint.x - endingPoint.x) * (controlPoint.x - endingPoint.x) + (controlPoint.y - endingPoint.y) * (controlPoint.y - endingPoint.y))
        );
    }

    public static void main(String[] args) {
        Boxy test = new Boxy(new Vector2(1, 2), new Vector2(3, 1));
        System.out.println(test.startingPoint);
        System.out.println(test.endingPoint);
        System.out.println(test.controlPoint);
        System.out.println(test.approxLength());
        System.out.println(test.ratio);
        Vector2 v = new Vector2();
        test.valueAt(v, 0.57f);
        System.out.println(v);
        test.valueAt(v, 0.6f);
        System.out.println(v);
        test.valueAt(v, 0.9f);
        System.out.println(v);


        Boxy test2 = new Boxy(new Vector2(2, 2), new Vector2(0, -1));
        System.out.println(test2.startingPoint);
        System.out.println(test2.endingPoint);
        System.out.println(test2.controlPoint);
        System.out.println(test2.approxLength());
        System.out.println(test2.ratio);
        Vector2 v2 = new Vector2();
        test.valueAt(v2, 0.57f);
        System.out.println(v2);
        test.valueAt(v2, 0.6f);
        System.out.println(v2);
        test.valueAt(v2, 0.9f);
        System.out.println(v2);

    }

}

