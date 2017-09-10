package studio.blockops.vyom.core.crypto.secp256k1;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import java.math.BigInteger;

import javax.inject.Inject;

import org.jukito.JukitoModule;
import org.jukito.JukitoRunner;
import org.junit.Before;
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
	
	protected Signer signer;
	
	@Before
	public void setup() {
	    final String privateKeyHexString = "c85ef7d79691fe79573b1a7064c19c1a9819ebdbd1faaab1a8ec92344438aaf4";
	    final PrivateKey privateKey = PrivateKey.create(new BigInteger(privateKeyHexString, 16));
		final KeyGenerator keyGenerator = engine.createKeyGenerator();
		final KeyPair keyPair = KeyPair.create(privateKey, keyGenerator.derivePublicKey(privateKey));
		
		signer = engine.createSigner(keyPair);
	}
	
	@RunWith(JukitoRunner.class)
	public static class SignTest extends SecP256K1SignerTest {

		public static class Module extends JukitoModule {

			@Override
			protected void configureTest() {
				install(new CryptoModule());
				install(new SecP256K1Module());
			}
		}
		
		@Test
		public void signerProducesCorrectSignature() {
			final String exampleMessage = "This is an example of a signed message.";
			final Signature signature = signer.sign(exampleMessage.getBytes());
			
			final String expectedSignature = "d2ce488f4da29e68f22cb05cac1b19b75df170a12b4ad1bdd4531b8e9115c6fb75c1fe50a95e8ccffcbb5482a1e42fbbdd6324131dfe75c3b3b7f9a7c721eccb01";
			
			assertThat(signature.toString(), equalTo(expectedSignature));
		}
	}
	
	@RunWith(JukitoRunner.class)
	public static class VerifyTest extends SecP256K1SignerTest {

		public static class Module extends JukitoModule {

			@Override
			protected void configureTest() {
				install(new CryptoModule());
				install(new SecP256K1Module());
			}
		}

	    @Test
	    public void testVerifySignature1() {	        
			final String exampleMessage = "This is an example of a signed message.";	
			final byte[] input = exampleMessage.getBytes();
			
	        final BigInteger r = new BigInteger("95350169487015575001444507567851457309689354890536757640816472151471942911739");
	        final BigInteger s = new BigInteger("53263359985948886716508128220321578640585687230908938312863706887350789467339");
	        final Signature signature = Signature.create(r, s, (byte) 28);
			
	        assertTrue(signer.verify(input, signature));
	    }
		
		@Test
		public void testVerifySignature2() {
			final String exampleMessage = "This is an example of a signed message.";			
			final byte[] input = exampleMessage.getBytes();
			
			final Signature signature = signer.sign(input);
			
	        assertTrue(signer.verify(input, signature));
		}

	    @Test
	    public void testVerifySignature3() {	        
			final String exampleMessage = "This is an example of a signed message.";	
			final byte[] input = exampleMessage.getBytes();
			
	        final BigInteger r = new BigInteger("28157690258821599598544026901946453245423343069728565040002908283498585537001");
	        final BigInteger s = new BigInteger("30212485197630673222315826773656074299979444367665131281281249560925428307087");
	        final Signature signature = Signature.create(r, s, (byte) 28);
			
	        assertFalse(signer.verify(input, signature));
	    }
	}
}
