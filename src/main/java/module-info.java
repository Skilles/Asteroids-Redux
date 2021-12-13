/**
 * Provides the starter code for the <strong>cs1302-omega</strong> project.
 */
module cs1302.omega {
    requires transitive java.logging;
    requires transitive javafx.controls;
    requires transitive javafx.media;
    exports cs1302.game;
    exports cs1302.omega;
    exports cs1302.game.content;
    exports cs1302.game.content.sprites;
    exports cs1302.game.content.managers;
    exports cs1302.game.content.animations;
} // module
