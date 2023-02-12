package nanometro.model;

import nanometro.gfx.Line;
import nanometro.gfx.Location;
import nanometro.model.service.ModelService;
import nanometro.model.service.ModelServiceImpl;

public class ModelServiceFactory {
    private static ModelService<Location, Line> instance;

    public static ModelService<Location, Line> getInstance() {
        if (instance == null) {
            instance = new ModelServiceImpl<>();
        }
        return instance;
    }
}
