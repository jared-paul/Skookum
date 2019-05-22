package org.jared.dungeoncrawler.generation;

import com.google.common.collect.Lists;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.util.Vector;
import org.jared.dungeoncrawler.api.concurrency.Callback;
import org.jared.dungeoncrawler.api.data.Tuple;
import org.jared.dungeoncrawler.api.generation.DungeonMap;
import org.jared.dungeoncrawler.api.generation.IGenerationContext;
import org.jared.dungeoncrawler.api.generation.State;
import org.jared.dungeoncrawler.api.generation.block.IBlockPlaceTask;
import org.jared.dungeoncrawler.api.generation.block.IBlockPlacer;
import org.jared.dungeoncrawler.api.generation.cell.IHallway;
import org.jared.dungeoncrawler.api.generation.cell.IRoom;
import org.jared.dungeoncrawler.api.generation.delaunay.DT_Point;
import org.jared.dungeoncrawler.api.generation.delaunay.DelaunayTriangulation;
import org.jared.dungeoncrawler.api.generation.delaunay.DisjointSetForest;
import org.jared.dungeoncrawler.api.generation.delaunay.EdgeList;
import org.jared.dungeoncrawler.api.generation.maps.IMapContext;
import org.jared.dungeoncrawler.api.generation.util.XORShiftRandom;
import org.jared.dungeoncrawler.api.material.IMaterialAndData;
import org.jared.dungeoncrawler.api.plugin.DungeonCrawler;
import org.jared.dungeoncrawler.api.settings.DungeonSetting;
import org.jared.dungeoncrawler.api.settings.SettingObject;
import org.jared.dungeoncrawler.api.util.RandomUtil;
import org.jared.dungeoncrawler.api.util.VectorUtil;
import org.jared.dungeoncrawler.generation.block.BlockPlacer;
import org.jared.dungeoncrawler.generation.states.InitializeState;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class GenerationContext implements IGenerationContext
{
    private IMapContext mapContext;
    private World physicsWorld;

    private Callback<Boolean> isFinished;
    private List<Vector> gate = Lists.newArrayList();

    private Map<DungeonSetting, SettingObject> dungeonSettings;
    private DungeonMap map;

    private IBlockPlacer BLOCK_PLACER;

    private Location base;
    private String dungeonName;

    private Vector areaSize = new Vector(256 + 128, 0, 192);
    private Vector areaCenter = new Vector(areaSize.getBlockX() >> 1, 0, areaSize.getBlockZ() >> 1);
    private Vector cellSize = new Vector(1, 0, 20);

    private int minRooms = 4;
    private int roomAreaThreshold = 42;

    private int cellCount = 15;
    private int radius = cellCount + 15;
    private List<IRoom> cells = Lists.newArrayList();
    private List<IRoom> fillerCells = Lists.newArrayList();
    private List<IRoom> cellsMaster = Lists.newArrayList();
    private List<IRoom> rooms = Lists.newArrayList();
    private List<IRoom> intersectingCells = Lists.newArrayList();

    private List<IHallway> hallways = Lists.newArrayList();

    private DelaunayTriangulation delaunayTriangulation;

    //minimum spanning tree
    private List<EdgeList.Edge> minTree = Lists.newArrayList();
    private List<EdgeList.Edge> edges = Lists.newArrayList();
    private List<EdgeList.Edge> discardedEdges = Lists.newArrayList();

    private DisjointSetForest<DT_Point> forest;

    private Vector baseVector;

    private XORShiftRandom random;

    private State state;

    public GenerationContext(Location base, String dungeonName)
    {
        this.physicsWorld = new World(new Vec2(0, 0));
        this.dungeonName = dungeonName;
        this.dungeonSettings = DungeonCrawler.getDungeonSettings().getDungeonSettings(dungeonName);
        this.base = base;
        this.BLOCK_PLACER = new BlockPlacer();
        Bukkit.getScheduler().runTaskTimer(DungeonCrawler.getPlugin(), this.BLOCK_PLACER, 0, 1);
    }

    @Override
    public void start(Callback<Boolean> isFinished)
    {
        this.isFinished = isFinished;
        setState(new InitializeState());
        runState();
    }

    @Override
    public Callback<Boolean> isFinished()
    {
        return isFinished;
    }

    @Override
    public void setState(State state)
    {
        this.state = state;
    }

    @Override
    public State getState()
    {
        return state;
    }

    @Override
    public void runState()
    {
        state.run(this);
    }

    @Override
    public IMapContext getMapContext()
    {
        return mapContext;
    }

    @Override
    public void setMapContext(IMapContext mapContext)
    {
        this.mapContext = mapContext;
    }

    @Override
    public World getPhysicsWorld()
    {
        return physicsWorld;
    }

    @Override
    public void setPhysicsWorld(World physicsWorld)
    {
        this.physicsWorld = physicsWorld;
    }

    @Override
    public List<Vector> getGate()
    {
        return gate;
    }

    @Override
    public void setGate(List<Vector> gate)
    {
        this.gate = gate;
    }

    @Override
    public DungeonMap getMap()
    {
        return map;
    }

    @Override
    public void setMap(DungeonMap map)
    {
        this.map = map;
    }

    @Override
    public Vector getAreaCenter()
    {
        return areaCenter;
    }

    @Override
    public Vector getRoomSize()
    {
        return cellSize;
    }

    @Override
    public int getRadius()
    {
        return radius;
    }

    @Override
    public int getMinRooms()
    {
        return minRooms;
    }

    @Override
    public int getRoomAreaThreshold()
    {
        return roomAreaThreshold;
    }

    @Override
    public int getRoomCount()
    {
        return cellCount;
    }

    @Override
    public List<IRoom> getFillerCells()
    {
        return fillerCells;
    }

    @Override
    public List<IRoom> getRooms()
    {
        return cells;
    }

    @Override
    public List<IRoom> getCellsMaster()
    {
        return cellsMaster;
    }

    @Override
    public List<IRoom> getIntersectingCells()
    {
        return intersectingCells;
    }

    @Override
    public DelaunayTriangulation getDelaunayTriangulation()
    {
        return delaunayTriangulation;
    }

    @Override
    public void setDelaunayTriangulation(DelaunayTriangulation delaunayTriangulation)
    {
        this.delaunayTriangulation = delaunayTriangulation;
    }

    @Override
    public List<EdgeList.Edge> getMinTree()
    {
        return minTree;
    }

    @Override
    public List<EdgeList.Edge> getEdges()
    {
        return edges;
    }

    @Override
    public void setEdges(List<EdgeList.Edge> edges)
    {
        this.edges = edges;
    }

    @Override
    public List<EdgeList.Edge> getDiscardedEdges()
    {
        return discardedEdges;
    }

    @Override
    public DisjointSetForest<DT_Point> getForest()
    {
        return forest;
    }

    @Override
    public void setForest(DisjointSetForest<DT_Point> forest)
    {
        this.forest = forest;
    }

    @Override
    public XORShiftRandom getRandom()
    {
        long seed = ThreadLocalRandom.current().nextLong();

        return new XORShiftRandom(seed);
    }

    @Override
    public IBlockPlacer getBlockPlacer()
    {
        return BLOCK_PLACER;
    }

    @Override
    public String getDungeonName()
    {
        return dungeonName;
    }

    @Override
    public Map<DungeonSetting, SettingObject> getDungeonSettings()
    {
        return dungeonSettings;
    }

    @Override
    public Location getBaseLocation()
    {
        return base;
    }

    @Override
    public Vector getBaseVector()
    {
        return baseVector;
    }

    @Override
    public void setBaseVector(Vector vector)
    {
        this.baseVector = vector;
    }

    @Override
    public void drawImmediately(Vector vector, IMaterialAndData... materialData)
    {
        drawImmediately(Lists.newArrayList(vector), materialData);
    }

    @Override
    public void drawImmediately(List<Vector> vectors, IMaterialAndData... materialData)
    {
        List<Tuple<Vector, IMaterialAndData>> data = Lists.newArrayList();

        for (Vector vector : vectors)
        {
            data.add(new Tuple<>(vector, (IMaterialAndData) RandomUtil.getRandomElementFromArray(materialData)));
        }

        drawImmediately(data);
    }

    @Override
    public void drawImmediately(List<Tuple<Vector, IMaterialAndData>> data)
    {
        for (Tuple<Vector, IMaterialAndData> dataTuple : data)
        {
            Vector vector = dataTuple.getA();
            Location location = VectorUtil.toLocation(vector, base);

            IMaterialAndData materialData = dataTuple.getB();

            Bukkit.getScheduler().runTask(DungeonCrawler.getPlugin(), () ->
                    DungeonCrawler.getBlockUtil().setBlockFast(location, materialData));

            Bukkit.getScheduler().runTask(DungeonCrawler.getPlugin(), () ->
                    location.getWorld().refreshChunk(location.getBlockX(), location.getBlockZ()));
        }
    }

    @Override
    public void drawImmediately(Tuple<List<Vector>, IMaterialAndData> dataTuple)
    {
        List<Vector> vectors = dataTuple.getA();
        List<Location> locations = VectorUtil.toLocations(vectors, base);

        IMaterialAndData data = dataTuple.getB();

        for (Location location : locations)
        {
            Bukkit.getScheduler().runTask(DungeonCrawler.getPlugin(), () ->
                    DungeonCrawler.getBlockUtil().setBlockFast(location, data));
        }

        Bukkit.getScheduler().runTask(DungeonCrawler.getPlugin(), () ->
                DungeonCrawler.getBlockUtil().refreshChunkLocations(locations));
    }

    @Override
    public void drawInQueue(IBlockPlaceTask blockPlaceTask)
    {
        BLOCK_PLACER.addTask(blockPlaceTask);
    }

    @Override
    public List<IHallway> getHallways()
    {
        return hallways;
    }
}
