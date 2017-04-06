package com.company;


import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {
    private final static String FILE_PATH = "./saved.txt";
    private final static int XOR_INDX = 15; // just random value
    private final static String CHARSET = "UTF-8";
    // using random access modifiers just because
    // properties to serialize
    @Save
    private static final String TEXT_FIELD = "private text property to serialize";
    @Save
    public static final int NUM_FIELD = 8;
    //properties to skip during serialization
    private static final String TEXT_FIELD_2 = "private text property to skip";
    public static final int NUM_FIELD_2 = 16;


    public static void main(String[] args) {
        serialize();
        deserialize();
    }

    private static void serialize() {
        System.out.println("running serialize()");
        Class<?> cls = Main.class;
        System.out.println("Searching for property to save...");
        String propertiesToSerializeString = "";
        for (Field property : cls.getDeclaredFields()) {
            if (property.isAnnotationPresent(Save.class)) {
                try {
                    String propertyName = property.getName();
                    Class<?> propertyType = property.getType();
                    Object propertyValue = property.get(null);

                    String propertyString = String.format("name: %s, type: %s, value: %s",
                            propertyName, propertyType.getSimpleName(), propertyValue.toString());
                    propertiesToSerializeString += propertyString + "\n";
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println("class properties before serialization");
        System.out.println(propertiesToSerializeString);
        //writing properties data to file
        try {
            byte[] propertiesBytes = propertiesToSerializeString.getBytes(CHARSET);
            byte[] encrypted = encrypt(propertiesBytes);
            Files.write(Paths.get(FILE_PATH), encrypted);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void deserialize() {
        System.out.println("running deserialize()");
        try {
            byte[] encryptedBytes = Files.readAllBytes(Paths.get(FILE_PATH));
            String restoredRawPropertiesString = new String(encryptedBytes, CHARSET);
            System.out.println("class properties after deserialization before decrypt");
            System.out.println(restoredRawPropertiesString);

            byte[] decryptedBytes = decrypt(encryptedBytes);
            String restoredPropertiesString = new String(decryptedBytes, CHARSET);
            System.out.println("class properties after deserialization after decrypt");
            System.out.println(restoredPropertiesString);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static byte[] encrypt(byte[] bytes) {
        byte[] encrypted = new byte[bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            encrypted[i] = (byte) (bytes[i] ^ XOR_INDX);
        }
        //printBytes(encrypted);
        return encrypted;
    }

    private static byte[] decrypt(byte[] bytes) {
        byte[] decrypted = new byte[bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            decrypted[i] = (byte) (bytes[i] ^ XOR_INDX);
        }
        //printBytes(decrypted);
        return decrypted;
    }

    private static void printBytes(byte[] bytes) {
        System.out.println("printing bytes...");
        for (Byte b : bytes)
            System.out.println(b.byteValue());
    }

}
