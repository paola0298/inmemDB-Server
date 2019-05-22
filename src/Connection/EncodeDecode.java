package Connection;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class EncodeDecode {
    public static String cifrarBase64(String a){
        Base64.Encoder encoder = Base64.getEncoder();
        String b = encoder.encodeToString(a.getBytes(StandardCharsets.UTF_8) );
        return b;
    }

    public static String descifrarBase64(String a){
        Base64.Decoder decoder = Base64.getDecoder();
        byte[] decodedByteArray = decoder.decode(a);

        String b = new String(decodedByteArray);
        return b;
    }

//    public static void main(String[] args) {
//        String cifrado = cifrarBase64("hola");
//        String decifrado = descifrarBase64(cifrado);
//
//        System.out.println("String cifrado " + cifrado);
//        System.out.println("String decifrado " + decifrado);
//    }

}
