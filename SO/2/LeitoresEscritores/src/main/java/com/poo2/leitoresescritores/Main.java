/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.poo2.leitoresescritores;
import buffer.Buffer;
import process.Process;
import process.Reader;
import process.Writer;
import scheduler.Scheduler;

/**
 *
 * @author andre
 */
public class Main {
    public static void main(String[] args) {
        final int BUFFER_SIZE = 3;
        Buffer b = new Buffer(BUFFER_SIZE);
        Process.setProcessingMax(BUFFER_SIZE);
        Writer w = new Writer("w1");
        Reader r = new Reader("r1");
        Scheduler s = new Scheduler(b, w, r);
        s.schedule(10);
    }
}
