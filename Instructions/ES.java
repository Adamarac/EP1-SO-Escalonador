package Instructions;

import Modules.Interpreter;

public class ES extends Instruction{
    @Override
    public void run(Interpreter interpreter) throws Exception {
        interpreter.getLogger().logES(interpreter.getRunningProcess().getPID());
        interpreter.incrementPC();
        throw new Instructions.Exceptions.EntradaSaidaException();
    }
}
