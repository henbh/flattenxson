package flattenxson.exceptions;

public class UnflattenException extends FlattenXsonException {
    public UnflattenException(String message, Exception e) {
        super(message, e);
    }
}
