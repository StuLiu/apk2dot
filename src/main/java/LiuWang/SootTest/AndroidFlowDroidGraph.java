package LiuWang.SootTest;

import soot.Scene;
import soot.SootMethod;
import soot.jimple.infoflow.InfoflowConfiguration;
import soot.jimple.infoflow.android.InfoflowAndroidConfiguration;
import soot.jimple.infoflow.android.SetupApplication;
import soot.jimple.toolkits.callgraph.CallGraph;
 
/**
 * @ 获取android应用控制流图
 * @author liuwang
 * @create 2019-03-09 19:20
 * ---------------------------------------------------------------   
 *	原作者：dmfrm 
 *	来源：CSDN 
 *	原文：https://blog.csdn.net/u010889616/article/details/80878224 
 *	版权声明：本文为博主原创文章，转载请附上博文链接！
 * --------------------------------------------------------------- 
 **/
public class AndroidFlowDroidGraph {
	
    private SetupApplication setupApplication;
    
    public AndroidFlowDroidGraph(){
    	String apkPath = ".\\testAPKs\\enriched1.apk";
    	String jarsPath = "E:\\android-sdk-windows\\platforms";
    	String androidCallbackPath = ".\\AndroidCallbacks.txt";
    	init(apkPath, jarsPath, androidCallbackPath);
    }
    
    /**
     * @param apkPath:要分析的apk的路径
     * @param jarsPath:android jar路径
     * @param androidCallbackPath:文件一定要有，文件内容可以为空
     */
    public AndroidFlowDroidGraph(String apkPath, String jarsPath, String androidCallbackPath){
    	init(apkPath, jarsPath, androidCallbackPath);
    }
    
    public SootMethod getMainMethod(){
    	return setupApplication.getDummyMainMethod();
    }
    
    public CallGraph getCallGraph(){
        //构建控制流图，比较耗时
        setupApplication.constructCallgraph();
    	return Scene.v().getCallGraph();
    }
    
    /**
     * init SetupApplication
     * @param apkPath:要分析的apk的路径
     * @param jarsPath:android jar路径
     * @param androidCallbackPath:文件一定要有，文件内容可以为空
     */
    private void init(String apkPath, String jarsPath, String androidCallbackPath){
    	String androidJarPath = Scene.v().getAndroidJarPath(jarsPath, apkPath);
        setupApplication = new SetupApplication(androidJarPath, apkPath);
        InfoflowAndroidConfiguration config = setupApplication.getConfig();
        config.setCallgraphAlgorithm(InfoflowConfiguration.CallgraphAlgorithm.SPARK);
        setupApplication.setCallbackFile(androidCallbackPath);
    }
    
//    public static void main(String[] args) {
//        //初始化soot配置
//    	AndroidFlowDroidGraph afdg = new AndroidFlowDroidGraph();
//        System.out.println(afdg.getCallGraph());
//        System.out.println("mainMethod:"+afdg.getMainMethod());
//    }

}
