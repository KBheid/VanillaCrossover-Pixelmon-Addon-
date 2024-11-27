# Vanilla Crossover
## What is it?
In short, Vanilla Crossover is a mod that adds some 'vanilla' features to Pixelmon in the world. This mod aims to be highly configurable for the end user (you, the person using the mod!) It has some default functionality, which you can see below!

## Default Settings
If you install the mod and run it as-is out of the box, you'll have access to these features:

- Ability Effects
  - **Overgrow**: Pokemon that have this ability will double the growth stage of a crop when it successfully grows (at its natural rate). This applies to wild or owned Pokemon.
  - **Harvest**: Pokemon that have this ability will periodically harvest crops around them. By default, this applies only to owned Pokemon.
  - **Seed Sower**: Pokemon with this ability (ie. Arboliva) will plant seeds where they walk (do not need to pick any seeds up).
- Move Effects:
  - **Ally Switch**: Pokemon that know this move will swap positions with the player if they fall into lava. (3 minute cooldown)
- Status Effect Items
  - **Pufferfish**: Feeding a pufferfish to a Pokemon will cause it to have the Poisoned status effect. This can be useful for either a Pokemon with Guts or the Poison Heal ability.
  - **Blaze Powder**: Throwing this on a Pokemon will cause it to have the Burn status effect. This can be useful for Guts.
  - With default settings, these can only be applied to owned Pokemon.
- Pokemon-specific effects
  - **Spoink**: When right clicked, Spoink will launch the player in the air. The height of the bounce is determined by the Spoink's Special Attack IV.
    - The player will not receive fall damage upon landing.
  - **Trubbish & Garbadore**: These Pokemon will occasionally drop "garbage" items around them. Some of this is actually garbage - but some of it's useful! Slimeballs, netherite scrap (rarely), and food are all useful drops.
  - **Mudbray & Mudsdale**: These Pokemon can store items for you! In order to access this feature, you must first apply a barrel to them. These Pokemon have custom palettes with baskets that they'll receive! (Both normal and Shiny supported.)
    - These Pokemon have 5 different sizes for their inventory ranging from 9 to 104 slots of storage. The amount they have is influenced by:
      - *HP IVs*: If they have 16 or more HP IVs, this mon will have a +1 to its storage size.
      - *HP EVs*: If they have 126 or more HP EVs, this mon will have a +1 to its storage size.
      - *Dynamax*: If they have 5 or more Dynamax power, this mon will have a +1 to its storage size.
      - *Any of the above maxed*: This Pokemon will have a +1 to its storage size.
      - The base size is 1 and maxes at 5 (+1 for each of the above). The sizes are: 9, 18, 27, 54, 104. 

## Configuration
**TODO**

## Future Features (ordered by difficulty to implement):
- Torrent ability applies Dolphin's Grace periodically, re-implementing dolphin effect.
- Snorlax can act as a sleeping bag, allowing the player to sleep anywhere (thus healing all of their mons and turning night to day). Should not set their spawn point.
- Pokemon with the Seed Bomb move will pick up seeds/carrots/etc. near them and plant them nearby (if able).
- Sandshrew/Sandslash apply a large mining speed bonus to nearby players if the Pokemon is standing on Sand or Sandstone. (Haste beacon effect, maybe Haste 3 but testing needs to be done).
  - This effect will be configurable as "Apply *X* effect if *Y* mon is on *Z* block"
- Machoke/Machamp will apply a periodic boost to mining speed, haste effect (say a 30 second buff once every couple minutes, for reference but undecided).
  - This effect will be configurable as "*W* mon applies *X* effect every *Y* seconds for *Z* seconds"
- Blaze ability provides periodic fuel to nearby furnaces.
- Heatran will instant-smelt items in nearby furnaces occasionally.
- Pokemon with the Cheek Pouch ability will have a very small storage (9 slots).
- Some Pokemon that can be traded with to re-implement Piglin bartering.
- Cramorant will fish occasionally (vanilla fishing rewards, but configurable)
- Camerupt works as a (slow? but) mobile furnace with infinite fuel.
- ~Unified configuration - allow Species, Ability, and Move specifiers for each effect. Ie. no hard-coded requirements. (This might be priority)~
- ~Swap to yaml for config files, which should allow cleaner and more robust configuration (looking at you Periodic Drops! Also, pixelmon does this.)~
- Timburr evo line can chop trees. (Requires an AI framework for controlling Pixelmon movement).
- AI Framework for controlling Pokemon entities' movement/actions.
- The ability to leave Pokemon out while not in your party. (This will likely be a large task and may require the AI framework).
