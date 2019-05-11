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
                Card.name, "play 1",
                Card.exp, 10.0,
                Card.expMax, 100.0,
                Card.lvl, 5,
                Card.curStats, mapof(),
                Card.baseStats, mapof(
                        CardStats.hpMax, 100.0,
                        CardStats.pdef, 1.0,
                        CardStats.patk, 1.0
                ),
                Card.money, 3_000.0,
                Card.equip, mapof(
                        ItemSlot.head, itemRef.newInstance("helmet")
                ),
                Card.storage, mapof(
                        Storage.inventory, Lists.newArrayList(itemRef.newInstance("helmet"), itemRef.newInstance("gloves"))
                ),
                Card.location, mapof(
                        ZoneTrait._id, 1,
                        ZoneTrait.cell, CoordRef.of(0, 0)
                )
        );
~~~~


Function to drop item from inventory

~~~~
    public void drop(Object cardId, Object itemInstanceId) {
        var cardObj = cardRefService.get(cardId);
        if (isTrading(cardObj)) {
            log.error("Can't drop while trading");
            return;
        }

        var itemWidExisting = search(cardObj, Card.storage, Storage.inventory, itemInstanceId);
        if (itemWidExisting == null) {
            log.error("trying to drop not owned item={}", itemInstanceId);
            return;
        }

        set(card, Lists.newArrayList(),
                (prevSet, newSet) -> {
                    aslist(prevSet).remove(itemInstanceId);
                    return prevSet;
                }, Card.storage, Storage.inventory);
    }
~~~~
