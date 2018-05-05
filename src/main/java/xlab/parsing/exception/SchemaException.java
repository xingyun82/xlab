package xlab.parsing.exception;

public class SchemaException extends Exception {

    public SchemaException(String msg) {
        super(msg);
    }

    public SchemaException(String msg, Throwable e) {
        super(msg, e);
    }

    public SchemaException(Throwable e) {
        super(e);
    }
}
