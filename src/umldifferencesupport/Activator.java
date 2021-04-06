package umldifferencesupport;

import org.eclipse.jdt.core.JavaCore;
import org.eclipse.ui.IStartup;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import element.GetPluginResource;
import listner.ElementChangedListener;


/**
 * The activator class controls the plug-in life cycle
 */
public class Activator implements IStartup,BundleActivator {

	// The plug-in ID
	public static final String PLUGIN_ID = "UMLDifferenceSupport"; //$NON-NLS-1$
	private static BundleContext context;

	// The shared instance
	private static Activator plugin;

	// 要素取得用リスナー
	ElementChangedListener ecl;

	/**
	 * The constructor
	 */
	public Activator() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		Activator.context = context;
		System.out.println("ActivatorActivatorActivatorActivatorActivatorActivator");
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		Activator.context = null;
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}

	public void log(String messege) { //ログ出力
		System.err.println(messege);
	}

	@Override
	public void earlyStartup() {
		// TODO 自動生成されたメソッド・スタブ
		System.err.println("UMLDifferenceSupport.Activator");
		plugin = this;

		final IWorkbench workbench = GetPluginResource.getWorkbench();
		workbench.getDisplay().asyncExec(new Runnable() {
			@Override
			public void run() {
				System.out.println("Activator.run");
				IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
				if (window != null) {
					// do something
					ecl = new ElementChangedListener();
					JavaCore.addElementChangedListener(ecl);
				}
			}
		});
	}

}
