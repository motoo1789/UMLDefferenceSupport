package element;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class SendElementsToDatabase implements IElementOperation {

	GetElements getelement;

	SendElementsToDatabase(GetElements getelement)
	{
		this.getelement = getelement;
	}

	@Override
	public void excute() {
		// TODO 自動生成されたメソッド・スタブ
		System.out.println("SendElementsToDatabase.excute");

		getClassList();
		getFieldList();
		getMethodList();
	}

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
}
