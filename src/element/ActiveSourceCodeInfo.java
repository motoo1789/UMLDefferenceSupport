package element;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;

public class ActiveSourceCodeInfo {

	private String projectname = new String();
	private String classname = new String();
	private Map<String, String> fieldMap = new HashMap<String,String>(); 						// Map<フィールドの名前,フィールドの型>
	private Map<String, String> methodreturnvalueMap = new HashMap<String,String>(); 			// Map<メソッド名,戻りの型>
	private Map<String, List<String>> methodparatypeMap = new HashMap<String,List<String>>(); 	// Map<メソッドの名前,メソッドの引数の型>
	private Map<String, List<String>> methodparanameMap = new HashMap<String,List<String>>(); 	// Map<メソッドの名前,メソッドの引数の名前>


	public ActiveSourceCodeInfo()
	{
		System.out.println("クラス情報格納のためのObjectが生成");
	}

	void setinfo(IType type) throws JavaModelException
	{
		setClassName(type);
		setFieldInfo(type);
		setMethodInfo(type);
	}

	void setClassName(IType type)
	{
		this.classname = type.getElementName();
	}

	void setFieldInfo(IType type) throws JavaModelException
	{
		System.out.println("classname:" + type.getElementName());
		IField[] fields = type.getFields();

		//フィールドの名前と型をMapで」保存
		for(IField field : fields)
		{
			System.out.println("fieldname:" + field.getElementName() + " type:" + field.getTypeSignature());
			fieldMap.put(field.getElementName(), field.getTypeSignature());
		}
		System.out.println("フィールドの格納が終わった");
	}

	void setMethodInfo(IType type) throws JavaModelException
	{
		IMethod[] methods = type.getMethods();

		for(IMethod method : methods)
		{
			System.out.println("----------methodname----------");
			String methodName = method.getElementName();
			System.out.println("methodname:" + methodName);

			// メソッドの戻り値について
			setMethodReturnValue(methodName,method);

			// メソッドのパラメータについて
			setMethodParaType(methodName,method); // パラメータの型
			setMethodParaName(methodName,method); // パラメータの名前

		}

	}

	// 戻り値
	void setMethodReturnValue(String methodName, IMethod method) throws JavaModelException
	{
		System.out.println("----------returntype----------");
		System.out.println(" returntype:" + method.getReturnType());
		methodreturnvalueMap.put(methodName, method.getReturnType());
	}

	// パラメータの型
	void setMethodParaType(String methodName, IMethod method)
	{
		List<String> methodparatypeList = new ArrayList<String>();

		System.out.println("----------paratype----------");
		for(String paratype : method.getParameterTypes())
		{
			System.out.println("paratype:" + paratype);
			methodparatypeList.add(paratype);
		}
		methodparatypeMap.put(methodName, methodparatypeList);
		System.out.println("メソッドのパラメータの型をListにaddした");
	}

	// パラメータの名前
	void setMethodParaName(String methodName, IMethod method) throws JavaModelException
	{
		List<String> methodparanameList = new ArrayList<String>();

		System.out.println("----------paraname----------");
		for(String paraname : method.getParameterNames())
		{
			System.out.println("paraname:" + paraname);
			methodparanameList.add(paraname);
		}
		methodparanameMap.put(methodName, methodparanameList);
		System.out.println("メソッドのパラメータの名前をListにaddした");

		System.out.println();
	}

	String getClassName()
	{
		return classname;
	}

	Map<String, String> getFieldMap()
	{
		return fieldMap;
	}

	Map<String, String> getMethodeRturnValueMap()
	{
		return methodreturnvalueMap;
	}

	Map<String, List<String>> getMethodParaNameMap()
	{
		return methodparanameMap;
	}

	Map<String, List<String>> getMethodParaTypeMap()
	{
		return methodparatypeMap;
	}
}
