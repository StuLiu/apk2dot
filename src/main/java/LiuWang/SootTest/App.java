package LiuWang.SootTest;

/**
 * Hello world!
 *
 */
public class App {
    public static void main( String[] args ) {
        System.out.println( "==========   Begin!  =============" ); 
//        ApkToDot.run(
//        		"testAPKs", 
//        		"testOutput", 
//        		"enriched1.apk", 
//        		"E:\\android-sdk-windows\\platforms", 
//        		"AndroidCallbacks.txt");
        
        ApkToDot.runBatch("testAPKs", 
        		"testOutput",  
        		"E:\\android-sdk-windows\\platforms", 
        		"AndroidCallbacks.txt");
        System.out.println("==========   Finished!  =============");
    }
}
