package com.openlap.visualizer.C3.Charts;

import com.openlap.dataset.OpenLAPColumnDataType;
import com.openlap.dataset.OpenLAPDataColumnFactory;
import com.openlap.dataset.OpenLAPDataSet;
import com.openlap.exceptions.OpenLAPDataColumnException;
import com.openlap.exceptions.VisualizationCodeGenerationException;
import com.openlap.template.DataTransformer;
import com.openlap.template.VisualizationCodeGenerator;
import com.openlap.visualizer.C3.Transformers.ObjectList;
import com.openlap.visualizer.C3.Transformers.ObjectSingleList;

import java.util.*;
import java.util.stream.Collectors;

/**
 Created by Ao
 on 2022.11.27
 */
public class ScatterPlot extends VisualizationCodeGenerator {

    public ScatterPlot() {
    }

    @Override
    public String getName() {
        return "Stacked Bar Chart";
    }

    public void initializeDataSetConfiguration() {
        this.setInput(new OpenLAPDataSet());
        this.setOutput(new OpenLAPDataSet());
        try {
            this.getInput().addOpenLAPDataColumn(
                    OpenLAPDataColumnFactory.createOpenLAPDataColumnOfType("xAxisStrings", OpenLAPColumnDataType.Numeric, true, "X-Axis Items", "List of items to be displayed on X-Axis of the graph")
            );
            this.getInput().addOpenLAPDataColumn(
                    OpenLAPDataColumnFactory.createOpenLAPDataColumnOfType("yAxisValues", OpenLAPColumnDataType.Numeric, true, "Y-Axis Values", "List of values to be displayed on Y-Axis of the graph")
            );
            this.getInput().addOpenLAPDataColumn(
                    OpenLAPDataColumnFactory.createOpenLAPDataColumnOfType("GroupBy", OpenLAPColumnDataType.Text, true, "Group By Values", "List of values which will be used to group values in column Y-Axis")
            );
        } catch (OpenLAPDataColumnException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Class getDataTransformer() {
        return ObjectSingleList.class;
    }

    public String visualizationCode(DataTransformer dataTransformer, Map<String, Object> map) throws VisualizationCodeGenerationException {
        ObjectSingleList objectSingleList = (ObjectSingleList) dataTransformer;

        LinkedHashMap<String, Object[]> datalist = objectSingleList.getTransformedData().getData();

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
        stringBuilder.append("legend: { position: 'inset' }, ");
        stringBuilder.append("data: { xs:{");
        datalist.entrySet().forEach(e -> {
            if (e.getKey().contains("_x")) {
                stringBuilder.append(String.format("%s:'%s',", e.getKey().split("_x")[0], e.getKey()));
            }
        });
        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        stringBuilder.append("},columns: [ ");
        if (datalist.size() > 1) {
            datalist.entrySet().forEach(e -> {
                String labels = "'" + e.getKey() + "',";
                //stringBuilder.append("['" + keySet[i] + "'");
                stringBuilder.append("[");
                int rowDataLength = e.getValue().length;
                for (int j = 0; j < rowDataLength; j++) {
                    List val = ((ArrayList) e.getValue()[j]);
                    String o = (String) val.stream().map(Object::toString)
                            .collect(Collectors.joining(", "));
                    String n = String.format("'%s', %s", e.getKey(), o);
                    if (val == null)
                        val = new ArrayList();
                    stringBuilder.append(n + "],");
                }
            });
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        }

        stringBuilder.append("], ");
        stringBuilder.append("type: 'scatter' ");
        stringBuilder.append("}}");
        stringBuilder.append(");");
        stringBuilder.append("</script>");

        return stringBuilder.toString();
    }

    public String visualizationLibraryScript() {
        return "<link href='https://cdnjs.cloudflare.com/ajax/libs/c3/0.7.18/c3.min.css' rel='stylesheet' type='text/css'><script src='https://d3js.org/d3.v5.min.js' charset='utf-8'></script><script src='https://cdnjs.cloudflare.com/ajax/libs/c3/0.7.18/c3.min.js'></script>";
    }
}
