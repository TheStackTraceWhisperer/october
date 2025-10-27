# Sprite Sheet Support - Usage Guide

This document provides comprehensive examples of how to use the new sprite sheet, animation, and directional mapping features.

## Overview

The sprite sheet support includes:
- **SpriteSheetRegion**: Defines rectangular regions within sprite sheets
- **SpriteSheet**: Manages sprite sheet textures and regions
- **Direction**: Enum for 8-directional sprite orientation
- **SpriteAnimation**: Frame-based animation with timing control
- **AnimatedSpriteComponent**: Component for animated sprite entities
- **SpriteSheetLoader**: Utility to load configurations from YML files

## Quick Start

### 1. Create a Sprite Sheet YML File

Create a file in `src/main/resources/spritesheets/player.yml`:

```yaml
texture: "textures/player_spritesheet.png"

regions:
  idle_down:
    x: 0
    y: 0
    width: 32
    height: 32
  
  walk_down_0:
    x: 32
    y: 0
    width: 32
    height: 32
  walk_down_1:
    x: 64
    y: 0
    width: 32
    height: 32

animations:
  idle:
    frames: [idle_down]
    frameDuration: 0.1
    loop: true
  
  walk_down:
    frames: [walk_down_0, walk_down_1]
    frameDuration: 0.15
    loop: true

directionalAnimations:
  walk:
    DOWN: walk_down
    UP: walk_up
    LEFT: walk_left
    RIGHT: walk_right
```

### 2. Load the Sprite Sheet

```java
// Load sprite sheet from YML
SpriteSheet playerSheet = SpriteSheetLoader.loadSpriteSheet("spritesheets/player.yml");

// Load animations
Map<String, SpriteAnimation> animations = SpriteSheetLoader.loadAnimations("spritesheets/player.yml");

// Load directional mappings
Map<String, Map<String, String>> dirMappings = 
    SpriteSheetLoader.loadDirectionalAnimations("spritesheets/player.yml");
```

### 3. Use AnimatedSpriteComponent

```java
// Create an entity with animated sprite
Entity player = world.createEntity();

// Add animated sprite component
AnimatedSpriteComponent sprite = new AnimatedSpriteComponent(
    "player_sheet",  // Sprite sheet handle
    "walk_down"      // Initial animation
);

world.addComponent(player, sprite);
```

### 4. Update Animation State

```java
// Change animation
AnimatedSpriteComponent updated = sprite.withAnimation("walk_up");
world.updateComponent(player, updated);

// Change direction
updated = sprite.withDirection(Direction.LEFT);
world.updateComponent(player, updated);

// Advance animation time (typically done in update loop)
updated = sprite.withTimeAdvanced(deltaTime);
world.updateComponent(player, updated);

// Pause animation
updated = sprite.withPlaying(false);
world.updateComponent(player, updated);

// Reset animation to start
updated = sprite.withReset();
world.updateComponent(player, updated);
```

## Programmatic Usage (Without YML)

You can also create sprite sheets and animations programmatically:

```java
// Create sprite sheet
Texture texture = AssetLoaderUtility.loadTexture("textures/player.png");
SpriteSheet sheet = new SpriteSheet(texture);

// Add regions
sheet.addRegion("idle", new SpriteSheetRegion(0, 0, 32, 32))
     .addRegion("walk_1", new SpriteSheetRegion(32, 0, 32, 32))
     .addRegion("walk_2", new SpriteSheetRegion(64, 0, 32, 32));

// Create animation
SpriteAnimation walkAnim = new SpriteAnimation(
    "walk",
    Arrays.asList("walk_1", "walk_2"),
    0.15f,  // Frame duration
    true    // Loop
);

// Get current frame
int frameIndex = walkAnim.getFrameIndex(elapsedTime);
String frameName = walkAnim.getFrameName(frameIndex);
SpriteSheetRegion region = sheet.getRegion(frameName);
```

## Direction Conversion

Convert angles to directions for directional sprites:

```java
// Convert player movement angle to direction
float movementAngle = 45.0f;  // Northeast
Direction direction = Direction.fromAngle(movementAngle);
// Returns Direction.UP_RIGHT

// Update sprite to face that direction
AnimatedSpriteComponent updated = sprite.withDirection(direction);
```

## Animation System Integration

Example system to update animated sprites:

```java
@Singleton
public class AnimationSystem implements ISystem {
  
  @Override
  public void update(World world, float deltaTime) {
    world.getEntitiesWithComponents(AnimatedSpriteComponent.class)
         .forEach(entity -> {
           AnimatedSpriteComponent sprite = 
               world.getComponent(entity, AnimatedSpriteComponent.class);
           
           if (sprite.playing()) {
             // Advance animation time
             AnimatedSpriteComponent updated = sprite.withTimeAdvanced(deltaTime);
             world.updateComponent(entity, updated);
           }
         });
  }
}
```

## Rendering with Sprite Sheets

Example of how to render a specific frame from a sprite sheet:

```java
// Get the current animation frame
SpriteAnimation animation = animations.get(sprite.currentAnimation());
int frameIndex = animation.getFrameIndex(sprite.animationTime());
String frameName = animation.getFrameName(frameIndex);

// Get the region from the sprite sheet
SpriteSheetRegion region = spriteSheet.getRegion(frameName);

// Get normalized texture coordinates
Texture texture = spriteSheet.getTexture();
float[] coords = region.getNormalizedCoordinates(
    texture.getWidth(), 
    texture.getHeight()
);

// Use coords[0-3] (u_min, v_min, u_max, v_max) for rendering
```

## Best Practices

1. **Use YML for Configuration**: Keep sprite data in YML files for easier editing
2. **Group Related Animations**: Use directional animation mappings for character movement
3. **Immutable Components**: AnimatedSpriteComponent is immutable - always use `withX()` methods
4. **Cache Loaded Assets**: Load sprite sheets once and reuse them
5. **Frame Duration**: Typical values are 0.1-0.2 seconds per frame
6. **Non-looping Animations**: Use `loop: false` for one-shot animations like attacks

## YML Format Reference

```yaml
# Required: Path to sprite sheet texture
texture: "path/to/texture.png"

# Required: Define all sprite regions
regions:
  region_name:
    x: 0        # X coordinate in pixels
    y: 0        # Y coordinate in pixels
    width: 32   # Width in pixels
    height: 32  # Height in pixels

# Optional: Define animations
animations:
  animation_name:
    frames: [region1, region2, region3]  # List of region names
    frameDuration: 0.15                   # Seconds per frame
    loop: true                            # Loop animation?

# Optional: Map directions to animations
directionalAnimations:
  animation_group:
    DOWN: animation_down
    UP: animation_up
    LEFT: animation_left
    RIGHT: animation_right
    DOWN_RIGHT: animation_right  # Can reuse animations
    DOWN_LEFT: animation_left
    UP_RIGHT: animation_right
    UP_LEFT: animation_left
```

## Testing

All classes include comprehensive unit tests. To run:

```bash
mvn test -Dtest=SpriteSheetRegionTest,SpriteSheetTest,DirectionTest,SpriteAnimationTest,AnimatedSpriteComponentTest
```

## Future Enhancements

Potential additions:
- Animation state machine for complex transitions
- Sprite sheet packing tool
- Animation preview tool
- Support for texture atlases with padding/margins
- Blend modes for sprite overlays
- Per-frame callbacks for events
