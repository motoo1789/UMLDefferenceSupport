package listner;

import java.util.HashMap;
import java.util.Map;

import element.GetElements;
import element.SendElementsToDatabase;

public abstract class AbstractListner {

	final protected String GET_KEY = "要素の取得";
	final protected String SEND_KEY = "要素の送信";
	protected Map<String,Object> elementoperation = new HashMap<String,Object>();

	AbstractListner()
	{
		if(elementoperation.isEmpty())
		{
			// 空だったら要素に対する操作のオブジェクトを生成
			setElementObject();
		}
		else
		{
			// すでに生成されてたら何もしない
			System.out.println("AbstractListner:すでにMapにインスタンスはある");
		}
	}

	private void setElementObject()
	{
		// マップへオブジェクトの追加
		elementoperation.put(GET_KEY, new GetElements());
	}

	protected abstract void listnerToElement();

}
