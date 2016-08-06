package nu.peg.svmeal.model;


public class Response<T> {

    public Status status;
    public T data;
    public String error;

    public Response() {
    }

    public Response(T data) {
        this.status = Status.Ok;
        this.data = data;
    }

    public Response(Status status, String error) {
        this.status = status;
        this.error = error;
    }

    public Response(Status status, T data, String error) {
        this.status = status;
        this.data = data;
        this.error = error;
    }

    public Status getStatus() {
        return status;
    }

    public T getData() {
        return data;
    }

    public String getError() {
        return error;
    }

    public enum Status {
        Ok, Error
    }

}
