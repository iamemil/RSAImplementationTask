package io.github.iamemil;

import java.math.*;
import java.util.*;

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

    public List<BigInteger> ExtEuclideanAlgo(BigInteger a, BigInteger b) throws Exception{
        List<BigInteger> result = new ArrayList<BigInteger>();
        result.add(b);
        result.add(BigInteger.ZERO);
        result.add(BigInteger.ONE);
        if(a==null){
            throw new Exception("a should be present");
        }
        if(b==null){
            throw new Exception("b should be present");
        }
        if(a.equals(BigInteger.valueOf(0))){
            return result;
        }
        result.clear();

        BigInteger xPrev = BigInteger.valueOf(1);
        BigInteger yPrev = BigInteger.valueOf(0);
        BigInteger xCurr = BigInteger.valueOf(0);
        BigInteger yCurr = BigInteger.valueOf(1);

        while(!b.equals(BigInteger.valueOf(0))){
            BigInteger bTmp = a.divide(b);
            BigInteger newB = a.mod(b);
            a=b;
            b=newB;

            BigInteger xNewCoeff = xPrev.subtract(bTmp.multiply(xCurr));
            BigInteger yNewCoeff = yPrev.subtract(bTmp.multiply(yCurr));

            xPrev = xCurr;
            yPrev = yCurr;
            xCurr = xNewCoeff;
            yCurr = yNewCoeff;
        }

        result.add(a);
        result.add(xPrev);
        result.add(yPrev);
        return result;
    }

    public boolean MillerRabinPrimalityTest(BigInteger p, BigInteger a) throws Exception {
        if(p.compareTo(BigInteger.valueOf(3))<0){
            throw new Exception("p should be greater than 3");
        }
        if(p.mod(BigInteger.valueOf(2)).equals(BigInteger.ZERO)){
            throw new Exception("p should be odd");
        }
        if(!ExtEuclideanAlgo(p,a).get(0).equals(BigInteger.valueOf(1)))
        {
            throw new Exception("gcd("+p+","+a+") should be 1");
        }
        //BigInteger pTemp = p;
        //p=p.subtract(BigInteger.ONE);
        return true;
    }
}
