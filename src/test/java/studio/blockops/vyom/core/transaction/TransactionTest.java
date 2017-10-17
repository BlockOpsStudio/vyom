package studio.blockops.vyom.core.transaction;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.math.BigInteger;

import javax.inject.Inject;

import org.jukito.JukitoModule;
import org.jukito.JukitoRunner;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.spongycastle.util.encoders.Hex;

import studio.blockops.vyom.core.Address;
import studio.blockops.vyom.crypto.CryptoEngine;
import studio.blockops.vyom.crypto.CryptoModule;
import studio.blockops.vyom.crypto.KeyPair;
import studio.blockops.vyom.crypto.PrivateKey;
import studio.blockops.vyom.crypto.PublicKey;
import studio.blockops.vyom.crypto.secp256k1.SecP256K1Module;
import studio.blockops.vyom.serialization.rlp.RLPDecoder;

@RunWith(Enclosed.class)
public class TransactionTest {

    @RunWith(JukitoRunner.class)
    public static class EthereumTest extends TransactionTest {

        public static class Module extends JukitoModule {

            @Override
            protected void configureTest() {
                install(new CryptoModule());
                install(new SecP256K1Module());
            }
        }

        @Inject
        private TransactionBuilder builder;

        @Inject
        private CryptoEngine engine;

        @Test
        public void afterEIP158Test() {
            int chainId = 1;
            final String privateKeyString = "4646464646464646464646464646464646464646464646464646464646464646";
            final BigInteger signatureR = new BigInteger("18515461264373351373200002665853028612451056578545711640558177340181847433846");
            final BigInteger signatureS = new BigInteger("46948507304638947509940763649030358759909902576025900602547168820602576006531");
            final String expectedEncodedTransaction = "f86c098504a817c800825208943535353535353535353535353535353535353535880de0b6b3a76400008025a028ef61340bd939bc2195fe537567866003e1a15d3c71ff63e1590620aa636276a067cbe9d8997f761aecb703304b3800ccf555c9f3dc64214b297fb1966a3b6d83";

            final PrivateKey privateKey = PrivateKey.createFromHexString(privateKeyString);
            final PublicKey publicKey = engine.createKeyGenerator().derivePublicKey(privateKey);
            final KeyPair keyPair = KeyPair.create(privateKey, publicKey);

            final Transaction actualTransaction =
                    builder.create(
                        new BigInteger("9"),
                        new BigInteger("20000000000"),
                        new BigInteger("21000"),
                        new BigInteger("1000000000000000000"),
                        null,
                        chainId)
                    .to(Address.createFromHexString("3535353535353535353535353535353535353535"))
                    .sign(keyPair)
                    .build();

            assertThat(actualTransaction.getSignature().get().getR(), equalTo(signatureR));
            assertThat(actualTransaction.getSignature().get().getS(), equalTo(signatureS));

            final byte[] actualEncodedTransaction = actualTransaction.getEncoded();
            assertThat(actualEncodedTransaction, equalTo(Hex.decode(expectedEncodedTransaction)));

            final Transaction decodedTransaction =
                    builder.decode(
                            new RLPDecoder(actualEncodedTransaction))
                    .build();

            assertThat(actualTransaction, equalTo(decodedTransaction));
        }

    }

}
