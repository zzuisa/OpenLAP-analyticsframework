package com.openlap.template;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openlap.dataset.OpenLAPDataSet;
import com.openlap.dataset.OpenLAPDataSetConfigValidationResult;
import com.openlap.dataset.OpenLAPPortConfig;
import com.openlap.dataset.OpenLAPPortMapping;
import com.openlap.exceptions.DataSetValidationException;
import com.openlap.exceptions.UnTransformableData;
import com.openlap.exceptions.VisualizationCodeGenerationException;
import com.openlap.template.model.TransformedData;

import java.util.Map;

/**
 * The abstract class which needs to be extended in order to add new concrete visualization methods
 * The abstract method "initializeDataSetConfiguration" should be overriden with the input OpenLAPDataSet configuration for the visualization method being implemented.
 * Furthermore, the overriden method "visualizationCode" method should return the actual client visualization code
 *
 * @author Bassim Bashir
 */
public abstract class VisualizationCodeGenerator {

    private OpenLAPDataSet input;
    private OpenLAPDataSet output;

    public abstract String getName();

    public abstract void initializeDataSetConfiguration();

    public abstract Class getDataTransformer();

    public abstract String visualizationLibraryScript();

    public abstract String visualizationCode(DataTransformer dataTransformer, Map<String, Object> additionalParams) throws VisualizationCodeGenerationException;


    public boolean isDataProcessable(OpenLAPPortConfig openlapPortConfig) throws DataSetValidationException {
        if (input == null)
            initializeDataSetConfiguration();

        OpenLAPDataSetConfigValidationResult validationResult = input.validateConfiguration(openlapPortConfig);
        if (validationResult.isValid())
            return true;
        else
            throw new DataSetValidationException(validationResult.getValidationMessage());
    }

    public String generateVisualizationCode(OpenLAPDataSet openLAPDataSet, OpenLAPPortConfig portConfig, Map<String, Object> additionalParams) throws VisualizationCodeGenerationException, UnTransformableData, DataSetValidationException, IllegalAccessException, InstantiationException {
        if (input == null)
            initializeDataSetConfiguration();
        // is the configuration valid?
        if (isDataProcessable(portConfig)) {
            // for each configuration element of the configuration
            for (OpenLAPPortMapping mappingEntry : portConfig.getMapping()) {
                // map the data of the column c.id==element.id to the input
                input.getColumns().get(mappingEntry.getInputPort().getId()).setData(openLAPDataSet.getColumns().get(mappingEntry.getOutputPort().getId()).getData());
            }

            DataTransformer dataTransformer = (DataTransformer) getDataTransformer().newInstance();

            TransformedData<?> transformedData = dataTransformer.transformData(input);
            if (transformedData == null)
                throw new UnTransformableData("Data could not be transformed.");
            else
                return visualizationCode(dataTransformer, additionalParams);
        } else {
            return "Data could not be transformed.";
        }
    }

    public String getVisualizationLibraryScript() {
        return visualizationLibraryScript();
    }

    public OpenLAPDataSet getInput() {
        if (input == null)
            initializeDataSetConfiguration();
        return input;
    }

    public void setInput(OpenLAPDataSet input) {
        this.input = input;
    }

    public OpenLAPDataSet getOutput() {
        return output;
    }

    public void setOutput(OpenLAPDataSet output) {
        this.output = output;
    }

    public String getOutputAsJsonString() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(this.output);
        } catch (JsonProcessingException | NullPointerException exception) {
            return "";
        }
    }

    public String getInputAsJsonString() {
        if (input == null)
            initializeDataSetConfiguration();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(this.input);
        } catch (JsonProcessingException exception) {
            return "";
        }

    }

}
