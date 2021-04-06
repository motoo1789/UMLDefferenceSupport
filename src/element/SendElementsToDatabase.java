package element;

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
		HashMap<String,List<String>> instancedata = this.getelement.getInstanceList();

		Iterator<String> key_itr = instancedata.keySet().iterator();

		while(key_itr.hasNext())
		{
			String key = key_itr.next();
			System.out.println(instancedata.get(key));
		}

	}

}
