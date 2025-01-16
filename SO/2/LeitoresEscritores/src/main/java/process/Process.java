/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package process;
import buffer.Buffer;
import enums.*;
import funcoes.RandomNumber;

/**
 *
 * @author andre
 */
public abstract class Process {
    private static int processingMax = 10;

    private final String name;
    private Situation situation; // 'W' for Wakeup or 'S' Sleep.
    private State state; // 'E' for Executing or 'D' for Done (waiting).
    protected int processing;

    public Process(String name) {
        this.name = name;
        this.situation = Situation.WAKEUP;
        this.state = State.DONE;
        this.setProcessing();
    }

    public static void setProcessingMax(int processingMax) {
        Process.processingMax = processingMax;
    }

    public void setProcessing() {
        this.processing = RandomNumber.between(1, Process.processingMax);
    }

    public String getName() { return name; }
    public int getProcessing() { return processing; }

    public void sleep() {
        this.situation = Situation.SLEEP;
    }

    public void wakeUp() { this.situation = Situation.WAKEUP; }

    public void setExecuting() {
        if (this.state != State.EXECUTING) {
            this.state = State.EXECUTING;
        }
    }

    public void setDone() {
        if (this.state != State.DONE) {
            this.state = State.DONE;
        }
    }

    public boolean isSleeping() { return this.situation == Situation.SLEEP; }
    public void decreaseProcessing() {
        if (this.processing > 0) {
            this.processing--;
        } else {
            throw new IllegalStateException("Processing is already zero.");
        }
    }

    public abstract void performAction(Buffer b);

    @Override
    public String toString() {
        String[] typeArray = this.getClass().getName().split("\\.");
        String typeName = typeArray[typeArray.length - 1];
        return String.format("| %-6s | %-11s |  | %-10s |  | %-10s |  | %-10s |", this.name, typeName,
                this.situation.name(), this.state.name(), this.processing);
    }
}