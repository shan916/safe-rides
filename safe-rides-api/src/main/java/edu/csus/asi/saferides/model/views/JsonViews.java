package edu.csus.asi.saferides.model.views;

/**
 * Used for serializing Json based on role
 */
public class JsonViews {

    public interface Rider {
    }

    public interface Driver extends Rider {
    }

    public interface Coordinator extends Driver {
    }

}