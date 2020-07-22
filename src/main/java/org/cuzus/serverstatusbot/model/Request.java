package org.cuzus.serverstatusbot.model;

public enum Request {
    /**
     * A challenge key, used internally in other requests.  You will not
     * need to use this.
     * 
     * @since 2007-11-10
     */
    CHALLENGE,
    
    /**
     * Very basic server information
     * 
     * @since 2007-11-18
     */
    BASIC,
    
    /**
     * General server information
     * 
     * @since 2007-11-10
     */
    INFORMATION,
    
    /**
     * The list of players and their scores
     * 
     * @since 2007-11-10
     */
    PLAYERS,
    
    /**
     * The list of server rules and cvars
     * 
     * @since 2007-11-10
     */
    RULES,
    
    /**
     * Don't request this, as all plugins are loaded automatically.  This
     * is only used in the Listener methods.
     * 
     * @since 2007-11-28
     */
    PLUGIN
};