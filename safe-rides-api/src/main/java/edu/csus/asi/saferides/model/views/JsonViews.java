package edu.csus.asi.saferides.model.views;

/**
 * Used for serializing Json based on role
 */
public class JsonViews {

    /**
     * Rider role
     */
    public interface Rider {
    }

    /**
     * Driver role
     */
    public interface Driver extends Rider {
    }

    /**
     * Coordinator role
     */
    public interface Coordinator extends Driver {
    }

}