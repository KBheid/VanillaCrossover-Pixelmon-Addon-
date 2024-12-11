![title](https://github.com/user-attachments/assets/d1a2af1b-e610-44d2-b71a-b2f8fe51a3b0) 

## What is it?
In short, Vanilla Crossover is a mod that adds some 'vanilla' features to Pixelmon in the world. This mod aims to be highly configurable for the end user (you, the person using the mod!) It has some default functionality, which you can see below!

## Default Settings
If you install the mod and run it as-is out of the box, you'll have access to these features:

- Ability Effects
  - **Overgrow**: Pokemon that have this ability will double the growth stage of a crop when it successfully grows (at its natural rate). This applies to wild or owned Pokemon.
  - **Torrent**: Pokemon that have this ability will apply Dolphin's Grace periodically, re-implementing dolphin effect.
  - **Blaze**: Pokemon that have this ability will periodically provide fuel to nearby furnaces. (Range and duration based on Sp.Atk, cooldown based on Speed)
  - **Harvest**: Pokemon that have this ability will periodically harvest crops around them. By default, this applies only to owned Pokemon.
  - **Seed Sower**: Pokemon with this ability (ie. Arboliva) will plant seeds where they walk (do not need to pick any seeds up).
  - **Cheek Pouch**: Pokemon with this ability will have a small storage of 9 slots. When they hold an item, their cheeks will be a bit puffier.
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
  - Snorlax can act as a sleeping bag, allowing the player to sleep anywhere (thus healing all of their mons and turning night to day). Should not set their spawn point.
  - Pokemon with the Seed Bomb move will pick up seeds/carrots/etc. near them and plant them nearby (if able).
  - Sandshrew/Sandslash apply a large mining speed bonus to nearby players if the Pokemon is standing on Sand or Sandstone. (Haste beacon effect, maybe Haste 3 but testing needs to be done).
    - This effect will be configurable as "Apply *X* effect if *Y* mon is on *Z* block"
  - Machoke/Machamp will apply a periodic boost to mining speed, haste effect (say a 30 second buff once every couple minutes, for reference but undecided).
    - This effect will be configurable as "*W* mon applies *X* effect every *Y* seconds for *Z* seconds" 
  - Heatran will instant-smelt items in nearby furnaces occasionally.
  - Some Pokemon that can be traded with to re-implement Piglin bartering.
  - Add some cosmetic blocks.
  - When a battle completes, if the owned Pokemon cannot gain experience, drop XP orbs on the ground for the player. (e.g. level locked or level 100 pokemon)
  - Cramorant will fish occasionally (vanilla fishing rewards, but configurable)
  - Camerupt works as a (slow? but) mobile furnace with infinite fuel.
  - Multiple riders on a single Pokemon - like camels and boats.
  - When the player is flying (with elytra or on a pokemon) any flying Pokemon from their party that are out will fly in a V pattern with the player at the front. (Require AI framework)
  - Timburr evo line can chop trees. (Requires an AI framework for controlling Pixelmon movement).
  - Optionally enable old Pixelmon breeding behavior (best crossover was removed :( ) See the old way here: https://pixelmonmod.com/wiki/Breeding/Environment
  - AI Framework for controlling Pokemon entities' movement/actions.
  - The ability to leave Pokemon out while not in your party. (This will likely be a large task and may require the AI framework).

- Completed meta-tasks:
  - ~Unified Pokemon stat config~ (done)
  - ~Unified configuration - allow Species, Ability, and Move specifiers for each effect. Ie. no hard-coded requirements.~ (done)
  - ~Swap to yaml for config files, which should allow cleaner and more robust configuration~ (done)
