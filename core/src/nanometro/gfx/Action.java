package nanometro.gfx;

public class Action {
    public enum ActionType {
        ADD_MIDDLE, REMOVE_HEAD, REMOVE_TAIL, REMOVE_MIDDLE
    }

    public ActionType type;
    public Object arg1;
    public Object arg2;
    public boolean completed = false;

    public Action(ActionType type, Object arg1, Object arg2) {
        this.type = type;
        this.arg1 = arg1;
        this.arg2 = arg2;
    }
}
