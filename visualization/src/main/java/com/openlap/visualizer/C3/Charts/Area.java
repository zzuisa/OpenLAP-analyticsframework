package com.openlap.visualizer.C3.Charts;

import com.openlap.dataset.OpenLAPColumnDataType;
import com.openlap.dataset.OpenLAPDataColumnFactory;
import com.openlap.dataset.OpenLAPDataSet;
import com.openlap.exceptions.OpenLAPDataColumnException;
import com.openlap.template.DataTransformer;
import com.openlap.template.VisualizationCodeGenerator;
import com.openlap.exceptions.VisualizationCodeGenerationException;
import com.openlap.template.model.TransformedData;
import com.openlap.visualizer.C3.Transformers.PairList;
import javafx.util.Pair;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Arham Muslim
 * on 29-Jul-16.
 */
public class Area extends VisualizationCodeGenerator {

    public Area(){
    }

    @Override
    public String getName() {
        return "Area Chart";
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
                    OpenLAPDataColumnFactory.createOpenLAPDataColumnOfType("xAxisStrings", OpenLAPColumnDataType.Text, true, "X-Axis Items", "List of items to be displayed on X-Axis of the graph")
            );
            this.getInput().addOpenLAPDataColumn(
                    OpenLAPDataColumnFactory.createOpenLAPDataColumnOfType("yAxisValues", OpenLAPColumnDataType.Numeric, true, "Y-Axis Values", "List of values to be displayed on Y-Axis of the graph")
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
        stringBuilder.append("data: {columns: [ [' '");

        String labels = "";
        for(Pair<String, Float> pair: transformedPairList) {
            stringBuilder.append(", " + pair.getValue());

            labels += "'" + pair.getKey() + "',";
        }
        stringBuilder.append("] ], ");
        stringBuilder.append("type: 'area-spline'}, ");

        stringBuilder.append("legend: {show:false}, ");
        stringBuilder.append("axis: { x: { type: 'category', categories: [");
        if(labels.length()>0)
            stringBuilder.append(labels, 0, labels.length()-1);
        stringBuilder.append("] } }");

        stringBuilder.append("});");
        stringBuilder.append("</script>");

        return stringBuilder.toString();
    }
}