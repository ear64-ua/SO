package model;

public class ProcessNotFound extends Exception {
    private Process process;

    /**
     * Constructor of exception
     * @param process
     */
    public ProcessNotFound(Process process) {
            super();
            this.process = process;
    }

    /**
     * Getter of the message caused by the process
     * @return a message
     */
    public String getMessage() {
        return "ERROR: process " + process + " not found";
    }
}
