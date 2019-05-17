package org.jared.dungeoncrawler.api.generation;

public interface State
{
    int order();

    void run(IGenerationContext generationContext);
}
