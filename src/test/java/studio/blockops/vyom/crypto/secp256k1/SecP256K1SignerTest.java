package studio.blockops.vyom.crypto.secp256k1;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.math.BigInteger;

import javax.inject.Inject;

import org.jukito.All;
import org.jukito.JukitoModule;
import org.jukito.JukitoRunner;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import studio.blockops.vyom.crypto.CryptoEngine;
import studio.blockops.vyom.crypto.CryptoModule;
import studio.blockops.vyom.crypto.KeyGenerator;
import studio.blockops.vyom.crypto.KeyPair;
import studio.blockops.vyom.crypto.PrivateKey;
import studio.blockops.vyom.crypto.Signature;
import studio.blockops.vyom.crypto.Signer;

@RunWith(Enclosed.class)
public class SecP256K1SignerTest {

    private static final String EXAMPLE1 = "This is an example of a signed message.";
    private static final String EXAMPLE2 = "We want, neither to rule nor to be ruled.";

    @Inject
    protected CryptoEngine engine;

    @RunWith(JukitoRunner.class)
    public static class SignTest extends SecP256K1SignerTest {

        public static class Module extends JukitoModule {

            @Override
            protected void configureTest() {
                install(new CryptoModule());
                install(new SecP256K1Module());

                bindManyInstances(SignTestData.class,
                        new SignTestData(
                                EXAMPLE1,
                                "c85ef7d79691fe79573b1a7064c19c1a9819ebdbd1faaab1a8ec92344438aaf4",
                                "d2ce488f4da29e68f22cb05cac1b19b75df170a12b4ad1bdd4531b8e9115c6fb75c1fe50a95e8ccffcbb5482a1e42fbbdd6324131dfe75c3b3b7f9a7c721eccb01"),
                        new SignTestData(
                                EXAMPLE2,
                                "16dcadddaf26707f475d821b3a63c215c93eebe5f26a67f0437b21c5924b4e06",
                                "b280e3c18d4b1829bb776d401d71039973a455a5cac628e7880beec9ddab706c64fcbe4ca5e8cf457663bf8365e1cb0802852e5cbfa681e780c2957e68a5ff2600"));
            }
        }

        @Test
        public void signerProducesCorrectSignature(@All SignTestData data) {
            final Signer signer = createSigner(data.privateKey);
            final Signature signature = signer.sign(data.example.getBytes());

            assertThat(signature.toHexString(), equalTo(data.expectedSignature));
        }

        private static class SignTestData extends TestData {
            private final String expectedSignature;
            private SignTestData(String example, String privateKey, String expectedSignature) {
                super(example, privateKey);
                this.expectedSignature = expectedSignature;
            }
        }
    }

    @RunWith(JukitoRunner.class)
    public static class VerifyTest extends SecP256K1SignerTest {

        public static class Module extends JukitoModule {

            @Override
            protected void configureTest() {
                install(new CryptoModule());
                install(new SecP256K1Module());

                bindManyNamedInstances(VerifyTestData.class, "passingOne",
                        new VerifyTestData(
                                EXAMPLE1,
                                "c85ef7d79691fe79573b1a7064c19c1a9819ebdbd1faaab1a8ec92344438aaf4"),
                        new VerifyTestData(
                                EXAMPLE1,
                                "9201D5322CDB870181830D7529EDB9A668A09324277263865B5D136500234CB2"),
                        new VerifyTestData(
                                EXAMPLE1,
                                "FED8F9D7E0428821D24E5429FFA5F8232FC08313D61C1BF6DF9B1DDF81973ADE"),
                        new VerifyTestData(
                                EXAMPLE2,
                                "008c1f538e0f15a1253692b076dfad46578dc874912b148019dd3c7076d4025dba"),
                        new VerifyTestData(
                                EXAMPLE2,
                                "009e728371176014fb287ba3afef07b6d92986e888b3b02cdeba7d211d4504c94d"),
                        new VerifyTestData(
                                EXAMPLE2,
                                "16dcadddaf26707f475d821b3a63c215c93eebe5f26a67f0437b21c5924b4e06"));

                bindManyNamedInstances(VerifyTestData.class, "passingTwo",
                        new VerifyTestData(
                                EXAMPLE1,
                                "c85ef7d79691fe79573b1a7064c19c1a9819ebdbd1faaab1a8ec92344438aaf4",
                                Signature.create(
                                        "95350169487015575001444507567851457309689354890536757640816472151471942911739",
                                        "53263359985948886716508128220321578640585687230908938312863706887350789467339",
                                        (byte) 28)),
                        new VerifyTestData(
                                EXAMPLE1,
                                "39275331bcae98fc128f807eeb891679ff37cd2c2cbbb0f6471d6c3eefac28fa",
                                Signature.create(
                                        "53523084094929846674767071520381850459571807150769623136856554462268220921307",
                                        "37013409979285123448901959175903102439833020928129976400005656816267627623998",
                                        (byte) 27)),
                        new VerifyTestData(
                                EXAMPLE2,
                                "76b0ae30ad0daa8827d4212ff6fdc39a4f8721e1f71bfab1d13673b1e6c9cf76",
                                Signature.create(
                                        "91937090222526495204360920025118885454746045900904063857674139170604458522453",
                                        "39718484358250258087468342763765637350677774940942902234616491724613568213885",
                                        (byte) 28)),
                        new VerifyTestData(
                                EXAMPLE2,
                                "00c9658e78146a62ee0bc222a25ca79667b7a7e5225b839fce1d4aa20e64aecf1a",
                                Signature.create(
                                        "3481889508855353400212866021453388788216477259592210790574622014743414393141",
                                        "41176147825937572686088104069824857097502298904298423301423542898017523614710",
                                        (byte) 28)));

                bindManyNamedInstances(VerifyTestData.class, "failing",
                        new VerifyTestData(
                                EXAMPLE1,
                                "c85ef7d79691fe79573b1a7064c19c1a9819ebdbd1faaab1a8ec92344438aaf4",
                                Signature.create(
                                        "28157690258821599598544026901946453245423343069728565040002908283498585537001",
                                        "30212485197630673222315826773656074299979444367665131281281249560925428307087",
                                        (byte) 28)));
            }
        }

        @Test
        public void testVerifySignature1(@All("passingOne") VerifyTestData data) {
            final Signer signer = createSigner(data.privateKey);
            final Signature signature = signer.sign(data.example.getBytes());

            assertTrue(signer.verify(data.example.getBytes(), signature));
        }

        @Test
        public void testVerifySignature2(@All("passingTwo") VerifyTestData data) {
            final Signer signer = createSigner(data.privateKey);

            assertTrue(signer.verify(data.example.getBytes(), data.signature));
        }

        @Test
        public void testVerifySignature3(@All("failing") VerifyTestData data) {
            final Signer signer = createSigner(data.privateKey);

            assertFalse(signer.verify(data.example.getBytes(), data.signature));
        }

        private static class VerifyTestData extends TestData {
            private final Signature signature;
            private VerifyTestData(String example, String privateKey) {
                this(example, privateKey, null);
            }
            private VerifyTestData(String example, String privateKey, Signature expectedSignature) {
                super(example, privateKey);
                this.signature = expectedSignature;
            }
        }
    }

    protected Signer createSigner(String privateKeyHexString) {
        final PrivateKey privateKey = PrivateKey.create(new BigInteger(privateKeyHexString, 16));
        final KeyGenerator keyGenerator = engine.createKeyGenerator();
        final KeyPair keyPair = KeyPair.create(privateKey, keyGenerator.derivePublicKey(privateKey));
        return engine.createSigner(keyPair);
    }

    private static abstract class TestData {
        protected final String example;
        protected final String privateKey;
        private TestData(String example, String privateKey) {
            this.example = example;
            this.privateKey = privateKey;
        }
    }
}
