package org.jared.dungeoncrawler.game.states.preparation;

import org.bukkit.ChatColor;
import org.jared.dungeoncrawler.api.concurrency.Callback;
import org.jared.dungeoncrawler.api.game.IGameContext;
import org.jared.dungeoncrawler.api.game.State;
import org.jared.dungeoncrawler.api.plugin.DungeonCrawler;
import org.jared.dungeoncrawler.game.Countdown;
import org.jared.dungeoncrawler.game.states.StartState;

public class CountdownState implements State
{
    @Override
    public void run(IGameContext gameContext)
    {
        new Countdown(10, ChatColor.RED + "The game is about to begin!", gameContext.getPlayers(), new Callback<Boolean>()
        {
            @Override
            public void onSuccess(Boolean aBoolean)
            {
                gameContext.setState(new StartState());
                gameContext.runState();
            }

            @Override
            public void onFailure(Throwable cause)
            {

            }
        }).runTaskTimer(DungeonCrawler.getPlugin(), 20, 20);
    }
}
