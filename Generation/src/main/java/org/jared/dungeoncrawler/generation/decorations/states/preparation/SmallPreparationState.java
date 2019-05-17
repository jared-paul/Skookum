package org.jared.dungeoncrawler.generation.decorations.states.preparation;

import org.jared.dungeoncrawler.api.generation.IGenerationContext;
import org.jared.dungeoncrawler.api.generation.decorations.IDecoration;
import org.jared.dungeoncrawler.generation.decorations.DecorationLoader;
import org.jared.dungeoncrawler.generation.decorations.states.LoadingState;

public class SmallPreparationState extends PreparationState
{
    public SmallPreparationState()
    {
        super(IDecoration.Size.SMALL, 5);
    }

    @Override
    public void run(IGenerationContext context, DecorationLoader decorationLoader)
    {
        super.run(context, decorationLoader);

        decorationLoader.setState(new LoadingState());
        decorationLoader.runState();
    }
}
