package com.bitcoin.address;

import com.bitcoin.common.ByteUtilities;
import com.google.common.collect.ImmutableList;
import com.google.common.primitives.Bytes;
import org.bitcoinj.core.Base58;
import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.Sha256Hash;
import org.bitcoinj.crypto.ChildNumber;
import org.bitcoinj.crypto.DeterministicHierarchy;
import org.bitcoinj.crypto.DeterministicKey;
import org.bitcoinj.crypto.HDKeyDerivation;
import org.bitcoinj.wallet.DeterministicSeed;
import org.bitcoinj.wallet.UnreadableWalletException;
import org.spongycastle.crypto.digests.RIPEMD160Digest;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * Created by Administrator on 2018/4/20 0020.
 */
public class LtcAddressUtil {

    private static final String MASTER_KEY_PASSPHRASE = "";

    /**
     * 根据主KEY以及序号生成相应的子私钥
     *
     * @param masterKey 主key
     * @param index     子KEY的序号
     * @return 子KEY
     */
    public static DeterministicKey genSubKeyFromMasterKey(DeterministicKey masterKey, int index) {
        DeterministicHierarchy dh = new DeterministicHierarchy(masterKey);
        ImmutableList<ChildNumber> list = ImmutableList.of(new ChildNumber(index));
        return dh.get(list, true, true);
    }

    /**
     * 根据SEED字符串生成主私钥与公钥
     *
     * @param seedStr SEED字符串
     */
    public static DeterministicKey genMasterPriKey(String seedStr) {
        try {
            DeterministicSeed dSeed = new DeterministicSeed(seedStr, null, MASTER_KEY_PASSPHRASE,
                    System.currentTimeMillis());
            return HDKeyDerivation.createMasterPrivateKey(dSeed.getSeedBytes());
        } catch (UnreadableWalletException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据私钥生成CTC钱包地址
     *
     * @param key 某个货币的私钥（也包含了公钥)
     * @return 生成私钥对应的CTC钱包地址
     */
    public static String genLTCAddress(ECKey key) {
        ECKey pubKey = key.isCompressed() ? key : ECKey.fromPublicOnly(ECKey.compressPoint(key.getPubKeyPoint()));
        byte[] pubKeyBytes = pubKey.getPubKey();
        byte[] prefix = new byte[]{(byte) 48};

        // 获取公钥基础
        byte[] tempPubKeyBytes = Sha256Hash.hash(pubKeyBytes);
        RIPEMD160Digest hash160 = new RIPEMD160Digest();
        hash160.update(tempPubKeyBytes, 0, tempPubKeyBytes.length);
        byte[] out = new byte[20];
        hash160.doFinal(out, 0);
        byte[] extenPubKey = Bytes.concat(prefix, out);

        // 生成Suffix
        byte[] suffix = Arrays.copyOfRange(Sha256Hash.hashTwice(extenPubKey), 0, 4);

        byte[] addr = Bytes.concat(extenPubKey, suffix);
        return Base58.encode(addr);
    }

    /**
     * 生成LTC公钥BASE58格式
     *
     * @param key 私钥
     * @return BASE58格式的公钥
     */
    public static String genLTCPubKeyBase58(ECKey key) {
        byte[] pubKeyBytes = null;
        if (key.isCompressed()) {
            pubKeyBytes = key.getPubKey();
        } else {
            pubKeyBytes = ECKey.fromPublicOnly(ECKey.compressPoint(key.getPubKeyPoint())).getPubKey();
        }

        RIPEMD160Digest hash160 = new RIPEMD160Digest();
        hash160.update(pubKeyBytes, 0, pubKeyBytes.length);
        byte[] out = new byte[20];
        hash160.doFinal(out, 0);
        byte[] orignBytes = Bytes.concat(pubKeyBytes, Arrays.copyOfRange(out, 0, 4));
        return Base58.encode(orignBytes);
    }

    /**
     * 生成LTC公钥BASE58格式
     *
     * @param key 私钥
     * @return BASE58格式的公钥
     */
    public static String genLTCPubKeyHex(ECKey key) {
        byte[] pubKeyBytes = null;
        if (key.isCompressed()) {
            pubKeyBytes = key.getPubKey();
        } else {
            pubKeyBytes = ECKey.fromPublicOnly(ECKey.compressPoint(key.getPubKeyPoint())).getPubKey();
        }

        RIPEMD160Digest hash160 = new RIPEMD160Digest();
        hash160.update(pubKeyBytes, 0, pubKeyBytes.length);
        byte[] out = new byte[20];
        hash160.doFinal(out, 0);
        byte[] orignBytes = Bytes.concat(pubKeyBytes, Arrays.copyOfRange(out, 0, 4));
        return ByteUtilities.toHexString(orignBytes);
    }

    /**
     * WIF格式的私钥，用于私钥导出
     *
     * @param priKey 私钥
     * @return WIF格式的私钥
     */
    public static String genLTCPriKeyWif(ECKey priKey) {
        byte[] priKeyBytes = priKey.getSecretBytes();
        byte[] needToHash = new byte[priKeyBytes.length + 1];
        byte[] prefix = new byte[]{(byte) 176};
        needToHash = Bytes.concat(prefix, priKeyBytes);
        String alg = "SHA-256";
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance(alg);
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
        byte[] seedBytes = md.digest(md.digest(needToHash));
        byte[] suffix = Arrays.copyOfRange(seedBytes, 0, 4);
        return Base58.encode(Bytes.concat(needToHash, suffix));
    }

    public static void main(String[] args) {

        String seedStr = "hello word this is china"; // 生成主私钥
        DeterministicKey masterKey = genMasterPriKey(seedStr); // 生成BTC/UB的子私钥与地址
        String privateKey = genLTCPriKeyWif(masterKey);
        System.out.println("LTC的私钥：" + privateKey);
        String address = genLTCAddress(masterKey);
        System.out.println("LTC的地址：" + address);

        String publickey = genLTCPubKeyHex(masterKey);
        System.out.println("LTC的公钥：" + publickey);
    }

}
