/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testingwithhsqldb;

/**
 *
 * @author Dalfrak
 */
public class NegativePriceException extends Exception {

    public NegativePriceException(String msg) {
        super(msg);
    }
    
}
