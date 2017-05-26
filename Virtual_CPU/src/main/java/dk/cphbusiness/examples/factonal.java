/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.cphbusiness.examples;

import java.math.BigInteger;

/**
 *
 * @author Emil
 */
public class factonal {
    public static BigInteger factonal(BigInteger i){
        if(i.intValue()<=1)return i;
        else return i.add(factonal(BigInteger.valueOf(i.intValue() -2)).add(factonal(i.subtract(BigInteger.valueOf(i.intValue() -1)))));
    }
    public static void main(String[] args) {
        System.out.println(factonal(BigInteger.valueOf(100000)));
    }
}
