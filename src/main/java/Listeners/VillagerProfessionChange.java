package Listeners;

import l10.dev.main.Seaseon2;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.VillagerCareerChangeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Merchant;
import org.bukkit.inventory.MerchantRecipe;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class VillagerProfessionChange implements Listener {

    Seaseon2 plugin = Seaseon2.getInstance();

    FileConfiguration config = plugin.getConfig();

    @EventHandler
    public void VillagerCareerChange(VillagerCareerChangeEvent e) {
        Villager vill = e.getEntity();
        Villager.Profession newProf = e.getProfession();

        if(newProf == Villager.Profession.NONE) return;

        vill.setProfession(newProf);
        vill.setRecipes(GetProfessionRecipes(newProf));
    }

    List<MerchantRecipe> GetProfessionRecipes(Villager.Profession prof){
        List<MerchantRecipe> recipes = new ArrayList<MerchantRecipe>(){};

        for (Map.Entry<String, Object> entry : config.getConfigurationSection("villager_trades."+prof.name()).getValues(false).entrySet()) {
            String PathForBaseStuff = "villager_trades."+prof.name()+"."+entry.getKey();

            ItemStack sellItem = config.getItemStack(PathForBaseStuff+".sell_item");
            int purchaseLimit = config.getInt(PathForBaseStuff+".purchase_limit");
            List<ItemStack> buyItem = new ArrayList<ItemStack>();

            for(Map.Entry<String, Object> entry2 : config.getConfigurationSection("villager_trades."+prof.name()+"."+entry.getKey()+".buy_items").getValues(false).entrySet()){
                String PathForBuyItems = "villager_trades."+prof.name()+"."+entry.getKey()+".buy_items."+entry2.getKey();
                buyItem.add(config.getItemStack(PathForBuyItems));
            }

            MerchantRecipe recipe = new MerchantRecipe(sellItem, purchaseLimit); //Selling, Max purchace limit
            for(int i = 0; i < buyItem.size(); i++){
                recipe.addIngredient(buyItem.get(i)); //What's required to buy the item
            }

            recipes.add(recipe);
        }

        return recipes;
    }
}
