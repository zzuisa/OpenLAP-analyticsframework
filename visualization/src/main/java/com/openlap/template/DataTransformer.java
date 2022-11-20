package com.openlap.template;

import com.openlap.dataset.OpenLAPDataSet;
import com.openlap.exceptions.UnTransformableData;
import com.openlap.template.model.TransformedData;

/**
 * The interface which defines the methods of a DataTransformer which the concrete implementations should implement
 *
 * @author Bassim Bashir
 */
public interface DataTransformer {

    /**
     * @param openLAPDataSet The dataset which needs to be transformed in a
     *                    dataset that is understood by the visualization code
     * @return null if the data could not be transformed
     */
    TransformedData<?> transformData(OpenLAPDataSet openLAPDataSet) throws UnTransformableData;

}

