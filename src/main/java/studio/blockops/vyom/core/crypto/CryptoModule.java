package studio.blockops.vyom.core.crypto;

import java.security.Provider;

import com.google.inject.AbstractModule;

public class CryptoModule extends AbstractModule {
	
	@Override
	protected void configure() {
		bind(Provider.class).toProvider(BouncyCastleInstaller.class).asEagerSingleton();
	}

}
