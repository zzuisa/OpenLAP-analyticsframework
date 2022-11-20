package com.openlap.template.model;

/**
 * Class which serves as a general model to hold the data transformed by a DataTransformer from a OpenLAPDataSet into
 * an instance of this class
 *
 * @author Bassim Bashir
 */
public class TransformedData<T> {

    private T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
