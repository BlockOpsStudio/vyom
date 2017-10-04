package studio.blockops.vyom.crypto;

import java.security.Provider;

import com.google.inject.AbstractModule;

/**
 * Base module for all crypto needs.
 */
public class CryptoModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(Provider.class).toProvider(SpongyCastleInstaller.class).asEagerSingleton();
    }

}
