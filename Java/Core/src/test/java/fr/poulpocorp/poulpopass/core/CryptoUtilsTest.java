package fr.poulpocorp.poulpopass.core;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.util.concurrent.atomic.AtomicReference;

public class CryptoUtilsTest {

    @Test
    void encryptAndDecrypt() {
        final byte[] data = LOREM_IPSUM.getBytes(StandardCharsets.UTF_8);
        final char[] password = new char[] {'p', 'a', 's', 's', 'w', 'o', 'r', 'd'};

        AtomicReference<byte[]> encryptedData = new AtomicReference<>();
        Assertions.assertDoesNotThrow(() -> {
            byte[] d = CryptoUtils.encrypt(data, password);

            encryptedData.set(d);
        });

        AtomicReference<byte[]> decryptedData = new AtomicReference<>();
        Assertions.assertDoesNotThrow(() -> {
            byte[] d = CryptoUtils.decrypt(encryptedData.get(), password);

            decryptedData.set(d);
        });

        String text = new String(decryptedData.get(), StandardCharsets.UTF_8);

        Assertions.assertEquals(LOREM_IPSUM, text);
    }

    @Test
    void encryptAndDecrypt2() {
        final byte[] data = LOREM_IPSUM.getBytes(StandardCharsets.UTF_8);
        final char[] password = new char[] {'p', 'a', 's', 's', 'w', 'o', 'r', 'd'};
        final char[] password2 = new char[] {'p', 'a', 's', 's', 'w', 'o', 'r', 'd', '2'};

        AtomicReference<byte[]> encryptedData = new AtomicReference<>();
        Assertions.assertDoesNotThrow(() -> {
            byte[] d = CryptoUtils.encrypt(data, password);

            encryptedData.set(d);
        });

        AtomicReference<byte[]> decryptedData = new AtomicReference<>();
        Assertions.assertThrows(InvalidKeyException.class, () -> {
            byte[] d = CryptoUtils.decrypt(encryptedData.get(), password2);

            decryptedData.set(d);
        });
    }

    private static final String LOREM_IPSUM = """
            Lorem ipsum dolor sit amet, consectetur adipiscing elit. 
            Aenean vel commodo urna, posuere vestibulum tortor. Morbi 
            pulvinar lectus non euismod mollis. Morbi hendrerit, velit 
            id vehicula viverra, neque justo semper sapien, sed tempor 
            sapien eros in neque. Sed sed magna ornare ligula varius 
            eleifend. Suspendisse condimentum facilisis lectus, vitae 
            convallis urna iaculis vel. In mattis mattis quam ac aliquam. 
            Curabitur tincidunt mi quis velit faucibus viverra. Nam neque 
            nisi, interdum ac erat in, consequat auctor enim. Curabitur eu
            feugiat est. In justo ante, fringilla laoreet mi a, fermentum
            euismod augue. Aliquam volutpat efficitur massa vel bibendum.
            Fusce lacinia purus vel turpis suscipit, ac facilisis orci
            finibus. Proin et ante consequat ligula tempor scelerisque ut
            quis diam. Nunc viverra magna est, sed condimentum sem dictum nec.
                        
            Mauris faucibus ultrices velit a molestie. Integer condimentum 
            nunc eget tortor egestas, nec blandit lacus rhoncus. Praesent 
            semper venenatis tortor, non posuere enim scelerisque et. Maecenas 
            feugiat magna ac dapibus pretium. Integer vel orci et metus fermentum 
            placerat et sed dolor. Morbi ornare commodo nisl eget lacinia. Maecenas 
            vel odio tellus. Aliquam accumsan, mi vitae semper hendrerit, nisl quam 
            interdum augue, ut mattis turpis neque ut urna. Donec semper lorem at 
            purus aliquam, non elementum nisl imperdiet. Etiam elementum aliquam elit 
            id vulputate. Sed interdum, quam id consectetur pharetra, nulla leo luctus 
            justo, ut iaculis erat nisl sed elit.
                        
            Praesent dolor nisl, maximus vitae imperdiet id, imperdiet ut eros. 
            Nam luctus quis justo at luctus. Praesent at justo in quam cursus 
            dictum. Vivamus dapibus sollicitudin elit sed tempus. Proin pharetra, 
            leo ut dapibus rutrum, tortor velit mattis neque, vulputate pretium dui 
            est vitae tortor. Donec non erat ut massa condimentum gravida. Duis ac 
            ex sit amet dui congue molestie vel ac nulla. Maecenas scelerisque massa 
            eget lectus lacinia sodales. Vestibulum bibendum ac nulla a porta. Fusce 
            lobortis orci id ipsum consectetur, dignissim scelerisque est interdum. 
            Praesent et arcu pellentesque, efficitur neque sed, venenatis metus. Ut 
            suscipit lorem nisl, sit amet efficitur massa pharetra vitae. Ut accumsan 
            fermentum viverra. In ac vestibulum nibh, eget pharetra mauris.
                        
            Quisque faucibus nibh eget sem commodo elementum. Maecenas sit amet mauris 
            odio. Duis non venenatis ex. Pellentesque congue, orci nec consectetur 
            vulputate, nisl elit congue lacus, sed commodo magna ex sit amet nulla. 
            Nullam sit amet elit urna. Nullam molestie felis quis condimentum porta. 
            In rutrum scelerisque euismod. Integer eget mi et nunc laoreet blandit 
            quis eget dolor. Class aptent taciti sociosqu ad litora torquent per 
            conubia nostra, per inceptos himenaeos. Suspendisse ligula arcu, facilisis 
            a tincidunt eget, facilisis malesuada risus. Integer et congue lacus, in 
            ultricies sapien. Etiam eget tortor quis risus semper auctor vel eu lacus. 
            Morbi non sem hendrerit, pretium ex id, vestibulum sapien. Pellentesque quis 
            porta leo, sit amet vestibulum diam. In ut mauris suscipit, aliquet orci ut, 
            mollis sapien. Sed rutrum blandit libero ac aliquet.
                        
            Vivamus gravida sit amet nulla varius fermentum. Maecenas vitae nisi nibh. 
            Duis in iaculis tellus, vitae elementum nisi. Sed sagittis bibendum neque sed 
            laoreet. Morbi sed tristique turpis. Maecenas diam ante, ullamcorper in orci 
            sit amet, feugiat malesuada quam. Proin finibus eleifend semper. Donec tempor, 
            libero nec dapibus sodales, odio magna bibendum dolor, non fermentum ante nisi 
            quis ex. Donec at blandit felis.\s
            """;
}