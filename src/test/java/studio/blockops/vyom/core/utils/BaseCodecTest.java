package studio.blockops.vyom.core.utils;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.jukito.All;
import org.jukito.JukitoModule;
import org.jukito.JukitoRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

public class BaseCodecTest {

	@RunWith(JukitoRunner.class)
	public static class EncodeBase16Test extends BaseCodecTest {
		
		public static class Module extends JukitoModule {
			
			@Override
			protected void configureTest() {
				bindManyInstances(TestData.class, 
						new TestData(new byte[] { 0x4e, 0x45, 0x4d, 0x46, 0x54, 0x57 }, "4e454d465457"),
						new TestData(new byte[] { 0x00, 0x00, 0x0d, 0x46, 0x54, 0x57 }, "00000d465457"),
						new TestData(new byte[] { 0x7F, 0x6E, 0x5D, 0x4C, 0x3B, 0x2A }, "7f6e5d4c3b2a"));
			}
		}
		
		@Test
		public void canEncodeBytesToBase16String(@All TestData data) {
			final String actual = BaseCodec.encodeBase16(data.bytes);
			
			assertThat(actual, equalTo(data.expected));
		}
		
		private final static class TestData {
			
			private final byte[] bytes;
			
			private final String expected;
			
			private TestData(byte[] bytes, String expected) {
				this.bytes = bytes;
				this.expected = expected;
			}
		}
	}

	@RunWith(JukitoRunner.class)
	public static class DecodeBase16Test extends BaseCodecTest {
		
		public static class Module extends JukitoModule {
			
			@Override
			protected void configureTest() {
				bindManyInstances(TestData.class, 
						new TestData("4e454d465457", new byte[] { 0x4e, 0x45, 0x4d, 0x46, 0x54, 0x57 }),
						new TestData("00000d465457", new byte[] { 0x00, 0x00, 0x0d, 0x46, 0x54, 0x57 }),
						new TestData("7f6e5d4c3b2a", new byte[] { 0x7F, 0x6E, 0x5D, 0x4C, 0x3B, 0x2A }),						
						new TestData("e454d465457",  new byte[] { 0x0e, 0x45, 0x4d, 0x46, 0x54, 0x57 }));
			}
		}
		
		@Test
		public void canEncodeBytesToBase16String(@All TestData data) {
			final byte[] actual = BaseCodec.decodeBase16(data.value);
			
			assertThat(actual, equalTo(data.expected));
		}
		
		private final static class TestData {
			
			private final String value;
			
			private final byte[] expected;
			
			private TestData(String value, byte[] expected) {
				this.value = value;
				this.expected = expected;
			}
		}
	}

}
