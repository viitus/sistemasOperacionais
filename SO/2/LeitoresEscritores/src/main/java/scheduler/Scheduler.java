/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package scheduler;
import buffer.Buffer;
import cpu.CPU;
import enums.Semaphore;
import process.Process;
import process.Reader;
import process.Writer;

/**
 *
 * @author andre
 */
public class Scheduler {
    private final CPU cpu;

    private final Writer writer;

    private final Reader reader;
    private final Buffer buffer;

    private int full, empty, mutex;

    {
        this.cpu = new CPU();
        this.mutex = 1;
    }

    public Scheduler(Buffer b, Writer w, Reader r) {
        this.buffer = b;
        this.writer = w;
        this.reader = r;
        this.empty = b.getSize() - b.getFull();
    }

    public void schedule(int times) {
        Process currentProcess = this.writer;
        this.displayView();
        this.cpu.setProcess(this.writer);
        this.cpu.setBuffer(this.buffer);
        this.displayView();
        for (int i = 0; i < times; i++) {
            while (currentProcess.getProcessing() > 0) {
                if (!this.handle(currentProcess)) {
                    break;
                }
            }
            currentProcess.setDone();
            currentProcess.setProcessing();
            currentProcess = this.nextProcess(currentProcess);
            this.cpu.setProcess(currentProcess);
        }
    }

    public boolean handle(Process p) {
        boolean isWriter = p instanceof Writer, isReader = p instanceof Reader;
        p.setExecuting();
        Semaphore sDown, sUp;
        if (isWriter) {
            if (this.empty == 0) {
                p.sleep();
                this.displayView();
                return false;
            }
            sDown = Semaphore.EMPTY;
            sUp = Semaphore.FULL;
        } else if (isReader) {
            if (this.full == 0) {
                p.sleep();
                this.displayView();
                return false;
            }
            sDown = Semaphore.FULL;
            sUp = Semaphore.EMPTY;
        } else {
            throw new IllegalArgumentException("Invalid argument p");
        }
        this.down(sDown);
        this.down(Semaphore.MUTEX);
        this.cpu.process();
        this.displayView();
        this.up(Semaphore.MUTEX);
        this.up(sUp);
        return true;
    }

    public Process nextProcess(Process current) {
        if (current instanceof Writer) {
            return this.reader;
        } else if (current instanceof Reader) {
            return this.writer;
        } else {
            throw new IllegalArgumentException("Current is of an invalid type.");
        }
    }

    public String view() {
        StringBuilder builder = new StringBuilder();
        String bufferData, processStr = "";
        String format = String.format("%-11s  %-24s  %-14s  %-14s  %-14s\n", "-".repeat(11),
                "-".repeat(24), "-".repeat(14), "-".repeat(14), "-".repeat(14));
        builder.append(format);
        builder.append(String.format("| %-7s |  | %-20s |  | %-10s |  | %-10s |  | %-10s |\n", "Buffer", "Processes",
                "Situation", "State", "Processing"));
        builder.append(format);
        int iterationLength = Math.max(buffer.getSize(), 6);
        String s = String.format("%-14s %-10s %-14s  %-14s  %-14s", "-".repeat(14), "",
                "-".repeat(14), "-".repeat(14), "-".repeat(14));
        for (int i = 0; i < iterationLength; i++) {
            if (i < this.buffer.getSize()) {
                String temp = this.buffer.getData()[i];
                bufferData = String.format("| %-7s |", temp == null ? "" : temp);
            } else if (i == this.buffer.getSize()) {
                bufferData = String.format("%-11s", "-".repeat(11));
            } else {
                bufferData = String.format("%-11s", "");
            }
            if (i == 0) {
                processStr = this.writer.toString();
            } else if (i == 1) {
                processStr = this.reader.toString();
            } else if (i == 2) {
                processStr = String.format("%-24s  %-14s  %-14s  %-14s", "-".repeat(24), "-".repeat(14),
                        "-".repeat(14), "-".repeat(14));
            } else if (i == 3) {
                processStr = String.format("| %-10s | %-10s | %-10s |  | %-10s |  | %-10s |", "Full", "", "Empty",
                        "Mutex", "CPU");
            } else if (i == 4) {
                processStr = s;;
            }else if (i == 5) {
                String cpuProcessName = this.cpu.getProcess() == null ? "Empty" : this.cpu.getProcess().getName();
                processStr = String.format("| %-10d | %-10s | %-10d |  | %-10d |  | %-10s |", this.full, "", this.empty
                        ,this.mutex, cpuProcessName);
            } else if (i == 6) {
                processStr = s;
            } else {
                processStr = "";
            }
            builder.append(String.format("%s  %s\n", bufferData, processStr));
        }
        String bufferEnd = this.buffer.getSize() >= 6 ? "-".repeat(11) : "";
        String counterCpuEnd = this.buffer.getSize() >= 7 ? "" : s;
        builder.append(String.format("%-11s  %s\n", bufferEnd, counterCpuEnd));
        return builder.toString();
    }

    public void displayView() {
        System.out.println(this.view());
    }

    public void down(Semaphore s) {
        switch (s) {
            case FULL -> this.full--;
            case EMPTY -> this.empty--;
            case MUTEX -> this.mutex--;
        }
        this.displayView();
    }

    public void up(Semaphore s) {
        switch (s) {
            case FULL -> {
                this.full++;
                if (this.reader.isSleeping()) {
                    this.reader.wakeUp();
                }
            }
            case EMPTY -> {
                this.empty++;
                if (this.writer.isSleeping()) {
                    this.writer.wakeUp();
                }
            }
            case MUTEX -> this.mutex++;
        }
        this.displayView();
    }
}