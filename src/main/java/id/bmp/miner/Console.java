package id.bmp.miner;

import id.bmp.miner.configuration.EncryptConfig;

public class Console {

    // This class is used to generate encrypted value(s) for application.properties

    private static EncryptConfig config = new EncryptConfig();

    public static void main(String[] args) {
        String[] strArr = {"203.194.114.182","crypto_4life","crypto_user","CryptoLife!2025"};
        encrypt(strArr);
        decrypt(strArr);

/*       String encrypted = EncryptionManager.getInstance().encrypt("bbjn qmob nwhk twzj");
       System.out.println(encrypted);*/
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
