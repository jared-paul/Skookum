package org.jared.dungeoncrawler.generation.decorations.states.preparation;

import org.jared.dungeoncrawler.api.generation.IGenerationContext;
import org.jared.dungeoncrawler.api.generation.decorations.IDecoration;
import org.jared.dungeoncrawler.generation.decorations.DecorationLoader;

public class MediumPreparationState extends PreparationState
{
    public MediumPreparationState()
    {
        super(IDecoration.Size.MEDIUM, 3);
    }

    @Override
    public void run(IGenerationContext context, DecorationLoader decorationLoader)
    {
        super.run(context, decorationLoader);

        decorationLoader.setState(new SmallPreparationState());
        decorationLoader.runState();
    }
}
