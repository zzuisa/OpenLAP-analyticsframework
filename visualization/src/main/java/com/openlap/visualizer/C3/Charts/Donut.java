package com.openlap.visualizer.C3.Charts;

import com.openlap.dataset.OpenLAPColumnDataType;
import com.openlap.dataset.OpenLAPDataColumnFactory;
import com.openlap.dataset.OpenLAPDataSet;
import com.openlap.template.DataTransformer;
import com.openlap.template.VisualizationCodeGenerator;
import com.openlap.exceptions.VisualizationCodeGenerationException;
import com.openlap.template.model.TransformedData;
import com.openlap.exceptions.OpenLAPDataColumnException;
import com.openlap.visualizer.C3.Transformers.PairList;
import javafx.util.Pair;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Arham Muslim
 * on 29-Jul-16.
 */
public class Donut extends VisualizationCodeGenerator {

    public Donut(){
    }

    @Override
    public String getName() {
        return "Donut Chart";
    }

    @Override
    public String visualizationLibraryScript(){
        return "<link href='https://cdnjs.cloudflare.com/ajax/libs/c3/0.7.18/c3.min.css' rel='stylesheet' type='text/css'><script src='https://d3js.org/d3.v5.min.js' charset='utf-8'></script><script src='https://cdnjs.cloudflare.com/ajax/libs/c3/0.7.18/c3.min.js'></script>";
    }

    @Override
    public void initializeDataSetConfiguration() {
        this.setInput(new OpenLAPDataSet());
        this.setOutput(new OpenLAPDataSet());
        try {
            this.getInput().addOpenLAPDataColumn(
                    OpenLAPDataColumnFactory.createOpenLAPDataColumnOfType("SliceLabel", OpenLAPColumnDataType.Text, true, "Slice Labels", "List of labels for each slice of the Pie")
            );
            this.getInput().addOpenLAPDataColumn(
                    OpenLAPDataColumnFactory.createOpenLAPDataColumnOfType("SliceValue", OpenLAPColumnDataType.Numeric, true, "Slice Values", "List of values for each slice of the Pie")
            );
        } catch (OpenLAPDataColumnException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Class getDataTransformer(){
        return PairList.class;
    }

    @Override
    public String visualizationCode(DataTransformer dataTransformer, Map<String, Object> map) throws VisualizationCodeGenerationException {
        PairList pairList = (PairList) dataTransformer;

        List<Pair<String, Float>> transformedPairList = pairList.getTransformedData().getData();

        long postfix = (new Date()).getTime();

        String width = (map.containsKey("width")) ? map.get("width").toString() : "500";
        String height = (map.containsKey("height")) ? map.get("height").toString() : "350";

        String xLabel = (map.containsKey("xLabel")) ? map.get("xLabel").toString() : "";
        String yLabel = (map.containsKey("yLabel")) ? map.get("yLabel").toString() : "";

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<div id='chartdiv_" + postfix + "'></div>");
        stringBuilder.append("<script type='text/javascript'> ");
        stringBuilder.append("var width_" + postfix + " = " + width + ";");
        stringBuilder.append("var height_" + postfix + " = " + height + ";");
        stringBuilder.append("var chart_" + postfix + " = c3.generate({");
        stringBuilder.append("bindto: '#chartdiv_" + postfix + "', ");
        stringBuilder.append("size: { width: (width_" + postfix + " - 20), height: (height_" + postfix + " - 20) }, ");
        stringBuilder.append("data: {columns: [ ");

        if(transformedPairList.size()>0) {
            for (Pair<String, Float> pair : transformedPairList)
                stringBuilder.append("['" + pair.getKey() + "', " + pair.getValue() + "],");
        }
        else
            stringBuilder.append("['No data',0],");

        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        stringBuilder.append(" ], ");
        stringBuilder.append("type: 'donut'} ");

        stringBuilder.append("});");
        stringBuilder.append("</script>");

        return stringBuilder.toString();
    }
}
