package file;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Vector;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class FileSearcher {
	
	public static Collection<? extends Container> searchFile(File file, Path reserveFolder, Path successFolder, Path failedFolder) {
		Vector<Container> output = new Vector<Container>();
		if(file.isFile() && file.getName().endsWith(".zip")) {
			try {
				ZipFile zipFile = new ZipFile(file);
				Enumeration<? extends ZipEntry> entries = zipFile.entries();
				while(entries.hasMoreElements()){
					Container temp = new Container(file, entries.nextElement());
					temp.setReservePath(reserveFolder);
					temp.setSuccessPath(successFolder);
					temp.setFailedPath(failedFolder);
					temp.moveToReserve();
					output.add(temp);
				}
				zipFile.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
//				e.printStackTrace();
			}
		} else if(file.isFile() && (file.getName().endsWith(".dat") || file.getName().endsWith(".gz"))) {
			Container temp = new Container(file);
			temp.setReservePath(reserveFolder);
			temp.setSuccessPath(successFolder);
			temp.setFailedPath(failedFolder);
			temp.moveToReserve();
			output.add(new Container(file));
		}
		return output;
	}
	
	public static Collection<? extends Container> searchMultiFile(Path rootPath, Path reserveFolder, Path successFolder, Path failedFolder) {
		File root = rootPath.toFile();
		if(!root.exists()) return null;
		String[] fileArray = root.list();
		if(fileArray == null) return null;
		Vector<Container> output = new Vector<Container>(fileArray.length);
		for(String fileName: fileArray) {
			File file = rootPath.resolve(fileName).toFile();
			if(file.isFile() && file.getName().endsWith(".zip")) {
				try {
					ZipFile zipFile = new ZipFile(file);
					Enumeration<? extends ZipEntry> entries = zipFile.entries();
					while(entries.hasMoreElements()){
						Container temp = new Container(file, entries.nextElement());
						temp.setReservePath(reserveFolder);
						temp.setSuccessPath(successFolder);
						temp.setFailedPath(failedFolder);
						temp.moveToReserve();
						output.add(temp);
					}
					zipFile.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
//					e.printStackTrace();
				}
			} else if(file.isFile() && (file.getName().endsWith(".dat") || file.getName().endsWith(".gz"))) {
				Container temp = new Container(file);
				temp.setReservePath(reserveFolder);
				temp.setSuccessPath(successFolder);
				temp.setFailedPath(failedFolder);
				temp.moveToReserve();
				output.add(new Container(file));
			}
		}
		return output;
	}

	public static Collection<? extends Container> searchAll(Path rootPath, Path reserveFolder, Path successFolder, Path failedFolder) {
		File root = rootPath.toFile();
		if(!root.exists()) return null;
		if(root.isFile()) return searchFile(root, reserveFolder, successFolder, failedFolder);
		String[] fileArray = root.list();
		if(fileArray == null) return null;
		Vector<Container> output = new Vector<Container>(fileArray.length);
		for(String fileName: fileArray) {
			File file = rootPath.resolve(fileName).toFile();
			if(file.isDirectory()) {
				Collection<? extends Container> list = searchAll(file.toPath(), (reserveFolder != null)? reserveFolder.resolve(file.getName()): null, (successFolder != null)? successFolder.resolve(file.getName()): null, (failedFolder != null)? failedFolder.resolve(file.getName()): null);
				if(list != null) output.addAll(list);
			}
		}
		Collection<? extends Container> containers = searchMultiFile(rootPath, reserveFolder, successFolder, failedFolder);
		if(containers != null) output.addAll(containers);
		return output;
	}
	
	public static Collection<? extends Container> searchSubDir(Path rootPath, Path reserveFolder, Path successFolder, Path failedFolder) {
		File root = rootPath.toFile();
		if(!root.exists()) return null;
		String[] fileArray = root.list();
		if(fileArray == null) return null;
		Vector<Container> output = new Vector<Container>(fileArray.length);
		for(String fileName: fileArray) {
			File file = rootPath.resolve(fileName).toFile();
			if(file.isDirectory()) {
				Collection<? extends Container> list = searchMultiFile(file.toPath(), (reserveFolder != null)? reserveFolder.resolve(file.getName()): null, (successFolder != null)? successFolder.resolve(file.getName()): null, (failedFolder != null)? failedFolder.resolve(file.getName()): null);
				if(list != null) output.addAll(list);
			}
		}
		return output;
	}
	
	public static Collection<? extends Container> searchSubDir(Path rootPath, int maxCount, Path reserveFolder, Path successFolder, Path failedFolder) {
		File root = rootPath.toFile();
		if(!root.exists()) return null;
		String[] fileArray = root.list();
		if(fileArray == null) return null;
		Vector<String> fileVector = new Vector<String>(List.of(fileArray));
		Collections.sort(fileVector);
		if(maxCount < 0) {
			Collections.reverse(fileVector);
			maxCount *= -1;
		}
		Vector<Container> output = new Vector<Container>(fileArray.length);
		int count = 0;
		for(String fileName: fileVector) {
			if(count >= maxCount) break;
			File file = rootPath.resolve(fileName).toFile();
			if(file.isDirectory()) {
				Collection<? extends Container> list = searchMultiFile(file.toPath(), (reserveFolder != null)? reserveFolder.resolve(file.getName()): null, (successFolder != null)? successFolder.resolve(file.getName()): null, (failedFolder != null)? failedFolder.resolve(file.getName()): null);
				if(list != null) {
					count++;
					output.addAll(list);
				}
			}
		}
		return output;
	}
	
}
