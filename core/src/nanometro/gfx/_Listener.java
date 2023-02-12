package nanometro.gfx;

import com.badlogic.gdx.graphics.g3d.Model;

import static nanometro.GameScreen.camera;
import static nanometro.GameScreen.world;
import static nanometro.GameScreen.modelService;

import com.badlogic.gdx.physics.box2d.*;

import java.util.List;

public class _Listener implements ContactListener {

    @Override
    public void beginContact(Contact contact) {
//        Body a=contact.getFixtureA().getBody();
//        Body b=contact.getFixtureB().getBody();
//        if (a.getUserData() instanceof Location && b.getUserData() instanceof Train) {
//            Location l = (Location) a.getUserData();
//            Train t = (Train) b.getUserData();
//            for (Passenger p : l.passengerList) {
//                List<Location> lst =  modelService.findDestinations(p.getType(), t.thisLocation, t.nextLocation);
//                System.out.println(lst);
//            }

//        }
    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
