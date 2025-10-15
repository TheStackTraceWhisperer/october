# Terminology

## Stateless Classes

| Terminology | Icon      | State     |
|-------------|-----------|-----------|
| `Utility`   | :toolbox: | Stateless |

## Framework Classes

| Terminology | Icon            | State     | Purpose                    |
|-------------|-----------------|-----------|----------------------------|
| `Bean`      | :beans:         | Mixed     |                            |
| `Service`   | :brain:         | Stateful  | @Singletons Classes        |
| `State`     | :world_map:     | Stateful  | Application State Classes  |
| `Event`     | :envelope:      | Immutable | @EventListener DTO Classes |

## Core Classes

| Terminology | Icon            | Managed By            | Purpose                    |
|-------------|-----------------|-----------------------|----------------------------|
| `Bean`      | :beans:         | Framework             |                            |
| `Service`   | :brain:         | Framework             | @Singletons Classes        |
| `State`     | :world_map:     | Framework             | Application State Classes  |
| `Utility`   | :toolbox:       | Static                | Stateless Utility Classes  |
| `Event`     | :envelope:      | Application/Framework | @EventListener DTO Classes |

### Data-Oriented Architecture Classes

| Terminology | Icon                  | Managed By | Purpose |
|-------------|-----------------------|------------|---------|
| `Entity`    | :identification_card: | ECS        |         |
| `Component` | :jigsaw:              | ECS        |         |
| `System`    | :gear:                | ECS        |         |


### 
:dart:
:warning:
:x:
:o:
:heavy_exclamation_mark:
:link:
:art:
:beans:
:blue_book:
:books:
:bookmark_tabs:
:book:
:brain:
:bulb:
:chess_pawn:
:bust_in_silhouette:
:bubbles:
:clipboard:
:closed_book:
:hammer_and_wrench:
:crossed_swords:
:dagger:
:credit_card:
:european_castle:
:floppy_disk:
:gear:
:headstone:
:identification_card:
:keyboard:
:ledger:
:carrot:
:computer:
:jar:
:memo:
:pencil:
:scroll:
:sparkles:
:sound:
:mag:
:envelope:
:package:
:joystick:
:zap:


| Terminology | Icon                  | Lifecycle | Purpose                    |
|-------------|-----------------------|-----------|----------------------------|
| `Bean`      | :beans:               | DI        |                            |
| `Service`   | :brain:               | DI        | @Singletons Classes        |
| `Utility`   | :toolbox:             | Static    | Stateless Utility Classes  |
| `Event`     | :envelope:            | Adhoc     | @EventListener DTO Classes |
| `State`     | :world_map:           | DI        | Application State Classes  |
| `Entity`    | :identification_card: | ECS       |                            |
| `Component` | :jigsaw:              | ECS       |                            |
| `System`    | :gear:                | ECS       |                            |