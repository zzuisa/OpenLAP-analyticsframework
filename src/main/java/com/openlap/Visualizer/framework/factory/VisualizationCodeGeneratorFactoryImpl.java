package com.openlap.Visualizer.framework.factory;

import com.openlap.Visualizer.exceptions.VisualizationCodeGeneratorCreationException;
import com.openlap.template.VisualizationCodeGenerator;
import org.xeustechnologies.jcl.JarClassLoader;
import org.xeustechnologies.jcl.JclObjectFactory;
import org.xeustechnologies.jcl.exception.JclException;
import org.xeustechnologies.jcl.proxy.CglibProxyProvider;
import org.xeustechnologies.jcl.proxy.ProxyProviderFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * A concrete implementation of the VisualizationCodeGeneratorFactory interface, providing a method for the creation of the
 * VisualizationCodeGenerators. Uses the JCL (https://github.com/kamranzafar/JCL) library
 *
 * @author Bassim Bashir
 */
public class VisualizationCodeGeneratorFactoryImpl implements VisualizationCodeGeneratorFactory {

	private JclObjectFactory jclObjectFactory;
	private JarClassLoader jarClassLoader;

	public VisualizationCodeGeneratorFactoryImpl(String locationOfJar) {
		jarClassLoader = new JarClassLoader();
       /* try {
            jarClassLoader.add(new FileInputStream(new File(locationOfJar)));
        }catch (IOException ioException){
            throw new VisualizationCodeGeneratorCreationException(ioException.getMessage());
        }*/
		jarClassLoader.add(locationOfJar);
		initializeClassLoader();
	}

	public VisualizationCodeGeneratorFactoryImpl() {
	}

	public VisualizationCodeGeneratorFactoryImpl(InputStream jarStream) {
		jarClassLoader = new JarClassLoader();
		jarClassLoader.add(jarStream);
		initializeClassLoader();
	}

	public void setupVisualizationCodeGeneratorFactoryImpl(String locationOfJar) {
		jarClassLoader = new JarClassLoader();
		try {
			jarClassLoader.add(new FileInputStream(new File(locationOfJar)));
		} catch (IOException ioException) {
			throw new VisualizationCodeGeneratorCreationException(ioException.getMessage());
		}
		initializeClassLoader();
	}

	private void initializeClassLoader() {
        /*jarClassLoader.getThreadLoader().setOrder(1);
        jarClassLoader.getCurrentLoader().setOrder(2);

        jarClassLoader.getParentLoader().setEnabled(false);
        jarClassLoader.getSystemLoader().setEnabled(false);
        jarClassLoader.getLocalLoader().setEnabled(false);*/

		jarClassLoader.getParentLoader().setOrder(2);
		jarClassLoader.getLocalLoader().setOrder(4);
		jarClassLoader.getSystemLoader().setOrder(5);
		jarClassLoader.getThreadLoader().setOrder(3);
		jarClassLoader.getCurrentLoader().setOrder(1);
		// Set default to cglib (from version 2.2.1)
		ProxyProviderFactory.setDefaultProxyProvider(new CglibProxyProvider());

		//Create a factory of castable objects/proxies
		jclObjectFactory = JclObjectFactory.getInstance(true);
	}

	@Override
	public VisualizationCodeGenerator createVisualizationCodeGenerator(String nameOfCodeGenerator) throws VisualizationCodeGeneratorCreationException {
		//Create and cast object of loaded class
		try {
			return (VisualizationCodeGenerator) jclObjectFactory.create(jarClassLoader, nameOfCodeGenerator);
		} catch (JclException jclException) {
			throw new VisualizationCodeGeneratorCreationException(jclException.getLocalizedMessage());
		}
	}
}
