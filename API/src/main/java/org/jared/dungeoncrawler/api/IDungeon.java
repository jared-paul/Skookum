package org.jared.dungeoncrawler.api;

import org.jared.dungeoncrawler.api.game.IGameContext;
import org.jared.dungeoncrawler.api.generation.IGenerationContext;

public interface IDungeon
{
    String getName();

    IGenerationContext getGenerationContext();

    IGameContext getGameContext();
}
