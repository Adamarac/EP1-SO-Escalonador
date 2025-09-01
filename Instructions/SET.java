package Instructions;

import Modules.Interpreter;

public class SET extends Instruction{
    public String operand;
    public int value;

    public SET(String operand, int value) {
        this.operand = operand.trim();
        this.value = value;
    }

    @Override
    public void run(Interpreter interpreter) {
        if (operand.equals("X")) {
            interpreter.setX(value);
        } else if (operand.equals("Y")) {
            interpreter.setY(value);
        } else {
            throw new IllegalArgumentException("Operando desconhecido: " + operand);
        }
    }
}
