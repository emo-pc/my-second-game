# 🍄 Super Mario Bros. — 2D Tile-Based Platformer

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![StdDraw](https://img.shields.io/badge/Library-StdDraw-blue?style=for-the-badge)
![Status](https://img.shields.io/badge/Status-Completed-success?style=for-the-badge)

A 2D tile-based Super Mario platformer built from scratch in Java using object-oriented architecture and the **StdDraw** library. Developed for **Boğaziçi University CMPE 160 (Object-Oriented Programming)**.

The project features custom 4-directional boundary physics, dual-directional portal teleportation with crouch animations, dynamic camera follow/zoom modes, and level mechanics with custom enemy AI.

---
## 🎬 Gameplay Demo

🎥 **Watch Gameplay Video:** [YouTube Demo Linki](https://youtube.com/shorts/cvpB_91XZaA)

---
## 🎮 Features & Gameplay Dynamics

### 1. Physics & Collision Handling
- **4-Directional Collision Detection:** Independent boundary checks for ground (`isOnGround`), ceiling (`isOnCeiling`), left wall (`isWallOnLeft`), and right wall (`isWallOnRight`).
- **Gravity & Jump Mechanics:** Constant gravitational acceleration with initial jump velocity and ground teleportation alignment.

### 2. Portals & Level Progression
- **Dual Portal Teleportation:** Top-to-bottom immediate teleportation and bottom-to-top crouch-activated (`S` key) teleportation with state timers.
- **Coin Collection & Level Conditions:** Progression system requiring all coins to be collected in specific levels before opening the finish pipe.

### 3. Dynamic Camera & HUD
- **Camera Focus Mode (`C` key):** Dynamic viewport zoom centered around Mario ($200$-unit range) with boundary clamping to prevent out-of-bounds rendering.
- **Heads-Up Display (HUD):** Real-time tracking of current level, elapsed time, total death count, and level clues.

---

## 👾 Enemy AI & Custom Levels

| Enemy Type | Behavior & Mechanics |
| :--- | :--- |
| **Mushroom** | Oscillates horizontally within pre-defined boundaries ($x_1 \le x \le x_2$). |
| **Sneaky Hedgehog (Custom Enemy)** | **Proximity Tracking AI:** Detects Mario when inside its line of sight ($420 < x < 620 \land y > 619$) and dynamically changes direction to chase the player. |

---

## 🕹️ Controls

| Key | Action |
| :--- | :--- |
| `A / D` | Walk Left / Right |
| `W` | Jump |
| `S` | Crouch / Enter Bottom Portal |
| `C` | Toggle Dynamic Camera (Zoom In/Out) |
| `R` | Restart Game / Reset Level 1 Stats |
| `Space` | Start Game / Continue to Next Level |
| `← / →` | Adjust Game FPS in Main Menu |

---

## 🏗️ Architecture & Class Structure

The codebase is organized into a clean Object-Oriented structure:

```text
my-second-game/
├── Game.java    # Main orchestrator (Game loop, physics coordination, HUD, camera)
├── Map.java     # Manages terrain grids, collision validation, portals, and coins
├── Mario.java   # Player entity (Movement physics, state management, jump, death/respawn)
├── Enemy.java   # Enemy entities (Movement mechanics and pursuit AI)
└── Level.java   # Stores level data, coin locations, clues, and active entities
