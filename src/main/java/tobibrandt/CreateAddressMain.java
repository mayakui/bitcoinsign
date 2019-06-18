package tobibrandt;

import tobibrandt.bitcoincash.BitcoinCashAddressFormatter;
import tobibrandt.bitcoincash.BitcoinCashAddressType;

/**
 * Created by Administrator on 2018/6/6 0006.
 */
public class CreateAddressMain {


    public static void testCreateBitcoinCashAddressAndValidate() {
        byte[] sha256hash160 = new byte[] { -102, -34, 26, 42, -22, 88, -67, -91, 105, 103, -3, 9,
                -102, 9, 81, 118, 35, 60, 86, -109 };

        String cashAddress = BitcoinCashAddressFormatter.toCashAddress(BitcoinCashAddressType.P2PKH, sha256hash160,
                MoneyNetwork.REG);

        System.out.println(cashAddress);

    }


    public static void main(String[] args){
        testCreateBitcoinCashAddressAndValidate();
    }
}
