package states;

/**
 * Shared context for Cucumber step definitions.
 * Uses a static field so multiple step def classes can share the same Context instance.
 */
public class SharedContext {
    public static Context context;
}
