package studio.blockops.vyom.core.crypto.secp256k1;

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

import studio.blockops.vyom.core.crypto.CryptoEngine;
import studio.blockops.vyom.core.crypto.CryptoModule;
import studio.blockops.vyom.core.crypto.KeyGenerator;
import studio.blockops.vyom.core.crypto.KeyPair;
import studio.blockops.vyom.core.crypto.PrivateKey;
import studio.blockops.vyom.core.crypto.Signature;
import studio.blockops.vyom.core.crypto.Signer;

@RunWith(Enclosed.class)
public class SecP256K1SignerTest {
	
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
								"This is an example of a signed message.", 
								"c85ef7d79691fe79573b1a7064c19c1a9819ebdbd1faaab1a8ec92344438aaf4",
								"d2ce488f4da29e68f22cb05cac1b19b75df170a12b4ad1bdd4531b8e9115c6fb75c1fe50a95e8ccffcbb5482a1e42fbbdd6324131dfe75c3b3b7f9a7c721eccb01"));
			}
		}
		
		@Test
		public void signerProducesCorrectSignature(@All SignTestData data) {
			final Signer signer = createSigner(data.privateKey);
			final Signature signature = signer.sign(data.example.getBytes());
			
			assertThat(signature.toString(), equalTo(data.expectedSignature));
		}
		
		private static class SignTestData extends TestData {
			private final String expectedSignature;
			public SignTestData(String example, String privateKey, String expectedSignature) {
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
				
				bindManyNamedInstances(VerifyTestData.class, "one",
						new VerifyTestData(
								"This is an example of a signed message.",
								"c85ef7d79691fe79573b1a7064c19c1a9819ebdbd1faaab1a8ec92344438aaf4",
								null));
				
				bindManyNamedInstances(VerifyTestData.class, "two",
						new VerifyTestData(
								"This is an example of a signed message.",
								"c85ef7d79691fe79573b1a7064c19c1a9819ebdbd1faaab1a8ec92344438aaf4",
								Signature.create(
										"95350169487015575001444507567851457309689354890536757640816472151471942911739",
										"53263359985948886716508128220321578640585687230908938312863706887350789467339", 
										(byte) 28))
						);
				
				bindManyNamedInstances(VerifyTestData.class, "three",
						new VerifyTestData(
								"This is an example of a signed message.",
								"c85ef7d79691fe79573b1a7064c19c1a9819ebdbd1faaab1a8ec92344438aaf4",
								Signature.create(
										"28157690258821599598544026901946453245423343069728565040002908283498585537001",
										"30212485197630673222315826773656074299979444367665131281281249560925428307087", 
										(byte) 28))
						);
			}
		}
		
		@Test
		public void testVerifySignature1(@All("one") VerifyTestData data) {	
			final Signer signer = createSigner(data.privateKey);
			final Signature signature = signer.sign(data.example.getBytes());
			
	        assertTrue(signer.verify(data.example.getBytes(), signature));
		}

	    @Test
	    public void testVerifySignature2(@All("two") VerifyTestData data) {	
			final Signer signer = createSigner(data.privateKey);
			
	        assertTrue(signer.verify(data.example.getBytes(), data.signature));
	    }

	    @Test
	    public void testVerifySignature3(@All("three") VerifyTestData data) {	
			final Signer signer = createSigner(data.privateKey);
			
	        assertFalse(signer.verify(data.example.getBytes(), data.signature));
	    }
		
		private static class VerifyTestData extends TestData {
			private final Signature signature;
			public VerifyTestData(String example, String privateKey, Signature expectedSignature) {
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
		public TestData(String example, String privateKey) {
			this.example = example;
			this.privateKey = privateKey;
		}
	}
}
