package file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class Container {

	private File file;
	private ZipEntry zipEntry;
	
	private Path reservePath = null;
	private Path successPath = null;
	private Path failedPath = null;
	
	private byte[] rawData;
	private boolean hasRead = false;
	
	public Container() {
		
	}
	
	public Container(File file) {
		this.file = file;
	}
	
	public Container(File file, ZipEntry zipEntry) {
		this.file = file;
		this.zipEntry = zipEntry; 
	}
	
	public File getFile() {
		return file;
	}
	
	public String getFileName() {
		if(file == null) return null;
		if(zipEntry != null) return file.getName() + "_" + zipEntry.getName();
		return file.getName();
	}
	
	public String getFolderName() {
		if(file == null) return null;
		return file.getParentFile().getName();
	}
	
	public String getPath() {
		if(file == null) return null;
		return file.getPath();
	}
	
	public byte[] getRawData() {
		if(!ensureRead()) return null;
		return rawData;
	}
	
	public boolean ensureRead() {
		if(hasRead) return true;
		hasRead = read();
		return hasRead;
	}
	
	public boolean read() {
		String[] tArr = file.getName().replace(".", ";").split(";");
		String type = tArr[tArr.length - 1].toLowerCase();
		switch (type) {
			case "dat":
				return readDAT();
			case "gz":
				return readGZ();
			case "zip":
				return readZIP();
			default:
				return false;
		}
	}
	
	private boolean readDAT() {
		try {
			InputStream reader = new FileInputStream(file);
			rawData = reader.readAllBytes();
			reader.close();
			return true;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
			return false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
			return false;
		}
	}
	
	private boolean readGZ() {
		try {
			InputStream reader = new GZIPInputStream(new FileInputStream(file));
			rawData = reader.readAllBytes();
			reader.close();
			return true;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
			return false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
			return false;
		}
	}
	
	private boolean readZIP() {
		if(zipEntry == null) return false;
		try {
			ZipFile zipFile = new ZipFile(file);
			InputStream reader = zipFile.getInputStream(zipEntry);;
			rawData = reader.readAllBytes();
			reader.close();
			zipFile.close();
			return true;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
			return false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
			return false;
		}
	}

	public void setReservePath(Path reservePath) {
		this.reservePath = reservePath;
	}

	public void setSuccessPath(Path successPath) {
		this.successPath = successPath;
	}

	public void setFailedPath(Path failedPath) {
		this.failedPath = failedPath;
	}
	
	public void moveToReserve() {
		if(reservePath == null || file == null || !file.exists()) return;
		if(!reservePath.toFile().exists()) reservePath.toFile().mkdirs();
		file.renameTo(reservePath.resolve(getFileName()).toFile());
	}
	
	public void moveToSuccess() {
		if(successPath == null || file == null || !file.exists()) return;
		if(!successPath.toFile().exists()) successPath.toFile().mkdirs();
		file.renameTo(successPath.resolve(getFileName()).toFile());
	}
	
	public void moveToFailed() {
		if(failedPath == null || file == null || !file.exists()) return;
		if(!failedPath.toFile().exists()) failedPath.toFile().mkdirs();
		file.renameTo(failedPath.resolve(getFileName()).toFile());
	}
	
}
