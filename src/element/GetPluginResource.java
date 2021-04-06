package element;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;

public class GetPluginResource {

	private static GetPluginResource singleton = new GetPluginResource();

	GetPluginResource() {

	}

	public static GetPluginResource getInstance()
	{
		return singleton;
	}

	public static IWorkspaceRoot getWorcspaceRoot()
	{

		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		return workspace.getRoot();

	}

	public static IProject getProject(String projectName)
	{

		IWorkspaceRoot root = getWorcspaceRoot();
		return root.getProject(projectName);

	}

	public static IResource getResource(String projectName)
	{

		IProject project = getProject(projectName);
		return (IResource)project.getAdapter(IResource.class);

	}

	public static IWorkbench getWorkbench()
	{
		IWorkbench workbench = PlatformUI.getWorkbench();
		return workbench;
	}

	/*

	Union3で使ったリソースの取得関連
	おそらく使わない


	//パスからプロジェクトの名前をとる
	public static String getProjectname() {

		String projectname = new String();

		try {

			View view = (View) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView("Union3.ErrorView");
			String projectpath = view.getDirectory();
			String[] pathlist = projectpath.split("\\\\",0);

			//srcの1個前のディレクトリがプロジェクト直下なのでそれを探す
			for(int index = 0; index < pathlist.length; index++)
			{
				if(pathlist[index].equals("src"))
				{
					projectname = pathlist[index - 1];
					//System.out.println("getProjectname" + projectname);
					break;
				}
			}

		} catch (PartInitException e) {
			// TODO �����������ꂽ catch �u���b�N
			e.printStackTrace();
		}

		return projectname;
	}

	public static String getDatabase(IResource resource) {

		try {
			resource.accept(new IResourceVisitor() {
				public boolean visit(IResource resource) throws CoreException {
					if(resource.getType() == IResource.FILE)
					{
						if(resource.getName().equals("test.sqlite3"))
						{
							dbPath = getProjectPath(resource);
						}
					}
					return true;
				}
			});
		} catch (CoreException e2) {
			// TODO �����������ꂽ catch �u���b�N
			e2.printStackTrace();
		}

		return dbPath;
	}

	//実験用
	public static String getSWTResult(IResource resource) {

		try {
			resource.accept(new IResourceVisitor() {
				public boolean visit(IResource resource) throws CoreException {
					if(resource.getType() == IResource.FILE)
					{
						if(resource.getName().equals("swtresult.txt"))
						{
							swtresultPath = getProjectPath(resource);
						}
					}
					return true;
				}
			});
		} catch (CoreException e2) {
			// TODO �����������ꂽ catch �u���b�N
			e2.printStackTrace();
		}

		return swtresultPath;
	}
	public static String getProjectPath(IResource resouece) {

		return resouece.getLocation().toString();

	}

	*/
}