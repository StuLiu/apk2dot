package LiuWang.SootTest;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/**
 * 
 * @author liuwang 
 * @department WHU CS
 * @date 2019/3/9
 */
public class App {
    public static void main( String[] args ) {
    	CommandLineParser parser = new BasicParser( );
		Options options = new Options( );
		options.addOption("h", "helpMsg", false, "Print this usage information.");
		options.addOption("f", "typeFile", false, "transfor an apk file to dot file." );
		options.addOption("d", "typeDir", false, "transfor a directory of apk files to dot files." );
		options.addOption("i", "input", true, "the path of input directory storing .apk files or of an apk file.");
		options.addOption("o", "outputDir", true, "the output directory to store output .dot files.");
		options.addOption("s", "sdkDir", true, "the android sdk directory.");
		options.addOption("c", "callbackFile", true, "the call backs file.");
		// Parse the program arguments and do transforming
		CommandLine cl;
		try {
			cl = parser.parse( options, args );
			
			System.out.println(cl.hasOption('h')+""+cl.hasOption('i')+cl.hasOption('o')+cl.hasOption('s')+cl.hasOption('c'));

			if( cl.hasOption('h') ) {
				System.out.println(helpMsg);
				System.exit(0);
			}
			
			if( cl.hasOption('f') && cl.hasOption('d') || !cl.hasOption('f') && !cl.hasOption('d')) {
				throw new IllegalArgumentException("Ether -f or -d!");
			} else {
				if( !cl.hasOption('i') || !cl.hasOption('o') || !cl.hasOption('s') || !cl.hasOption('c') ){
					throw new IllegalArgumentException("The options(-i, -o, -s and -c) are nessesary!");
				}
				String input = cl.getOptionValue('i'); 			// "testAPKs";
				String outputDir = cl.getOptionValue('o'); 		// "testOutput";
				String sdkDir = cl.getOptionValue('s'); 		// "E:\\android-sdk-windows\\platforms";
				String callbackFile = cl.getOptionValue('c'); 	// "AndroidCallbacks.txt";
				System.out.println("=================== Begin transforming ===================");
				if( cl.hasOption('f') ) {
					ApkToDot.runSingle(input, outputDir, sdkDir, callbackFile);
				} else {
					ApkToDot.runBatch(input, outputDir, sdkDir, callbackFile);
				}
				System.out.println("===================      Finished!     ===================");
			}	
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			System.out.println("Parse Error\n" + helpMsg);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
		} catch (Exception e){
			System.out.println(e);
		}
		
    }
    
    private static final String helpMsg = 
    		"apk to dot 0.0.1\tHelp messages:\n"
    		+ "-f\ttransfor an apk file to dot file.\n"
    		+ "-d\ttransfor a directory of apk files to dot files.\n"
    		+ "-i\tthe input directory storing .apk files.\n"
    		+ "-o\tthe output directory to store .dot files.\n"
    		+ "-s\tthe android sdk directory, like \"E:\\android-sdk-windows\\platforms\".\n"
    		+ "-c\tthe call backs file. It can be an empty file but nessesary.\n"
    		+ "Example 1:java -jar apk2dot.jar -f -i chrome.apk -o outdir -s E:\\android-sdk-windows\\platforms -c cb.txt\n"
    		+ "Example 2:java -jar apk2dot.jar -d -i inputdir -o outdir -s E:\\android-sdk-windows\\platforms -c cb.txt\n";
}
