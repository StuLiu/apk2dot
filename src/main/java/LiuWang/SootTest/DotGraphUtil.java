package LiuWang.SootTest;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import soot.MethodOrMethodContext;
import soot.SootMethod;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.jimple.toolkits.callgraph.Targets;
import soot.util.dot.DotGraph;

/**
 * 
 * @author YunhaoLiu
 * @TODO FlowDroid获取APK的函数调用图
 * 我建立了一个工具类，其中的一个函数可以将call graph以dot的形式输出，同时，为了配合输出，我设立了两个静态变量，
 * 第一个一个是DotGraph类的对象，用它来存储构造好的dot图结构；
 * 第二个是一个map集合，key是一个表示结点名称的字符串，value代表这个结点是否已被访问过（true代表访问过，false代表没有被访问过）。
 * 在绘制call graph的函数中，传入参数有两个：一个CallGraph类对象和一个SootMethod类对象。
 * 第一个参数是解析过程完成后，我们通过soot提供的接口Scene.v().getCallGraph()获取到的CallGraph类对象，我们的目标就是根据这个CallGraph类对象提供的信息构造一个DotGraph；
 * 第二个参数是当前我们要处理的结点，其实就是源程序中的一个方法（在最开始调用该方法时，这个当前访问参数应该是整个应用的入口函数：DummyMain），在soot中使用SootMethod这个类来代表。
 * 整体来看，这个函数做了一件事：在一个CallGraph中访问其中的一个结点，然后递归的访问其前驱和后继。
 * 当我们访问CallGraph中的一个结点时，首先要获取其签名或者函数名称；
 * 然后将其加入之前提到过的那个静态map集合，表示该结点已经被访问过；
 * 然后调用DotGraph类的相关方法以这个SootMethod的信息（函数签名或者函数名）构造DotGraph结点。结点构造完之后，就该为结点添加相应的边。
 * 画边分为两个部分：一块是对当前访问的SootMethod的前驱结点，即它的调用者进行处理，另一块是对当前访问的SootMethod的后继结点，即它调用的SootMethod进行处理。
 * 这两块的处理策略并不相同，下面分开介绍。对于当前SootMethod前驱的处理策略是，获取到前驱（caller）方法集之后遍历他们，然后依次将它们作为参数之一递归调用本函数。
 * 对于当前SootMethod后继的处理策略是，获取到后继（callee）方法集之后遍历他们，构造从当前访问的SootMethod到该callee的边（使用DotGraph类中的方法即可），
 * 然后判断当前callee是否已被访问，若未被访问过则以其为参数递归调用本函数。
	--------------------- 
	作者：YunhaoLiu 
	来源：CSDN 
	原文：https://blog.csdn.net/liu1075538266/article/details/64446343 
	版权声明：本文为博主原创文章，转载请附上博文链接！
	--------------------- 
 * 上面的例子中有两个构造dot图的函数，第一个是将每个SootMethod的签名作为每一个dot图结点的表述的，一般的函数签名可能会比较长，这样dot图显示出来可能会比复杂，不好看，
 * 因此我又写了第二个函数，它可以只通过函数名称构造dot图，原理都一样，不赘述。这样可以缩减dot结点中的内容长度，使图的整体看起来更加简单易读。
 *
 */
public class DotGraphUtil {
	
	// a collection store the visited point in a graph when iterate the graph
	// map集合，key是一个表示结点名称的字符串，value代表这个结点是否已被访问过（true代表访问过，false代表没有被访问过）
	public static Map<String, Boolean> visitedNodes = new HashMap<String, Boolean>();
	
	// an DotGraph object，用它来存储构造好的dot图结构
	public static DotGraph dotGraph = new DotGraph("defult-dotgraph");
	
	public DotGraphUtil(){
		
	}
	
	/**
	 * output dot graph as a dot file
	 * @param fileName
	 * @param storeDir
	 * @param dotGraph
	 */
	public static void exportDotGraph(String fileName, String storeDir, DotGraph dotGraph){
		String outPath = storeDir + "/" + fileName + ".dot";
		dotGraph.plot(outPath);
	}
 
	/**
	 * represent call graph in the form of a dot graph
	 * @param callGraph: call graph of an APK
	 * @param curMethod: entry point of an APK (DummyMain)
	 */
	public static void CGToDotWithMethodsSignature(CallGraph callGraph, SootMethod curMethod){
		//if this is the first to call this function, method is start point
		String sigOfCurNode = curMethod.getSignature();
		//iterate the call graph starting from the startPoint, after visited a node, put it into the visitedNodes collection
		visitedNodes.put(sigOfCurNode, true);
		//draw the current node into call graph
		dotGraph.drawNode(sigOfCurNode);
		
		//after draw a node into datGraph, we can set some attribute for the node
		
		//get the callers of current node, that is to say, find out methods who have edge point into current method
		Iterator<MethodOrMethodContext> callersOfMethod = new Targets(callGraph.edgesInto(curMethod));
		if(callersOfMethod != null){
			while(callersOfMethod.hasNext()){
				SootMethod predecessor = (SootMethod) callersOfMethod.next();
				if(predecessor == null){
					System.out.println("This method is not existed!");
				}
				//if this predecessor of current node has not been visited, visit it! 
				if(!visitedNodes.containsKey(predecessor.getSignature())){
					CGToDotWithMethodsSignature(callGraph, predecessor);
				}
			}
		}
		
		//get the callees of current node, that is to say, get the methods called by curMethod
		Iterator<MethodOrMethodContext> calleesOfMethod = new Targets(callGraph.edgesOutOf(curMethod));
		if(calleesOfMethod != null){
			while(calleesOfMethod.hasNext()){
				SootMethod successor = (SootMethod) calleesOfMethod.next();
				if(successor == null){
					System.out.println("This method is not existed!");
				}
				//draw this callee into DotGraph
				dotGraph.drawNode(successor.getSignature());
				//add an edge from currentNode to this successor
				dotGraph.drawEdge(sigOfCurNode, successor.getSignature());
				//if this successor of currentNode has not been visited, visit it!
				if(!visitedNodes.containsKey(successor.getSignature())){
					CGToDotWithMethodsSignature(callGraph, successor);
				}
			}
		}
	}
	
	public static void CGToDotWithMethodsName(CallGraph callGraph, SootMethod curMethod){
		//if this is the first to call this function, method is start point
		String nameOfCurNode = curMethod.getName();
		//iterate the call graph starting from the startPoint, after visited a node, put it into the visitedNodes collection
		visitedNodes.put(nameOfCurNode, true);
		//draw the current node into call graph
		dotGraph.drawNode(nameOfCurNode);
		
		//after draw a node into datGraph, we can set some attribute for the node
		
		//get the callers of current node, that is to say, find out methods who have edge point into current method
		Iterator<MethodOrMethodContext> callersOfMethod = new Targets(callGraph.edgesInto(curMethod));
		if(callersOfMethod != null){
			while(callersOfMethod.hasNext()){
				SootMethod predecessor = (SootMethod) callersOfMethod.next();
				if(predecessor == null){
					System.out.println("This method is not existed!");
				}
				//if this predecessor of current node has not been visited, visit it! 
				if(!visitedNodes.containsKey(predecessor.getName())){
					CGToDotWithMethodsName(callGraph, predecessor);
				}
			}
		}
		
		//get the callees of current node, that is to say, get the methods called by curMethod
		Iterator<MethodOrMethodContext> calleesOfMethod = new Targets(callGraph.edgesOutOf(curMethod));
		if(calleesOfMethod != null){
			while(calleesOfMethod.hasNext()){
				SootMethod successor = (SootMethod) calleesOfMethod.next();
				if(successor == null){
					System.out.println("This method is not existed!");
				}
				//draw this callee into DotGraph
				dotGraph.drawNode(successor.getName());
				//add an edge from currentNode to this successor
				dotGraph.drawEdge(nameOfCurNode, successor.getName());
				//if this successor of currentNode has not been visited, visit it!
				if(!visitedNodes.containsKey(successor.getName())){
					CGToDotWithMethodsName(callGraph, successor);
				}
			}
		}
	}	
}
