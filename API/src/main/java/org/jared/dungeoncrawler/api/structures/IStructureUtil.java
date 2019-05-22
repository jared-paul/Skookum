package org.jared.dungeoncrawler.api.structures;

import java.io.File;

public interface IStructureUtil
{
    IStructure loadStructure(File source) throws Exception;
}
