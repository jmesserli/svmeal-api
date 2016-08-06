package nu.peg.svmeal.converter;

public interface Converter<I, O> {
    O convert(I from);
}
