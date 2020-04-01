
import sc.analysis.DepthFirstAdapter;
import sc.node.*;
import sa.*;

public class Sc2sa extends DepthFirstAdapter {
    private SaNode returnValue;

    public void caseStart(Start node) {
        super.caseStart(node);
    }

    public SaNode getRoot() {
        return returnValue;
    }


    public void caseAdecvarldecfoncProgramme(ADecvarldecfoncProgramme node){
        SaLDec var;
        SaLDec func;
        node.getOptdecvar().apply(this);
        var = (SaLDec) this.returnValue;
        node.getListedecfonc().apply(this);
        func = (SaLDec) this.returnValue;
        this.returnValue = new SaProg(var, func);
    }

    public void caseADecOptdecvar(ADecvarListedecvar node) {
        SaLDec listedecvar;
        node.getDecvar().apply(this);
        listedecvar = (SaLDec) this.returnValue;
        this.returnValue = listedecvar;
    }

    public void caseOptdecvarVide(ADecvarListedecvar node){

        this.returnValue = null;
    }

    public void caseAdecvarldecvar(ADecvarListedecvarbis node){
        SaDec decvar;
        SaLDec listedecvarbis;
        node.getDecvar().apply(this);
        decvar=(SaDec) this.returnValue;
        node.getDecvar().apply(this);
        listedecvarbis=(SaLDec)this.returnValue;


        this.returnValue=new SaLDec(decvar,listedecvarbis);
    }

    public void caseAlistedecvarbis(ADecvarListedecvarbis node){
        SaLDec listedecvarbis;
        node.getDecvar().apply(this);
        listedecvarbis=(SaLDec)this.returnValue;
        this.returnValue= listedecvarbis;
    }

    public void caseAdecVarEntierDecvar(ADecvarentierDecvar node ){

        String variable;
        node.getIdentif().apply(this);
        variable = String.valueOf(node.getIdentif()).trim();
        returnValue = new SaDecVar(variable);
    }


    public void caseAldecfonc(ALdecfoncProgramme node){
        SaDec func;
        SaLDec list;
        node.getListedecfonc().apply(this);
        func = (SaDecFonc) this.returnValue;
        node.getListedecfonc().apply(this);
        list = (SaLDec) this.returnValue;
        this.returnValue = new SaLDec(func, list);
    }

    public void caseAdecfoncfinal(ALdecfoncfinalListedecfonc node){
        this.returnValue=null;
    }


    public void caseAdecfonc(ADecvarinstrDecfonc node){

       String identif;
        SaLDec listeparam;
        SaLDec Optdecvar;
        SaInst instrbloc;
        node.getOptdecvar().apply(this);
        Optdecvar = (SaLDec) this.returnValue;
       node.getIdentif().apply(this);
       identif=this.returnValue.toString();
        node.getInstrbloc().apply(this);
        instrbloc= (SaInst) this.returnValue;
        node.getListeparam().apply(this);
        listeparam=(SaLDec)this.returnValue;
        this.returnValue = new SaDecFonc(identif, listeparam, Optdecvar, instrbloc);
    }

    public void caseAInstraffect(AInstraffect node){
        SaVar s;
        SaExp exp ;
        node.getVar().apply(this);
        s = (SaVar) this.returnValue;
        node.getExp().apply(this);
        exp = (SaExp) this.returnValue;
        this.returnValue = new SaInstAffect(s, exp);
    }


    public void caseAinstrbloc(AInstrbloc node) {
        SaLInst listeInstr;
        node.getListeinst().apply(this);
        listeInstr = (SaLInst) this.returnValue;
        this.returnValue = new SaInstBloc(listeInstr);
    }


    public void caseAinstrsi(AInstrsiInstr node){

        SaExp si;
        SaInst alors;
        SaInst sinon;
        node.getInstrsi().apply(this);
        si = (SaExp) this.returnValue;
        node.getInstrsi().apply(this);
        alors = (SaInst) this.returnValue;
        node.getInstrsi().apply(this);
        sinon = (SaInst) this.returnValue;
        this.returnValue = new SaInstSi(si, alors, sinon);
    }


    public void caseAinstrsinon(AInstrsinon node) { node.getInstrbloc().apply(this); }




    public void caseAinstrtantque(AInstrtantque node){
        SaExp condition ;
        SaInst instructions;
            node.getExp().apply(this);
             condition = (SaExp) this.returnValue;
            node.getInstrbloc().apply(this);
            instructions= (SaInst) this.returnValue;
            this.returnValue = new SaInstTantQue(condition, instructions);
        }

    public void caseAappelfonct(AAppelfct node){
        String id;
        SaLExp params;
             id = node.getIdentif().getText();
            node.getListeexp().apply(this);
            params = (SaLExp) this.returnValue;
            this.returnValue = new SaAppel(id, params);
        }

     public void caseAinstrretour(AInstrretour node){
         SaExp value;
        node.getExp().apply(this);
          value = (SaExp) this.returnValue;
         this.returnValue = new SaInstRetour(value);

    }


    public void caseAinstrecriture(AInstrecriture node){

        node.getExp().apply(this);
        SaExp ch = (SaExp) this.returnValue;
        this.returnValue = new SaInstEcriture(ch);
    }


    public void caseAinstrvide(AInstrvide node){
        this.returnValue=null ;
    }


    public void caseAExpouExp1(AOuExp node){

        SaExp op1;
        SaExp op2;
        node.getExp().apply(this);
        op1 = (SaExp) this.returnValue;
        node.getOu().apply(this);
        node.getExp1().apply(this);
        op2 = (SaExp) this.returnValue;
        this.returnValue = new SaExpOr(op1, op2);
    }

    public void caseAExp1EtExp2(AEtExp1 node){

        SaExp op1;
        SaExp op2;
        node.getExp1().apply(this);
        op1 = (SaExp) this.returnValue;
        node.getEt().apply(this);
        node.getExp2().apply(this);
        op2 = (SaExp) this.returnValue;
        this.returnValue = new SaExpAnd(op1, op2);
    }


    public void caseAExp2infExp3(AInfExp2 node){

        SaExp op1;
        SaExp op2;
        node.getExp2().apply(this);
        op1 = (SaExp) this.returnValue;
        node.getInferieur().apply(this);
        node.getExp3().apply(this);
        op2 = (SaExp) this.returnValue;
        this.returnValue = new SaExpInf(op1, op2);
    }

    public void caseAExp2egalExp3(AEgalExp2 node){
        SaExp op1;
        SaExp op2;
        node.getExp2().apply(this);
        op1 = (SaExp) this.returnValue;
        node.getEgal().apply(this);
        node.getExp3().apply(this);
        op2 = (SaExp) this.returnValue;
        this.returnValue = new SaExpEqual(op1, op2);
    }

    public void caseAExp3(AExp3Exp2 node){
            SaExp op;
            node.getExp3().apply(this);
            op=(SaExp)this.returnValue;
    }


    public void caseAExp3plusExp4(APlusExp3 node){
        SaExp op1;
        SaExp op2;
        node.getExp3().apply(this);
        op1 = (SaExp) this.returnValue;
        node.getPlus().apply(this);
        node.getExp4().apply(this);
        op2 = (SaExp) this.returnValue;
        this.returnValue = new SaExpAdd(op1, op2);

    }
    public void caseAExp3moinsExp4(AMoinsExp3 node){
        SaExp op1;
        SaExp op2;
        node.getExp3().apply(this);
        op1 = (SaExp) this.returnValue;
        node.getMoins().apply(this);
        node.getExp4().apply(this);
        op2 = (SaExp) this.returnValue;
        this.returnValue = new SaExpSub(op1, op2);

    }


    public void caseAExp4(AExp4Exp3 node){
        SaExp op;
        node.getExp4().apply(this);
        op=(SaExp)this.returnValue;
    }

    public void caseAExp4multExp5(AFoisExp4 node){
        SaExp op1;
        SaExp op2;
        node.getExp4().apply(this);
        op1 = (SaExp) this.returnValue;
        node.getFois().apply(this);
        node.getExp5().apply(this);
        op2 = (SaExp) this.returnValue;
        this.returnValue = new SaExpMult(op1, op2);
    }


    public void caseAExp4DiviseExp5(ADiviseExp4 node) {
        SaExp op1;
        SaExp op2;
        node.getExp4().apply(this);
        op1 = (SaExp) this.returnValue;
        node.getDivise().apply(this);
        node.getExp5().apply(this);
        op2 = (SaExp) this.returnValue;
        this.returnValue = new SaExpDiv(op1, op2);
    }

    public void caseAExp5(AExp5Exp4 node){
        SaExp op;
        node.getExp5().apply(this);
        op=(SaExp)this.returnValue;
    }


    public void caseANotExp5(ANonExp5 node) {
        SaExp op;
        node.getExp5().apply(this);
        op = (SaExp) this.returnValue;
        node.getNon().apply(this);
        this.returnValue = new SaExpNot(op);
    }

    public void caseAExp6 (AExp6Exp5 node){
        SaExp op;
        node.getExp6().apply(this);
        op=(SaExp)this.returnValue;
    }


    public void caseANumberExp6(ANombreExp6 node){
        SaExp op ;
        node.getNombre().apply(this);
        op=(SaExp)this.returnValue;
    }


    public void caseAappelfoncExp6(AAppelfctExp6 node){
        SaExp op;
        node.getAppelfct().apply(this);
        op=(SaExp)this.returnValue;
    }

    public void caseAVariableExp6(AVarExp6 node){
        SaExp op ;
        node.getVar().apply(this);
        op=(SaExp)this.returnValue;
    }

    public void caseAParenthesesExp6(AParenthesesExp6 node){
        SaExp o;
        SaExp f ;
        SaExp a ;
        node.getParentheseOuvrante().apply(this);
        o=(SaExp)this.returnValue;
        node.getExp().apply(this);
        a=(SaExp)this.returnValue;
        node.getParentheseFermante().apply(this);
        f=(SaExp)this.returnValue;

    }

    public void caseALireExp6(ALireExp6 node){
        this.returnValue = new SaExpLire();
    }


    public void caseAvartab(AVartabVar node){
        String name;

        node.getIdentif().apply(this);
        name= String.valueOf(node.getIdentif()).trim();
        node.getCrochetOuvrant().apply(this);
       SaExp o;
       node.getExp().apply(this);
       o=(SaExp)this.returnValue;
       node.getCrochetFermant().apply(this);
        returnValue = new SaVarIndicee(name,o);
    }


    public void caseAVarsimpleExp6(AVarExp6 node) {
        SaVar var;
        node.getVar().apply(this);
        var = (SaVar) this.returnValue;
        this.returnValue = new SaExpVar(var);
    }


    public void caseAListeexpbis(ADecvarListedecvarbis node) {
        SaInst instr;
        SaLInst listeInstr;
        node.getDecvar().apply(this);
        instr = (SaInst) this.returnValue;
        node.getDecvar().apply(this);
        listeInstr = (SaLInst) this.returnValue;
        this.returnValue = new SaLInst(instr, listeInstr);
    }


    public void caseAVideListInstr(ALinstfinalListeinst node) {
        this.returnValue = null;
    }


}






