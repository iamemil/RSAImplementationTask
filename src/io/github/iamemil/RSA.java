package io.github.iamemil;

import java.lang.reflect.Array;
import java.math.*;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class RSA {
    private final int bitSize =1024;
    private static BigInteger p;
    private static BigInteger q;

    private static BigInteger privateKey;
    public static BigInteger[] publicKey = new BigInteger[2];

    public RSA(){
        GenerateKeys();
    }

    public BigInteger getPrivateKey(){
        return this.privateKey;
    }

    public BigInteger[] getPublicKey(){
        return this.publicKey;
    }

    public BigInteger getRandomBigInt(int numofBits){
        Random random = new Random();
        return new BigInteger(numofBits,random);
    }

    public BigInteger getRandomPrimeBigInt(int numofBits){

        BigInteger newNum = getRandomBigInt(numofBits);
        while(MultiMillerRabinPrimalityTest(newNum,3)==false){
            newNum = getRandomBigInt(numofBits);
        }
        return newNum;
    }

    public BigInteger getRandomBigIntInRange(int numofBits,BigInteger min, BigInteger max){
        BigInteger x = getRandomBigInt(numofBits);
        while(!(x.compareTo(min)==1 && x.compareTo(max)==-1)){
            x = getRandomBigInt(numofBits);
        }
        return x;
    }

    public BigInteger FastModExpo(BigInteger a, BigInteger b, BigInteger m){
        if(b.compareTo(BigInteger.valueOf(1))<0){
            // Error return -1
            return BigInteger.valueOf(-1);
        }
        // Convert b to binary
        String temp = new StringBuilder(b.toString(2)).reverse().toString();
        BigInteger result = a;
        Map<BigInteger,Character> res = new HashMap<>();
        for (int i=0;i<temp.length();i++) {
            res.put(result,temp.charAt(i));
            result = result.multiply(result).mod(m);
        }
        result = BigInteger.ONE;
        for (Map.Entry<BigInteger, Character> entry: res.entrySet()){
            if(entry.getValue()=='1'){
                result = result.multiply(entry.getKey()).mod(m);
            }
        }
        res.clear();
        return result;
    }

    public List<BigInteger> ExtEuclideanAlgo(BigInteger a, BigInteger b){
        List<BigInteger> result = new ArrayList<BigInteger>();
        result.add(b);
        result.add(BigInteger.ZERO);
        result.add(BigInteger.ONE);
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


    public boolean SingleMillerRabinPrimalityTest(BigInteger p, BigInteger a) {

        if(p.mod(BigInteger.valueOf(2)).equals(BigInteger.ZERO)){
            return false;
        }
        if(!(ExtEuclideanAlgo(p,a).get(0).equals(BigInteger.valueOf(1))))
        {
            return false;
        }
        BigInteger pTemp = p.subtract(BigInteger.ONE);
        boolean doWhile=true;
        int iterations=1;
        BigInteger d = BigInteger.ONE;
        Integer S=0;
        while(doWhile){
            d = pTemp.divideAndRemainder(BigInteger.valueOf(2))[0];
            S = iterations;
            pTemp = pTemp.divide(BigInteger.valueOf(2));
            iterations++;
            if (pTemp.mod(BigInteger.valueOf(2))!=BigInteger.ZERO){
                doWhile=false;
            }
        }
        //if(FastModExpo(a.modPow(BigInteger.valueOf(1),p),d,p).modPow(d,p).equals(BigInteger.ONE)){
        //    return true;
        if(FastModExpo(a,d,p).equals(BigInteger.ONE)){
            return true;
        }else{
            for (int i=1; i<S;i++){
               if(FastModExpo(FastModExpo(a,BigInteger.valueOf(2).pow(i),p),d,p).equals(p.subtract(BigInteger.ONE))){
                    return true;
                }
                //if(FastModExpo(a.modPow(BigInteger.valueOf(2).pow(i),p),d,p).equals(p.subtract(BigInteger.ONE))){
                //    return true;
                //}
            }
            return false;
        }
    }

    public boolean MultiMillerRabinPrimalityTest(BigInteger p, int round) {
        for (int i=0;i<round;i++){
            BigInteger a = getRandomBigInt(p.bitLength());
            while(!(a.compareTo(BigInteger.valueOf(2))>=0 && a.compareTo(p)==-1)){
                a = getRandomBigInt(p.bitLength());
            }
            if(SingleMillerRabinPrimalityTest(p,a)==false){
                return false;
            }
        }
        return true;
    }

    public void GenerateKeys(){
        this.p = getRandomPrimeBigInt(bitSize);
        this.q = getRandomPrimeBigInt(bitSize);
        BigInteger n = p.multiply(q);
        BigInteger phin = (p.subtract(BigInteger.ONE)).multiply(q.subtract(BigInteger.ONE));

        BigInteger e = getRandomBigIntInRange(bitSize,BigInteger.ONE,phin);
        while(!(ExtEuclideanAlgo(e,phin).get(0).equals(BigInteger.valueOf(1)))){
            e = getRandomBigIntInRange(bitSize,BigInteger.ONE,phin);
        }
        this.privateKey = ExtEuclideanAlgo(phin,e).get(2).mod(phin);
        this.publicKey[0] = n;
        this.publicKey[1] = e;
    }

    public BigInteger Encrypt(BigInteger data){
        return FastModExpo(data,this.publicKey[1],this.publicKey[0]);
    }

    public List<BigInteger> EncryptString(String message){
        List<BigInteger> encryptedMessage = new ArrayList<BigInteger>();
        for (int i =0; i<message.length();i++){
            BigInteger messageChunk = Encrypt(BigInteger.valueOf(message.charAt(i)));
            //BigInteger messageChunk = FastModExpo(BigInteger.valueOf(message.charAt(i)),this.publicKey[1],this.publicKey[0]);
            encryptedMessage.add(messageChunk);
        }
        return encryptedMessage;
    }

    public BigInteger DecryptUsingFME(BigInteger data){
        return FastModExpo(data,this.getPrivateKey(),this.getPublicKey()[0]);
    }

    public String DecryptStringUsingFME(List<BigInteger> encryptedMessage){
       StringBuilder stringBuilder = new StringBuilder();

       for (int i=0; i< encryptedMessage.size();i++){
           BigInteger msg = DecryptUsingFME(encryptedMessage.get(i));
           stringBuilder.append((char)msg.intValue());
       }
       return stringBuilder.toString();
    }

    public BigInteger DecryptUsingCRT(BigInteger data){
        return ChineseRemTheory(data);
    }

    public BigInteger ChineseRemTheory(BigInteger c){
        BigInteger dp = this.privateKey.mod(p.subtract(BigInteger.ONE));
        BigInteger dq = this.privateKey.mod(q.subtract(BigInteger.ONE));

        BigInteger mp = FastModExpo(c,dp,p);
        BigInteger mq = FastModExpo(c,dq,q);
        List<BigInteger> euclidean = ExtEuclideanAlgo(p,q);
        return ((mp.multiply(euclidean.get(2)).multiply(this.q)).add((mq.multiply(euclidean.get(1)).multiply(this.p)))).mod(this.publicKey[0]);
    }


    public String DecryptStringUsingCRT(List<BigInteger> encryptedMessage){
        StringBuilder stringBuilder = new StringBuilder();

        for (int i=0; i< encryptedMessage.size();i++){
            BigInteger msg = DecryptUsingCRT(encryptedMessage.get(i));
            stringBuilder.append((char)msg.intValue());
        }
        return stringBuilder.toString();
    }



}
