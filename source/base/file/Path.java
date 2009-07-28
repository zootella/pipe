package base.file;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import base.data.Text;
import base.exception.DiskException;
import base.exception.DataException;

/** A Path is a parsed and valid looking absolute disk path, like "C:\folder\folder\file.ext". */
public class Path {
	
	// Make

	/** Parse the given String into a new absolute Path, or throw a DataException. */
	public Path(String s) {

		// Turn "C:" into "C:/", "/C:" into "/C:/", otherwise the Java File constructor will make a relative File
		if ((s.length() == 2 &&                       Text.isLetter(s.charAt(0)) && s.charAt(1) == ':') ||
			(s.length() == 3 && s.charAt(0) == '/' && Text.isLetter(s.charAt(1)) && s.charAt(2) == ':'))
			s = s + "/";

		// Have the Java File constructor parse and keep the text, this doesn't use the disk
		File file = new File(s);
		if (!file.isAbsolute()) throw new DataException(); // Absolute like "C:\file" or "\\computer\share\file", not relative like "file" or "folder\file"
		this.file = file; // Save it
	}

	/** Confirm File is absolute and make it into a new Path, or throw a DataException. */
	public Path(File file) {
		if (!file.isAbsolute()) throw new DataException(); // Make sure it's absolute
		this.file = file; // Save it
	}
	
	// Look
	
	/**
	 * A Java File object with the path text parsed inside.
	 * This isn't a file the program has open, and Java won't use the disk to make this object.
	 * This reference is final, and File is immutable, so path.file will never change.
	 */
	public final File file;
	
	// Convert

	/** Convert this Path to a String like "C:\folder\file.ext" or "\\computer\share\folder\file.ext". */
	public String toString() {
		return file.getPath(); // Return the String inside our Java File object as the File constructor parsed it
	}
	
	/** Get the file or folder Name at the end of this Path, like "file.ext" or "folder". */
	public Name name() {
		return new Name(file.getName()); // Get the last name in the sequence
	}
	
	// Navigate
	
	/** Return a new Path which is this one with name like "file.ext" added to it. */
	public Path add(Name name) { return add(name.toString()); }
	/** Return a new Path which is this one with path like "folder\folder\file.ext" added to it. */
	public Path add(PathName path) { return add(path.toString()); }
	/** Return a new Path which is this one with s like "folder\folder\file.ext" added to it. */
	public Path add(String s) {
		try {
			File f = new File(file, s); // Have the Java File constructor combine them
			return new Path(f);
		} catch (DataException e) { throw new IndexOutOfBoundsException(); } // New Path not absolute
	}

	/**
	 * Return a new Path which is this one with the last folder or file name chopped off.
	 * @throws IndexOutOfBoundsException This Path was a root, like "C:\"
	 */
	public Path up() {
		try {
			File f = file.getParentFile(); // As our File for its parent
			if (f == null) throw new IndexOutOfBoundsException(); // Make sure it has one
			return new Path(f);
		} catch (DataException e) { throw new IndexOutOfBoundsException(); } // New Path not absolute
	}
	
	// Look
	
	/** true if there is a file or folder on the disk at this Path, false if it is unoccupied. */
	public boolean exists() { return file.exists(); }
	/** true if there is a file on the disk at this Path. */
	public boolean existsFile() { return exists() && !existsFolder(); }
	/** true if there is a folder on the disk at this Path. */
	public boolean existsFolder() { return file.isDirectory(); }

	/** true if there is a folder on the disk at this Path that is not empty. */
	public boolean existsFolderFull() {
		if (!existsFolder()) return false;
		return !list().isEmpty();
	}
	/** true if there is an empty folder on the disk at this Path. */
	public boolean existsFolderEmpty() {
		if (!existsFolder()) return false;
		return list().isEmpty();
	}

	/** Get a list of the contents of this folder, throws DiskException if this Path isn't to a folder on the disk. */
	public List<Name> list() {

		// Get a list of the folders and files inside
		String[] a = file.list();
		if (a == null) throw new DiskException();

		// Make and return a List of Name objects
		List<Name> list = new ArrayList<Name>();
		for (String s : a)
			list.add(new Name(s));
		return list;
	}
	
	// Change
	
	/** Confirm this Path is to a folder on the disk, making folders as needed, throw a DiskException if it's not. */
	public void folder() {
		if (existsFolder()) return; // It's already a folder
		if (!file.mkdirs()) throw new DiskException(); // Turn returning false into an exception
	}

	/** Confirm this Path is to a folder on the disk we can write to, making folders as needed, or throw a DiskException. */
	public void folderWrite() {
		folder();
		
		// Try to make and delete a subfolder with a unique name
		Path temporary = add(Name.unique());
		temporary.folder();
		temporary.delete();
	}
	
	/**
	 * Move a file or folder at this Path to the given destination Path.
	 * move() can rename a file or a folder, even if the folder has contents.
	 * move() can move a file into an existing folder, but can't make a folder to move the file into.
	 */
	public void move(Path destination) {
		if (!file.renameTo(destination.file)) throw new DiskException(); // Move it by renaming it
	}
	
	/**
	 * Delete the file at the given path, or throw a DiskException.
	 * delete() can delete a file or an empty folder, but not a folder with contents.
	 */
	public void delete() {
		if (!exists()) return; // Nothing to delete, delete() below would return false
		if (!file.delete()) throw new DiskException(); // Turn returning false into an exception
	}
	
	// Work

	/** The Path to s in or relative from the working folder the launcher gave the program when it ran. */
	public static Path work(String s) { return work().add(s); }
	/** The Path to the working folder the launcher gave the program when it ran. */
	public static Path work() {
		return new Path(new File("").getAbsoluteFile()); // A blank File points to the present working directory
	}
}
