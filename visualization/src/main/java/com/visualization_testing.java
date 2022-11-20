package com;

import com.openlap.dataset.OpenLAPDataSet;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.openlap.dataset.OpenLAPPortConfig;
import com.openlap.dataset.Os;
import com.openlap.template.DataTransformer;
import com.openlap.template.VisualizationCodeGenerator;
import com.openlap.visualizer.C3.Charts.Bar;
import org.apache.tomcat.jni.OS;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Arham Muslim
 * on 07-Sep-16.
 */
public class visualization_testing {
    public static final String DATASET_RESOURCE_PATH = "/data.json";
    public static final String CONFIGURATION_RESOURCE_PATH = "/config-two";

    public static void main(String[] args) {
        visualization_testing context = new visualization_testing();
        ObjectMapper mapper = new ObjectMapper();

        mapper.configure(SerializationFeature.INDENT_OUTPUT, true);

        try {

            OpenLAPDataSet inputDataSet = mapper.readValue(context.loadResourceFile(DATASET_RESOURCE_PATH),OpenLAPDataSet.class);
            OpenLAPPortConfig inputConfiguration = mapper.readValue(context.loadResourceFile(CONFIGURATION_RESOURCE_PATH), OpenLAPPortConfig.class);

            VisualizationCodeGenerator codeGenerator = new Bar();

            Map<String, Object> additionalParams = new HashMap<String, Object>();
            additionalParams.put("width",700);
            additionalParams.put("height", 700);
            //additionalParams.put("xLabel","Item Labels");
            //additionalParams.put("yLabel","Item Count");

            String visualizationScript = null;
            try {
                visualizationScript = codeGenerator.generateVisualizationCode(inputDataSet, inputConfiguration, additionalParams);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }

            String libraryScript = codeGenerator.getVisualizationLibraryScript();

            System.out.println("Output of the test:");
            System.out.println(libraryScript);
            System.out.println(visualizationScript);

        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private InputStream loadResourceFile(String configurationResourcePath) {
        return this.getClass().getResourceAsStream(configurationResourcePath);
    }
}
