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
public class Reader extends Process {
    public Reader(String name) {
        super(name);
    }

    @Override
    public void performAction(Buffer b) {
         b.pop();
    }
}
