package org.jared.dungeoncrawler.generation.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.LeavesDecayEvent;

public class LeafDecay implements Listener
{
    //TODO fix so it doesn't effect other plugins
    @EventHandler
    public void onLeafDecay(LeavesDecayEvent decayEvent)
    {
        decayEvent.setCancelled(true);
    }
}
