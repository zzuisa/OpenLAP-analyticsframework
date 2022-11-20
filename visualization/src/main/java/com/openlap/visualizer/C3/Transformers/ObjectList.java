package openlap.visualizer.C3.Transformers;

import com.openlap.dataset.OpenLAPDataColumn;
import com.openlap.dataset.OpenLAPDataSet;
import com.openlap.template.DataTransformer;
import com.openlap.exceptions.UnTransformableData;
import com.openlap.template.model.TransformedData;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by Arham Muslim
 * on 13-Oct-16.
 */
public class ObjectList implements DataTransformer {
    TransformedData<LinkedHashMap<String, Object[]>> transformedData = new TransformedData<LinkedHashMap<String, Object[]>>();

    public TransformedData<?> transformData(OpenLAPDataSet openLAPDataSet) throws UnTransformableData {
        //List<OpenLAPDataColumn> columns = openLAPDataSet.getColumnsAsList(false);

        List<String> labels = openLAPDataSet.getColumns().get("xAxisStrings").getData();
        List<String> groupBy = openLAPDataSet.getColumns().get("GroupBy").getData();
        List<Double> frequencies = openLAPDataSet.getColumns().get("yAxisValues").getData();

        // Collecting the unique entries in the group column
        List<String> uniqueGroupItems = new ArrayList<String>();
        for (String item : groupBy) {
            if (!uniqueGroupItems.contains(item))
                uniqueGroupItems.add(item);
        }

        int dataArraySize = uniqueGroupItems.size();

        transformedData.setData(new LinkedHashMap<String, Object[]>());


        //Adding first row as the header with group column item names
        transformedData.getData().put("Header",new Object[dataArraySize]);
        for (int i = 0; i < uniqueGroupItems.size(); i++)
            transformedData.getData().get("Header")[i] = uniqueGroupItems.get(i);

        //Adding the data
        if(labels != null) {
            for (int i = 0; i < labels.size(); i++) {
                if(!transformedData.getData().containsKey(labels.get(i))) {
                    Object[] emptyArray = new Object[dataArraySize];
                    Arrays.fill(emptyArray, 0);
                    transformedData.getData().put(labels.get(i), emptyArray);
                }

                transformedData.getData().get(labels.get(i))[uniqueGroupItems.indexOf(groupBy.get(i))] = frequencies.get(i);
            }
        }

        return transformedData;
    }

    public TransformedData<LinkedHashMap<String, Object[]>> getTransformedData() {
        return transformedData;
    }
}
