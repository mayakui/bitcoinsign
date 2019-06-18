package com.bitcoin;

import com.bitcoin.common.Base58;
import javassist.bytecode.ByteArray;
import org.bitcoinj.core.Sha256Hash;
import org.spongycastle.util.encoders.Hex;

/**
 * Created by Administrator on 2019/1/21 0021.
 */
public class Base58Test {

    private static byte[] decode58Check(String input) throws Exception {
        byte[] decodeCheck = Base58.decode(input);
        if (decodeCheck.length <= 4) {
            return null;
        }
        byte[] decodeData = new byte[decodeCheck.length - 4];
        System.arraycopy(decodeCheck, 0, decodeData, 0, decodeData.length);
        byte[] hash0 = Sha256Hash.hash(decodeData);
        byte[] hash1 = Sha256Hash.hash(hash0);
        if (hash1[0] == decodeCheck[decodeData.length] &&
                hash1[1] == decodeCheck[decodeData.length + 1] &&
                hash1[2] == decodeCheck[decodeData.length + 2] &&
                hash1[3] == decodeCheck[decodeData.length + 3]) {
            return decodeData;
        }
        return null;
    }


    public static void main(String[] args) throws Exception {
        System.out.println(Hex.toHexString(decode58Check("THpGKz3PTy3c6gF1A4gsgnZCz5ecs8VHym")));
    }

}
