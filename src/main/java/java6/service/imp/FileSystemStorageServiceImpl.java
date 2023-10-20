package java6.service.imp;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.apache.commons.io.FilenameUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java6.config.StorageProperties;
import java6.exception.StorageException;
import java6.exception.StorageFileException;
import java6.service.StorageService;

@Service
public class FileSystemStorageServiceImpl implements StorageService {
	private final Path rootLocation;

	@Override
	public String getStoredFileName(MultipartFile file, String id) {
		String ext=FilenameUtils.getExtension(file.getOriginalFilename());
		return "p"+id+"."+ext;
	}

	public FileSystemStorageServiceImpl(StorageProperties properties) {
		this.rootLocation = Paths.get(properties.getLocation());
	}

	@Override
	public void store(MultipartFile file, String storedFileName) {
		try {
			if (file.isEmpty()) {
				throw new StorageException("Fail");
			}

			Path destinationFile = this.rootLocation.resolve(Paths.get(storedFileName))
					.normalize().toAbsolutePath();

			if (!destinationFile.getParent().equals(this.rootLocation.toAbsolutePath())) {
				throw new StorageException("Cannot store file outside current directory");
			}
			try (InputStream inputStream = file.getInputStream()) {
				Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
			}
		} catch (Exception e) {
			throw new StorageException("Failed", e);
		}
	}

	@Override
	public Resource loadAsResource(String fileName) {
		try {
			Path file = load(fileName);
			Resource resource = new UrlResource(file.toUri());
			if (resource.exists() || resource.isReadable()) {
				return resource;
			}
			throw new StorageFileException("Could not read file:" + fileName);
		} catch (Exception e) {
			throw new StorageFileException("Could not read file:" + fileName);
		}
	}

	@Override
	public Path load(String fileName) {
		return rootLocation.resolve(rootLocation);
	}
	
	@Override
	public void delete(String storedFileName) throws IOException {
		Path destinationFile=rootLocation.resolve(Paths.get(storedFileName)).normalize().toAbsolutePath();
		
		Files.delete(destinationFile);
	}
	
	@Override
	public void init() {
		try {
			Files.createDirectories(rootLocation);
			System.out.println(rootLocation.toString());
		} catch (Exception e) {
			throw new StorageException("Could not initialize storage",e);
		}
	}
}
