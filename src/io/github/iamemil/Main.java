package io.github.iamemil;

import io.github.iamemil.RSA;

import java.math.BigInteger;
import java.util.Random;

public class Main {

    public static void main(String[] args) throws Exception {
	// write your code here
        RSA rsa = new RSA();
        Random random = new Random();
        BigInteger a = rsa.getRandomBigInt(512);
        BigInteger b = BigInteger.probablePrime(512,random);
        BigInteger m = rsa.getRandomBigInt(512);
        //System.out.println("a: "+a);
        //System.out.println("b: "+b);
        //System.out.println("\n\n\n\n");
        //System.out.println("m: "+m);
        //System.out.println("FastModExpo(a,b,m): "+rsa.FastModExpo(a,b,m));
        //System.out.println("ExtEuclideanAlgo(a,b) => ((a,b),x,y): "+rsa.ExtEuclideanAlgo(a,b));
        //System.out.println("SingleMillerRabinPrimalityTest(a,200) => (false-composite, true- possibly prime): "+rsa.SingleMillerRabinPrimalityTest(b,BigInteger.valueOf(200)));
        //System.out.println("MultiMillerRabinPrimalityTest(a,5) => (false-composite, true- possibly prime): "+rsa.MultiMillerRabinPrimalityTest(b,10));
        //System.out.println(rsa.getRandomPrimeBigInt(100));
        System.out.println("Private key: "+ rsa.getPrivateKey());
        System.out.println("Public key: ["+rsa.getPublicKey()[0]+", "+rsa.getPublicKey()[1]+"]");
        System.out.println(10+" is encrypted as "+rsa.Encrypt(BigInteger.valueOf(10)));
        System.out.println(rsa.Encrypt(BigInteger.valueOf(10))+" is decrypted as "+rsa.DecryptUsingFME(rsa.Encrypt(BigInteger.valueOf(10))));
        System.out.println(rsa.DecryptUsingCRT(rsa.Encrypt(BigInteger.valueOf(10))));
        //System.out.println(rsa.FastModExpo(BigInteger.valueOf(911),BigInteger.valueOf(541),BigInteger.valueOf(691)));
        //System.out.println(rsa.ExtEuclideanAlgo(BigInteger.valueOf(37),BigInteger.valueOf(27)));
        //System.out.println(rsa.MillerRabinPrimalityTest(BigInteger.valueOf(37),BigInteger.valueOf(27)));

    }
}
