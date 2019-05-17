package org.jared.dungeoncrawler.api.generation.decorations.registry;

import org.jared.dungeoncrawler.api.generation.cell.IRoom;
import org.jared.dungeoncrawler.api.generation.decorations.IDecoration;

import java.util.List;

public interface IDecorationRegistry
{
    List<IDecoration> getDecorations();

    IDecoration getDecoration(String name);

    IDecoration getRandomDecoration(IRoom cell, IDecoration.Size size);

    void loadDecorations() throws Exception;
}
