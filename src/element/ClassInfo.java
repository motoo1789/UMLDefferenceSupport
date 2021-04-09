package element;

import java.util.ArrayList;
import java.util.List;

public class ClassInfo {

	String classname = new String();
	List<String> fieldList = new ArrayList<String>();
	List<String> methodList = new ArrayList<String>();
	
	void setClassName(String classname)
	{
		this.classname = classname;
	}
	
	void setFieldList(List<String> fieldList) 
	{
		this.fieldList = fieldList;
	}
	
	void setMethodList(List<String> methodList)
	{
		this.methodList = methodList;
	}
}
