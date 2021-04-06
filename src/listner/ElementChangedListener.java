package listner;

import org.eclipse.jdt.core.ElementChangedEvent;
import org.eclipse.jdt.core.IElementChangedListener;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaElementDelta;

import control.PluginControler;
import element.IElementOperation;
import umldifferencesupport.Activator;

public class ElementChangedListener extends AbstractListner implements IElementChangedListener {

	//private PluginControler controller;
	IElementOperation getelementobj = (IElementOperation) elementoperation.get(GET_KEY);

	public ElementChangedListener() {
		// TODO 自動生成されたコンストラクター・スタブ
		super(); // 親クラスのMapの生成
		System.out.println("ElementChangedListenerの生成");
		//this.controller = controller;
		Activator.getDefault().log("ElementChangedListener");
	}

	//吉田先輩のやつをいじってない
	@Override
	public void elementChanged(ElementChangedEvent arg0) {
		// TODO 自動生成されたメソッド・スタブ
		System.out.println("ElementChangedListenerのリスナー");
		int kind = arg0.getDelta().getKind();


		switch(kind){

		case IJavaElementDelta.ADDED:
		case IJavaElementDelta.REMOVED:
			listnerToElement();
			break;

		case IJavaElementDelta.CHANGED:
			if((arg0.getDelta().getFlags() & IJavaElementDelta.F_AST_AFFECTED) != 0) {
				if(_check(arg0.getDelta().getAffectedChildren())) {
					listnerToElement();
				}
			}
			break;

		default:
			break;
		}
	}

	//吉田先輩のやつをいじってない
	private boolean _check(IJavaElementDelta[] deltas) {
		for (IJavaElementDelta d : deltas) {
			switch(d.getElement().getElementType()) {
				case IJavaElement.ANNOTATION:
				case IJavaElement.FIELD:
				case IJavaElement.METHOD:
					return true;
				default:
					return _check(d.getAffectedChildren());
			}
		}
		return false;
	}

	@Override
	protected void listnerToElement() {
		// TODO 自動生成されたメソッド・スタブ
		getelementobj.excute();
	}

}