package com.ghca.vm.internal;

public class AuroraException extends RuntimeException {

    // Default public constructor. This should only be used by AMF client.
    public AuroraException() {
    }

    private String section;
    private String errorId;

    public String getSection() {
        return section;
    }

    public String getErrorId() {
        return section + "." + errorId;
    } // XXX: fix after merging with AuroraError

    public String getSimpleErrorId() {
        return errorId;
    }

    // Bad bad code.  What use is a message template
    // once it is thrown outside of context/stack?
    // But we have tons of place calling this so here it is.
    public String getErrorMessageTemplate() {
        return toString();
    }

    /* Error messages read from a config file. */




    /**
     * In debug build, return the exception with the current stack trace stitched
     * in front of the original exception stack trace.
     *
     * In non-debug build, set the current stack trace.
     *
     * @param e
     * @return the original exception.
     */
    public static AuroraException stitchStackTraces(AuroraException e) {
        // replace stack trace with those of current thread
        e.fillInStackTrace();
        return e;
    }
}
