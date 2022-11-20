package com.openlap.Visualizer.framework.factory;

import com.openlap.Visualizer.exceptions.VisualizationCodeGeneratorCreationException;
import com.openlap.template.VisualizationCodeGenerator;

/**
 * The interface specifying the Factory functions for creating the VisualizationCodeGenerator objects
 *
 * @author Bassim Bashir
 */
public interface VisualizationCodeGeneratorFactory {

	/**
	 * The function instantiates a VisualizationCodeGenerator object by the class name specified as an argument to the function
	 *
	 * @param nameOfCodeGenerator The name of the VisualizationCodeGenerator to instantiate
	 * @return The object of the VisualizationCodeGenerator, null in the case that the VisualizationCodeGenerator could not be instantiated
	 * @throws VisualizationCodeGeneratorCreationException Specifying the cause of failure while trying to create the VisualizationCodeGenerator object
	 */
	VisualizationCodeGenerator createVisualizationCodeGenerator(String nameOfCodeGenerator) throws VisualizationCodeGeneratorCreationException;
}
