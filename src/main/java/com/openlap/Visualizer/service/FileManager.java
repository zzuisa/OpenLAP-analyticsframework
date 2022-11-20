package com.openlap.Visualizer.service;

import com.openlap.Visualizer.exceptions.FileManagerException;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A service which provides functions to perform operations on the system's storage.
 *
 * @author Bassim Bashir
 */
@Service
public class FileManager {

	@Autowired
	private ConfigurationService configurationService;

	/**
	 * Saves the provided file in the storage.
	 *
	 * @param fileName   The custom name of the file. If the name is null or empty then the name of the provided file will be used
	 * @param fileToSave The file to save
	 * @return The absolute location where the file is stored
	 * @throws FileManagerException If the saving of the file was not successful
	 */
	public String saveJarFile(String fileName, MultipartFile fileToSave) throws FileManagerException {
		if (configurationService.getFileManagerStorageLocation() == null || fileToSave == null || configurationService.getFileManagerStorageLocation().isEmpty())
			throw new FileManagerException("Saving file failed");
		if (fileToSave.isEmpty())
			throw new FileManagerException("The file has no contents");
		// if the filename is not provided
		if (fileName == null || fileName.isEmpty()) {
			// then generate one with the timestamp
			fileName = "jar_" + fileToSave.getName() + "_" + System.currentTimeMillis();
		}
		//fileName += configurationService.getJarBundleExtension(); //add the JAR extension

		try {
			Path fileStorageLocation = Paths.get(configurationService.getFileManagerStorageLocation());
			//create the the file storage directory if it does not exist
			if (!fileStorageLocation.toFile().exists())
				fileStorageLocation.toFile().mkdir();

			Path fileFinalPath = Paths.get(fileStorageLocation.toString(), fileName);
			FileUtils.copyInputStreamToFile(fileToSave.getInputStream(), fileFinalPath.toFile());

			return fileFinalPath.toString();
		} catch (IOException | SecurityException exception) {
			throw new FileManagerException(exception.getMessage());
		}
	}

	/**
	 * @return A list of file name in the system's storage
	 */
	public List<String> listFilesInDirectory(String location) {
		File fileDirectory = Paths.get(location).toFile();
		if (fileDirectory.isDirectory())
			return Arrays.asList(fileDirectory.list());
		else
			return new ArrayList<>();
	}

	/**
	 * Deletes a file from the system's storage
	 *
	 * @param fileToDelete The name of the file to delete
	 * @throws FileManagerException If the file could not be deleted
	 */
	public void deleteFile(String fileToDelete) throws FileManagerException {
		Path filePath = Paths.get(fileToDelete);
		try {
			Files.delete(filePath);
		} catch (IOException exception) {
			throw new FileManagerException(exception.getMessage());
		}
	}

	/**
	 * Deletes a framework jar file from the system's storage
	 *
	 * @param fileToDelete The name of the file to delete
	 * @throws FileManagerException If the file could not be deleted
	 */
	public void deleteJarFile(String fileToDelete) throws FileManagerException {
		Path filePath = Paths.get(configurationService.getFileManagerStorageLocation(), fileToDelete);
		try {
			Files.delete(filePath);
		} catch (IOException exception) {
			throw new FileManagerException(exception.getMessage());
		}
	}

	/**
	 * Checks if a file already exists in the system's storage
	 *
	 * @param fileName The name of the file to check
	 */
	public boolean fileExists(String fileName) {
		//Path filePath = Paths.get(configurationService.getFileManagerStorageLocation(), fileName+configurationService.getJarBundleExtension());
		Path filePath = Paths.get(configurationService.getFileManagerStorageLocation(), fileName);
		return Files.exists(filePath);
	}
}
