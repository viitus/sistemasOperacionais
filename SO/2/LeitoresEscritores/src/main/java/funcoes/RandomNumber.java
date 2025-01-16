/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package funcoes;

/**
 *
 * @author andre
 */
public class RandomNumber {
    public static int between(int a, int b) {
        if (b < a) {
            throw new IllegalArgumentException("b must be greater than or equal to a.");
        }
        return (int) (a + (Math.random() * (b - a)));
    }
}
