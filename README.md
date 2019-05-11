# mapof
Data structure microframework for dynamically typed programming, inspired by Clojure.

Everything is a map!

## API
`map(key, val)` Creates a map

`search(root-map-object, path-varargs)` Finds a nested value by path within root object.

`set(root-map-object, value, MapUtils.replaceFn, path-varargs)` Replaces a nested value by path within root object. Creates empty map on its path if key is absent.

## Example
Suppose you decided to create a rpg game without OOP canser, so go ahead!

As a bonus, you'll get easy map-based data structure storage with MongoDB (prefer evolving schema).


Player 'class'

~~~~
var player = mapof(
                Player.name, "play 1",
                Player.exp, 10.0,
                Player.expMax, 100.0,
                Player.lvl, 5,
                Player.curStats, mapof(),
                Player.baseStats, mapof(
                        PlayerStats.hpMax, 100.0,
                        PlayerStats.pdef, 1.0,
                        PlayerStats.patk, 1.0
                ),
                Player.money, 3_000.0,
                Player.equip, mapof(
                        ItemSlot.head, itemRef.newInstance("helmet")
                ),
                Player.storage, mapof(
                        Storage.inventory, Lists.newArrayList(itemRef.newInstance("helmet"), itemRef.newInstance("gloves"))
                ),
                Player.location, mapof(
                        ZoneTrait._id, 1,
                        ZoneTrait.cell, CoordRef.of(0, 0)
                )
        );
~~~~


Function to drop item from inventory

~~~~
    public void drop(Object cardId, Object itemInstanceId) {
        var cardObj = cardRefService.get(cardId);

        var itemWidExisting = search(cardObj, Player.storage, Storage.inventory, itemInstanceId);
        if (itemWidExisting == null) {
            log.error("trying to drop not owned item={}", itemInstanceId);
            return;
        }

        set(card, Lists.newArrayList(),
                (prevSet, newSet) -> {
                    aslist(prevSet).remove(itemInstanceId);
                    return prevSet;
                }, Player.storage, Storage.inventory);
    }
~~~~
