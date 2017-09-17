package studio.blockops.vyom.core.serialization.rlp;

import static org.junit.Assert.assertArrayEquals;

import javax.inject.Inject;

import org.ethereum.util.ByteUtil;
import org.jukito.All;
import org.jukito.JukitoModule;
import org.jukito.JukitoRunner;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

@RunWith(Enclosed.class)
public class RLPEncoderTest {
	
	@Inject
	protected RLPEncoder encoder;

	@RunWith(JukitoRunner.class)
	public static class EncodeByteTest extends RLPEncoderTest {

		public static class Module extends JukitoModule {

			@Override
			protected void configureTest() {
				bindManyInstances(ByteTestData.class, 
						new ByteTestData((byte) 0, "80"),
						new ByteTestData((byte) 120, "78"),
						new ByteTestData((byte) 127, "7F"));
			}
		}

		@Test
		public void encodeByteTest(@All ByteTestData data) {
			encoder.encodeByte(data.input);
			
			byte[] actual = encoder.getEncoded();
			
			assertArrayEquals(data.expected, actual);
		}
		
		private static class ByteTestData extends TestData {
			private final byte input;
			private ByteTestData(byte input, String expected) {
				super(expected);
				this.input = input;
			}
		}
	}

	@RunWith(JukitoRunner.class)
	public static class EncodeShortTest extends RLPEncoderTest {

		public static class Module extends JukitoModule {

			@Override
			protected void configureTest() {
				bindManyInstances(ShortTestData.class, 
						new ShortTestData((byte) 0, "80"),
						new ShortTestData((byte) 120, "78"),
						new ShortTestData((byte) 127, "7F"),
						
						new ShortTestData((short) 30303, "82 76 5F"),
						new ShortTestData((short) 20202, "82 4E EA"),
						new ShortTestData((short) 40202, "82 9D 0A"));
			}
		}

		@Test
		public void encodeShortTest(@All ShortTestData data) {
			encoder.encodeShort(data.input);
			
			byte[] actual = encoder.getEncoded();
			
			assertArrayEquals(data.expected, actual);
		}
		
		private static class ShortTestData extends TestData {
			private final short input;
			private ShortTestData(short input, String expected) {
				super(expected);
				this.input = input;
			}
		}
	}

	@RunWith(JukitoRunner.class)
	public static class EncodeIntTest extends RLPEncoderTest {

		public static class Module extends JukitoModule {

			@Override
			protected void configureTest() {
				bindManyInstances(IntTestData.class, 
						new IntTestData((byte) 0, "80"),
						new IntTestData((byte) 120, "78"),
						new IntTestData((byte) 127, "7F"),

						new IntTestData(1024, "82 04 00"),
						new IntTestData(30303, "82 76 5F"),
						new IntTestData(20202, "82 4E EA"),
						new IntTestData(65536, "83 01 00 00"),
						
						new IntTestData(Integer.MIN_VALUE, "84 80 00 00 00"),
						new IntTestData(Integer.MAX_VALUE, "84 7F FF FF FF"),
						
						new IntTestData(0xFFFFFFFF, "84 FF FF FF FF"));
			}
		}

		@Test
		public void encodeIntTest(@All IntTestData data) {
			encoder.encodeInt(data.input);
			
			byte[] actual = encoder.getEncoded();
			
			assertArrayEquals(data.expected, actual);
		}
		
		private static class IntTestData extends TestData {
			private final int input;
			private IntTestData(int input, String expected) {
				super(expected);
				this.input = input;
			}
		}
	}
	
	private static abstract class TestData {
		protected final byte[] expected;
		private TestData(String expected) {
			this.expected = ByteUtil.hexStringToBytes(expected);
		}
	}

}
