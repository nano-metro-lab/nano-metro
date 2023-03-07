package nanometro.gfx;

public class Action {
    public enum ActionType {
        ADD_MIDDLE, REMOVE_HEAD, REMOVE_TAIL, REMOVE_MIDDLE
    }

    public ActionType type;
    public Object arg1;
    public Object arg2;
    public Object arg3;
    public SectionPreview sp1;
    public SectionPreview sp2;
    public boolean completed = false;

    public Action(ActionType type) {
        this.type = type;
    }
}
