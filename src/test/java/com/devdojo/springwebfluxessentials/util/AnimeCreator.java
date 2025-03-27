package com.devdojo.springwebfluxessentials.util;

import com.devdojo.springwebfluxessentials.domain.Anime;

public class AnimeCreator {

    public static Anime createAnimeToBeSaved(){
        return Anime.builder()
                .name("Solo Leveling")
                .build();
    }

    public static Anime createValidAnime(){
        return Anime.builder()
                .id(1)
                .name("Solo Leveling")
                .build();
    }

    public static Anime createValidUpdatedAnime(){
        return Anime.builder()
                .id(1)
                .name("Solo Leveling")
                .build();
    }
}
