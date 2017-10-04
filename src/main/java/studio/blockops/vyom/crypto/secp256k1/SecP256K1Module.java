package studio.blockops.vyom.crypto.secp256k1;

import org.spongycastle.asn1.sec.SECNamedCurves;
import org.spongycastle.asn1.x9.X9ECParameters;
import org.spongycastle.crypto.params.ECDomainParameters;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.assistedinject.FactoryModuleBuilder;

import studio.blockops.vyom.crypto.BlockCipher;
import studio.blockops.vyom.crypto.CryptoEngine;
import studio.blockops.vyom.crypto.Curve;
import studio.blockops.vyom.crypto.KeyGenerator;
import studio.blockops.vyom.crypto.Signer;

/**
 * The SecP256K1 guice module.
 */
public class SecP256K1Module extends AbstractModule {

    @Override
    protected void configure() {
        install(new FactoryModuleBuilder()
            .implement(Signer.class, SecP256K1Signer.class)
            .implement(KeyGenerator.class, SecP256K1KeyGenerator.class)
            .implement(BlockCipher.class, SecP256K1BlockCipher.class)
            .build(CryptoEngine.class));
    }

    /**
     * Guice provides method to get {@link Curve} inta
     * @return A {@link SecP256K1Curve} singleton instance
     */
    @Provides @Singleton
    Curve provideCurve() {
        final X9ECParameters params = SECNamedCurves.getByName("secp256k1");
        final ECDomainParameters ecParams = new ECDomainParameters(params.getCurve(), params.getG(), params.getN(), params.getH());
        return new SecP256K1Curve(ecParams);
    }
}
