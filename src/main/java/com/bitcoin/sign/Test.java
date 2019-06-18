package com.bitcoin.sign;

import com.bitcoin.sign.bitcoindrpc.Output;
import com.bitcoin.sign.common.BitcoinTools;
import org.bouncycastle.crypto.digests.RIPEMD160Digest;

import java.math.BigDecimal;
import java.security.MessageDigest;
import java.util.Arrays;


/**
 * Created by mayakui on 2018/2/11 0011.
 */
public class Test {

    public static void transaction(){
        Output output1 = new Output();
        output1.setOutputIndex(1);
        output1.setTransactionId("178de46a294edcc2ec565d347927d8770c60b20bc2ce6658912abe90e26067c4");
        output1.setScriptPubKey("76a914d9890ffa2f55b7e47a4e7575670772b89954402a88ac");
        //output1.setRedeemScript("5121033becd90f49301d02c40b9fa15f90e11d3306f0ae9b85d701a2fe2f7f3ffef1102102db4e8f154f530ff33a1950d479b970296926dada54721bb21ad9554fcbf4068e21031fca9a10bbdba6ebc4a05b88d9d41299615b2183cfc6d8b2fe9762698513fc7853ae");
        output1.setAddress("1LqDptecHpnS46BsAQbh965KZngz8mixLw");
        output1.setAmount(new BigDecimal("0.37766178"));

        Output[] outputs =  new Output[]{output1};

        BitcoinWallet wallet = new BitcoinWallet();
        //创建交易的unsignTranHex
        String rawTrx = "0100000001c46760e290be2a915866cec20bb2600c77d82779345d56ecc2dc4e296ae48d170100000000ffffffff0322020000000000001976a914b14ae193c5a1494211d1ab3510adcfd2aacfaa4188acf01a4002000000001976a914d9890ffa2f55b7e47a4e7575670772b89954402a88ac0000000000000000166a146f6d6e690000000000000003000000174876e80000000000";
        //私钥
        String privateKey = "L5WzNw8LSgtwr8p3TCdRb7HfAGJ1NH6V5KrXGvz9ycvBmm5PSfQ1";

        String address="1LqDptecHpnS46BsAQbh965KZngz8mixLw";

        String sign = wallet.signTransaction(address,rawTrx, privateKey,outputs,"BTC",(byte)0);
        System.out.println("签名结果：");
        System.out.println(sign);
    }


    public static void main(String[] args){
        transaction();
    }
}
