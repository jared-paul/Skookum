package org.jared.dungeoncrawler.generation.states;

import org.jared.dungeoncrawler.api.concurrency.Callback;
import org.jared.dungeoncrawler.api.generation.IGenerationContext;
import org.jared.dungeoncrawler.api.generation.State;
import org.jared.dungeoncrawler.api.plugin.DungeonCrawler;
import org.jared.dungeoncrawler.generation.decorations.DecorationLoader;

public class DecorationState implements State
{

    @Override
    public int order()
    {
        return 12;
    }

    @Override
    public void run(IGenerationContext generationContext)
    {
        DungeonCrawler.LOG.info("Loading Decorations...");

        //fills all the cells (rooms) with decorations
        DecorationLoader decorationLoader = new DecorationLoader(generationContext, new Callback<Boolean>()
        {
            @Override
            public void onSuccess(Boolean aBoolean)
            {
                generationContext.setState(new HiddenRoomState());
                generationContext.runState();
            }

            @Override
            public void onFailure(Throwable cause)
            {

            }
        });


        decorationLoader.runState();
    }
}
