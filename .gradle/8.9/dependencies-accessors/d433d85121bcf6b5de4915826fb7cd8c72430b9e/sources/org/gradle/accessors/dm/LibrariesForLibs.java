package org.gradle.accessors.dm;

import org.gradle.api.NonNullApi;
import org.gradle.api.artifacts.MinimalExternalModuleDependency;
import org.gradle.plugin.use.PluginDependency;
import org.gradle.api.artifacts.ExternalModuleDependencyBundle;
import org.gradle.api.artifacts.MutableVersionConstraint;
import org.gradle.api.provider.Provider;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.provider.ProviderFactory;
import org.gradle.api.internal.catalog.AbstractExternalDependencyFactory;
import org.gradle.api.internal.catalog.DefaultVersionCatalog;
import java.util.Map;
import org.gradle.api.internal.attributes.ImmutableAttributesFactory;
import org.gradle.api.internal.artifacts.dsl.CapabilityNotationParser;
import javax.inject.Inject;

/**
 * A catalog of dependencies accessible via the {@code libs} extension.
 */
@NonNullApi
public class LibrariesForLibs extends AbstractExternalDependencyFactory {

    private final AbstractExternalDependencyFactory owner = this;
    private final AndroidxLibraryAccessors laccForAndroidxLibraryAccessors = new AndroidxLibraryAccessors(owner);
    private final ComLibraryAccessors laccForComLibraryAccessors = new ComLibraryAccessors(owner);
    private final DeLibraryAccessors laccForDeLibraryAccessors = new DeLibraryAccessors(owner);
    private final DevLibraryAccessors laccForDevLibraryAccessors = new DevLibraryAccessors(owner);
    private final KotlinxLibraryAccessors laccForKotlinxLibraryAccessors = new KotlinxLibraryAccessors(owner);
    private final MeLibraryAccessors laccForMeLibraryAccessors = new MeLibraryAccessors(owner);
    private final VersionAccessors vaccForVersionAccessors = new VersionAccessors(providers, config);
    private final BundleAccessors baccForBundleAccessors = new BundleAccessors(objects, providers, config, attributesFactory, capabilityNotationParser);
    private final PluginAccessors paccForPluginAccessors = new PluginAccessors(providers, config);

    @Inject
    public LibrariesForLibs(DefaultVersionCatalog config, ProviderFactory providers, ObjectFactory objects, ImmutableAttributesFactory attributesFactory, CapabilityNotationParser capabilityNotationParser) {
        super(config, providers, objects, attributesFactory, capabilityNotationParser);
    }

    /**
     * Group of libraries at <b>androidx</b>
     */
    public AndroidxLibraryAccessors getAndroidx() {
        return laccForAndroidxLibraryAccessors;
    }

    /**
     * Group of libraries at <b>com</b>
     */
    public ComLibraryAccessors getCom() {
        return laccForComLibraryAccessors;
    }

    /**
     * Group of libraries at <b>de</b>
     */
    public DeLibraryAccessors getDe() {
        return laccForDeLibraryAccessors;
    }

    /**
     * Group of libraries at <b>dev</b>
     */
    public DevLibraryAccessors getDev() {
        return laccForDevLibraryAccessors;
    }

    /**
     * Group of libraries at <b>kotlinx</b>
     */
    public KotlinxLibraryAccessors getKotlinx() {
        return laccForKotlinxLibraryAccessors;
    }

    /**
     * Group of libraries at <b>me</b>
     */
    public MeLibraryAccessors getMe() {
        return laccForMeLibraryAccessors;
    }

    /**
     * Group of versions at <b>versions</b>
     */
    public VersionAccessors getVersions() {
        return vaccForVersionAccessors;
    }

    /**
     * Group of bundles at <b>bundles</b>
     */
    public BundleAccessors getBundles() {
        return baccForBundleAccessors;
    }

    /**
     * Group of plugins at <b>plugins</b>
     */
    public PluginAccessors getPlugins() {
        return paccForPluginAccessors;
    }

    public static class AndroidxLibraryAccessors extends SubDependencyFactory {
        private final AndroidxAnnotationLibraryAccessors laccForAndroidxAnnotationLibraryAccessors = new AndroidxAnnotationLibraryAccessors(owner);
        private final AndroidxNavigationLibraryAccessors laccForAndroidxNavigationLibraryAccessors = new AndroidxNavigationLibraryAccessors(owner);
        private final AndroidxPreferenceLibraryAccessors laccForAndroidxPreferenceLibraryAccessors = new AndroidxPreferenceLibraryAccessors(owner);

        public AndroidxLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>swiperefreshlayout</b> with <b>androidx.swiperefreshlayout:swiperefreshlayout</b> coordinates and
         * with version <b>1.1.0</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getSwiperefreshlayout() {
            return create("androidx.swiperefreshlayout");
        }

        /**
         * Group of libraries at <b>androidx.annotation</b>
         */
        public AndroidxAnnotationLibraryAccessors getAnnotation() {
            return laccForAndroidxAnnotationLibraryAccessors;
        }

        /**
         * Group of libraries at <b>androidx.navigation</b>
         */
        public AndroidxNavigationLibraryAccessors getNavigation() {
            return laccForAndroidxNavigationLibraryAccessors;
        }

        /**
         * Group of libraries at <b>androidx.preference</b>
         */
        public AndroidxPreferenceLibraryAccessors getPreference() {
            return laccForAndroidxPreferenceLibraryAccessors;
        }

    }

    public static class AndroidxAnnotationLibraryAccessors extends SubDependencyFactory {

        public AndroidxAnnotationLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>jvm</b> with <b>androidx.annotation:annotation-jvm</b> coordinates and
         * with version <b>1.7.0</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getJvm() {
            return create("androidx.annotation.jvm");
        }

    }

    public static class AndroidxNavigationLibraryAccessors extends SubDependencyFactory {
        private final AndroidxNavigationFragmentLibraryAccessors laccForAndroidxNavigationFragmentLibraryAccessors = new AndroidxNavigationFragmentLibraryAccessors(owner);
        private final AndroidxNavigationUiLibraryAccessors laccForAndroidxNavigationUiLibraryAccessors = new AndroidxNavigationUiLibraryAccessors(owner);

        public AndroidxNavigationLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Group of libraries at <b>androidx.navigation.fragment</b>
         */
        public AndroidxNavigationFragmentLibraryAccessors getFragment() {
            return laccForAndroidxNavigationFragmentLibraryAccessors;
        }

        /**
         * Group of libraries at <b>androidx.navigation.ui</b>
         */
        public AndroidxNavigationUiLibraryAccessors getUi() {
            return laccForAndroidxNavigationUiLibraryAccessors;
        }

    }

    public static class AndroidxNavigationFragmentLibraryAccessors extends SubDependencyFactory {

        public AndroidxNavigationFragmentLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>ktx</b> with <b>androidx.navigation:navigation-fragment-ktx</b> coordinates and
         * with version reference <b>androidx.navigation</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getKtx() {
            return create("androidx.navigation.fragment.ktx");
        }

    }

    public static class AndroidxNavigationUiLibraryAccessors extends SubDependencyFactory {

        public AndroidxNavigationUiLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>ktx</b> with <b>androidx.navigation:navigation-ui-ktx</b> coordinates and
         * with version reference <b>androidx.navigation</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getKtx() {
            return create("androidx.navigation.ui.ktx");
        }

    }

    public static class AndroidxPreferenceLibraryAccessors extends SubDependencyFactory {

        public AndroidxPreferenceLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>ktx</b> with <b>androidx.preference:preference-ktx</b> coordinates and
         * with version reference <b>androidx.preference</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getKtx() {
            return create("androidx.preference.ktx");
        }

    }

    public static class ComLibraryAccessors extends SubDependencyFactory {
        private final ComAndroidLibraryAccessors laccForComAndroidLibraryAccessors = new ComAndroidLibraryAccessors(owner);
        private final ComDrakeetLibraryAccessors laccForComDrakeetLibraryAccessors = new ComDrakeetLibraryAccessors(owner);
        private final ComGithubLibraryAccessors laccForComGithubLibraryAccessors = new ComGithubLibraryAccessors(owner);
        private final ComGoogleLibraryAccessors laccForComGoogleLibraryAccessors = new ComGoogleLibraryAccessors(owner);
        private final ComSquareupLibraryAccessors laccForComSquareupLibraryAccessors = new ComSquareupLibraryAccessors(owner);

        public ComLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Group of libraries at <b>com.android</b>
         */
        public ComAndroidLibraryAccessors getAndroid() {
            return laccForComAndroidLibraryAccessors;
        }

        /**
         * Group of libraries at <b>com.drakeet</b>
         */
        public ComDrakeetLibraryAccessors getDrakeet() {
            return laccForComDrakeetLibraryAccessors;
        }

        /**
         * Group of libraries at <b>com.github</b>
         */
        public ComGithubLibraryAccessors getGithub() {
            return laccForComGithubLibraryAccessors;
        }

        /**
         * Group of libraries at <b>com.google</b>
         */
        public ComGoogleLibraryAccessors getGoogle() {
            return laccForComGoogleLibraryAccessors;
        }

        /**
         * Group of libraries at <b>com.squareup</b>
         */
        public ComSquareupLibraryAccessors getSquareup() {
            return laccForComSquareupLibraryAccessors;
        }

    }

    public static class ComAndroidLibraryAccessors extends SubDependencyFactory {
        private final ComAndroidToolsLibraryAccessors laccForComAndroidToolsLibraryAccessors = new ComAndroidToolsLibraryAccessors(owner);

        public ComAndroidLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Group of libraries at <b>com.android.tools</b>
         */
        public ComAndroidToolsLibraryAccessors getTools() {
            return laccForComAndroidToolsLibraryAccessors;
        }

    }

    public static class ComAndroidToolsLibraryAccessors extends SubDependencyFactory {
        private final ComAndroidToolsBuildLibraryAccessors laccForComAndroidToolsBuildLibraryAccessors = new ComAndroidToolsBuildLibraryAccessors(owner);

        public ComAndroidToolsLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Group of libraries at <b>com.android.tools.build</b>
         */
        public ComAndroidToolsBuildLibraryAccessors getBuild() {
            return laccForComAndroidToolsBuildLibraryAccessors;
        }

    }

    public static class ComAndroidToolsBuildLibraryAccessors extends SubDependencyFactory {

        public ComAndroidToolsBuildLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>apksig</b> with <b>com.android.tools.build:apksig</b> coordinates and
         * with version <b>8.1.0</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getApksig() {
            return create("com.android.tools.build.apksig");
        }

    }

    public static class ComDrakeetLibraryAccessors extends SubDependencyFactory {

        public ComDrakeetLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>about</b> with <b>com.drakeet.about:about</b> coordinates and
         * with version <b>3.0.0</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getAbout() {
            return create("com.drakeet.about");
        }

        /**
         * Dependency provider for <b>multitype</b> with <b>com.drakeet.multitype:multitype</b> coordinates and
         * with version <b>4.3.0</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getMultitype() {
            return create("com.drakeet.multitype");
        }

    }

    public static class ComGithubLibraryAccessors extends SubDependencyFactory {
        private final ComGithubKirich1409LibraryAccessors laccForComGithubKirich1409LibraryAccessors = new ComGithubKirich1409LibraryAccessors(owner);
        private final ComGithubKyuubiranLibraryAccessors laccForComGithubKyuubiranLibraryAccessors = new ComGithubKyuubiranLibraryAccessors(owner);
        private final ComGithubLiujingxingLibraryAccessors laccForComGithubLiujingxingLibraryAccessors = new ComGithubLiujingxingLibraryAccessors(owner);
        private final ComGithubTopjohnwuLibraryAccessors laccForComGithubTopjohnwuLibraryAccessors = new ComGithubTopjohnwuLibraryAccessors(owner);

        public ComGithubLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Group of libraries at <b>com.github.kirich1409</b>
         */
        public ComGithubKirich1409LibraryAccessors getKirich1409() {
            return laccForComGithubKirich1409LibraryAccessors;
        }

        /**
         * Group of libraries at <b>com.github.kyuubiran</b>
         */
        public ComGithubKyuubiranLibraryAccessors getKyuubiran() {
            return laccForComGithubKyuubiranLibraryAccessors;
        }

        /**
         * Group of libraries at <b>com.github.liujingxing</b>
         */
        public ComGithubLiujingxingLibraryAccessors getLiujingxing() {
            return laccForComGithubLiujingxingLibraryAccessors;
        }

        /**
         * Group of libraries at <b>com.github.topjohnwu</b>
         */
        public ComGithubTopjohnwuLibraryAccessors getTopjohnwu() {
            return laccForComGithubTopjohnwuLibraryAccessors;
        }

    }

    public static class ComGithubKirich1409LibraryAccessors extends SubDependencyFactory {

        public ComGithubKirich1409LibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>viewbindingpropertydelegate</b> with <b>com.github.kirich1409:viewbindingpropertydelegate</b> coordinates and
         * with version <b>1.5.6</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getViewbindingpropertydelegate() {
            return create("com.github.kirich1409.viewbindingpropertydelegate");
        }

    }

    public static class ComGithubKyuubiranLibraryAccessors extends SubDependencyFactory {

        public ComGithubKyuubiranLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>ezxhelper</b> with <b>com.github.kyuubiran:EzXHelper</b> coordinates and
         * with version reference <b>ezxhelper</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getEzxhelper() {
            return create("com.github.kyuubiran.ezxhelper");
        }

    }

    public static class ComGithubLiujingxingLibraryAccessors extends SubDependencyFactory {
        private final ComGithubLiujingxingRxhttpLibraryAccessors laccForComGithubLiujingxingRxhttpLibraryAccessors = new ComGithubLiujingxingRxhttpLibraryAccessors(owner);

        public ComGithubLiujingxingLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Group of libraries at <b>com.github.liujingxing.rxhttp</b>
         */
        public ComGithubLiujingxingRxhttpLibraryAccessors getRxhttp() {
            return laccForComGithubLiujingxingRxhttpLibraryAccessors;
        }

    }

    public static class ComGithubLiujingxingRxhttpLibraryAccessors extends SubDependencyFactory implements DependencyNotationSupplier {
        private final ComGithubLiujingxingRxhttpConverterLibraryAccessors laccForComGithubLiujingxingRxhttpConverterLibraryAccessors = new ComGithubLiujingxingRxhttpConverterLibraryAccessors(owner);

        public ComGithubLiujingxingRxhttpLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>rxhttp</b> with <b>com.github.liujingxing:rxhttp</b> coordinates and
         * with version reference <b>liujingxing.rxhttp</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> asProvider() {
            return create("com.github.liujingxing.rxhttp");
        }

        /**
         * Dependency provider for <b>compiler</b> with <b>com.github.liujingxing:rxhttp-compiler</b> coordinates and
         * with version reference <b>liujingxing.rxhttp</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getCompiler() {
            return create("com.github.liujingxing.rxhttp.compiler");
        }

        /**
         * Group of libraries at <b>com.github.liujingxing.rxhttp.converter</b>
         */
        public ComGithubLiujingxingRxhttpConverterLibraryAccessors getConverter() {
            return laccForComGithubLiujingxingRxhttpConverterLibraryAccessors;
        }

    }

    public static class ComGithubLiujingxingRxhttpConverterLibraryAccessors extends SubDependencyFactory {

        public ComGithubLiujingxingRxhttpConverterLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>serialization</b> with <b>com.github.liujingxing:rxhttp-converter-serialization</b> coordinates and
         * with version reference <b>liujingxing.rxhttp</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getSerialization() {
            return create("com.github.liujingxing.rxhttp.converter.serialization");
        }

    }

    public static class ComGithubTopjohnwuLibraryAccessors extends SubDependencyFactory {
        private final ComGithubTopjohnwuLibsuLibraryAccessors laccForComGithubTopjohnwuLibsuLibraryAccessors = new ComGithubTopjohnwuLibsuLibraryAccessors(owner);

        public ComGithubTopjohnwuLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Group of libraries at <b>com.github.topjohnwu.libsu</b>
         */
        public ComGithubTopjohnwuLibsuLibraryAccessors getLibsu() {
            return laccForComGithubTopjohnwuLibsuLibraryAccessors;
        }

    }

    public static class ComGithubTopjohnwuLibsuLibraryAccessors extends SubDependencyFactory {

        public ComGithubTopjohnwuLibsuLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>core</b> with <b>com.github.topjohnwu.libsu:core</b> coordinates and
         * with version reference <b>topjohnwu.libsu</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getCore() {
            return create("com.github.topjohnwu.libsu.core");
        }

    }

    public static class ComGoogleLibraryAccessors extends SubDependencyFactory {
        private final ComGoogleAndroidLibraryAccessors laccForComGoogleAndroidLibraryAccessors = new ComGoogleAndroidLibraryAccessors(owner);
        private final ComGoogleFirebaseLibraryAccessors laccForComGoogleFirebaseLibraryAccessors = new ComGoogleFirebaseLibraryAccessors(owner);

        public ComGoogleLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Group of libraries at <b>com.google.android</b>
         */
        public ComGoogleAndroidLibraryAccessors getAndroid() {
            return laccForComGoogleAndroidLibraryAccessors;
        }

        /**
         * Group of libraries at <b>com.google.firebase</b>
         */
        public ComGoogleFirebaseLibraryAccessors getFirebase() {
            return laccForComGoogleFirebaseLibraryAccessors;
        }

    }

    public static class ComGoogleAndroidLibraryAccessors extends SubDependencyFactory {
        private final ComGoogleAndroidGmsLibraryAccessors laccForComGoogleAndroidGmsLibraryAccessors = new ComGoogleAndroidGmsLibraryAccessors(owner);

        public ComGoogleAndroidLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>material</b> with <b>com.google.android.material:material</b> coordinates and
         * with version reference <b>material</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getMaterial() {
            return create("com.google.android.material");
        }

        /**
         * Group of libraries at <b>com.google.android.gms</b>
         */
        public ComGoogleAndroidGmsLibraryAccessors getGms() {
            return laccForComGoogleAndroidGmsLibraryAccessors;
        }

    }

    public static class ComGoogleAndroidGmsLibraryAccessors extends SubDependencyFactory {
        private final ComGoogleAndroidGmsPlayLibraryAccessors laccForComGoogleAndroidGmsPlayLibraryAccessors = new ComGoogleAndroidGmsPlayLibraryAccessors(owner);

        public ComGoogleAndroidGmsLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Group of libraries at <b>com.google.android.gms.play</b>
         */
        public ComGoogleAndroidGmsPlayLibraryAccessors getPlay() {
            return laccForComGoogleAndroidGmsPlayLibraryAccessors;
        }

    }

    public static class ComGoogleAndroidGmsPlayLibraryAccessors extends SubDependencyFactory {
        private final ComGoogleAndroidGmsPlayServicesLibraryAccessors laccForComGoogleAndroidGmsPlayServicesLibraryAccessors = new ComGoogleAndroidGmsPlayServicesLibraryAccessors(owner);

        public ComGoogleAndroidGmsPlayLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Group of libraries at <b>com.google.android.gms.play.services</b>
         */
        public ComGoogleAndroidGmsPlayServicesLibraryAccessors getServices() {
            return laccForComGoogleAndroidGmsPlayServicesLibraryAccessors;
        }

    }

    public static class ComGoogleAndroidGmsPlayServicesLibraryAccessors extends SubDependencyFactory {

        public ComGoogleAndroidGmsPlayServicesLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>ads</b> with <b>com.google.android.gms:play-services-ads</b> coordinates and
         * with version <b>22.3.0</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getAds() {
            return create("com.google.android.gms.play.services.ads");
        }

    }

    public static class ComGoogleFirebaseLibraryAccessors extends SubDependencyFactory {
        private final ComGoogleFirebaseAnalyticsLibraryAccessors laccForComGoogleFirebaseAnalyticsLibraryAccessors = new ComGoogleFirebaseAnalyticsLibraryAccessors(owner);

        public ComGoogleFirebaseLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>bom</b> with <b>com.google.firebase:firebase-bom</b> coordinates and
         * with version reference <b>firebase.bom</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getBom() {
            return create("com.google.firebase.bom");
        }

        /**
         * Group of libraries at <b>com.google.firebase.analytics</b>
         */
        public ComGoogleFirebaseAnalyticsLibraryAccessors getAnalytics() {
            return laccForComGoogleFirebaseAnalyticsLibraryAccessors;
        }

    }

    public static class ComGoogleFirebaseAnalyticsLibraryAccessors extends SubDependencyFactory {

        public ComGoogleFirebaseAnalyticsLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>ktx</b> with <b>com.google.firebase:firebase-analytics-ktx</b> coordinates and
         * with <b>no version specified</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getKtx() {
            return create("com.google.firebase.analytics.ktx");
        }

    }

    public static class ComSquareupLibraryAccessors extends SubDependencyFactory {

        public ComSquareupLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>okhttp3</b> with <b>com.squareup.okhttp3:okhttp</b> coordinates and
         * with version reference <b>okhttp3</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getOkhttp3() {
            return create("com.squareup.okhttp3");
        }

    }

    public static class DeLibraryAccessors extends SubDependencyFactory {
        private final DeRobvLibraryAccessors laccForDeRobvLibraryAccessors = new DeRobvLibraryAccessors(owner);

        public DeLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Group of libraries at <b>de.robv</b>
         */
        public DeRobvLibraryAccessors getRobv() {
            return laccForDeRobvLibraryAccessors;
        }

    }

    public static class DeRobvLibraryAccessors extends SubDependencyFactory {
        private final DeRobvAndroidLibraryAccessors laccForDeRobvAndroidLibraryAccessors = new DeRobvAndroidLibraryAccessors(owner);

        public DeRobvLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Group of libraries at <b>de.robv.android</b>
         */
        public DeRobvAndroidLibraryAccessors getAndroid() {
            return laccForDeRobvAndroidLibraryAccessors;
        }

    }

    public static class DeRobvAndroidLibraryAccessors extends SubDependencyFactory {
        private final DeRobvAndroidXposedLibraryAccessors laccForDeRobvAndroidXposedLibraryAccessors = new DeRobvAndroidXposedLibraryAccessors(owner);

        public DeRobvAndroidLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Group of libraries at <b>de.robv.android.xposed</b>
         */
        public DeRobvAndroidXposedLibraryAccessors getXposed() {
            return laccForDeRobvAndroidXposedLibraryAccessors;
        }

    }

    public static class DeRobvAndroidXposedLibraryAccessors extends SubDependencyFactory {

        public DeRobvAndroidXposedLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>api</b> with <b>de.robv.android.xposed:api</b> coordinates and
         * with version <b>82</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getApi() {
            return create("de.robv.android.xposed.api");
        }

    }

    public static class DevLibraryAccessors extends SubDependencyFactory {
        private final DevRikkaLibraryAccessors laccForDevRikkaLibraryAccessors = new DevRikkaLibraryAccessors(owner);

        public DevLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Group of libraries at <b>dev.rikka</b>
         */
        public DevRikkaLibraryAccessors getRikka() {
            return laccForDevRikkaLibraryAccessors;
        }

    }

    public static class DevRikkaLibraryAccessors extends SubDependencyFactory {
        private final DevRikkaHiddenLibraryAccessors laccForDevRikkaHiddenLibraryAccessors = new DevRikkaHiddenLibraryAccessors(owner);
        private final DevRikkaRikkaxLibraryAccessors laccForDevRikkaRikkaxLibraryAccessors = new DevRikkaRikkaxLibraryAccessors(owner);

        public DevRikkaLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Group of libraries at <b>dev.rikka.hidden</b>
         */
        public DevRikkaHiddenLibraryAccessors getHidden() {
            return laccForDevRikkaHiddenLibraryAccessors;
        }

        /**
         * Group of libraries at <b>dev.rikka.rikkax</b>
         */
        public DevRikkaRikkaxLibraryAccessors getRikkax() {
            return laccForDevRikkaRikkaxLibraryAccessors;
        }

    }

    public static class DevRikkaHiddenLibraryAccessors extends SubDependencyFactory {

        public DevRikkaHiddenLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>compat</b> with <b>dev.rikka.hidden:compat</b> coordinates and
         * with version <b>2.0.0</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getCompat() {
            return create("dev.rikka.hidden.compat");
        }

        /**
         * Dependency provider for <b>stub</b> with <b>dev.rikka.hidden:stub</b> coordinates and
         * with version <b>2.0.0</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getStub() {
            return create("dev.rikka.hidden.stub");
        }

    }

    public static class DevRikkaRikkaxLibraryAccessors extends SubDependencyFactory {
        private final DevRikkaRikkaxMaterialLibraryAccessors laccForDevRikkaRikkaxMaterialLibraryAccessors = new DevRikkaRikkaxMaterialLibraryAccessors(owner);

        public DevRikkaRikkaxLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Group of libraries at <b>dev.rikka.rikkax.material</b>
         */
        public DevRikkaRikkaxMaterialLibraryAccessors getMaterial() {
            return laccForDevRikkaRikkaxMaterialLibraryAccessors;
        }

    }

    public static class DevRikkaRikkaxMaterialLibraryAccessors extends SubDependencyFactory implements DependencyNotationSupplier {

        public DevRikkaRikkaxMaterialLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>material</b> with <b>dev.rikka.rikkax:material</b> coordinates and
         * with version reference <b>rikkax.material</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> asProvider() {
            return create("dev.rikka.rikkax.material");
        }

        /**
         * Dependency provider for <b>preference</b> with <b>dev.rikka.rikkax:material-preference</b> coordinates and
         * with version reference <b>rikkax.material</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getPreference() {
            return create("dev.rikka.rikkax.material.preference");
        }

    }

    public static class KotlinxLibraryAccessors extends SubDependencyFactory {
        private final KotlinxSerializationLibraryAccessors laccForKotlinxSerializationLibraryAccessors = new KotlinxSerializationLibraryAccessors(owner);

        public KotlinxLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Group of libraries at <b>kotlinx.serialization</b>
         */
        public KotlinxSerializationLibraryAccessors getSerialization() {
            return laccForKotlinxSerializationLibraryAccessors;
        }

    }

    public static class KotlinxSerializationLibraryAccessors extends SubDependencyFactory {

        public KotlinxSerializationLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>json</b> with <b>org.jetbrains.kotlinx:kotlinx-serialization-json</b> coordinates and
         * with version <b>1.5.1</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getJson() {
            return create("kotlinx.serialization.json");
        }

    }

    public static class MeLibraryAccessors extends SubDependencyFactory {
        private final MeZhanghaiLibraryAccessors laccForMeZhanghaiLibraryAccessors = new MeZhanghaiLibraryAccessors(owner);

        public MeLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Group of libraries at <b>me.zhanghai</b>
         */
        public MeZhanghaiLibraryAccessors getZhanghai() {
            return laccForMeZhanghaiLibraryAccessors;
        }

    }

    public static class MeZhanghaiLibraryAccessors extends SubDependencyFactory {
        private final MeZhanghaiAndroidLibraryAccessors laccForMeZhanghaiAndroidLibraryAccessors = new MeZhanghaiAndroidLibraryAccessors(owner);

        public MeZhanghaiLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Group of libraries at <b>me.zhanghai.android</b>
         */
        public MeZhanghaiAndroidLibraryAccessors getAndroid() {
            return laccForMeZhanghaiAndroidLibraryAccessors;
        }

    }

    public static class MeZhanghaiAndroidLibraryAccessors extends SubDependencyFactory {

        public MeZhanghaiAndroidLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>appiconloader</b> with <b>me.zhanghai.android:appiconloader</b> coordinates and
         * with version reference <b>appiconloader</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getAppiconloader() {
            return create("me.zhanghai.android.appiconloader");
        }

    }

    public static class VersionAccessors extends VersionFactory  {

        private final AndroidxVersionAccessors vaccForAndroidxVersionAccessors = new AndroidxVersionAccessors(providers, config);
        private final FirebaseVersionAccessors vaccForFirebaseVersionAccessors = new FirebaseVersionAccessors(providers, config);
        private final LiujingxingVersionAccessors vaccForLiujingxingVersionAccessors = new LiujingxingVersionAccessors(providers, config);
        private final RikkaxVersionAccessors vaccForRikkaxVersionAccessors = new RikkaxVersionAccessors(providers, config);
        private final TopjohnwuVersionAccessors vaccForTopjohnwuVersionAccessors = new TopjohnwuVersionAccessors(providers, config);
        public VersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Version alias <b>agp</b> with value <b>8.1.0</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getAgp() { return getVersion("agp"); }

        /**
         * Version alias <b>appiconloader</b> with value <b>1.5.0</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getAppiconloader() { return getVersion("appiconloader"); }

        /**
         * Version alias <b>ezxhelper</b> with value <b>2.9.2</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getEzxhelper() { return getVersion("ezxhelper"); }

        /**
         * Version alias <b>kotlin</b> with value <b>1.9.0</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getKotlin() { return getVersion("kotlin"); }

        /**
         * Version alias <b>material</b> with value <b>1.9.0</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getMaterial() { return getVersion("material"); }

        /**
         * Version alias <b>okhttp3</b> with value <b>4.10.0</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getOkhttp3() { return getVersion("okhttp3"); }

        /**
         * Group of versions at <b>versions.androidx</b>
         */
        public AndroidxVersionAccessors getAndroidx() {
            return vaccForAndroidxVersionAccessors;
        }

        /**
         * Group of versions at <b>versions.firebase</b>
         */
        public FirebaseVersionAccessors getFirebase() {
            return vaccForFirebaseVersionAccessors;
        }

        /**
         * Group of versions at <b>versions.liujingxing</b>
         */
        public LiujingxingVersionAccessors getLiujingxing() {
            return vaccForLiujingxingVersionAccessors;
        }

        /**
         * Group of versions at <b>versions.rikkax</b>
         */
        public RikkaxVersionAccessors getRikkax() {
            return vaccForRikkaxVersionAccessors;
        }

        /**
         * Group of versions at <b>versions.topjohnwu</b>
         */
        public TopjohnwuVersionAccessors getTopjohnwu() {
            return vaccForTopjohnwuVersionAccessors;
        }

    }

    public static class AndroidxVersionAccessors extends VersionFactory  {

        public AndroidxVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Version alias <b>androidx.navigation</b> with value <b>2.7.0</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getNavigation() { return getVersion("androidx.navigation"); }

        /**
         * Version alias <b>androidx.preference</b> with value <b>1.2.1</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getPreference() { return getVersion("androidx.preference"); }

    }

    public static class FirebaseVersionAccessors extends VersionFactory  {

        public FirebaseVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Version alias <b>firebase.bom</b> with value <b>32.3.0</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getBom() { return getVersion("firebase.bom"); }

    }

    public static class LiujingxingVersionAccessors extends VersionFactory  {

        public LiujingxingVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Version alias <b>liujingxing.rxhttp</b> with value <b>3.1.0</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getRxhttp() { return getVersion("liujingxing.rxhttp"); }

    }

    public static class RikkaxVersionAccessors extends VersionFactory  {

        public RikkaxVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Version alias <b>rikkax.material</b> with value <b>2.0.0</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getMaterial() { return getVersion("rikkax.material"); }

    }

    public static class TopjohnwuVersionAccessors extends VersionFactory  {

        public TopjohnwuVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Version alias <b>topjohnwu.libsu</b> with value <b>5.2.0</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getLibsu() { return getVersion("topjohnwu.libsu"); }

    }

    public static class BundleAccessors extends BundleFactory {

        public BundleAccessors(ObjectFactory objects, ProviderFactory providers, DefaultVersionCatalog config, ImmutableAttributesFactory attributesFactory, CapabilityNotationParser capabilityNotationParser) { super(objects, providers, config, attributesFactory, capabilityNotationParser); }

    }

    public static class PluginAccessors extends PluginFactory {
        private final AgpPluginAccessors paccForAgpPluginAccessors = new AgpPluginAccessors(providers, config);
        private final KotlinPluginAccessors paccForKotlinPluginAccessors = new KotlinPluginAccessors(providers, config);
        private final NavPluginAccessors paccForNavPluginAccessors = new NavPluginAccessors(providers, config);

        public PluginAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Plugin provider for <b>autoresconfig</b> with plugin id <b>io.github.turastory.auto-res-config</b> and
         * with version <b>0.0.5</b>
         * <p>
         * This plugin was declared in catalog libs.versions.toml
         */
        public Provider<PluginDependency> getAutoresconfig() { return createPlugin("autoresconfig"); }

        /**
         * Plugin provider for <b>gms</b> with plugin id <b>com.google.gms.google-services</b> and
         * with version <b>4.3.15</b>
         * <p>
         * This plugin was declared in catalog libs.versions.toml
         */
        public Provider<PluginDependency> getGms() { return createPlugin("gms"); }

        /**
         * Plugin provider for <b>ksp</b> with plugin id <b>com.google.devtools.ksp</b> and
         * with version <b>1.9.0-1.0.13</b>
         * <p>
         * This plugin was declared in catalog libs.versions.toml
         */
        public Provider<PluginDependency> getKsp() { return createPlugin("ksp"); }

        /**
         * Plugin provider for <b>materialthemebuilder</b> with plugin id <b>dev.rikka.tools.materialthemebuilder</b> and
         * with version <b>2.0.0</b>
         * <p>
         * This plugin was declared in catalog libs.versions.toml
         */
        public Provider<PluginDependency> getMaterialthemebuilder() { return createPlugin("materialthemebuilder"); }

        /**
         * Plugin provider for <b>refine</b> with plugin id <b>dev.rikka.tools.refine</b> and
         * with version <b>4.3.0</b>
         * <p>
         * This plugin was declared in catalog libs.versions.toml
         */
        public Provider<PluginDependency> getRefine() { return createPlugin("refine"); }

        /**
         * Group of plugins at <b>plugins.agp</b>
         */
        public AgpPluginAccessors getAgp() {
            return paccForAgpPluginAccessors;
        }

        /**
         * Group of plugins at <b>plugins.kotlin</b>
         */
        public KotlinPluginAccessors getKotlin() {
            return paccForKotlinPluginAccessors;
        }

        /**
         * Group of plugins at <b>plugins.nav</b>
         */
        public NavPluginAccessors getNav() {
            return paccForNavPluginAccessors;
        }

    }

    public static class AgpPluginAccessors extends PluginFactory {

        public AgpPluginAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Plugin provider for <b>agp.app</b> with plugin id <b>com.android.application</b> and
         * with version reference <b>agp</b>
         * <p>
         * This plugin was declared in catalog libs.versions.toml
         */
        public Provider<PluginDependency> getApp() { return createPlugin("agp.app"); }

        /**
         * Plugin provider for <b>agp.lib</b> with plugin id <b>com.android.library</b> and
         * with version reference <b>agp</b>
         * <p>
         * This plugin was declared in catalog libs.versions.toml
         */
        public Provider<PluginDependency> getLib() { return createPlugin("agp.lib"); }

    }

    public static class KotlinPluginAccessors extends PluginFactory  implements PluginNotationSupplier{

        public KotlinPluginAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Plugin provider for <b>kotlin</b> with plugin id <b>org.jetbrains.kotlin.android</b> and
         * with version reference <b>kotlin</b>
         * <p>
         * This plugin was declared in catalog libs.versions.toml
         */
        public Provider<PluginDependency> asProvider() { return createPlugin("kotlin"); }

        /**
         * Plugin provider for <b>kotlin.serialization</b> with plugin id <b>org.jetbrains.kotlin.plugin.serialization</b> and
         * with version reference <b>kotlin</b>
         * <p>
         * This plugin was declared in catalog libs.versions.toml
         */
        public Provider<PluginDependency> getSerialization() { return createPlugin("kotlin.serialization"); }

    }

    public static class NavPluginAccessors extends PluginFactory {
        private final NavSafeargsPluginAccessors paccForNavSafeargsPluginAccessors = new NavSafeargsPluginAccessors(providers, config);

        public NavPluginAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Group of plugins at <b>plugins.nav.safeargs</b>
         */
        public NavSafeargsPluginAccessors getSafeargs() {
            return paccForNavSafeargsPluginAccessors;
        }

    }

    public static class NavSafeargsPluginAccessors extends PluginFactory {

        public NavSafeargsPluginAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Plugin provider for <b>nav.safeargs.kotlin</b> with plugin id <b>androidx.navigation.safeargs.kotlin</b> and
         * with version reference <b>androidx.navigation</b>
         * <p>
         * This plugin was declared in catalog libs.versions.toml
         */
        public Provider<PluginDependency> getKotlin() { return createPlugin("nav.safeargs.kotlin"); }

    }

}
