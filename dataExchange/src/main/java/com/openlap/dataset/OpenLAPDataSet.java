package com.openlap.dataset;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.openlap.exceptions.OpenLAPDataColumnException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * The main component of data transfer and validation in OpenLAP. This DataStructure fulfils two main purposes:
 * 1. Enable easy incorporated serialization into JSON with human-readable form
 * 2. Enable the dynamic validation of types and required fields as well as enable the check of valid configurations,
 * i.e. mappings between different OpenLAPDataSets to determine they are compatible on their column Types and required
 * parameters.
 */
public class OpenLAPDataSet {
    // Map of the columns with the String ID. The string is taken from the OpenLAPColumnConfigData of the column.
    private HashMap<String, OpenLAPDataColumn> columns;

    /**
     * Empty constructor
     */
    public OpenLAPDataSet() {
        this.columns = new HashMap<String, OpenLAPDataColumn>();
    }

    /**
     * @return the Columns with their respective IDs
     */
    public HashMap<String, OpenLAPDataColumn> getColumns() {
        return columns;
    }

    /**
     * Adds a column to this OpenLAPDataSet. Should be added with the help of the OpenLAPDataColumnFactory
     *
     * @param column The OpenLAPDataColumn to be added to the OpenLAPDataSet
     * @throws OpenLAPDataColumnException
     */
    public void addOpenLAPDataColumn(OpenLAPDataColumn<?> column) throws OpenLAPDataColumnException {
        String columnId = column.getConfigurationData().getId();
        if (columns.containsKey(columnId) || columnId.isEmpty() || columnId == null)
            throw new OpenLAPDataColumnException(OpenLAPDataColumnException.COLUMN_ALREADY_EXISTS, columnId);
        else {
            columns.put(columnId, column);
        }
    }

    /**
     * This method realizes the need for the OpenLAPDataSet to be able to dynamically (on runtime) check wheter a
     * configuration (of the OpenLAPPortConfig) is compatible with the current DataSet. It checks that the types and
     * required fields are present and that the incoming fields are all part of the present OpenLAPDataSet.
     *
     * @param configuration The configuration to be checked.
     * @return A OpenLAPDataSetConfigValidationResult that contains information about the validity of the
     * configuration and additional information about what fields are problematic in case the validation does not yield
     * a positive result.
     */
    public OpenLAPDataSetConfigValidationResult validateConfiguration(OpenLAPPortConfig configuration) {
        // Initialize object with results
        OpenLAPDataSetConfigValidationResult configResult = new OpenLAPDataSetConfigValidationResult();
        // Get the input as a list
        List<OpenLAPColumnConfigData> values =
                new ArrayList<OpenLAPColumnConfigData>(configuration.getInputColumnConfigurationData());

        // Check for required fields
        validatePresenceRequiredColumns(configResult, values);
        if (!configResult.isValid()) return configResult;

        // Check for incoming fields being present
        validateInputColumnsCorrespondence(configResult, values);
        if (!configResult.isValid()) return configResult;

        for (OpenLAPPortMapping mappingEntry : configuration.getMapping()) {
            // Validate types
            OpenLAPColumnConfigData inputColumn = mappingEntry.getInputPort();
            if (!inputColumn.validateConfigurationDataTypeFromOutputPort(mappingEntry.getOutputPort())) {
                configResult.setValid(false);
                configResult.appendValidationMessage(String.format("Port %s expected %s, got %s instead.",
                        inputColumn.getId(), inputColumn.getType(), mappingEntry.getOutputPort().getType()));
            }
        }

        if (!configResult.isValid()) return configResult;
        else {
            configResult.setValidationMessage(OpenLAPDataSetConfigValidationResult.VALID_CONFIGURATION);
            return configResult;
        }
    }


    /***
     * Utility method to get all required columns
     * @param onlyRequiredColumns if true, returns only required columns
     * @return A list of the OpenLAPDataColumns that are required
     */
    public List<OpenLAPDataColumn> getColumnsAsList(boolean onlyRequiredColumns) {
        List<OpenLAPDataColumn> columns = new ArrayList<OpenLAPDataColumn>(this.columns.values());
        if (!onlyRequiredColumns) return columns;
        else {
            List<OpenLAPDataColumn> requiredColumns = new ArrayList<OpenLAPDataColumn>();
            for (OpenLAPDataColumn column : columns) {
                if (column.getConfigurationData().isRequired()) requiredColumns.add(column);
            }
            return requiredColumns;
        }
    }

    /**
     * Utility method to get all required columns configuration data
     *
     * @param onlyRequiredColumnsConfigurationData if true, returns only required columns OpenLAPColumnConfigData
     * @return a list with the OpenLAPColumnConfigData of the required columns
     */
    public List<OpenLAPColumnConfigData> getColumnsConfigurationData(boolean onlyRequiredColumnsConfigurationData) {
        List<OpenLAPDataColumn> columns;
        if (onlyRequiredColumnsConfigurationData)
            columns = getColumnsAsList(true);
        else columns = getColumnsAsList(false);

        List<OpenLAPColumnConfigData> result = new ArrayList<OpenLAPColumnConfigData>();

        for (OpenLAPDataColumn column : columns) {
            result.add(column.getConfigurationData());
        }
        return result;
    }

    /**
     * Get a list of the OpenLAPColumnConfigData of all the columns of the Dataset
     *
     * @return Get a list of the OpenLAPColumnConfigData of all the columns of the Dataset
     */
    @JsonIgnore
    public List<OpenLAPColumnConfigData> getColumnsConfigurationData() {
        return this.getColumnsConfigurationData(false);
    }

    /**
     * Method compares this OpenLAPDataSet against the one passed through in the argument of this function
     *
     * @param openLAPDataSetToCompare The Dataset to compare against
     * @return true if both the column of this dataset are an exact match to that provided as an argument ot the function, false otherwise
     */
    public boolean compareToOpenLAPDataSet(OpenLAPDataSet openLAPDataSetToCompare) {
        List<OpenLAPColumnConfigData> columnsFirstInstance = new ArrayList<>(this.getColumnsConfigurationData());
        List<OpenLAPColumnConfigData> columnsSecondInstance = new ArrayList<>(openLAPDataSetToCompare.getColumnsConfigurationData());

        if (columnsFirstInstance.size() != columnsSecondInstance.size())
            return false;

        for (OpenLAPColumnConfigData columnFirstInstance : columnsFirstInstance) {
            for (int i = 0; i < columnsSecondInstance.size(); i++) {
                if (columnFirstInstance.equals(columnsSecondInstance.get(i))) {
                    columnsSecondInstance.remove(i);
                    break;
                }
            }
        }
        if (columnsSecondInstance.size() == 0)
            return true;
        else
            return false;

    }


    /**
     * Validates that all the required Columns of the DataSet are in a given list of Columns
     *
     * @param configResult The config result object that can be modified to contain error messages
     * @param values       The list to be checked if contains all the required values.
     */
    private void validatePresenceRequiredColumns(OpenLAPDataSetConfigValidationResult configResult,
                                                 List<OpenLAPColumnConfigData> values) {
        // Initialize a list of the required columns
        List<OpenLAPColumnConfigData> requiredColumnConfigData = getColumnsConfigurationData(true);
        // Remove from the list all the items that are in the values
        removeMatchingColumnData(requiredColumnConfigData, values);
        // If there are still elements left, there are missing values.
        if (requiredColumnConfigData.size() > 0) {
            configResult.setValid(false);
            configResult.appendValidationMessage("Required columns not found");
            // Put message of every column that is not found
            for (OpenLAPColumnConfigData remainingColumnConfigData : requiredColumnConfigData) {
                configResult.appendValidationMessage(
                        String.format("Column: %s is not found", remainingColumnConfigData.getId())
                );
            }
        }
        // Otherwise the required fields are present
        else configResult.setValid(true);
    }

    /**
     * Check that all the input columns are present on the actual DataSet
     *
     * @param configResult The config result object that can be modified to contain error messages
     * @param values       The list of columns to be checked if is all contained in the DataSet.
     */
    private void validateInputColumnsCorrespondence(OpenLAPDataSetConfigValidationResult configResult,
                                                    List<OpenLAPColumnConfigData> values) {
        // Get the columns present on the dataset
        List<OpenLAPColumnConfigData> dataSetColumns = getColumnsConfigurationData();
        List<OpenLAPColumnConfigData> valuesCopy = new ArrayList<OpenLAPColumnConfigData>(values);
        // Remove from the list all the items that are in the dataSet Columns
        removeMatchingColumnData(valuesCopy, dataSetColumns);
        // If there are elements left, it means there is a mapping to be done.
        if (valuesCopy.size() > 0) {
            configResult.setValid(false);
            configResult.appendValidationMessage("Columns not present on the destination DataSet");
            // Put message of every column that is not found
            for (OpenLAPColumnConfigData remainingColumn : valuesCopy) {
                configResult.appendValidationMessage(
                        String.format("Column: %s does not exist in the destination dataset", remainingColumn.getId())
                );
            }
        }
        // Otherwise the required fields are present
        else configResult.setValid(true);
    }

    /**
     * Remove the columns of the first parameter that exists on the second parameter
     *
     * @param original
     * @param removalList
     */
    private void removeMatchingColumnData(List<OpenLAPColumnConfigData> original,
                                          List<OpenLAPColumnConfigData> removalList) {
        //for each element of the removal list check if is valid on the original
        for (OpenLAPColumnConfigData removalListConfig : removalList) {
            original.removeIf(e -> (e.validateConfigurationDataCorrespondence(removalListConfig)));
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OpenLAPDataSet)) return false;

        OpenLAPDataSet that = (OpenLAPDataSet) o;

        return !(getColumns() != null ? !getColumns().equals(that.getColumns()) : that.getColumns() != null);

    }

    @Override
    public int hashCode() {
        return getColumns() != null ? getColumns().hashCode() : 0;
    }
}
