package Instructions;

import Modules.Interpreter;
import Instructions.Exceptions.SaidaException;

public class SAIDA extends Instruction {
    @Override
    public void run(Interpreter interpreter) throws Exception {
        throw new SaidaException();
    }
}
