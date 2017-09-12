package studio.blockops.vyom.core.crypto.secp256k1;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

import javax.inject.Inject;

import org.hamcrest.core.IsEqual;
import org.jukito.All;
import org.jukito.JukitoModule;
import org.jukito.JukitoRunner;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.spongycastle.util.encoders.Hex;

import studio.blockops.vyom.core.crypto.BlockCipher;
import studio.blockops.vyom.core.crypto.CryptoEngine;
import studio.blockops.vyom.core.crypto.CryptoModule;
import studio.blockops.vyom.core.crypto.PrivateKey;
import studio.blockops.vyom.core.crypto.PublicKey;

@RunWith(Enclosed.class)
public class SecP256K1BlockCipherTest {
	
	@Inject
	protected CryptoEngine engine;
	
	@RunWith(JukitoRunner.class)
	public static class DecryptTest extends SecP256K1BlockCipherTest {

		public static class Module extends JukitoModule {

			@Override
			protected void configureTest() {
				install(new CryptoModule());
				install(new SecP256K1Module());
				
				bindManyInstances(TestData.class,
						new TestData(
								"049934a7b2d7f9af8fd9db941d9da281ac9381b5740e1f64f7092f3588d4f87f5ce55191a6653e5e80c1c5dd538169aa123e70dc6ffc5af1827e546c0e958e42dad355bcc1fcb9cdf2cf47ff524d2ad98cbf275e661bf4cf00960e74b5956b799771334f426df007350b46049adb21a6e78ab1408d5e6ccde6fb5e69f0f4c92bb9c725c02f99fa72b9cdc8dd53cff089e0e73317f61cc5abf6152513cb7d833f09d2851603919bf0fbe44d79a09245c6e8338eb502083dc84b846f2fee1cc310d2cc8b1b9334728f97220bb799376233e113",
								"5e173f6ac3c669587538e7727cf19b782a4f2fda07c1eaa662c593e5e85e3051",
								"802b052f8b066640bba94a4fc39d63815c377fced6fcb84d27f791c9921ddf3e9bf0108e298f490812847109cbd778fae393e80323fd643209841a3b7f110397f37ec61d84cea03dcc5e8385db93248584e8af4b4d1c832d8c7453c0089687a700"));
			}
		}
		
		@Test
		public void decryptTest(@All TestData data) {
			final BlockCipher blockCipher = engine.createBlockCipher();
			
			final PrivateKey privateKey = PrivateKey.createFromHexString(data.privateKey);
			final byte[] input = Hex.decode(data.input);
			final byte[] actual = blockCipher.decrypt(privateKey, input);
			
			assertThat(Hex.toHexString(actual), equalTo(data.expected));
		}
	}
	
	@RunWith(JukitoRunner.class)
	public static class RoundTripTest extends SecP256K1BlockCipherTest {

		public static class Module extends JukitoModule {

			@Override
			protected void configureTest() {
				install(new CryptoModule());
				install(new SecP256K1Module());
			}			
		}
		
		@Test
		public void roundTripTest() {			
			final PrivateKey privateKey = PrivateKey.createFromHexString("5e173f6ac3c669587538e7727cf19b782a4f2fda07c1eaa662c593e5e85e3051");
			final PublicKey publicKey = engine.createKeyGenerator().derivePublicKey(privateKey);
	        final byte[] input = Hex.decode("1122334455");

			final BlockCipher blockCipher = engine.createBlockCipher();
			
			final byte[] encryptedBytes = blockCipher.encrypt(privateKey, publicKey, input);
			final byte[] decryptedBytes = blockCipher.decrypt(privateKey, encryptedBytes);
			
			assertThat(encryptedBytes, not(IsEqual.equalTo(decryptedBytes)));
			assertThat(decryptedBytes, equalTo(input));
		}
	}
	
	private static class TestData {
		protected final String input;
		protected final String privateKey;
		protected final String expected;
		public TestData(String input, String privateKey, String expected) {
			this.input = input;
			this.privateKey = privateKey;
			this.expected = expected;
		}
	}
}
