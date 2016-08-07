package nu.peg.svmeal.model;


import java.io.Serializable;

public class Response<T> implements Serializable {

    public Status status;
    public T data;
    public String error;

    public Response() {
    }

    public Response(T data) {
        this.status = Status.Ok;
        this.data = data;
    }

    public Response(String error) {
        this.status = Status.Error;
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
