/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package process;
import buffer.Buffer;
/**
 *
 * @author andre
 */
public class Writer extends Process {
    private int i;

    public Writer(String name) {
        super(name);
        this.i = 1;
    }

    @Override
    public void performAction(Buffer b) {
        String data = getName() + "(" + i + ")";
        b.push(data);
        if (i == 10) {
            i = 0;
        } else {
            i++;
        }
    }
}
