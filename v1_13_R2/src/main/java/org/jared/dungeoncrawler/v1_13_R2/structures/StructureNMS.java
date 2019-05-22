package org.jared.dungeoncrawler.v1_13_R2.structures;

import org.jared.dungeoncrawler.api.structures.AbstractStructure;
import org.jared.dungeoncrawler.api.structures.IStructureUtil;

import java.io.File;
import java.io.IOException;

public class StructureNMS implements IStructureUtil
{
    public AbstractStructure loadStructure(File file) throws IOException
    {
        return new Structure(file);
    }
}
