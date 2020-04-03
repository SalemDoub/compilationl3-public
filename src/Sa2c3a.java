import c3a.*;
import sa.*;
import ts.Ts;
import ts.TsItemVar;

public class Sa2c3a extends SaDepthFirstVisitor<C3aOperand> {
    private Ts globalTable;

    private C3a c3a;


    public Sa2c3a(SaNode root, Ts globalTable) {
        this.globalTable = globalTable;
        this.c3a = new C3a();
        root.accept(this);
    }



    public C3aOperand visit(SaProg node ){
        node.getFonctions().accept( this );
        return null;
    }


    public C3aOperand visit( SaAppel node ){
        if( node.getArguments() != null ) {
            SaLExp arg = node.getArguments();
            while (arg!=null) {
                C3aOperand param = arg.getTete().accept(this);
                c3a.ajouteInst(new C3aInstParam(param, ""));
                arg = arg.getQueue();
            }
        }
        return new C3aFunction( node.tsItem );
    }

    public C3aOperand visit( SaExpInt node ){
        C3aOperand op = new C3aConstant( node.getVal() );
        return op;
    }

    public C3aOperand visit ( SaExpAdd node ){
        C3aOperand op1 = node.getOp1().accept( this );
        C3aOperand op2 = node.getOp2().accept( this );
        C3aTemp tmp = c3a.newTemp();
        c3a.ajouteInst( new C3aInstAdd(op1, op2, tmp, "") );
        return tmp;
    }

    public C3aOperand visit ( SaExpDiv node ){
        C3aOperand op1 = node.getOp1().accept( this );
        C3aOperand op2 = node.getOp2().accept( this );
        C3aTemp tmp = c3a.newTemp();
        c3a.ajouteInst( new C3aInstDiv(op1, op2, tmp, "") );
        return tmp;
    }

    public C3aOperand visit ( SaExpMult node ){
        C3aOperand op1 = node.getOp1().accept( this );
        C3aOperand op2 = node.getOp2().accept( this );
        C3aTemp tmp = c3a.newTemp();
        c3a.ajouteInst( new C3aInstMult(op1, op2, tmp, "") );
        return tmp;
    }

    public C3aOperand visit ( SaExpSub node ){
        C3aOperand op1 = node.getOp1().accept( this );
        C3aOperand op2 = node.getOp2().accept( this );
        C3aTemp tmp = c3a.newTemp();
        c3a.ajouteInst( new C3aInstSub(op1, op2, tmp, "") );
        return tmp;
    }

    public C3aOperand visit ( SaExpAppel node ){
        C3aFunction op1 =(C3aFunction) node.getVal().accept( this );
        C3aTemp tmp = c3a.newTemp();
        c3a.ajouteInst( new C3aInstCall(op1, tmp, "") );
        return tmp;
    }

    public C3aOperand visit ( SaExpVar node ){
        return node.getVar().accept( this );
    }

    public C3aOperand visit ( SaExpLire node ){
        C3aTemp tmp = c3a.newTemp();
        c3a.ajouteInst( new C3aInstRead( tmp, "") );
        return tmp;
    }




    public C3aOperand visit ( SaExpAnd node ){
        C3aOperand t = c3a.newTemp();
        C3aLabel test= c3a.newAutoLabel();
        C3aLabel next = c3a.newAutoLabel();


        C3aOperand l = node.getOp1().accept( this );
        C3aOperand r = node.getOp2().accept( this );

        c3a.ajouteInst( new C3aInstJumpIfEqual( l, c3a.False, test, "" ));
        c3a.ajouteInst( new C3aInstJumpIfEqual( r, c3a.False, test, "" ));
        c3a.ajouteInst( new C3aInstAffect(c3a.True, t, "") );
        c3a.ajouteInst( new C3aInstJump(next, "") );
        c3a.addLabelToNextInst(test);
        c3a.ajouteInst( new C3aInstAffect(c3a.False, t, "") );
        c3a.addLabelToNextInst(next);
        return t;
    }
    public C3aOperand visit ( SaExpOr node ){
        C3aOperand t = c3a.newTemp();

        C3aLabel next = c3a.newAutoLabel();
        C3aLabel test = c3a.newAutoLabel();

        C3aOperand l= node.getOp1().accept( this );
        C3aOperand r = node.getOp2().accept( this );

        c3a.ajouteInst( new C3aInstJumpIfNotEqual( l, c3a.False, test, "" ));
        c3a.ajouteInst( new C3aInstJumpIfNotEqual( r, c3a.False, test, "" ));
        c3a.ajouteInst( new C3aInstAffect(c3a.False, t, "") );
        c3a.ajouteInst( new C3aInstJump(next, "") );
        c3a.addLabelToNextInst(test);
        c3a.ajouteInst( new C3aInstAffect(c3a.True, t, "") );
        c3a.addLabelToNextInst(next);
        return t;
    }

    public C3aOperand visit ( SaExpInf node ){
        C3aOperand t = c3a.newTemp();
        C3aLabel next = c3a.newAutoLabel();

        C3aOperand l = node.getOp1().accept( this );
        C3aOperand r = node.getOp2().accept( this );

        c3a.ajouteInst( new C3aInstAffect(c3a.True, t, "") );

        c3a.ajouteInst( new C3aInstJumpIfLess( l, r, next, "" ));

        c3a.ajouteInst( new C3aInstAffect(c3a.False, t, "") );

        c3a.addLabelToNextInst(next);
        return t;
    }


    public C3aOperand visit ( SaInstRetour node ){
        C3aOperand op = node.getVal().accept( this );
        c3a.ajouteInst( new C3aInstReturn(op, "") );
        return op;
    }

    public C3aOperand visit( SaInstEcriture node ){
        C3aOperand op = (C3aOperand) node.getArg().accept( this );
        c3a.ajouteInst( new C3aInstWrite( op, "" ) );
        return null;
    }

    public C3aOperand visit ( SaInstBloc node ){

        node.getVal().accept( this );
        return null;
    }


    public C3aOperand visit ( SaInstAffect node ){
        C3aVar l = (C3aVar) node.getLhs().accept( this );
        C3aOperand r = node.getRhs().accept( this );
        c3a.ajouteInst( new C3aInstAffect(r, l, "") );
        return l;
    }


    public C3aOperand visit ( SaInstSi node ){
        C3aLabel sinon_label = c3a.newAutoLabel();
        C3aLabel next = c3a.newAutoLabel();
        C3aOperand test = node.getTest().accept( this );

        if (node.getSinon() != null ) {
            c3a.ajouteInst( new C3aInstJumpIfEqual(test, c3a.False, sinon_label, "") );
            node.getAlors().accept( this );
            c3a.ajouteInst( new C3aInstJump(next, ""));

            c3a.addLabelToNextInst(sinon_label);
            node.getSinon().accept(this);
            c3a.addLabelToNextInst( next );
        }
        else{
            c3a.ajouteInst( new C3aInstJumpIfEqual(test, c3a.False, next, "") );
            node.getAlors().accept( this );
            c3a.addLabelToNextInst(next);
        }
        return null;
    }
    public C3aOperand visit ( SaInstTantQue node ){
        C3aOperand t= c3a.newTemp();
        C3aLabel revenir = c3a.newAutoLabel();
        C3aLabel next = c3a.newAutoLabel();
        C3aLabel test = c3a.newAutoLabel();


        c3a.addLabelToNextInst( revenir );
        c3a.ajouteInst( new C3aInstAffect(c3a.True, t, "") );
        C3aOperand test_gauche = ((SaExpInf) node.getTest()).getOp1().accept( this );
        C3aOperand test_droite= ((SaExpInf) node.getTest()).getOp2().accept( this );

        c3a.ajouteInst( new C3aInstJumpIfLess( test_gauche, test_droite, test, "") );
        c3a.ajouteInst( new C3aInstAffect(c3a.False, t, "") );

        c3a.addLabelToNextInst( test );
        c3a.ajouteInst( new C3aInstJumpIfEqual( t, c3a.False, next, ""));
        node.getFaire().accept( this );
        c3a.ajouteInst( new C3aInstJump( revenir, "") );

        c3a.addLabelToNextInst( next );
        return null;
    }



    public C3aOperand visit ( SaDecVar node ){
        TsItemVar tsItemVar = globalTable.getVar( node.getNom() );
        return new C3aVar(tsItemVar, null);
    }
    public C3aOperand visit ( SaDecTab node ){
        TsItemVar tsItemVar = globalTable.getVar( node.getNom() );
        C3aConstant length =  new C3aConstant( node.getTaille() );
        return new C3aVar(tsItemVar, length);
    }
    public C3aOperand visit( SaDecFonc node ){
        c3a.ajouteInst( new C3aInstFBegin(node.tsItem, "fonction"));
        node.getCorps().accept( this );
        c3a.ajouteInst( new C3aInstFEnd(""));
        return null;
    }

    public C3aOperand visit( SaVarSimple node ){
        TsItemVar tsItemVar = node.tsItem;
        C3aVar var =  new C3aVar(tsItemVar, null);
        return var;
    }
    public C3aOperand visit( SaVarIndicee node ){
        TsItemVar tsItemVar = node.tsItem;
        C3aOperand indice = node.getIndice().accept( this );
        return new C3aVar(tsItemVar, indice);
    }
    public C3a getC3a() { return c3a; }
}