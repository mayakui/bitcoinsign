package com.bitcoin.trx;

import com.bitcoin.common.Base58;
import com.bitcoin.common.ByteUtilities;
import com.bitcoin.common.crypto.Secp256k1;
import com.bitcoin.sign.common.BitcoinTools;
import com.bitcoin.sign.common.Constant;
import org.bitcoinj.core.ECKey;
import org.bouncycastle.crypto.digests.RIPEMD160Digest;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * Created by Administrator on 2019/1/28 0028.
 */
public class AddressTest {

    private static final String SHA256 = "SHA-256";

    private static byte[] getPublicKeyBytes(byte[] decodedPrivateKey) {
        try {
            //byte[] decodedPrivateKey = Base58.decode(privateKey);
            byte[] networkPrivateKeyBytes = new byte[decodedPrivateKey.length - 4];
            byte[] privateKeyChecksum = new byte[4];

            System.arraycopy(decodedPrivateKey, 0, networkPrivateKeyBytes, 0, decodedPrivateKey.length - 4);
            System.arraycopy(decodedPrivateKey, decodedPrivateKey.length - 4, privateKeyChecksum, 0, 4);

            // Is it valid?
            MessageDigest md = MessageDigest.getInstance(SHA256);
            md.update(networkPrivateKeyBytes);
            byte[] checksumCheck = Arrays.copyOfRange(md.digest(md.digest()), 0, 4);
            for (int i = 0; i < 4; i++) {
                if (privateKeyChecksum[i] != checksumCheck[i]) {
                    return new byte[0];
                }
            }

            // Strip leading network byte and get the public key
            byte[] privateKeyBytes =
                    Arrays.copyOfRange(networkPrivateKeyBytes, 1, 33);
            //byte[] privateKeyBytes =
            //        Arrays.copyOfRange(networkPrivateKeyBytes, 1, networkPrivateKeyBytes.length);
            return Secp256k1.getPublicKey(privateKeyBytes);

        } catch (Exception e) {
            return new byte[0];
        }
    }

    public static void main(String[] args){
        try {
            String priKeyHex = "412866be47955b663282251eafd3787f7066ea05217c50eae9711420cade5eab";

            ECKey eCkey = null;
            try {
                BigInteger priK = new BigInteger(priKeyHex, 16);
                eCkey = ECKey.fromPrivate(priK);
            } catch (Exception ex) {
                ex.printStackTrace();
                return;
            }

            byte[] publicKeyBytes = eCkey.getPubKey();
            int last = (int) publicKeyBytes[publicKeyBytes.length - 1];
            publicKeyBytes = Arrays.copyOfRange(publicKeyBytes, 1, 33);
            byte version ;
            if (last % 2 == 0) {
                version = 2;
            }else {
                version = 3;
            }

            byte[] publicKeyBytes2 = new byte[33];
            publicKeyBytes2[0] = version;
            System.arraycopy(publicKeyBytes, 0, publicKeyBytes2, 1, publicKeyBytes.length);

            MessageDigest md = MessageDigest.getInstance(SHA256);
            md.reset();
            md.update(publicKeyBytes2);
            byte[] publicShaKeyBytes = md.digest();

            RIPEMD160Digest ripemd = new RIPEMD160Digest();
            byte[] publicRipemdKeyBytes = new byte[20];
            ripemd.update(publicShaKeyBytes, 0, publicShaKeyBytes.length);
            ripemd.doFinal(publicRipemdKeyBytes, 0);

            //前面加入地址版本号（比特币主网版本号“0x00”）
            byte[] address = new byte[21];
            address[0] = 65;
            System.arraycopy(publicRipemdKeyBytes, 0, address, 1, publicRipemdKeyBytes.length);

            MessageDigest md1 = MessageDigest.getInstance(SHA256);
            md1.reset();
            md1.update(address);
            byte[] address256 = md1.digest();

            MessageDigest md2 = MessageDigest.getInstance(SHA256);
            md2.reset();
            md2.update(address256);
            byte[] address256256 = md2.digest();

            byte[] address25 = new byte[25];
            System.arraycopy(address, 0, address25, 0, address.length);
            System.arraycopy(address256256, 0, address25, address25.length - 4, 4);
            System.out.println(Base58.encode(address25));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
