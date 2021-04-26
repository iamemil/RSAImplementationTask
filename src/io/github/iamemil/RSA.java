package io.github.iamemil;

import java.math.BigInteger;
import java.util.BitSet;

public class RSA {


    public BigInteger FastModExpo(BigInteger a, BigInteger b, BigInteger m) throws Exception{
        if(a==null){
            throw new Exception("a should be present");
        }
        if(b==null){
            throw new Exception("b should be present");
        }
        if(m==null){
            throw new Exception("m should be present");
        }
        if(b.compareTo(BigInteger.valueOf(1))<0){
            throw new Exception("b should be bigger than 1");
        }
        String temp = b.toString(2);
        BigInteger result = BigInteger.ONE;
        for (int i=0;i<temp.length();i++) {
            if(temp.charAt(i)=='1'){
                result = result.multiply((a.pow(2).pow(i)).mod(m));
            }
        }
        return result.mod(m);
    }
}
