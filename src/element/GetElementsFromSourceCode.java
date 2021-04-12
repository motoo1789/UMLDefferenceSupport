    package element;

    import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaModel;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMethod;
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
    		realize();
    		sendobj.excute();

    	}

    	private synchronized void realize()
    	{
    		if(sendobj == null)
    			sendobj = new GetElementsList(this);
    	}

    	public void getElement(IWorkspaceRoot root) {

    		IJavaModel model = JavaCore.create(root);

    		try {
    			IJavaElement[] elements = model.getChildren();

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

    											List<String> fieldName = new ArrayList<>();
    											List<String> methodName = new ArrayList<>();
    											Map<String, String> fieldtypemap = new HashMap<String,String>();	// <フィールド名,型>
    											Map<String, String> methodreturnvalueMap = new HashMap<String,String>(); // <メソッド名,戻りの型>
    											Map<String, List<String>> methodparatypeMap = new HashMap<String,List<String>>(); // <メソッド名,引数の型>
    											Map<String, List<String>> methodparannmeMap = new HashMap<String,List<String>>(); // <メソッド名,引数の名前>
    											//List<String> instanceList = new ArrayList<>();
    											//List<Integer> misAnnotationList = new ArrayList<>();
    											Map<String, Integer> fieldPos = new HashMap<>();
    											Map<String, Integer> methodPos = new HashMap<>();
    											Map<String, Integer> instancePos = new HashMap<>();




    											IJavaElement elementss[] = compilationUnit.getChildren();
    											for(IJavaElement javaElement : elementss) {
    												System.out.println("GetElements.excute.:拡張for文");
    												String className = "ClassGetErr";

    												if(javaElement.getElementType() == IJavaElement.TYPE) { //ソースタイプ(クラス)かを確認
    													IType type = (IType)javaElement;

    													System.out.println();
    													System.out.println("classname:" + type.getElementName());
    													IField[] fields = type.getFields();
    													for(IField field : fields)
    														System.out.println("fieldname:" + field.getElementName() + " type:" + field.getTypeSignature());

    													IMethod[] methods = type.getMethods();
    													for(IMethod method : methods)
    													{
    														System.out.println("----------methodname----------");
    														System.out.println("methodname:" + method.getElementName());
    														System.out.println("----------signaturetype----------");
    														System.out.println("sigunaturetype:" + method.getSignature());
    														System.out.println();
    														System.out.println("----------returntype----------");
    														System.out.println(" returntype:" + method.getReturnType());
    														System.out.println("----------paratype----------");
    														for(String paratype : method.getParameterTypes())
    															System.out.println("paratype:" + paratype);
    														System.out.println();
    														System.out.println("----------paraname----------");
    														for(String paraname : method.getParameterNames())
    															System.out.println("paraname:" + paraname);
    														System.out.println();
    													}

    													fieldsList.put(className, fieldName);
    													methodsList.put(className, methodName);
    													//this.misAnnotationList.put(className, misAnnotationList);
    													this.fieldPos.put(className, fieldPos);
    													this.methodPos.put(className, methodPos);
    													this.instancePos.put(className, instancePos);

    													// getSignature()

//    													boolean addAnnotation = false;
//
//    													for(IAnnotation annotaion : type.getAnnotations()) {
//    														if(annotaion.getElementName().equals(CLASS_ANNOTATION)) {
//    															className = annotaion.getMemberValuePairs()[0].getValue().toString();
//    															classNameList.add(className);
//    															addAnnotation = true;
//    														}
//    													}
//
//    													if(!addAnnotation) {//アノテーションがついてなかったら
//    														className = type.getElementName();
//    														classNameList.add(className);//クラス名
//    													}
//
//    													originalClassName.put(type.getElementName(), className);
//    													classPos.put(type.getElementName(), type.getSourceRange().getOffset());
//
//
//    													for(IField field : fields) {
//    														System.out.println("GetElements.excute.IField拡張for文");
//    														addAnnotation = false;
//
//    														if(originalClassNameList.contains(field.toString().split(" ", 2)[0])) {//インスタンスかを確認
//    															for(IAnnotation annotation : field.getAnnotations()){
//    																if(annotation.getElementName().equals(INSTANCE_ANNOTATION)) {
//    																	instanceList.add(annotation.getMemberValuePairs()[0].getValue().toString());
//    																	addAnnotation = true;
//    																	instancePos.put(className, field.getNameRange().getOffset());
//    																}
//    																if(annotation.getElementName().equals(FIELD_ANNOTATION)) {//間違ったアノテーションがついていたらoffsetを記録
//    																	misAnnotationList.add(field.getNameRange().getOffset());
//    																}
//    															}
//
//    															if(!addAnnotation) {
//    																instanceList.add(field.getElementName());
//    																instancePos.put(field.getElementName(), field.getNameRange().getOffset());
//    															}
//    														}else { //インスタンスじゃない場合(普通のフィールド)
//    															for(IAnnotation annotation : field.getAnnotations()) {
//    																if(annotation.getElementName().equals(FIELD_ANNOTATION)) {
//    																	fieldName.add(annotation.getMemberValuePairs()[0].getValue().toString());
//    																	fieldPos.put(annotation.getMemberValuePairs()[0].getValue().toString(), field.getNameRange().getOffset());
//    																	addAnnotation = true;
//    																}
//
//    																if(annotation.getElementName().equals(INSTANCE_ANNOTATION)) {
//    																	misAnnotationList.add(field.getNameRange().getOffset());
//    																}
//    															}
//
//    															if(!addAnnotation) {
//    																fieldName.add(field.getElementName());
//    																fieldPos.put(field.getElementName(), field.getNameRange().getOffset());
//    															}
//    														}
//    													}
//
//    													for(IMethod method : methods) {
//    														addAnnotation = false;
//
//    														for(IAnnotation annotation : method.getAnnotations()) {
//    															if(annotation.getElementName().equals(METHOD_ANNOTATION)) {
//    																addAnnotation = true;
//    																methodName.add(annotation.getMemberValuePairs()[0].getValue().toString());
//    																methodPos.put(annotation.getMemberValuePairs()[0].getValue().toString(), method.getNameRange().getOffset());
//    															}
//    														}
//
//    														if(!addAnnotation) {
//    															methodName.add(method.getElementName());
//    														}
//    													}


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

