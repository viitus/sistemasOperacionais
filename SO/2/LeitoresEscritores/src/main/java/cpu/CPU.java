/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cpu;
import buffer.Buffer;
import process.Process;
/**
 *
 * @author andre
 */
public class CPU {
    private Process p;

    private Buffer b;

    public Process getProcess() { return p; }

    public void setProcess(Process p) {
        this.p = p;
    }

    public Buffer getBuffer() { return b; }

    public void setBuffer(Buffer b) { this.b = b; }

    public void process() {
        if (this.p == null) {
            throw new IllegalStateException("The underlying process must be not null.");
        }
        if (this.b == null) {
            throw new IllegalStateException("The underlying buffer must be not null.");
        }
        this.p.performAction(b);
        this.p.decreaseProcessing();
    }
}
