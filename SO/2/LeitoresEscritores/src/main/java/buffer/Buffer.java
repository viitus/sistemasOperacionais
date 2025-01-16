/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package buffer;
import java.util.EmptyStackException;
/**
 *
 * @author andre
 */
public class Buffer {
    private final String[] data;

    private String cache;
    private int full;

    public Buffer(int size) {
        if (size <= 0) {
            throw new IllegalArgumentException("Size must be > 0");
        }
        this.data = new String[size];
    }

    public int getSize() { return data.length; }

    public int getFull() { return full; }

    public String[] getData() { return data; }

    public void push(String data) {
        if (full == this.data.length) {
            throw new StackOverflowError(); // Buffer is full of elements.
        }
        this.data[full++] = data;
        this.cache = data;
    }

    public void pop() {
        if (full == 0) {
            throw new EmptyStackException(); // Buffer is out of elements.
        }
        cache = data[--full];
        data[full] = null;
    }

    public String view() {
        StringBuilder str = new StringBuilder("| Buffer |> ");
        for (int i = 0; i < full; i++) {
            str.append(data[i]);
            if (i < full - 1) {
                str.append(" ");
            } else {
                str.append("\n");
            }
        }
        return str.toString();
    }

    public String getCache() { return cache; }
}
