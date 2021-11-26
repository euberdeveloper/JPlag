package de.jplag;

/**
 * @author Emeric Kwemou
 * @date 22.01.2005
 */
public abstract class AbstractParser {
    protected ErrorConsumer program;
    protected int errors = 0;
    private int errorsSum = 0;

    public boolean hasErrors() {
        return errors != 0;
    }

    public int errorsCount() {
        return errorsSum;
    }

    protected void parseEnd() {
        errorsSum += errors;
    }

    public ErrorConsumer getProgram() {
        return program;
    }

    public void setProgram(ErrorConsumer prog) {
        this.program = prog;
    }
}