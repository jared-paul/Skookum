package org.jared.dungeoncrawler.generation.decorations;

import com.google.common.collect.Lists;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.RegexFileFilter;
import org.jared.dungeoncrawler.api.generation.cell.IRoom;
import org.jared.dungeoncrawler.api.generation.decorations.IDecoration;
import org.jared.dungeoncrawler.api.generation.decorations.registry.IDecorationRegistry;
import org.jared.dungeoncrawler.api.plugin.DungeonCrawler;
import org.jared.dungeoncrawler.api.registry.DataRegistry;
import org.jared.dungeoncrawler.api.structures.IStructure;

import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class DecorationRegistry extends DataRegistry implements IDecorationRegistry
{
    private List<IDecoration> decorations = Lists.newArrayList();

    public DecorationRegistry(String folderName)
    {
        super(folderName);
    }

    @Override
    public List<IDecoration> getDecorations()
    {
        return decorations;
    }

    @Override
    public IDecoration getDecoration(String name)
    {
        for (IDecoration decoration : decorations)
        {
            if (name.equalsIgnoreCase(decoration.getName()))
            {
                return decoration;
            }
        }

        return null;
    }

    @Override
    public IDecoration getRandomDecoration(IRoom cell, IDecoration.Size size)
    {
        Collections.shuffle(decorations);

        IDecoration decoration = null;

        int index = 0;
        while (index < decorations.size())
        {
            decoration = decorations.get(index);

            if (decoration.getSize() == size && !cell.getDecorations().contains(decoration))
                break;
            else if (decoration.getSize() != size || cell.getDecorations().contains(decoration))
                decoration = null;

            index++;
        }

        return decoration;
    }

    @Override
    public void loadDecorations() throws Exception
    {
        Collection<File> files = FileUtils.listFiles(folder, new RegexFileFilter("^(.*?)"), DirectoryFileFilter.DIRECTORY);

        for (File decorationFile : files)
        {
            IStructure structure = DungeonCrawler.getStructureUtil().loadStructure(decorationFile);

            IDecoration.Size size = IDecoration.Size.fromVolume(structure.getVolume());
            IDecoration.Type type = IDecoration.Type.fromFile(structure.getFile());

            decorations.add(new Decoration(structure, size, type));
        }

        Collections.shuffle(decorations);
    }
}


