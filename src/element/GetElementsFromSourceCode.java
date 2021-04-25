    package element;

    import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaModel;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;

    public class GetElementsFromSourceCode implements IElementOperation{

    	final GetPluginResource singletonPluginResource = GetPluginResource.getInstance();
    	private IElementOperation sendobj;

    	final String CLASS_ANNOTATION = "UMLClass";
    	final String FIELD_ANNOTATION = "UMLField";
    	final String METHOD_ANNOTATION = "UMLMethod";
    	final String INSTANCE_ANNOTATION = "UMLAssosiation";

    	@Override
    	public void excute()
    	{
    		System.out.println("GetElements.excute");
    		// TODO 自動生成されたメソッド・スタブ
    		IWorkspaceRoot root = singletonPluginResource.getWorcspaceRoot();
    		getElement(root);
    	}


    	public void getElement(IWorkspaceRoot root) {

    		IJavaModel model = JavaCore.create(root);

    		try {
    			IJavaElement[] elements = model.getChildren();
    			Map<String, ActiveSourceCodeInfo> sendDatabaseinfo = new HashMap<String,ActiveSourceCodeInfo>();
    			SendElementsToDatabase singletonDatabaseObject = SendElementsToDatabase.getInstance();

    			for(int i = 0; i < elements.length; i++) {
    				System.out.println("GetElements.excute.i:for");
    				if(elements[i].getElementType() == 2) {
    					for(int j = 0; j < ((IJavaProject)elements[i]).getChildren().length; j++) {
    						System.out.println("GetElements.excute.j:for");
    						if(((IJavaProject)elements[i]).getChildren()[j].getElementType() == 3) {
    							IPackageFragmentRoot packageFragmentRoot = ((IPackageFragmentRoot)((IJavaProject)elements[i]).getChildren()[j]);

    							for(int k = 0; k < packageFragmentRoot.getChildren().length; k++) {//すべてのパッケージを網羅
    								System.out.println("GetElements.excute.k:for");
    								if(packageFragmentRoot.getChildren()[k].getElementType() == 4) {
    									IPackageFragment packageFragment = ((IPackageFragment)packageFragmentRoot.getChildren()[k]);

    									for(int n = 0; n < packageFragment.getChildren().length; n++) {//パッケージからicompilationunitを探す
    										System.out.println("GetElements.excute.n:for");
    										if(packageFragment.getChildren()[n].getElementType() == 5) {

    											ICompilationUnit compilationUnit = ((ICompilationUnit)packageFragment.getChildren()[n]);
    											IJavaElement elementss[] = compilationUnit.getChildren();

    											for(IJavaElement javaElement : elementss) {
    												System.out.println("GetElements.excute.:拡張for文");
    												String className = "ClassGetErr";

    												if(javaElement.getElementType() == IJavaElement.TYPE) { //ソースタイプ(クラス)かを確認
    													IType type = (IType)javaElement;
    													className = type.getElementName();


    													// ClassInfoクラスに処理をやらせるかどうか
    													ActiveSourceCodeInfo sourceinfo = new ActiveSourceCodeInfo();
    													sourceinfo.setinfo(type);

    													sendDatabaseinfo.put(className, sourceinfo);

    													System.out.println();

    												}
    											}
    										}
    									}
    								}
    							}
    						}
    					}
    				}
    			}
    			// データベースに送るメソッド
    			singletonDatabaseObject.sendDatabase(sendDatabaseinfo);

    		}catch (JavaModelException e) {
    			// TODO 自動生成された catch ブロック
    			e.printStackTrace();
    		}

    		System.err.println("比較終了");

    	}


    }

