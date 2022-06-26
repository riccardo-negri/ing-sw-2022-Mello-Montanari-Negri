package it.polimi.ingsw.client.ui.cli.utils;

public class Regex {
    private Regex () {
        
    }

    public static final String MOVE_STUDENT = "^(move-student )(green|red|yellow|pink|blue)( to )(dining-room|((island-)([0-9]|1[0-1])))\s?$";
    public static final String MOVE_MOTHER_NATURE = "^(move-mother-nature steps )([1-7])\s?$";
    public static final String SELECT_CLOUD = "^(select-cloud )([0-3])\s?$";
    public static final String PLAY_ASSISTANT = "^(play-assistant )([1-9]|1[0])\s?$";
    public static final String USE_CHARACTER_1 = "^(use-character-1 move )(green|red|yellow|pink|blue)( to )(dining-room|((island-)([0-9]|1[0-1])))\s?$";
    public static final String USE_CHARACTER_2 = "^(use-character-2)\s?$";
    public static final String USE_CHARACTER_3 = "^(use-character-3 select )((island-)([0-9]|1[0-1]))\s?$";
    public static final String USE_CHARACTER_4 = "^(use-character-4)\s?$";
    public static final String USE_CHARACTER_5 = "^(use-character-5 select )((island-)([0-9]|1[0-1]))\s?$";
    public static final String USE_CHARACTER_6 = "^(use-character-6)\s?$";
    public static final String USE_CHARACTER_7 = "^(use-character-7 take )(green|red|yellow|pink|blue)( )(green|red|yellow|pink|blue|nothing)( )(green|red|yellow|pink|blue|nothing)( remove-from-entrance )(green|red|yellow|pink|blue)( )(green|red|yellow|pink|blue|nothing)( )(green|red|yellow|pink|blue|nothing)\s?$";
    public static final String USE_CHARACTER_8 = "^(use-character-8)\s?$";
    public static final String USE_CHARACTER_9 = "^(use-character-9 select )(green|red|yellow|pink|blue)\s?$";
    public static final String USE_CHARACTER_10 = "^(use-character-10 take-from-entrance )(green|red|yellow|pink|blue)( )(green|red|yellow|pink|blue|nothing)( exchange-with-from-dining )(green|red|yellow|pink|blue)( )(green|red|yellow|pink|blue|nothing)\s?$";
    public static final String USE_CHARACTER_11 = "^(use-character-11 take )(green|red|yellow|pink|blue)\s?$";
    public static final String USE_CHARACTER_12 = "^(use-character-12 select )(green|red|yellow|pink|blue)\s?$";
    public static final String HELPER_CHARACTER_START = "^(character-info character-";
    public static final String HELPER_CHARACTER_END = ")\s?$";
}
