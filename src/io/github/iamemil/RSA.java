package io.github.iamemil;

import java.lang.reflect.Array;
import java.math.*;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class RSA {
    private int bitSize = 1025;
    private BigInteger p;
    private BigInteger q;

    private BigInteger privateKey;
    public BigInteger[] publicKey = new BigInteger[2];

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
        while(MultiMillerRabinPrimalityTest(newNum,10)==false){
            newNum = getRandomBigInt(numofBits);
        }
        return newNum;
    }

    public BigInteger FastModExpo(BigInteger a, BigInteger b, BigInteger m){
        if(b.compareTo(BigInteger.valueOf(1))<0){
            return BigInteger.valueOf(-1);
        }
        String temp = new StringBuilder(b.toString(2)).reverse().toString();
        BigInteger result = a.mod(m);
        Map<BigInteger,Character> res = new HashMap<BigInteger, Character>();
        //System.out.println(temp);
        for (int i=0;i<temp.length();i++) {
            res.put(result,temp.charAt(i));
            result = result.multiply(result).mod(m);
        }
        result = BigInteger.ONE;
        for (Map.Entry<BigInteger, Character> entry: res.entrySet()){
            if(entry.getValue()=='1'){
                result = result.multiply(entry.getKey().mod(m));
            }
        }
        res.clear();
        return result.mod(m);
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
        if(FastModExpo(a.modPow(BigInteger.valueOf(2),p),d,p).modPow(d,p).equals(BigInteger.ONE)){
            return true;
        }else{
            for (int i=1; i<S;i++){
               if(FastModExpo(a.modPow(BigInteger.valueOf(2).pow(i),p),d,p).equals(p.subtract(BigInteger.ONE))){
                    return true;
                }
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

    public BigInteger getRandomBigIntInRange(int numofBits,BigInteger min, BigInteger max){

        BigInteger x = getRandomBigInt(numofBits);
        while(!(x.compareTo(min)==1 && x.compareTo(max)==-1)){
            x = getRandomBigInt(numofBits);
        }
        return x;
    }

    public void GenerateKeys(){
        this.p = getRandomPrimeBigInt(bitSize);
        this.q = getRandomPrimeBigInt(bitSize);
        //p=BigInteger.valueOf(23);
        //q = BigInteger.valueOf(11);
        //System.out.println("p: "+p+"\nq: "+ q);
        BigInteger n = p.multiply(q);
        //System.out.println("n: "+n);
        BigInteger phin = (p.subtract(BigInteger.ONE)).multiply(q.subtract(BigInteger.ONE));

        BigInteger e = getRandomBigIntInRange(bitSize,BigInteger.ONE,phin);
        while(!(ExtEuclideanAlgo(e,phin).get(0).equals(BigInteger.valueOf(1)))){
            e = getRandomBigIntInRange(bitSize,BigInteger.ONE,phin);
        }
        e=BigInteger.valueOf(7);
        //System.out.println("e: "+ e);
        this.privateKey = ExtEuclideanAlgo(phin,e).get(2).mod(phin);
        this.publicKey[0] = n;
        this.publicKey[1] = e;
    }

    public BigInteger Encrypt(BigInteger data){
        return FastModExpo(data,this.publicKey[1],this.publicKey[0]);
    }

    public BigInteger DecryptUsingFME(BigInteger data){
        return FastModExpo(data,this.getPrivateKey(),this.getPublicKey()[0]);
    }

    public BigInteger DecryptUsingCRT(BigInteger data){
        BigInteger dp = this.privateKey.mod(p.subtract(BigInteger.ONE));
        //System.out.println("dp: "+ dp);
        BigInteger dq = this.privateKey.mod(q.subtract(BigInteger.ONE));
        //System.out.println("dq: "+ dq);

        BigInteger mp = data.modPow(dp,p);
        //System.out.println("mp: "+ mp);
        BigInteger mq = data.modPow(dq,q);
        //System.out.println("mq: "+ mq);
        List<BigInteger> euclidean = ExtEuclideanAlgo(p,q);
        //System.out.println(euclidean);
        //System.out.println(euclidean.get(2));

        BigInteger m = ((mp.multiply(euclidean.get(2)).multiply(this.q)).add((mq.multiply(euclidean.get(1)).multiply(this.p)))).mod(this.publicKey[0]);

        return m;
    }



}
