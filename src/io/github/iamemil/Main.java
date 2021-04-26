package io.github.iamemil;

import io.github.iamemil.RSA;

import java.math.BigInteger;
import java.util.Random;

public class Main {

    public static void main(String[] args) throws Exception {
	// write your code here
        RSA rsa = new RSA();
        //Random random = new Random();
        //BigInteger a = BigInteger.probablePrime(1000,random);
        //BigInteger b = BigInteger.probablePrime(1000,random);
        //BigInteger m = BigInteger.probablePrime(1000,random);
        //System.out.println(rsa.FastModExpo(a,b,m));
        System.out.println(rsa.FastModExpo(BigInteger.valueOf(6),BigInteger.valueOf(73),BigInteger.valueOf(100)));
        System.out.println(rsa.ExtEuclideanAlgo(BigInteger.valueOf(402),BigInteger.valueOf(123)));
    }
}
