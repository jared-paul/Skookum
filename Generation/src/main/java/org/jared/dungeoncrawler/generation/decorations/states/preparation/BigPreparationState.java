package org.jared.dungeoncrawler.generation.decorations.states.preparation;

import org.jared.dungeoncrawler.api.generation.IGenerationContext;
import org.jared.dungeoncrawler.api.generation.decorations.IDecoration;
import org.jared.dungeoncrawler.generation.decorations.DecorationLoader;

public class BigPreparationState extends PreparationState
{
    public BigPreparationState()
    {
        super(IDecoration.Size.BIG, 1);
    }

    @Override
    public void run(IGenerationContext context, DecorationLoader decorationLoader)
    {
        super.run(context, decorationLoader);

        decorationLoader.setState(new MediumPreparationState());
        decorationLoader.runState();
    }
}
