package tobibrandt;

import tobibrandt.bitcoincash.BitcoinCashAddressDecodedParts;
import tobibrandt.bitcoincash.BitcoinCashAddressFormatter;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Administrator on 2018/6/6 0006.
 */
public class GenerateOldAddress {

    public static void main(String[] args){
        try {
            //私钥：cRtYUe4FyNWCyHZ7GtbD1CzydbGqp3oeU3mJgMQRmnaE9bA2rqDm
            BitcoinCashAddressDecodedParts decodedCashAddress = BitcoinCashAddressFormatter
                    .decodeCashAddress("bchreg:qzddux32afvtmftfvl7snxsf29mzx0zkjv3egpn6tp", MoneyNetwork.REG);


            byte[] address = new byte[21];

            address[0] = 111;
            System.arraycopy(decodedCashAddress.getHash(), 0, address, 1, 20);

            MessageDigest md1 = MessageDigest.getInstance("SHA-256");
            md1.reset();
            md1.update(address);
            byte[] address256 = md1.digest();

            MessageDigest md2 = MessageDigest.getInstance("SHA-256");
            md2.reset();
            md2.update(address256);
            byte[] address256256 = md2.digest();

            byte[] address25 = new byte[25];
            System.arraycopy(address, 0, address25, 0, address.length);
            System.arraycopy(address256256, 0, address25, address25.length - 4, 4);
            System.out.println(Base58.encode(address25));
            //mudpRXc9ZZNXibS5WsCWeT6z4tyXAfdLR1
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
}
