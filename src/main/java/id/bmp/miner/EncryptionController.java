package id.bmp.miner;

import id.bmp.miner.manager.EncryptionManager;

public class EncryptionController {

    public static void main(String[] args) {

        String plainText = "128781998";
        String encrypted = EncryptionManager.getInstance().encrypt(plainText);
        System.out.println(encrypted);

        String encryptedText = "Zu2edhc5Mz5blvw0tj/KpDam46ZhdNJKjewjG3R4f9uNesTplU5hHiHcdC+iMyKNf3AMqhr8YX8ZzI4REGX27Q==";
        String decrypted = EncryptionManager.getInstance().decrypt(encryptedText);
        System.out.println(decrypted);
    }
}
