package org.jared.dungeoncrawler.api.maps.wrappers;

import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.jared.dungeoncrawler.api.maps.ArrayImage;
import org.jared.dungeoncrawler.api.maps.controllers.MultiMapController;
import org.jared.dungeoncrawler.api.maps.util.MapImageUtil;
import org.jared.dungeoncrawler.api.maps.util.MatrixCallback;

import java.awt.image.BufferedImage;

public class MultiMapWrapper implements MapWrapper
{
    DefaultMapWrapper[][] mapWrappers;
    ArrayImage mainImage;
    BufferedImage mainBufferedImage;

    int rows;
    int columns;

    MultiMapController mapController = new MultiMapController()
    {
        @Override
        public void addViewer(Player player)
        {
            iterateMatrix(new MatrixCallback()
            {
                @Override
                public void call(MapWrapper mapWrapper)
                {
                    mapWrapper.getMapController().addViewer(player);
                }
            });
        }

        @Override
        public void removeViewer(Player player)
        {
            iterateMatrix(new MatrixCallback()
            {
                @Override
                public void call(MapWrapper mapWrapper)
                {
                    mapWrapper.getMapController().removeViewer(player);
                }
            });
        }

        @Override
        public void clearViewers()
        {
            iterateMatrix(new MatrixCallback()
            {
                @Override
                public void call(MapWrapper mapWrapper)
                {
                    mapWrapper.getMapController().clearViewers();
                }
            });
        }

        @Override
        public boolean isViewing(Player player)
        {
            for (int row = 0; row < mapWrappers.length; row++)
            {
                for (int column = 0; column < mapWrappers[row].length; column++)
                {
                    if (!mapWrappers[row][column].getMapController().isViewing(player))
                        return false;
                }
            }

            return true;
        }

        @Override
        public void update(BufferedImage image)
        {
            mainBufferedImage = MapImageUtil.resizeImage(image, mainBufferedImage.getWidth(), mainBufferedImage.getHeight());

            ArrayImage[][] splitImage = MapImageUtil.splitImage(mainBufferedImage, rows, columns);
            for (int row = 0; row < rows; row++)
            {
                for (int column = 0; column < columns; column++)
                {
                    mapWrappers[row][column].getMapController().update(splitImage[row][column]);
                }
            }
        }

        @Override
        public void update(ArrayImage image)
        {
            iterateMatrix(new MatrixCallback()
            {
                @Override
                public void call(MapWrapper mapWrapper)
                {
                    mapWrapper.getMapController().update(image);
                }
            });
        }

        @Override
        public ArrayImage getImage()
        {
            return mainImage;
        }

        @Override
        public void sendImage(Player player)
        {
            iterateMatrix(new MatrixCallback()
            {
                @Override
                public void call(MapWrapper mapWrapper)
                {
                    mapWrapper.getMapController().sendImage(player);
                }
            });
        }

        @Override
        public void showInFrames(Player player, ItemFrame[][] frameMatrix)
        {
            showInFrames(player, toItemFrameIDMatrix(frameMatrix));
        }

        @Override
        public void showInFrames(Player player, int[][] itemFrameIDMatrix)
        {
            for (int row = 0; row < mapWrappers.length; row++)
            {
                for (int column = 0; column < mapWrappers[row].length; column++)
                {
                    DefaultMapWrapper mapWrapper = mapWrappers[row][column];

                    mapWrapper.getMapController().showInFrame(player, itemFrameIDMatrix[row][column]);
                }
            }
        }

        @Override
        public void clearFrames(Player player, ItemFrame[][] frameMatrix)
        {

        }

        @Override
        public void clearFrames(Player player, int[][] itemFrameIDMatrix)
        {

        }
    };

    public MultiMapWrapper(BufferedImage mainBufferedImage, DefaultMapWrapper[][] mapWrappers, int rows, int columns)
    {
        this.mainBufferedImage = mainBufferedImage;
        this.mapWrappers = mapWrappers;
        this.rows = rows;
        this.columns = columns;
    }

    public BufferedImage getMainBufferedImage()
    {
        return mainBufferedImage;
    }

    public MultiMapController getMapController()
    {
        return mapController;
    }

    public void iterateMatrix(MatrixCallback callback)
    {
        for (int row = 0; row < mapWrappers.length; row++)
        {
            for (int column = 0; column < mapWrappers[row].length; column++)
            {
                callback.call(mapWrappers[row][column]);
            }
        }
    }

    public int[][] toItemFrameIDMatrix(ItemFrame[][] itemFrameMatrix)
    {
        //rectangle
        int[][] itemFrameIDMatrix = new int[itemFrameMatrix.length][itemFrameMatrix[0].length];

        for (int row = 0; row < itemFrameMatrix.length; row++)
        {
            for (int column = 0; column < itemFrameMatrix[row].length; column++)
            {
                itemFrameIDMatrix[row][column] = itemFrameMatrix[row][column].getEntityId();
            }
        }

        return itemFrameIDMatrix;
    }
}
