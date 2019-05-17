package org.jared.dungeoncrawler.api.generation;

import org.bukkit.Location;
import org.bukkit.util.Vector;
import org.jared.dungeoncrawler.api.concurrency.Callback;
import org.jared.dungeoncrawler.api.data.Tuple;
import org.jared.dungeoncrawler.api.generation.block.IBlockPlaceTask;
import org.jared.dungeoncrawler.api.generation.block.IBlockPlacer;
import org.jared.dungeoncrawler.api.generation.block.IMaterialAndData;
import org.jared.dungeoncrawler.api.generation.cell.IHallway;
import org.jared.dungeoncrawler.api.generation.cell.IRoom;
import org.jared.dungeoncrawler.api.generation.delaunay.DT_Point;
import org.jared.dungeoncrawler.api.generation.delaunay.DelaunayTriangulation;
import org.jared.dungeoncrawler.api.generation.delaunay.DisjointSetForest;
import org.jared.dungeoncrawler.api.generation.delaunay.EdgeList;
import org.jared.dungeoncrawler.api.generation.maps.IMapContext;
import org.jared.dungeoncrawler.api.generation.util.XORShiftRandom;
import org.jared.dungeoncrawler.api.settings.DungeonSetting;
import org.jared.dungeoncrawler.api.settings.SettingObject;
import org.jbox2d.dynamics.World;

import java.util.List;
import java.util.Map;

public interface IGenerationContext
{
    void start(Callback<Boolean> isFinished);

    Callback<Boolean> isFinished();

    void setState(State state);

    State getState();

    void runState();

    IMapContext getMapContext();

    void setMapContext(IMapContext mapContext);

    World getPhysicsWorld();

    void setPhysicsWorld(World physicsWorld);

    List<Vector> getGate();

    void setGate(List<Vector> gate);

    DungeonMap getMap();

    void setMap(DungeonMap map);

    Vector getAreaCenter();

    Vector getRoomSize();

    int getRadius();

    int getMinRooms();

    int getRoomAreaThreshold();

    int getRoomCount();

    List<IRoom> getFillerCells();

    List<IRoom> getRooms();

    List<IRoom> getCellsMaster();

    List<IRoom> getIntersectingCells();

    DelaunayTriangulation getDelaunayTriangulation();

    void setDelaunayTriangulation(DelaunayTriangulation delaunayTriangulation);

    List<EdgeList.Edge> getMinTree();

    List<EdgeList.Edge> getEdges();

    void setEdges(List<EdgeList.Edge> edges);

    List<EdgeList.Edge> getDiscardedEdges();

    DisjointSetForest<DT_Point> getForest();

    void setForest(DisjointSetForest<DT_Point> forest);

    String getDungeonName();

    Map<DungeonSetting, SettingObject> getDungeonSettings();

    Location getBaseLocation();

    Vector getBaseVector();

    void setBaseVector(Vector vector);

    List<IHallway> getHallways();

    XORShiftRandom getRandom();

    IBlockPlacer getBlockPlacer();

    void drawImmediately(Vector vector, IMaterialAndData... materialData);

    void drawImmediately(List<Vector> vectors, IMaterialAndData... materialData);

    void drawImmediately(List<Tuple<Vector, IMaterialAndData>> data);

    void drawImmediately(Tuple<List<Vector>, IMaterialAndData> dataTuple);

    void drawInQueue(IBlockPlaceTask blockPlaceTask);
}
