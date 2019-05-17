package org.jared.dungeoncrawler.generation.decorations.states;

import org.jared.dungeoncrawler.api.generation.IGenerationContext;
import org.jared.dungeoncrawler.generation.decorations.DecorationLoader;

public interface IDecorationLoadingState
{
    void run(IGenerationContext context, DecorationLoader decorationLoader);
}
