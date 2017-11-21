package analysis.runners;

import java.net.URLClassLoader;

public class IsolatingClassLoader extends URLClassLoader {

    public IsolatingClassLoader() {
        super(((URLClassLoader) Thread.currentThread().getContextClassLoader()).getURLs(), null);
    }
}
