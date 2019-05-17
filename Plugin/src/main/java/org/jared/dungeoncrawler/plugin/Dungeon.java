package org.jared.dungeoncrawler.plugin;

import org.jared.dungeoncrawler.api.IDungeon;
import org.jared.dungeoncrawler.api.game.IGameContext;
import org.jared.dungeoncrawler.api.generation.IGenerationContext;

public class Dungeon implements IDungeon
{
    private IGenerationContext generationContext;
    private IGameContext gameContext;

    public Dungeon(IGenerationContext generationContext, IGameContext gameContext)
    {
        this.generationContext = generationContext;
        this.gameContext = gameContext;
    }

    @Override
    public String getName()
    {
        return generationContext.getDungeonName();
    }

    @Override
    public IGenerationContext getGenerationContext()
    {
        return generationContext;
    }

    @Override
    public IGameContext getGameContext()
    {
        return gameContext;
    }
}
