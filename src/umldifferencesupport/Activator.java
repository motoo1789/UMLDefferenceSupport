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
public class Activator extends AbstractUIPlugin implements IStartup,BundleActivator {

	// The plug-in ID
	public static final String PLUGIN_ID = "UMLDifferenceSupport"; //$NON-NLS-1$

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
		super.start(context);
		System.out.println("ActivatorActivatorActivatorActivatorActivatorActivator");
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
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
		System.out.println("ActivatorActivatorActivatorActivatorActivatorActivator");
		plugin = this;

		final IWorkbench workbench = GetPluginResource.getWorkbench();
		workbench.getDisplay().asyncExec(new Runnable() {

			@Override
			public void run() {
				// TODO 自動生成されたメソッド・スタブ
				ecl = new ElementChangedListener();
				JavaCore.addElementChangedListener(ecl);
			}

		});
	}

}
