import java.util.ArrayList;
import java.util.Arrays;
public class Classes {
	/* Class Variables */
	private static Classes currentClass;
	private String className; // Name of the class
	private ArrayList<String> names; // Names of people in the class
	private static ArrayList<String> classesNameList = new ArrayList<String>();
	private static ArrayList<Classes> classesList = new ArrayList<Classes>();
	private ArrayList<String> removedNames;
	private Wheel classWheel;
	/**
	 * Constructs a default Classes object
	 */
	public Classes() {
		this.className = "New Class";
		this.names = new ArrayList<String>(Arrays.asList("Ledoot", "Mary", "Larry", "Cherry", "Sherry", "Huckleberry", "Barry"));
		classWheel = new Wheel(this);
		addNameToClassList(this.className);
		classesList.add(this);
		Control_Panel.addClassToBox(this);
		currentClass = this;
		Control_Panel.getLaBox().setSelectedIndex(Classes.getClassesList().size() - 1);
		removedNames = new ArrayList<String>();
	}
	/**
	 * Constructs a Classes object
	 */
	public Classes(String cName, ArrayList<String> n) {
		this.className = cName;
		this.names = n;
		classWheel = new Wheel(this);
		addNameToClassList(this.className);
		classesList.add(this);
		Control_Panel.addClassToBox(this);
		currentClass = this;
		Control_Panel.getLaBox().setSelectedIndex(Classes.getClassesList().size() - 1);
		removedNames = new ArrayList<String>();
	}
	/* Helper method to add name to classesNameList ensuring uniqueness */
	private void addNameToClassList(String name) {
		String uniqueName = name.trim();
		int count = 1;
		while (classesNameList.contains(uniqueName)) {
			uniqueName = name.trim() + "(" + count + ")";
			count++;
		}
		this.className = uniqueName;
		classesNameList.add(this.className);
	}
	/* Return names list for this class */
	public ArrayList<String> getNames(){
		return this.names;
	}
	/* Returns the name of a Classes Object */
	public String getClassName() {
		return this.className;
	}
	public void setClassName(String s) {
		String trimmedNewName = s;
		String uniqueNewName = trimmedNewName;
		int count = 1;
		int currentIndex = classesNameList.indexOf(this.className);
		// Check for duplicates excluding the current class name
		for (int i = 0; i < classesNameList.size(); i++) {
			if (i != currentIndex && classesNameList.get(i).equals(uniqueNewName)) {
				uniqueNewName = trimmedNewName + "(" + count + ")";
				count++;
				i = -1; // Reset to re-check from the beginning
			}
		}
		this.className = uniqueNewName;
		if (currentIndex != -1) {
			classesNameList.set(currentIndex, this.className);
		}
	}
	/* Returns the currently open class */
	public static Classes getCurrentClass() {
		return currentClass;
	}
	public static void setCurrentClass(Classes c) {
		currentClass = c;
	}
	/* returns list of all classes */
	public static ArrayList<String> getClassesNameList() {
		return classesNameList;
	}
	/* returns the wheel object for the class */
	public Wheel getClassWheel() {
		return this.classWheel;
	}
	public ArrayList<String> getRemovedNames(){
		return this.removedNames;
	}
	public void setRemovedNames(ArrayList<String> r){
		this.removedNames = r;
	}
	public void putIfAbsent(ArrayList<String> r) {
		if(this.getRemovedNames() == null) {
			removedNames = r;
		}
	}
	/* Finds the Classes object with the specified Name */
	public static Classes getClassFromName(String s) {
		for (Classes c : classesList) {
			if (c.getClassName().equals(s)) {
				return c;
			}
		}
		return null;
	}
	/* Sets the names for the class and its Wheel */
	public void setClassNames(ArrayList<String> s) {
		this.names = s;
		this.classWheel.setNames(s);
	}
	public static ArrayList<Classes> getClassesList(){
		return classesList;
	}
	// Removed noRepeatName() method
}


