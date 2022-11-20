package com.openlap.visualizer.C3.Transformers;

import com.openlap.dataset.OpenLAPColumnDataType;
import com.openlap.dataset.OpenLAPDataColumn;
import com.openlap.dataset.OpenLAPDataSet;
import com.openlap.template.DataTransformer;
import com.openlap.exceptions.UnTransformableData;
import com.openlap.template.model.TransformedData;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Arham Muslim
 * on 17-Mar-16.
 */
public class PairList implements DataTransformer {
    TransformedData<List<Pair<String, Float>>> transformedData = new TransformedData<List<Pair<String, Float>>>();

    public TransformedData<?> transformData(OpenLAPDataSet openLAPDataSet) throws UnTransformableData {
        List<OpenLAPDataColumn> columns = openLAPDataSet.getColumnsAsList(true);

        List<String> labels = null;
        List<Float> frequencies = null;

        transformedData.setData(new ArrayList<>());

        for(OpenLAPDataColumn column: columns) {
            if (column.getConfigurationData().getType().equals(OpenLAPColumnDataType.Numeric)) {
                frequencies = column.getData();
            } else {
                labels = column.getData();
            }
        }

        if(labels != null) {
            for (int i = 0; i < labels.size(); i++) {
                transformedData.getData().add(new Pair<>(labels.get(i), Float.parseFloat(String.valueOf(frequencies.get(i)))));
            }
        }

        return transformedData;
    }

    public TransformedData<List<Pair<String, Float>>> getTransformedData() {
        return transformedData;
    }
}
