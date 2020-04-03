import c3a.*;
import nasm.*;
import ts.Ts;
import ts.TsItemFct;
import ts.TsItemVar;

public class C3a2nasm implements C3aVisitor<NasmOperand> {
    private Nasm nasm;
    private TsItemFct currentFct;
    private Ts tableGlobale;

    public C3a2nasm(C3a c3a, Ts tableGlobale) {
        nasm = new Nasm(tableGlobale);
        nasm.setTempCounter(c3a.getTempCounter());
        NasmLabel lMain = new NasmLabel("Main");
        nasm.ajouteInst(new NasmCall(null, lMain, ""));
        NasmRegister reg_eax = nasm.newRegister();
        reg_eax.colorRegister(Nasm.REG_EAX);
        NasmRegister reg_ebx = nasm.newRegister();
        reg_ebx.colorRegister(Nasm.REG_EBX);
        nasm.ajouteInst(new NasmMov(null, reg_ebx, new NasmConstant(0), " vrp"));
        nasm.ajouteInst(new NasmMov(null, reg_eax, new NasmConstant(1), ""));
        nasm.ajouteInst(new NasmInt(null, ""));
        for (C3aInst inst : c3a.listeInst) {
            inst.accept(this);
        }
    }

    public Nasm getNasm() {
        return nasm;
    }

    @Override
    public NasmOperand visit(C3aInstAdd inst) {
        NasmOperand label1 = (inst.label != null) ? inst.label.accept(this) : null;
        NasmOperand op1 = inst.op1.accept(this);
        NasmOperand op2 = inst.op2.accept(this);
        NasmOperand result = inst.result.accept(this);
        nasm.ajouteInst(new NasmMov(label1, result, op1, ""));
        nasm.ajouteInst(new NasmAdd(null, result, op2, ""));
        return null;
    }

    @Override
    public NasmOperand visit(C3aInstCall inst) {
        NasmRegister reg_esp = new NasmRegister(Nasm.REG_ESP);
        reg_esp.colorRegister(Nasm.REG_ESP);
        NasmLabel Fname = new NasmLabel(inst.op1.toString());
        nasm.ajouteInst(new NasmSub(null, reg_esp, new NasmConstant(4), "mémoire allouée pour la valeur de result"));
        nasm.ajouteInst(new NasmCall(null, Fname, ""));
        nasm.ajouteInst(new NasmPop(null, inst.result.accept(this), " retour valeur de result "));
        if (inst.op1.val.nbArgs > 0) {
            nasm.ajouteInst(new NasmAdd(null, reg_esp, new NasmConstant(inst.op1.val.nbArgs  * 4), "args désalloués"));
        }
        return null;
    }

    @Override
    public NasmOperand visit(C3aInstFBegin inst) {
        currentFct = inst.val;
        currentFct.nbArgs = inst.val.nbArgs;
        System.out.println(currentFct.getNbArgs());
        NasmRegister reg_ebp = new NasmRegister(Nasm.REG_EBP);
        reg_ebp.colorRegister(Nasm.REG_EBP);
        NasmRegister reg_esp = new NasmRegister(Nasm.REG_ESP);
        reg_esp.colorRegister(Nasm.REG_ESP);
        NasmLabel labelMain = new NasmLabel(currentFct.getIdentif());
        nasm.ajouteInst(new NasmPush(labelMain, reg_ebp, "sauvegarde  ebp"));
        nasm.ajouteInst(new NasmMov(null, reg_ebp, reg_esp, "nouvelle valeur ebp"));
        nasm.ajouteInst(new NasmSub(null, reg_esp, new NasmConstant(currentFct.getTable().nbVar() * 4), "alloc  variables locales"));
        return null;
    }

    @Override
    public NasmOperand visit(C3aInst inst) {
        return null;
    }

    @Override
    public NasmOperand visit(C3aInstJumpIfLess inst) {
        NasmOperand label1 = (inst.label != null) ? inst.label.accept(this) : null;
        NasmOperand gotoLabel1 = inst.result.accept(this);
        nasm.ajouteInst(new NasmCmp(label1, inst.op1.accept(this), inst.op2.accept(this), "JumpIfLess 1"));
        nasm.ajouteInst(new NasmJl(null, gotoLabel1, "JumpIfLess 2"));
        return null;
    }

    @Override
    public NasmOperand visit(C3aInstMult inst) {
        NasmOperand label1 = (inst.label != null) ? inst.label.accept(this) : null;
        NasmOperand result = inst.result.accept(this);
        nasm.ajouteInst(new NasmMov(label1, result, inst.op1.accept(this), ""));
        nasm.ajouteInst(new NasmMul(null, result, inst.op2.accept(this), ""));
        return null;
    }

    @Override
    public NasmOperand visit(C3aInstRead inst) {
        return null;
    }

    @Override
    public NasmOperand visit(C3aInstSub inst) {
        NasmOperand label1 = (inst.label != null) ? inst.label.accept(this) : null;
        NasmOperand result = inst.result.accept(this);
        nasm.ajouteInst(new NasmMov(label1, result, inst.op1.accept(this), ""));
        nasm.ajouteInst(new NasmSub(null, result, inst.op2.accept(this), ""));
        return result;
    }

    @Override
    public NasmOperand visit(C3aInstAffect inst) {
        NasmOperand label1 = (inst.label != null) ? inst.label.accept(this) : null;
        nasm.ajouteInst(new NasmMov(label1, inst.result.accept(this), inst.op1.accept(this), "Affect"));
        return null;
    }

    @Override
    public NasmOperand visit(C3aInstDiv inst) {
        NasmOperand label1 = (inst.label != null) ? inst.label.accept(this) : null;
        NasmOperand result = inst.result.accept(this);
        NasmRegister reg_eax = nasm.newRegister();
        reg_eax.colorRegister(Nasm.REG_EAX);
        nasm.ajouteInst(new NasmMov(label1, reg_eax, inst.op1.accept(this), ""));
        NasmRegister dest= nasm.newRegister();
        nasm.ajouteInst(new NasmMov(null, dest, inst.op2.accept(this), ""));
        nasm.ajouteInst(new NasmDiv(null, dest, ""));
        nasm.ajouteInst(new NasmMov(null, result, reg_eax, ""));
        return result;
    }

    @Override
    public NasmOperand visit(C3aInstFEnd inst) {
        NasmOperand label1 = (inst.label != null) ? inst.label.accept(this) : null;
        NasmRegister reg_esp = new NasmRegister(Nasm.REG_ESP);
        reg_esp.colorRegister(Nasm.REG_ESP);
        NasmRegister reg_ebp = new NasmRegister(Nasm.REG_EBP);
        reg_ebp.colorRegister(Nasm.REG_EBP);

        nasm.ajouteInst(new NasmAdd(label1, reg_esp, new NasmConstant(currentFct.getTable().nbVar() * 4), "désalloc  variables locales"));
        nasm.ajouteInst(new NasmPop(null, reg_ebp, "restaure  ebp"));
        nasm.ajouteInst(new NasmRet(null, ""));
        return null;
    }

    @Override
    public NasmOperand visit(C3aInstJumpIfEqual inst) {
        NasmOperand label1 = (inst.label != null) ? inst.label.accept(this) : null;
        NasmOperand gotoLabel1 = inst.result.accept(this);
        nasm.ajouteInst(new NasmCmp(label1, inst.op1.accept(this), inst.op2.accept(this), "JumpIfEqual 1"));
        nasm.ajouteInst(new NasmJe(null, gotoLabel1, "JumpIfEqual 2"));
        return null;
    }

    @Override
    public NasmOperand visit(C3aInstJumpIfNotEqual inst) {
        NasmOperand label1 = (inst.label != null) ? inst.label.accept(this) : null;
        NasmOperand gotoLabel1 = inst.result.accept(this);
        nasm.ajouteInst(new NasmCmp(label1, inst.op1.accept(this), inst.op2.accept(this), "jumpIfNotEqual 1"));
        nasm.ajouteInst(new NasmJne(null, gotoLabel1, "jumpIfNotEqual 2"));
        return null;
    }

    @Override
    public NasmOperand visit(C3aInstJump inst) {
        NasmOperand label1 = (inst.label != null) ? inst.label.accept(this) : null;
        nasm.ajouteInst(new NasmJmp(label1, inst.result.accept(this), "Jump"));
        return null;
    }

    @Override
    public NasmOperand visit(C3aInstParam inst) {
        nasm.ajouteInst(new NasmPush(null, inst.op1.accept(this), "Param"));
        return null;
    }

    @Override
    public NasmOperand visit(C3aInstReturn inst) {
        NasmOperand label = (inst.label != null) ? inst.label.accept(this) : null;
        NasmRegister reg_ebp = new NasmRegister(Nasm.REG_EBP);
        reg_ebp.colorRegister(Nasm.REG_EBP);
        NasmRegister reg_esp = new NasmRegister(Nasm.REG_ESP);
        reg_esp.colorRegister(Nasm.REG_ESP);
//
        nasm.ajouteInst(new NasmMov(label, new NasmAddress(reg_ebp, '+', new NasmConstant(2)), inst.op1.accept(this), "valeur de retour"));
        return null;
    }

    @Override
    public NasmOperand visit(C3aInstWrite inst) {
        NasmOperand label = (inst.label != null) ? inst.label.accept(this) : null;
        NasmRegister reg_eax = nasm.newRegister();
        reg_eax.colorRegister(Nasm.REG_EAX);
        NasmLabel labelIprintLF = new NasmLabel("iprintLF");
        nasm.ajouteInst(new NasmMov(label, reg_eax, inst.op1.accept(this), "ecrire 1"));
        nasm.ajouteInst(new NasmCall(null, labelIprintLF, "ecrire 2"));
        return null;
    }

    @Override
    public NasmOperand visit(C3aConstant op) {
        return new NasmConstant(op.val);
    }

    @Override
    public NasmOperand visit(C3aLabel op) {
        return new NasmLabel(op.toString());
    }

    @Override
    public NasmOperand visit(C3aTemp op) {
        return new NasmRegister(op.num);
    }

    @Override
    public NasmOperand visit(C3aVar op) {
        TsItemVar variable = op.item;
        NasmRegister reg_ebp = new NasmRegister(Nasm.REG_EBP);
        reg_ebp.colorRegister(Nasm.REG_EBP);
        if (variable.isParam) {
            //argument
            return new NasmAddress(reg_ebp, '+', new NasmConstant(2 + variable.portee.nbArg() - variable.adresse));
        }
        if (op.index != null) {
            //tableau
            return new NasmAddress(new NasmLabel(variable.getIdentif()), '+', op.index.accept(this));
        }
        if (currentFct.getTable().variables.containsKey(variable.identif)) {
            //variable locale
            return new NasmAddress(reg_ebp, '-', new NasmConstant(1 + variable.adresse));
        }
        //identifiant
        return new NasmAddress(new NasmLabel(variable.identif));
    }

    @Override
    public NasmOperand visit(C3aFunction op) {
        return null;
    }
}