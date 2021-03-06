package io.github.iamemil;

import io.github.iamemil.RSA;

import java.math.BigInteger;
import java.util.*;

public class Main {

    public static void main(String[] args) {
        RSA rsa = new RSA();
        //Random random = new Random();
        //BigInteger a = rsa.getRandomBigInt(512);
        BigInteger b = rsa.getRandomBigInt(1024);
        //BigInteger m = rsa.getRandomBigInt(512);
        //System.out.println("a: "+a);
        //System.out.println("b: "+b);
        //System.out.println("\n\n\n\n");
        //System.out.println("m: "+m);
        //System.out.println("FastModExpo(a,b,m): "+rsa.FastModExpo(a,b,m));
        //System.out.println("ExtEuclideanAlgo(a,b) => ((a,b),x,y): "+rsa.ExtEuclideanAlgo(BigInteger.valueOf(12),BigInteger.valueOf(125)));
        //System.out.println("SingleMillerRabinPrimalityTest(a,200) => (false-composite, true- possibly prime): "+rsa.SingleMillerRabinPrimalityTest(b,BigInteger.valueOf(200)));
        //System.out.println("MultiMillerRabinPrimalityTest(a,5) => (false-composite, true- possibly prime): "+rsa.MultiMillerRabinPrimalityTest(b,10));
        //System.out.println(rsa.getRandomPrimeBigInt(100));
        System.out.println("Private key: "+ rsa.getPrivateKey());
        System.out.println("Public key: ["+rsa.getPublicKey()[0]+", "+rsa.getPublicKey()[1]+"]");
//        System.out.println("==============================");
//        BigInteger test = b;
//        BigInteger enResult =rsa.Encrypt(test);
//        BigInteger fmeResult = rsa.DecryptUsingFME(enResult);
//        BigInteger crtResult = rsa.DecryptUsingCRT(enResult);
//            System.out.println(test+" is encrypted as "+rsa.Encrypt(test));
//            System.out.println(enResult+" is decrypted as "+fmeResult);
//            System.out.println(test.equals(fmeResult));
//            System.out.println(enResult+ " is decrypted using CRT as "+crtResult);
//            System.out.println(test.equals(crtResult));
System.out.println("==============================");
        String testmsg = "Emil Ismayilzada";
        List<BigInteger> testMsgResult =rsa.EncryptString(testmsg);
        String fmeMsgResult = rsa.DecryptStringUsingFME(testMsgResult);
        String crtMsgResult = rsa.DecryptStringUsingCRT(testMsgResult);
        System.out.println(testmsg+"\nis encrypted as\n"+testMsgResult);
        System.out.println("==============================");
        System.out.println(testMsgResult+"\nis decrypted as\n"+fmeMsgResult);
        System.out.println("==============================");
        System.out.println(testMsgResult+ "\nis decrypted using CRT as\n"+crtMsgResult);
        System.out.println("==============================");

    }
}
