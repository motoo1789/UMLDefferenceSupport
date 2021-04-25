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

	private String projectname = new String();	// プロジェクトの名前　未定
	private String className = new String();	// クラスの名前
	private int classaccess = 0;	// クラスのアクセス修飾子

	private Map<String, String> 		fieldMap = new HashMap<String,String>(); 						// Map<フィールドの名前,フィールドの型>
	private Map<String, Integer> 		fieldAccessMap = new HashMap<String,Integer>(); 				// Map<フィールドの名前,アクセス修飾子>
	private Map<String, String> 		methodreturnvalueMap = new HashMap<String,String>(); 			// Map<メソッド名,戻りの型>
	private Map<String, Integer> 		methodAccessMap = new HashMap<String,Integer>(); 				// Map<メソッド名,戻りの型>
	private Map<String, List<String>> 	methodparatypeMap = new HashMap<String,List<String>>(); 		// Map<メソッドの名前,メソッドの引数の型>
	private Map<String, List<String>> 	methodparanameMap = new HashMap<String,List<String>>(); 		// Map<メソッドの名前,メソッドの引数の名前>


	public ActiveSourceCodeInfo()
	{
		System.out.println("クラス情報格納のためのObjectが生成");
	}

	void setinfo(IType type) throws JavaModelException
	{
		setClassInfo(type);
		setFieldInfo(type);
		setMethodInfo(type);
	}

	void setClassInfo(IType type)
	{
		try {
			className = type.getElementName();
			classaccess = type.getFlags();
		} catch (JavaModelException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	}

	void setFieldInfo(IType type) throws JavaModelException
	{
		System.out.println("classname:" + type.getElementName());
		IField[] fields = type.getFields();

		//フィールドの名前と型をMapで」保存
		for(IField field : fields)
		{
			System.out.println("fieldname:" + field.getElementName() + " type:" + field.getTypeSignature());
			System.out.println("field修飾子:" + field.getFlags());
			fieldAccessMap.put(field.getElementName(), field.getFlags());
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
			System.out.println("修飾子：" + method.getFlags());
			System.out.println("----------methodname----------");
			String methodName = method.getElementName();
			System.out.println("methodname:" + methodName);

			//アクセス修飾子
			methodAccessMap.put(methodName, method.getFlags());

			// メソッドの戻り値について
			methodreturnvalueMap.put(methodName, method.getReturnType());

			// メソッドのパラメータについて
			setMethodParaType(methodName,method); // パラメータの型
			setMethodParaName(methodName,method); // パラメータの名前

		}

	}

	// パラメータの型
	void setMethodParaType(String methodName, IMethod method)
	{
		List<String> methodparatypeList = new ArrayList<String>();

		System.out.println("----------paratype----------");
		for(String paratype : method.getParameterTypes())
		{
			methodparatypeList.add(paratype);
			System.out.println("paratype:" + methodparatypeList);
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
			methodparanameList.add(paraname);
			System.out.println("paraname:" + methodparanameList);
		}
		methodparanameMap.put(methodName, methodparanameList);
		System.out.println("メソッドのパラメータの名前をListにaddした");

		System.out.println();
	}

	String getClassName()
	{
		return className;
	}

	int getClassAccess()
	{
		return classaccess;
	}

	Map<String, String> getFieldMap()
	{
		return fieldMap;
	}

	Map<String, Integer> getFieldAccessMap()
	{
		return fieldAccessMap;
	}

	Map<String, String> getMethodeRturnValueMap()
	{
		return methodreturnvalueMap;
	}

	Map<String, Integer> getMethodAccessMap()
	{
		return methodAccessMap;
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
