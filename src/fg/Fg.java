package fg;
import nasm.*;
import util.graph.*;
import java.util.*;
import java.io.*;

public class Fg implements NasmVisitor <Void> {
    public Nasm nasm;
    public Graph graph;
    Map< NasmInst, Node> inst2Node;
    Map< Node, NasmInst> node2Inst;
    Map< String, NasmInst> label2Inst;

    public Fg(Nasm nasm){
	this.nasm = nasm;
	this.inst2Node = new HashMap< NasmInst, Node>();
	this.node2Inst = new HashMap< Node, NasmInst>();
	this.label2Inst = new HashMap< String, NasmInst>();
	this.graph = new Graph();
    }

    public void affiche(String baseFileName){
	String fileName;
	PrintStream out = System.out;

	if (baseFileName != null){
	    try {
		baseFileName = baseFileName;
		fileName = baseFileName + ".fg";
		out = new PrintStream(fileName);
	    }
	    
	    catch (IOException e) {
		System.err.println("Error: " + e.getMessage());
	    }
	}
	
	for(NasmInst nasmInst : nasm.listeInst){
	    Node n = this.inst2Node.get(nasmInst);
	    out.print(n + " : ( ");
	    for(NodeList q=n.succ(); q!=null; q=q.tail) {
		out.print(q.head.toString());
		out.print(" ");
	    }
	    out.println(")\t" + nasmInst);
	}
    }
    
    public Void visit(NasmAdd inst){
        Node node = graph.newNode();
        label2Inst.put(inst.label.toString(),inst);
        inst2Node.put(inst,node);
        node2Inst.put(node,inst);

        return null;
    }
    public Void visit(NasmCall inst){
        Node node = graph.newNode();
        label2Inst.put(inst.label.toString(),inst);
        inst2Node.put(inst,node);
        node2Inst.put(node,inst);

        return null;
    }
    public Void visit(NasmDiv inst){
        Node node = graph.newNode();
        label2Inst.put(inst.label.toString(),inst);
        inst2Node.put(inst,node);
        node2Inst.put(node,inst);

        return null;
    }
    public Void visit(NasmJe inst){
        Node node = graph.newNode();
        label2Inst.put(inst.label.toString(),inst);
        inst2Node.put(inst,node);
        node2Inst.put(node,inst);

        return null;
    }
    public Void visit(NasmJle inst){
        Node node = graph.newNode();
        label2Inst.put(inst.label.toString(),inst);
        inst2Node.put(inst,node);
        node2Inst.put(node,inst);

        return null;
    }
    public Void visit(NasmJne inst){
        Node node = graph.newNode();
        label2Inst.put(inst.label.toString(),inst);
        inst2Node.put(inst,node);
        node2Inst.put(node,inst);

        return null;
    }
    public Void visit(NasmMul inst){
        Node node = graph.newNode();
        label2Inst.put(inst.label.toString(),inst);
        inst2Node.put(inst,node);
        node2Inst.put(node,inst);

        return null;
    }
    public Void visit(NasmOr inst){
        Node node = graph.newNode();
        label2Inst.put(inst.label.toString(),inst);
        inst2Node.put(inst,node);
        node2Inst.put(node,inst);

        return null;
    }
    public Void visit(NasmCmp inst){
        Node node = graph.newNode();
        label2Inst.put(inst.label.toString(),inst);
        inst2Node.put(inst,node);
        node2Inst.put(node,inst);

        return null;
    }
    public Void visit(NasmInst inst){
        Node node = graph.newNode();
        label2Inst.put(inst.label.toString(),inst);
        inst2Node.put(inst,node);
        node2Inst.put(node,inst);

        return null;
    }
    public Void visit(NasmJge inst){
        Node node = graph.newNode();
        label2Inst.put(inst.label.toString(),inst);
        inst2Node.put(inst,node);
        node2Inst.put(node,inst);

        return null;
    }
    public Void visit(NasmJl inst){
        Node node = graph.newNode();
        label2Inst.put(inst.label.toString(),inst);
        inst2Node.put(inst,node);
        node2Inst.put(node,inst);

        return null;
    }
    public Void visit(NasmNot inst){
        Node node = graph.newNode();
        label2Inst.put(inst.label.toString(),inst);
        inst2Node.put(inst,node);
        node2Inst.put(node,inst);

        return null;
    }
    public Void visit(NasmPop inst){
        Node node = graph.newNode();
        label2Inst.put(inst.label.toString(),inst);
        inst2Node.put(inst,node);
        node2Inst.put(node,inst);

        return null;
    }
    public Void visit(NasmRet inst){
        Node node = graph.newNode();
        label2Inst.put(inst.label.toString(),inst);
        inst2Node.put(inst,node);
        node2Inst.put(node,inst);

        return null;
    }
    public Void visit(NasmXor inst){

        Node node = graph.newNode();
        label2Inst.put(inst.label.toString(),inst);
        inst2Node.put(inst,node);
        node2Inst.put(node,inst);

        return null;
    }
    public Void visit(NasmAnd inst){
        Node node = graph.newNode();
        label2Inst.put(inst.label.toString(),inst);
        inst2Node.put(inst,node);
        node2Inst.put(node,inst);

        return null;
    }
    public Void visit(NasmJg inst){
        Node node = graph.newNode();
        label2Inst.put(inst.label.toString(),inst);
        inst2Node.put(inst,node);
        node2Inst.put(node,inst);

        return null;
    }
    public Void visit(NasmJmp inst){
        Node node = graph.newNode();
        label2Inst.put(inst.label.toString(),inst);
        inst2Node.put(inst,node);
        node2Inst.put(node,inst);

        return null;
    }
    public Void visit(NasmMov inst){
        Node node = graph.newNode();
        label2Inst.put(inst.label.toString(),inst);
        inst2Node.put(inst,node);
        node2Inst.put(node,inst);

        return null;
    }
    public Void visit(NasmPush inst){
        Node node = graph.newNode();
        label2Inst.put(inst.label.toString(),inst);
        inst2Node.put(inst,node);
        node2Inst.put(node,inst);

        return null;
    }
    public Void visit(NasmSub inst){
        Node node = graph.newNode();
        label2Inst.put(inst.label.toString(),inst);
        inst2Node.put(inst,node);
        node2Inst.put(node,inst);

        return null;
    }
    public Void visit(NasmEmpty inst){
        Node node = graph.newNode();
        label2Inst.put(inst.label.toString(),inst);
        inst2Node.put(inst,node);
        node2Inst.put(node,inst);

        return null;
    }

    public Void visit(NasmAddress operand){
        return null;
    }
    public Void visit(NasmConstant operand){
        return null;
    }
    public Void visit(NasmLabel operand){
        return null;
    }
    public Void visit(NasmRegister operand){
        return null;
    }



}
