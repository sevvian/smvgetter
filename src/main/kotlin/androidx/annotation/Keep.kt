package androidx.annotation

/**
 * A mock for the Android @Keep annotation.
 * This annotation is used to prevent code from being removed by optimizers
 * like Proguard. In our server environment, it has no effect, so we provide
 * an empty annotation class to satisfy the compiler.
 */
@Target(
    AnnotationTarget.CLASS,
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER,
    AnnotationTarget.FIELD,
    AnnotationTarget.CONSTRUCTOR
)
@Retention(AnnotationRetention.BINARY)
annotation class Keep