package LiuWang.SootTest;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ApkToDot {
	/**
	 * 
	 * transform one apk to .dot file
	 * @param inputDir:the directory stores the input .apk files
	 * @param outputDir:the directory to store output .dot files
	 * @param apkName: input file name
	 * @param jarsPath : "android-SDK\\platforms\\"
	 * @param androidCallbackPath: an empty .txt file or others
	 */
	public static void run(String inputDir, String outputDir, String apkName, String jarsPath, String androidCallbackPath){
		System.out.println( "-------- transforming " + apkName +"\t-----------");
        AndroidFlowDroidGraph afdg = new AndroidFlowDroidGraph(inputDir+"\\"+apkName, jarsPath, androidCallbackPath);
//        System.out.println(afdg.getCallGraph());
//        System.out.println(afdg.getMainMethod());
        DotGraphUtil.CGToDotWithMethodsName(afdg.getCallGraph(), afdg.getMainMethod());
        DotGraphUtil.exportDotGraph("enriched1", outputDir, DotGraphUtil.dotGraph);
	}
	
	public static void runBatch(String inputDir, String outputDir, String jarsPath, String androidCallbackPath){
		List<String> apkFilesNameList = getApkFiles(inputDir);
		for(String name : apkFilesNameList){
			run(inputDir, outputDir, name, jarsPath, androidCallbackPath);
		}
	}
	
	private static List<String> getApkFiles(String baseDir){
		List<String> result = new ArrayList<String>();
		File dir = new File(baseDir);
		System.out.println(dir.getAbsolutePath()+", isfile:"+dir.isFile());
		File[] dirFiles = dir.listFiles();
		for(File temp : dirFiles){
			//查找指定的文件
			if(temp.isFile() && temp.getAbsolutePath().endsWith(".apk") ){
				System.out.println(temp.getName() + " " + temp.getAbsolutePath());
//				readFileContent(temp);
				result.add(temp.getName());
			}
		}
		return result;
	}
	
}
