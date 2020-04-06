package ig;

import fg.*;
import nasm.*;
import util.graph.*;
import util.intset.*;
import java.util.*;
import java.io.*;

public class Ig {
    public Graph graph;
    public FgSolution fgs;
    public int regNb;
    public Nasm nasm;
    public Node int2Node[];
	private int[] pre_colors;
	private ColorGraph colorGraph;
    
    public Ig(FgSolution fgs){
	this.fgs = fgs;
 	this.graph = new Graph();
	this.nasm = fgs.nasm;
	this.regNb = this.nasm.getTempCounter();
	this.int2Node = new Node[regNb];
	this.construction();
		pre_colors = getPrecoloredTemporaries();
		colorGraph = new ColorGraph(graph, regNb, pre_colors);
		allocateRegisters();
    }

    public void construction(){
		for( int i=0 ; i<regNb ; i++ )
			int2Node[i] = graph.newNode();
		for( NasmInst inst : nasm.listeInst ){
			IntSet inInst  = fgs.in.get(  inst );
			IntSet outInst = fgs.out.get( inst );
			ConstruireIntSet( inInst  );
			ConstruireIntSet( outInst );
		}

    }
	private void ConstruireIntSet(IntSet set ) {
		if (set.getSize() <= 1) return;
		for (int reg = 0; reg < regNb; reg++) {
			if (set.isMember(reg))
				for (int reg1 = reg + 1; reg1 < regNb; reg1++)
					if (set.isMember(reg1))
						graph.addNOEdge(int2Node[reg], int2Node[reg1]);
		}
	}


    public int[] getPrecoloredTemporaries()
    {
		int[] preColoredTemperaries= new int[this.regNb];
		for ( NasmInst inst : nasm.listeInst ){
			if( inst.source instanceof NasmRegister )
				traiterNRegister( (NasmRegister) inst.source , preColoredTemperaries);
			if( inst.destination instanceof NasmRegister )
				traiterNRegister( (NasmRegister) inst.destination , preColoredTemperaries);
		}
		return preColoredTemperaries;
    }

	private void traiterNRegister(NasmRegister register, int[] colors ){
		if( register.isGeneralRegister() )
			if( register.color != Nasm.REG_UNK )
				colors[register.val] = register.color;
	}

    public void allocateRegisters(){
		int[] colors  = colorGraph.couleur;
		for( NasmInst inst : nasm.listeInst ){
			if( inst.source instanceof NasmRegister )
				allocateColorRegister( (NasmRegister) inst.source     , colors);
			if( inst.destination instanceof NasmRegister )
				allocateColorRegister( (NasmRegister) inst.destination, colors);

		}
    }

	private void allocateColorRegister(NasmRegister register, int[] colors){
		if( register!=null && register.isGeneralRegister() )
			if( register.color == Nasm.REG_UNK )
				register.colorRegister( colors[register.val] );
	}


    public void affiche(String baseFileName){
	String fileName;
	PrintStream out = System.out;
	
	if (baseFileName != null){
	    try {
		baseFileName = baseFileName;
		fileName = baseFileName + ".ig";
		out = new PrintStream(fileName);
	    }
	    
	    catch (IOException e) {
		System.err.println("Error: " + e.getMessage());
	    }
	}
	
	for(int i = 0; i < regNb; i++){
	    Node n = this.int2Node[i];
	    out.print(n + " : ( ");
	    for(NodeList q=n.succ(); q!=null; q=q.tail) {
		out.print(q.head.toString());
		out.print(" ");
	    }
	    out.println(")");
	}
    }
}
	    
    

    
    
