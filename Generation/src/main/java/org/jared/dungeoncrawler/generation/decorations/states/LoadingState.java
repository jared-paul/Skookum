package org.jared.dungeoncrawler.generation.decorations.states;

import com.google.common.collect.Lists;
import org.bukkit.util.Vector;
import org.jared.dungeoncrawler.api.concurrency.Callback;
import org.jared.dungeoncrawler.api.data.Tuple;
import org.jared.dungeoncrawler.api.generation.IGenerationContext;
import org.jared.dungeoncrawler.api.generation.block.IMaterialAndData;
import org.jared.dungeoncrawler.api.generation.cell.IRoom;
import org.jared.dungeoncrawler.api.generation.decorations.IDecoration;
import org.jared.dungeoncrawler.generation.decorations.DecorationLoader;

import java.util.List;
import java.util.ListIterator;

public class LoadingState implements IDecorationLoadingState
{
    public LoadingState()
    {
    }

    @Override
    public void run(IGenerationContext context, DecorationLoader decorationLoader)
    {
        /*
        String capitalizedSize = size.name().substring(0, 1).toUpperCase() + size.name().substring(1);
        DungeonCrawler.LOG.info("   Loading " + capitalizedSize + " Decorations...");
        */

        List<Tuple<Vector, IMaterialAndData>> blockData = Lists.newArrayList();

        for (int i = 0; i < context.getRooms().size(); i++)
        {
            int finalI = i;
            IRoom cell = context.getRooms().get(i);

            ListIterator<IDecoration> decorationIterator = cell.getDecorations().listIterator();
            while (decorationIterator.hasNext())
            {
                IDecoration decoration = decorationIterator.next();

                if (decoration.hasBeenPlaced())
                    continue;

                Vector suitablePosition = decorationLoader.getSuitablePosition(decoration, cell);
                if (suitablePosition == null)
                {
                    decorationIterator.remove();
                    continue;
                }

                decoration.setPlaced(true);

                decoration.place(context.getBaseLocation(), context.getBlockPlacer(), new Callback<Boolean>()
                {
                    @Override
                    public void onSuccess(Boolean aBoolean)
                    {
                        if (finalI == context.getRooms().size() - 1 && context.getBlockPlacer().getPlacingQueue().isEmpty())
                        {
                            decorationLoader.callback.onSuccess(true);
                        }
                    }

                    @Override
                    public void onFailure(Throwable cause)
                    {

                    }
                });

            }
        }
    }
}
