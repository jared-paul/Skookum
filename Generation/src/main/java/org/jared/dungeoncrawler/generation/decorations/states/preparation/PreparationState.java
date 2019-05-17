package org.jared.dungeoncrawler.generation.decorations.states.preparation;

import org.jared.dungeoncrawler.api.generation.IGenerationContext;
import org.jared.dungeoncrawler.api.generation.cell.IRoom;
import org.jared.dungeoncrawler.api.generation.decorations.IDecoration;
import org.jared.dungeoncrawler.api.plugin.DungeonCrawler;
import org.jared.dungeoncrawler.generation.decorations.DecorationLoader;
import org.jared.dungeoncrawler.generation.decorations.states.IDecorationLoadingState;

public abstract class PreparationState implements IDecorationLoadingState
{
    private IDecoration.Size size;
    private int amount;

    public PreparationState(IDecoration.Size size, int amount)
    {
        this.size = size;
        this.amount = amount;
    }

    @Override
    public void run(IGenerationContext context, DecorationLoader decorationLoader)
    {
        for (IRoom cell : context.getRooms())
        {
            for (int i = 0; i <= amount; i++)
            {
                IDecoration decoration = DungeonCrawler.getDecorationRegistry().getRandomDecoration(cell, size);

                if (decoration != null)
                {
                    cell.getDecorations().add(decoration.clone());
                }
            }
        }
    }
}
