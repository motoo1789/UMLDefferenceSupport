package element;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClassInfo {

	private String projectname = new String();
	private String classname = new String();
	private List<String> fieldList = new ArrayList<String>();
	private List<String> methodList = new ArrayList<String>();
	private Map<String, String> fieldtypemap = new HashMap<String,String>(); 					// Map<クラスの名前,フィールドの型>
	private Map<String, String> fieldtypename = new HashMap<String,String>(); 					// Map<クラスの名前,フィールの名前>
	private Map<String, List<String>> methodparatypemap = new HashMap<String,List<String>>(); 	// Map<メソッドの名前,メソッドの引数の型>
	private Map<String, List<String>> methodparanamemap = new HashMap<String,List<String>>(); 	// Map<メソッドの名前,メソッドの引数の名前>

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

	String getClassName()
	{
		return classname;
	}

	List<String> getFieldList()
	{
		return fieldList;
	}

	List<String> getMethodList()
	{
		return methodList;
	}
}
