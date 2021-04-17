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

    	private HashMap<String, IResource> resourceList = new HashMap<>();
    	private HashMap<String, List<String>> instanceList = new HashMap<>(); //key=クラス名, v=インスタンスの一覧
    	private ArrayList<String> classNameList = new ArrayList<>(); //アノテーション適用後のクラス名一覧
    	private HashMap<String, String> originalClassName = new HashMap<>(); //key=もとのクラス名, value=アノテーションのクラス名(アノテーションがない場合はもとの名前)
    	private ArrayList<String> originalClassNameList = new ArrayList<>();//ソースのもとのクラス名一覧(インスタンスの判別に使う)
    	private HashMap<String, Integer> classPos = new HashMap<>();//クラス名の開始位置 key:オリジナルクラス名
    	private HashMap<String, List<String>> fieldsList = new HashMap<>(); //key=クラス名, value=keyクラスのフィールド名の配列
    	private HashMap<String, List<String>> methodsList = new HashMap<>(); //key=クラス名, value=keyクラスのメソッド名の配列
    	private HashMap<String, Map<String, Integer>> instancePos = new HashMap<>(); //key=クラス名, v=インスタンスのオフセット
    	private HashMap<String, Map<String, Integer>> fieldPos = new HashMap<>(); //key=クラス名, v=フィールドのオフセット(key=アノテーションのクラス名)
    	private HashMap<String, Map<String, Integer>> methodPos = new HashMap<>(); //key=クラス名, v=メソッドのオフセット
    	private HashMap<String, List<Integer>> misAnnotationList = new HashMap<>(); //key=クラス名, value=keyクラスの間違ったアノテーションがついているフィールドのoffset


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

    									makeOriginalClassNameList(packageFragment);//先にもとのクラス名の一覧を取得

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

    	private void makeOriginalClassNameList(IPackageFragment packageFragment) {//インスタンスを見つけるために先にクラス名の一覧を取得,リソースの一覧も同時に取得

    		try {
    			for(int n = 0; n < packageFragment.getChildren().length; n++) {//パッケージからicompilationunitを探す
    				if(packageFragment.getChildren()[n].getElementType() == 5) {
    					ICompilationUnit compilationUnit = ((ICompilationUnit)packageFragment.getChildren()[n]);
    					IJavaElement elementss[] = compilationUnit.getChildren();
    					for(IJavaElement javaElement : elementss) {

    						String className = "ClassGetErr";

    						if(javaElement.getElementType() == IJavaElement.TYPE) { //ソースタイプ(クラス)かを確認
    							IType type = (IType)javaElement;
    							className = type.getElementName();
    							originalClassNameList.add(className);
    							resourceList.put(className, type.getResource());
    						}
    					}
    				}
    			}
    		} catch (JavaModelException e) {
    			// TODO 自動生成された catch ブロック
    			e.printStackTrace();
    		}
    	}

    	public IResource getResource(String activeTab) {
    		return resourceList.get(activeTab);
    	}

    	public HashMap<String, List<String>> getInstanceList(){
    		return instanceList;
    	}

    	public ArrayList<String> getClassList(){
    		return classNameList;
    	}

    	public String getAnnotationClassName(String className) {
    		return originalClassName.get(className);
    	}

    	public int getClassPos(String className) {
    		return classPos.get(className);
    	}

    	public HashMap<String, List<String>> getFieldList(){
    		return fieldsList;
    	}

    	public HashMap<String, List<String>> getMethodList(){
    		return methodsList;
    	}

    	public HashMap<String, Map<String, Integer>> getFieldPos(){
    		return fieldPos;
    	}

    	public HashMap<String, Map<String, Integer>> getMethodPos(){
    		return methodPos;
    	}

    	public HashMap<String, Map<String, Integer>> getInstancePos(){
    		return instancePos;
    	}

    	public List<Integer> getMisAnnotationList(String activeTab){
    		return misAnnotationList.get(originalClassName.get(activeTab));
    	}

    	public void listClear() {
    		resourceList.clear();
    		instanceList.clear();
    		classNameList.clear();
    		originalClassName.clear();
    		originalClassNameList.clear();
    		fieldsList.clear();
    		methodsList.clear();
    		instancePos.clear();
    		fieldPos.clear();
    		methodPos.clear();
    		misAnnotationList.clear();
    	}

    }

