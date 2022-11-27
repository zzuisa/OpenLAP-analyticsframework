package com.openlap.visualizer.C3.Transformers;

import com.openlap.dataset.OpenLAPDataSet;
import com.openlap.exceptions.UnTransformableData;
import com.openlap.template.DataTransformer;
import com.openlap.template.model.TransformedData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 Created by Ao
 on 2022.11.27
 */
public class ObjectSingleList implements DataTransformer {
    TransformedData<LinkedHashMap<String, Object[]>> transformedData = new TransformedData<LinkedHashMap<String, Object[]>>();

    public TransformedData<?> transformData(OpenLAPDataSet openLAPDataSet) throws UnTransformableData {
        //List<OpenLAPDataColumn> columns = openLAPDataSet.getColumnsAsList(false);

        List<String> xData = openLAPDataSet.getColumns().get("xAxisStrings").getData();
        List<String> yData = openLAPDataSet.getColumns().get("yAxisValues").getData();
        List<String> clusters = openLAPDataSet.getColumns().get("GroupBy").getData();

        // Collecting the unique entries in the group column
//        List<String> uniqueGroupItems = new ArrayList<String>();
//        for (String item : clusters) {
//            if (!uniqueGroupItems.contains(item))
//                uniqueGroupItems.add(item);
//        }
//
//        int dataArraySize = uniqueGroupItems.size();

        transformedData.setData(new LinkedHashMap<String, Object[]>());


        //Adding first row as the header with group column item names
//        transformedData.getData().put("Header", new Object[dataArraySize]);
//        for (int i = 0; i < uniqueGroupItems.size(); i++)
//            transformedData.getData().get("Header")[i] = uniqueGroupItems.get(i);

        //Adding the data
        if (xData != null) {
            for (int i = 0; i < xData.size(); i++) {
                if (!transformedData.getData().containsKey(xData.get(i))) {
//                    Object[] emptyArray = new Object[dataArraySize];
//                    Arrays.fill(emptyArray, 0);
//                    transformedData.getData().put(String.valueOf(xData.get(i)), emptyArray);
                    transformedData.getData().put(String.valueOf(clusters.get(i)) + "_x", Arrays.asList(Arrays.stream(
                                            String.valueOf(xData.get(i)).split(","))
                                    .mapToDouble(Double::parseDouble)
                                    .boxed()
                                    .collect(Collectors.toList()))
                            .toArray());
                    transformedData.getData().put(String.valueOf(clusters.get(i)), Arrays.asList(Arrays.stream(
                                            String.valueOf(yData.get(i)).split(","))
                                    .mapToDouble(Double::parseDouble)
                                    .boxed()
                                    .collect(Collectors.toList()))
                            .toArray());
                }

//                transformedData.getData().get(xData.get(i))[uniqueGroupItems.indexOf(clusters.get(i))] = yData.get(i);
            }
        }

        return transformedData;
    }

    public TransformedData<LinkedHashMap<String, Object[]>> getTransformedData() {
        return transformedData;
    }
}
