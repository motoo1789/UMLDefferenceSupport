package element;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class GetElementsList implements IElementOperation {

	GetElementsFromSourceCode getelement;
	//Map<String,List<String>> sendClassInfo = new HashMap<String,List<String>>();
	Map<String,ClassInfo> sendClassInfo = new HashMap<String,ClassInfo>();


	GetElementsList(GetElementsFromSourceCode getelement)
	{
		this.getelement = getelement;
	}

	@Override
	public void excute()
	{
		// TODO 自動生成されたメソッド・スタブ
		System.out.println("SendElementsToDatabase.excute");

		getClassList();
		sendDatabase();
	}

	private void sendDatabase()
	{

	}

	private void getClassList()
	{
		ArrayList<String> classnamedata = this.getelement.getClassList();

		if(!classnamedata.isEmpty())
		{
			Iterator<String> key_itr = classnamedata.iterator();

			while(key_itr.hasNext())
			{
				String classname = key_itr.next();
				System.out.println(classname + "：[" + classname + "]");
				List<String> fieldList = getFieldList(classname);
				List<String> methodList = getMethodList(classname);

				ClassInfo classinfo = new ClassInfo();
				classinfo.setClassName(classname);
				classinfo.setFieldList(fieldList);
				classinfo.setMethodList(methodList);

				sendClassInfo.put(classname, classinfo);
			}
		}
		else
		{
			System.out.println("SendElementsToDatabase.excute.getFieldList:空らしい");
		}
	}

	//GetElementクラスからフィールドの一覧を取得
	private List<String> getFieldList(String key)
	{
		HashMap<String,List<String>> fielddata = this.getelement.getFieldList();

		return fielddata.get(key);
	}

	//GetElementクラスからメソッドの一覧を取得
	private List<String> getMethodList(String key)
	{
		HashMap<String,List<String>> methoddata = this.getelement.getMethodList();

		return methoddata.get(key);
	}

	/*
	private void getClassList() {
		ArrayList<String> classdata = this.getelement.getClassList();

		if(!classdata.isEmpty())
		{
			Iterator<String> key_itr = classdata.iterator();

			while(key_itr.hasNext())
			{
				String classname = key_itr.next();
				System.out.println(classname + "：[" + classname + "]");
			}
		}
		else
		{
			System.out.println("SendElementsToDatabase.excute.getFieldList:空らしい");
		}
	}





	private void getFieldList() {
		HashMap<String,List<String>> fielddata = this.getelement.getFieldList();
		Iterator<String> key_itr = fielddata.keySet().iterator();

		if(!fielddata.isEmpty())
		{
			while(key_itr.hasNext())
			{
				String filedname = key_itr.next();
				System.out.println(filedname + "：" + fielddata.get(filedname));
			}
		}
		else
		{
			System.out.println("SendElementsToDatabase.excute.getFieldList:空らしい");
		}
	}

	private void getMethodList() {
		HashMap<String,List<String>> methoddata = this.getelement.getMethodList();
		Iterator<String> key_itr = methoddata.keySet().iterator();

		if(!methoddata.isEmpty())
		{
			while(key_itr.hasNext())
			{
				String methodname = key_itr.next();
				System.out.println(methodname + "：" + methoddata.get(methodname));
			}
		}
		else
		{
			System.out.println("SendElementsToDatabase.excute.getMethodList:空らしい");
		}
	}
	*/
}
