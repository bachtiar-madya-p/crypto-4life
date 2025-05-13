package id.bmp.miner;

import id.bmp.miner.manager.EncryptionManager;

public class EncryptionController {

    public static void main(String[] args) {

        String plainText = "";
        String encrypted = EncryptionManager.getInstance().encrypt(plainText);
        System.out.println(encrypted);

        String encryptedText = "";
        String decrypted = EncryptionManager.getInstance().decrypt(encryptedText);
        System.out.println(decrypted);
    }
}
