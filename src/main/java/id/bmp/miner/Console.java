package id.bmp.miner;

import id.bmp.miner.configuration.EncryptConfig;

public class Console {

    // This class is used to generate encrypted value(s) for application.properties

    private static EncryptConfig config = new EncryptConfig();

    public static void main(String[] args) {
        String[] strArr = {"QbeWPTri71+OeDXTshm65IB8lZoc0Qds","XFA4PS5bvuBElYgrc52THA==","T9eZvNXd9U1nRLaFxDT+lMsLweIzP81i","QaHDN620xqnVVLMf5tW+Db5fonFmcfF+","zNbQUjeVid6efnMjFAlRzegNGSq9wzZJ"};
        encrypt(strArr);
        decrypt(strArr);

    }

    static void encrypt(String[] arr) {
        for (String str : arr) {
            String encrypted = config.stringEncryptor().encrypt(str);
            System.out.println("Clear : " + str);
            System.out.println("Encrypted : ENC(" + encrypted + ")");
            System.out.println();
        }
    }

    static void decrypt(String[] arr) {
        for (String str : arr) {
            String encrypted = config.stringEncryptor().decrypt(str);
            System.out.println("Encrypted : " + str);
            System.out.println("Clear :" + encrypted);
            System.out.println();
        }
    }
}
