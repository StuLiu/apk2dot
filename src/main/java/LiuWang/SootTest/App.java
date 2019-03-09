package LiuWang.SootTest;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" ); 
        String apkPath = ".\\testAPKs\\enriched1.apk";
        String jarPath = "E:\\android-sdk-windows\\platforms";
        String CallBacksPath = ".\\AndroidCallbacks.txt";
        AndroidFlowDroidGraph afdg = new AndroidFlowDroidGraph(apkPath, jarPath ,CallBacksPath);
        System.out.println(afdg.getCallGraph());
        
        DotGraphUtil.CGToDotWithMethodsName(afdg.getCallGraph(), afdg.getMainMethod());
        DotGraphUtil.exportDotGraph("enriched1", ".\\testOutput", DotGraphUtil.dotGraph);
    }
}
