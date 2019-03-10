package LiuWang.SootTest;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ApkToDot {
	
	/**
	 * transform one apk to .dot file
	 * @param inputDir:the directory stores the input .apk files
	 * @param outputDir:the directory to store output .dot files
	 * @param apkName: input file name
	 * @param jarsPath : "android-SDK\\platforms\\"
	 * @param androidCallbackPath: an empty .txt file or others
	 */
	public static void runSingle(String apkPath, String outputDir, String jarsPath, String androidCallbackPath){
		File apkFile = new File(apkPath);
		System.out.println( "-------- transforming " + apkFile.getName() +"\t-----------");
        AndroidFlowDroidGraph afdg = new AndroidFlowDroidGraph(apkPath, jarsPath, androidCallbackPath);
        DotGraphUtil.CGToDotWithMethodsName(afdg.getCallGraph(), afdg.getMainMethod());
        DotGraphUtil.exportDotGraph(apkFile.getName(), outputDir, DotGraphUtil.dotGraph);
	}
	
	
	/**
	 * transform a directory of apk files to dot files
	 * @param inputDir:the dir storing apk files being transformed
	 * @param outputDir:the dir storing dot files
	 * @param jarsPath:android sdk platforms path
	 * @param androidCallbackPath: android callback text file
	 */
	public static void runBatch(String inputDir, String outputDir, String jarsPath, String androidCallbackPath){
		List<String> apkAbsPathList = getApkFiles(inputDir);
		for(String apkAbsPath : apkAbsPathList){
			runSingle(apkAbsPath, outputDir, jarsPath, androidCallbackPath);
		}
	}
	
	
	
	/**
	 * get absolute path of apk files in basedir
	 * @param baseDir:the dir storing apk files
	 * @return a list of apk abspath
	 */
	private static List<String> getApkFiles(String baseDir){
		List<String> result = new ArrayList<String>();
		File dir = new File(baseDir);
		System.out.println(dir.getAbsolutePath()+", isfile:"+dir.isFile());
		File[] dirFiles = dir.listFiles();
		for(File temp : dirFiles){
			//查找.apk文件
			if(temp.isFile() && temp.getAbsolutePath().endsWith(".apk") ){
				System.out.println(temp.getAbsolutePath());
				result.add(temp.getAbsolutePath());
			}
		}
		return result;
	}
	
}
