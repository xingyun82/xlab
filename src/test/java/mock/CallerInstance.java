package mock;

public class CallerInstance {

    public String hello() {
        try {
            return UtilityInstance.hello();
        } catch (Exception e) {
            return "bad";
        }
    }
}
