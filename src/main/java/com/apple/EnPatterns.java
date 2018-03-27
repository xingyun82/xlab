package com.apple;

import java.util.regex.Pattern;

import static com.apple.GroupName.*;
import static com.apple.NameEntityTag.ARTIST_NAME_TAG;
import static com.apple.NameEntityTag.BEATS1_SHOW_NAME_TAG;

public class EnPatterns {

    private final static String REGEX_SOME = "(a|an|the|all|all the|some|something)";

    private final static String REGEX_NEWEST = "(?<" + GROUP_SORT_NEWEST + ">latest|newest|new)";
    private final static String REGEX_POPULAR = "(popular|top|hot)";

    private final static String REGEX_SHOW = "(radio|station|show)";
    private final static String REGEX_EPISODE = "episode";

    private final static String REGEX_BEATS1 = "beats (1|one)";
    private final static String REGEX_ON_BEATS1 = "on " + REGEX_BEATS1;

    private final static String REGEX_HOST_NAME = "(?<" + GROUP_HOST_NAME + ">.+(?<!'s))('s)";
    private final static String REGEX_FEATURING = "(with|featuring)";
    private final static String REGEX_WITH_GUEST = "(" + REGEX_FEATURING + " )(guest )?(?<" + GROUP_WITH_SOMEONE + ">.+(?<! " + REGEX_ON_BEATS1 + "))";
    private final static String REGEX_SHOW_EPISODE_OF = "(" + REGEX_SHOW + " )?" + "(" + REGEX_EPISODE + " )?" + "of";

    private final static String REGEX_SONG = "(?<" + GROUP_SONG_TYPE + ">song|songs)";
    private final static String REGEX_ALBUM = "(?<" + GROUP_ALBUM_TYPE + ">album|albums)";
    private final static String REGEX_MUSIC = "(" + REGEX_SONG + "|" + REGEX_ALBUM + "|music)";

    private final static String REGEX_PLAY_RADIO_SHOW = "(" + REGEX_SOME + " )?" + "(" + REGEX_NEWEST + " )?"
            + "(" + REGEX_SHOW_EPISODE_OF + " )?" + "(" + REGEX_HOST_NAME + " )?" + BEATS1_SHOW_NAME_TAG
            + "( " + REGEX_SHOW + ")?" + "( " + REGEX_WITH_GUEST + ")?" + "( " + REGEX_ON_BEATS1 + ")?";

    private final static String REGEX_PLAY_MUSIC_BY_ARTIST = "(" + REGEX_SOME + " )?" + "(" + REGEX_NEWEST + " )?"
            + "(" + REGEX_POPULAR + " )?" +  "(" + REGEX_MUSIC + " by )" + ARTIST_NAME_TAG;

    private final static String REGEX_PLAY_ARTIST_MUSIC = "(" + REGEX_SOME + " )?" + ARTIST_NAME_TAG + "('s)?" + "( " + REGEX_NEWEST + ")?"
            + "( " + REGEX_POPULAR + ")?" + "( " + REGEX_MUSIC + ")";

    private final static String REGEX_PLAY_DETAIL_MUSIC_BY_ARTIST = "(?<" + GROUP_MUSIC_NAME + ">.+(?<! by)) by " + ARTIST_NAME_TAG;


    public final static Pattern EN_PATTERN_PLAY_RADIO_SHOW = Pattern.compile("^" + REGEX_PLAY_RADIO_SHOW + "$");
    public final static Pattern EN_PATTERN_PLAY_ARTIST_MUSIC = Pattern.compile("^" + REGEX_PLAY_ARTIST_MUSIC + "$");
    public final static Pattern EN_PATTERN_PLAY_MUSIC_BY_ARTIST = Pattern.compile("^" + REGEX_PLAY_MUSIC_BY_ARTIST + "$");
    public final static Pattern EN_PATTERN_PLAY_DETAIL_MUSIC_BY_ARTIST = Pattern.compile("^" + REGEX_PLAY_DETAIL_MUSIC_BY_ARTIST + "$");

}
