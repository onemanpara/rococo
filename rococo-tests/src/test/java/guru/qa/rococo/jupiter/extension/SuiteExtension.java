package guru.qa.rococo.jupiter.extension;

import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public interface SuiteExtension extends BeforeAllCallback {

    @Override
    default void beforeAll(ExtensionContext extensionContext) throws Exception {
        extensionContext.getRoot().getStore(ExtensionContext.Namespace.GLOBAL)
                .getOrComputeIfAbsent(this.getClass(), k -> {
                    beforeAllTests(extensionContext);
                    return new ExtensionContext.Store.CloseableResource() {
                        @Override
                        public void close() throws Throwable {
                            afterAllTests();
                        }
                    };
                });

    }

    default void beforeAllTests(ExtensionContext extensionContext) {
    }

    default void afterAllTests() {
    }
}