/**
 * 
 */
package it.eurotn.panjea.rich.factory.navigationloader;

/**
 * @author fattazzo
 * 
 */
public class NavigationLoaderContext {

    public static final NavigationLoaderContext DEFAULT_CONTEXT = new NavigationLoaderContext("");

    private String name;

    /**
     * Costruttore.
     * 
     * @param name
     *            name
     */
    public NavigationLoaderContext(final String name) {
        super();
        this.name = name;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        NavigationLoaderContext other = (NavigationLoaderContext) obj;
        if (name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!name.equals(other.name)) {
            return false;
        }
        return true;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }
}
