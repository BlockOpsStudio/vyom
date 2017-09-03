package studio.blockops.vyom.core.crypto;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.math.BigInteger;

import org.jukito.All;
import org.jukito.JukitoModule;
import org.jukito.JukitoRunner;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

@RunWith(Enclosed.class)
public class PrivateKeyTest {

	@RunWith(JukitoRunner.class)
	public static class BigIntegerPrivateKeyTest extends PrivateKeyTest {

		public static class Module extends JukitoModule {

			@Override
			protected void configureTest() {
				bindManyInstances(String.class, "2847", "2275");
			}
		}

		@Test
		public void canCreateFromBigInteger(@All String value) {
			final PrivateKey key = PrivateKey.create(new BigInteger(value));

			assertThat(key.getRaw(), equalTo(new BigInteger(value)));
		}

		@Test(expected = NullPointerException.class)
		public void cannotCreateFromNull() {
			PrivateKey.create(null);
		}	
	}

	@RunWith(JukitoRunner.class)	
	public static class DecimalStringPrivateKeyTest extends PrivateKeyTest {

		public static class Module extends JukitoModule {

			@Override
			protected void configureTest() {
				bindManyNamedInstances(String.class, "can",    "5426", "-7461");
				bindManyNamedInstances(String.class, "cannot", "6A87", "F5A4");
			}
		}

		@Test
		public void canCreateFromDecimalString(@All("can") String value) {
			final PrivateKey key = PrivateKey.createFromDecimalString(value);

			assertThat(key.getRaw(), equalTo(new BigInteger(value)));
		}

		@Test(expected = CryptoException.class)
		public void cannotCreateFromDecimalString(@All("cannot") String value) {
			PrivateKey.createFromDecimalString(value);
		}

		@Test(expected = NullPointerException.class)
		public void cannotCreateFromNull() {
			PrivateKey.createFromDecimalString(null);
		}		
	}

	@RunWith(JukitoRunner.class)	
	public static class HexStringPrivateKeyTest extends PrivateKeyTest {

		public static class Module extends JukitoModule {

			@Override
			protected void configureTest() {
				bindManyNamedInstances(String.class, "can",    "08e3", "227F", "7461");
				bindManyNamedInstances(String.class, "cannot", "1H33", "22G75");
			}
		}

		@Test
		public void canCreateFromHexString(@All("can") String value) {
			final PrivateKey key = PrivateKey.createFromHexString(value);

			assertThat(key.getRaw(), equalTo(new BigInteger(value, 16)));
		}

		@Test(expected = CryptoException.class)
		public void cannotCreateFromHexString(@All("cannot") String value) {
			PrivateKey.createFromHexString(value);
		}

		@Test(expected = NullPointerException.class)
		public void cannotCreateFromNull() {
			PrivateKey.createFromHexString(null);
		}		
	}

	@RunWith(JukitoRunner.class)	
	public static class EqualsPrivateKeyTest extends PrivateKeyTest {

		@Test
		public void equalsOnlyReturnsTrueForEquivalentObjects() {
			final PrivateKey key = PrivateKey.create(new BigInteger("5641"));

			assertThat(key, is(notNullValue()));
			assertThat(key, equalTo(PrivateKey.createFromDecimalString("5641")));
			assertThat(key, is(not(equalTo(PrivateKey.createFromDecimalString("5640")))));
			assertThat(key, is(not(equalTo(PrivateKey.createFromHexString("5641")))));
		}		
	}

	@RunWith(JukitoRunner.class)	
	public static class HashCodePrivateKeyTest extends PrivateKeyTest {

		@Test
		public void hashCodeOnlyReturnsTrueForEquivalentObjects() {
			final PrivateKey key = PrivateKey.create(new BigInteger("5641"));
			final int hashCode = key.hashCode();

			assertThat(hashCode, equalTo(PrivateKey.createFromDecimalString("5641").hashCode()));
			assertThat(hashCode, equalTo(PrivateKey.createFromHexString("1609").hashCode()));
			assertThat(hashCode, is(not(equalTo(PrivateKey.createFromDecimalString("5640").hashCode()))));
			assertThat(hashCode, is(not(equalTo(PrivateKey.createFromHexString("5641").hashCode()))));
		}		
	}

	@RunWith(JukitoRunner.class)	
	public static class ToStringPrivateKeyTest extends PrivateKeyTest {
		
		public static class Module extends JukitoModule {
			
			@Override
			protected void configureTest() {
				bindManyNamedInstances(TestData.class, "decimal",
						new TestData("2275", "08e3"),
						new TestData("-7461", "e2db"));
				bindManyNamedInstances(TestData.class, "hex",
						new TestData("2275", "2275"),
						new TestData("227F", "227f"),
						new TestData("08e3", "08e3"));
			}
		}

		@Test
		public void toStringDecimalStringPrivateKeyTest(@All("decimal") TestData data) {
			final PrivateKey key = PrivateKey.createFromDecimalString(data.value);

			assertThat(key.toString(), equalTo(data.expected));
		}	

		@Test
		public void toStringHexStringPrivateKeyTest(@All("hex") TestData data) {
			final PrivateKey key = PrivateKey.createFromHexString(data.value);

			assertThat(key.toString(), equalTo(data.expected));
		}	
		
		private final static class TestData {
			
			private final String value;
			
			private final String expected;
			
			private TestData(String value, String expected) {
				this.value = value;
				this.expected = expected;
			}
		}	
	}

}
